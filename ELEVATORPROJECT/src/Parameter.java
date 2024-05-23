public class Parameter {
    static Character  TrafficMode=new Character('u');//设定交通模式
    static double arrival=0.25;
    static Integer Thishigh=10;
    static Integer AverageStaytime;
    public static String outputFile = "D:\\test.xls";
    public static int starttime=8;
    public static int[] populations=new int[]{15,70,150,100,75,60,90,85,70,50,70,60,60,50,40,30,30,25,25,20};
    public static void sets(Character i,Integer t)
    {
        TrafficMode=i;
        Thishigh=t;
    }

}
