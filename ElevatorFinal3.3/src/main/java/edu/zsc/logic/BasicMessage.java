package edu.zsc.logic;

/**
 * 电梯的基础参数设置
 * topFloor    绝对楼层数 顶楼
 * bottomFloor 绝对楼层数 底楼
 * doorPos     楼层可用门
 * lift        电梯号>=1
 * speed       额定梯速，speed /1000 = 0.000 m/s
 * time        平均停靠时间
 * people      荷载人数
 */
public class BasicMessage {
    byte topFloor;
    byte bottomFloor;
    byte[] doorPos;
    byte lift;
    int speed;
    int time;
    int people;

    public byte getTopFloor() {
        return topFloor;
    }

    public void setTopFloor(byte topFloor) {
        this.topFloor = topFloor;
    }

    public byte getBottomFloor() {
        return bottomFloor;
    }

    public void setBottomFloor(byte bottomFloor) {
        this.bottomFloor = bottomFloor;
    }

    public byte[] getDoorPos() {
        return doorPos;
    }

    public void setDoorPos(byte[] doorPos) {
        this.doorPos = doorPos;
    }

    public byte getLift() {
        return lift;
    }

    public void setLift(byte lift) {
        this.lift = lift;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }
}
