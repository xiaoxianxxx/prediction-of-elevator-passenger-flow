package edu.zsc.ui.view;



import edu.zsc.ui.controller.ElevatorController;

import javax.swing.*;
import java.awt.*;

/**这个类主要负责主界面的布局，
并将它代理给ElevatorController，
使得某些监听事件的逻辑添加可以写在ElevatorController中。
其中的函数方法都用于UI设置。*/

public class ElevatorGUI extends JFrame {
	/*创建 ElevatorController 对象 delegate*/
	public ElevatorController delegate;

	/*设置 delegate*/
	public ElevatorGUI(ElevatorController delegate) throws HeadlessException {
		super();
		this.delegate = delegate;
	}

	/*设置用户界面的方法,设置 JFrame*/
	private void configureJFrame() {
		this.setTitle("Elevator Simulation");// 设置标题
        double width = Toolkit.getDefaultToolkit().getScreenSize().width; //得到当前屏幕分辨率的高
        double height = Toolkit.getDefaultToolkit().getScreenSize().height;//得到当前屏幕分辨率的宽
        this.setSize((int)width,(int)height-50);//设置窗口大小
		//this.setResizable(false);// 设置为不能改变大小
		this.setLocationRelativeTo(null);// 设置窗口在屏幕中心的位置
	}

	/*配置 Floor ComboBoxes Panel：界面左边的下拉列表*/
	private void configureFloorComboBoxesPanel() {
		this.delegate.addFloorComboBoxes();
	}
	
	/*配置 Elevator Buttons Panel：用户界面右边的多部电梯*/
	private void configureElevatorButtonsPanel() {
		this.delegate.addElevator(0);
	}

	/*配置 Elevator StatusBar Panel：用户界面的状态栏*/
	private void configureElevatorStatusBarPanel() {
		this.delegate.addStatusBar();
	}

	/*配置 Elevator StatusBar Panel：用户界面的说明栏*/
	private void configureElevatorExplainBarPanel() {
		this.delegate.addExplainBar();
	}
	
	/*显示 Frame*/
	public void showFrame() {
        //this.setLayout(new GridLayout(1, ElevatorConst.TOTAL_ELEVATOR + 1));
		this.setLayout(new BorderLayout());
        this.configureJFrame();//设置用户界面的方法,设置 JFrame
        this.configureFloorComboBoxesPanel();//界面左边的下拉列表
        this.configureElevatorButtonsPanel();//界面右边的多部电梯
		this.configureElevatorStatusBarPanel();//添加状态栏
		this.configureElevatorExplainBarPanel();//添加说明栏
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置退出应用程序
	}
}
