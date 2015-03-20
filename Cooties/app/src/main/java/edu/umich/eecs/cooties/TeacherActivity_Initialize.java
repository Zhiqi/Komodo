package edu.umich.eecs.cooties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.umich.imlc.collabrify.client.CollabrifyEvent;
import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.CollabrifyParticipant;
import edu.umich.imlc.collabrify.client.CollabrifySession;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;


public class TeacherActivity_Initialize extends Activity implements CollabrifyListener.CollabrifyCreateSessionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//        TextView textView = new TextView(this);
//        textView.setTextSize(40);
//        textView.setText(message);
//        setContentView(textView);

        setContentView(R.layout.activity_teacher_initialize);
        //Intent intent = getIntent();

        //can ignore this if using Globals structure
        //final String username = Globals.username;
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(), "Entered as Teacher: "+ Globals.username, Toast.LENGTH_LONG).show();
            }
        });


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

    // After teacher enter a session name and click on "create"
    // call this function
    // Start a new session and set the listener to
    public void create(View view) {
        //System.out.println("create start");

        EditText editText = (EditText) findViewById(R.id.sessionName);
        String sessionName = editText.getText().toString();


        try {
            //System.out.println("Placeholder for create session");
            Globals.myclient.createSession(sessionName,Globals.tags,null,0,false,this);

        }
        catch(Exception a){
            System.out.println("@@@create exception");
        }

        //System.out.println("create done");



    }

//    private long sessionID;
//    private String sessionName;
//
    @Override
    public void onSessionCreated(final CollabrifySession session)
    {

        Globals.mysession = session;
        Globals.selfId = session.owner().getId();
        System.out.println("@@@session created with id " + Globals.mysession.id());

        Intent intent = new Intent(this, TeacherActivity_Lobby.class);

        startActivity(intent);
        finish();

//        sessionID = session.id();
//        sessionName = session.name();


//        Toast.makeText(getApplicationContext(), "Session created, id: " + session.id(), Toast.LENGTH_LONG).show();
//        runOnUiThread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                showToast("Session created, id: " + session.id());
//                connectButton.setText(sessionName);
//            }
//        });



    }

    @Override
    public void onError(CollabrifyException e) {

    }


//    System.out.println("collabrify onerror");
//    Log.e("Teacher Activity Error", "error", e);
//
//
//    System.out.println("collabrify partiicpant joined:" + p.getDisplayName());
//    System.out.println(p.getDisplayName() + " has Left!");
//
//    System.out.println(" Collabrify session ended!:"+id);
//    this.finish();
//
//    System.out.println("basefile upload done ");
//
//    System.out.println("basefile received ");
//
//    System.out.println("event received ");
//
//    System.out.println("Further Joins Prevented ");

}
