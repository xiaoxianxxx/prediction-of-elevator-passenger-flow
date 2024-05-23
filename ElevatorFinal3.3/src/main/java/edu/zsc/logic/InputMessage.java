package edu.zsc.logic;

import lombok.Data;

/**
 * 控制层输入电梯状态到算法
 */
@Data
public class InputMessage {
    private byte direction;     //各电梯正在运行，或即将运行方向
    private byte level;         //各电梯当前绝对楼层位置
    private byte[] target;      //各电梯当前已登记的任务楼层(目的楼层)
    private byte lift;          //各电梯号
    private byte limitFloor;    //各电梯当前允许截梯楼层
    private byte[] upFloor;     //各电梯当前向上的登记任务列表（类似于外呼）
    private byte[] downFloor;   //各电梯当前向下的登记任务列表（类似于外呼）

    public byte getDirection() {
        return direction;
    }

    public void setDirection(byte direction) {
        this.direction = direction;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte[] getTarget() {
        return target;
    }

    public void setTarget(byte[] target) {
        this.target = target;
    }

    public byte getLift() {
        return lift;
    }

    public void setLift(byte lift) {
        this.lift = lift;
    }

    public byte getLimitFloor() {
        return limitFloor;
    }

    public void setLimitFloor(byte limitFloor) {
        this.limitFloor = limitFloor;
    }

    public byte[] getUpFloor() {
        return upFloor;
    }

    public void setUpFloor(byte[] upFloor) {
        this.upFloor = upFloor;
    }

    public byte[] getDownFloor() {
        return downFloor;
    }

    public void setDownFloor(byte[] downFloor) {
        this.downFloor = downFloor;
    }




//    private int[] ED;//电梯方向
//    private int[] EF;//电梯楼层
//    private int[] LF;//电梯最近截梯楼层
//    private int[][][] Mission;//电梯任务列表
}
