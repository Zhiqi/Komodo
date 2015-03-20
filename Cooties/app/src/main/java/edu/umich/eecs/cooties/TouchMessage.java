package edu.umich.eecs.cooties;

import edu.umich.eecs.cooties.Touch.Touch1;

/**
 * Created by luke on 2/28/15.
 */
public class TouchMessage {
    public long timestamp;
    public long sourceUserId;
    public long targetUserId;
    public boolean infected;
    public boolean incubation;
    Touch1 touch;

    // Creat a protobuf into collabrify
    public Touch1 initWithInfected(boolean infected, boolean incubation, long sourceUserId, long targetUserId, long timestamp) {
        Touch1.Builder touch1 = Touch1.newBuilder();
        touch1.setInfected(infected);
        touch1.setIncubation(incubation);
        touch1.setSourceUserId(sourceUserId);
        touch1.setTimestamp(timestamp);
        touch1.setTargetUserId(targetUserId);

        touch = touch1.build();

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
            touch = Touch1.parseFrom(data);

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

    // Parse data into string
    byte[] outputBuffer(){
        byte[] buffer = touch.toByteArray();
        return buffer;
    }
}
