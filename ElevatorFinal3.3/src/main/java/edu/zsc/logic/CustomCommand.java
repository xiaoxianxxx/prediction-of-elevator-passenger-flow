package edu.zsc.logic;

/**
 * 面板向算法外层输入呼梯请求
 */
public class CustomCommand {
    private byte startFloor;  //呼梯楼层
    private byte targetFloor;  //目的楼层
    private byte number;  // 人数

    /*返回呼梯楼层*/
    public byte getStartFloor() {
        return startFloor;
    }

    /*返回目的楼层*/
    public byte getTargetFloor() {
        return targetFloor;
    }

    /*返回人数*/
    public byte getNumber() {
        return number;
    }

    /*设置呼梯楼层*/
    public void setStartFloor(byte startFloor) {
        this.startFloor = startFloor;
    }

    /*设置目的楼层*/
    public void setTargetFloor(byte targetFloor) {
        this.targetFloor = targetFloor;
    }

    /*设置人数*/
    public void setNumber(byte number) {
        this.number = number;
    }

}
