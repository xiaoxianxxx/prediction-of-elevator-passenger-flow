package edu.zsc.control;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static edu.zsc.ElevatorConfig.EACH_FLOOR_HEIGHT;

@RunWith(JUnit4.class)
public class Tests {
    @Test
    public void test() {
        System.out.println((int) Math.floor(19.99999999999996 / EACH_FLOOR_HEIGHT));
        System.out.println(Math.floor(19.99999999999996 / EACH_FLOOR_HEIGHT));
        System.out.println(19.99999999999996 / EACH_FLOOR_HEIGHT);
    }
}
