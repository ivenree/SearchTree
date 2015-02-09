
// -------------------------------------------------------------------------
/**
 *  Internal Node calss.
 *
 *  @author wenfeng ren(rwenfeng)
 *  @version Oct 4, 2014
 */
public class InternalNode extends Node
{

    private Node lNode;
    private Node mNode;
    private Node rNode;
    private KVPair lPair;
    private KVPair rPair;

    // ----------------------------------------------------------
    /**
     * Create a new InternaLnode object.
     */
    public InternalNode()
    {
        lNode = null;
        mNode = null;
        rNode = null;
        lPair = null;
        rPair = null;
    }

    // ----------------------------------------------------------
    /**
     * get left pair.
     * @return Left pair
     */
    public KVPair getLPair()
    {
        return lPair;
    }

    // ----------------------------------------------------------
    /**
     * set left pair.
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
     * set right pair.
     * @param rpair to be set
     */
    public void setRPair(KVPair rpair)
    {
        this.rPair = rpair;
    }

    // ----------------------------------------------------------
    /**
     * get left node.
     * @return Left node
     */
    public Node getLNode()
    {
        return lNode;
    }

    // ----------------------------------------------------------
    /**
     * set left node.
     * @param lnode to be set
     */
    public void setLNode(Node lnode)
    {
        this.lNode = lnode;
    }

    // ----------------------------------------------------------
    /**
     * get middle node.
     * @return Middle Node
     */
    public Node getMNode()
    {
        return mNode;
    }

    // ----------------------------------------------------------
    /**
     * set middle node.
     * @param mnode to be set
     */
    public void setMNode(Node mnode)
    {
        this.mNode = mnode;
    }

    // ----------------------------------------------------------
    /**
     * get the right node.
     * @return Right node
     */
    public Node getRNode()
    {
        return rNode;
    }

    // ----------------------------------------------------------
    /**
     * set right node.
     * @param rnode to be set
     */
    public void setRNode(Node rnode)
    {
        this.rNode = rnode;
    }

}
