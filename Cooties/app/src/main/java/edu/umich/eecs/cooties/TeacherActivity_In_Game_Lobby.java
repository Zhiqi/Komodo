package edu.umich.eecs.cooties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

import edu.umich.imlc.collabrify.client.CollabrifyEvent;
import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;


public class TeacherActivity_In_Game_Lobby extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_activity__in__game__lobby);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher_activity__in__game__lobby, menu);
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


    public void showStudents(View view) {
        Intent intent = new Intent(this, TeacherActivity_List.class);

        startActivity(intent);

    }

    public void showHistory(View view) {
        Intent intent = new Intent(this, TeacherActivity_History.class);

        startActivity(intent);

    }

    public void stopSimulation(View view) {
        CollabrifyListener.CollabrifyBroadcastListener listen;
        listen = new CollabrifyListener.CollabrifyBroadcastListener() {
            @Override
            public void onBroadcastDone(CollabrifyEvent collabrifyEvent) {
                //System.out.println("@@@Event broadcast done");
            }

            @Override
            public void onError(CollabrifyException e) {
                //System.out.println("@@@CollabrifyBroadcastListener error");
            }
        };

        //create base file
        byte[] data = new byte[1];
        Globals.myclient.broadcast(data, "StopSim", listen);
        Intent intent = new Intent(this, TeacherActivity_Lobby.class);

        startActivity(intent);

    }
}
