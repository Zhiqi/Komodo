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
public class PlayerAnnounceJoin {
    //Globals global = new Globals();
    static Hashtable<Short, Long> playerMinors = new Hashtable<Short, Long>(); // map minor to collabrify userId
    static Hashtable<Short, PlayerInfo> playerInfo = new Hashtable<Short, PlayerInfo>();
    String displayName;
    CollabrifyListener.CollabrifyBroadcastListener listen;
    int incubationTimer;
    boolean incubationCompleted;
    boolean infected;

    // broadcast the message
    public void broadcast(byte[] data, String eventType){
        //try {
            Globals.myclient.broadcast(data, eventType, listen);
        //}
        //catch (CollabrifyException except){
        //    System.out.println("unable to broadcast");
        //}
    }

    // create a new minor for beacon
    short newMinor(long playerId) {
        short minor = (short)(playerId % Short.MAX_VALUE);
        while(playerMinors.containsKey(minor) == true) {
            if(minor == Short.MAX_VALUE - 1){
                minor = 0;
            }
            minor++;
        }
        return minor;
    }

    // announce player himself when joining a session
    public void announcePlayer() {
        long playerId = Globals.myclient.currentSessionParticipantId();
        short minor = newMinor(playerId);
        // Create an object for PlayAnnounce message
        PlayerAnnounceMessage msg = new PlayerAnnounceMessage();
        msg.initWithInfectedUser(playerId, minor, displayName);
        // broadcast the message
        broadcast(msg.outputBuffer(), "PlayerAnnounce");
    }


    // get a new user
    void acquiredNewParticipant(PlayerAnnounceMessage participant) {
        long playerId = participant.user_id;
        short minor = participant.minor;
        if(playerMinors.containsKey(minor) == false) {
            playerMinors.put(minor, playerId);

            if(playerId == Globals.myclient.currentSessionParticipantId()){

            }

            PlayerInfo player = new PlayerInfo();
            player.name = participant.displayName;
            player.playerId = playerId;
            player.minor = minor;
            player.left = false;
            playerInfo.put(minor, player);
            /*
            if([self.delegate respondsToSelector:@selector(participantJoined:)]) {
                dispatch_async(dispatch_get_main_queue(), ^{
                        [self.delegate participantJoined:@{@"id":playerId, @"name":participant.displayName}];
                });
            }
            */
        } else {
            if(playerId  == Globals.myclient.currentSessionParticipantId()) {
                announcePlayer();
            }
        }
    }

    // When two devices are touched, broadcast a touch message into session
    void detectedDevice(short minor) {
        boolean inIncubation = incubationTimer > 0 && !incubationCompleted;
        long timestamp = System.currentTimeMillis();
        TouchMessage msg =  new TouchMessage();
        msg.initWithInfected(infected, inIncubation, Globals.myclient.currentSessionParticipantId(), playerMinors.get(minor), timestamp);
        broadcast(msg.outputBuffer(), "Touch");

        /*
        if(self.delegate) {
            [self.delegate connectingToDevice:self.playerMinors[minor]];
        }
        */
    }

    /*

    void collabrifyReceivedEvent(CollabrifyEvent event){
        String eventType = event.type();
        byte[] data = event.data();

        boolean gameStarted = false;
        if(eventType.equals("initialSettings") == true) {
        BaseFileMessage msg = msg.initWithBuffer(data);
        [self.beaconManager setMajor:[[NSNumber alloc] initWithInt:msg.ibeaconMajor]];
        self.incubationTimer = msg.incubationTime;
        self.infected = [msg.infectedUserId containsObject:[NSNumber numberWithLongLong:[[HMCCollabrify sharedinstance] participantId]]];
        gameStarted = [self turnOnBeacons];
    } else if([eventType isEqualToString:@"PlayerAnnounce"]) {
        PlayerAnnounceMessage *msg = [[PlayerAnnounceMessage alloc] initWithBuffer:data];
        [self acquiredNewParticipant:msg];

        if([msg.user_id longLongValue] == [[HMCCollabrify sharedinstance] participantId]) {
            gameStarted = [self turnOnBeacons];
        }
    } else if([eventType isEqualToString:@"Touch"]) {
        [self touchEventHelper:[[TouchMessage alloc] initWithBuffer:data]];
    } else if([eventType isEqualToString:@"StopSim"]) {
        [self turnOffBeacons];
        self.simulationStopped = true;
        [self.historyDelegate refresh];
    } else if([eventType isEqualToString:@"Restart"]) {
        [self.historyDelegate gameRestarted];
        [self gameClear];
    } else if([eventType isEqualToString:@"ShowMeetingRank"]) {
        self.showMeetingRank = true;
        [self.historyDelegate refresh];
    } else if([eventType isEqualToString:@"HideMeetingRank"]) {
        self.showMeetingRank = false;
        [self.historyDelegate refresh];
    }

        if(self.delegate && [self.delegate respondsToSelector:@selector(onReceiveEvent:orderId:eventType:)]) {
        [self.delegate onReceiveEvent:data
        orderId:event.orderID
        eventType:eventType];
    }

        if(gameStarted && [self.delegate respondsToSelector:@selector(gameStart)])
        [self.delegate gameStart];
    }

    */
}
