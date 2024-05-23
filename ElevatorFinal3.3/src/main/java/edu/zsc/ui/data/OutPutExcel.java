package edu.zsc.ui.data;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static edu.zsc.ElevatorConfig.outputFile;


public class OutPutExcel {

    public static void createExcel() {
        try {

            HSSFWorkbook wb = new HSSFWorkbook();
            //建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("输出表");

            //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row1 = sheet.createRow(0);
            //设置单元格内容
            row1.createCell(0).setCellValue("时间");
            row1.createCell(1).setCellValue("起始楼层");
            row1.createCell(2).setCellValue("目的楼层");
            row1.createCell(3).setCellValue("人数");
            row1.createCell(4).setCellValue("电梯号");
            row1.createCell(5).setCellValue("电梯等待时间");
            row1.createCell(6).setCellValue("电梯完成时间");

            FileOutputStream output = new FileOutputStream(outputFile);
            wb.write(output);
            System.out.println("创建表成功！");
            output.close();
        } catch (Exception e) {
            System.out.println("数据写入错误！");
            e.printStackTrace();
        }
    }

    public static void writeExcel(int time, int startFloor, int endFloor, int peopleNum, int bestElevator) {
        FileInputStream fs;
        try {
            fs = new FileInputStream(outputFile);
            POIFSFileSystem ps = new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
            HSSFWorkbook wb = new HSSFWorkbook(ps);
            HSSFSheet sheet = wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
            HSSFRow row = sheet.getRow(0);
            int hang = 0;
            if ("".equals(row) || row == null) {
                hang = 0;
            } else {
                hang = sheet.getLastRowNum();
                hang = hang + 1;
            }
            //分别得到最后一行的行号，和一条记录的最后一个单元格
            FileOutputStream out = new FileOutputStream(outputFile);  //向dataFile中写数据
            row = sheet.createRow((short) (hang)); //在现有行号后追加数据
            row.createCell(0).setCellValue(time); //设置第一个（从0开始）单元格的数据
            row.createCell(1).setCellValue(startFloor); //设置第二个（从0开始）单元格的数据
            row.createCell(2).setCellValue(endFloor); //设置第三个（从0开始）单元格的数据
            row.createCell(3).setCellValue(peopleNum); //设置第四个（从0开始）单元格的数据
            row.createCell(4).setCellValue(bestElevator); //设置第五个（从0开始）单元格的数据
            out.flush();
            wb.write(out);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     * @param time 呼梯时间
     * @param startFloor 起始楼层
     * @param endFloor 目的楼层
     * @param peopleNum 人数
     * @param bestElevator 最优电梯号
     * @param waitTime 电梯等待时间
     * @param completeTime 电梯完成时间
     */
    public static void writeExcel(int time, int startFloor, int endFloor, int peopleNum, int bestElevator,double waitTime,double completeTime) {
        FileInputStream fs;
        try {
            fs = new FileInputStream(outputFile);
            POIFSFileSystem ps = new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
            HSSFWorkbook wb = new HSSFWorkbook(ps);
            HSSFSheet sheet = wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
            HSSFRow row = sheet.getRow(0);
            int hang = 0;
            if ("".equals(row) || row == null) {
                hang = 0;
            } else {
                hang = sheet.getLastRowNum();
                hang = hang + 1;
            }
            //分别得到最后一行的行号，和一条记录的最后一个单元格
            FileOutputStream out = new FileOutputStream(outputFile);  //向dataFile中写数据
            row = sheet.createRow((short) (hang)); //在现有行号后追加数据
            row.createCell(0).setCellValue(time); //设置第一个（从0开始）单元格的数据
            row.createCell(1).setCellValue(startFloor); //设置第二个（从0开始）单元格的数据
            row.createCell(2).setCellValue(endFloor); //设置第三个（从0开始）单元格的数据
            row.createCell(3).setCellValue(peopleNum); //设置第四个（从0开始）单元格的数据
            row.createCell(4).setCellValue(bestElevator); //设置第五个（从0开始）单元格的数据
            row.createCell(5).setCellValue(waitTime); //设置第六个（从0开始）单元格的数据
            row.createCell(6).setCellValue(completeTime); //设置第七个（从0开始）单元格的数据
            out.flush();
            wb.write(out);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
