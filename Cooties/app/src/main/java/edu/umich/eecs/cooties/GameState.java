package edu.umich.eecs.cooties;

import java.io.File;
import java.util.ArrayList;

import edu.umich.imlc.collabrify.client.CollabrifyEvent;
import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.CollabrifyParticipant;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;

/**
 * Created by mtkliema on 3/10/15.
 * Serves as Collabrify Callback
 * Accessed through GlobalSingleton.model
 */
public class GameState implements CollabrifyListener.CollabrifySessionListener, CollabrifyListener.CollabrifyBroadcastListener {




    @Override
    public void onBaseFileUploadComplete(long l) {
        System.out.println("@@@basefile upload done ");

    }

    @Override
    public void onBaseFileReceived(File file) {

        System.out.println("@@@basefile received ");

    }

    //similar to HMCTouchBeaconEngine.m:collabrifyReceivedEvent
    @Override
    public void onReceiveEvent(CollabrifyEvent event) {


        System.out.println("Leave Session");

        System.out.println("Receiving Collabrify event in GameState:" + event.type());
        String eventType = event.type();
        byte[] data = event.data();
        System.out.println("@@@event received " + eventType);

        if(eventType.equals("initialSettings")){
            BaseFileMessage msg = new BaseFileMessage();
            msg.initWithBuffer(data);

            GlobalSingleton.getInstance().major = msg.ibeaconMajor;

            //may need to do deep copy instead of reference

            for (Long a : msg.infectedUserId){
                GlobalSingleton.getInstance().initial_infected_user_ids.add(a);
            }
//            GlobalSingleton.initial_infected_user_ids = msg.infectedUserId;
            GlobalSingleton.getInstance().incubation_time = msg.incubationTime;
            GlobalSingleton.getInstance().hide_health_status = msg.hideHealthStatus;


            if(GlobalSingleton.getInstance().initial_infected_user_ids.contains(GlobalSingleton.getInstance().selfId)){
                GlobalSingleton.getInstance().infected_status = true;
            }

            for(Long a : GlobalSingleton.getInstance().initial_infected_user_ids){
                GlobalSingleton.getInstance().infected_status_by_player.put(a, true);
                System.out.println("Initial Infected User ID "+a);

            }




            System.out.println("Received Basefile  Message");
            System.out.println("Major is "+ GlobalSingleton.getInstance().major);
            System.out.println("Incubation Time is "+ GlobalSingleton.getInstance().incubation_time);
            System.out.println("Hide health status is "+ GlobalSingleton.getInstance().hide_health_status);

            GlobalSingleton.getInstance().base_received = true;

            if(GlobalSingleton.getInstance().interstitial_wait != null)
            {
                GlobalSingleton.getInstance().interstitial_wait.lets_go();
            }

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
            System.out.println("Received Player Announce Message for:" + msg.displayName);

            acquiredNewParticipant(msg);
            /*
            if(msg.user_id == GlobalSingleton.selfId) {
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

        //we definately can't do this because it's not in the ios version.
        //we need to find the reason behind the callback not being executed
        //i think it could be too many touch messages being send out - as the incoming limit to google is 2/sec
        //google would drop the messages-> in player init
        else if(eventType.equals("ResendPlayer")) {
            // If player find that he missed player announce message, he will broadcast this event
            // user will re-broadcast "playerAnnounce" message if receive this event



        }
        else if(eventType.equals("StopSim")) {
            //disable beacons
            if (!GlobalSingleton.getInstance().username.equals(GlobalSingleton.TEACHER_NAME))
                GlobalSingleton.getInstance().studentPlayActivity.leaveSession();
        }
        else if(eventType.equals("Restart")) {

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
        if(GlobalSingleton.getInstance().playerMinors.containsKey(minor) == false) {
            GlobalSingleton.getInstance().playerMinors.put(minor, playerId);
//            GlobalSingleton.getInstance().logUsernameToDisplay(participant.displayName);
            PlayerInfo player = new PlayerInfo();
            player.name = participant.displayName;
            player.playerId = playerId;
            player.minor = minor;
            player.left = false;
            GlobalSingleton.getInstance().playerInfo.put(playerId, player);
            /*
            if([self.delegate respondsToSelector:@selector(participantJoined:)]) {
                dispatch_async(dispatch_get_main_queue(), ^{
                        [self.delegate participantJoined:@{@"id":playerId, @"name":participant.displayName}];
                });
            }
            */
        }

        if(GlobalSingleton.getInstance().infected_status_by_player.containsKey(playerId)){
            //do nothing, key with infection status already in from basemsg
        }
        else{
            GlobalSingleton.getInstance().infected_status_by_player.put(playerId, false);
        }
        /*
        else {
            if(playerId  == GlobalSingleton.selfId) {
                announcePlayer();
            }
        }
        */
    }

    public void receivedEventFrom(short minor){
        final long timeUntilNextAllowedConnectAfterConnect = 30;

        //after an event is received, prevent another connection for 30 seconds
        GlobalSingleton.getInstance().lastSend.put(minor, Math.round((double)System.currentTimeMillis()/1000) + timeUntilNextAllowedConnectAfterConnect);
    }

    //called on receipt of touch msg - finds corresponding events in history
    void touchEventHelper(TouchMessage msg){
        CheckTouch checkTouch = new CheckTouch();


            //obtain corresponding messages already received with same users during approximate time
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

                PlayerInfo firstPlayer; //self
                PlayerInfo secondPlayer; //opposite
                if(msg.sourceUserId == GlobalSingleton.getInstance().selfId){
                    firstPlayer = GlobalSingleton.getInstance().playerInfo.get(msg.sourceUserId);
                    secondPlayer = GlobalSingleton.getInstance().playerInfo.get(touch.sourceUserId);
                } else {
                    firstPlayer = GlobalSingleton.getInstance().playerInfo.get(touch.sourceUserId);
                    secondPlayer = GlobalSingleton.getInstance().playerInfo.get(msg.sourceUserId);
                    //if(infection == HMCFirstUser) infection = HMCSecondUser;
                    //else if(infection == HMCSecondUser) infection = HMCFirstUser;
                }


                if(msg.infected || touch.infected){

                    if(firstPlayer.playerId == GlobalSingleton.getInstance().selfId || secondPlayer.playerId == GlobalSingleton.getInstance().selfId){
                        GlobalSingleton.getInstance().infected_status = true;

                        if(GlobalSingleton.getInstance().studentPlayActivity != null){
                            GlobalSingleton.getInstance().studentPlayActivity.showInfected();
                        }


                    }

                    GlobalSingleton.getInstance().infected_status_by_player.put(firstPlayer.playerId, true);
                    GlobalSingleton.getInstance().infected_status_by_player.put(secondPlayer.playerId, true);
                    //update_infection_screen();
                }


                // The block can only be run on only one thread for any given time
                HistoryItem historyItem = new HistoryItem(msg.timestamp, firstPlayer, secondPlayer);
                historyItem.increment();



                if(GlobalSingleton.getInstance().historyList.contains(historyItem)) {
                    //History item records how many are created.  If history list already contains it, then subtract
                    historyItem.decrement();
                }
                else {
                    GlobalSingleton.getInstance().historyList.add(historyItem);
//                    GlobalSingleton.logMeetingToDisplay(historyItem.firstUser.name + " and " + historyItem.secondUser.name + " meet at " + historyItem.timestamp);



                    //prevent interactions for 30 seconds - may not be accurate if an app joins late and receives a bunch of prior messages
                    if(msg.sourceUserId == GlobalSingleton.getInstance().selfId || touch.sourceUserId == GlobalSingleton.getInstance().selfId) {
                        short targetMinor;
                        if(firstPlayer.playerId == GlobalSingleton.getInstance().selfId) {
                            targetMinor = secondPlayer.minor;
                        }
                        else {
                            targetMinor = firstPlayer.minor;
                        }

                        receivedEventFrom(targetMinor);
                            /*
                            for (HistoryItem HI : GlobalSingleton.historyList){
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
        System.out.println("collabrify participant joined:" + collabrifyParticipant.getDisplayName());
        GlobalSingleton.getInstance().active_players.put(collabrifyParticipant.getId(),collabrifyParticipant.getDisplayName());
        System.out.println("@@@collabrify participant joined:" + collabrifyParticipant.getDisplayName());

    }

    @Override
    public void onParticipantLeft(CollabrifyParticipant collabrifyParticipant) {
        System.out.println(collabrifyParticipant.getDisplayName() + " has Left!");
        GlobalSingleton.getInstance().active_players.remove(collabrifyParticipant.getId());

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
        System.out.println("@@@number of players in the session is:" + GlobalSingleton.getInstance().playerInfo.size());
        for (long key: GlobalSingleton.getInstance().playerInfo.keySet()) {
            System.out.println(key +":" + GlobalSingleton.getInstance().playerInfo.get(key).name +":id is:"+ GlobalSingleton.getInstance().playerInfo.get(key).playerId);
        }
    }

    @Override
    public void onBroadcastDone(CollabrifyEvent collabrifyEvent) {

    }
}
