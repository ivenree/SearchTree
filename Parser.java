import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

// -------------------------------------------------------------------------
/**
 * Parser the command file.
 *
 * @author wenfeng ren (rwenfeng)
 * @version Sep 12, 2014
 */
public class Parser
{
    /**
     * the hash table to store song name.
     */
    private Hashtable  songTable;
    /**
     * the hash table to store artist name.
     */
    private Hashtable  artistTable;

    /**
     * memory manager to handle with the memory pool and freeblock list.
     */
    private MemManager manager;

    /**
     *
     */
    private Tree       tree;


    /**
     * Create a new Client object.
     *
     * @param manager
     *            the memory manager that handles memory pool and freeblock list
     * @param tableSize
     *            the initial table size
     * @param fileName
     *            the command file that needs to read
     * @throws FileNotFoundException
     */
    public Parser(MemManager manager, int tableSize, String fileName)
        throws FileNotFoundException
    {
        songTable = new Hashtable(tableSize);
        artistTable = new Hashtable(tableSize);
        // create tree object.
        tree = new Tree();

        this.manager = manager;

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(new File(fileName));
        String command;
        while (scanner.hasNext())
        {
            command = scanner.nextLine();

            // insert command
            if (command.startsWith("insert"))
            {
                String[] content = command.split("insert ");
                content = content[1].split("<SEP>");
                //
                insertToTree(
                    insertArtist(content[0]),
                    content[0],
                    insertSong(content[1]),
                    content[1]);
            }

            // remove song
            else if (command.startsWith("remove song"))
            {
                String[] songName = command.split("remove song ");
                // System.out.println("enter remove: " + songName[1]);
                removeSong(songName[1]);
            }
            // remove artist
            else if (command.startsWith("remove artist"))
            {
                String[] artistName = command.split("remove artist ");
                removeArtist(artistName[1]);
            }
            else if (command.startsWith("print artist"))
            {
                System.out.println(artistTable.print(manager) + "artists: "
                    + artistTable.usedSize());
            }
            else if (command.startsWith("print song"))
            {
                System.out.println(songTable.print(manager) + "songs: "
                    + songTable.usedSize());
            }
            else if (command.startsWith("print blocks"))
            {
                manager.dump();
            }
            // delete tree command
            else if (command.startsWith("delete"))
            {
                String[] content = command.split("delete ");
                content = content[1].split("<SEP>");
                //
                deleteFromTree(content[0], content[1]);
            }
            else if (command.startsWith("list song"))
            {
                String[] songName = command.split("list song ");
                listSong(songName[1]);
            }
            else if (command.startsWith("list artist"))
            {
                String[] artistName = command.split("list artist ");
                listArtist(artistName[1]);
            }
            else if (command.startsWith("print tree"))
            {
                System.out.println(tree.print());
            }

        }
    }


    // ----------------------------------------------------------
    /**
     * insert song name into songTable and memory pool.
     *
     * @param song
     *            that need to be inserted
     * @return song handle
     */
    public Handle insertSong(String song)
    {

        // insert into memory pool and get the handle
        Handle songHandle = songTable.getHandle(song);
        // insert into hash table
        if (songHandle != null)
        {
            System.out.println("|" + song
                + "| duplicates a record already in the song database.");
            return songHandle;
        }
        else
        {
            byte[] songByte = stringToByte(song);
            if (songTable.rehashNeed(song))
            {
                System.out.println("Song hash table size doubled.");
            }
            Handle newHandle = manager.insert(songByte, songByte.length);
            songTable.insert(song, newHandle);
            System.out.println("|" + song + "| is added to the song database.");

            return newHandle;
        }
    }


    // ----------------------------------------------------------
    /**
     * insert artist name into songTable and memory pool.
     *
     * @param artist
     *            to be inserted
     * @return artist handle
     */
    public Handle insertArtist(String artist)
    {
        // check exist
        Handle artistHandle = artistTable.getHandle(artist);
        // insert into hash table
        if (artistHandle != null)
        {
            System.out.println("|" + artist
                + "| duplicates a record already in the artist database.");
            return artistHandle;
        }
        else
        {

            byte[] artistByte = stringToByte(artist);
            if (artistTable.rehashNeed(artist))
            {
                System.out.println("Artist hash table size doubled.");
            }
            Handle newHandle = manager.insert(artistByte, artistByte.length);
            artistTable.insert(artist, newHandle);
            System.out.println("|" + artist
                + "| is added to the artist database.");

            return newHandle;
        }
    }


    // ----------------------------------------------------------
    /**
     * remove song name from hash table.
     *
     * @param song
     *            name
     */
    public void removeSong(String song)
    {
        Handle songHandle = songTable.getHandle(song);
        if (songHandle == null)
        {
            System.out.println("|" + song
                + "| does not exist in the song database.");
        }
        else
        {
            // remove from tree
            KVPair songPair = new KVPair(songHandle, new Handle(-1));
            Leaf leaf = (Leaf)tree.searchHelp(tree.getRoot(), songPair);

            if (leaf.getLPair().key().compareTo(songHandle) != 0
                && leaf.getRPair() != null
                && leaf.getRPair().key().compareTo(songHandle) != 0)
            {
                leaf = leaf.getNext();
            }
            if (leaf.getLPair().key().compareTo(songHandle) != 0
                && leaf.getRPair() == null)
            {
                leaf = leaf.getNext();
            }

            while (leaf != null
                && (leaf.getLPair().key().compareTo(songHandle) == 0 || (leaf
                    .getRPair() != null && leaf.getRPair().key()
                    .compareTo(songHandle) == 0)))
            {
                if (leaf.getLPair().key().compareTo(songHandle) == 0)
                {
                    Handle artistHandle = leaf.getLPair().value();
                    tree.delete(new KVPair(artistHandle, songHandle));
                    tree.delete(new KVPair(songHandle, artistHandle));
                    String artist = manager.get(artistHandle);

                    System.out.println("The KVPair (|" + song + "|,|" + artist
                        + "|) is deleted from the tree.");
                    System.out.println("The KVPair (|" + artist + "|,|" + song
                        + "|) is deleted from the tree.");

                    // update removeSongString
                    KVPair checkArt = new KVPair(artistHandle, new Handle(-1));
                    KVPair checkSong = new KVPair(songHandle, new Handle(-1));
                    if (!tree.findKey(checkSong))
                    {
                        songTable.remove(song);
                        manager.remove(songHandle);
                        System.out.println("|" + song
                            + "| is deleted from the song database.");
                    }
                    if (!tree.findKey(checkArt))
                    {
                        artistTable.remove(artist);
                        manager.remove(artistHandle);
                        System.out.println("|" + artist
                            + "| is deleted from the artist database.");
                    }

                }

                if (leaf.getRPair() != null
                    && leaf.getRPair().key().compareTo(songHandle) == 0)
                {
                    Handle artistHandle = leaf.getRPair().value();
                    tree.delete(new KVPair(artistHandle, songHandle));
                    tree.delete(new KVPair(songHandle, artistHandle));
                    String artist = manager.get(artistHandle);

                    System.out.println("The KVPair (|" + song + "|,|" + artist
                        + "|) is deleted from the tree.");
                    System.out.println("The KVPair (|" + artist + "|,|" + song
                        + "|) is deleted from the tree.");

                    // update removeSongString
                    KVPair checkArt = new KVPair(artistHandle, new Handle(-1));
                    KVPair checkSong = new KVPair(songHandle, new Handle(-1));
                    if (!tree.findKey(checkSong))
                    {
                        songTable.remove(song);
                        manager.remove(songHandle);
                        System.out.println("|" + song
                            + "| is deleted from the song database.");
                    }
                    if (!tree.findKey(checkArt))
                    {
                        artistTable.remove(artist);
                        manager.remove(artistHandle);
                        System.out.println("|" + artist
                            + "| is deleted from the artist database.");
                    }

                }

                leaf = (Leaf)tree.searchHelp(tree.getRoot(), songPair);

                if (leaf.getLPair().key().compareTo(songHandle) != 0)
                {
                    leaf = leaf.getNext();
                }

            }
        }
    }


    // ----------------------------------------------------------
    /**
     * remove artist name from hash table.
     *
     * @param artist
     *            name
     */
    public void removeArtist(String artist)
    {
        Handle artistHandle = artistTable.getHandle(artist);
        if (artistHandle == null)
        {
            System.out.println("|" + artist + "|"
                + " does not exist in the artist database.");
        }
        else
        {
            // remove from tree
            KVPair artistPair = new KVPair(artistHandle, new Handle(-1));
            Leaf leaf = (Leaf)tree.searchHelp(tree.getRoot(), artistPair);

            if (leaf.getLPair().key().compareTo(artistHandle) != 0
                && leaf.getRPair() != null
                && leaf.getRPair().key().compareTo(artistHandle) != 0)
            {
                leaf = leaf.getNext();
            }
            if (leaf.getLPair().key().compareTo(artistHandle) != 0
                && leaf.getRPair() == null)
            {
                leaf = leaf.getNext();
            }

            while (leaf != null
                && (leaf.getLPair().key().compareTo(artistHandle) == 0 || (leaf
                    .getRPair() != null && leaf.getRPair().key()
                    .compareTo(artistHandle) == 0)))
            {

                if (leaf.getLPair().key().compareTo(artistHandle) == 0)
                {
                    Handle songHandle = leaf.getLPair().value();
                    tree.delete(new KVPair(artistHandle, songHandle));
                    tree.delete(new KVPair(songHandle, artistHandle));

                    // get the name of song
                    String song = manager.get(songHandle);

                    System.out.println("The KVPair (|" + artist + "|,|" + song
                        + "|) is deleted from the tree.");
                    System.out.println("The KVPair (|" + song + "|,|" + artist
                        + "|) is deleted from the tree.");

                    // update removeSongString
                    KVPair checkArt = new KVPair(artistHandle, new Handle(-1));
                    KVPair checkSong = new KVPair(songHandle, new Handle(-1));
                    if (!tree.findKey(checkArt))
                    {
                        artistTable.remove(artist);
                        manager.remove(artistHandle);
                        System.out.println("|" + artist
                            + "| is deleted from the artist database.");
                    }
                    if (!tree.findKey(checkSong))
                    {
                        songTable.remove(song);
                        manager.remove(songHandle);
                        System.out.println("|" + song
                            + "| is deleted from the song database.");
                    }
                }

                if (leaf.getRPair() != null
                    && leaf.getRPair().key().compareTo(artistHandle) == 0)
                {
                    Handle songHandle = leaf.getRPair().value();
                    tree.delete(new KVPair(artistHandle, songHandle));

                    tree.delete(new KVPair(songHandle, artistHandle));

                    // get the name of song
                    String song = manager.get(songHandle);

                    System.out.println("The KVPair (|" + artist + "|,|" + song
                        + "|) is deleted from the tree.");
                    System.out.println("The KVPair (|" + song + "|,|" + artist
                        + "|) is deleted from the tree.");

                    // update removeSongString
                    KVPair checkArt = new KVPair(artistHandle, new Handle(-1));
                    KVPair checkSong = new KVPair(songHandle, new Handle(-1));
                    if (!tree.findKey(checkArt))
                    {
                        artistTable.remove(artist);
                        manager.remove(artistHandle);
                        System.out.println("|" + artist
                            + "| is deleted from the artist database.");
                    }
                    if (!tree.findKey(checkSong))
                    {
                        songTable.remove(song);
                        manager.remove(songHandle);
                        System.out.println("|" + song
                            + "| is deleted from the song database.");
                    }
                }

                leaf = (Leaf)tree.searchHelp(tree.getRoot(), artistPair);

                if (leaf.getLPair().key().compareTo(artistHandle) != 0)
                {
                    leaf = leaf.getNext();
                }

            }

        }
    }


    // ----------------------------------------------------------
    /**
     * insert to tree.
     *
     * @param art
     *            artist handle
     * @param artist
     *            name
     * @param sog
     *            song handle
     * @param song
     *            name
     */
    public void insertToTree(Handle art, String artist, Handle sog, String song)
    {
        // check duplicate
        KVPair pair1 = new KVPair(art, sog);
        KVPair pair2 = new KVPair(sog, art);
        boolean find = tree.search(pair1);
        if (find)
        {
            System.out.println("The KVPair (|" + artist + "|,|" + song + "|),("
                + art.getPosition() + "," + sog.getPosition()
                + ") duplicates a record already in the tree.");
            System.out.println("The KVPair (|" + song + "|,|" + artist + "|),("
                + sog.getPosition() + "," + art.getPosition()
                + ") duplicates a record already in the tree.");
        }
        else
        {
            tree.insert(pair1);
            tree.insert(pair2);
            System.out.println("The KVPair (|" + artist + "|,|" + song + "|),("
                + art.getPosition() + "," + sog.getPosition()
                + ") is added to the tree.");
            System.out.println("The KVPair (|" + song + "|,|" + artist + "|),("
                + sog.getPosition() + "," + art.getPosition()
                + ") is added to the tree.");
        }
    }


    // ----------------------------------------------------------
    /**
     * delete pair from tree.
     *
     * @param artist
     *            to be deleted
     * @param song
     *            to be deleted
     */
    public void deleteFromTree(String artist, String song)
    {
        //
        Handle artistHandle = artistTable.getHandle(artist);
        Handle songHandle = songTable.getHandle(song);
        if (artistHandle == null)
        {
            System.out.println("|" + artist + "|"
                + " does not exist in the artist database.");
        }
        else if (songHandle == null)
        {
            System.out.println("|" + song + "|"
                + " does not exist in the song database.");
        }
        else
        {
            // check duplicate
            KVPair pair1 = new KVPair(artistHandle, songHandle);
            KVPair pair2 = new KVPair(songHandle, artistHandle);
            boolean find = tree.search(pair1);
            if (!find)
            {
                System.out.println("The KVPair (|" + artist + "|,|" + song
                    + "|),(" + artistHandle.getPosition() + ","
                    + songHandle.getPosition()
                    + ") does not exist on the tree.");
            }
            else
            {
                tree.delete(pair1);
                tree.delete(pair2);
                System.out.println("The KVPair (|" + artist + "|,|" + song
                    + "|) is deleted from the tree.");
                System.out.println("The KVPair (|" + song + "|,|" + artist
                    + "|) is deleted from the tree.");

                // check if it is last artist or song
                KVPair checkArt = new KVPair(artistHandle, new Handle(-1));
                KVPair checkSong = new KVPair(songHandle, new Handle(-1));

                if (!tree.findKey(checkArt))
                {
                    artistTable.remove(artist);
                    manager.remove(artistHandle);
                    System.out.println("|" + artist
                        + "| is deleted from the artist database.");
                }
                if (!tree.findKey(checkSong))
                {
                    songTable.remove(song);
                    manager.remove(songHandle);
                    System.out.println("|" + song
                        + "| is deleted from the song database.");
                }
            }
        }

    }


    // ----------------------------------------------------------
    /**
     * list all song's artists.
     *
     * @param song
     *            will be list
     */
    public void listSong(String song)
    {
        //
        Handle songHandle = songTable.getHandle(song);
        if (songHandle == null)
        {
            System.out.println("|" + song
                + "| does not exist in the song database.");
        }
        else
        {
            // find song and create KVPair
            KVPair pair = new KVPair(songHandle, new Handle(-1));
            // System.out.println(tree.list(manager, pair));
            ArrayList<String> printlist = tree.list(manager, pair);
            for (int i = 0; i < printlist.size(); i++)
            {
                System.out.println(printlist.get(i));
            }
            // System.out.print(printlist.get(printlist.size() - 1));
        }
    }


    // ----------------------------------------------------------
    /**
     * list all artist's songs.
     *
     * @param artist
     *            to be list
     */
    public void listArtist(String artist)
    {
        //
        //
        Handle artistHandle = artistTable.getHandle(artist);
        if (artistHandle == null)
        {
            System.out.println("|" + artist
                + "| does not exist in the artist database.");
        }
        else
        {
            // find artist and create KVPair
            KVPair pair = new KVPair(artistHandle, new Handle(-1));
            // System.out.println(tree.list(manager, pair));
            // find song and create KVPair
            ArrayList<String> printlist = tree.list(manager, pair);
            for (int i = 0; i < printlist.size(); i++)
            {
                System.out.println(printlist.get(i));
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * listTree(songName[1]); string to byte method.
     *
     * @param name
     *            needs to convert to String
     * @return data array
     */
    public static byte[] stringToByte(String name)
    {
        byte[] data = new byte[name.length()];

        try
        {
            ByteBuffer.wrap(data).put(name.getBytes("US-ASCII"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return data;
    }

}
