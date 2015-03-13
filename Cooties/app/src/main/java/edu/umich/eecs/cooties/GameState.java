package edu.umich.eecs.cooties;

import java.io.File;

import edu.umich.imlc.collabrify.client.CollabrifyEvent;
import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.CollabrifyParticipant;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;

/**
 * Created by mtkliema on 3/10/15.
 */
public class GameState implements CollabrifyListener.CollabrifySessionListener {

    @Override
    public void onBaseFileUploadComplete(long l) {
        System.out.println("basefile upload done ");

    }

    @Override
    public void onBaseFileReceived(File file) {
        System.out.println("basefile received ");

    }

    //similar to HMCTouchBeaconEngine.m:collabrifyReceivedEvent
    @Override
    public void onReceiveEvent(CollabrifyEvent event) {
        System.out.println("event received ");
        String eventType = event.type();
        byte[] data = event.data();

        if(eventType.equals("initialSettings")){
            BaseFileMessage msg = new BaseFileMessage();
            msg.initWithBuffer(data);
            // Set beacon majorsaq
            //[self.beaconManager setMajor:[[NSNumber alloc] initWithInt:msg.ibeaconMajor]];
            //incubationTimer = msg.incubationTime;
            //infected = msg.infectedUserId.contains(selfId);
            // check if beacon is turned on
            //gameStarted = [self turnOnBeacons];
        }
        else if(eventType.equals("PlayerAnnounce")){
            PlayerAnnounceMessage msg = new PlayerAnnounceMessage();
            msg.initWithBuffer(data);
            acquiredNewParticipant(msg);

            if(msg.user_id == Globals.selfId) {
                System.out.println("self detected");
                // check if beacon is turned on
                //gameStarted = [self turnOnBeacons];
            }

            printPlayerList();
        }
        else if(eventType.equals("Touch")) {
            //[self touchEventHelper:[[TouchMessage alloc] initWithBuffer:data]];
        }
        /*
        else if([eventType isEqualToString:@"StopSim"]) {
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
*/
        /*
        if(self.delegate && [self.delegate respondsToSelector:@selector(onReceiveEvent:orderId:eventType:)]) {
            [self.delegate onReceiveEvent:data
            orderId:event.orderID
            eventType:eventType];
        }

        if(gameStarted && [self.delegate respondsToSelector:@selector(gameStart)])
            [self.delegate gameStart];

        }
        */


    /*
        if(collabrifyEvent.type() == "initialSettings"){
            BaseFileMessage msg = new BaseFileMessage();
            msg.initWithBuffer(collabrifyEvent.data());
        }
        else if(collabrifyEvent.type() == "PlayerAnnounce"){
            PlayerAnnounceMessage msg = new PlayerAnnounceMessage();
            msg.initWithBuffer(collabrifyEvent.data());

        }
        else if(collabrifyEvent.type() == "Touch"){
            TouchMessage msg = new TouchMessage();
            msg.initWithBuffer(collabrifyEvent.data());

        }
        else if(collabrifyEvent.type() == "StopSim"){

        }
        else if(collabrifyEvent.type() == "Restart"){

        }
        else if(collabrifyEvent.type() == "ShowMeetingRank"){

        }
        else if(collabrifyEvent.type() == "HideMeetingRank"){

        }
        else{
            //unrecognized event type
        }
*/

    }

    // get a new user
    private void acquiredNewParticipant(PlayerAnnounceMessage participant) {
        long playerId = participant.user_id;
        short minor = participant.minor;
        if(Globals.playerMinors.containsKey(minor) == false) {
            Globals.playerMinors.put(minor, playerId);

            PlayerInfo player = new PlayerInfo();
            player.name = participant.displayName;
            player.playerId = playerId;
            player.minor = minor;
            player.left = false;
            Globals.playerInfo.put(minor, player);
            /*
            if([self.delegate respondsToSelector:@selector(participantJoined:)]) {
                dispatch_async(dispatch_get_main_queue(), ^{
                        [self.delegate participantJoined:@{@"id":playerId, @"name":participant.displayName}];
                });
            }
            */
        }
        /*
        else {
            if(playerId  == Globals.selfId) {
                announcePlayer();
            }
        }
        */
    }

    @Override
    public void onParticipantJoined(CollabrifyParticipant collabrifyParticipant) {
        System.out.println("collabrify partiicpant joined:" + collabrifyParticipant.getDisplayName());

    }

    @Override
    public void onParticipantLeft(CollabrifyParticipant collabrifyParticipant) {
        System.out.println(collabrifyParticipant.getDisplayName() + " has Left!");

    }

    @Override
    public void onSessionEnd(long l) {
        System.out.println(" Collabrify session ended!:" + l);

    }

    @Override
    public void onFurtherJoinsPrevented() {
        System.out.println("Further Joins Prevented ");

    }

    @Override
    public void onError(CollabrifyException e) {

    }

    private void printPlayerList(){
        for (short key: Globals.playerMinors.keySet()) {
            System.out.println(key +":" + Globals.playerMinors.get(key));
        }
    }
}
