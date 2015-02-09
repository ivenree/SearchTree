import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 * 2-3+ tree implementation.
 *
 * @author wenfeng ren
 * @version Oct 5, 2014
 */
public class Tree
{
    /**
     * The root of the tree.
     */
    Node   root;

    /**
     * update internal pair
     */
    KVPair updateInternalPair;


    // ----------------------------------------------------------
    /**
     * Create a new Tree object.
     */
    //
    public Tree()
    {
        root = null;
    }


    // ----------------------------------------------------------
    /**
     * get root of tree.
     *
     * @return root of tree
     */
    public Node getRoot()
    {
        return root;
    }


    // ----------------------------------------------------------
    /**
     * search method.
     *
     * @param pair
     *            to find
     * @return true if find the pair otherwise false
     */
    public boolean search(KVPair pair)
    {
        Leaf leaf = (Leaf)searchHelp(root, pair);
        if (leaf == null)
        {
            return false;
        }
        return ((leaf.getLPair() != null
            && (leaf.getLPair().compareTo(pair)) == 0)
            || (leaf.getRPair() != null
            && (leaf.getRPair().compareTo(pair) == 0)));

    }


    // ----------------------------------------------------------
    /**
     * Search help method helps find the leaf node.
     *
     * @param node
     *            node of tree/subtree
     * @param pair
     *            to be search in the tree
     * @return true if pair found otherwise false
     */
    public Node searchHelp(Node node, KVPair pair)
    {
        if (node == null)
        {
            return null;
        }
        // Leaf node
        if (node.isLeaf())
        {
            // cast to leaf node
            Leaf leaf = (Leaf)node;
            return leaf;

        }
        // Internal node
        else
        {
            // cast to internal node
            InternalNode internal = (InternalNode)node;
            // only one KVPair
            if (internal.getRPair() == null)
            {
                // goes to left
                if (pair.compareTo(internal.getLPair()) < 0)
                {
                    return searchHelp(internal.getLNode(), pair);
                }
                // goes to center
                else
                {
                    return searchHelp(internal.getMNode(), pair);
                }

            }
            // have two KVPairs
            else
            {
                // goes to left
                if (pair.compareTo(internal.getLPair()) < 0)
                {
                    return searchHelp(internal.getLNode(), pair);
                }
                // goes to center
                else if (pair.compareTo(internal.getLPair()) >= 0
                    && pair.compareTo(internal.getRPair()) < 0)
                {
                    return searchHelp(internal.getMNode(), pair);
                }
                else
                {
                    return searchHelp(internal.getRNode(), pair);
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * insert method.
     *
     * @param pair
     *            to be inserted
     * @return true always
     */
    public boolean insert(KVPair pair)
    {
        // first time insert
        if (root == null)
        {
            root = new Leaf();
            Leaf leaf = (Leaf)root;
            leaf.setLPair(pair);
        }
        //
        else
        {
            InternalNode returnNode = insertHelp(root, pair);
            if (returnNode != null)
            {
                root = returnNode;
            }
        }
        return true;

    }


    // ----------------------------------------------------------
    /**
     * insert help method.
     *
     * @param currentNode
     *            of sub tree
     * @param pair
     *            to be inserted
     * @return promote node
     */
    public InternalNode insertHelp(Node currentNode, KVPair pair)
    {

        // when find the leaf, check left or right to insert
        if (currentNode.isLeaf())
        {

            // cast to leaf.
            Leaf leaf = (Leaf)currentNode;
            if (leaf.getLPair() == null)
            {
                leaf.setLPair(pair);
                return null;
            }

            // check if leaf is full
            if (leaf.getRPair() == null) // Not full two cases
            {
                if (pair.compareTo(leaf.getLPair()) > 0)
                {
                    leaf.setRPair(pair);
                }
                else
                {
                    leaf.setRPair(leaf.getLPair());
                    leaf.setLPair(pair);
                }

                return null;
            }
            else
            // leaf is full
            {
                // create promoted internalNode;
                InternalNode promoteNode = new InternalNode();
                // split old leaf node, create the new leaf node
                Leaf newLeaf = new Leaf();

                // link old leaf and newleaf
                if (leaf.getNext() != null)
                {
                    newLeaf.setNext(leaf.getNext());
                    leaf.getNext().setPrev(newLeaf);
                }
                leaf.setNext(newLeaf);
                newLeaf.setPrev(leaf);

                // check lowest & split leaf node
                if (pair.compareTo(leaf.getLPair()) < 0)
                {
                    // update new leaf
                    newLeaf.setLPair(leaf.getLPair());
                    newLeaf.setRPair(leaf.getRPair());
                    // update old leaf
                    leaf.setLPair(pair);
                    leaf.setRPair(null);
                }
                else if (pair.compareTo(leaf.getRPair()) > 0)
                {
                    //
                    newLeaf.setLPair(leaf.getRPair());
                    newLeaf.setRPair(pair);
                    // update old leaf
                    leaf.setRPair(null);
                }
                else
                {
                    newLeaf.setLPair(pair);
                    newLeaf.setRPair(leaf.getRPair());
                    // update old leaf
                    leaf.setRPair(null);
                }

                // update promoteNode
                promoteNode.setLPair(newLeaf.getLPair());
                promoteNode.setMNode(newLeaf);
                promoteNode.setLNode(leaf);

                return promoteNode;
            }

        }

        // internal node
        else
        {
            // cast to internal node
            InternalNode internal = (InternalNode)currentNode;

            InternalNode returnNode;

            // internal node is not full
            if (internal.getRPair() == null)
            {

                // goes to left, flag = 1
                if (pair.compareTo(internal.getLPair()) <= 0)
                {
                    returnNode = insertHelp(internal.getLNode(), pair);
                    if (returnNode != null)
                    {
                        internal.setRPair(internal.getLPair());
                        internal.setLPair(returnNode.getLPair());
                        internal.setRNode(internal.getMNode());
                        internal.setLNode(returnNode.getLNode());
                        internal.setMNode(returnNode.getMNode());
                    }
                    return null;
                }
                // goes to middle, flag = 2
                else
                {
                    returnNode = insertHelp(internal.getMNode(), pair);
                    if (returnNode != null)
                    {
                        internal.setRPair(returnNode.getLPair());
                        internal.setRNode(returnNode.getMNode());
                    }
                    return null;

                }
            }

            // internal is full
            else
            {

                //
                if (pair.compareTo(internal.getLPair()) < 0)
                {
                    returnNode = insertHelp(internal.getLNode(), pair);

                    if (returnNode == null)
                    {
                        return null;
                    }
                    else
                    {
                        // create promote node
                        InternalNode promoteNode = new InternalNode();
                        // update promote node
                        promoteNode.setLPair(internal.getLPair());
                        promoteNode.setLNode(returnNode);
                        promoteNode.setMNode(internal);
                        // update internal node
                        internal.setLPair(internal.getRPair());
                        internal.setRPair(null);
                        internal.setLNode(internal.getMNode());
                        internal.setMNode(internal.getRNode());
                        internal.setRNode(null);
                        return promoteNode;
                    }

                }

                else if (pair.compareTo(internal.getLPair()) > 0
                    && pair.compareTo(internal.getRPair()) < 0)
                {
                    returnNode = insertHelp(internal.getMNode(), pair);
                    if (returnNode == null)
                    {
                        return null;
                    }
                    else
                    {
                        InternalNode siblingNode = new InternalNode();
                        siblingNode.setLPair(internal.getRPair());
                        siblingNode.setMNode(internal.getRNode());
                        siblingNode.setLNode(returnNode.getMNode());
                        // update internal node
                        internal.setRPair(null);
                        internal.setRNode(null);
                        // promote return node
                        returnNode.setLNode(internal);
                        returnNode.setMNode(siblingNode);
                        return returnNode;
                    }
                }
                else
                {
                    returnNode = insertHelp(internal.getRNode(), pair);
                    if (returnNode == null)
                    {
                        return null;
                    }
                    else
                    {
                        // create promoteNode
                        InternalNode promoteNode = new InternalNode();
                        // update old internal node
                        internal.setRNode(null);
                        promoteNode.setLPair(internal.getRPair());
                        internal.setRPair(null);

                        // update promoteNode
                        promoteNode.setLNode(internal);
                        promoteNode.setMNode(returnNode);
                        return promoteNode;
                    }

                }

            }

        }

    }


    // ----------------------------------------------------------
    /**
     * delete method.
     *
     * @param pair
     *            to be delete
     * @return true if delete otherwise false
     */
    public boolean delete(KVPair pair)
    {
        if (root == null)
        {
            return false;
        }
        else
        {
            Node returnNode = deleteHelp(root, pair);
            if (returnNode != null)
            {
                if (!returnNode.isLeaf())
                {
                    InternalNode internal = (InternalNode)returnNode;
                    if (internal.getMNode() == null)
                    {
                        // update root
                        root = internal.getLNode();
                    }
                }
                else
                {
                    Leaf leaf = (Leaf)returnNode;
                    leaf.setLPair(null);
                }
            }
        }
        return true;
    }


    // ----------------------------------------------------------
    /**
     * delete help method.
     *
     * @param current
     *            node
     * @param pair
     *            to be delete
     * @return merge node or null
     */
    public Node deleteHelp(Node current, KVPair pair)
    {
        if (current.isLeaf())
        {

            // cast to leaf
            Leaf leaf = (Leaf)current;

            if(leaf.getNext()!=null)
            {
                updateInternalPair = leaf.getNext().getLPair();
            }

            // leaf is full
            if (leaf.getRPair() != null)
            {
                // delete without merge
                if (leaf.getLPair().compareTo(pair) == 0)
                {
                    leaf.setLPair(leaf.getRPair());
                    leaf.setRPair(null);
                    return null;
                }
                else
                {
                    leaf.setRPair(null);
                    return null;
                }
            }
            // leaf is not full
            else
            {
                return leaf;
            }

        }
        // internal node
        else
        {
            // cast to internal node
            InternalNode internal = (InternalNode)current;

            // internal is full
            if (internal.getRPair() != null)
            {

                if (pair.compareTo(internal.getLPair()) < 0)
                {
                    //
                    Node returnNode = deleteHelp(internal.getLNode(), pair);
                    if (returnNode == null)
                    {
                        return null;
                    }
                    // need to borrow or merge
                    else
                    {
                        if (returnNode.isLeaf())
                        {
                            // cast to leaf
                            Leaf leaf = (Leaf)returnNode;

                            // updateInternalPair

                            if(leaf.getNext()!=null)
                            {
                                updateInternalPair = leaf.getNext().getLPair();
                            }

                            // goes from right, borrow from right silb
                            if (leaf.getNext().getRPair() != null)
                            {
                                // update leaf and silb
                                leaf.setLPair(leaf.getNext().getLPair());
                                leaf.getNext().setLPair(
                                    leaf.getNext().getRPair());
                                leaf.getNext().setRPair(null);

                                // update internal
                                internal.setLPair(leaf.getNext().getLPair());

                                return null;
                            }
                            // goes from right, cannot borrow, but merge(delete
// found leaf)
                            else
                            {

                                // delete found leaf
                                if (leaf.getPrev() != null)
                                {
                                    leaf.getPrev().setNext(leaf.getNext());
                                    leaf.getNext().setPrev(leaf.getPrev());
                                    leaf = null;
                                }
                                else
                                {
                                    leaf.getNext().setPrev(null);
                                    leaf = null;
                                }
                                // update current internal
                                internal.setLPair(internal.getRPair());
                                internal.setRPair(null);
                                internal.setLNode(internal.getMNode());
                                internal.setMNode(internal.getRNode());
                                internal.setRNode(null);

                                return null;
                            }
                        }

                        // returnNode is internal
                        else
                        {
                            // cast to internal
                            InternalNode returnInternal =
                                (InternalNode)returnNode;

                            // update internal Lpair
                            // internal.setLPair(updateInternalPair);

                            // borrow from right silb
                            if (((InternalNode)(internal.getMNode()))
                                .getRPair() != null)
                            {
                                // update returninternal
                                returnInternal.setLPair(internal.getLPair());
                                returnInternal
                                    .setMNode(((InternalNode)(internal
                                        .getMNode())).getLNode());

                                // update internal
                                internal.setLPair(((InternalNode)(internal
                                    .getMNode())).getLPair());

                                // update silb
                                ((InternalNode)(internal.getMNode()))
                                    .setLPair(((InternalNode)(internal
                                        .getMNode())).getRPair());
                                ((InternalNode)(internal.getMNode()))
                                    .setRPair(null);
                                ((InternalNode)(internal.getMNode()))
                                    .setLNode(((InternalNode)(internal
                                        .getMNode())).getMNode());
                                ((InternalNode)(internal.getMNode()))
                                    .setMNode(((InternalNode)(internal
                                        .getMNode())).getRNode());
                                ((InternalNode)(internal.getMNode()))
                                .setRNode(null);
                                return null;
                            }

                            // cannot borrow, merge needed
                            else
                            {
                                // update returnInternal
                                returnInternal.setLPair(internal.getLPair());
                                returnInternal
                                    .setRPair(((InternalNode)(internal
                                        .getMNode())).getLPair());
                                returnInternal
                                    .setMNode(((InternalNode)(internal
                                        .getMNode())).getLNode());
                                returnInternal
                                    .setRNode(((InternalNode)(internal
                                        .getMNode())).getMNode());

                                // update internal
                                internal.setLPair(internal.getRPair());
                                internal.setRPair(null);
                                internal.setMNode(internal.getRNode());
                                internal.setRNode(null);

                                return null;
                            }
                        }
                    }
                }
                // goes to Middle node
                else if (pair.compareTo(internal.getLPair()) >= 0
                    && pair.compareTo(internal.getRPair()) < 0)
                {

                    // goes to middle
                    Node returnNode = deleteHelp(internal.getMNode(), pair);

                    // update internal: RPair
                    if (internal.getLPair().compareTo(pair) == 0)
                    {
                        internal.setLPair(updateInternalPair);
                    }

                    if (returnNode == null)
                    {
                        if (internal.getMNode().isLeaf())
                        {
                            internal.setLPair(((Leaf)internal.getMNode())
                                .getLPair());
                        }
                        return null;
                    }

                    // need to borrow or merge
                    else if (returnNode.isLeaf())
                    {
                        // cast to leaf
                        Leaf leaf = (Leaf)returnNode;

                        if(leaf.getNext()!=null)
                        {
                            updateInternalPair = leaf.getNext().getLPair();
                        }


                        // goes from middle, borrow from left silb
                        if (leaf.getPrev().getRPair() != null)
                        {
                            // update found leaf and silb
                            leaf.setLPair(leaf.getPrev().getRPair());
                            leaf.getPrev().setRPair(null);

                            // update internal
                            internal.setLPair(leaf.getLPair());

                            return null;
                        }
                        // goes from middle, borrow from right
                        else if (leaf.getNext().getRPair() != null)
                        {
                            // update leaf and silb
                            leaf.setLPair(leaf.getNext().getLPair());
                            leaf.getNext().setLPair(leaf.getNext().getRPair());
                            leaf.getNext().setRPair(null);

                            // update current internal
                            internal.setLPair(leaf.getLPair());
                            internal.setRPair(leaf.getNext().getLPair());
                        }
                        // cannot borrow, but merge(delete found leaf)
                        else
                        {
                            // update leaf and silb

                            leaf.getPrev().setNext(leaf.getNext());
                            leaf.getNext().setPrev(leaf.getPrev());

                            // update internal
                            internal.setLPair(internal.getRPair());
                            internal.setMNode(internal.getRNode());
                            internal.setRPair(null);
                            internal.setRNode(null);

                        }

                        return null;
                    }

                    // returnNode is internal
                    else
                    {
                        // cast to internal
                        InternalNode returnInternal = (InternalNode)returnNode;

                        // borrow from left silb
                        if (((InternalNode)(internal.
                            getLNode())).getRPair() != null)
                        {
                            // update return internal
                            returnInternal.setMNode(returnInternal.getLNode());
                            returnInternal.setLNode(((InternalNode)(internal
                                .getLNode())).getRNode());
                            returnInternal.setLPair(internal.getLPair());

                            // update internal
                            internal.setLPair(((InternalNode)(internal
                                .getLNode())).getRPair());

                            // update right silb
                            ((InternalNode)(internal.getLNode()))
                                .setRPair(null);
                            ((InternalNode)(internal.getLNode()))
                                .setRNode(null);

                            // if (internal.getLPair() == null)
                            // System.out.println("no left pair!!!!!!!!!!");

                            return null;
                        }

                        // borrow from right
                        else if (((InternalNode)(internal.getRNode()))
                            .getRPair() != null)
                        {
                            // update return internal
                            returnInternal.setLPair(internal.getRPair());
                            returnInternal.setMNode(((InternalNode)(internal
                                .getRNode())).getLNode());

                            // update internal
                            internal.setRPair(((InternalNode)(internal
                                .getRNode())).getLPair());

                            // update right silb
                            ((InternalNode)(internal.getRNode()))
                                .setLPair(((InternalNode)(internal.getRNode()))
                                    .getRPair());
                            ((InternalNode)(internal.getRNode()))
                                .setRPair(null);
                            ((InternalNode)(internal.getRNode()))
                                .setLNode(((InternalNode)(internal.getRNode()))
                                    .getMNode());
                            ((InternalNode)(internal.getRNode()))
                                .setMNode(((InternalNode)(internal.getRNode()))
                                    .getRNode());
                            ((InternalNode)(internal.getRNode()))
                                .setRNode(null);

                            return null;

                        }

                        // cannot borrow, merge needed
                        else
                        {
                            // update right silb
                            ((InternalNode)(internal.getLNode()))
                                .setRPair(internal.getLPair());
                            ((InternalNode)(internal.getLNode()))
                                .setRNode(returnInternal.getLNode());

                            // delete returnInternal
                            returnInternal.setLPair(null);
                            returnInternal.setLNode(null);
                            returnInternal.setMNode(null);

                            // internal
                            internal.setLPair(internal.getRPair());
                            internal.setMNode(internal.getRNode());
                            internal.setRPair(null);

                            return null;

                        }
                    }

                }

                // goes to right
                else
                {
                    // call rightnode
                    Node returnNode = deleteHelp(internal.getRNode(), pair);
                    // update internal: RPair
                    if (internal.getRPair().compareTo(pair) == 0)
                    {
                        internal.setRPair(updateInternalPair);
                    }

                    if (returnNode == null)
                    {
                        if (internal.getRNode().isLeaf())
                        {
                            internal.setRPair(((Leaf)internal.getRNode())
                                .getLPair());
                        }

                        return null;
                    }
                    // need to borrow or merge
                    else if (returnNode.isLeaf())
                    {
                        // cast to leaf
                        Leaf leaf = (Leaf)returnNode;

                        if(leaf.getNext()!=null)
                        {
                            updateInternalPair = leaf.getNext().getLPair();
                        }

                        // goes from left, borrow from left silb
                        if (leaf.getPrev().getRPair() != null)
                        {
                            // update leaf and silb
                            leaf.setLPair(leaf.getPrev().getRPair());
                            leaf.getPrev().setRPair(null);

                            // update internal
                            internal.setRPair(leaf.getLPair());

                            return null;
                        }
                        // goes from left, cannot borrow, but merge(delete found
// leaf)
                        else
                        {
                            // delete found leaf
                            if (leaf.getNext() != null)
                            {
                                leaf.getPrev().setNext(leaf.getNext());
                                leaf.getNext().setPrev(leaf.getPrev());
                                leaf = null;
                            }
                            else
                            {
                                leaf.getPrev().setNext(null);
                                leaf = null;
                            }
                            // update current internal
                            internal.setRPair(null);
                            internal.setRNode(null);

                            return null;
                        }
                    }

                    // returnNode is internal
                    else
                    {
                        // cast to internal
                        InternalNode returnInternal = (InternalNode)returnNode;

                        // borrow from left silb
                        if (((InternalNode)(internal.
                            getMNode())).getRPair() != null)
                        {
                            // update returninternal
                            returnInternal.setLPair(internal.getRPair());
                            returnInternal.setMNode(returnInternal.getLNode());
                            returnInternal.setLNode(((InternalNode)(internal
                                .getMNode())).getRNode());

                            // update internal
                            internal.setRPair(((InternalNode)(internal
                                .getMNode())).getRPair());

                            // update silb
                            ((InternalNode)(internal.getMNode()))
                                .setRPair(null);
                            ((InternalNode)(internal.getMNode()))
                                .setRNode(null);

                            return null;
                        }

                        // cannot borrow, merge needed
                        else
                        {
                            // update silb
                            ((InternalNode)(internal.getMNode()))
                                .setRPair(internal.getRPair());
                            ((InternalNode)(internal.getMNode()))
                                .setRNode(returnInternal.getLNode());

                            // update internal
                            internal.setRPair(null);
                            internal.setRNode(null);

                            return null;
                        }
                    }
                }
            }

            // internal is NOT full !!!
            else
            {
                // goes to left
                if (pair.compareTo(internal.getLPair()) < 0)
                {
                    //
                    Node returnNode = deleteHelp(internal.getLNode(), pair);
                    if (returnNode == null)
                    {

                        return null;
                    }
                    else if (returnNode.isLeaf())
                    {

                        // cast to leaf
                        Leaf leaf = (Leaf)returnNode;

                        if(leaf.getNext()!=null)
                        {
                            updateInternalPair = leaf.getNext().getLPair();
                        }

                        // goes from right, borrow from right silb
                        if (leaf.getNext().getRPair() != null)
                        {
                            // update leaf and silb
                            leaf.setLPair(leaf.getNext().getLPair());
                            leaf.getNext().setLPair(leaf.getNext().getRPair());
                            leaf.getNext().setRPair(null);

                            // update internal
                            internal.setLPair(leaf.getNext().getLPair());

                            return null;
                        }

                        // cannot borrow, but merge(delete found leaf)
                        else
                        {
                            leaf.setLPair(null);
                            //
                            if (leaf.getPrev() != null)
                            {
                                leaf.getPrev().setNext(leaf.getNext());
                                leaf.getNext().setPrev(leaf.getPrev());
                                leaf.setNext(null);
                                leaf.setPrev(null);
                            }
                            else
                            {
                                leaf.getNext().setPrev(null);
                                leaf.setNext(null);
                                leaf.setPrev(null);
                            }

                            // update internal
                            internal.setLNode(internal.getMNode());
                            internal.setMNode(null);

                            return internal;
                        }
                    }
                    // returnNode is internal
                    else
                    {
                        // cast to internal
                        InternalNode returnInternal = (InternalNode)returnNode;

                        // borrow from right silb
                        if (((InternalNode)(internal.
                            getMNode())).getRPair() != null)
                        {
                            // update returnInternal
                            returnInternal.setLPair(internal.getLPair());
                            returnInternal.setMNode(((InternalNode)(internal
                                .getMNode())).getLNode());

                            // internal
                            internal.setLPair(((InternalNode)(internal
                                .getMNode())).getLPair());

                            // update right silb
                            ((InternalNode)(internal.getMNode()))
                                .setLPair(((InternalNode)(internal.getMNode()))
                                    .getRPair());
                            ((InternalNode)(internal.getMNode()))
                                .setLNode(((InternalNode)(internal.getMNode()))
                                    .getMNode());
                            ((InternalNode)(internal.getMNode()))
                                .setMNode(((InternalNode)(internal.getMNode()))
                                    .getRNode());
                            ((InternalNode)(internal.getMNode()))
                                .setRPair(null);
                            ((InternalNode)(internal.getMNode()))
                                .setRNode(null);
                        }
                        // cannot borrow, but merge
                        else
                        {
                            // update returnInternal
                            returnInternal.setLPair(internal.getLPair());
                            returnInternal.setRPair(((InternalNode)(internal
                                .getMNode())).getLPair());
                            returnInternal.setMNode(((InternalNode)(internal
                                .getMNode())).getLNode());
                            returnInternal.setRNode(((InternalNode)(internal
                                .getMNode())).getMNode());

                            internal.setMNode(null);

                            return internal;
                        }
                    }
                }

                // goes to middle
                else
                {
                    Node returnNode = deleteHelp(internal.getMNode(), pair);

                    // update internal: LPair
                    if (internal.getLPair().compareTo(pair) == 0)
                    {
                        internal.setLPair(updateInternalPair);
                    }

                    if (returnNode == null)
                    {
                        if (internal.getMNode().isLeaf())
                        {
                            internal.setLPair(((Leaf)internal.getMNode())
                                .getLPair());
                        }

                        return null;
                    }
                    else if (returnNode.isLeaf())
                    {

                        // cast to leaf
                        Leaf leaf = (Leaf)returnNode;

                        // update internal for later use
                        if (leaf.getNext() != null)
                        {
                            updateInternalPair = leaf.getNext().getLPair();
                        }
                        // borrow from left silb
                        if (leaf.getPrev().getRPair() != null)
                        {
                            // update found leaf and silb
                            leaf.setLPair(leaf.getPrev().getRPair());
                            leaf.getPrev().setRPair(null);

                            // update internal
                            internal.setLPair(leaf.getLPair());

                            return null;
                        }

                        // cannot borrow, but merge
                        else
                        {
                            //
                            if (leaf.getNext() != null)
                            {
                                leaf.getPrev().setNext(leaf.getNext());
                                leaf.getNext().setPrev(leaf.getPrev());
                                leaf = null;
                            }
                            else
                            {
                                leaf.getPrev().setNext(null);
                                leaf = null;

                            }

                            // update internal
                            internal.setMNode(null);


                            return internal;
                        }
                    }
                    // return is internal
                    else
                    {
                        // cast to internal
                        InternalNode returnInternal = (InternalNode)returnNode;

                        // borrow from left silb
                        if (((InternalNode)(internal.
                            getLNode())).getRPair() != null)
                        {
                            // update returnInternal
                            returnInternal.setLPair(internal.getLPair());
                            returnInternal.setMNode(returnInternal.getLNode());
                            returnInternal.setLNode(((InternalNode)(internal
                                .getLNode())).getRNode());

                            // internal
                            internal.setLPair(((InternalNode)(internal
                                .getLNode())).getRPair());

                            // update left silb
                            ((InternalNode)(internal.getLNode()))
                                .setRPair(null);
                            ((InternalNode)(internal.getLNode()))
                                .setRNode(null);
                        }
                        // cannot borrow, but merge
                        else
                        {
                            // update left silb
                            ((InternalNode)(internal.getLNode()))
                                .setRPair(internal.getLPair());
                            ((InternalNode)(internal.getLNode()))
                                .setRNode((returnInternal.getLNode()));

                            // update internal
                            internal.setMNode(null);

                            return internal;
                        }
                    }
                }

            }

        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * print tree.
     *
     * @return tree string
     */
    public String print()
    {
        String tree;
        tree = "Printing 2-3 tree:";
        if (root != null)
        {
            if (root.isLeaf())
            {
                Leaf leaf = (Leaf)root;
                if (leaf.getLPair() == null)
                {
                    return tree;
                }
            }

            int level = 0;
            tree += printHelp(root, level);
        }

        return tree;
    }


    // ----------------------------------------------------------
    /**
     * print tree help method.
     *
     * @param current
     *            current node
     * @param level
     *            the level of tree
     * @return treeString to be printed
     */
    public String printHelp(Node current, int level)
    {

        if (current.isLeaf())
        {
            Leaf leaf = (Leaf)current;
            String leafString = "\n";
            for (int i = 0; i < level; i++)
            {
                leafString += "  ";
            }

            leafString +=
                leaf.getLPair().key().getPosition() + " "
                    + leaf.getLPair().value().getPosition();

            if (leaf.getRPair() != null)
            {
                leafString +=
                    " " + leaf.getRPair().key().getPosition() + " "
                        + leaf.getRPair().value().getPosition();
            }

            return leafString;
        }
        else
        {
            // cast
            InternalNode internal = (InternalNode)current;

            if (internal.getRPair() == null)
            {
                String internalString = "\n";
                for (int i = 0; i < level; i++)
                {
                    internalString += "  ";
                }

                internalString +=
                    internal.getLPair().key().getPosition() + " "
                        + internal.getLPair().value().getPosition();

                if (internal.getRPair() != null)
                {
                    internalString +=
                        " " + internal.getRPair().key().getPosition() + " "
                            + internal.getRPair().value().getPosition();
                }

                return internalString
                    + printHelp(internal.getLNode(), level + 1)
                    + printHelp(internal.getMNode(), level + 1);
            }
            else
            {
                String internalString = "\n";
                for (int i = 0; i < level; i++)
                {
                    internalString += "  ";
                }

                internalString +=
                    internal.getLPair().key().getPosition() + " "
                        + internal.getLPair().value().getPosition();

                if (internal.getRPair() != null)
                {
                    internalString +=
                        " " + internal.getRPair().key().getPosition() + " "
                            + internal.getRPair().value().getPosition();
                }

                return internalString
                    + printHelp(internal.getLNode(), level + 1)
                    + printHelp(internal.getMNode(), level + 1)
                    + printHelp(internal.getRNode(), level + 1);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * list all song or artist.
     *
     * @param mem
     *            to get string
     * @param key
     *            that should match
     * @return string outprint
     */
    public ArrayList<String> list(MemManager mem, KVPair key)
    {
        //
        // String string = "";
        ArrayList<String> list = new ArrayList<String>();
        Leaf leaf = (Leaf)searchHelp(root, key);
        while (leaf != null)
        {
            if (leaf.getLPair().key().compareTo(key.key()) == 0)
            {
// string =
// string + "|" + mem.get(leaf.getLPair().value()) + "|\n";
                list.add("|" + mem.get(leaf.getLPair().value()) + "|");
            }

            if (leaf.getRPair() != null
                && leaf.getRPair().key().compareTo(key.key()) == 0)
            {
                // string = string + "|" + mem.get(leaf.getRPair().value()) +
// "|";
                list.add("|" + mem.get(leaf.getRPair().value()) + "|");
            }

            leaf = leaf.getNext();
        }

        // string =
        // return string;
        return list;
    }


    // ----------------------------------------------------------
    /**
     * find leaf node.
     *
     * @param key to find
     * @return the leaf node
     */
    public boolean findKey(KVPair key)
    {
        boolean find = false;
        Leaf leaf = (Leaf)searchHelp(root, key);
        while (leaf != null && leaf.getLPair() != null)
        {
            if (leaf.getLPair().key().compareTo(key.key()) == 0)
            {
                find = true;
            }

            if (leaf.getRPair() != null
                && leaf.getRPair().key().compareTo(key.key()) == 0)
            {
                find = true;
            }

            leaf = leaf.getNext();
        }

        return find;
    }
}
