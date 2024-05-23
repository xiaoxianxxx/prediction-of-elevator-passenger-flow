package edu.zsc.ui.view;



import edu.zsc.ElevatorConfig;
import edu.zsc.ui.controller.ElevatorController;


import javax.swing.*;
import java.awt.*;

/**这个类主要负责主界面的布局，
 并将它代理给ElevatorController，
 使得某些监听事件的逻辑添加可以写在ElevatorController中。
 其中的函数方法都用于UI设置。*/

public class ElevatorState extends JFrame {
    /*创建 ElevatorController 对象 delegateState*/
    public ElevatorController delegateState;

    /*设置 delegate*/
    public ElevatorState(ElevatorController delegateState) throws HeadlessException {
        super();
        this.delegateState = delegateState;
    }

    /*设置电梯状态界面的方法,设置 JFrame*/
    private void configureJFrameState() {
        this.setTitle("Elevator State");// 设置标题
        //double width = Toolkit.getDefaultToolkit().getScreenSize().width; //得到当前屏幕分辨率的高
        //double height = Toolkit.getDefaultToolkit().getScreenSize().height;//得到当前屏幕分辨率的宽
        this.setSize(800,500);//设置窗口大小
        this.setLocation(0,0);
        //this.setResizable(false);// 设置为不能改变大小
        //this.setLocationRelativeTo(null);// 设置窗口在屏幕中心的位置
    }

    /*配置 Elevator TextArea Panel：电梯状态界面的多部电梯*/
    private void configureElevatorStatePanel() {
        this.delegateState.addElevator(1);
    }

    //显示 FrameState
    public void showFrameState() {
        this.setLayout(new GridLayout(1, ElevatorConfig.ELEVATOR_COUNT + 1));
        this.configureJFrameState();//设置电梯状态界面的方法,设置 JFrame
        this.configureElevatorStatePanel();//电梯状态界面的多部电梯
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置退出应用程序
    }
}
