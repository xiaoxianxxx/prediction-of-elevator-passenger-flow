import java.util.Random;

public class floor
{
    public int getPopulation()
    {
        return population;
    }

    public floor(int population,int number,double AverageStaytime)
    {
        Random R=new Random();
        this.population = population;
        this.number=number;
        this.staytime=R.nextDouble()*300+AverageStaytime;
    }

    int population;
    double staytime;// 标记估计的平均停留时间
    double origins=0;// 该楼层的起始密度
    int number;//定义楼层数
    double possbilities=0;//被选走起始楼层的可能性
    public void setOrigins(Bulidings B,TrafficParameter T)
    {
        if (this.number==0) this.origins=T.U;
        else if(this.number!=0) this.origins=(T.I+T.D)*(double)(this.population)/(double)B.population;

    }

    public void setNumber(int number) {
        this.number = number;
    }
}
