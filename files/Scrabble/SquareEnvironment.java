import java.util.ArrayList;

public abstract class SquareEnvironment
    implements Environment
{

    private boolean includeDiagonals;

    public SquareEnvironment()
    {
        includeDiagonals = false;
    }

    public SquareEnvironment(boolean flag)
    {
        includeDiagonals = flag;
    }

    public int numCellSides()
    {
        return 4;
    }

    public int numAdjacentNeighbors()
    {
        return includeDiagonals ? 8 : 4;
    }

    public Direction getDirection(Location location, Location location1)
    {
        if(location.equals(location1))
        {
            return Direction.NORTH;
        } else
        {
            int i = location.row() - location1.row();
            int j = location1.col() - location.col();
            double d = Math.atan2(i, j);
            double d1 = 90D - Math.toDegrees(d);
            Direction direction = new Direction((int)d1);
            return direction.roundedDir(numAdjacentNeighbors(), Direction.NORTH);
        }
    }

    public Location getNeighbor(Location location, Direction direction)
    {
        Direction direction1 = direction.roundedDir(numAdjacentNeighbors(), Direction.NORTH);
        int i = 90 - direction1.inDegrees();
        double d = Math.toRadians(i);
        int j = (int)(Math.cos(d) * Math.sqrt(2D));
        int k = -(int)(Math.sin(d) * Math.sqrt(2D));
        return new Location(location.row() + k, location.col() + j);
    }

    public ArrayList<Location> neighborsOf(Location location)
    {
        ArrayList<Location> arraylist = new ArrayList<Location>();
        Direction direction = Direction.NORTH;
        for(int i = 0; i < numAdjacentNeighbors(); i++)
        {
            Location location1 = getNeighbor(location, direction);
            if(isValid(location1))
            {
                arraylist.add(location1);
            }
            direction = direction.toRight(360 / numAdjacentNeighbors());
        }

        return arraylist;
    }

    public abstract Letter[] allObjects();

    public abstract boolean isValid(Location location);

    public abstract Letter objectAt(Location location);

    public abstract int numObjects();

    public abstract void remove(Letter letter);

    public abstract void add(Letter letter);

    public abstract boolean isEmpty(Location location);
}
