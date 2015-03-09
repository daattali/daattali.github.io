public class Direction
{

    public static final Direction NORTH = new Direction(0);
    public static final Direction NORTHEAST = new Direction(45);
    public static final Direction EAST = new Direction(90);
    public static final Direction SOUTHEAST = new Direction(135);
    public static final Direction SOUTH = new Direction(180);
    public static final Direction SOUTHWEST = new Direction(225);
    public static final Direction WEST = new Direction(270);
    public static final Direction NORTHWEST = new Direction(315);
    public static final int FULL_CIRCLE = 360;
    private static final String dirNames[] = {
        "North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest"
    };
    private int dirInDegrees;

    public Direction()
    {
        dirInDegrees = 0;
    }

    public Direction(int degrees)
    {
        dirInDegrees = degrees % 360;
        if(dirInDegrees < 0)
        {
            dirInDegrees += 360;
        }
    }

    public Direction(String str)
    {
        int regionWidth = 360 / dirNames.length;
        for(int k = 0; k < dirNames.length; k++)
        {
            if(str.equalsIgnoreCase(dirNames[k]))
            {
                dirInDegrees = k * regionWidth;
                return;
            }
        }

        throw new IllegalArgumentException("Illegal direction specified: \"" + str + "\"");
    }

    public int inDegrees()
    {
        return dirInDegrees;
    }

    public boolean equals(Object other)
    {
        if(!(other instanceof Direction))
        {
            return false;
        } else
        {
            Direction d = (Direction)other;
            return inDegrees() == d.inDegrees();
        }
    }

    public int hashCode()
    {
        return inDegrees();
    }

    public Direction toRight()
    {
        return new Direction(dirInDegrees + 90);
    }

    public Direction toRight(int deg)
    {
        return new Direction(dirInDegrees + deg);
    }

    public Direction toLeft()
    {
        return new Direction(dirInDegrees - 90);
    }

    public Direction toLeft(int deg)
    {
        return new Direction(dirInDegrees - deg);
    }

    public Direction reverse()
    {
        return new Direction(dirInDegrees + 180);
    }

    public String toString()
    {
        int regionWidth = 360 / dirNames.length;
        if(dirInDegrees % regionWidth == 0)
        {
            return dirNames[dirInDegrees / regionWidth];
        } else
        {
            return dirInDegrees + " degrees";
        }
    }

    public Direction roundedDir(int numDirections, Direction startingDir)
    {
        int degreesFromStartingDir = dirInDegrees - startingDir.inDegrees();
        int regionWidth = 360 / numDirections;
        int numRegions = Math.round((float)degreesFromStartingDir / (float)regionWidth);
        return startingDir.toRight(numRegions * regionWidth);
    }

}
