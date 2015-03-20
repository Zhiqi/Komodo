package edu.umich.eecs.cooties;

import edu.umich.eecs.cooties.BaseFile.BaseFile1;
import java.util.*;

/**
 * Created by luke on 2/28/15.
 */
public class BaseFileMessage {
    // List of infected user id
    public ArrayList<Long> infectedUserId = new ArrayList<Long>();
    public int incubationTime = 0;
    public int ibeaconMajor = 0;
    public int hideHealthStatus = 1; // 1 for true and 0 for false
    BaseFile1 baseFile;

    // Creat a protobuf into collabrify
    public BaseFile1 initWithInfectedUser(ArrayList<Long> userId, int incTime, int major, int hideHealthStatus) {
        BaseFile1.Builder baseFile1 = BaseFile1.newBuilder();

        for(int i = 0; i < userId.size(); i++) {
            baseFile1.setInfectedUserId(i, userId.get(i));
        }
        baseFile1.setIbeaconMajor(major);
        baseFile1.setIncubationTimer(incTime);
        baseFile1.setHideHealthStatus(hideHealthStatus);

        baseFile = baseFile1.build();

        ibeaconMajor = baseFile.getIbeaconMajor();
        infectedUserId = userId;
        hideHealthStatus = (int)baseFile.getHideHealthStatus();
        incubationTime = (int)baseFile.getIncubationTimer();

        return baseFile;
    }

    // Get a protobuf from collabrify
    public void initWithBuffer(byte[] data){

        try {
            baseFile = BaseFile1.parseFrom(data);

            ibeaconMajor = baseFile.getIbeaconMajor();

            ArrayList<Long> infected = new ArrayList<Long>();
            for(int i = 0; i < baseFile.getInfectedUserIdCount(); i++) {
                infected.add(baseFile.getInfectedUserId(i));
            }
            infectedUserId = infected;
            hideHealthStatus = (int)baseFile.getHideHealthStatus();
            incubationTime = (int)baseFile.getIncubationTimer();
        }
        catch (com.google.protobuf.InvalidProtocolBufferException except){
            System.out.println("Unreadable baseFile message");
        }
    }

    // Parse data into string
    byte[] outputBuffer(){
        byte[] buffer = baseFile.toByteArray();
        return buffer;
    }

}
