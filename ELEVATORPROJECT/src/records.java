public class records
{
    int number;
    floor startfloor;
    floor destination;
    double time;
    String Realtime;
    public records(int number, floor startfloor, floor destination, double time,String realtime)
    {
        this.number = number;
        this.startfloor = startfloor;
        this.destination = destination;
        this.time = time;
        this.Realtime=realtime;
    }
    public Massage newmassage(Timemachine t)
    {
        Massage tomodify=new Massage(t.time+this.destination.staytime,this.startfloor,this.destination);//生成一条待修改数据
        return tomodify;//返回该待修改数据
    }
}
