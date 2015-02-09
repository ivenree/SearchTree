
// -------------------------------------------------------------------------
/**
 * handle stores a position information of the record returned by memmanager.
 *
 * @author wenfeng ren (rwenfeng)
 * @version Sep 7, 2014
 */
public class Handle
    implements Comparable<Handle>
{
    // data fields
    /**
     * the position that MemManager returned.
     */
    private int position;


    // ----------------------------------------------------------
    /**
     * Create a new Handle object with initial position.
     *
     * @param position
     *            to be set
     */
    public Handle(int position)
    {
        this.position = position;
    }


    // ----------------------------------------------------------
    /**
     * get the position stored in handle.
     *
     * @return position stores in handle
     */
    public int getPosition()
    {
        return position;
    }


    // ----------------------------------------------------------
    /**
     * set new position for handle.
     *
     * @param newPosition
     *            that needs to be set
     */
    public void setPosition(int newPosition)
    {
        this.position = newPosition;
    }


    // ----------------------------------------------------------
    /**
     * set new position for handle.
     *
     * @param arg
     *            the handle to compare that needs to be set
     * @return 0 or 1 or -1
     */
    public int compareTo(Handle arg)
    {
        if (this.position == arg.getPosition())
        {
            return 0;
        }
        else if (this.position > arg.getPosition())
        {
            return 1;
        }
        else
        {
            return -1;
        }

    }

}
