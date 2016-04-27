package robot;

import java.util.ArrayList;
import java.util.Random;

import static robot.Direction.*;
import static robot.Instruction.*;

public class Robot
{
    private Coordinates position;
    private Direction direction;
    private boolean isLanded;
    private RoadBook roadBook;
    private Battery cell;
    private LandSensor sensor;
    
    /**
     * Energie ideale consommee pour la realisation d'une action.
     */
    private final double energyConsumption;

    public Robot()
    {
        this(1.0, new Battery(), new LandSensor(new Random()));
    }
    
    public Robot(Battery cell, LandSensor sensor)
    {
        this(1.0, cell, sensor);
    }

    public Robot(double energyConsumption, Battery cell, LandSensor sensor)
    {
        this.isLanded = false;
        this.energyConsumption = energyConsumption;
        this.cell = cell;
        this.sensor = sensor;
        
        this.cell.setUp();
    }

    public void land(Coordinates landPosition)
    {
        this.position = landPosition;
        this.direction = NORTH;
        this.isLanded = true;
    }

    public int getXposition() throws UnlandedRobotException
    {
        if(!this.isLanded)
        {
            throw new UnlandedRobotException();
        }
        
        return this.position.getX();
    }

    public int getYposition() throws UnlandedRobotException
    {
        if(!this.isLanded)
        {
            throw new UnlandedRobotException();
        }
        
        return this.position.getY();
    }

    public Direction getDirection() throws UnlandedRobotException
    {
        if(!this.isLanded)
        {
            throw new UnlandedRobotException();
        }
        
        return this.direction;
    }

    /**
     * @throws UnlandedRobotException If the robot hasn't landed yet.
     * @throws InsufficientChargeException If the robot tries doing something it doesn't
     * have enough energy for.
     * @throws InterruptedException If the thread cannot be put to sleep.
     */
    public void moveForward() throws UnlandedRobotException, InsufficientChargeException, InterruptedException
    {
        if(!this.isLanded)
        {
            throw new UnlandedRobotException();
        }
        
        /*
        // MapTools.nextForwardPosition(position, direction);
        position = MapTools.nextForwardPosition(position, direction);
        */
        
        // Use the land sensor to modulate the consumed energy
        Coordinates nextPosition = MapTools.nextForwardPosition(this.position, this.direction);
        double requiredEnergy = this.energyConsumption * this.sensor.getPointToPointEnergyCoefficient(this.position, nextPosition);
        
        // If there isn't enough energy, wait for the battery to charge itself
        if(this.cell.getChargeLevel() < requiredEnergy)
        {
            do
            {
                Thread.sleep(this.cell.timeToSufficientCharge(requiredEnergy));
            }
            while(this.cell.getChargeLevel() < requiredEnergy);
        }
        
        // There is enough energy to move forward
        this.cell.use(requiredEnergy);
        this.position = nextPosition;
    }

    /**
     * @throws UnlandedRobotException If the robot hasn't landed yet.
     * @throws InsufficientChargeException If the robot tries doing something it doesn't
     * have enough energy for.
     * @throws InterruptedException If the thread cannot be put to sleep.
     */
    public void moveBackward() throws UnlandedRobotException, InterruptedException, InsufficientChargeException
    {
        if(!this.isLanded)
        {
            throw new UnlandedRobotException();
        }
        
        /*
        // MapTools.nextForwardPosition(position, direction);
        this.position = MapTools.nextBackwardPosition(this.position, this.direction);
        */
        
        // Use the land sensor to modulate the consumed energy
        Coordinates nextPosition = MapTools.nextBackwardPosition(this.position, this.direction);
        double requiredEnergy = this.energyConsumption * this.sensor.getPointToPointEnergyCoefficient(this.position, nextPosition);
        
        // If there isn't enough energy, wait for the battery to charge itself
        if(this.cell.getChargeLevel() < requiredEnergy)
        {
            do
            {
                Thread.sleep(this.cell.timeToSufficientCharge(requiredEnergy));
            }
            while(this.cell.getChargeLevel() < requiredEnergy);
        }
        
        // There is enough energy to move forward
        this.cell.use(requiredEnergy);
        this.position = nextPosition;
    }

    public void turnLeft() throws UnlandedRobotException
    {
        if(!this.isLanded)
        {
            throw new UnlandedRobotException();
        }
        
        this.direction = MapTools.counterclockwise(this.direction);
    }

    public void turnRight() throws UnlandedRobotException
    {
        if(!this.isLanded)
        {
            throw new UnlandedRobotException();
        }
        
        this.direction = MapTools.clockwise(direction);
    }

    public void setRoadBook(RoadBook roadBook)
    {
        this.roadBook = roadBook;
    }

    /**
     * @throws UnlandedRobotException If the robot hasn't landed yet.
     * @throws InsufficientChargeException If the robot tries doing something it doesn't
     * have enough energy for.
     * @throws InterruptedException If the thread cannot be put to sleep.
     */
    public void letsGo() throws UnlandedRobotException, InsufficientChargeException, InterruptedException
    {
        while(this.roadBook.hasInstruction())
        {
            Instruction nextInstruction = this.roadBook.next();
            
            if(null != nextInstruction)
            {
                switch(nextInstruction)
                {
                    case FORWARD:
                        moveForward();
                        break;
                    case BACKWARD:
                        moveBackward();
                        break;
                    case TURNLEFT:
                        // turnRight();
                        turnLeft();
                        break;
                    case TURNRIGHT:
                        turnRight();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * @throws UnlandedRobotException If the robot hasn't landed yet.
     * @throws InsufficientChargeException If the robot tries doing something it doesn't
     * have enough energy for.
     * @throws InterruptedException If the thread cannot be put to sleep.
     */
    public void moveTo(Coordinates destination) throws UnlandedRobotException, InsufficientChargeException, InterruptedException
    {
        if(!this.isLanded)
        {
            throw new UnlandedRobotException();
        }
        
        RoadBook book = RoadBookCalculator.calculateRoadBook(this.direction, this.position, destination, new ArrayList<>());
        this.setRoadBook(book);
        this.letsGo();
    }
}
