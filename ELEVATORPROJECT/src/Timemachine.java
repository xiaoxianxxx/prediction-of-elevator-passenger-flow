import java.util.Random;
import java.lang.Math;
public class Timemachine
{
    double time=0;//当前时间
    double arrival;//到达率
    String Realtime;
    int change1=1;
    int change2=1;
    public double getTime()
    {
        return time;
    }

    public void setTime(double time)
    {
        this.time = time;
    }

    public Timemachine(double time, double arrival)
    {
        this.time = time;
        this.arrival = arrival;
        int temp=(int)Math.floor(time);
        int hh = temp / 3600+Parameter.starttime;
        int mm = (temp % 3600) / 60;
        int ss = (temp % 3600) % 60;
        this.Realtime= (hh < 10 ? ("0" + hh) : hh) + ":" +
                (mm < 10 ? ("0" + mm) : mm) + ":" +
                (ss < 10 ? ("0" + ss) : ss);
    }

     public void nexttime(char t)
    {
        Random r=new Random();
        time-=(Math.log(r.nextDouble())/arrival);
        int temp=(int)Math.floor(time);
        int hh = temp / 3600+Parameter.starttime;
        int mm = (temp % 3600) / 60;
        int ss = (temp % 3600) % 60;
        this.Realtime= (hh < 10 ? ("0" + hh) : hh) + ":" +
                (mm < 10 ? ("0" + mm) : mm) + ":" +
                (ss < 10 ? ("0" + ss) : ss);

            if (this.time>=1800&&this.arrival>0.1) this.arrival/=12;
            if (this.time>=36000&&this.arrival<0.1) this.arrival*=12;



    }



}
