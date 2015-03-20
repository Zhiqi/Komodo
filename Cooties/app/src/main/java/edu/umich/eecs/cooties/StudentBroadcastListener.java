package edu.umich.eecs.cooties;

import java.util.Hashtable;
import edu.umich.eecs.cooties.Touch;
import edu.umich.eecs.cooties.PlayerAnnounceMessage;
import edu.umich.eecs.cooties.Globals;
import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.CollabrifyEvent;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;
import edu.umich.eecs.cooties.StudentJoinActivity;
import edu.umich.eecs.cooties.PlayerInfo;

/**
 * Created by luke on 3/10/15.
 */

//broadcasts player information and device detection information
public class StudentBroadcastListener {

    CollabrifyListener.CollabrifyBroadcastListener listen;
    int incubationTimer;
    boolean incubationCompleted;
//    boolean infected;


    //called by both searchBeacon and studentPlayActivity
    public StudentBroadcastListener() {

        listen = new CollabrifyListener.CollabrifyBroadcastListener() {
            @Override
            public void onBroadcastDone(CollabrifyEvent collabrifyEvent) {
                //System.out.println("@@@Event broadcast done");
            }

            @Override
            public void onError(CollabrifyException e) {
                //System.out.println("@@@CollabrifyBroadcastListener error");
            }
        };
    }

    //called by studentplayactivity:oncreate
    // announce player himself when joining a session
    // return a minor value which will be used for beacon creation
    public String announcePlayer() {
        System.out.println("@@@@Announce player");
        long playerId = Globals.selfId;
        short minor = newMinor(playerId);
        // Create an object for PlayAnnounce message
        PlayerAnnounceMessage msg = new PlayerAnnounceMessage();
        msg.initWithInfectedUser(playerId, minor, Globals.username);
        // broadcast the message
        broadcast(msg.outputBuffer(), "PlayerAnnounce");
        return String.valueOf(minor);
    }

    // broadcast the message
    private void broadcast(byte[] data, String eventType){
        //System.out.println("@@@Attempt to broadcast");
        Globals.myclient.broadcast(data, eventType, listen);
    }

    //called by announceplayer -> returned to StudentPlayActivity:onCreate for setting up bluetooth beacon
    // create a new minor for beacon
    private short newMinor(long playerId) {
        short minor = (short)(playerId % Short.MAX_VALUE);
        while(Globals.playerMinors.containsKey(minor) == true) {
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
        msg.initWithInfected(Globals.infected_status, inIncubation, Globals.selfId, Globals.playerMinors.get(minor), timestamp);
        broadcast(msg.outputBuffer(), "Touch");

        /*
        if(self.delegate) {
            [self.delegate connectingToDevice:self.playerMinors[minor]];
        }
        */
    }


}
