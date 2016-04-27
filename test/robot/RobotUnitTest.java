package robot;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import org.mockito.Matchers;
import org.mockito.Mockito;

import static robot.Direction.WEST;
import static robot.Direction.EAST;

public class RobotUnitTest
{
    @Test
    public void testLand() throws UnlandedRobotException
    {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        assertEquals(3, robot.getXposition());
        assertEquals(0, robot.getYposition());
    }

    @Test(expected=UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeAnyMove() throws Exception
    {
        Robot robot = new Robot();
        robot.moveForward();
    }

    @Test
    public void testMoveForward() throws UnlandedRobotException, InterruptedException, InsufficientChargeException
    {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        robot.moveForward();
        assertEquals(currentXposition, robot.getXposition());
        assertEquals(currentYposition + 1, robot.getYposition());

    }
    
    @Test
    public void testMoveForwardWithMockedBatteryAndLandSensor() throws UnlandedRobotException, InterruptedException, InsufficientChargeException
    {
        // Mock battery
        Battery cell = Mockito.mock(Battery.class);
        Mockito
            .when(cell.getChargeLevel())
            .thenReturn(100f)
            .thenReturn(0f)
            .thenReturn(100f)
        ;
        
        // Mock land sensor
        LandSensor sensor = Mockito.mock(LandSensor.class);
        Mockito
            .when(sensor.getPointToPointEnergyCoefficient(Mockito.any(Coordinates.class), Mockito.any(Coordinates.class)))
            .thenReturn(1.)
        ;
        
        // Then, create robot and run assertions
        Robot robot = new Robot(cell, sensor);
        
        robot.land(new Coordinates(3, 0));
        
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        
        robot.moveForward();
        
        assertEquals(currentXposition, robot.getXposition());
        assertEquals(currentYposition + 1, robot.getYposition());
        
        robot.moveForward();
        
        Mockito.verify(cell, Mockito.times(1)).timeToSufficientCharge(Matchers.anyDouble());
        assertEquals(currentXposition, robot.getXposition());
        assertEquals(currentYposition + 2, robot.getYposition());
        
        robot.moveForward();
        
        assertEquals(currentXposition, robot.getXposition());
        assertEquals(currentYposition + 3, robot.getYposition());
    }

    @Test
    public void testMoveBackward() throws UnlandedRobotException, InterruptedException, InsufficientChargeException
    {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        robot.moveBackward();
        assertEquals(currentXposition, robot.getXposition());
        assertEquals(currentYposition - 1, robot.getYposition());
    }

    @Test
    public void testTurnLeft() throws UnlandedRobotException
    {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.turnLeft();
        assertEquals(WEST, robot.getDirection());
    }

    @Test
    public void testTurnRight() throws UnlandedRobotException
    {
        Robot robot = new Robot();
        robot.land(new Coordinates(3, 0));
        robot.turnRight();
        assertEquals(EAST, robot.getDirection());
    }

    @Test
    public void testFollowInstruction() throws UnlandedRobotException, InsufficientChargeException, InterruptedException
    {
        Robot robot = new Robot();
        robot.land(new Coordinates(5, 7));
        robot.setRoadBook(new RoadBook(Arrays.asList(Instruction.FORWARD, Instruction.FORWARD, Instruction.TURNLEFT, Instruction.FORWARD)));
        robot.letsGo();
        assertEquals(4, robot.getXposition());
        assertEquals(9, robot.getYposition());
    }
}
