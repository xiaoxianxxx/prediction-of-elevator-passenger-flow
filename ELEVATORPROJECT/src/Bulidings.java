import java.util.List;
import java.util.Scanner;

public class Bulidings
{
    int population=0;
    int high;
    public int getPopulation()
    {
        return population;
    }

    public Bulidings(List<floor> T,int high1)
    {
        for(floor x:T) this.population+=x.population;
        this.high=high1;
    }
    public int getHigh()
    {
        return high;
    }

    public double[][] setMatrix(List<floor> list,TrafficParameter P)
    {
        double [][]matrix=new double[high][high]; //初始化矩阵
        for (int i=0;i<high;i++)
        {
            for (int j=0;j<high;j++)
            {
                if (i==j) matrix[i][j]=0;
                else if(i==0&&j!=0) matrix[i][j]=(double)(list.get(i).population)/(double)this.population; //从底层到J层的概率矩阵
                else if(i!=0&&j==0) matrix[i][j]=P.D/(P.I+P.D); //从J层下到底层的概率矩阵
                else if(i!=0&&j!=0) matrix[i][j]=P.I/(P.D+P.I)*((double)(list.get(j).population)/(double)this.population);//层间概率
            }
        }
        return  matrix;
    }
   /* public double changeset(double[][]oldmatrix,double []oldorigins) //修改函数，修改起始密度和OD矩阵的值
    {

    }*/
}
