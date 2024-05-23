import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WINDOW {
    public static void setWindows()
    {
        //这里是窗口内容的起点


        JFrame frmMain = new JFrame("ElevatorOccasionSelect");
        frmMain.setSize(825, 500);
        frmMain.setLocation(200, 200);
        // 设置布局器为null，即进行绝对定位，容器上的组件都需要指定位置和大小
        frmMain.setLayout(null);
        JButton shop = new JButton("商场");
        // 指定位置和大小 btn1.setBounds(x, y, width, height);
        shop.setBounds(50, 25, 200, 100);
        JButton hospital = new JButton("医院");
        hospital.setBounds(50, 350, 200, 100);
        JButton home = new JButton("住宅");
        home.setBounds(550, 25, 200, 100);
        // 没有指定位置和大小，不会出现在容器上
        JButton office = new JButton("办公楼");
        office.setBounds(550, 350, 200, 100);
        shop.addActionListener(new ActionListener() {//给医院按钮设定启动
            @Override
            public void actionPerformed(ActionEvent e) {
                Parameter P1=new Parameter();
                P1.Thishigh=20;
                P1.AverageStaytime=900;
                P1.outputFile="D:\\商场.xls";
                P1.TrafficMode='i';//商城以层间交通为主
                RUN.RunWays();
                frmMain.setVisible(false);
//                Parameter.stophere=0;
            }
        });
        hospital.addActionListener(new ActionListener() {//给医院按钮设定启动
            @Override
            public void actionPerformed(ActionEvent e) {
                Parameter P1=new Parameter();
                P1.Thishigh=20;
                P1.AverageStaytime=500;
                P1.outputFile="D:\\医院.xls";
                P1.TrafficMode='i';//医院以层间交通为主
                RUN.RunWays();
                frmMain.setVisible(false);
//                Parameter.stophere=0;
            }
        });
        office.addActionListener(new ActionListener() {//给医院按钮设定启动
            @Override
            public void actionPerformed(ActionEvent e) {
                Parameter P1=new Parameter();
                P1.Thishigh=20;
                P1.AverageStaytime=1000;
                P1.outputFile="D:\\办公楼.xls";
                if (Parameter.starttime==8)
                P1.TrafficMode='u';//早上下楼
                else if (Parameter.starttime==10)
                    P1.TrafficMode='i';//
                else if (Parameter.starttime==18)
                    P1.TrafficMode='d';//
                RUN.RunWays();
                frmMain.setVisible(false);
//                Parameter.stophere=0;
            }
        });
        home.addActionListener(new ActionListener() {//给住宅按钮设定启动
            @Override
            public void actionPerformed(ActionEvent e) {
                Parameter P1=new Parameter();
                P1.Thishigh=20;
                P1.AverageStaytime=2000;
                P1.outputFile="D:\\住宅楼.xls";
                P1.arrival=0.1;//住宅的到达率较低
                if (P1.starttime==8)
                P1.TrafficMode='d';//早上下楼
                else if (P1.starttime==10)
                    P1.TrafficMode='i';//
                else if(P1.starttime==18)
                    P1.TrafficMode='u';//晚上上楼
                RUN.RunWays();
                frmMain.setVisible(false);
//                Parameter.stophere=0;
            }
        });





        frmMain.add(shop);
        frmMain.add(hospital);
        frmMain.add(home);
        // b4没有指定位置和大小，不会出现在容器上
        frmMain.add(office);
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frmMain.setVisible(true);
        //这里是窗口类内容的终点
    }
}