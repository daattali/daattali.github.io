import java.util.ArrayList;

/**
 *  AP&reg; Computer Science Marine Biology Simulation:<br>
 *  <code>Environment</code> provides an interface for a two-dimensional,
 *  grid-like environment containing locatable objects.  For example,
 *  it could be an environment of fish for a marine biology simulation.
 *
 *  <p>
 *  The <code>Environment</code> interface is
 *  copyright&copy; 2002 College Entrance Examination Board
 *  (www.collegeboard.com).
 *
 *  @author Alyce Brady
 *  @author APCS Development Committee
 *  @version 1 July 2002
 *  @see Direction
 *  @see Locatable
 *  @see Location
 **/

public interface Environment
{

    /** Verifies whether a location is valid in this environment.
     *  @param  loc    location to check
     *  @return <code>true</code> if <code>loc</code> is valid;
     *          <code>false</code> otherwise
     **/
    boolean isValid(Location loc);

    /** Returns the number of sides around each cell.
     *  @return    the number of cell sides in this environment
     **/
    int numCellSides();

    /** Returns the number of adjacent neighbors around each cell.
     *  @return    the number of adjacent neighbors
     **/
    int numAdjacentNeighbors();

    /** Returns the direction from one location to another.
     *  @param  fromLoc       starting location for search
     *  @param  toLoc         destination location
     *  @return direction from <code>fromLoc</code> to <code>toLoc</code>
     **/
    Direction getDirection(Location fromLoc, Location toLoc);

    /** Returns the adjacent neighbor (whether valid or invalid) of a location
     *  in the specified direction.
     *  @param  fromLoc       starting location for search
     *  @param  compassDir    direction in which to look for adjacent neighbor
     *  @return neighbor of <code>fromLoc</code> in given direction
     **/
    Location getNeighbor(Location fromLoc, Direction compassDir);

    /** Returns the adjacent neighbors of a specified location.
     *  Only neighbors that are valid locations in the environment will be
     *  included.
     *  @param  ofLoc       location whose neighbors to get
     *  @return a list of locations that are neighbors of <code>ofLoc</code>
     **/
    ArrayList neighborsOf(Location ofLoc);


  // accessor methods that deal with objects in this environment

    /** Returns the number of objects in this environment.
     *  @return   the number of objects
     **/
    int numObjects();

    /** Returns all the objects in this environment.
     *  @return    an array of all the environment objects
     **/
    Letter[] allObjects();

    /** Determines whether a specific location in this environment is
     *  empty.
     *  @param loc  the location to test
     *  @return     <code>true</code> if <code>loc</code> is a
     *              valid location in the context of this environment
     *              and is empty; <code>false</code> otherwise
     **/
    boolean isEmpty(Location loc);

    /** Returns the object at a specific location in this environment.
     *  @param loc    the location in which to look
     *  @return       the object at location <code>loc</code>;
     *                <code>null</code> if <code>loc</code> is not
     *                in the environment or is empty
     **/
    Letter objectAt(Location loc);


  // modifier methods

    /** Adds a new object to this environment at the location it specifies.
     *  (Precondition: <code>obj.location()</code> is a valid empty location.)
     *  @param obj the new object to be added
     *  @throws    IllegalArgumentException if the precondition is not met
     **/
    void add(Letter obj);

    /** Removes the object from this environment.
     *  (Precondition: <code>obj</code> is in this environment.)
     *  @param obj     the object to be removed
     *  @throws    IllegalArgumentException if the precondition is not met
     **/
    void remove(Letter obj);

}
