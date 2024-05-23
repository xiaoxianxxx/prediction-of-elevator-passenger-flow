package edu.zsc.logic;

/**
 * 算法返回给面板的任务
 */
public class OutputMessageToGUI {
    int target; //电梯号
    Byte startfloor; //起始楼层
    Byte targerfloor;//目的楼层
    Byte number;//人数

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public Byte getStartfloor() {
        return startfloor;
    }

    public void setStartfloor(Byte startfloor) {
        this.startfloor = startfloor;
    }

    public Byte getTargerfloor() {
        return targerfloor;
    }

    public void setTargerfloor(Byte targerfloor) {
        this.targerfloor = targerfloor;
    }

    public Byte getNumber() {
        return number;
    }

    public void setNumber(Byte number) {
        this.number = number;
    }

}
