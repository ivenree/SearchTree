import junit.framework.TestCase;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

// -------------------------------------------------------------------------
/**
 * test tree.
 *
 * @author ree
 * @version Oct 9, 2014
 */
public class SearchTreeTest
    extends TestCase
{


    // ----------------------------------------------------------
    /**
     * test main class.
     *
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws IOException
     */
    public void testSearchTree()
        throws IllegalArgumentException,
        IllegalAccessException,
        InvocationTargetException,
        IOException
    {
        String[] strr = { "10", "32"};
        String[] str = { "10", "32", "input.txt" };
        SearchTree.main(str);
        assertEquals(SearchTree.tableSize, 10);
        assertEquals(SearchTree.blockSize, 32);
        //

    }


}
