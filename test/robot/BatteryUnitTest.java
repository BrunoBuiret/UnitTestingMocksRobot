package robot;

import static org.junit.Assert.*;
import org.junit.Test;

public class BatteryUnitTest
{
    /**
     * Unit test for {@link robot.Battery#charge()}.
     * 
     * The new level of energy after charging should be equal to the computed
     * level in {@link robot.Battery#charge()} : currentLevel * 1.1 + 1.
     */
    @Test
    public void testCharge()
    {
        // Initialize vars
        Battery cell = new Battery();
        
        // Run assertions
        assertEquals(
            "The battery's charge level should have been equal to the one set in " +
            "the constructor.",
            100f,
            cell.getChargeLevel(),
            0);
        
        cell.charge();
        
        assertEquals(
            "The battery's charge level should have been equal to the computed value " +
            "of the initial value.",
            111f,
            cell.getChargeLevel(),
            0
        );
    }
    
    /**
     * Unit test for {@link robot.Battery#setUp()}.
     */
    @Test
    public void testSetUp()
    {
        // Initialize vars
        Battery cell = new Battery();
        
        // Run assertions
        cell.setUp();
        
        try
        {
            // Wait
            Thread.sleep(cell.getChargeTop());
            
            // Then, assert
            assertEquals(
                "The battery's charge level should have been more or less equal " +
                "to the computed value of the initial value.",
                111f,
                cell.getChargeLevel(),
                20
            );
        }
        catch(InterruptedException ex)
        {
            AssertionError error = new AssertionError("Couldn't method setUp().");
            error.addSuppressed(ex);
            
            throw error;
        }
    }

    /**
     * Unit test for {@link robot.Battery#use(double)}.
     * 
     * The energy being consumed is lower than the initial amount of it, so no
     * exception should be thrown and the next level of energy should be right.
     * 
     * @throws InsufficientChargeException 
     */
    @Test
    public void testUseWithEnoughEnergy() throws InsufficientChargeException
    {
        // Initialize vars
        Battery cell = new Battery();
        double energy = 50f;
        
        // Run assertions
        cell.use(energy);
        
        assertEquals(
            "The battery's charge level should have been decreased by the amount " +
            "of used energy.",
            50f,
            cell.getChargeLevel(),
            0
        );
    }
    
    /**
     * Unit test for {@link robot.Battery#use(double)}.
     * 
     * The energy being consumed is way higher than the initial amount of it, so
     * an exception {@link robot.InsufficientChargeException} should be thrown.
     * 
     * @throws InsufficientChargeException 
     */
    @Test(expected=InsufficientChargeException.class)
    public void testUseWithoutEnoughEnergy() throws InsufficientChargeException
    {
        // Initialize vars
        Battery cell = new Battery();
        double energy = 500f;
        
        // Run assertions
        cell.use(energy);
    }
    
    /**
     * Unit test for {@link robot.Battery#timeToSufficientCharge(double)};
     * 
     * The time needed to charge the battery should be correctly computed.
     */
    @Test
    public void testTimeToSufficientCharge()
    {
        // Initialize vars
        Battery cell = new Battery();
        
        // Run assertions
        assertEquals(
            "The computed time of wait isn't valid for the given charge level.",
            2000,
            cell.timeToSufficientCharge(120f)
        );
    }
}
