package edu.zsc.ui.run;




import edu.zsc.ui.data.OutPutExcel;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.io.FileInputStream;

import static edu.zsc.ElevatorConfig.dataFile;


/**该类主要用于在某一时间点向仿真系统发送呼梯请求数据
 * */
public class DataToSystem {
    public static int [][] eleData;//呼梯请求的集合
    public static int num;//数据的行数

    //构造函数
    public DataToSystem(int n,int m){// n为数据行数，m为电梯数
        this.eleData = new int[n][m];
    }

    //获取数据
    public  static void getData() {
        jxl.Workbook readwb = null;
        try {
            // 构建Workbook对象, 只读Workbook对象 直接从本地文件创建Workbook
            //上行高峰数据.xls
            //随机数据.xls
            //下行高峰数据.xls
            readwb = Workbook.getWorkbook(new FileInputStream(new File(dataFile)));
            Sheet sheet;

            //获取每个Sheet表
            sheet = readwb.getSheet(0);// Sheet的下标是从0开始,获取每个Sheet表

            int rsColumns = sheet.getColumns();// 获取Sheet表中所包含的总列数
            int rsRows = sheet.getRows();// 获取Sheet表中所包含的总行数
            num = rsRows;
            DataToSystem data =  new DataToSystem(rsRows,rsColumns);

            //数据的读取过程
            for (int i = 0; i < rsColumns; i++) {
                for (int j = 0; j < rsRows; j++) {
                    Cell cell = sheet.getCell(i, j);
                    DataToSystem.eleData[j][i] = Integer.parseInt(cell.getContents());
                }
            }
        } catch (Exception e) {
            System.out.println("文件读取出错,出错的原因有：\n(1)文件名后缀没有为.xls\n(2)代码路径名不正确\n(3)excel的多个表格数据格式、数据数量不统一");
            e.printStackTrace();
        } finally {
            readwb.close();
        }

    }

    //输出数据
    public static void printData(){
        DataToSystem.getData();
        for(int i = 0;i<DataToSystem.num;i++){
            for (int j = 0;j<DataToSystem.eleData[i].length;j++){
                System.out.print(DataToSystem.eleData[i][j]+"  ");
            }
            System.out.println("\n");
        }
    }

//    //将不同时间段的呼梯请求传递给仿真系统
//    public static void DataOfCallElevatorToSystem(){
//        DataToSystem.getData();
//        long startTime1 = System.currentTimeMillis();    //获取开始时间
//        long endTime1;//获取结束时间
//        double time;//程序运行的时间
//        int j=0;
//        while(true) {
//            endTime1 = System.currentTimeMillis();    //获取结束时间
//            time = endTime1 - startTime1;//得到运行到现在为止程序一共运行了多久
//
//            //到程序运行到指定的时间后，执行相应的代码
//            if((time/1000) >= DataToSystem.eleData[j][0]) {
//                DataOfCallElevetor d = new DataOfCallElevetor();
//                d.timeOfCallElevetor = DataToSystem.eleData[j][0];
//                d.startOfCallElevetor = DataToSystem.eleData[j][1];
//                d.endOfCallElevetor = DataToSystem.eleData[j][2];
//                //TODO：将类变量d传递给仿真系统
//
//                d = null;//系统回收类变量d
//                if(j<DataToSystem.num-1)j++;
//                else break;
//            }
//        }
//    }
}
