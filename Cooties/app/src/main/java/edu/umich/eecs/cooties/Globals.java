package edu.umich.eecs.cooties;

import android.app.Activity;
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
public class Globals {
//    public static final String login_email = "collabrify.tester@gmail.com";
//    public static final String login_id = "4891981239025664";
//    public static final String login_email = "441winter2014@umich.edu";
//    public static final String login_id = "338692774BBE";
//    public static final List<String> tags = Arrays.asList("tag13"); //tag identifying cooties+ application on collabrify


    public static final String login_email = "imlcteam@gmail.com";
    public static final String login_id = "2468";
    public static final List<String> tags = Arrays.asList("tag61"); //tag identifying cooties+ application on collabrify

    public static final String TEACHER_NAME = "T";

    public static String username; /* username that user entered set in MainActivity.java */
    public static GameState model = new GameState(); // session listener for myclient
    public static CollabrifyClient myclient; // set in MainActivity.java
    public static CollabrifySession mysession; /* joined session set in StudentJoinActivity or TeacherActivity_Initialize*/
    public static long selfId; /* participant id set in StudentJoinActivity */

    public static StudentPlayActivity studentPlayActivity = null;
    public static Student_Intersitital_Activity_2 interstitial_wait = null;
    public static Boolean base_received = false;



//MESSAGE STUFF

    //BASEFILE ATTRIBUTES STUFF-------------------------------------------------------------------
    public static ArrayList<Long> initial_infected_user_ids;
    public static long incubation_time = 0;
    public static long hide_health_status = 1;
    public static Boolean infected_status = false;


    //NEW PARTICIPANT MESSAGE STUFF-------------------------------------------------------------------
    /* key: minor value: userId (participant id)
     * set in acquiredNewParticipant announce message
     * used by bluetooth listener*/
    public static Hashtable<Short, Long> playerMinors = new Hashtable<Short, Long>();
    /* key: userId (participant id); value: playerInfo
    * set in acuiredNewParticipant annouce message
    * */
    public static Hashtable<Long, PlayerInfo> playerInfo = new Hashtable<Long, PlayerInfo>();
    // key: userId (participant id); value: playername -  added to by collabrifylistener in gamestate for student joins/leaves
    //not set by new participant message
    public static Hashtable<Long, String> active_players = new Hashtable<Long, String>();


    //CHECKTOUCH MESSAGE STUFF-------------------------------------------------------------------
    //set in checktouch
    public static Hashtable<Long, ArrayList> touchDict = new Hashtable<Long, ArrayList>();
     // key: timestamp, value: list of touch message received



    //TOUCH EVENT HELPER STUFF-------------------------------------------------------------------
    //List of confirmed meetings
    //Set in toucheventhelper
    public static ArrayList<HistoryItem> historyList = new ArrayList<HistoryItem>();
    /* key: userId value: true for infected, false for not infected (default)
    * set in touchevent helper and basefilemessage, default false for not infected
    * IGNORE: FOR TESTING ONLY : ONLY CONSIDER MSG INFECTION STATUS*/
    public static Hashtable<Long, Boolean> infected_status_by_player = new Hashtable<Long, Boolean>();






    //BLUETOOTH STUFF-------------------------------------------------------------------
    //major included in basefile message
    //could set independ. based on session id
    public static int major = 0;
    public static final UUID bt_uuid = UUID.fromString("f52333ca-84ea-4fe7-9192-1ee0fce480e9");

    // key: minor, value: timestamp;
    // Set/checked in scanForSignficantConnection , beacons ()
    public static Hashtable<Short, Long> lastSend = new Hashtable<Short, Long>();

    // Set/checked in scanForSignficantConnection , beacons ()
    // key: minor, value: timestamp
    public static Hashtable<Short, Long> rangedBeacons = new Hashtable<Short, Long>();

    //used in searchbeacon
    public static short stickyMinor;


    public static void clear() {
        username=null;
        myclient=null;
        mysession=null;
        studentPlayActivity=null;
        interstitial_wait=null;
        base_received=false;
        infected_status=false;
//        initial_infected_user_ids.clear();
//        playerInfo.clear();
//        playerMinors.clear();
//        infected_status_by_player.clear();
//        active_players.clear();
//        touchDict.clear();
//        historyList.clear();
//        lastSend.clear();
//        rangedBeacons.clear();
    }

    public static void logUsernameToDisplay(final String line) {
        if (username.equals(TEACHER_NAME)) {
        }
        else {
            if(Globals.studentPlayActivity != null){
                Globals.studentPlayActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        EditText editText = (EditText) Globals.studentPlayActivity
                                .findViewById(R.id.rangingText);
                        editText.append(line + "\n");
                    }
                });

            }


        }
    }
    public static void logMeetingToDisplay(final String line) {
        if (username.equals(TEACHER_NAME)) {
        }
        else {
            if(Globals.studentPlayActivity != null) {

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

}

