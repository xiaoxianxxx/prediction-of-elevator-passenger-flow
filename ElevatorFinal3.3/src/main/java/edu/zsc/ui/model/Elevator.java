package edu.zsc.ui.model;

import edu.zsc.ElevatorConfig;
import edu.zsc.control.ElevatorTask;


import javax.swing.*;
import java.awt.*;
import java.util.List;

/**这个类表示电梯，实现了电梯内部的逻辑功能*/

public class Elevator extends JPanel  {

	// 成员变量
	public static ElevatorS eleS;//电梯状态类
	public static final long serialVersionUID = 2L;
	public static int frameNum = 1;	//用户界面的电梯号
	public int currentFloor;//	记录电梯当前楼层
	public double currentTime = 0;//	记录电梯运行时间
	public JLabel elevatorNum;//   用户界面电梯号
	public JLabel statusLabel;//	显示用户界面电梯的信息面板
	public JButton[] floorButtons;//	单个电梯界面右侧一列的按钮，显示电梯的运动，不可点击
	public JPanel jPanelInformation;//信息面板
	public JComboBox elevatorTaskComboBox;//电梯任务下拉列表
	public List<ElevatorTask> taskInfoList;//电梯任务列表

	// 构造函数(用户界面)
	public Elevator() {
		super();
		// 初始化按钮阵列
		this.floorButtons = new JButton[ElevatorConfig.FLOOR_COUNT];

		// 用户界面方法
		this.configureElevator();
	}

	// 用户界面方法
	private void configureElevator() {
		this.setLayout((new GridLayout(ElevatorConfig.FLOOR_COUNT + 2, 1)));
		//this.setBorder(new MatteBorder(0, 1, 0, 1, Color.orange));

		//加入信息面板
        jPanelInformation = new JPanel();
        jPanelInformation.setLayout((new GridLayout(1,2)));
        this.elevatorNum = new JLabel("# "+numberToLetter(frameNum), SwingConstants.CENTER);
		this.elevatorNum.setFont(new Font("宋体",Font.PLAIN,20));
		this.elevatorNum.setForeground(Color.BLUE);
		frameNum++;
        jPanelInformation.add(elevatorNum);
        //#n后面的数字表示当前电梯运行时间
		int time = (int)currentTime;
		statusLabel = new JLabel("time:"+String.valueOf(time)+"s",
				SwingConstants.LEFT);
		statusLabel.setForeground(Color.RED);
        jPanelInformation.add(statusLabel);
		this.add(jPanelInformation);

		//加入电梯任务列表
		this.elevatorTaskComboBox = new JComboBox();
		this.add(elevatorTaskComboBox);

		//加入电梯
		int n = ElevatorConfig.FLOOR_COUNT;//总楼层
		int m = n-1;//总楼层减一
		for (int i = 0; i < ElevatorConfig.FLOOR_COUNT; i++) {
			this.floorButtons[m - i] = new JButton();
			this.floorButtons[m - i].setEnabled(false);
			this.floorButtons[m - i].setOpaque(true);
			this.floorButtons[m - i].setBackground(Color.white);
			this.add(this.floorButtons[m - i]);
		}
	}

	//数字转字母 1-26 ： A-Z
	private String numberToLetter(int num) {
		if (num <= 0) {
			return null;
		}
		String letter = "";
		num--;
		do {
			if (letter.length() > 0) {
				num--;
			}
			letter = ((char) (num % 26 + (int) 'A')) + letter;
			num = (int) ((num - num % 26) / 26);
		} while (num > 0);
		return letter;
	}
}
