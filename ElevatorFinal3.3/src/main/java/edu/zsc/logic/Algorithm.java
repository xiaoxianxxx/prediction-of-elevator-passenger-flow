package edu.zsc.logic;


import edu.zsc.ElevatorConfig;
import edu.zsc.control.ElevatorProperty;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;


public class Algorithm {
    private static Algorithm INSTANT = new Algorithm();
    private Callback callback;
    private SendToControl send;

    private int[] myED; //电梯当前方向(0:无方向  1：向上   2：向下)
    private int[] myEF; //电梯当前楼层
    private int[] myLF; //电梯最近截梯楼层
    private int[][][] oneMission; //第一张三维任务列表（存放正常情况，优先级高）
    private int[][][] twoMission; //第二张三维任务列表（存放特殊情况）

    private int theBestElevatorNum;//最优电梯号


    /**
     * 算法核心成员属性
     */
    //楼层电梯固定属性
    private int F;//楼层数
    private int N;//电梯数
    private double H;//一楼层的高度
    private int[] bottomFloor;//每台电梯的最低楼层
    private int[] topFloor;//每台电梯的最高楼层
    private int[][] doorPos;    //每台电梯在每个楼层可用门情况：0表示不开门，1表示开门
    private int[] StanNum;//每台电梯的荷载人数
    private double[] v;//每台电梯的平均运行速度
    private double[] T;//每台电梯的平均停靠时间
    // 内测属性
    private static final double w1 = 0.6, w2 = 0.4, w3 = 0;//三个时间的权重系数

    //返回电梯数
    public int getN() {
        return this.N;
    }


    /**
     * 对外暴露的接口1
     * 初始化要安装该电梯的建筑物的相关参数
     *
     * @param F 该建筑物的楼层数
     * @param N 该建筑物的要安装的电梯数量
     * @param H 该建筑物的每一层的楼层高度
     */
    public void initBuilding(int F, int N, int H) {
        this.F = F;//该建筑物的楼层数
        this.N = N;//该建筑物的要安装的电梯数量
        this.H = H;//该建筑物的每一层的楼层高度

        //初始化UI界面参数
        ElevatorConfig.FLOOR_COUNT = F;
        ElevatorConfig.ELEVATOR_COUNT = N;
        ElevatorConfig.EACH_FLOOR_HEIGHT = H;

        //电梯相关参数初始化
        this.topFloor = new int[N];
        this.bottomFloor = new int[N];
        this.doorPos = new int[N][F];
        this.StanNum = new int[N];
        this.v = new double[N];
        this.T = new double[N];


        // 初始化三维任务列表
        this.oneMission = new int[N][F][4];
        this.twoMission = new int[N][F][4];
        this.myED = new int[N];
        this.myEF = new int[N];
        this.myLF = new int[N];
        this.prevMessage = new HashMap<Byte, InputMessage>();

    }

    /**
     * 对外暴露的接口2
     * 一部电梯基础设置参数
     * @param basicMessage
     */
    public void BasicSettingMessage(BasicMessage basicMessage) {
        int lift=basicMessage.lift;//电梯号
        this.topFloor[lift - 1] = basicMessage.topFloor - 1;//绝对楼层数 顶楼
        this.bottomFloor[lift - 1] = basicMessage.bottomFloor - 1;//绝对楼层数 底楼

        //楼层可用门
        for (int i = 0; i < basicMessage.doorPos.length; i++) {
            this.doorPos[lift - 1][i] = basicMessage.doorPos[i];
        }

        this.StanNum[lift - 1] = basicMessage.people;//荷载人数
        this.v[lift - 1] = basicMessage.speed;//额定梯速，speed /1000 = 0.000 m/s
        this.T[lift - 1] = basicMessage.time;//平均停靠时间
    }

//    /**
//     * 对外暴露的接口3
//     * 根据传进来的请求，调用算法
//     * @param customCommand
//     */
//    public void getDataFromGUI(CustomCommand customCommand) {
//        theBestElevatorNum = SetOutputMessage(customCommand);//三维任务表的接受面板输入
//        AddMyMission(customCommand);//oneMission和twoMission两张三维任务列表的添加
//    }

    /**
     * 对外暴露的接口4
     * 根据传进来的请求，调用算法，并且回调参数给面板
     * @param customCommand
     * @param callback
     */
    public void getDataFromGUI(CustomCommand customCommand, Callback callback) {
        try {
            //获取面板输入实例对象
            theBestElevatorNum = SetOutputMessage(customCommand);//接受面板输入，计算结果，发送给控制
            AddMyMission(customCommand);//oneMission和twoMission两张三维任务列表的添加

            OutputMessageToGUI outputMessageToGUI=new OutputMessageToGUI();
            outputMessageToGUI.target=theBestElevatorNum+1;
            outputMessageToGUI.startfloor=customCommand.getStartFloor();
            outputMessageToGUI.targerfloor=customCommand.getTargetFloor();
            outputMessageToGUI.number=customCommand.getNumber();
            //接口回调
            callback.onCommandCompleted(outputMessageToGUI);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化群控系统，仅用于此仿真系统
     */
    public void initController() {
        ElevatorProperty.SPEEDS = this.v;
        ElevatorProperty.STOP_TIMES = this.T;
    }

    /**
     * 根据乘客需求计算最优电梯，并发送给控制端
     * @param customCommand
     * @return
     */
    public int SetOutputMessage(CustomCommand customCommand) {

        //调用算法得到最优电梯号
        theBestElevatorNum = FindBestElevator((int) customCommand.getStartFloor() - 1, (int) customCommand.getTargetFloor() - 1, (int) customCommand.getNumber(), myED, myEF, myLF, oneMission, twoMission);

        if (theBestElevatorNum == -1)//满载的情况
        {
            System.out.println("SetOutputMessage:最优电梯号为-1");
        } else if (theBestElevatorNum != -1)//非满载的情况
        {
            //OutputMessage数据存储
            byte start = (byte) (customCommand.getStartFloor());//传给控制系统需要楼层数
            byte target = (byte) (customCommand.getTargetFloor());//传给控制系统需要楼层数
            byte elevatorNum = (byte) (theBestElevatorNum + 1);//传给控制系统需要电梯号+1

            OutputMessage outputMessage = new OutputMessage();
            outputMessage.setLift(elevatorNum);
            outputMessage.setCallFloor(start);
            outputMessage.setTargetFloor(target);
            send.sendToControl(outputMessage);
        }
        return theBestElevatorNum;

    }

    public static Algorithm getInstance() {
        return INSTANT;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * 回调面板的接口
     */
    public interface Callback {
        /**
         * 用于完成Command计算后的回调
         */
        void onCommandCompleted(OutputMessageToGUI outputMessageToGUI);
    }

    /**
     * 给控制发送任务的接口
     */
    @FunctionalInterface
    public interface SendToControl {
        void sendToControl(OutputMessage outputMessage);
    }

    public void setSend(SendToControl send) {
        this.send = send;
    }
    /**
     * 电梯基础参数的默认值设置
     */
    private Algorithm() {
        initBuilding(10, 4, 4);//默认值：最高楼层10楼,安装4部电梯，楼层高度为4
        BasicMessage basicMessage1 = new BasicMessage();
        BasicMessage basicMessage2 = new BasicMessage();
        BasicMessage basicMessage3 = new BasicMessage();
        BasicMessage basicMessage4 = new BasicMessage();
        byte[] doorPos = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};//楼层可用门
        basicMessage1.setTopFloor(Byte.parseByte(String.valueOf(10)));
        basicMessage1.setBottomFloor(Byte.parseByte(String.valueOf(1)));
        basicMessage1.setDoorPos(doorPos);
        basicMessage1.setLift(Byte.parseByte(String.valueOf(1)));
        basicMessage1.setSpeed(2);
        basicMessage1.setTime(4);
        basicMessage1.setPeople(10);
        basicMessage2.setTopFloor(Byte.parseByte(String.valueOf(10)));
        basicMessage2.setBottomFloor(Byte.parseByte(String.valueOf(1)));
        basicMessage2.setDoorPos(doorPos);
        basicMessage2.setLift(Byte.parseByte(String.valueOf(2)));
        basicMessage2.setSpeed(2);
        basicMessage2.setTime(4);
        basicMessage2.setPeople(10);
        basicMessage3.setTopFloor(Byte.parseByte(String.valueOf(10)));
        basicMessage3.setBottomFloor(Byte.parseByte(String.valueOf(1)));
        basicMessage3.setDoorPos(doorPos);
        basicMessage3.setLift(Byte.parseByte(String.valueOf(3)));
        basicMessage3.setSpeed(2);
        basicMessage3.setTime(4);
        basicMessage3.setPeople(10);
        basicMessage4.setTopFloor(Byte.parseByte(String.valueOf(10)));
        basicMessage4.setBottomFloor(Byte.parseByte(String.valueOf(1)));
        basicMessage4.setDoorPos(doorPos);
        basicMessage4.setLift(Byte.parseByte(String.valueOf(4)));
        basicMessage4.setSpeed(2);
        basicMessage4.setTime(4);
        basicMessage4.setPeople(10);
        BasicSettingMessage(basicMessage1);//第一部电梯参数设置
        BasicSettingMessage(basicMessage2);//第二部电梯参数设置
        BasicSettingMessage(basicMessage3);//第三部电梯参数设置
        BasicSettingMessage(basicMessage4);//第四部电梯参数设置
        initController();//初始化群控系统
    }


    /**
     * oneMission和twoMission两张三维任务列表的添加
     */
    public void AddMyMission(CustomCommand customCommand) {
        if (theBestElevatorNum == -1)//满载的情况
        {
            System.out.println("AddMyMission:最优电梯号为-1");
        } else if (theBestElevatorNum != -1)//非满载的情况
        {
            int targetFloor = customCommand.getTargetFloor() - 1;//目的楼层
            int startFloor = customCommand.getStartFloor() - 1;//起始楼层
            int num = customCommand.getNumber();//人数
            //todo
            if ((myED[theBestElevatorNum] == 1 && customCommand.getTargetFloor() > customCommand.getStartFloor() && startFloor < myEF[theBestElevatorNum])
                    || (myED[theBestElevatorNum] == 1 && customCommand.getTargetFloor() > customCommand.getStartFloor() && startFloor >= myEF[theBestElevatorNum] && myLF[theBestElevatorNum] > startFloor))//特殊情况1
            {
                addMissionData(targetFloor, startFloor, num, twoMission);//三维任务列表数据的添加
            } else if (myED[theBestElevatorNum] == 2 && customCommand.getTargetFloor() < customCommand.getStartFloor() && startFloor > myEF[theBestElevatorNum]
                    || (myED[theBestElevatorNum] == 2 && customCommand.getTargetFloor() < customCommand.getStartFloor() && startFloor <= myEF[theBestElevatorNum] && myLF[theBestElevatorNum] < startFloor))//特殊情况2
            {
                addMissionData(targetFloor, startFloor, num, twoMission);//三维任务列表数据的添加
            } else {
                addMissionData(targetFloor, startFloor, num, oneMission);//三维任务列表数据的添加
            }

        }
    }

    /**
     * 三维任务列表数据的添加
     */
    private void addMissionData(int targetFloor, int startFloor, int num, int[][][] Mission) {
        //todo
        if (targetFloor > startFloor) {
            Mission[theBestElevatorNum][startFloor][0] += num;
            Mission[theBestElevatorNum][targetFloor][2] += num;
        } else if (targetFloor < startFloor) {
            Mission[theBestElevatorNum][startFloor][1] += num;
            Mission[theBestElevatorNum][targetFloor][3] += num;
        }
    }

//    /**
//     * 输出所有电梯的任务列表
//     */
//    private void ShowMission(int[][][] Mission) {
//        for (int i = 0; i < ElevatorConfig.ELEVATOR_COUNT; i++) {
//            System.out.println("=======第 " + i + " 号电梯任务列表=======");
//            for (int j = this.F - 1; j >= 0; j--) {
//                for (int k = 0; k < 4; k++) {
//                    System.out.print(Mission[i][j][k] + "       ");
//                }
//                System.out.println("\n");
//            }
//
//            System.out.println("\n");
//        }
//    }

    /**
     * key : 各电梯号
     * value ： 电梯号对应的电梯的上一次状态
     */
    private Map<Byte, InputMessage> prevMessage;

    /**
     * 当电梯状态发生改变的时候被调用
     *
     * @param inputMessage 电梯状态值
     */
    public void handleElevatorStateChange(InputMessage inputMessage) {
        byte eleNumByte = inputMessage.getLift(); //电梯号，Byte类型
        int eleNumInt = eleNumByte - 1; //电梯号，int类型
        InputMessage prevInput = prevMessage.get(eleNumByte); //相对应的上一次电梯状态
        if (prevInput == null) {
            prevMessage.put(eleNumByte, inputMessage);
            return;
        }
        this.myED[eleNumInt] = inputMessage.getDirection(); //电梯方向
        this.myEF[eleNumInt] = inputMessage.getLevel() - 1; //电梯楼层
        this.myLF[eleNumInt] = inputMessage.getLimitFloor() - 1; //电梯最近截梯楼层

        changeMyMission(eleNumInt, inputMessage, prevInput);

        prevMessage.put(eleNumByte, inputMessage); //覆盖电梯的上一次状态
    }

    /**
     * 变化三维任务列表（由1变为0时改变）
     *
     * @param eleNumInt
     * @param inputMessage
     * @param prevInput
     */
    private void changeMyMission(int eleNumInt, InputMessage inputMessage, InputMessage prevInput) {
        int[] NowTarget = new int[F];
        int[] NowUpFloor = new int[F];
        int[] NowDownFloor = new int[F];
        int[] prevTarget = new int[F]; //电梯上一次登记的目的任务楼层
        int[] prevUpFloor = new int[F]; //电梯上一次登记的呼梯任务楼层（向上）
        int[] prevDownFloor = new int[F]; //电梯上一次登记的呼梯任务楼层（向下）
        NowTarget = getStatus(inputMessage.getTarget());
        NowUpFloor = getStatus(inputMessage.getUpFloor());
        NowDownFloor = getStatus(inputMessage.getDownFloor());
        prevTarget = getStatus(prevInput.getTarget());
        prevUpFloor = getStatus(prevInput.getUpFloor());
        prevDownFloor = getStatus(prevInput.getDownFloor());
        //todo
        for (int i = 0; i < F; i++) {
            if (NowUpFloor[i] != prevUpFloor[i] && prevUpFloor[i] == 1 && NowUpFloor[i] == 0) {
                oneMission[eleNumInt][i][0] = 0;
            }
            if (NowDownFloor[i] != prevDownFloor[i] && prevDownFloor[i] == 1 && NowDownFloor[i] == 0) {
                oneMission[eleNumInt][i][1] = 0;
            }
            if (NowTarget[i] != prevTarget[i] && prevTarget[i] == 1 && NowTarget[i] == 0) {
                if (prevInput.getDirection() == 1) {
                    oneMission[eleNumInt][i][2] = 0;
                }
                if (prevInput.getDirection() == 2) {
                    oneMission[eleNumInt][i][3] = 0;
                }
            }
        }
        // 当电梯方向变化时，将特殊情况的第二张表 twoMission 赋给第一张表 oneMission

        if ((prevInput.getDirection() == 1 && inputMessage.getDirection() == 2) || (prevInput.getDirection() == 2 && inputMessage.getDirection() == 1)) {
            for (int i = 0; i < F; i++) {
                for (int j = 0; j < 4; j++) {
                    if (twoMission[eleNumInt][i][j] > 0) {
                        oneMission[eleNumInt][i][j] = twoMission[eleNumInt][i][j];
                    }
                    twoMission[eleNumInt][i][j] = 0;
                }
            }
        }
    }

    /**
     * 将状态byte类型数组转化为int类型数组
     * @param status
     * @return
     */
    private int[] getStatus(byte[] status) {
        //todo
        int[] temp = new int[F];
        BitSet bitSet = new BitSet(status.length * 8);
        int index = 0;
        for (int i = 0; i < status.length; i++) {
            for (int j = 0; j < 8; j++) {
                bitSet.set(index++, (status[i] & (1 << j)) >> j == 1 ? true : false);
            }
        }
        for (int k = 0; k < F; k++) {
            if (bitSet.get(k)) {
                temp[k] = 1;
            }
        }
        return temp;
    }

    /**
     * 返回最优电梯号
     */
    public int getTheBestElevatorNum() {
        return theBestElevatorNum;
    }


    /**
     * 核心算法层
     * Sf:乘客起始
     * Df:乘客目的
     * Num：同行人数
     * ED： 电梯方向
     * EF： 电梯楼层
     * LF： 电梯截梯楼层
     */
    //根据乘客信息和电梯状态计算最优梯号
    public int FindBestElevator(int Sf, int Df, int Num, int[] ED, int[] EF, int[] LF, int[][][] Mission, int[][][] nextMission) {
        System.out.println("**************************开始***********************");
        System.out.println("SF = " + Sf);
        System.out.println("DF = " + Df);
        System.out.println("Num = " + Num);
        for (int i = 0; i < N; i++) {
            System.out.println("ED[" + i + "] = " + ED[i]);
        }
        for (int i = 0; i < N; i++) {
            System.out.println("EF[" + i + "] = " + EF[i]);
        }
        for (int i = 0; i < N; i++) {
            System.out.println("LF[" + i + "] = " + LF[i]);
        }
        //System.out.println("表1");
        //ShowMission(Mission);
        //System.out.println("表2");
        //ShowMission(nextMission);
        double[] Score = new double[N];
        for (int i = 0; i < N; i++)//初始化评分数组
        {
            Score[i] = 999;
        }
        int PD = FindDirectionForPassenger(Sf, Df);// 乘客乘梯方向
        double Twait = 0;//等待时间
        double Tride = 0;//乘梯时间
        int Ed;//电梯方向
        int Ef;//电梯所在楼层
        int Lf;//电梯最近截梯楼层
        int i = 0;//从第一个电梯开始计算评分
        int flag = 0;//用于判断是否所有电梯都无法接该乘客（超载或不在服务楼层）
        int goal = -1;//评分最高的电梯 若为-1则全部电梯超载
        int countNum;//存放呼梯到目的的最大累积人数

        for (; i < N; i++) {//为每一台电梯计算评分
            if (Sf < bottomFloor[i] || Sf > topFloor[i] || Df < bottomFloor[i] || Df > topFloor[i] || doorPos[i][Sf] == 0 || doorPos[i][Df] == 0)
                continue;//乘客的呼梯需求不在该部电梯可服务的楼层内
            countNum = CalCountNumber(i, PD, Sf, Df, Mission);//计算本部电梯在呼梯和目的之间的最大累积人数
            Ed = ED[i];//本部电梯方向 0静止 1向上 2向下 3静止向上 4静止向下
            Ef = EF[i];//本部电梯所在楼层
            Lf = LF[i];//本部电梯的截梯楼层
            if (Ed == 0)//电梯无任务静止
            {
                if (countNum + Num > StanNum[i]) continue;//预判的本趟最大累积人数+本任务人数大于额定人数，则直接排除该电梯
                Twait = abs(Ef - Sf) * H / v[i];
                Tride = abs(Sf - Df) * H / v[i];
            } else if ((Ed == 1 && PD == 1 && Ef == Sf && Lf == Sf) || (Ed == 2 && PD == 2 && Ef == Sf && Lf == Sf))//电梯正在上下乘客 电梯与乘客楼层和方向均相同
            {
                if (countNum + Num > StanNum[i]) continue;
                Twait = 0;//无等待时间
                Tride = abs(Sf - Df) * H / v[i] + FindBetweenInArray1(i, Sf, Df, Mission) * T[i];
                //乘梯时间为电梯运行时间，和电梯在服务该乘客时中途开关门时间
            }
//乘客方向向上
            else if (PD == 1 && Ed == 1 && Ef < Sf && Sf >= Lf && Sf < FindtopFloor(i, Ef, Mission))//乘客方向向上 电梯方向向上 电梯楼层小于呼梯楼层 乘客楼层不小于截梯楼层 可以截梯
            {
                if (countNum + Num > StanNum[i]) continue;
                Twait = abs(Ef - Sf) * H / v[i] + FindBetweenInArray3(i, Ef, Sf, Mission) * T[i];
                Tride = abs(Sf - Df) * H / v[i] + FindBetweenInArray1(i, Sf, Df, Mission) * T[i];
            } else if ((PD == 1 && Ed == 1 && Ef <= Sf && Sf < Lf) || (PD == 1 && Ed == 1 && Ef > Sf) || (PD == 1 && Ed == 1 && Sf >= FindtopFloor(i, Ef, Mission)))
            //乘客方向向上 电梯方向向上 电梯楼层不大于呼梯楼层 乘客楼层小于截梯楼层 不可以截梯 //乘客方向向上 电梯方向向上 电梯楼层大于呼梯楼层
            // 乘客方向向上 电梯方向向上 呼梯楼层不小于本趟任务的最高楼层
            {
                int nextCountNum = CalCountNumber(i, PD, Sf, Df, nextMission);//计算下一趟任务本部电梯在呼梯和目的之间的最大累积人数
                if (nextCountNum + Num > StanNum[i]) continue;//预判的下趟最大累积人数+本任务人数大于额定人数，则直接排除该电梯
                int Max = FindtopFloor(i, Ef, Mission);//本趟任务的最高楼层
                int Min = min(FindbottomFloor1(i, Max, Mission), FindbottomFloor(i, topFloor[i], nextMission));//本趟任务的最低楼层与下趟任务的最低楼层的最小者
                Twait = (abs(Ef - Max) + abs(Max - Min) + abs(Min - Sf)) * H / v[i] + (FindBetweenInArray3(i, Ef, Max, Mission) +
                        FindBetweenInArray3(i, Max, Min, Mission) + FindBetweenInArray3(i, Min, Sf, nextMission)) * T[i];
                Tride = abs(Sf - Df) * H / v[i] + FindBetweenInArray1(i, Sf, Df, nextMission) * T[i];
            } else if (PD == 1 && Ed == 2)//乘客方向向上 电梯方向向下
            {
                if (countNum + Num > StanNum[i]) continue;
                int Min = FindbottomFloor(i, Ef, Mission);
                Twait = (abs(Ef - Min) + abs(Min - Sf)) * H / v[i] + (FindBetweenInArray3(i, Ef, Min, Mission) + FindBetweenInArray3(i, Min, Sf, Mission)) * T[i];
                Tride = abs(Sf - Df) * H / v[i] + FindBetweenInArray1(i, Sf, Df, Mission) * T[i];
            }

//乘客方向向下
            else if (PD == 2 && Ed == 2 && Ef > Sf && Sf <= Lf && Sf > FindbottomFloor(i, Ef, Mission))//乘客方向向下 电梯方向向下 电梯楼层大于呼梯楼层 乘客楼层不大于截梯楼层 可以截梯
            {
                if (countNum + Num > StanNum[i]) continue;
                Twait = abs(Ef - Sf) * H / v[i] + FindBetweenInArray3(i, Ef, Sf, Mission) * T[i];
                Tride = abs(Sf - Df) * H / v[i] + FindBetweenInArray1(i, Sf, Df, Mission) * T[i];
            } else if ((PD == 2 && Ed == 2 && Ef >= Sf && Sf > Lf) || (PD == 2 && Ed == 2 && Ef < Sf) || (PD == 2 && Ed == 2 && Sf <= FindbottomFloor(i, Ef, Mission)))
            //乘客方向向下 电梯方向向下 电梯楼层不小于呼梯楼层 乘客楼层大于截梯楼层 不可以截梯//乘客方向向下 电梯方向向下 电梯楼层小于呼梯楼层
            // 乘客方向向下 电梯方向向下 呼梯楼层不大于本趟任务的最低楼层
            {
                int nextCountNum = CalCountNumber(i, PD, Sf, Df, nextMission);
                if (nextCountNum + Num > StanNum[i]) continue;
                int Min = FindbottomFloor(i, Ef, Mission);
                int Max = max(FindtopFloor1(i, Min, Mission), FindtopFloor(i, bottomFloor[i], nextMission));
                Twait = (abs(Ef - Min) + abs(Min - Max) + abs(Max - Sf)) * H / v[i] + (FindBetweenInArray3(i, Ef, Min, Mission) +
                        FindBetweenInArray3(i, Min, Max, Mission) + FindBetweenInArray3(i, Max, Sf, nextMission)) * T[i];
                Tride = abs(Sf - Df) * H / v[i] + FindBetweenInArray1(i, Sf, Df, nextMission) * T[i];
            } else if (PD == 2 && Ed == 1)//乘客方向向下 电梯方向向上
            {
                if (countNum + Num > StanNum[i]) continue;
                int Max = FindtopFloor(i, Ef, Mission);
                Twait = (abs(Ef - Max) + abs(Max - Sf)) * H / v[i] + (FindBetweenInArray3(i, Ef, Max, Mission) + FindBetweenInArray3(i, Max, Sf, Mission)) * T[i];
                Tride = abs(Sf - Df) * H / v[i] + FindBetweenInArray1(i, Sf, Df, Mission) * T[i];
            }
            Score[i] = w1 * Twait + w2 * Tride;//总评分为乘客等待时间x等待时间权重+乘客乘梯时间x乘梯时间权重
            System.out.println("Twait : " + Twait);
            System.out.println("Tride : " + Tride);
            System.out.println(Score[i]);
            flag = 1;//表示至少一台电梯可以去接该乘客
        }
        if (flag == 1) {
            goal = FindMinScoreNumber(Score);
        }

        System.out.println("goal = " + goal);
        return goal;
    }

    private static int abs(int i) {
        return Math.abs(i);
    }//求绝对值

    private static int min(int x, int y) {//返回最小值
        if (x <= y) return x;
        else return y;
    }

    private static int max(int x, int y) {//返回最大值
        if (x >= y) return x;
        else return y;
    }

    private int FindMinScoreNumber(double[] Score) {//返回评分数组的最小值
        int Min = 0;
        for (int i = 1; i < N; i++) {
            if (Score[i] < Score[Min]) {
                Min = i;
            }
        }
        return Min;
    }

    //根据乘客起始楼层和目的楼层判断方向 1向上 2向下
    private int FindDirectionForPassenger(int PassengerCurrentFloor, int PassengerDestinationFloor) {
        int direction;
        if (PassengerCurrentFloor - PassengerDestinationFloor < 0) {//起始小于目的
            direction = 1;
        } else {//起始大于目的
            direction = 2;
        }
        return direction;
    }

    //根据任务列表计算每部电梯在此任务的楼层间（起始到目的楼层）的最大累积人数
    private int CalCountNumber(int i, int PD, int FC, int FD, int[][][] Mission) {
        int MaxNum = 0;//用于存放呼梯楼层到目的楼层的最大累积人数
        if (PD == 1) {//乘客方向向上
            int[] Num = new int[FD];//存放0到目的楼层之间 每层楼的累积人数
            Num[0] = Mission[i][0][0];
            for (int k = 1; k < FD; k++)//根据任务列表计算从第一层到乘客目的楼层，每一层关门时电梯的人数
            {
                Num[k] = Num[k - 1] + Mission[i][k][0] - Mission[i][k][2];//每层楼的累积人数为上一层楼的累积人数+本层楼进入人数-本层楼出去人数
            }
            for (int k = FC; k < FD; k++)//找出呼梯楼层到目的楼层的最大人数
            {
                if (Num[k] > MaxNum) {
                    MaxNum = Num[k];
                }
            }

        } else {//乘客方向向下，同理向上
            int[] Num = new int[F];//存放最高楼层到目的楼层之间 每层楼的累积人数
            Num[F - 1] = Mission[i][F - 1][1];
            for (int k = F - 2; k > FD; k--) {
                Num[k] = Num[k + 1] + Mission[i][k][1] - Mission[i][k][3];
            }
            for (int k = FC; k > FD; k--) {
                if (Num[k] > MaxNum) {
                    MaxNum = Num[k];
                }
            }
        }
        return MaxNum;
    }

    private int FindtopFloor(int i, int floor, int[][][] Mission) {//寻找整张任务列表的最大楼层
        int Max = floor;//从电梯所在位置开始向上寻找
        for (int j = floor; j < F; j++) {
            if (Mission[i][j][0] != 0 || Mission[i][j][2] != 0 || Mission[i][j][1] != 0 || Mission[i][j][3] != 0) {
                //只要该任务列表中，一个楼层有任意停靠任务，则视为该电梯需要到达的楼层
                if (j > Max) {
                    Max = j;
                }
            }
        }
        return Max;
    }

    private int FindtopFloor1(int i, int floor, int[][][] Mission) {//寻找向上任务的最高楼层
        int Max = floor;//从电梯所在位置开始向上寻找
        for (int j = floor; j < F; j++) {
            if (Mission[i][j][0] != 0 || Mission[i][j][2] != 0) {
                //只要该任务列表中，一个楼层有向上的任意停靠任务，则视为该电梯需要到达的楼层
                if (j > Max) {
                    Max = j;
                }
            }
        }
        return Max;
    }

    private int FindbottomFloor(int i, int floor, int[][][] Mission) {//寻找整张任务列表的最小楼层
        int Min = floor;//从电梯所在楼层往下开始寻找
        for (int j = floor - 1; j >= 0; j--) {
            if (Mission[i][j][1] != 0 || Mission[i][j][3] != 0 || Mission[i][j][0] != 0 || Mission[i][j][2] != 0) {
                //只要该任务列表中，一个楼层有任意停靠任务，则视为该电梯需要到达的楼层
                if (j < Min) {
                    Min = j;
                }
            }
        }
        return Min;
    }

    private int FindbottomFloor1(int i, int floor, int[][][] Mission) {//寻找向下任务的最小楼层
        int Min = floor;
        for (int j = floor - 1; j >= 0; j--) {
            if (Mission[i][j][1] != 0 || Mission[i][j][3] != 0) {
                //只要该任务列表中，一个楼层有向下的任意停靠任务，则视为该电梯需要到达的楼层
                if (j < Min) {
                    Min = j;
                }
            }
        }
        return Min;
    }

    //统计在floor1与floor2之间的停靠次数，不包括floor1与floor2
    private int FindBetweenInArray1(int i, int floor1, int floor2, int[][][] Mission) {
        int direction = floor1 - floor2;
        int stop_floor = 0;
        if (direction < 0) {//向上
            for (int j = floor1 + 1; j < floor2; j++) {
                if (Mission[i][j][0] != 0 || Mission[i][j][2] != 0) {
                    stop_floor++;
                }
            }

        } else {//向下

            for (int j = floor2 + 1; j < floor1; j++) {
                if (Mission[i][j][1] != 0 || Mission[i][j][3] != 0) {
                    stop_floor++;
                }
            }
        }
        return stop_floor;
    }

    //统计在floor1与floor2之间的停靠次数，不包括floor2
    private int FindBetweenInArray3(int i, int floor1, int floor2, int[][][] FloorCount) {//左闭右开
        int direction = floor1 - floor2;
        int stop_floor = 0;
        if (direction < 0) {//向上
            for (int j = floor1; j < floor2; j++) {
                if (FloorCount[i][j][0] != 0 || FloorCount[i][j][2] != 0) {
                    stop_floor++;
                }
            }
        } else {//向下
            for (int j = floor2 + 1; j <= floor1; j++) {
                if (FloorCount[i][j][1] != 0 || FloorCount[i][j][3] != 0) {
                    stop_floor++;
                }
            }
        }
        return stop_floor;
    }

    //统计在floor1与floor2之间的停靠次数，不包括floor1
    private int FindBetweenInArray2(int i, int floor1, int floor2, int[][][] FloorCount) {//左开右闭
        int direction = floor1 - floor2;
        int stop_floor = 0;
        if (direction < 0) {//向上
            for (int j = floor1 + 1; j <= floor2; j++) {
                if (FloorCount[i][j][0] != 0 || FloorCount[i][j][2] != 0) {
                    stop_floor++;
                }
            }

        } else {//向下

            for (int j = floor2; j < floor1; j++) {
                if (FloorCount[i][j][1] != 0 || FloorCount[i][j][3] != 0) {
                    stop_floor++;
                }
            }
        }
        return stop_floor;
    }
}
