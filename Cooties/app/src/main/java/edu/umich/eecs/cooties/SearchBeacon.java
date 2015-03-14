package edu.umich.eecs.cooties;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Comparator;
import java.util.Collections;


public class SearchBeacon {

    StudentBroadcastListener listener;

    public SearchBeacon(){
        listener = new StudentBroadcastListener();
    }

    private void scanForSignificantConnection() {
        System.out.println("@@@enter scanForSignificantConnection() ");

        final int lengthOfConnection = 1;
        final int timeSinceLastSend = 2;

        long now = Math.round((double)System.currentTimeMillis()/1000);
        for (Short key : Globals.rangedBeacons.keySet()) {
            System.out.println("@@@timestamp for device " + key+ " is " +Globals.rangedBeacons.get(key));

            if (now - Globals.rangedBeacons.get(key) > lengthOfConnection) {
                System.out.println("@@@Connection time satisfied");
                if (Globals.lastSend.get(key) == null || now - Globals.lastSend.get(key) > timeSinceLastSend) {
                    Globals.lastSend.put(key, now);
                    Globals.stickyMinor = key;
                    listener.detectedDevice(key);
                    break;
                }
            }
        }
        System.out.println("@@@leave scanForSignificantConnection() ");

    }

    public void beacons(Collection<Beacon> beacons) {
        System.out.println("@@@enter beacons() ");

        int highBoundRSSI = -40;

        //sort beacons by rssi descending
        Beacon [] beaconArr = beacons.toArray(new Beacon[beacons.size()]);
        ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
        for (Beacon b : beaconArr){
            beaconList.add(b);
        }
        Collections.sort(beaconList, new beaconComp());
       // System.out.println("@@@break point 0 ");

        // Only one thread can execute this block of code at any given time
        //filter found beacon array into tmp dictionary with timestamps with highBoundRSSI
        Hashtable<Short, Long> tmpRangedBeacons = new Hashtable<Short, Long>();
        boolean sticked = false;
        for(Beacon beacon : beaconList) {
            //eliminate 0 signal strengths
            if(beacon.getRssi() == 0)
                continue;

            //filter by bound
            if(beacon.getRssi() < highBoundRSSI) {
                break;
            }

            Long timestamp = Math.round((double)System.currentTimeMillis() / 1000);
            short minor = (short)beacon.getId3().toInt();
            tmpRangedBeacons.put(minor, timestamp);

            //if stickyMinor found, then restrict to only sticky minor
            if(minor == Globals.stickyMinor) {
                tmpRangedBeacons = new Hashtable<Short, Long>();
                tmpRangedBeacons.put(minor, timestamp);
                sticked = true;
                break;
            }
        }
        //System.out.println("@@@break point 1 ");


        //store oldest ranged beacon timestamps in rangedBeacons, but only save those beacons found in current ranging
        Hashtable<Short, Long> prevRangedBeacons = (Hashtable<Short, Long>)Globals.rangedBeacons.clone();
        Globals.rangedBeacons = new Hashtable<Short, Long>();
        for (Short key : tmpRangedBeacons.keySet()){
            if (prevRangedBeacons.containsKey(key)){
                Globals.rangedBeacons.put(key, prevRangedBeacons.get(key));
            }
            else{
                Globals.rangedBeacons.put(key, tmpRangedBeacons.get(key));
            }
        }
        //System.out.println("@@@break point 2 ");

        //if devices go out of range and come back into range, allow sending
        if(!sticked) {
            Globals.stickyMinor = -1;

            for (Short key : prevRangedBeacons.keySet()){
                if (!Globals.rangedBeacons.containsKey(key)){
                    Globals.lastSend.remove(key);
                }
            }
        }

        //System.out.println("@@@break point 3 ");

        scanForSignificantConnection();
        System.out.println("@@@leave beacons() ");

    }
}

class beaconComp implements Comparator<Beacon>{
    @Override
    public int compare(Beacon b1, Beacon b2) {
        if(b1.getRssi() < b2.getRssi()){
            return 1;
        }
        else if(b1.getRssi() > b2.getRssi()){
            return -1;
        }
        else{
            return 0;
        }
    }
}
