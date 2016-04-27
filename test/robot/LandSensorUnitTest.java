package robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LandSensorUnitTest
{
    /**
     * A list of coordinates to perform tests.
     */
    protected List<Coordinates> coordinatesList = new ArrayList<>();
    
    /**
     * Initializes a list of coordinates to perform tests.
     */
    @Before
    public void setUp()
    {
        // Clear list in case of changes
        this.coordinatesList.clear();
        
        // Pick a point at the center of the grid and one in each part and the meeting lines
        this.coordinatesList.add(new Coordinates(0, 0));
        
        this.coordinatesList.add(new Coordinates(5, 2));
        this.coordinatesList.add(new Coordinates(3, -7));
        this.coordinatesList.add(new Coordinates(-6, 4));
        this.coordinatesList.add(new Coordinates(-1, -8));
        
        this.coordinatesList.add(new Coordinates(0, 9));
        this.coordinatesList.add(new Coordinates(0, -10));
        this.coordinatesList.add(new Coordinates(13, 0));
        this.coordinatesList.add(new Coordinates(-12, 0));
    }
    
    /**
     * Unit test for {@link robot.LandSensor#getPointToPointEnergyCoefficient(robot.Coordinates, robot.Coordinates)}.
     * 
     * The energy coefficient between two equal points can't be computed.
     */
    @Test
    public void testPointToPointEnergyCoefficientEquals()
    {
        // Initialize vars
        LandSensor sensor = new LandSensor(new Random());
        Coordinates coordinates = new Coordinates(5, 5);
        
        //Run assertions
        assertEquals(
            "The energy coefficient between a point and itself should not be an actual number.",
            sensor.getPointToPointEnergyCoefficient(coordinates, coordinates),
            Double.NaN,
            0
        );
    }
    
    /**
     * Unit test for {@link robot.LandSensor#getPointToPointEnergyCoefficient(robot.Coordinates, robot.Coordinates)}.
     * 
     * The energy coefficient between two different points should not be null.
     */
    @Test
    public void testPointToPointEnergyCoefficientDifferent()
    {
        // Initialize vars
        Random randomizer = Mockito.mock(Random.class);
        Mockito.when(randomizer.nextDouble()).thenReturn(0.5);
        LandSensor sensor = new LandSensor(randomizer);
        Coordinates coordinates1 = new Coordinates(0, 0);
        Coordinates coordinates2 = new Coordinates(5, 5);
        
        //Run assertions
        assertEquals(
            "The energy coefficient between the two points should have been ~3.",
            sensor.getPointToPointEnergyCoefficient(coordinates1, coordinates2),
            3,
            0.01
        );
    }
    
    /**
     * Unit test for {@link robot.LandSensor#getPointToPointEnergyCoefficient(robot.Coordinates, robot.Coordinates)}.
     * 
     * The energy coefficient between two different points should be at least 2.
     */
    @Test
    public void testPointToPointEnergyCoefficientNotLessThanTwo()
    {
        // Initialize vars
        LandSensor sensor = new LandSensor(new Random());
        
        // Run assertions
        this.coordinatesList.stream().forEach((c1) ->
        {
            this.coordinatesList.stream().forEach((c2) ->
            {
                if(!c1.equals(c2))
                {
                    assertTrue(
                        String.format(
                            "The energy coefficient between (%d ; %d) and (%d ; %d) " +
                            "is lower than 2.",
                            c1.getX(), c1.getY(),
                            c2.getX(), c2.getY()
                        ),
                        sensor.getPointToPointEnergyCoefficient(c1, c2) >= 2
                    );
                }
            });
        });
    }
    
    /**
     * Unit test for {@link robot.LandSensor#distance(robot.Coordinates, robot.Coordinates)}.
     * 
     * The distance between a point and itself should be null.
     */
    @Test
    public void testDistanceEquals()
    {
        // Initialize vars
        LandSensor sensor = new LandSensor(new Random());
        Coordinates coordinates = new Coordinates(5, 5);
            
        //Run assertions
        assertEquals(
            "The distance between a point and itself should be null.",
            sensor.distance(coordinates, coordinates),
            0,
            0
        );
    }
    
    /**
     * Unit test for {@link robot.LandSensor#distance(robot.Coordinates, robot.Coordinates)}.
     * 
     * The distance between two different points should not be null.
     */
    @Test
    public void testDistanceDifferent()
    {
        // Initialize vars
        LandSensor sensor = new LandSensor(new Random());
        Coordinates coordinates1 = new Coordinates(0, 0);
        Coordinates coordinates2 = new Coordinates(5, 5);
            
        //Run assertions
        assertEquals(
            "The distance between the two points should have been ~7.07.",
            sensor.distance(coordinates1, coordinates2),
            7.07,
            0.01
        );
    }
    
    /**
     * Unit test for {@link robot.LandSensor#distance(robot.Coordinates, robot.Coordinates)}.
     * 
     * The distance between two points should never be negative.
     */
    @Test
    public void testDistanceNotNegative()
    {
        // Initialize vars
        LandSensor sensor = new LandSensor(new Random());
        
        // Run assertions
        this.coordinatesList.stream().forEach((c1) ->
        {
            this.coordinatesList.stream().forEach((c2) ->
            {
                assertTrue(
                    String.format(
                        "The distance between (%d ; %d) and (%d ; %d) is negative.",
                        c1.getX(), c1.getY(),
                        c2.getX(), c2.getY()
                    ),
                    sensor.distance(c1, c2) >= 0
                );
            });
        });
    }
}
