package edu.umich.eecs.cooties;

import edu.umich.eecs.cooties.PlayerAnnounce.PlayerAnnounce1;

/**
 * Created by luke on 2/28/15.
 */
public class PlayerAnnounceMessage {
    public long user_id;
    public int minor;
    public String displayName;

    // Creat a protobuf into collabrify
    public PlayerAnnounce1.Builder initWithInfectedUser(long userId, int minor, String name) {
        PlayerAnnounce1.Builder player = PlayerAnnounce1.newBuilder();
        player.setUserId(userId);
        player.setMinor(minor);
        player.setDisplayName(name);

        player.build();

        minor = player.getMinor();
        user_id = player.getUserId();
        displayName = player.getDisplayName();

        return player;
    }

    // Get a protobuf from the collabrify
    public void initWithBuffer(byte[] data){
        try {
            PlayerAnnounce1 player = PlayerAnnounce1.parseFrom(data);

            minor = player.getMinor();
            user_id = player.getUserId();
            displayName = player.getDisplayName();
        }
        catch (com.google.protobuf.InvalidProtocolBufferException except){
            System.out.println("unreadable playerAnnounce message");
        }
    }
}
