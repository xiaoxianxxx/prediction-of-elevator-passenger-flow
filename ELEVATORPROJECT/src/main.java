import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
public class main
{

    public static void main(String[] args)
    {
        WINDOW.setWindows();
        //RUN.RunWays();
     //   Character  TrafficMode=new Character('i');//设定交通模式
      //  Integer Thishigh=10;

       /* System.out.println("请输入楼房高度");
        Scanner sd=new Scanner(System.in);
        int Thishigh=sd.nextInt();
        System.out.println("请输入交通模式");
        char TrafficMode=sd.next().charAt(0);//u表示上行 i表示层间 d表示下行*/
    //    Character TrafficMode=new Character('i');//设定交通模式
     //   Integer Thishigh=10;

        /*Random r=new Random();
        Timemachine timecounter=new Timemachine(0,0.05); //计时器类
        TrafficParameter T=new TrafficParameter(Parameter.TrafficMode);//输入交通模式
        List<floor> FloorList=new ArrayList<>();
        for (int i=0;i<Parameter.Thishigh;i++) FloorList.add(new floor(r.nextInt(20),i));//建立楼层list 人数随机为20以下的随机数
        Bulidings B=new Bulidings(FloorList, Parameter.Thishigh);//初始化建筑物
        double [][]matrix=B.setMatrix(FloorList,T);//初始化该楼的OD矩阵
        List<records> Myrecords=new ArrayList<>();  //初始化记录序列
        for (floor F:FloorList) F.setOrigins(B,T);//初始化起始密度
        List<Massage> MassageList=new ArrayList<>();//初始化一个 消息队列

        for (int i = 0; i < 1000; i++) {
            floor mystart=FloorList.get(Elector.ElectStart(B,FloorList, Parameter.Thishigh,T));
            floor myend=FloorList.get(Elector.ElectDestination(Parameter.Thishigh,matrix,mystart.number));
            Myrecords.add(new records(i,mystart,myend,timecounter.time));//记录序号，时间，起始楼层，到达楼层
            System.out.println(String.format("%.2f",timecounter.time));
            timecounter.nexttime();
            MassageList.add(Myrecords.get(i).newmassage(timecounter));
            if (!MassageList.isEmpty())
            {
                for (Massage x:MassageList) if (x.correcttime<timecounter.time) x.upchange(FloorList);
            }
            System.out.println(i+"出发楼层"+mystart.number);
            System.out.println(i+"目标楼层"+myend.number);
            System.out.println("---------------");
        }*/



       /* for (int i=0;i<Thishigh;i++)
        {
            for (int j=0;j<Thishigh;j++) System.out.println(matrix[i][j]);
        }*/
      /*  int start=Elector.ElectStart(B,FloorList,Thishigh,T);
      for (int i = 0; i < 100; i++) {
            int start=Elector.ElectStart(B,FloorList,Thishigh,T);
            System.out.println(start);
        }
       Elector.ElectDestination(Thishigh,matrix,start);*/




    }
}
