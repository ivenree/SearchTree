
// -------------------------------------------------------------------------
/**
 * This is the Node class.
 *
 * @author wenfeng ren (rwenfeng)
 * @version Sep 7, 2014 the general type for node
 */

public class Block
{
    // data fields
    private int   blockSize = -1;
    private int   position  = -1;
    private Block next      = null;
    private Block prev      = null;


    // ----------------------------------------------------------
    /**
     * Create a new Node object.
     */
    public Block()
    {
        //
    }


    // ----------------------------------------------------------
    /**
     * Create a new Node object with data element.
     *
     * @param blockSize
     *            the size of block
     * @param position
     *            starting position
     */
    public Block(int blockSize, int position)
    {
        this.blockSize = blockSize;
        this.position = position;
    }


    // ----------------------------------------------------------
    /**
     * get the blockSize.
     *
     * @return blockSize
     */
    public int getBlockSize()
    {
        return blockSize;
    }


    // ----------------------------------------------------------
    /**
     * set blockSize.
     *
     * @param blockSize
     *            to be set
     */
    public void setBlockSize(int blockSize)
    {
        this.blockSize = blockSize;
    }


    // ----------------------------------------------------------
    /**
     * get position.
     *
     * @return position
     */
    public int getPosition()
    {
        return position;
    }


    // ----------------------------------------------------------
    /**
     * set position.
     *
     * @param position
     *            to be set
     */
    public void setPosition(int position)
    {
        this.position = position;
    }


    // ----------------------------------------------------------
    /**
     * Get the next node.
     *
     * @return the next node
     */
    public Block next()
    {
        return next;
    }


    // ----------------------------------------------------------
    /**
     * set next node.
     *
     * @param next
     *            to be set
     */
    public void setNext(Block next)
    {
        this.next = next;
    }


    // ----------------------------------------------------------
    /**
     * Get the prev node.
     *
     * @return the prev node
     */
    public Block prev()
    {
        return prev;
    }


    // ----------------------------------------------------------
    /**
     * set the previous node.
     *
     * @param prev
     *            to be set
     */
    public void setPrev(Block prev)
    {
        this.prev = prev;
    }


    // ----------------------------------------------------------
    /**
     * print out the info of the node.
     *
     * @return str
     */
    public String toString()
    {
        String str = "";
        str = "(" + position + "," + blockSize + ")";

        return str;
    }

}
