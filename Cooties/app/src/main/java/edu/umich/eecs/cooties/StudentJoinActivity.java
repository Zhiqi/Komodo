package edu.umich.eecs.cooties;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.umich.imlc.collabrify.client.CollabrifyEvent;
import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.CollabrifyParticipant;
import edu.umich.imlc.collabrify.client.CollabrifySession;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;


public class StudentJoinActivity extends Activity implements CollabrifyListener.CollabrifyListSessionsListener, CollabrifyListener.CollabrifyJoinSessionListener, CollabrifyListener.CollabrifySessionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_join);
        try{
            Globals.myclient.requestSessionList(Globals.tags, this);

        }
        catch(Exception e){

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_join, menu);
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

    // After student select a session and click on "join" button
    // call this function
    // Join in that session and wait teacher to start the game (need to be done)
    public void join(View view) {
        System.out.println("join");

    }


    private long sessionId;
    private String sessionName;

    @Override
    public void onReceiveSessionList(final List<CollabrifySession> sessionList)
    {
        System.out.println("Receiving SEssion List in Student Activity");

        if( sessionList.isEmpty())
        {
            System.out.println("No Session Available using Tags"+ Globals.tags.get(0));
//            Log.i("CCO", "No Session Available using Tags: " + clientListener.tags.get(0));
//            runOnUiThread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    Toast.makeText(getBaseContext(), "No possible Sessions to Join", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            });
            return;
        }

        List<String> sessionNames = new ArrayList<String>();
        for(CollabrifySession s : sessionList)
        {
            sessionNames.add(s.name());
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Choose a session").setItems(
                sessionNames.toArray(new String[sessionList.size()]),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try
                        {


//                            long sessionId = sessionList.get(which).id();

                            sessionId = sessionList.get(which).id();
                            sessionName = sessionList.get(which).name();

                            Globals.myclient.joinSession(sessionId, null, true, StudentJoinActivity.this, StudentJoinActivity.this);

                        }
                        catch( Exception e)
                        {
                            Log.i("CCO", "Join Session Failed", e);
                            finish();
                        }
                    }
                });
        runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                try{
                    builder.show();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onError(CollabrifyException e)
    {
        System.out.println("Student Join Error");

//        Toast.makeText(getApplicationContext(), "Collabrify Error", Toast.LENGTH_LONG).show();

        Log.e("Student Activity Error", "error", e);

//        if(!e.isRecoverable())
//        {
//            //the client has been reset and we are no longer in a session, update UI
//        }
    }


    @Override
    public void onParticipantJoined(CollabrifyParticipant p)
    {
        System.out.println("collabrify partiicpant joined:" + p.getDisplayName());

//        Toast.makeText(getBaseContext(), p.getDisplayName() + " has Joined!", Toast.LENGTH_LONG).show();
//        panCakeLocal obj = localUndoStack.peek();
//        obj.run();
    }
    @Override
    public void onParticipantLeft(CollabrifyParticipant p)
    {
        System.out.println(p.getDisplayName() + " has Left!");

//        Toast.makeText(getBaseContext(), p.getDisplayName() + " has Left!", Toast.LENGTH_LONG).show();
    }


    public void onSessionEnd(long id)
    {
        System.out.println(" Collabrify session ended!:"+id);

//        Toast.makeText(getApplicationContext(), "Session ended:" +id, Toast.LENGTH_LONG).show();

        this.finish();
    }

    public void onBaseFileUploadComplete(long l){
        System.out.println("basefile upload done ");

//        Toast.makeText(getApplicationContext(), "Basefile Upload Complete", Toast.LENGTH_LONG).show();

    }

    public void onBaseFileReceived(java.io.File file){
        System.out.println("basefile received ");

//        Toast.makeText(getApplicationContext(), "Basefile Received", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onReceiveEvent(CollabrifyEvent collabrifyEvent) {

    }

    public void onReceiveEvent(long l, int i, java.lang.String s, byte[] bytes, long l1){
        System.out.println("event received ");

//        Toast.makeText(getApplicationContext(), "Event Received", Toast.LENGTH_LONG).show();

    }



    public void onFurtherJoinsPrevented(){
        System.out.println("Further Joins Prevented ");

//        Toast.makeText(getApplicationContext(), "Further Joins Prevented", Toast.LENGTH_LONG).show();



    }


    @Override
    public void onSessionJoined(CollabrifySession collabrifySession) {

    }
}
