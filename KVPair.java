/**
 * KVPair class definition: Pretty specific for Project 2
 *
 * @author CS3114 Instructor and TAs
 * @version 9/22/2014
 */
public class KVPair
    implements Comparable<KVPair>
{
    private Handle theKey;
    private Handle theVal;


    /**
     * Constructor
     *
     * @param k
     *            the key (first Handle)
     * @param v
     *            the value (second Handle)
     */
    public KVPair(Handle k, Handle v)
    {
        theKey = k;
        theVal = v;
    }


    /**
     * The magic that lets us compare two KVPairs. KVPairs are all that this
     * knows how to compare against First compare the key field. If they are
     * identical, then break the tie with the value field.
     *
     * @return the ususal for a comparable (+, 0, -)
     * @param it
     *            the KVPair to compare "this" against
     */
    public int compareTo(KVPair it)
    {
        int compKey = theKey.compareTo(it.key());
        return compKey == 0 ? theVal.compareTo(it.value()) : compKey;
    }


    /**
     * Compare a KVPair to a Handle, by comparing theKey to the Handle's
     * position. Note that this relies on Handle having a compareTo method.
     *
     * @return the ususal for a comparable (+, 0, -)
     * @param it
     *            the Handle to compare "this" against
     */
    public int compareTo(Handle it)
    {
        return theKey.compareTo(it);
    }


    /**
     * Getter for "key" Handle
     *
     * @return the key
     */
    public Handle key()
    {
        return theKey;
    }


    /**
     * Getter for "value" Handle
     *
     * @return the value
     */
    public Handle value()
    {
        return theVal;
    }


    /**
     * Standard toString method
     *
     * @return the printable string
     */
    public String toString()
    {
        return theKey.toString() + " " + theVal.toString();
    }

}
