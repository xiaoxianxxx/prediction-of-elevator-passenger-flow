package edu.zsc.control;

import edu.zsc.ElevatorConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ElevatorTaskSummary implements Comparable<ElevatorTaskSummary> {

    /**
     * 电梯编号
     */
    private int elevatorId;

    /**
     * 电梯等待时间（s）
     */
    private double waitTime;

    /**
     * 电梯完成时间 (s)
     */
    private double completeTime;

    /**
     * 电梯起始楼层
     */
    private int startIndex;

    /**
     * 电梯目的楼层
     */
    private int endIndex;

    /**
     * 电梯开始时间（内部排序使用的）
     */
    private Long startTime;

    public static ElevatorTaskSummary fromTask(ElevatorTask task) {
        ElevatorTaskSummary summary = new ElevatorTaskSummary();

        summary.setElevatorId(task.getElevatorId());
        summary.setStartIndex(task.getStartIndex());
        summary.setEndIndex(task.getEndIndex());

        double completeTime = task.getCompleteFlushCount() * ElevatorConfig.FLUSH_TIME;
        double waitTime = task.getWaitFlushCount() * ElevatorConfig.FLUSH_TIME;
        summary.setCompleteTime(completeTime);
        summary.setWaitTime(waitTime);
        summary.setStartTime(task.getCreateTime());
        return summary;
    }

    @Override
    public int compareTo(ElevatorTaskSummary o) {
        return this.startTime.compareTo(o.startTime);
    }
}
