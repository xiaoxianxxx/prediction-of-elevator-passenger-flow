import java.util.List;
import java.util.Random;

public class Elector
{
    public static int ElectStart(Bulidings B, List<floor> TheFloorList,int high,TrafficParameter T)//建筑物 楼层列表 高度
    {
        Random r=new Random();
      /*  double []origins=new double[high];//建立起始密度向量数组
        for (int i=0;i<high;i++)
        {
            if (i==0) origins[i]=T.U;
            else if(i!=0) origins[i]=(T.I+T.D)*(double)(TheFloorList.get(i).population)/(double)B.population;
        }*/
        //确定起始楼层和目标楼层
        double Sumorigins=0;
        for(floor x:TheFloorList) Sumorigins+=x.origins;//计算总起始密度 步骤一
     //   System.out.println("F="+Sumorigins);
        for(floor x:TheFloorList) x.possbilities=x.origins/Sumorigins;//各楼层的选择概率,这里是公式里的Pi 步骤二
        double []possbility=new double[high];//累计密度 这里是公式里的Qi
        for (int i = 0; i <high; i++)
            for (int j=0;j<=i;j++) possbility[i]+=TheFloorList.get(j).possbilities;//possbility[i]+=origins[j];//步骤三   这里那个公式看不清 感觉应该是这样的
        //for (int i=0;i<high;i++) System.out.println(possbility[i]);//准备//
        // Qi=小于该层的选择概率的累加 计算各个楼层的累计概率
        double R=r.nextDouble();//步骤四  生成一个double随机数选择起始楼层
    //   System.out.println("R="+R);
       // for (int j=0;j<high;j++) System.out.println(possbility[j]);
        int currentF=0;//记录需要找到的楼层R满足 Qi>=r>=Qi-1
        if (R<possbility[0]) currentF=0 ;
        for (int i=1;i<high;i++) if (possbility[i]>=R&&possbility[i-1]<=R) currentF=i;

        return currentF;//返回起始楼层
    }
    public static int ElectDestination(int high,double[][] matrix,int start)
    {
        //步骤一确定起始楼层已完成
        Random r=new Random();
        double[]sumsOD=new double[high];
        for (int i=0;i<high;i++)
        {
            for (int j=0;j<high;j++) sumsOD[i]+=matrix[i][j];//步骤二 计算Fi 这里的sumsOD[i]即为Fi
        }

        double []Possbs=new  double[high];//计算第j层被选为目标的概率

        for (int j=0;j<high;j++) {
            Possbs[j]=matrix[start][j]/sumsOD[start];//Possbs[j]即为Pij，这里的i=start； //步骤三完成od（i,j）/F(i)
        }
        double []Qijs=new  double[high];
        for (int j=0;j<high;j++)
        {
            for (int k=0;k<j;k++) Qijs[j] +=Possbs[k]; //步骤四 Qij=Pij的累加
        }
        int currentFloor=0;
        double ran=r.nextDouble();
        if(ran<Qijs[0]&&start!=0) currentFloor=0;
        for(int i=1;i<high;i++) if (Qijs[i]>=ran&&Qijs[i-1]<ran&&i!=start) currentFloor=i;
        while (currentFloor==start)//这里经常出现随机数满足不了论文中所给条件的情况，文中没说怎么处理的= =所以设置随机到成功为止
        {
            ran=r.nextDouble();
            if(ran<Qijs[0]&&start!=0) currentFloor=0;
            for(int i=1;i<high;i++) if (Qijs[i]>=ran&&Qijs[i-1]<ran&&i!=start) currentFloor=i;
        }

   //     for(int i=0;i<high;i++) System.out.println(Qijs[i]); //准备//
        return currentFloor;  //步骤五



    }


}
