public class TrafficParameter
{
    double U;
    double I;
    double D;
    public double getD()
    {
        return D;
    }
    public double getU()
    {
        return U;
    }
    public double getI()
    {
        return I;
    }



    public TrafficParameter(char t)
    {
        if(t=='u')
        {
            this.U=0.9;
            this.I=0.45;
            this.D=0.05;
        }
        else if(t=='d')
        {
            this.U=0.05;
            this.I=0.05;
            this.D=0.9;
        }
        else if(t=='i')
        {
            this.D=0.05;
            this.I=0.1;
            this.U=0.05;
        }
    }





}
