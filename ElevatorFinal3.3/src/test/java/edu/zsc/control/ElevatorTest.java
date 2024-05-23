package edu.zsc.control;

import edu.zsc.logic.OutputMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class ElevatorTest {

    private ElevatorManager manager;

    @Before
    public void setup() {
        ElevatorProperty.STOP_TIMES = new double[]{4.0, 3.0, 2.0};
        ElevatorProperty.SPEEDS = new double[]{4.0, 2.0, 6.0};
        manager = ElevatorManager.getInstance();
    }

    @Test
    public void acceptTaskTest() {

        OutputMessage message1 = new OutputMessage();
        message1.setLift((byte) 1);
        message1.setCallFloor((byte) 1);
        message1.setTargetFloor((byte) 5);

        OutputMessage message2 = new OutputMessage();
        message2.setLift((byte) 1);
        message2.setCallFloor((byte) 7);
        message2.setTargetFloor((byte) 1);

        manager.handleMessage(message1);
        manager.handleMessage(message2);
        for (int i = 0; i < 500; i++) {
            List<ElevatorDetails> elevatorDetails = manager.flushState();
        }
    }

}
