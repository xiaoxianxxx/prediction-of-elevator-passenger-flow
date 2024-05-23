package edu.zsc.control;

import edu.zsc.logic.OutputMessage;
import lombok.Data;

/**
 * @author AJinGo, Ymcc
 */
@Data
public class ElevatorTask implements Comparable<ElevatorTask> {

    private int startIndex;
    private int endIndex;
    private boolean isAscent;
    private boolean isReceive;
    private Long createTime;

    private int waitFlushCount;
    private int completeFlushCount;
    private int elevatorId;

    private ElevatorTask(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.isAscent = startIndex < endIndex;
        this.isReceive = false;
        this.createTime = System.currentTimeMillis();
        this.waitFlushCount = 0;
        this.completeFlushCount = 0;
    }

    public static final ElevatorTask EMPTY_TASK = new ElevatorTask(-1, -1);

    public static ElevatorTask of(int startIndex, int endIndex) {
        return new ElevatorTask(startIndex, endIndex);
    }

    public static ElevatorTask fromMessage(OutputMessage message) {
        int startIndex = message.getCallFloor() - 1, endIndex = message.getTargetFloor() - 1;
        return of(startIndex, endIndex);
    }

    @Override
    public int compareTo(ElevatorTask task) {
        return this.createTime.compareTo(task.createTime);
    }

    public void addWaitFlushCount() {
        this.waitFlushCount++;
    }

    public void addCompleteFlushCount() {
        this.completeFlushCount++;
    }
}
