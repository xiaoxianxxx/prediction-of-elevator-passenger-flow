package edu.zsc.control;

/**
 * @author AJinGo, Ymcc
 */

public enum ElevatorDirection {
    // 电梯向上运行
    UP(1),
    // 电梯向下运行
    DOWN(2),
    // 电梯无任务停止
    STOP(0),
    // 电梯正在某层楼开关门
    OPEN(4);

    public final int value;

    ElevatorDirection(int value) {
        this.value = value;
    }


    /**
     * @return up如果方向为down，down如果方向为up
     */
    public edu.zsc.control.ElevatorDirection invert() {
        switch (edu.zsc.control.ElevatorDirection.this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            default:
                throw new IllegalStateException("Unexpected value: " + edu.zsc.control.ElevatorDirection.this);
        }
    }
}
