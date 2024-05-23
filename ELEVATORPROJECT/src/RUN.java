import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class RUN {

    public static void RunWays()
    {
        Random r=new Random();
        //Timemachine timecounter=new Timemachine(0,0.5); //计时器类
        TrafficParameter T=new TrafficParameter(Parameter.TrafficMode);//输入交通模式
        Timemachine timecounter=new Timemachine(0,Parameter.arrival); //计时器类
        List<floor> FloorList=new ArrayList<>();
        for (int i=0;i<Parameter.Thishigh;i++) FloorList.add(new floor(Parameter.populations[i],i,Parameter.AverageStaytime));//建立楼层list 人数随机为20以下的随机数

        Bulidings B=new Bulidings(FloorList, Parameter.Thishigh);//初始化建筑物
        double [][]matrix=B.setMatrix(FloorList,T);//初始化该楼的OD矩阵
        List<records> Myrecords=new ArrayList<>();  //初始化记录序列
        for (floor F:FloorList) F.setOrigins(B,T);//初始化起始密度
        List<Massage> MassageList=new ArrayList<>();//初始化一个 消息队列

        for (int i = 0; i < 500; i++) {
            floor mystart=FloorList.get(Elector.ElectStart(B,FloorList, Parameter.Thishigh,T));
            floor myend=FloorList.get(Elector.ElectDestination(Parameter.Thishigh,matrix,mystart.number));
            if(myend.number== mystart.number) mystart.number++;

            Myrecords.add(new records(i,mystart,myend,timecounter.time,timecounter.Realtime));//记录序号，时间，起始楼层，到达楼层
            System.out.println(String.format("%.2f",timecounter.time));
            System.out.println(timecounter.Realtime);
            timecounter.nexttime(Parameter.TrafficMode);

            MassageList.add(Myrecords.get(i).newmassage(timecounter));
            if (!MassageList.isEmpty())
            {
                for (Massage x:MassageList) if (x.correcttime<timecounter.time) x.upchange(FloorList);
            }//改变概率密度
            int outnum1=mystart.number;
            int outnum2=myend.number;
            System.out.println(i+"出发楼层"+outnum1);
            System.out.println(i+"目标楼层"+outnum2);
            System.out.println("---------------");


            }
        output.setExcel(Myrecords);
        }
    }


