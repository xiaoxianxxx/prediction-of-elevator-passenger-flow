package edu.zsc.control;

import edu.zsc.ElevatorConfig;

/**
 * @author AJinGo, Ymcc
 */
public class ElevatorFlushProvider {

    private final double[] speeds;

    private Elevator elevator;

    public static final double FLUSH_TIME = ElevatorConfig.FLUSH_TIME;

//    public static final double SPEED = ElevatorConfig.SPEED;

    public ElevatorFlushProvider(double[] speeds) {
        this.speeds = speeds;
    }


    public void bind(Elevator elevator) {
        this.elevator = elevator;
    }

    public void unBind() {
        this.elevator = null;
    }

    public ElevatorDetails flush() {
        // 获取电梯的下一次运行方向
        ElevatorDirection direction = elevator.nextMove();
        switch (direction) {
            case UP:
                handleMoveUp();
                break;
            case DOWN:
                handleMoveDown();
                break;
            case OPEN:
                handleOpenDoor();
                break;
            case STOP:
                handleStop();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        elevator.afterFlushComplete();
        return elevator.obtainDetails();
    }

    // --------------------------------状态处理函数-------------------------------------
    // 下列函数会改变电梯的内部属性值

    private void handleMoveUp() {
        double height = speeds[elevator.getId() - 1] * FLUSH_TIME;
        elevator.goUp(height);
    }

    private void handleMoveDown() {
        double height = speeds[elevator.getId() - 1] * FLUSH_TIME;
        elevator.goDown(height);
    }

    private void handleOpenDoor() {
        ElevatorDirection direction = elevator.getDirection();
        if (direction != ElevatorDirection.OPEN) {
            elevator.openDoor();
        } else {
            elevator.subWaitTime(FLUSH_TIME);
            if (elevator.hasCompleteWait()) {
                int floor = elevator.computeFloor();
                elevator.handleTasks(floor);
            }
        }
    }

    private void handleStop() {
        elevator.stop();
    }

    // --------------------------------状态处理函数-------------------------------------
}
