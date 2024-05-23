package edu.zsc.ui.model;

import edu.zsc.ElevatorConfig;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**这个类表示电梯，实现了电梯内部的逻辑功能*/

public class ElevatorS extends JPanel  {

    // 成员变量
    public static final long serialVersionUID = 2L;
    public JButton[] floorButtons;//	单个电梯界面右侧一列的按钮，显示电梯的运动，不可点击
    public TextArea textArea;//	显示电梯状态界面的状态的文本框
    public JPanel jPanelStateDown;//电梯状态界面上面的面板

    // 构造函数(电梯状态界面)
    public ElevatorS() {
        super();
        // 初始化按钮阵列
        this.floorButtons = new JButton[ElevatorConfig.FLOOR_COUNT];

        // 电梯状态界面方法
        this.configureElevatorState();
    }

    // 电梯状态界面方法
    private void configureElevatorState() {
        this.setLayout((new GridLayout(1, 1)));
        this.setBorder(new MatteBorder(0, 2, 0, 2, Color.orange));

        jPanelStateDown = new JPanel();
        jPanelStateDown.setLayout((new GridLayout(1,1)));
        //jPanelStateDown.setBounds(0,300,300,450);
        //jPanelStateDown.setPreferredSize(new Dimension(300, 450));//设置JPanel的大小
        this.textArea = new TextArea();
        this.textArea.setEditable(false);
        this.textArea.setFont(new Font("黑体",Font.BOLD,16));
        jPanelStateDown.add(textArea, BorderLayout.CENTER);
        this.add(jPanelStateDown);
    }
}
