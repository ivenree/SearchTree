import student.TestCase;

// -------------------------------------------------------------------------
/**
 * test class for tree.
 *
 * @author wenfeng ren
 * @version Oct 12, 2014
 */
public class TreeTest
    extends TestCase
{

    // ----------------------------------------------------------
    /**
     * tree
     */

    Tree   st    = new Tree();

    /**
     * kvpair
     */
    KVPair pair1 = new KVPair(new Handle(100), new Handle(0));


    // ----------------------------------------------------------
    /**
     * test tree.
     */
    public void testTree()
    {
        assertEquals(st.print(), "Printing 2-3 tree:");
        assertEquals(false, st.delete(pair1));

        System.out.println(st.print());
    }

}
