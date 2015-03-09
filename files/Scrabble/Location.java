
public class Location
    implements Comparable
{

    private int myRow;
    private int myCol;

    public Location(int i, int j)
    {
        myRow = i;
        myCol = j;
    }

    public int row()
    {
        return myRow;
    }

    public int col()
    {
        return myCol;
    }

    public boolean isValid()
    {
        return myRow >= 0 && myCol >= 0;
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof Location))
        {
            return false;
        } else
        {
            Location location = (Location)obj;
            return row() == location.row() && col() == location.col();
        }
    }

    public int hashCode()
    {
        return row() * 3737 + col();
    }

    public int compareTo(Object obj)
    {
        Location location = (Location)obj;
        if(equals(obj))
        {
            return 0;
        }
        if(row() == location.row())
        {
            return col() - location.col();
        } else
        {
            return row() - location.row();
        }
    }

    public String toString()
    {
        return (new StringBuilder()).append("(").append(row()).append(", ").append(col()).append(")").toString();
    }
}
