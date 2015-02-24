package edu.umich.eecs.cooties;

import android.app.Activity;
import android.bluetooth.le.AdvertiseSettings;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;


public class TeacherActivity extends Activity {

    private BeaconTransmitter beaconTransmitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//        TextView textView = new TextView(this);
//        textView.setTextSize(40);
//        textView.setText(message);
//        setContentView(textView);

        setContentView(R.layout.activity_teacher);
        Toast.makeText(getApplicationContext(), "Output customer: "+Globals.customer, Toast.LENGTH_LONG).show();

    }





    public void startBLE(View view) {

        if(beaconTransmitter != null){
            //bluetooth may be off
            return;
        }

        int result = BeaconTransmitter.checkTransmissionSupported(getApplicationContext());
        if(result == BeaconTransmitter.SUPPORTED) {
            Toast.makeText(getApplicationContext(), "Beacon Supported\n Starting Transmission", Toast.LENGTH_SHORT).show();

            Beacon beacon = new Beacon.Builder()
                    .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                    .setId2("12345")
                    .setId3("54321")
                    .setManufacturer(0x4c00)
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[]{0l}))
                    .build();
            BeaconParser beaconParser = new BeaconParser()
                    // altbeacon
                    // .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");

                    //ibeacon
                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
            beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
            beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
            beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
            beaconTransmitter.startAdvertising(beacon);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher, menu);
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
}
