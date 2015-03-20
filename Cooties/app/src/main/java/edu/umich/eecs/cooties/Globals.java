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

    public static String username; /* username that user entered
                                        set in MainActivity.java */
    public static GameState model = new GameState(); // session listener for myclient
    public static CollabrifyClient myclient; // set in MainActivity.java
    public static CollabrifySession mysession; /* joined session
                                                    set in StudentJoinActivity */

    public static long selfId; /* participant id set in StudentJoinActivity */

    public static int major = 0;
    public static String uuid = "F52333CA-84EA-4FE7-9192-1EE0FCE480E9";



    public static ArrayList<Long> infected_user_ids;
    public static long incubation_time;
    public static long hide_health_status;

    public static Boolean infected_status = false;

    /* key: minor value: userId (participant id) */
    public static Hashtable<Short, Long> playerMinors = new Hashtable<Short, Long>();

    /* key: userId (participant id); value: playerInfo */
    public static Hashtable<Long, PlayerInfo> playerInfo = new Hashtable<Long, PlayerInfo>();

    // key: minor, value: timestamp;
    public static Hashtable<Short, Long> lastSend = new Hashtable<Short, Long>();

    // key: minor, value: timestamp
    public static Hashtable<Short, Long> rangedBeacons = new Hashtable<Short, Long>();

    //List of confirmed meetings
    public static ArrayList<HistoryItem> historyList = new ArrayList<HistoryItem>();

    public static Hashtable<Long, ArrayList> touchDict = new Hashtable<Long, ArrayList>(); // key: timestamp, value: list of touch message received

    public static short stickyMinor;

    public static List<String> tags = Arrays.asList("tag61"); //tag identifying cooties+ application on collabrify
    public static Activity studentPlayActivity = null;

    public static TeacherActivity_4_Student_List tal = null;
//    public static Activity teacher_list = null;
    public static Activity teacher_interactions = null;


// key: userId (participant id); value: playername -  added to by collabrifylistener in gamestate for student joins/leaves
    public static Hashtable<Long, String> active_players = new Hashtable<Long, String>();

    public static void logUsernameToDisplay(final String line) {

//        if(studentPlayActivity != null){
            Globals.studentPlayActivity.runOnUiThread(new Runnable() {
                public void run() {
                    EditText editText = (EditText) Globals.studentPlayActivity
                            .findViewById(R.id.rangingText);
                    editText.append(line + "\n");

                }
            });

//        }
//        else if(tal != null){
//            tal.add_additional_player(line);
////                ListView editText = (ListView) Globals.teacher_list.findViewById(R.id.listView);
////            ArrayAdapter<String> codeLearnArrayAdapter = editText.getAdapter();
////                editText.append(line +"\n");
//        }


    }

    public static void logMeetingToDisplay(final String line) {

//        if (studentPlayActivity != null) {
            Globals.studentPlayActivity.runOnUiThread(new Runnable() {
                public void run() {
                    EditText editText = (EditText) Globals.studentPlayActivity
                            .findViewById(R.id.meetHistory);
                    editText.append(line + "\n");
                }
            });

//        }
//        else if(teacher_interactions != null){
//            Globals.teacher_interactions.runOnUiThread(new Runnable() {
//                public void run() {
//                    EditText editText = (EditText) Globals.teacher_interactions
//                            .findViewById(R.id.);
//                    editText.append(line + "\n");
//                }
//            });

//        }
    }

}

