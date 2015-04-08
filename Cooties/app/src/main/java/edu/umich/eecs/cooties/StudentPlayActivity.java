package edu.umich.eecs.cooties;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Arrays;
import java.util.Collection;

import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;


public class StudentPlayActivity extends Activity implements BeaconConsumer, CollabrifyListener.CollabrifyLeaveSessionListener, StudentPlayFragment_Infected.OnFragmentInteractionListener, StudentPlayFragment_Not_Infected.OnFragmentInteractionListener {

    private BeaconManager beaconManager;
    private BeaconTransmitter beaconTransmitter;
    private String minor;

    private StudentBroadcastListener broadcastListener; // use this for broadcast

    private SearchBeacon searchBeacon; // select matched device


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_play);
        Globals.studentPlayActivity = this;

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.spFragment);
        if (fragment == null) {

            if(Globals.infected_status){
                fragment = new StudentPlayFragment_Infected();
            }
            else{
                fragment = new StudentPlayFragment_Not_Infected();
            }

            fm.beginTransaction()
                    .add(R.id.spFragment, fragment)
                    .commit(); }

        //NEED TO WAIT UNTIL THIS THREAD HAS BASEFILE BROADCAST MESSAGE BEFORE STARTING,
        //BECAUSE ORDER SHOULD BE LIKE THIS
        //TEACHER INITIALIZE
        //THEN STUDENT JOIN SESSION
        //THEN TEACHER PICK PARAMETERS AND SEND
        //THEN STUDENTS CAN START PLAYING

//        while(true){
//            try{
//                Thread.sleep(1000);
//                if()
//
//            }
//            catch(Exception e){
//                log.e("SPA", "Sleep Exception")
//            }
//
//        }



        verifyBluetooth();

        broadcastListener = new StudentBroadcastListener();
        minor = broadcastListener.announcePlayer();
        searchBeacon = new SearchBeacon();
        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);

        startBLE();
    }

    //will update the ui with infected fragment
    public void showInfected() {

        // Create new fragment and transaction
        Fragment newFragment = new StudentPlayFragment_Infected();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.spFragment, newFragment);

        // Commit the transaction
        transaction.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }

            });
            builder.show();
        }
    }

    //callback for beacon receiver initialization - specifies how received beacons are handled and begins receives
    @Override
    public void onBeaconServiceConnect() {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(), "Beacon service connected:", Toast.LENGTH_SHORT).show();
            }
        });
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {

                    //add and search received beacons for any significant connections
                    searchBeacon.beacons(beacons);
                }
            }

        });
        try {

            Identifier uuid = Identifier.parse(Globals.bt_uuid.toString());
            Identifier maj = Identifier.fromInt(Globals.major);
            Identifier min = Identifier.fromInt(Integer.parseInt(minor));
            Log.d("SPA", "Beacon Service Beacon Range ");
            Log.d("SPA", "uuid is "+uuid.toUuidString());
            Log.d("SPA", "maj is "+maj.toString());
            Log.d("SPA", "min is "+min.toString());

            beaconManager.startRangingBeaconsInRegion(new Region("myApplicationRanger", uuid, maj, null));
//            beaconManager.startRangingBeaconsInRegion(new Region("myApplicationRanger", uuid, maj, null));
//                    new Region("myRangingUniqueI",null),null,null)
        } catch (RemoteException e) {
        Log.e("SPA", "Beacon Service Connect Start Ranging error", e);
        }
    }


    public void startBLE(View view) {

        //Verify receipt of basemsg
        if(Globals.major == 0){
            Toast.makeText(getApplicationContext(), "FAIL! \nHaven't received basefile yet", Toast.LENGTH_LONG).show();
            return;
        }




        if(beaconTransmitter != null){
            //bluetooth may be off
            return;
        }

        int result = BeaconTransmitter.checkTransmissionSupported(getApplicationContext());
        if(result == BeaconTransmitter.SUPPORTED) {
            //Toast.makeText(getApplicationContext(), "Beacon Supported\n Starting Transmission", Toast.LENGTH_SHORT).show();
            System.out.println("transmit iBeacon");
            Beacon beacon = new Beacon.Builder()
                    .setId1(Globals.bt_uuid.toString())
                    .setId2(String.valueOf(Globals.major))
                    .setId3(minor)
                    .setManufacturer(0x4c00)
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[]{0l}))
                    .build();
            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
            beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
            //beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
            //beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            System.out.println("transmit iBeacon start:" + beaconTransmitter.isStarted());
            //Toast.makeText(getApplicationContext(), "transmit iBeacon start:" + beaconTransmitter.isStarted(), Toast.LENGTH_SHORT).show();
            AdvertiseCallback callback = new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    super.onStartSuccess(settingsInEffect);
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "transmit iBeacon start: " + beaconTransmitter.isStarted(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            beaconTransmitter.startAdvertising(beacon, callback);


        }
        else if(result == BeaconTransmitter.NOT_SUPPORTED_MIN_SDK
                || result == BeaconTransmitter.NOT_SUPPORTED_BLE
                || result == BeaconTransmitter.NOT_SUPPORTED_MULTIPLE_ADVERTISEMENTS
                || result == BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER){
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(getApplicationContext(), "Beacon Not Supported\n on This Device", Toast.LENGTH_LONG).show();
                    finish();

                }
            });
        }

    }

    public void startBLE() {

        //Verify receipt of basemsg
        if(Globals.major == 0){
            Toast.makeText(getApplicationContext(), "FAIL! \nHaven't received basefile yet", Toast.LENGTH_LONG).show();

            return;
        }




        if(beaconTransmitter != null){
            //bluetooth may be off
            return;
        }

        int result = BeaconTransmitter.checkTransmissionSupported(getApplicationContext());
        if(result == BeaconTransmitter.SUPPORTED) {
            //Toast.makeText(getApplicationContext(), "Beacon Supported\n Starting Transmission", Toast.LENGTH_SHORT).show();
            System.out.println("transmit iBeacon");
            Beacon beacon = new Beacon.Builder()
                    .setId1(Globals.bt_uuid.toString())
                    .setId2(String.valueOf(Globals.major))
                    .setId3(minor)
                    .setManufacturer(0x4c00)
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[]{0l}))
                    .build();
            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
            beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
            //beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
            //beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            System.out.println("transmit iBeacon start:" + beaconTransmitter.isStarted());
            //Toast.makeText(getApplicationContext(), "transmit iBeacon start:" + beaconTransmitter.isStarted(), Toast.LENGTH_SHORT).show();
            AdvertiseCallback callback = new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    super.onStartSuccess(settingsInEffect);
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "transmit iBeacon start: " + beaconTransmitter.isStarted(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            beaconTransmitter.startAdvertising(beacon, callback);


        }
        else if(result == BeaconTransmitter.NOT_SUPPORTED_MIN_SDK
                || result == BeaconTransmitter.NOT_SUPPORTED_BLE
                || result == BeaconTransmitter.NOT_SUPPORTED_MULTIPLE_ADVERTISEMENTS
                || result == BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER){
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(getApplicationContext(), "Beacon Not Supported\n on This Device", Toast.LENGTH_LONG).show();
                    finish();

                }
            });
        }

    }

    public void endBLE(View view) {
        if(beaconTransmitter != null){
            beaconTransmitter.stopAdvertising();
            beaconTransmitter = null;
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(getApplicationContext(), "Beacon Off", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void endBLE() {
        if(beaconTransmitter != null){
            beaconTransmitter.stopAdvertising();
            beaconTransmitter = null;
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(getApplicationContext(), "Beacon Off", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void leaveSession(View view) {
        try {
            Globals.myclient.leaveSession(false, this);
            Globals.mysession = null;
            endBLE(view);
        }
        catch(Exception e) {

        }
    }

    public void leaveSession() {
        try {
            Globals.myclient.leaveSession(false, this);
            Globals.mysession = null;
            endBLE();
        }
        catch(Exception e) {

        }
    }

    @Override
    public void onDisconnect() {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(), "You have left the session", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
        System.out.println("Leave Session");
    }

    @Override
    public void onError(CollabrifyException e) {
        System.out.println("Leave session error"+e.toString());
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
