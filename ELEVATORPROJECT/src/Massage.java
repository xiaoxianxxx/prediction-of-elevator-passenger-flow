import java.util.List;

public class Massage
{
    double correcttime;//定义一个修改时间
    floor startfloor;
    floor destfloor;//定义起始和目标


    public Massage(double correcttime, floor startfloor, floor destfloor)
    {
        this.correcttime = correcttime;
        this.startfloor = startfloor;
        this.destfloor = destfloor;
    }
//修改 OD矩阵 起始和终止楼层
    public void upchange(List<floor> TheFloorlist)//触发条件就执行，然后修改OD矩阵和层的origin密度
    {

        this.destfloor.origins+=0.04;
        double sumorigins=0;
        for (floor x:TheFloorlist) sumorigins+=x.origins;//
        for (floor x:TheFloorlist) x.possbilities=x.origins/sumorigins;//重新计算possbilities
    }

}
