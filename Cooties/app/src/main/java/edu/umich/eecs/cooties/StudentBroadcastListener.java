package edu.umich.eecs.cooties;

import android.util.Log;

import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.CollabrifyEvent;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;

/**
 * Created by luke on 3/10/15.
 */

//broadcasts player information and device detection information
public class StudentBroadcastListener {

    private static final String TAG = "StudentBroadcastLisr";


    CollabrifyListener.CollabrifyBroadcastListener listen;
    int incubationTimer;
    boolean incubationCompleted;


    //called by both searchBeacon and studentPlayActivity
    //used to broadcast player annouce message and
    public StudentBroadcastListener() {

        listen = new CollabrifyListener.CollabrifyBroadcastListener() {
            @Override
            public void onBroadcastDone(CollabrifyEvent collabrifyEvent) {
                System.out.println("@@@Event broadcast done of type "+collabrifyEvent.type());
            }

            @Override
            public void onError(CollabrifyException e) {
                System.out.println("@@@CollabrifyBroadcastListener error");
                Log.e(TAG, "BroadcastListener Error", e);
            }
        };
    }

    //called by studentplayactivity:oncreate
    // announce player himself when joining a session
    // return a minor value which will be used for beacon creation
    public String announcePlayer() {
        System.out.println("@@@@Announce player");
        long playerId = GlobalSingleton.getInstance().selfId;
        short minor = newMinor(playerId);
        // Create an object for PlayAnnounce message
        PlayerAnnounceMessage msg = new PlayerAnnounceMessage();
        msg.initWithInfectedUser(playerId, minor, GlobalSingleton.getInstance().username);
        // broadcast the message
        broadcast(msg.outputBuffer(), "PlayerAnnounce");
        return String.valueOf(minor);
    }

    // broadcast the message
    private void broadcast(byte[] data, String eventType){
        //System.out.println("@@@Attempt to broadcast");
        GlobalSingleton.getInstance().myclient.broadcast(data, eventType, listen);
    }

    //called by announceplayer -> returned to StudentPlayActivity:onCreate for setting up bluetooth beacon
    // create a new minor for beacon
    private short newMinor(long playerId) {
        short minor = (short)(playerId % Short.MAX_VALUE);
        while(GlobalSingleton.getInstance().playerMinors.containsKey(minor) == true) {
            if(minor == Short.MAX_VALUE - 1){
                minor = 0;
            }
            minor++;
        }
        return minor;
    }


    //called by searchbeacon:scanForSignificantConnection()
    // When two devices are touched, broadcast a touch message into session
    public void detectedDevice(short minor) {
        System.out.println("@@@enter detectedDevice()");
        boolean inIncubation = incubationTimer > 0 && !incubationCompleted;
        long timestamp = Math.round((double)System.currentTimeMillis() /1000);
        TouchMessage msg =  new TouchMessage();
        msg.initWithInfected(GlobalSingleton.getInstance().infected_status, inIncubation, GlobalSingleton.getInstance().selfId, GlobalSingleton.getInstance().playerMinors.get(minor), timestamp);
        broadcast(msg.outputBuffer(), "Touch");

        /*
        if(self.delegate) {
            [self.delegate connectingToDevice:self.playerMinors[minor]];
        }
        */
    }


}
