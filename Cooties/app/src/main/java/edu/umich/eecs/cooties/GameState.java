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
    public void onReceiveEvent(CollabrifyEvent collabrifyEvent) {
        System.out.println("event received ");
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
        System.out.println(" Collabrify session ended!:"+l);

    }

    @Override
    public void onFurtherJoinsPrevented() {
        System.out.println("Further Joins Prevented ");

    }

    @Override
    public void onError(CollabrifyException e) {

    }
}
