package edu.zsc.ui.controller;



import edu.zsc.ElevatorConfig;
import edu.zsc.ui.data.EleRunData;
import edu.zsc.ui.model.Elevator;

import edu.zsc.ui.model.ElevatorS;
import edu.zsc.ui.view.ElevatorGUI;
import edu.zsc.ui.view.ElevatorState;
import edu.zsc.ui.view.InputNum;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** 这个类是核心控制类。*/

public class ElevatorController {
	// 成员变量
	public static ElevatorGUI view;//	controller 对应的 view
	public static ElevatorState viewState;//	controller 对应的 view
	private Elevator[] elevators;//	用于储存用户界面 TOTAL_ELEVATOR 个电梯的数组
    public  ElevatorS[] elevatorsState;//	用于储存电梯状态界面 TOTAL_ELEVATOR 个电梯的数组
	private JComboBox[] goalJComboBox;//	储存目的楼层和派梯编号的下拉列表
	private boolean[] upStatus;//	记录对应的向上按键是否被按下
	private boolean[] downStatus;//	记录对应的向下按键是否被按下
	public EleRunData erd = new EleRunData();//存储着TOTAL_ELEVATOR部电梯的数据
	public JButton speedButton;// 变速按钮
	public JButton pauseButton;// 暂停按钮
	public Boolean judge;//判断线程的变速，true代表加速，false代表减速
	public JLabel[] label;//表示楼层号


	//状态栏
	public JLabel floorLabel;//楼层数标签
	public JLabel elevatorLabel;//电梯数标签
	public JLabel versionLabel;//版本号标签
	public JLabel teamLabel;//团队名标签

	//说明栏
	public JLabel sLabel;
	public JLabel eLabel;
	public JLabel isALabel;
	public JLabel isRLabel;


	// 构造函数
	public ElevatorController() {
		super();
		
		// 初始化成员变量
		this.elevatorsState = new ElevatorS[ElevatorConfig.ELEVATOR_COUNT];/**初始化电梯状态界面*/
		this.elevators = new Elevator[ElevatorConfig.ELEVATOR_COUNT];/**初始化电梯用户界面*/
		this.goalJComboBox = new JComboBox[ElevatorConfig.FLOOR_COUNT];/**目的楼层和派梯编号的下拉列表*/
		this.label = new JLabel[ElevatorConfig.FLOOR_COUNT];/**初始化楼层号*/
		this.upStatus = new boolean[ElevatorConfig.FLOOR_COUNT];
		this.downStatus = new boolean[ElevatorConfig.FLOOR_COUNT];


		//线程变速
		this.judge = true;
		this.speedButton = new JButton("正常速度");
		this.speedButton.setHorizontalAlignment(JButton.CENTER);
		this.speedButton.setFont(new Font("宋体",Font.BOLD,20));
		this.speedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if(InputNum.time == 200){
					judge = true;
				}
				if(InputNum.time == 25){
					judge = false;
				}
				if(InputNum.time>25 && judge == true){
					InputNum.time/=2;
					if(InputNum.time==50){
						speedButton.setText("二倍速");
					}else if(InputNum.time==25){
						speedButton.setText("四倍速");
					}else if(InputNum.time==100){
						speedButton.setText("正常速度");
					}else if(InputNum.time==200){
						speedButton.setText("半倍速");
					}
				}
				else if(InputNum.time<200 && judge == false){
					InputNum.time*=2;
					if(InputNum.time==50){
						speedButton.setText("二倍速");
					}else if(InputNum.time==25){
						speedButton.setText("四倍速");
					}else if(InputNum.time==100){
						speedButton.setText("正常速度");
					}else if(InputNum.time==200){
						speedButton.setText("半倍速");
					}
				}
			}
		});

		//线程暂停
		this.pauseButton = new JButton("暂停");
		this.pauseButton.setHorizontalAlignment(JButton.CENTER);
		this.pauseButton.setFont(new Font("宋体",Font.BOLD,20));
		this.pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if(pauseButton.getText()=="暂停"){
					InputNum.g.pauseThread();//暂停线程
					pauseButton.setText("运行");
				}else {
					InputNum.g.resumeThread();//线程运行
					pauseButton.setText("暂停");
				}
			}
		});

		for (int i = 0; i < ElevatorConfig.FLOOR_COUNT; i++) {
			this.upStatus[i] = false;
			this.downStatus[i] = false;
		}
		
		this.view = new ElevatorGUI(this);// 设置 ElevatorGUI 对象
		this.viewState = new ElevatorState(this);// 设置 ElevatorState 对象
	}

	// 显示view
	public void showView() {
		this.view.showFrame();
	}

	// 显示Stateview
	public void showStateView() {
		this.viewState.showFrameState();
	}

	//在FrameGUI界面添加状态栏
	public void addStatusBar(){
		JPanel statusBarPanel = new JPanel();
		statusBarPanel.setLayout(new GridLayout(1, 4));
		//statusBarPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));
		floorLabel = new JLabel("    楼层数：" + ElevatorConfig.FLOOR_COUNT, SwingConstants.LEFT);
		this.floorLabel.setFont(new Font("黑体",Font.BOLD,20));
		statusBarPanel.add(floorLabel);

		elevatorLabel = new JLabel("    电梯数：" + ElevatorConfig.ELEVATOR_COUNT, SwingConstants.LEFT);
		this.elevatorLabel.setFont(new Font("黑体",Font.BOLD,20));
		statusBarPanel.add(elevatorLabel);

		versionLabel = new JLabel("    版本号：1.4.1", SwingConstants.LEFT);
		this.versionLabel.setFont(new Font("黑体",Font.BOLD,20));
		statusBarPanel.add(versionLabel);

		teamLabel = new JLabel("    团队名：大数据与智能计算团队", SwingConstants.LEFT);
		this.teamLabel.setFont(new Font("黑体",Font.BOLD,20));
		statusBarPanel.add(teamLabel);

		// 添加到ElevatorGUI
		this.view.add(statusBarPanel, BorderLayout.SOUTH);
	}

	//在FrameGUI界面添加说明栏
	public void addExplainBar(){
		JPanel explainBarPanel = new JPanel();
		explainBarPanel.setLayout(new GridLayout(1, 4));
		explainBarPanel.setBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK));
		sLabel = new JLabel("s：呼梯楼层", SwingConstants.CENTER);
		this.sLabel.setFont(new Font("黑体",Font.BOLD,20));
		explainBarPanel.add(sLabel);

		eLabel = new JLabel("e：目的楼层", SwingConstants.CENTER);
		this.eLabel.setFont(new Font("黑体",Font.BOLD,20));
		explainBarPanel.add(eLabel);

		isALabel = new JLabel("isA：呼梯楼层到目的楼层的方向是否为上", SwingConstants.CENTER);
		this.isALabel.setFont(new Font("黑体",Font.BOLD,20));
		explainBarPanel.add(isALabel);

		isRLabel = new JLabel("isR：该任务是否接到乘客", SwingConstants.CENTER);
		this.isRLabel.setFont(new Font("黑体",Font.BOLD,20));
		explainBarPanel.add(isRLabel);

		// 添加到ElevatorGUI
		this.view.add(explainBarPanel, BorderLayout.NORTH);
	}

	//添加FrameGUI左侧的所有下拉列表
	public void addFloorComboBoxes() {
		JPanel floorComboBoxesPanel = new JPanel();
		
		// 设置 FloorComboBoxesPanel 样式2列显示
		floorComboBoxesPanel.setLayout(new GridLayout(ElevatorConfig.FLOOR_COUNT + 2, 2));

		floorComboBoxesPanel.add(pauseButton);//加入暂停按钮
		floorComboBoxesPanel.add(speedButton);//加入变速按钮
		floorComboBoxesPanel.add(new JLabel("楼层", SwingConstants.CENTER));
		floorComboBoxesPanel.add(new JLabel("呼梯请求及派梯编号 \\ 电梯任务列表", SwingConstants.CENTER));

		int n = ElevatorConfig.FLOOR_COUNT;//总楼层
		int m = n-1;//总楼层减一
		for (int i = 0; i < ElevatorConfig.FLOOR_COUNT; i++) {
			//创建JLabel，并居中显示，表示楼层号
			label[i] = new JLabel(String.valueOf(m-i+1), SwingConstants.CENTER);
			erd.label[i] = label[i];/**将初始化的楼层号存进EleRunData*/
			floorComboBoxesPanel.add(label[i]);

			//创建目的楼层和派梯编号的下拉列表
			this.goalJComboBox[i] = new JComboBox();
			erd.goalFloor[i] = this.goalJComboBox[i];/**将初始化的目的楼层存进EleRunData*/
			floorComboBoxesPanel.add(this.goalJComboBox[i]);
		}
		// 添加到ElevatorGUI
		this.view.add(floorComboBoxesPanel, BorderLayout.WEST);
	}

	//在FrameGUI界面和FrameState 界面添加 ElevatorConst.TOTAL_ELEVATOR 部电梯
	public void addElevator(int mark) {
		/**0:电梯用户界面
		 * 1:电梯状态界面*/
		if(mark == 0){
			JPanel floorElevatorPanel = new JPanel();
			floorElevatorPanel.setLayout(new GridLayout(1,ElevatorConfig.ELEVATOR_COUNT));
			for (int i = 0; i < ElevatorConfig.ELEVATOR_COUNT; i++) {
				this.elevators[i] = new Elevator();
				floorElevatorPanel.add(this.elevators[i]);
				erd.e1[i] = this.elevators[i];/**将初始化的电梯存进EleRunData*/
			}
			// 添加到ElevatorGUI
			this.view.add(floorElevatorPanel, BorderLayout.CENTER);

		} else if(mark == 1){
			for (int i = 0; i < ElevatorConfig.ELEVATOR_COUNT; i++) {
				this.elevatorsState[i] = new ElevatorS();
				this.viewState.add(this.elevatorsState[i]);
				erd.e2[i] = this.elevatorsState[i];/**将初始化的电梯存进EleRunData*/
			}
		}
	}
}
