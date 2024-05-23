package edu.zsc;

import edu.zsc.logic.Algorithm;
import edu.zsc.logic.BasicMessage;
import edu.zsc.ui.view.InputNum;


public class Main {

    public static void main(String[] args) {
        Algorithm algorithm = Algorithm.getInstance();//获取算法实例




        /**
         * 初始化要安装该电梯的建筑物的相关参数
         * int F = 10;//该建筑物的楼层数
         * int N = 4;//该建筑物的要安装的电梯数量
         * int H = 4;//该建筑物的每一层的楼层高度
         */
        algorithm.initBuilding(10,4,4);//初始化要安装改电梯建筑物的参数

        /**
         * 一部电梯基础设置参数
         * byte topFloor =  绝对楼层数 顶楼
         * byte bottomFloor 绝对楼层数 底楼
         * byte[] doorPos 楼层可用门
         * byte lift 电梯号>=1
         * int speed 额定梯速，speed /1000 = 0.000 m/s
         * int time 平均停靠时间
         * int people 荷载人数
         */
        BasicMessage basicMessage1 = new BasicMessage();
        BasicMessage basicMessage2 = new BasicMessage();
        BasicMessage basicMessage3 = new BasicMessage();
        BasicMessage basicMessage4 = new BasicMessage();
        byte[] doorPos1 ={1,0,1,1,1,1,1,0,1,0};
        byte[] doorPos2 ={1,1,1,1,1,1,1,1,1,0};
        byte[] doorPos3 ={1,1,1,1,1,1,1,0,1,1};
        byte[] doorPos4 ={1,1,1,1,1,1,1,1,1,1};
        basicMessage1.setTopFloor(Byte.parseByte(String.valueOf(10)));
        basicMessage1.setBottomFloor(Byte.parseByte(String.valueOf(1)));
        basicMessage1.setDoorPos(doorPos1);
        basicMessage1.setLift(Byte.parseByte(String.valueOf(1)));
        basicMessage1.setSpeed(2);
        basicMessage1.setTime(4);
        basicMessage1.setPeople(10);
        basicMessage2.setTopFloor(Byte.parseByte(String.valueOf(10)));
        basicMessage2.setBottomFloor(Byte.parseByte(String.valueOf(1)));
        basicMessage2.setDoorPos(doorPos2);
        basicMessage2.setLift(Byte.parseByte(String.valueOf(2)));
        basicMessage2.setSpeed(2);
        basicMessage2.setTime(4);
        basicMessage2.setPeople(10);
        basicMessage3.setTopFloor(Byte.parseByte(String.valueOf(10)));
        basicMessage3.setBottomFloor(Byte.parseByte(String.valueOf(1)));
        basicMessage3.setDoorPos(doorPos3);
        basicMessage3.setLift(Byte.parseByte(String.valueOf(3)));
        basicMessage3.setSpeed(2);
        basicMessage3.setTime(4);
        basicMessage3.setPeople(10);
        basicMessage4.setTopFloor(Byte.parseByte(String.valueOf(10)));
        basicMessage4.setBottomFloor(Byte.parseByte(String.valueOf(1)));
        basicMessage4.setDoorPos(doorPos4);
        basicMessage4.setLift(Byte.parseByte(String.valueOf(4)));
        basicMessage4.setSpeed(2);
        basicMessage4.setTime(4);
        basicMessage4.setPeople(10);
        algorithm.BasicSettingMessage(basicMessage1);//第一部电梯参数设置
        algorithm.BasicSettingMessage(basicMessage2);//第二部电梯参数设置
        algorithm.BasicSettingMessage(basicMessage3);//第三部电梯参数设置
        algorithm.BasicSettingMessage(basicMessage4);//第四部电梯参数设置
        /**
         * 初始化群控系统
         */
        algorithm.initController();



        InputNum inputNum = new InputNum();
        inputNum.showFrame(algorithm);//将算法实例传入

    }
}


