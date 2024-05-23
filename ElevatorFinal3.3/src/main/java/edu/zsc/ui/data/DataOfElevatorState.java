package edu.zsc.ui.data;



import edu.zsc.ElevatorConfig;
import edu.zsc.control.ElevatorTask;

import java.util.ArrayList;
import java.util.List;


/**电梯每一帧的数据，通过一帧一帧的形式实现电梯动态化
 * 用于调用仿真系统得到最新的一帧的电梯状态
 * */
public class DataOfElevatorState {
    public static double timeOfCurrentEle;//电梯此时的当前时间
    public static int []floorOfCurrentEle = new int[ElevatorConfig.ELEVATOR_COUNT];//TOTAL_ELEVATOR部电梯当前的楼层
    public static int []StateOfCurrentEle = new int[ElevatorConfig.ELEVATOR_COUNT];//TOTAL_ELEVATOR部电梯当前的状态（0：停止 1：向上 2：向下）
    public static List<List<ElevatorTask>> totalTaskInfoList = new ArrayList<>();//TOTAL_ELEVATOR部电梯当前的任务列表
    public static double[] height = new double[ElevatorConfig.ELEVATOR_COUNT];//当前电梯的距地高度
    public static double[] preHeight = new double[ElevatorConfig.ELEVATOR_COUNT];//上一帧电梯的距地高度


//    public static double []openCloseTime = new double[ELEVATOR_COUNT];//TOTAL_ELEVATOR部电梯当前开关门时间
//    public static int []peopleOfCurrentEle = new int[ELEVATOR_COUNT];//TOTAL_ELEVATOR部电梯当前的人数
//    public static int []prePeople = new int[ElevatorConfig.ELEVATOR_COUNT];//TOTAL_ELEVATOR部电梯上一帧的人数
//    public static int []preState = new int[ElevatorConfig.ELEVATOR_COUNT];//TOTAL_ELEVATOR部电梯上一帧的状态
}

