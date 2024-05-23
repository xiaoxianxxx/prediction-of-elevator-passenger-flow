import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class output {

    public static void setExcel(List<records> Myrecords)
    {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            //建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("输出表");
            HSSFRow row1 = sheet.createRow(0);
            //设置单元格内容
            row1.createCell(0).setCellValue("序号");
            row1.createCell(1).setCellValue("时间");
            row1.createCell(2).setCellValue("起始楼层");
            row1.createCell(3).setCellValue("目的楼层");
//            row1.createCell(4).setCellValue("起始楼层人数");
//            row1.createCell(5).setCellValue("目的楼层人数");

            for (int ki = 1; ki < Myrecords.size(); ki++) {
                HSSFRow rowt=sheet.createRow(ki);
                rowt.createCell(0).setCellValue(Myrecords.get(ki).number);
                rowt.createCell(1).setCellValue(Myrecords.get(ki).Realtime);
                rowt.createCell(2).setCellValue(Myrecords.get(ki).startfloor.number+1);
                rowt.createCell(3).setCellValue(Myrecords.get(ki).destination.number+1);

            }
            FileOutputStream output = new FileOutputStream(Parameter.outputFile);
            wb.write(output);
        }
        catch (Exception e) {
            System.out.println("已运行 xlCreate() : " + e);
    }


    }
}
