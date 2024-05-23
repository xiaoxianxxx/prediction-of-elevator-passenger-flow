package edu.zsc.ui.run;


import edu.zsc.control.ElevatorTask;
import edu.zsc.ui.data.DataOfElevatorState;
import edu.zsc.ui.model.Elevator;
import edu.zsc.ui.model.ElevatorS;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static edu.zsc.ElevatorConfig.ELEVATOR_COUNT;
import static edu.zsc.ElevatorConfig.FLOOR_COUNT;


public class RunElevator {
    //成员变量
    Elevator[] ele;//电梯类
    ElevatorS[] eleState;//电梯状态类
    DataOfElevatorState D;
    JComboBox[] goalFloor;//目的楼层和派梯编号


    //构造函数初始化
    public RunElevator(Elevator[] e1, ElevatorS[] e2, JComboBox[] goalFloor, DataOfElevatorState D) {
        super();
        this.ele = e1;
        this.eleState = e2;
        this.goalFloor = goalFloor;
        this.D = D;
    }

    //电梯的运动
    public void startRun() {
        for (int i = 0; i < ELEVATOR_COUNT; i++) {
            Run r = new Run(i, this.ele[i], this.eleState[i], D);
            r.run();
        }
    }

}

class Run{
    int index;//电梯的标号
    Elevator e1;//电梯类，用于改变电梯当前的楼层
    ElevatorS e2;//电梯状态类，用于改变电梯当前的楼层
    DataOfElevatorState D;


    //构造函数初始化
    Run(int i, Elevator e1, ElevatorS e2, DataOfElevatorState D) {
        super();
        this.index = i;
        this.e1 = e1;
        this.e2 = e2;
        this.D = D;
    }

    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }


    Object lock = new Object();

    //电梯界面的数据更新
    public void run() {
        synchronized (lock) {

            //更新当前任务列表
            e1.elevatorTaskComboBox.removeAllItems();
            e1.taskInfoList = D.totalTaskInfoList.get(index);

            for (ElevatorTask taskInfo : e1.taskInfoList) {
                String string = "  s:" + (taskInfo.getStartIndex() + 1) + "  e:" + (taskInfo.getEndIndex() + 1)
                        + "  isA:" + taskInfo.isAscent() + "  isR:" + taskInfo.isReceive();
                e1.elevatorTaskComboBox.addItem(string);
            }


            Font font = new Font("黑体", Font.BOLD, 21);
            //更新一次电梯的状态，使电梯回归最初的状态
            int time = (int) D.timeOfCurrentEle;
            e1.statusLabel.setText("time:" + String.valueOf(time) + "s");//更新当前电梯的运行时间
            e1.currentFloor = D.floorOfCurrentEle[index];//更新当前楼层

            for (int i = 0; i < FLOOR_COUNT; i++) {
                e1.floorButtons[i].setBackground(Color.white);
                e1.floorButtons[i].setForeground(Color.BLACK);
                e1.floorButtons[i].setFont(font);
                e1.floorButtons[i].setText(" ");
            }


            /**电梯的当前状态0,1,2;0:停止（红色表示）  1:向上（蓝色表示）    2:向下（淡蓝色表示）*/
//            if (D.StateOfCurrentEle[index] == 1) {
////            System.out.println("上一帧高度："+D.preHeight[index]);
////            System.out.println("当前高度："+D.height[index]);
//                if (D.preHeight[index] == D.height[index])//正在开关门
//                {
//                    e1.floorButtons[e1.currentFloor].setBackground(Color.green);//当前楼层显示为绿色
//                    e1.floorButtons[e1.currentFloor].setText("正在上下乘客");
//                } else //电梯向上
//                {
//                    e1.floorButtons[e1.currentFloor].setBackground(new Color(0,255,255));//当前楼层显示为淡蓝色
//                    e1.floorButtons[e1.currentFloor].setText("上");
//                }
//            } else if (D.StateOfCurrentEle[index] == 2) {
//                if (D.preHeight[index] == D.height[index])//正在开关门
//                {
//                    e1.floorButtons[e1.currentFloor].setBackground(Color.green);//当前楼层显示为绿色
//                    e1.floorButtons[e1.currentFloor].setText("正在上下乘客");
//                } else //电梯向下
//                {
//                    e1.floorButtons[e1.currentFloor].setBackground(new Color(0, 0, 255));//当前楼层显示为深蓝色
//                    e1.floorButtons[e1.currentFloor].setText("下");
//                }
//            } else if (D.StateOfCurrentEle[index] == 0) {
//                /**电梯的当前状态用红色表示0:停*/
//                e1.floorButtons[e1.currentFloor].setBackground(Color.red);//当前楼层显示为红色
//                e1.floorButtons[e1.currentFloor].setText("停");
//            }

            if (D.StateOfCurrentEle[index] == 1) {
//            System.out.println("上一帧高度："+D.preHeight[index]);
//            System.out.println("当前高度："+D.height[index]);
                if (D.preHeight[index] == D.height[index])//正在开关门
                {
                    e1.floorButtons[e1.currentFloor].setBackground(Color.green);//当前楼层显示为绿色
                    e1.floorButtons[e1.currentFloor].setText("正在上下乘客 ↑");
                } else //电梯向上
                {
                    e1.floorButtons[e1.currentFloor].setBackground(new Color(0,255,255));//当前楼层显示为淡蓝色
                    e1.floorButtons[e1.currentFloor].setText("上");
                }
            } else if (D.StateOfCurrentEle[index] == 2) {
                if (D.preHeight[index] == D.height[index])//正在开关门
                {
                    e1.floorButtons[e1.currentFloor].setBackground(Color.green);//当前楼层显示为绿色
                    e1.floorButtons[e1.currentFloor].setText("正在上下乘客 ↓");
                } else //电梯向下
                {
                    e1.floorButtons[e1.currentFloor].setBackground(new Color(0, 0, 255));//当前楼层显示为深蓝色
                    e1.floorButtons[e1.currentFloor].setText("下");
                }
            } else if (D.StateOfCurrentEle[index] == 0) {
                /**电梯的当前状态用红色表示0:停*/
                e1.floorButtons[e1.currentFloor].setBackground(Color.red);//当前楼层显示为红色
                e1.floorButtons[e1.currentFloor].setText("停");
            }

        }
    }
}
