package edu.zsc.control;

import edu.zsc.ElevatorConfig;
import edu.zsc.utils.DoubleUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author AJinGo, Ymcc
 */
@Slf4j
public class Elevator {

//    public static final double STOP_TIME = ElevatorConfig.STOP_TIME;

    public static final double EACH_FLOOR_HEIGHT = ElevatorConfig.EACH_FLOOR_HEIGHT;

    public static final int FLOOR_COUNT = ElevatorConfig.FLOOR_COUNT;

    /**
     * 电梯编号
     */
    @Getter
    private final int id;

    /**
     * 当前电梯停靠时间
     */
    private final double stopTime;

    /**
     * 当前电梯所在的高度
     */
    private double height;

    /**
     * 当前电梯的运行方向
     */
    @Getter
    private ElevatorDirection direction;

    /**
     * 电梯主要的方向。
     * 表示电梯运行的持续方向。
     */
    private ElevatorDirection mainDirection;

    /**
     * 电梯的任务列表
     */
    private final List<ElevatorTask> tasks;

    /**
     * 电梯的任务列表（用户记录所有任务）
     */
    private final List<ElevatorTask> allTasks;

    public List<ElevatorTask> getAllTasks() {
        return Collections.unmodifiableList(allTasks);
    }

    /**
     * 电梯还需要停止的时间
     */
    private double waitTime;

    /**
     * 代表电梯的运行时的最高楼层和最低楼层
     */
    private Tuple<Integer, Integer> threshold;

    private static class Tuple<F, S> {
        F min;
        S max;

        public static Tuple<Integer, Integer> create() {
            return new Tuple<>(Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        public Tuple(F min, S max) {
            this.min = min;
            this.max = max;
        }
    }

    public Elevator(int id, double stopTime) {
        this.id = id;
        this.stopTime = stopTime;
        this.direction = ElevatorDirection.STOP;
        this.mainDirection = ElevatorDirection.STOP;
        this.waitTime = 0.0;
        this.tasks = new CopyOnWriteArrayList<>();
        this.allTasks = new CopyOnWriteArrayList<>();
        this.height = 0.0;
        this.threshold = Tuple.create();
    }

    public void afterFlushComplete() {
        for (ElevatorTask task : tasks) {
            if (!task.isReceive()) {
                task.addWaitFlushCount();
            }
            task.addCompleteFlushCount();
        }
    }

    public ElevatorDetails obtainDetails() {
        ElevatorDetails details = new ElevatorDetails.Builder()
                .id(id)
                .direction(obtainOutputDirection())
                .floor(obtainFloor())
                .limitFloor(obtainLimitFloor())
                .height(height)
                .tasks(Collections.unmodifiableList(tasks))
                .build();

        log.info(details.toString());
        log.info("max:{} , min:{}", threshold.max, threshold.min);
        return details;
    }


    /**
     * @return 电梯当前所在的楼层，如果没有到达每层楼对应的实际高度则返回下一层楼
     */
    private int obtainFloor() {
        int floor = computeFloor();
        if (floor == -1) {
            if (direction == ElevatorDirection.UP) {
                floor = (int) Math.floor(height / EACH_FLOOR_HEIGHT);
            } else if (direction == ElevatorDirection.DOWN) {
                floor = (int) Math.ceil(height / EACH_FLOOR_HEIGHT);
            }
//            assert floor < 10 && floor >= 0;
            return floor;
        }
        return floor;
    }

    /**
     * @return 获取需要传送出去的电梯运行方向
     */
    private ElevatorDirection obtainOutputDirection() {
        switch (direction) {
            case UP:
            case DOWN:
            case STOP:
                return direction;
            case OPEN: {
                int floor = computeFloor();
                List<ElevatorTask> tasks = this.tasks
                        .stream().filter(task -> !task.isReceive()).collect(Collectors.toList());
                if (floor == threshold.max) {
                    return tasks.isEmpty() ? ElevatorDirection.STOP : ElevatorDirection.DOWN;
                } else if (floor == threshold.min) {
                    return tasks.isEmpty() ? ElevatorDirection.STOP : ElevatorDirection.UP;
                } else {
                    return mainDirection;
                }
            }
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    /**
     * @return 电梯的截断楼层
     */
    private int obtainLimitFloor() {
        int floor = obtainFloor();
        if (direction == ElevatorDirection.OPEN || direction == ElevatorDirection.STOP) {
            return floor;
        } else {
            if (direction == ElevatorDirection.UP) {
                return Math.min(FLOOR_COUNT, floor + 1);
            } else {
                return Math.max(0, floor - 1);
            }
        }
    }

    /**
     * @param task 接收到的电梯需要执行的任务
     */
    public void acceptTask(ElevatorTask task) {
        if (mainDirection == ElevatorDirection.STOP) {
            mainDirection = task.isAscent() ? ElevatorDirection.UP : ElevatorDirection.DOWN;
        }
        this.tasks.add(task);
        // 确定电梯的运行的最高楼层和最低楼层
        int startIndex = task.getStartIndex(), endIndex = task.getEndIndex();
        if (startIndex >= threshold.max) {
            if (task.isAscent()) {
                threshold.max = endIndex;
            } else {
                threshold.max = startIndex;
            }
        }
        if (startIndex <= threshold.min) {
            if (!task.isAscent()) {
                threshold.min = endIndex;
            } else {
                threshold.min = startIndex;
            }
        }
    }

    /**
     * @return 电梯是否刚好完成等待操作
     */
    public boolean hasCompleteWait() {
        return DoubleUtils.isEqual(waitTime, 0) && direction == ElevatorDirection.OPEN;
    }

    /**
     * 处理电梯在floor层上下乘客的操作
     * 会修改内部 threshold 属性
     *
     * @param floor 电梯所在楼层
     */
    public void handleTasks(int floor) {
        if (floor == -1) {
            throw new IllegalStateException("floor 状态错误 : " + floor);
        }


        // 处理任务方向与主运行方向相同的上下乘客
        List<ElevatorTask> oneWayTasks = obtainTaskByDirection(mainDirection)
                .collect(Collectors.toList());
        List<ElevatorTask> completedTasks = new ArrayList<>();
        for (ElevatorTask task : oneWayTasks) {
            int startIndex = task.getStartIndex(), endIndex = task.getEndIndex();
            // 判断该层楼是否有乘客需要出去
            if (task.isReceive() && endIndex == floor) {
                completedTasks.add(task);
                // todo 记录电梯完成任务的时间
            }
            // 判断该层楼是否有乘客需要进入
            if (!task.isReceive() && startIndex == floor) {
                task.setReceive(true);
                task.setElevatorId(id);
                if (endIndex > threshold.max) {
                    threshold.max = endIndex;
                }
                if (endIndex < threshold.min) {
                    threshold.min = endIndex;
                }
            }
        }

        // 避免出现 ConcurrentModificationException
        oneWayTasks.removeAll(completedTasks);
        tasks.removeAll(completedTasks);
        allTasks.addAll(completedTasks);

        // 判断主运行方向的任务是否已经全部完成
        // 若主运行方向的任务全部完成后，处理换向后的该层楼的上下乘客
        boolean isMax = floor == threshold.max && mainDirection == ElevatorDirection.UP;
        boolean isMin = floor == threshold.min && mainDirection == ElevatorDirection.DOWN;
        if (isMax || isMin) {
            Stream<ElevatorTask> invertWayTasks = obtainTaskByDirection(mainDirection.invert());
            invertWayTasks.filter(task -> task.getStartIndex() == floor)
                    .forEach(task -> {
                        int endIndex = task.getEndIndex();
                        task.setReceive(true);
                        task.setElevatorId(id);
                        if (endIndex > threshold.max) {
                            threshold.max = endIndex;
                        }
                        if (endIndex < threshold.min) {
                            threshold.min = endIndex;
                        }
                    });
        }
        // 判断该任务的endIndex 是否为阈值的
        if (floor == threshold.max) {
            threshold.max = Integer.MIN_VALUE;
        } else if (floor == threshold.min) {
            threshold.min = Integer.MAX_VALUE;
        }
    }

    /**
     * @return 如果height等于对应楼层的高度，则返回对应的楼层， 反之返回-1
     */
    public int computeFloor() {
        int floor = -1;
        for (int i = 0; i < FLOOR_COUNT; i++) {
            if (DoubleUtils.isEqual(this.height, i * EACH_FLOOR_HEIGHT)) {
                floor = i;
                break;
            }
        }
        return floor;
    }


    public void openDoor() {
        this.direction = ElevatorDirection.OPEN;
        this.waitTime = stopTime;
    }

    public void goUp(double height) {
        assert DoubleUtils.isEqual(waitTime, 0);
        this.height += height;
        this.mainDirection = ElevatorDirection.UP;
        this.direction = ElevatorDirection.UP;
    }

    public void goDown(double height) {
        assert DoubleUtils.isEqual(waitTime, 0);
        this.height -= height;
        this.mainDirection = ElevatorDirection.DOWN;
        this.direction = ElevatorDirection.DOWN;
    }

    public void stop() {
        assert DoubleUtils.isEqual(waitTime, 0);
        assert tasks.isEmpty();
        this.mainDirection = ElevatorDirection.STOP;
        this.direction = ElevatorDirection.STOP;
        this.threshold = Tuple.create();
    }

    public void subWaitTime(double timespan) {
        assert !DoubleUtils.isEqual(waitTime, 0);
        this.waitTime -= timespan;
    }

    public ElevatorDirection nextMove() {
        ElevatorDirection nextDirection;
        switch (direction) {
            case UP:
                nextDirection = obtainDirectionFromUp();
                break;
            case DOWN:
                nextDirection = obtainDirectionFromDown();
                break;
            case OPEN:
                nextDirection = obtainDirectionFromOpen();
                break;
            case STOP:
                nextDirection = obtainDirectionFromStop();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        return nextDirection;
    }

    // --------------------------状态函数--------------------------------------

    private ElevatorDirection obtainDirectionFromUp() {
        ElevatorDirection direction;
        int floor = computeFloor();
        if (isNeedOpen(ElevatorDirection.UP, floor)) {
            direction = ElevatorDirection.OPEN;
        } else {
            direction = ElevatorDirection.UP;
        }
        return direction;
    }

    private ElevatorDirection obtainDirectionFromDown() {
        ElevatorDirection direction;
        int floor = computeFloor();
        if (isNeedOpen(ElevatorDirection.DOWN, floor)) {
            direction = ElevatorDirection.OPEN;
        } else {
            direction = ElevatorDirection.DOWN;
        }
        return direction;
    }

    private ElevatorDirection obtainDirectionFromOpen() {
        if (!DoubleUtils.isEqual(waitTime, 0)) {
            return ElevatorDirection.OPEN;
        }

        int floor = computeFloor();
        if (mainDirection == ElevatorDirection.UP) {
            if (floor < threshold.max) {
                return ElevatorDirection.UP;
            }

            if (tasks.isEmpty()) {
                return ElevatorDirection.STOP;
            } else {
                return ElevatorDirection.DOWN;
            }
        } else if (mainDirection == ElevatorDirection.DOWN) {
            if (floor > threshold.min) {
                return ElevatorDirection.DOWN;
            }
            if (tasks.isEmpty()) {
                return ElevatorDirection.STOP;
            } else {
                return ElevatorDirection.UP;
            }
        } else {
            ElevatorTask originTask = findOriginTask();
            return obtainDirectionByTask(originTask);
        }
    }

    private ElevatorDirection obtainDirectionFromStop() {
        ElevatorTask originTask = findOriginTask();
        return obtainDirectionByTask(originTask);
    }

    // --------------------------状态函数--------------------------------------

    /**
     * @return Stop状态下返回最开始加入takes的任务
     */
    private ElevatorTask findOriginTask() {
        return tasks.stream().min(ElevatorTask::compareTo)
                .orElse(ElevatorTask.EMPTY_TASK);
    }


    /**
     * 用户判断电梯在相应楼层是否需要开门上下乘客
     *
     * @param direction 电梯运行方向 只能为UP 或者 DOWN
     * @param floor     电梯当前所在楼层
     * @return floor == -1时返回false
     */
    private boolean isNeedOpen(ElevatorDirection direction, int floor) {
        Stream<ElevatorTask> oneWayTasks = obtainTaskByDirection(direction);
        boolean isMax = floor == threshold.max && direction == ElevatorDirection.UP;
        boolean isMin = floor == threshold.min && direction == ElevatorDirection.DOWN;
        if (isMax || isMin) {
            return isNeedOpenInThreshold(direction, floor);
        }
        return oneWayTasks.anyMatch(task ->
                task.getStartIndex() == floor && !task.isReceive() ||
                        task.getEndIndex() == floor && task.isReceive());
    }

    private boolean isNeedOpenInThreshold(ElevatorDirection direction, int floor) {
        if (direction == ElevatorDirection.UP) {
            return floor == threshold.max;
        } else if (direction == ElevatorDirection.DOWN) {
            return floor == threshold.min;
        }
        return false;
    }


    /***
     * 获取上或下运行方向的任务
     * @param direction 电梯的运行方向
     * @return 一个filter之后的Stream对象
     */
    private Stream<ElevatorTask> obtainTaskByDirection(ElevatorDirection direction) {

        Stream<ElevatorTask> tasks = this.tasks.stream();

        if (direction == ElevatorDirection.UP) {
            tasks = tasks.filter(ElevatorTask::isAscent);
        } else if (direction == ElevatorDirection.DOWN) {
            tasks = tasks.filter(task -> !task.isAscent());
        } else {
            return Stream.empty();
        }
        return tasks;
    }

    /**
     * @param originTask 电梯列表中没有任务的时候，最先加入列表的任务
     * @return 根据电梯任务判断电梯从stop转为的direction
     */
    private ElevatorDirection obtainDirectionByTask(ElevatorTask originTask) {
        if (originTask == ElevatorTask.EMPTY_TASK) {
            return ElevatorDirection.STOP;
        }
        int elevatorFloor = computeFloor();
        int taskStartFloor = originTask.getStartIndex();

        // 判断电梯位置与任务的相对位置
        int diff = elevatorFloor - taskStartFloor;
        ElevatorDirection direction;
        if (diff > 0) {
            direction = ElevatorDirection.DOWN;
        } else if (diff < 0) {
            direction = ElevatorDirection.UP;
        } else {
            direction = ElevatorDirection.OPEN;
        }
        return direction;
    }
}
