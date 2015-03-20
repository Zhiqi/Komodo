package edu.umich.eecs.cooties;

import android.app.Activity;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import edu.umich.imlc.collabrify.client.CollabrifyClient;
import edu.umich.imlc.collabrify.client.CollabrifySession;

/**
 * Created by mtkliema on 2/24/15.
 */
public class Globals {

    public static String TEACHER_NAME = "T";
    public static String username; /* username that user entered
                                        set in MainActivity.java */
    public static GameState model = new GameState(); // session listener for myclient
    public static CollabrifyClient myclient; // set in MainActivity.java
    public static CollabrifySession mysession; /* joined session
                                                    set in StudentJoinActivity or TeacherActivity_Initialize*/

    public static long selfId; /* participant id
                                    set in StudentJoinActivity */
    public static Hashtable<Short, Long> playerMinors = new Hashtable<Short, Long>(); /* key: minor value: userId (participant id)
                                                            */
    public static Hashtable<Long, PlayerInfo> playerInfo = new Hashtable<Long, PlayerInfo>(); /* key: userId (participant id); value: playerInfo
                                                               */
    public static Hashtable<Short, Long> lastSend = new Hashtable<Short, Long>(); // key: minor, value: timestamp;
    public static Hashtable<Short, Long> rangedBeacons = new Hashtable<Short, Long>(); // key: minor, value: timestamp

    public static ArrayList<HistoryItem> historyList = new ArrayList<HistoryItem>();

    public static Hashtable<Long, ArrayList> touchDict = new Hashtable<Long, ArrayList>(); // key: timestamp, value: list of touch message received

    public static short stickyMinor;

    public static List<String> tags = Arrays.asList("tag61");
    public static Activity studentPlayActivity;

    public static void logUsernameToDisplay(final String line) {
        if (username.equals(TEACHER_NAME)) {
        }
        else {
            Globals.studentPlayActivity.runOnUiThread(new Runnable() {
                public void run() {
                    EditText editText = (EditText) Globals.studentPlayActivity
                            .findViewById(R.id.rangingText);
                    editText.append(line + "\n");
                }
            });

        }
    }
    public static void logMeetingToDisplay(final String line) {
        if (username.equals(TEACHER_NAME)) {
        }
        else {
            Globals.studentPlayActivity.runOnUiThread(new Runnable() {
                public void run() {
                    EditText editText = (EditText) Globals.studentPlayActivity
                            .findViewById(R.id.meetHistory);
                    editText.append(line + "\n");
                }
            });
        }
    }

}

