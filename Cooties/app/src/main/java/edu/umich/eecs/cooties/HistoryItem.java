package edu.umich.eecs.cooties;

/**
 * Created by luke on 3/13/15.
 */
public class HistoryItem {
    long timestamp;
    PlayerInfo firstUser;
    PlayerInfo secondUser;
    static int numItems = 0;

    public HistoryItem(long timestamp1, PlayerInfo firstUser1, PlayerInfo secondUser1){
        timestamp = timestamp1;
        firstUser = firstUser1;
        secondUser = secondUser1;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HistoryItem) {
            HistoryItem object = (HistoryItem) o;
            return (object.timestamp - timestamp) < 7 &&
                    (((object.firstUser.playerId == firstUser.playerId) && (object.secondUser.playerId == secondUser.playerId)) ||
                            ((object.firstUser.playerId == secondUser.playerId) && (object.firstUser.playerId == secondUser.playerId)));
        }
        else return false; 
    }

    boolean containsUserId(long userId){
        return (firstUser.playerId == userId || secondUser.playerId == userId);
    }

    void reset(){
        numItems = 0;
    }

    void decrement(){
        numItems--;
    }

    void increment(){
        numItems++;
    }
}
