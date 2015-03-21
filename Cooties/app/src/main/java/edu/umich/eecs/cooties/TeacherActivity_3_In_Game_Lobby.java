package edu.umich.eecs.cooties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;


public class TeacherActivity_3_In_Game_Lobby extends Activity implements CollabrifyListener.CollabrifyLeaveSessionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_3);
    }

    public void showStudents(View view) {
        Intent intent = new Intent(this, TeacherActivity_4_Student_List.class);
        startActivity(intent);
    }


    public void showHistory(View view) {
        Intent intent = new Intent(this, TeacherActivity_5_History.class);
        startActivity(intent);
        //finish();
    }

    public void end_Session(){
        try{
            Globals.myclient.leaveSession(true, this);
            Globals.mysession = null;
        }
        catch(Exception e){
            Log.e("Teacher_Activity_3", "End Session Exception", e);
        }

    }

    //go back to session creation
    public void stopSimulation(View view) {
        byte [] msg = new byte[1];
        Globals.myclient.broadcast(msg, "StopSim", Globals.model);
        end_Session();
    }

//    public void onStop(){
////        end_Session();
//        super.onStop();
//    }

    @Override
    public void onDisconnect() {
        Log.d("TA_3_InGameLobby", "destroy session complete");
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Session destroyed", Toast.LENGTH_LONG).show();
            }
        });

//        finish();

    }

    @Override
    public void onError(CollabrifyException e) {
        Log.e("TA_3_InGameLobby", "disconnect error",e);
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

}
