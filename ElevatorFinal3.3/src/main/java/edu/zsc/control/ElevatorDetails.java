package edu.zsc.control;

import edu.zsc.logic.InputMessage;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 * @author AJinGo, Ymcc
 */
@Data
@Builder(builderClassName = "Builder")
public class ElevatorDetails {

    /**
     * 电梯编号
     */
    private int id;
    /**
     * 电梯运行方向
     */
    private ElevatorDirection direction;
    /**
     * 电梯所在楼层
     */
    private int floor;
    /**
     * 电梯允许截断楼层
     */
    private int limitFloor;
    /**
     * 电梯高度
     */
    private double height;
    /**
     * 电梯当前任务
     */
    private List<ElevatorTask> tasks;

    public InputMessage toMessage() {
        InputMessage message = new InputMessage();
        message.setLift((byte) id);
        message.setDirection((byte) direction.value);
        message.setLevel((byte) (floor + 1));
        message.setLimitFloor((byte) (limitFloor + 1));
        // target
        BitSet bitSet = new BitSet();
        tasks.stream().filter(ElevatorTask::isReceive)
                .forEach(task -> bitSet.set(task.getEndIndex()));
        message.setTarget(fixBitArray(bitSet));

        // upFloor
        bitSet.clear();
        tasks.stream().filter(task -> !task.isReceive() && task.isAscent())
                .forEach(task -> bitSet.set(task.getStartIndex()));
        message.setUpFloor(fixBitArray(bitSet));

        // downFloor
        bitSet.clear();
        tasks.stream().filter(task -> !task.isReceive() && !task.isAscent())
                .forEach(task -> bitSet.set(task.getStartIndex()));
        message.setDownFloor(fixBitArray(bitSet));

        return message;
    }

    private byte[] fixBitArray(BitSet bitSet) {
        byte[] byteArray = bitSet.toByteArray();
        byte[] newByteArray = Arrays.copyOf(byteArray, 8);
        Arrays.fill(newByteArray, byteArray.length, 8, (byte) 0);
        return newByteArray;
    }
}
