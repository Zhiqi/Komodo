package edu.umich.eecs.cooties;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
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
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Arrays;
import java.util.Collection;


public class StudentPlayActivity extends Activity implements BeaconConsumer {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    private BeaconManager beaconManager;
    private BeaconTransmitter beaconTransmitter;
    private String minor;
    private StudentBroadcastListener broadcastListener; // use this for broadcast


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_play);

        /*
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            username =(String) b.get(EXTRA_MESSAGE);
            // Toast.makeText(getApplicationContext(), "username is " + username ,Toast.LENGTH_SHORT).show();

        }
        */

        verifyBluetooth();

        broadcastListener = new StudentBroadcastListener();
        minor = broadcastListener.announcePlayer();

        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
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

    @Override
    public void onBeaconServiceConnect() {
        System.out.println("Beacon service connected:");
        Toast.makeText(getApplicationContext(), "Beacon service connected:", Toast.LENGTH_SHORT).show();

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                //System.out.println("Start Beacon rangingkkkk");

                if (beacons.size() > 0) {
                    //Toast.makeText(getApplicationContext(), "Beacon detected", Toast.LENGTH_SHORT).show();
                    //("Beacon detected");

                    //EditText editText = (EditText)StudentPlayActivity.this
                    //      .findViewById(R.id.rangingText);

                    //System.out.println("The first beacon "+firstBeacon.toString()+" is about "+firstBeacon.getDistance()+" meters away.");
                    logToDisplay("@@@@@number of beacons detected is: " + beacons.size());
                    Beacon [] beaconArr = beacons.toArray(new Beacon[beacons.size()]);
                    for(Beacon beacon : beaconArr) {
                        logToDisplay("Beacon with name " + beacon.getId2() + " is about " + beacon.getDistance() + " meters away.");
                    }
                }
            }

        });
        //startBLE();
        try {
            System.out.println("attempt to Start Beacon ranging");
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId",null,null,null));
//                    Identifier.parse("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6"),
            //                  Identifier.parse("12345"), Identifier.parse("54321")));

        } catch (RemoteException e) {   }
    }

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                EditText editText = (EditText)StudentPlayActivity.this
                        .findViewById(R.id.rangingText);
                editText.append(line+"\n");
            }
        });
    }

    public void startBLE(View view) {

        if(beaconTransmitter != null){
            //bluetooth may be off
            return;
        }

        int result = BeaconTransmitter.checkTransmissionSupported(getApplicationContext());
        if(result == BeaconTransmitter.SUPPORTED) {
            Toast.makeText(getApplicationContext(), "Beacon Supported\n Starting Transmission", Toast.LENGTH_SHORT).show();
            System.out.println("transmit iBeacon");
            Beacon beacon = new Beacon.Builder()
                    .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                    .setId2(minor)
                    .setId3("54321")
                    .setManufacturer(0x4c00)
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[]{0l}))
                    .build();
            BeaconParser beaconParser = new BeaconParser()
                    // altbeacon
                    // .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");

                    //ibeacon
                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
            beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
            //beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
            //beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            System.out.println("transmit iBeacon start:" + beaconTransmitter.isStarted());
            Toast.makeText(getApplicationContext(), "transmit iBeacon start:" + beaconTransmitter.isStarted(), Toast.LENGTH_SHORT).show();
            AdvertiseCallback callback = new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    super.onStartSuccess(settingsInEffect);

                    Toast.makeText(getApplicationContext(), "transmit iBeacon startiii:" + beaconTransmitter.isStarted(), Toast.LENGTH_SHORT).show();

                }
            };
            beaconTransmitter.startAdvertising(beacon, callback);


        }
        else if(result == BeaconTransmitter.NOT_SUPPORTED_MIN_SDK
                || result == BeaconTransmitter.NOT_SUPPORTED_BLE
                || result == BeaconTransmitter.NOT_SUPPORTED_MULTIPLE_ADVERTISEMENTS
                || result == BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER){
            Toast.makeText(getApplicationContext(), "Beacon Not Supported\n on This Device", Toast.LENGTH_SHORT).show();

        }

    }

    public void endBLE(View view) {
        if(beaconTransmitter != null){
            beaconTransmitter.stopAdvertising();
            beaconTransmitter = null;
            Toast.makeText(getApplicationContext(), "Beacon Off", Toast.LENGTH_SHORT).show();
        }
    }

}
