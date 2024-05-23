//package edu.zsc.ui.view;
//
//
//import edu.zsc.ElevatorConfig;
//import edu.zsc.control.ElevatorDetails;
//import edu.zsc.control.ElevatorManager;
//import edu.zsc.logic.Algorithm;
//import edu.zsc.ui.CustomCommand;
//import edu.zsc.ui.controller.ElevatorController;
//import edu.zsc.ui.data.DataOfElevatorState;
//import edu.zsc.ui.data.InputNumClass;
//
//import edu.zsc.ui.data.OutPutExcel;
//import edu.zsc.ui.run.DataToSystem;
//import edu.zsc.ui.run.RunElevator;
//
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.List;
//
///**
// * 控制面板数据的输入，接收任务的类
// */
//
//public class InputNum extends JFrame {
//    public static int time; //线程睡眠时间
//
//    public static int FLOOR_COUNT;// 电梯最高楼层
//    public static int ELEVATOR_COUNT;// 电梯的数量
//    public static int STAN_COUNT;// 负载人数
//    public static double SPEED;// 电梯运行的平均速度
//    public static double EACH_FLOOR_HEIGHT;//楼层高度
//    public static double STOP_TIME;// 平均停靠时间
//    public static Boolean GUI_show = false;
//
//    private JLabel JLFloorCount;
//    private JTextField JTFloorCount;//总楼层
//    private JLabel JLElevatorCount;
//    private JTextField JTElevatorCount;//电梯数量
//    private JLabel JLStanCount;
//    private JTextField JTStanCount;// 负载人数
//    private JLabel JLSpeed;
//    private JTextField JTSpeed;// 电梯运行的平均速度
//    private JLabel JLEachFloorHeight;
//    private JTextField JTEachFloorHeight;//楼层高度
//    private JLabel JLStopTime;
//    private JTextField JTStopTime;// 平均停靠时间
//
//    private JButton Determine;//确定按钮
//    private JButton Exit;//退出按钮
//    private ElevatorController controller;//
//    private InputNumClass inputNumClass;
//    private Algorithm.Callback callback;
//
//
//    public static Go g;
//
//    public InputNum() {
//        super();
//        inputNumClass = new InputNumClass();
//        inputNumClass.input = this;
//
//    }
//
//    /*面板信息输入*/
//    private void configureInput() {
//        this.setTitle(" Enter the number of elevators and floors");// 设置标题
//        this.setSize(500, 480);// 设置窗口大小
//        this.setResizable(false);// 设置为不能改变大小
//        this.setLocationRelativeTo(null);// 设置窗口在屏幕中心的位置
//        this.setFont(new Font("宋体", Font.PLAIN, 20));
//        this.setLayout(null);
//        this.getContentPane().setBackground(new Color(235, 235, 235));
//
//        this.JLFloorCount = new JLabel("电梯最高楼层：", SwingConstants.LEFT);
//        this.JLFloorCount.setBounds(50, 50, 150, 30);
//        this.JLFloorCount.setFont(new Font("宋体", Font.BOLD, 20));
//        this.add(this.JLFloorCount);
//
//        this.JTFloorCount = new JTextField(10);
//        this.JTFloorCount.setHorizontalAlignment(JTextField.CENTER);
//        this.JTFloorCount.setBounds(220, 50, 200, 30);
//        this.JTFloorCount.setFont(new Font("宋体", Font.BOLD, 20));
//        this.JTFloorCount.setToolTipText("楼层数不能超过60层！");
//        this.JTFloorCount.setText("10");
//        this.add(this.JTFloorCount);
//
//        this.JLElevatorCount = new JLabel("电梯的数量：", SwingConstants.LEFT);
//        this.JLElevatorCount.setBounds(50, 100, 150, 30);
//        this.JLElevatorCount.setFont(new Font("宋体", Font.BOLD, 20));
//        this.add(this.JLElevatorCount);
//
//        this.JTElevatorCount = new JTextField(10);
//        this.JTElevatorCount.setHorizontalAlignment(JTextField.CENTER);
//        this.JTElevatorCount.setBounds(220, 100, 200, 30);
//        this.JTElevatorCount.setFont(new Font("宋体", Font.BOLD, 20));
//        this.JTElevatorCount.setToolTipText("电梯数不能超过10部！");
//        this.JTElevatorCount.setText("4");
//        this.add(this.JTElevatorCount);
//
//        this.JLStanCount = new JLabel("负载人数：", SwingConstants.LEFT);
//        this.JLStanCount.setBounds(50, 150, 150, 30);
//        this.JLStanCount.setFont(new Font("宋体", Font.BOLD, 20));
//        this.add(this.JLStanCount);
//
//        this.JTStanCount = new JTextField(10);
//        this.JTStanCount.setHorizontalAlignment(JTextField.CENTER);
//        this.JTStanCount.setBounds(220, 150, 200, 30);
//        this.JTStanCount.setFont(new Font("宋体", Font.BOLD, 20));
//        this.JTStanCount.setText("10");
//        this.add(this.JTStanCount);
//
//        this.JLSpeed = new JLabel("平均速度：", SwingConstants.LEFT);
//        this.JLSpeed.setBounds(50, 200, 150, 30);
//        this.JLSpeed.setFont(new Font("宋体", Font.BOLD, 20));
//        this.add(this.JLSpeed);
//
//        this.JTSpeed = new JTextField(10);
//        this.JTSpeed.setHorizontalAlignment(JTextField.CENTER);
//        this.JTSpeed.setBounds(220, 200, 200, 30);
//        this.JTSpeed.setFont(new Font("宋体", Font.BOLD, 20));
//        this.JTSpeed.setText("2");
//        this.add(this.JTSpeed);
//
//        this.JLEachFloorHeight = new JLabel("楼层高度：", SwingConstants.LEFT);
//        this.JLEachFloorHeight.setBounds(50, 250, 150, 30);
//        this.JLEachFloorHeight.setFont(new Font("宋体", Font.BOLD, 20));
//        this.add(this.JLEachFloorHeight);
//
//        this.JTEachFloorHeight = new JTextField(10);
//        this.JTEachFloorHeight.setHorizontalAlignment(JTextField.CENTER);
//        this.JTEachFloorHeight.setBounds(220, 250, 200, 30);
//        this.JTEachFloorHeight.setFont(new Font("宋体", Font.BOLD, 20));
//        this.JTEachFloorHeight.setText("4");
//        this.add(this.JTEachFloorHeight);
//
//        this.JLStopTime = new JLabel("平均停靠时间：", SwingConstants.LEFT);
//        this.JLStopTime.setBounds(50, 300, 150, 30);
//        this.JLStopTime.setFont(new Font("宋体", Font.BOLD, 20));
//        this.add(this.JLStopTime);
//
//        this.JTStopTime = new JTextField(10);
//        this.JTStopTime.setHorizontalAlignment(JTextField.CENTER);
//        this.JTStopTime.setBounds(220, 300, 200, 30);
//        this.JTStopTime.setFont(new Font("宋体", Font.BOLD, 20));
//        this.JTStopTime.setText("4");
//        this.add(this.JTStopTime);
//
//        MouseListener numListener = new NumButtonAction();//鼠标监听器,监听事件
//        this.Determine = new JButton("确定");
//        this.Determine.setHorizontalAlignment(JButton.CENTER);
//        this.Determine.setBounds(60, 370, 100, 30);
//        this.Determine.setFont(new Font("宋体", Font.BOLD, 20));
//        this.Determine.addMouseListener(numListener);
//        this.add(this.Determine);
//
//        this.Exit = new JButton("退出");
//        this.Exit.setHorizontalAlignment(JButton.CENTER);
//        this.Exit.setBounds(340, 370, 100, 30);
//        this.Exit.setFont(new Font("宋体", Font.BOLD, 20));
//        this.Exit.addMouseListener(numListener);
//        this.add(this.Exit);
//
//        this.time = 100;//初始化线程时间
//    }
//
//    public void showFrame() {
//        this.configureInput();
//        this.setVisible(true);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置退出应用程序
//    }
//
//    public void colseFrame() {
//        this.setVisible(false);
//    }
//
//
//    public int getJTFloorCount() {
//        int Floor_num;
//        try {
//            Floor_num = Integer.parseInt(this.JTFloorCount.getText().trim());
//        } catch (Exception e) {
//            JOptionPane.showConfirmDialog(null, "请输入正确的数据格式：整数！", "提示", JOptionPane.WARNING_MESSAGE);
//            this.JTFloorCount.setText("");
//            return -1;
//        }
//        if (Floor_num < 1 || Floor_num > 60) {
//            JOptionPane.showConfirmDialog(null, "楼层数量应在1~60之间，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
//            return -1;
//        }
//        return Floor_num;
//    }
//
//    public int getJTElevatorCount() {
//        int Elevator_num;
//        try {
//            Elevator_num = Integer.parseInt(this.JTElevatorCount.getText().trim());
//        } catch (Exception e) {
//            JOptionPane.showConfirmDialog(null, "请输入正确的数据格式：整数！", "提示", JOptionPane.WARNING_MESSAGE);
//            this.JTElevatorCount.setText("");
//            return -1;
//        }
//        if (Elevator_num < 1 || Elevator_num > 10) {
//            JOptionPane.showConfirmDialog(null, "电梯数量应在1~10之间，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
//            return -1;
//        }
//        return Elevator_num;
//    }
//
//    public int getJTStanCount() {
//        int stanCount;
//        stanCount = Integer.parseInt(this.JTStanCount.getText().trim());
//        return stanCount;
//    }
//
//    public double getJTSpeed() {
//        double speed;
//        speed = Double.parseDouble(this.JTSpeed.getText().trim());
//        return speed;
//    }
//
//    public double getJTEachFloorHeight() {
//        double eachFloorHeight;
//        eachFloorHeight = Double.parseDouble(this.JTEachFloorHeight.getText().trim());
//        return eachFloorHeight;
//    }
//
//    public double getJTStopTime() {
//        double stopTime;
//        stopTime = Double.parseDouble(this.JTStopTime.getText().trim());
//        return stopTime;
//    }
//
//    //添加鼠标事件
//    class NumButtonAction extends MouseAdapter implements MouseListener {
//        //添加鼠标事件的时候。用适配器mouseAdapter可以不用实现所有的方法。只需要写自己需要的方法即可
//        public void mousePressed(MouseEvent e) { //鼠标被按下，一直不松手，不论停留在原处还是移动，此时触发的是 mousePressed 事件
//            if (e.getSource() == Determine) {
//                if (getJTFloorCount() == -1) return;
//                else FLOOR_COUNT = getJTFloorCount();//设置楼层数量
//
//                if (getJTElevatorCount() == -1) return;
//                else ELEVATOR_COUNT = getJTElevatorCount();//设置电梯数量
//
//                STAN_COUNT = getJTStanCount();//设置电梯负载人数
//                SPEED = getJTSpeed();//设置电梯平均运行速度
//                EACH_FLOOR_HEIGHT = getJTEachFloorHeight();//设置电梯的每层楼高度
//                STOP_TIME = getJTStopTime();//设置电梯平均停靠时间
//
//                controller = new ElevatorController();
//                colseFrame();
//
//                /**展示两个窗口*/
//                //controller.showStateView();
//                controller.showView();
//
//                /**创建电梯号输出表Excel*/
//                OutPutExcel.createExcel();
//                g = new Go();
//                g.start();
//
//            }
//            if (e.getSource() == Exit) {
//                if (JOptionPane.showConfirmDialog(null, "您确定要退出吗？", "提示", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//                    System.exit(0);
//                } else {
//                    return;
//                }
//            }
//        }
//    }
//
//    /**
//     * 该线程主要用于时时获取最新每一帧的数据
//     */
//    public class Go extends Thread {
//        private final Object lock = new Object();
//        private boolean pause = false;//判断线程是否暂停
//
//        Go() {
//            super();
//        }
//
//        //时时获取最新每一帧的数据
//        public void run() {
//            int callEleTime;//呼梯人请求的时间
//            int startOfCallEle;//呼梯人的起始楼层
//            int endOfCallEle;//呼梯人的目的楼层
//            int numOfCallEle;//呼梯的数量
//            double t = 0;//电梯当前的运行时间
//
//
//            /*用于计算电梯刷新次数*/
//            double time = 0.1;//电梯刷新的时间间隔
//            int prevTime = 0;//上一帧的时间
//
//            DataToSystem.getData();//得到呼梯请求的数据
//
//
//
//
//            //OutPutExcel.createExcel(); //创建电梯号输出表Excel
//            Algorithm algorithm = Algorithm.getInstance();
//
//            /**
//             * 初始化要安装该电梯的建筑物的相关参数
//             * int F = 10;//该建筑物的楼层数
//             * int N = 4;//该建筑物的要安装的电梯数量
//             * int H = 4;//该建筑物的每一层的楼层高度
//             */
//            algorithm.initBuilding(10,4,4);//初始化要安装改电梯建筑物的参数
//
//            byte[] doorPos ={1,1,1,1,1,1,1,1,1,1};
//            /**
//             * 一部电梯基础设置参数
//             * byte topFloor =  绝对楼层数 顶楼
//             * byte bottomFloor 绝对楼层数 底楼
//             * byte[] doorPos 楼层可用门
//             * byte lift 电梯号>=1
//             * int speed 额定梯速，speed /1000 = 0.000 m/s
//             * int time 平均停靠时间
//             * int people 荷载人数
//             */
//            algorithm.BasicSettingMessage(Byte.parseByte(String.valueOf(10)),Byte.parseByte(String.valueOf(0)),doorPos,Byte.parseByte(String.valueOf(1)),2,4,10);
//            algorithm.BasicSettingMessage(Byte.parseByte(String.valueOf(10)),Byte.parseByte(String.valueOf(0)),doorPos,Byte.parseByte(String.valueOf(2)),2,4,10);
//            algorithm.BasicSettingMessage(Byte.parseByte(String.valueOf(10)),Byte.parseByte(String.valueOf(0)),doorPos,Byte.parseByte(String.valueOf(3)),2,4,10);
//            algorithm.BasicSettingMessage(Byte.parseByte(String.valueOf(10)),Byte.parseByte(String.valueOf(0)),doorPos,Byte.parseByte(String.valueOf(4)),2,4,10);
//            /**
//             * 初始化群控系统
//             */
//            algorithm.initController();
//
//            //电梯上一帧的距地高度，用于判断是否正在开关门
//            double []preHeight = new double[ElevatorConfig.ELEVATOR_COUNT];
//
//            for (int i = 0; i < DataToSystem.num; i++) {
//                /*呼梯请求参数*/
//                callEleTime = DataToSystem.eleData[i][0];
//                startOfCallEle = DataToSystem.eleData[i][1];
//                endOfCallEle = DataToSystem.eleData[i][2];
//                numOfCallEle = DataToSystem.eleData[i][3];
//
//                int flushTime = (int) ((callEleTime - prevTime) * (1 / time));//刷新次数
//                for (int j = 0; j < flushTime; j++) {
//                    //判断线程是否暂停
//                    while (pause) onPause();
//                    eleSleep();
//                    t = t + 0.1;
//
//                    List<ElevatorDetails> details = ElevatorManager.getInstance().flushState();/**获取刷新后的数据*/
//
//                    //GUI获取数据实例化对象
//                    DataOfElevatorState D = new DataOfElevatorState();
//
//
//                    for (int k = 0; k < ElevatorConfig.ELEVATOR_COUNT; k++) {
//                        ElevatorDetails d = details.get(k);
//                        TransformData(D, d, k, t,preHeight);//向GUI传输数据
//                        d = null;
//                    }
//
//                    /*更新电梯GUI界面*/
//                    RunElevator R = new RunElevator(inputNumClass.input.controller.erd.e1, inputNumClass.input.controller.erd.e2, inputNumClass.input.controller.erd.goalFloor, D);
//                    R.startRun();
//
//                    /*存储上一帧的电梯距地高度*/
//                    for(int n = 0; n<ElevatorConfig.ELEVATOR_COUNT; n++){
//                        preHeight[n] = D.height[n];
//                    }
//
//
//                }
//
////                /*获取呼梯请求，并实例化呼梯请求对象*/
////
////
//////                CustomCommand customCommand = new CustomCommand();
//////                customCommand.setStartFloor((byte) startOfCallEle);
//////                customCommand.setTargetFloor((byte) endOfCallEle);
//////                customCommand.setNumber((byte) numOfCallEle);
////
////                /*发送呼梯请求*/
////                //algorithm.SetOutputMessage(customCommand);//发送请求并赋值OutputMessage
////                //algorithm.AddMyMission();//三维任务表的数据添加
//
//                /**
//                 *  startOfCallEle;	//起始楼层
//                 *  endOfCallEle;  //目标楼层
//                 *  numOfCallEle;  //乘客人数
//                 */
//                algorithm.getDataFromGUI((byte)startOfCallEle,(byte)endOfCallEle,(byte)numOfCallEle,new Algorithm.Callback() {
//                    @Override
//                    public void onCommandCompleted(int target, Byte startfloor, Byte targerfloor, Byte number) {
//                        System.out.println("回调的电梯号 = "+target);
//                        System.out.println("回调的起始楼层 = "+startfloor);
//                        System.out.println("回调的目标楼层 = "+targerfloor);
//                        System.out.println("回调的乘客人数 = "+number);
//                    }
//                });
//
//
//
//
//                /*电梯界面显示呼梯请求*/
////                System.out.println("algorithm.getTheBestElevatorNum() = "+algorithm.getTheBestElevatorNum());
////                if(algorithm.getTheBestElevatorNum() == -1) inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].addItem("电梯繁忙，请稍后再呼梯");
////                else if(algorithm.getTheBestElevatorNum() != -1)
////                {
////                    inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].addItem(((startOfCallEle) < 10 ? "0"
////                            + (startOfCallEle) : (startOfCallEle))  + "—>" + ((endOfCallEle) < 10 ? "0"
////                            + (endOfCallEle) : (endOfCallEle))
////                            + "        time: " +  callEleTime + "s"
////                            +"        电梯号: "+ numberToLetter(algorithm.getTheBestElevatorNum()));
////                }
////                else inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].addItem("电梯出现未知错误");
//
//
//                System.out.println("algorithm.getTheBestElevatorNum() = "+algorithm.getTheBestElevatorNum());
//                if(algorithm.getTheBestElevatorNum() == -1) inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].insertItemAt("电梯繁忙，请稍后再呼梯", 0);
//                else if(algorithm.getTheBestElevatorNum() != -1)
//                {
//                    inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].insertItemAt(
//                            ((startOfCallEle) < 10 ? "0" + (startOfCallEle) : (startOfCallEle))
//                                    + "—>"
//                                    + ((endOfCallEle) < 10 ? "0"
//                                    + (endOfCallEle) : (endOfCallEle))
//                                    + "        time: " +  callEleTime + "s"
//                                    +"        电梯号: "+ numberToLetter(algorithm.getTheBestElevatorNum()), 0);
//                }
//                else inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].insertItemAt("电梯出现未知错误", 0);
//                inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].setSelectedIndex (0);
//
//
//
//
//
//
//
//
//
//
//
//
//
//                prevTime = callEleTime;//赋值上一次的请求的时间
//
//                /**乘客对应电梯号写入Excel*/
//                OutPutExcel.writeExcel(callEleTime,startOfCallEle,endOfCallEle,numOfCallEle,algorithm.getTheBestElevatorNum());
//
//            }
//        }
//
//        /*向GUI传输数据*/
//        private void TransformData(DataOfElevatorState D, ElevatorDetails d, int k, double t,double []preHeight) {
//            D.timeOfCurrentEle = t;//GUI时间赋值
//
//            /**获取一帧的电梯数据：电梯的状态与数字的转化
//             *UP = 1;  DOWN = 2; STOP = 0;
//             */
//            if (d.getDirection().toString().equals("UP")) {
//                D.StateOfCurrentEle[k] = 1;
//            } else if (d.getDirection().toString().equals("DOWN")) {
//                D.StateOfCurrentEle[k] = 2;
//            } else if (d.getDirection().toString().equals("STOP")) {
//                D.StateOfCurrentEle[k] = 0;
//            } else System.out.println("电梯状态类型出错");
//
//            /**获取一帧的电梯数据：电梯的当前楼层*/
//            D.floorOfCurrentEle[k] = d.getFloor();
//            //System.out.println("d.getFloor() = "+d.getFloor());
//
//            /**获取一帧的电梯数据：电梯的当前楼层距地高度*/
//            D.height[k] = d.getHeight();
//
//            /**获取一帧的电梯数据：电梯任务列表*/
//            D.totalTaskInfoList.add(k, d.getTasks());
//
//            /**获取一帧的电梯数据：电梯上一帧的距地高度*/
//            D.preHeight[k] = preHeight[k];
//        }
//
//        //线程睡眠时间
//        public void eleSleep() {
//            try {
//                Thread.sleep(time);
//            } catch (Exception e) {
//                System.exit(0);//退出程序
//            }
//        }
//
//        //调用该方法实现线程的暂停
//        public void pauseThread() {
//            pause = true;
//        }
//
//        //调用该方法实现恢复线程的运行
//        public void resumeThread() {
//            pause = false;
//            synchronized (lock) {
//                lock.notify();
//            }
//        }
//
//        //暂停
//        void onPause() {
//            synchronized (lock) {
//                try {
//                    lock.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        //数字与字符串转换
//        private String numberToLetter(int num) {
//            if (num < 0) {
//                return null;
//            }
//
//            String letter = "";
//            num--;
//            do {
//                if (letter.length() > 0) {
//                    num--;
//                }
//                letter = ((char) (num % 26 + (int) 'B')) + letter;
//                num = (int) ((num - num % 26) / 26);
//            } while (num > 0);
//            return letter;
//        }
//    }
//}
//
//
