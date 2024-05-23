package edu.zsc.ui.data;



import edu.zsc.ElevatorConfig;
import edu.zsc.ui.model.Elevator;
import edu.zsc.ui.model.ElevatorS;

import javax.swing.*;



public  class EleRunData {
    public Elevator[]e1 = new Elevator[ElevatorConfig.ELEVATOR_COUNT];//存放TOTAL_ELEVATOR部电梯的用户界面的对象
    public ElevatorS[]e2 = new ElevatorS[ElevatorConfig.ELEVATOR_COUNT];//存放TOTAL_ELEVATOR部电梯的状态界面的对象
    public JComboBox[] goalFloor = new JComboBox[ElevatorConfig.FLOOR_COUNT];//存放TOTAL_FLOOR部电梯的对应的目的楼层和派梯编号界面
    public JLabel[] label = new JLabel[ElevatorConfig.FLOOR_COUNT];//存放TOTAL_FLOOR个楼层号
}
