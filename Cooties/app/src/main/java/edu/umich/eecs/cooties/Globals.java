package edu.umich.eecs.cooties;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import edu.umich.imlc.collabrify.client.CollabrifyClient;
import edu.umich.imlc.collabrify.client.CollabrifySession;

/**
 * Created by mtkliema on 2/24/15.
 */
public class Globals {

    public static String username; /* username that user entered
                                        set in MainActivity.java */
    public static GameState model = new GameState(); // session listener for myclient
    public static CollabrifyClient myclient; // set in MainActivity.java
    public static CollabrifySession mysession; /* joined session
                                                    set in StudentJoinActivity */

    public static long selfId; /* participant id
                                    set in StudentJoinActivity */
    public static Hashtable<Short, Long> playerMinors = new Hashtable<Short, Long>();; /* key: minor value: userId (participant id)
                                                            */
    public static Hashtable<Short, PlayerInfo> playerInfo = new Hashtable<Short, PlayerInfo>(); /* key: minor value; value: playerInfo
                                                               */
    public static List<String> tags = Arrays.asList("tag61");

}

