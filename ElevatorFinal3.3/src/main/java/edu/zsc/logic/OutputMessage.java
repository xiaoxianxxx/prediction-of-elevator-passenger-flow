package edu.zsc.logic;

import lombok.Data;

/**
 * 算法外层输出给控制层的任务
 */
@Data
public class OutputMessage {
    private byte lift;  //指定派送任务的电梯号
    private byte callFloor; //接载候梯乘客的楼层
    private byte targetFloor; //指定派送任务的目标楼层（绝对楼层）

    public void setLift(byte lift){
        this.lift = lift;
    }//设置指定派送任务的电梯号
    public void setCallFloor(byte callFloor) {
        this.callFloor = callFloor;
    }//设置接载侯悌乘客的楼层
    public void setTargetFloor(byte targetFloor) {
        this.targetFloor = targetFloor;
    }//设置指定派送任务的目标楼层（绝对楼层）

    public byte getLift() {
        return lift;
    }//返回指定派送任务的电梯号
    public byte getCallFloor() {
        return callFloor;
    }//返回接载侯悌乘客的楼层
    public byte getTargetFloor() {
        return targetFloor;
    }//返回指定派送任务的目标楼层（绝对楼层）
}
