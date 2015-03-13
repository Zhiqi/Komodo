package edu.umich.eecs.cooties;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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


public class StudentJoinActivity extends Activity implements CollabrifyListener.CollabrifyListSessionsListener, CollabrifyListener.CollabrifyJoinSessionListener {

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

    private long sessionId;
    private String sessionName;

    @Override
    public void onReceiveSessionList(final List<CollabrifySession> sessionList)
    {
        System.out.println("Receiving Session List in Student Activity");
        if( sessionList.isEmpty())
        {
            System.out.println("No Session Available using Tags"+ Globals.tags.get(0));
//            Log.i("CCO", "No Session Available using Tags: " + clientListener.tags.get(0));...........
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
        builder.setTitle("Choose a session");
        builder.setItems(
                sessionNames.toArray(new String[sessionList.size()]),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            sessionId = sessionList.get(which).id();
                            sessionName = sessionList.get(which).name();
                            Globals.myclient.joinSession(sessionId, null, StudentJoinActivity.this);
                        } catch (Exception e) {
                            System.out.println( "session not joined");
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
        Log.e("Student Activity Error", "error", e);

    }

    @Override
    public void onSessionJoined(CollabrifySession collabrifySession) {
        // After join the session successfully, set some variables in Globals
        Globals.mysession = collabrifySession;
        Globals.selfId = Globals.myclient.currentSessionParticipantId();
        Intent intent = new Intent(this, StudentPlayActivity.class);

        startActivity(intent);
        finish();
    }
}
