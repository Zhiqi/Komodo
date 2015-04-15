package edu.umich.eecs.cooties;

/**
 * Created by Zhiqi on 3/12/2015.
 */

import java.util.ArrayList;


public class CheckTouch {

    static final int timeSpanAllowed = 3;
    //called by toucheventhelper in Gamestate
    //search for corresponding touch interaction messages within timespan and returns array of candidates
    ArrayList bumpEvents(TouchMessage msg) {

        ArrayList<TouchMessage> found = new ArrayList<TouchMessage>();

        // Find valid touch message
        for(int i = -timeSpanAllowed; i < timeSpanAllowed + 1; ++i) {
            long num = msg.timestamp + i;
            ArrayList<TouchMessage> search = GlobalSingleton.getInstance().touchDict.get(num);
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
        ArrayList<TouchMessage> array = GlobalSingleton.getInstance().touchDict.get(msg.timestamp);
        if(array == null) {
            array = new ArrayList();
            GlobalSingleton.getInstance().touchDict.put(msg.timestamp, array);
        }
        array.add(msg);
    }

}
