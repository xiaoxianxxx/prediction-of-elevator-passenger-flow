package edu.zsc;

import edu.zsc.ui.view.InputNum;

/**
 * 楼宇电梯固定属性
 */
public class ElevatorConfig {
    // 电梯最高楼层
    public static  int FLOOR_COUNT;

    // 电梯的数量
    public static  int ELEVATOR_COUNT;

    //每层楼高度
    public static  double EACH_FLOOR_HEIGHT;

    // 电梯刷新的时间间隔
    public static double FLUSH_TIME = 0.1;

    //输入数据文件
    public static final String dataFile = "data\\Up.xls";//呼梯请求数据文件

    //输出数据文件
    public static final String outputFile = "data\\Output.xls";//呼梯请求对应最优电梯号数据文件

    //    // 平均停靠时间
//    public static final double STOP_TIME = InputNum.STOP_TIME;

//    // 负载人数
//    public static final int STAN_COUNT = InputNum.STAN_COUNT;
//
//    // 电梯运行的平均速度
//    public static final double SPEED = InputNum.SPEED;


//    //楼宇电梯固定属性
//    private int F;//楼层数
//    private int N;//电梯数
//    private int StanNum;//荷载人数
//    private float v;//平均运行速度
//    private float H;//楼层高度
//    private float T;//平均停靠时间

}
