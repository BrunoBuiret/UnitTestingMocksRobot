package robot;

import java.util.Timer;
import java.util.TimerTask;
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
        assertEquals(100f, cell.getChargeLevel(), 0);
        cell.charge();
        assertEquals(111f, cell.getChargeLevel(), 0);
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
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            //definition de la tache a accomplir
            @Override
            public void run()
            {
                System.out.print("In TimerTask");
                assertEquals(111f, cell.getChargeLevel(), 0);
            }
        }, 1005);
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
        assertEquals(50f, cell.getChargeLevel(), 0);
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
        assertEquals(2000, cell.timeToSufficientCharge(120f));
    }
}
