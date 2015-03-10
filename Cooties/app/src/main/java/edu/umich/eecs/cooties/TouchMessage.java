package edu.umich.eecs.cooties;

import edu.umich.eecs.cooties.Touch.Touch1;
import java.util.*;

/**
 * Created by luke on 2/28/15.
 */
public class TouchMessage {
    public long timestamp;
    public long sourceUserId;
    public long targetUserId;
    public boolean infected;
    public boolean incubation;

    // Creat a protobuf into collabrify
    public Touch1.Builder initWithInfected(boolean infected, boolean incubation, long sourceUserId, long targetUserId, long timestamp) {
        Touch1.Builder touch = Touch1.newBuilder();
        touch.setInfected(infected);
        touch.setIncubation(incubation);
        touch.setSourceUserId(sourceUserId);
        touch.setTimestamp(timestamp);
        touch.setTargetUserId(targetUserId);

        touch.build();

        infected = touch.getInfected();
        incubation = touch.getIncubation();
        sourceUserId = touch.getSourceUserId();
        targetUserId = touch.getTargetUserId();
        timestamp = touch.getTimestamp();

        return touch;
    }

    // Get a protobuf from the collabrify
    public void initWithBuffer(byte[] data){
        try {
            Touch1 touch = Touch1.parseFrom(data);

            infected = touch.getInfected();
            incubation = touch.getIncubation();
            sourceUserId = touch.getSourceUserId();
            timestamp = touch.getTimestamp();
            targetUserId = touch.getTargetUserId();
        }
        catch (com.google.protobuf.InvalidProtocolBufferException except){
            System.out.println("unreadable touch message");
        }
    }

}
