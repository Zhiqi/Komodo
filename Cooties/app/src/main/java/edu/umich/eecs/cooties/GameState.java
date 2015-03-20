package edu.umich.eecs.cooties;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.widget.EditText;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import edu.umich.imlc.collabrify.client.CollabrifyEvent;
import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.CollabrifyParticipant;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;

/**
 * Created by mtkliema on 3/10/15.
 */
public class GameState implements CollabrifyListener.CollabrifySessionListener {

    private BeaconTransmitter beaconTransmitter;


    @Override
    public void onBaseFileUploadComplete(long l) {
        System.out.println("@@@basefile upload done ");

    }

    @Override
    public void onBaseFileReceived(File file) {
        System.out.println("@@@basefile received ");

    }

    public void startBLE() {

        if(beaconTransmitter != null){
            //bluetooth may be off
            return;
        }

        int result = BeaconTransmitter.checkTransmissionSupported(Globals.studentPlayActivity.getApplicationContext());
        if(result == BeaconTransmitter.SUPPORTED) {
            //Toast.makeText(getApplicationContext(), "Beacon Supported\n Starting Transmission", Toast.LENGTH_SHORT).show();
            System.out.println("transmit iBeacon");
            Beacon beacon = new Beacon.Builder()
                    .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                    .setId2(Globals.major)
                    .setId3(Globals.minor)
                    .setManufacturer(0x4c00)
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[]{0l}))
                    .build();
            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
            beaconTransmitter = new BeaconTransmitter(Globals.studentPlayActivity.getApplicationContext(), beaconParser);
            //beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
            //beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            System.out.println("transmit iBeacon start:" + beaconTransmitter.isStarted());
            //Toast.makeText(getApplicationContext(), "transmit iBeacon start:" + beaconTransmitter.isStarted(), Toast.LENGTH_SHORT).show();
            AdvertiseCallback callback = new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    super.onStartSuccess(settingsInEffect);
                    Globals.studentPlayActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Globals.studentPlayActivity.getApplicationContext(), "transmit iBeacon start: " + beaconTransmitter.isStarted(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            beaconTransmitter.startAdvertising(beacon, callback);


        }
        else if(result == BeaconTransmitter.NOT_SUPPORTED_MIN_SDK
                || result == BeaconTransmitter.NOT_SUPPORTED_BLE
                || result == BeaconTransmitter.NOT_SUPPORTED_MULTIPLE_ADVERTISEMENTS
                || result == BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER){
            Globals.studentPlayActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(Globals.studentPlayActivity.getApplicationContext(), "Beacon Not Supported\n on This Device", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void endBLE() {
        if(beaconTransmitter != null){
            beaconTransmitter.stopAdvertising();
            beaconTransmitter = null;
            Globals.studentPlayActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(Globals.studentPlayActivity.getApplicationContext(), "Beacon Off", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //similar to HMCTouchBeaconEngine.m:collabrifyReceivedEvent
    @Override
    public void onReceiveEvent(CollabrifyEvent event) {
        String eventType = event.type();
        byte[] data = event.data();
System.out.println("@@@event received " + eventType);

        if(eventType.equals("initialSettings")){
            BaseFileMessage msg = new BaseFileMessage();
            msg.initWithBuffer(data);
            // Set beacon majorsaq
            //[self.beaconManager setMajor:[[NSNumber alloc] initWithInt:msg.ibeaconMajor]];
            Globals.major = String.valueOf(msg.ibeaconMajor);
            //incubationTimer = msg.incubationTime;
            //infected = msg.infectedUserId.contains(selfId);
            // check if beacon is turned on
            if (!Globals.username.equals(Globals.TEACHER_NAME)) {
                System.out.println("@@@Start sim");
                startBLE();
            }
        }
        else if(eventType.equals("PlayerAnnounce")){
            PlayerAnnounceMessage msg = new PlayerAnnounceMessage();
            msg.initWithBuffer(data);
            acquiredNewParticipant(msg);
            /*
            if(msg.user_id == Globals.selfId) {
                System.out.println("self detected");
                // check if beacon is turned on
                //gameStarted = [self turnOnBeacons];
            }
            */
            printPlayerList();
        }
        else if(eventType.equals("Touch")) {

            TouchMessage msg = new TouchMessage();
            msg.initWithBuffer(data);
            touchEventHelper(msg);
            System.out.println("@@@Touch message received, sent by Player " +msg.sourceUserId);
        }
        else if(eventType.equals("ResendPlayer")) {
            // If player find that he missed player announce message, he will broadcast this event
            // user will re-broadcast "playerAnnounce" message if receive this event



        }
        else if(eventType.equals("StopSim")) {
            if (!Globals.username.equals(Globals.TEACHER_NAME)) {
                System.out.println("@@@Stop sim");
                endBLE();
            }
            //self.simulationStopped = true;
            //[self.historyDelegate refresh];
        }
        /*
        else if([eventType isEqualToString:@"Restart"]) {
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

    // get a new user
    private void acquiredNewParticipant(PlayerAnnounceMessage participant) {
        long playerId = participant.user_id;
        short minor = participant.minor;
        if(Globals.playerMinors.containsKey(minor) == false) {
            Globals.playerMinors.put(minor, playerId);
            Globals.logUsernameToDisplay(participant.displayName);
            PlayerInfo player = new PlayerInfo();
            player.name = participant.displayName;
            player.playerId = playerId;
            player.minor = minor;
            player.left = false;
            Globals.playerInfo.put(playerId, player);
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

    public void receivedEventFrom(short minor){
        final long timeUntilNextAllowedConnectAfterConnect = 30;

        //after an event is received, prevent another connection for 30 seconds
        Globals.lastSend.put(minor, Math.round((double)System.currentTimeMillis()/1000) + timeUntilNextAllowedConnectAfterConnect);
    }

    void touchEventHelper(TouchMessage msg){
        CheckTouch checkTouch = new CheckTouch();
        ArrayList<TouchMessage> foundEvents = checkTouch.bumpEvents(msg);

        if(foundEvents.size() > 0) {
            for(TouchMessage touch : foundEvents) {
                /*
                hmc_infection_t infection;
                if(msg.infected && touch.infected) infection = HMCAlreadyInfected;
                else if(msg.infected && !touch.infected) infection = HMCSecondUser;
                else if(!msg.infected && touch.infected) infection = HMCFirstUser;
                else infection = HMCNone;
                */

                PlayerInfo firstPlayer;
                PlayerInfo secondPlayer;
                if(msg.sourceUserId == Globals.selfId){
                    firstPlayer = Globals.playerInfo.get(msg.sourceUserId);
                    secondPlayer = Globals.playerInfo.get(touch.sourceUserId);
                } else {
                    firstPlayer = Globals.playerInfo.get(touch.sourceUserId);
                    secondPlayer = Globals.playerInfo.get(msg.sourceUserId);
                    //if(infection == HMCFirstUser) infection = HMCSecondUser;
                    //else if(infection == HMCSecondUser) infection = HMCFirstUser;
                }

                // The block can only be run on only one thread for any given time
                    HistoryItem historyItem = new HistoryItem(msg.timestamp, firstPlayer, secondPlayer);
                    historyItem.increment();

                    if(Globals.historyList.contains(historyItem)) {
                        //History item records how many are created.  If history list already contains it, then subtract
                        historyItem.decrement();
                    }
                    else {
                        Globals.historyList.add(historyItem);
                        Globals.logMeetingToDisplay(historyItem.firstUser.name + " and " + historyItem.secondUser.name + " meet at " + historyItem.timestamp);
                        if(msg.sourceUserId == Globals.selfId || touch.sourceUserId == Globals.selfId) {
                            short targetMinor;
                            if(firstPlayer.playerId == Globals.selfId) {
                                targetMinor = secondPlayer.minor;
                            }
                            else {
                                targetMinor = firstPlayer.minor;
                            }
                            receivedEventFrom(targetMinor);
                            /*
                            for (HistoryItem HI : Globals.historyList){
                                System.out.println("@@@Player " + HI.firstUser.name + " and Player " + HI.secondUser.name + " meet at " + HI.timestamp);
                            }
                            */
                            /*
                            bool newlyInfected = false;
                            if(!self.infected && (infection == HMCFirstUser || infection == HMCSecondUser)) {
                                newlyInfected = true;
                            }

                            if(self.delegate && [self.delegate respondsToSelector:@selector(receivedTouchEvent:isInfected:)]) {
                                NSNumber *other;
                                if([msg.sourceUserId longLongValue] == [[HMCCollabrify sharedinstance] participantId]) {
                                    other = touch.sourceUserId;
                                } else {
                                    other = msg.sourceUserId;
                                }

                                HMCPlayerInfo *playerInfo = [self.playerInfo objectForKey:other];
                                [self.delegate receivedTouchEvent:playerInfo isInfected:newlyInfected];
                            }
                            */
                        }
                    }
            }

        }

    }

    @Override
    public void onParticipantJoined(CollabrifyParticipant collabrifyParticipant) {
        System.out.println("@@@collabrify participant joined:" + collabrifyParticipant.getDisplayName());

    }

    @Override
    public void onParticipantLeft(CollabrifyParticipant collabrifyParticipant) {
        System.out.println(collabrifyParticipant.getDisplayName() + " has Left!");

    }

    @Override
    public void onSessionEnd(long l) {
        System.out.println(" @@@Collabrify session ended!:" + l);

    }

    @Override
    public void onFurtherJoinsPrevented() {
        System.out.println("Further Joins Prevented ");

    }

    @Override
    public void onError(CollabrifyException e) {

    }

    // Helper function for print all players in session
    // just for debug
    private void printPlayerList(){
        System.out.println("@@@number of players in the session is:" + Globals.playerInfo.size());
        for (long key: Globals.playerInfo.keySet()) {
            System.out.println(key +":" + Globals.playerInfo.get(key).name +":id is:"+Globals.playerInfo.get(key).playerId);
        }
    }
}
