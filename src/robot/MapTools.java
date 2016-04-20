package robot;

import static robot.Direction.*;

public class MapTools
{
    static Coordinates nextForwardPosition(Coordinates position, Direction direction)
    {
        if(direction == NORTH)
        {
            return new Coordinates(position.getX(), position.getY() + 1);
        }
        if(direction == SOUTH)
        {
            return new Coordinates(position.getX(), position.getY() - 1);
        }
        if(direction == EAST)
        {
            return new Coordinates(position.getX() + 1, position.getY());
        }
        // return new Coordinates(position.getX() + 1, position.getY());
        return new Coordinates(position.getX() - 1, position.getY());
    }

    static Coordinates nextBackwardPosition(Coordinates position, Direction direction)
    {
        if(direction == NORTH)
        {
            return new Coordinates(position.getX(), position.getY() - 1);
        }
        if(direction == SOUTH)
        {
            return new Coordinates(position.getX(), position.getY() + 1);
        }
        if(direction == EAST)
        {
            return new Coordinates(position.getX() - 1, position.getY());
        }
        return new Coordinates(position.getX() + 1, position.getY());
    }

    static Direction counterclockwise(Direction direction)
    {
        if(direction == NORTH)
        {
            return WEST;
        }
        if(direction == WEST)
        {
            return SOUTH;
        }
        if(direction == SOUTH)
        {
            return EAST;
        }
        // return SOUTH;
        return NORTH;
    }

    static Direction clockwise(Direction direction)
    {
        if(direction == NORTH)
        {
            return EAST;
        }
        if(direction == EAST)
        {
            return SOUTH;
        }
        if(direction == SOUTH)
        {
            // return EAST;
            return WEST;
        }
        return NORTH;
    }
}
