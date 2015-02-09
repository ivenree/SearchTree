// -------------------------------------------------------------------------
/**
 * Leaf node class which extends the Node class and all Leaf Nodes are linked
 * together.
 *
 * @author ree
 * @version Oct 5, 2014
 */
public class Leaf
    extends Node
{
    //
    private Leaf prev;
    private Leaf next;
    private KVPair lPair;
    private KVPair rPair;


    // ----------------------------------------------------------
    /**
     * Create a new Leaf object.
     *
     */
    public Leaf()
    {
        prev = null;
        next = null;
        lPair = null;
        rPair = null;

    }


    // ----------------------------------------------------------
    /**
     * get prev leaf.
     *
     * @return prev leaf node
     */
    public Leaf getPrev()
    {
        return prev;
    }


    // ----------------------------------------------------------
    /**
     * set prev leaf.
     *
     * @param prevLeaf the prev leaf
     */
    public void setPrev(Leaf prevLeaf)
    {
        this.prev = prevLeaf;
    }


    // ----------------------------------------------------------
    /**
     * get next leaf.
     *
     * @return next leaf node
     */
    public Leaf getNext()
    {
        return next;
    }


    // ----------------------------------------------------------
    /**
     * set next leaf.
     *
     * @param nextLeaf the next leaf
     */
    public void setNext(Leaf nextLeaf)
    {
        this.next = nextLeaf;
    }


    // ----------------------------------------------------------
    /**
     * get leaf pair.
     * @return Left pair
     */
    public KVPair getLPair()
    {
        return lPair;
    }


    // ----------------------------------------------------------
    /**
     * set leat pair.
     * @param lpair to be set
     */
    public void setLPair(KVPair lpair)
    {
        this.lPair = lpair;
    }


    // ----------------------------------------------------------
    /**
     * get right pair.
     * @return Right pair
     */
    public KVPair getRPair()
    {
        return rPair;
    }


    // ----------------------------------------------------------
    /**
     *set right pair.
     * @param rpair to be set
     */
    public void setRPair(KVPair rpair)
    {
        this.rPair = rpair;
    }

 // ----------------------------------------------------------
    /**
     *check is leaf.
     * @return true if leaf
     */
    @Override
    public boolean isLeaf()
    {
        return true;
    }

}
