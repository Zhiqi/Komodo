package edu.umich.eecs.cooties;

/**
 * Created by Zhiqi on 3/12/2015.
 */

import java.util.ArrayList;
import java.util.Hashtable;


public class CheckTouch {

    //called by toucheventhelper in Gamestate
    //search for corresponding touch interaction messages within timespan and returns array of candidates
    ArrayList bumpEvents(TouchMessage msg) {
        final int timeSpanAllowed = 3;

        ArrayList<TouchMessage> found = new ArrayList<TouchMessage>();

        // Find valid touch message
        for(int i = -timeSpanAllowed; i < timeSpanAllowed + 1; ++i) {
            long num = msg.timestamp + i;
            ArrayList<TouchMessage> search = Globals.touchDict.get(num);
            if(search != null) {
                //test if any collected touches are in the current touch and vice versa
                for(TouchMessage touch : search) {
                    if(msg.targetUserId == touch.sourceUserId && touch.targetUserId == msg.sourceUserId) {
                        found.add(touch);
                    }
                }
            }
        }

        addTouch(msg);


        //may need to change to keep the oldest message - if returning only one, do not use arraylist???
        // Remove duplicate
        if(found.size() > 0) {
            for (int i = found.size() - 1; i > 0; i--){
                if (found.indexOf(found.get(i)) < i){
                    found.remove(i);
                }
            }
        }
        return found;
    }

    void addTouch(TouchMessage msg){
        ArrayList<TouchMessage> array = Globals.touchDict.get(msg.timestamp);
        if(array == null) {
            array = new ArrayList();
            Globals.touchDict.put(msg.timestamp, array);
        }
        array.add(msg);
    }

}
