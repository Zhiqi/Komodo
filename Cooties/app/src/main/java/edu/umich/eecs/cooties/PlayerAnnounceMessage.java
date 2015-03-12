package edu.umich.eecs.cooties;

import edu.umich.eecs.cooties.PlayerAnnounce.PlayerAnnounce1;

/**
 * Created by luke on 2/28/15.
 */
public class PlayerAnnounceMessage {
    public long user_id;
    public short minor;
    public String displayName;
    PlayerAnnounce1 player;

    // Creat a protobuf into collabrify
    public PlayerAnnounce1 initWithInfectedUser(long userId, int minor, String name) {
        PlayerAnnounce1.Builder player1 = PlayerAnnounce1.newBuilder();
        player1.setUserId(userId);
        player1.setMinor(minor);
        player1.setDisplayName(name);

        player = player1.build();

        minor = player.getMinor();
        user_id = player.getUserId();
        displayName = player.getDisplayName();

        return player;
    }

    // Get a protobuf from the collabrify
    public void initWithBuffer(byte[] data){
        try {
            player = PlayerAnnounce1.parseFrom(data);

            minor = (short)player.getMinor();
            user_id = player.getUserId();
            displayName = player.getDisplayName();
        }
        catch (com.google.protobuf.InvalidProtocolBufferException except){
            System.out.println("unreadable playerAnnounce message");
        }
    }

    // Parse data into string
    byte[] outputBuffer(){
        byte[] buffer = player.toByteArray();
        return buffer;
    }
}
