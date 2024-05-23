package edu.zsc.control;

import edu.zsc.ElevatorConfig;
import edu.zsc.logic.Algorithm;
import edu.zsc.logic.InputMessage;
import edu.zsc.logic.OutputMessage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 单例对象用于提供给外部调用
 *
 * @author AJinGo, Ymcc
 */
public class ElevatorManager {

    private final List<Elevator> elevators;

    private final Map<Integer, InputMessage> prevMessages;

    public static ElevatorManager instance = null;

    private final ElevatorFlushProvider flushProvider;

    public static ElevatorManager getInstance() {
        if (instance == null) {
            synchronized (ElevatorManager.class) {
                if (instance == null) {
                    instance = new ElevatorManager();
                }
            }
        }
        return instance;
    }

    private ElevatorManager() {
        if (ElevatorProperty.SPEEDS == null) {
            throw new NullPointerException("未设置电梯速度数组");
        }
        if (ElevatorProperty.STOP_TIMES == null) {
            throw new NullPointerException("未设置电梯停靠时间数组");
        }
        this.prevMessages = new HashMap<>(ElevatorConfig.ELEVATOR_COUNT);
        this.elevators = new ArrayList<>(ElevatorConfig.ELEVATOR_COUNT);
        this.flushProvider = new ElevatorFlushProvider(ElevatorProperty.SPEEDS);
        for (int i = 0; i < ElevatorConfig.ELEVATOR_COUNT; i++) {
            this.elevators.add(new Elevator(i + 1, ElevatorProperty.STOP_TIMES[i]));
        }
        Algorithm.getInstance().setSend(this::handleMessage);
    }

    /**
     * 用于刷新电梯状态
     *
     * @return 电梯详情
     */
    public List<ElevatorDetails> flushState() {
        List<ElevatorDetails> elevatorDetails = new ArrayList<>();
        for (Elevator elevator : elevators) {
            flushProvider.bind(elevator);
            ElevatorDetails details = flushProvider.flush();
            compareMessage(details);
            elevatorDetails.add(details);
            flushProvider.unBind();
        }
        return elevatorDetails;
    }

    /**
     * 获取电梯任务汇总信息
     *
     * @return 电梯详情
     */
    public List<ElevatorTaskSummary> summaryTask() {
        List<ElevatorTaskSummary> result = new ArrayList<>();
        for (Elevator elevator : elevators) {
            List<ElevatorTask> allTasks = elevator.getAllTasks();
            result.addAll(allTasks.stream().map(ElevatorTaskSummary::fromTask)
                    .collect(Collectors.toList()));
        }
        Collections.sort(result);
        return result;
    }


    /**
     * 用于接收任务
     *
     * @param message OutputMessage
     */
    public void handleMessage(OutputMessage message) {
        int id = message.getLift();
        this.elevators.stream()
                .filter(elevator -> elevator.getId() == id)
                .findFirst()
                .ifPresent(elevator -> elevator.acceptTask(ElevatorTask.fromMessage(message)));
    }

    private void compareMessage(ElevatorDetails details) {
        assert details != null;
        int id = details.getId();
        InputMessage message = details.toMessage();
        InputMessage prevMessage = this.prevMessages.get(id);
        boolean hasDifference = !message.equals(prevMessage);
        if (hasDifference) {
            onElevatorStateChange(message);
            this.prevMessages.put(id, message);
        }
    }


    private void onElevatorStateChange(InputMessage inputMessage) {
        Algorithm.getInstance().handleElevatorStateChange(inputMessage);
    }
}
