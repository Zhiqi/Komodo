package edu.umich.eecs.cooties;

import android.provider.Settings;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import edu.umich.imlc.collabrify.client.CollabrifyClient;
import edu.umich.imlc.collabrify.client.CollabrifySession;

/**
 * Created by mtkliema on 2/24/15.
 */
public class GlobalSingleton {

    public static final String login_email = "imlcteam@gmail.com";
    public static final String login_id = "2468";
    public static final List<String> tags = Arrays.asList("tag61"); //tag identifying cooties+ application on collabrify
    public static final String TEACHER_NAME = "T";
    public static final UUID bt_uuid = UUID.fromString("f52333ca-84ea-4fe7-9192-1ee0fce480e9");

    private static GlobalSingleton mInstance = null;

    public  String username; /* username that user entered set in MainActivity.java */
    public  GameState model; // session listener for myclient
    public  CollabrifyClient myclient; // set in MainActivity.java
    public  CollabrifySession mysession; /* joined session set in StudentJoinActivity or TeacherActivity_Initialize*/
    public  long selfId; /* participant id set in StudentJoinActivity */

    public  StudentPlayActivity studentPlayActivity;
    public  Student_Intersitital_Activity_2 interstitial_wait;
    public  Boolean base_received;

    //MESSAGE STUFF

    //BASEFILE ATTRIBUTES STUFF-------------------------------------------------------------------
    public  ArrayList<Long> initial_infected_user_ids;
    public  long incubation_time;
    public  long hide_health_status;
    public  Boolean infected_status;


    //NEW PARTICIPANT MESSAGE STUFF-------------------------------------------------------------------
    /* key: minor value: userId (participant id)
     * set in acquiredNewParticipant announce message
     * used by bluetooth listener*/
    public  Hashtable<Short, Long> playerMinors;
    /* key: userId (participant id); value: playerInfo
    * set in acuiredNewParticipant annouce message
    * */
    public  Hashtable<Long, PlayerInfo> playerInfo;
    // key: userId (participant id); value: playername -  added to by collabrifylistener in gamestate for student joins/leaves
    //not set by new participant message
    public  Hashtable<Long, String> active_players;


    //CHECKTOUCH MESSAGE STUFF-------------------------------------------------------------------
    //set in checktouch
    // key: timestamp, value: list of touch message received
    public  Hashtable<Long, ArrayList> touchDict;


    //TOUCH EVENT HELPER STUFF-------------------------------------------------------------------
    //List of confirmed meetings
    //Set in toucheventhelper
    public  ArrayList<HistoryItem> historyList;

    /* key: userId value: true for infected, false for not infected (default)
    * set in touchevent helper and basefilemessage, default false for not infected
    * IGNORE: FOR TESTING ONLY : ONLY CONSIDER MSG INFECTION STATUS*/
    public  Hashtable<Long, Boolean> infected_status_by_player;


    //BLUETOOTH STUFF-------------------------------------------------------------------
    //major included in basefile message
    //could set independ. based on session id
    public  int major;

    // key: minor, value: timestamp;
    // Set/checked in scanForSignficantConnection , beacons ()
    public  Hashtable<Short, Long> lastSend;

    // Set/checked in scanForSignficantConnection , beacons ()
    // key: minor, value: timestamp
    public  Hashtable<Short, Long> rangedBeacons;

    //used in searchbeacon
    public  short stickyMinor;


//    private String mString;

    private GlobalSingleton(){
        username = null;
        selfId = 0;
        myclient = null;
        mysession = null;
        model = new GameState();
        studentPlayActivity = null;
        interstitial_wait = null;
        base_received = false;
        initial_infected_user_ids = new ArrayList<Long>();
        incubation_time = 0;
        hide_health_status = 1;
        infected_status = false;
        playerMinors = new Hashtable<Short, Long>();
        playerInfo = new Hashtable<Long, PlayerInfo>();
        active_players = new Hashtable<Long, String>();
        touchDict = new Hashtable<Long, ArrayList>();
        historyList = new ArrayList<HistoryItem>();
        infected_status_by_player = new Hashtable<Long, Boolean>();
        major = 0;
        lastSend = new Hashtable<Short, Long>();
        rangedBeacons = new Hashtable<Short, Long>();
        stickyMinor = 0;
    }

    public static GlobalSingleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new GlobalSingleton();
        }
        return mInstance;
    }

//    public String getString(){
//        return this.mString;
//    }
//
//    public void setString(String value){
//        mString = value;
//    }

    public void resetInstance(){
        username = null;
        selfId = 0;
        myclient = null;
        mysession = null;
        model = new GameState();
        studentPlayActivity = null;
        interstitial_wait = null;
        base_received = false;
        initial_infected_user_ids = new ArrayList<Long>();
        incubation_time = 0;
        hide_health_status = 1;
        infected_status = false;
        playerMinors = new Hashtable<Short, Long>();
        playerInfo = new Hashtable<Long, PlayerInfo>();
        active_players = new Hashtable<Long, String>();
        touchDict = new Hashtable<Long, ArrayList>();
        historyList = new ArrayList<HistoryItem>();
        infected_status_by_player = new Hashtable<Long, Boolean>();
        major = 0;
        lastSend = new Hashtable<Short, Long>();
        rangedBeacons = new Hashtable<Short, Long>();
        stickyMinor = 0;
    }







//    public static void logUsernameToDisplay(final String line) {
//        if (username.equals(TEACHER_NAME)) {
//        }
//        else {
//            if(GlobalSingleton.studentPlayActivity != null){
//                GlobalSingleton.studentPlayActivity.runOnUiThread(new Runnable() {
//                    public void run() {
//                        EditText editText = (EditText) GlobalSingleton.studentPlayActivity
//                                .findViewById(R.id.rangingText);
//                        editText.append(line + "\n");
//                    }
//                });
//
//            }
//
//
//        }
//    }
//    public static void logMeetingToDisplay(final String line) {
//        if (username.equals(TEACHER_NAME)) {
//        }
//        else {
//            if(GlobalSingleton.studentPlayActivity != null) {
//
//                GlobalSingleton.studentPlayActivity.runOnUiThread(new Runnable() {
//                    public void run() {
//                        EditText editText = (EditText) GlobalSingleton.studentPlayActivity
//                                .findViewById(R.id.meetHistory);
//                        editText.append(line + "\n");
//                    }
//                });
//            }
//        }
//    }

}

