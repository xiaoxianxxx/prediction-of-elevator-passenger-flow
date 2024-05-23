package edu.zsc.ui.view;


import edu.zsc.ElevatorConfig;
import edu.zsc.control.ElevatorDetails;
import edu.zsc.control.ElevatorManager;
import edu.zsc.control.ElevatorTaskSummary;
import edu.zsc.logic.Algorithm;
import edu.zsc.logic.CustomCommand;
import edu.zsc.logic.OutputMessageToGUI;
import edu.zsc.ui.controller.ElevatorController;
import edu.zsc.ui.data.DataOfElevatorState;
import edu.zsc.ui.data.InputNumClass;

import edu.zsc.ui.data.OutPutExcel;
import edu.zsc.ui.run.DataToSystem;
import edu.zsc.ui.run.RunElevator;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制面板数据的输入，接收任务的类
 */

public class InputNum extends JFrame {
    public static int time=100; //线程睡眠时间
    private ElevatorController controller;
    private InputNumClass inputNumClass;
    private static Algorithm algorithm;
    public static Go g;
    public CustomCommand customCommand;

    public InputNum() {
        super();
        inputNumClass = new InputNumClass();
        inputNumClass.input = this;
    }

    //接口调用处
    public void showFrame(Algorithm algorithm) {
        this.algorithm = algorithm;

        /**创建输出表Excel*/
        OutPutExcel.createExcel();

        controller = new ElevatorController();
        controller.showView();
        g = new Go();
        g.start();
    }


    /**
     * 该线程主要用于时事获取最新每一帧的数据
     */
    public class Go extends Thread {
        private final Object lock = new Object();
        private boolean pause = false;//判断线程是否暂停

        Go() {
            super();
        }

        //时事获取最新每一帧的数据
        public void run() {
            int callEleTime;//呼梯人请求的时间
            int startOfCallEle;//呼梯人的起始楼层
            int endOfCallEle;//呼梯人的目的楼层
            int numOfCallEle;//呼梯的数量
            double t = 0;//电梯当前的运行时间

            /*用于计算电梯刷新次数*/
            double time = 0.1;//电梯刷新的时间间隔
            int prevTime = 0;//上一帧的时间

            DataToSystem.getData();//得到呼梯请求的数据
            List<Integer> callEleTimeList = new ArrayList<>();//暂存用户的呼梯时间，用于表格写入
            List<Integer> numOfCallEleList = new ArrayList<>();//暂存用户的呼梯人数，用于表格写入
            double []preHeight = new double[ElevatorConfig.ELEVATOR_COUNT]; //电梯上一帧的距地高度，用于判断是否正在开关门

            for (int i = 0; i < DataToSystem.num; i++) {
                /*呼梯请求参数*/
                callEleTime = DataToSystem.eleData[i][0];
                startOfCallEle = DataToSystem.eleData[i][1];
                endOfCallEle = DataToSystem.eleData[i][2];
                numOfCallEle = DataToSystem.eleData[i][3];
                callEleTimeList.add(callEleTime);
                numOfCallEleList.add(numOfCallEle);


                int flushTime = (int) ((callEleTime - prevTime) * (1 / time));//刷新次数
                for (int j = 0; j < flushTime; j++) {
                    //判断线程是否暂停
                    while (pause) onPause();
                    eleSleep();
                    t = t + 0.1;
                    List<ElevatorDetails> details = ElevatorManager.getInstance().flushState();/**获取刷新后的数据*/
                    DataOfElevatorState D = new DataOfElevatorState();//GUI获取数据实例化对象
                    for (int k = 0; k < ElevatorConfig.ELEVATOR_COUNT; k++) {
                        ElevatorDetails d = details.get(k);
                        TransformData(D, d, k, t,preHeight);//向GUI传输数据
                        d = null;
                    }

                    /*更新电梯GUI界面*/
                    RunElevator R = new RunElevator(inputNumClass.input.controller.erd.e1, inputNumClass.input.controller.erd.e2, inputNumClass.input.controller.erd.goalFloor, D);
                    R.startRun();

                    /*存储上一帧的电梯距地高度*/
                    for(int n = 0; n<ElevatorConfig.ELEVATOR_COUNT; n++){
                        preHeight[n] = D.height[n];
                    }
                }
                customCommand=new CustomCommand();
                customCommand.setNumber((byte)numOfCallEle);
                customCommand.setStartFloor((byte)startOfCallEle);
                customCommand.setTargetFloor((byte)endOfCallEle);
                /**
                 *  startOfCallEle;	//起始楼层
                 *  endOfCallEle;  //目标楼层
                 *  numOfCallEle;  //乘客人数
                 */
                algorithm.getDataFromGUI(customCommand,new Algorithm.Callback() {
                    @Override
                    public void onCommandCompleted(OutputMessageToGUI outputMessageToGUI) {
                        System.out.println("回调的电梯号 = "+outputMessageToGUI.getTarget());
                        System.out.println("回调的起始楼层 = "+outputMessageToGUI.getStartfloor());
                        System.out.println("回调的目标楼层 = "+outputMessageToGUI.getTargerfloor());
                        System.out.println("回调的乘客人数 = "+outputMessageToGUI.getNumber());
                    }
                });


                System.out.println("algorithm.getTheBestElevatorNum() = "+algorithm.getTheBestElevatorNum());
                if(algorithm.getTheBestElevatorNum() == -1) inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].insertItemAt("电梯繁忙，请稍后再呼梯", 0);
                else if(algorithm.getTheBestElevatorNum() != -1)
                {
                    inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].insertItemAt(
                            ((startOfCallEle) < 10 ? "0" + (startOfCallEle) : (startOfCallEle))
                                    + "—>"
                                    + ((endOfCallEle) < 10 ? "0"
                                    + (endOfCallEle) : (endOfCallEle))
                                    + "        time: " +  callEleTime + "s"
                                    +"        电梯号: "+ numberToLetter(algorithm.getTheBestElevatorNum()), 0);
                }
                else inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].insertItemAt("电梯出现未知错误", 0);
                inputNumClass.input.controller.erd.goalFloor[ElevatorConfig.FLOOR_COUNT - startOfCallEle].setSelectedIndex (0);

                prevTime = callEleTime;//赋值上一次的请求的时间
            }

            //数据写入Excel表格
            List<ElevatorTaskSummary> elevatorTaskSummaries = ElevatorManager.getInstance().summaryTask();
            for (int i = 0; i < elevatorTaskSummaries.size(); i++) {
                OutPutExcel.writeExcel(
                        callEleTimeList.get(i),//呼梯时间
                        elevatorTaskSummaries.get(i).getStartIndex()+1,//起始楼层
                        elevatorTaskSummaries.get(i).getEndIndex()+1,//目的楼层
                        numOfCallEleList.get(i),//呼梯人数
                        elevatorTaskSummaries.get(i).getElevatorId(),//最优的电梯号
                        elevatorTaskSummaries.get(i).getWaitTime(),//电梯等待时间
                        elevatorTaskSummaries.get(i).getCompleteTime());//电梯完成时间
                System.out.println("呼梯时间:"+callEleTimeList.get(i)+"呼梯的人数："+numOfCallEleList.get(i)+"数据："+elevatorTaskSummaries.get(i));
            }

        }

        /*向GUI传输数据*/
        private void TransformData(DataOfElevatorState D, ElevatorDetails d, int k, double t, double []preHeight) {
            D.timeOfCurrentEle = t;//GUI时间赋值

            /**获取一帧的电梯数据：电梯的状态与数字的转化
             *UP = 1;  DOWN = 2; STOP = 0;
             */
            if (d.getDirection().toString().equals("UP")) {
                D.StateOfCurrentEle[k] = 1;
            } else if (d.getDirection().toString().equals("DOWN")) {
                D.StateOfCurrentEle[k] = 2;
            } else if (d.getDirection().toString().equals("STOP")) {
                D.StateOfCurrentEle[k] = 0;
            } else System.out.println("电梯状态类型出错");

            /**获取一帧的电梯数据：电梯的当前楼层*/
            D.floorOfCurrentEle[k] = d.getFloor();
            //System.out.println("d.getFloor() = "+d.getFloor());

            /**获取一帧的电梯数据：电梯的当前楼层距地高度*/
            D.height[k] = d.getHeight();

            /**获取一帧的电梯数据：电梯任务列表*/
            D.totalTaskInfoList.add(k, d.getTasks());

            /**获取一帧的电梯数据：电梯上一帧的距地高度*/
            D.preHeight[k] = preHeight[k];
        }

        //线程睡眠时间
        public void eleSleep() {
            try {
                Thread.sleep(time);
            } catch (Exception e) {
                System.exit(0);//退出程序
            }
        }

        //调用该方法实现线程的暂停
        public void pauseThread() {
            pause = true;
        }

        //调用该方法实现恢复线程的运行
        public void resumeThread() {
            pause = false;
            synchronized (lock) {
                lock.notify();
            }
        }

        //暂停
        void onPause() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //数字与字符串转换
        private String numberToLetter(int num) {
            if (num < 0) {
                return null;
            }

            String letter = "";
            num--;
            do {
                if (letter.length() > 0) {
                    num--;
                }
                letter = ((char) (num % 26 + (int) 'B')) + letter;
                num = (int) ((num - num % 26) / 26);
            } while (num > 0);
            return letter;
        }
    }
}


