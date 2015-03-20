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

import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.CollabrifySession;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;


public class TeacherActivity_1_Initialize extends Activity implements CollabrifyListener.CollabrifyCreateSessionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_1);
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(), "Entered as Teacher: "+ Globals.username, Toast.LENGTH_LONG).show();
            }
        });

    }


    // After teacher enter a session name and click on "create",call this function
    // Start a new session and set the listener too
    public void create(View view) {
        EditText editText = (EditText) findViewById(R.id.sessionName);
        String sessionName = editText.getText().toString();

        try {
            //System.out.println("Placeholder for create session");
            Globals.myclient.createSession(sessionName,Globals.tags,null,0,false,this);

        }
        catch(Exception a){
            Log.e("TA1","@@@create exception",a);
        }


    }

    //session confirm callback which will trigger next activity
    @Override
    public void onSessionCreated(final CollabrifySession session){
        Globals.mysession = session;
        Globals.selfId = session.owner().getId();
        System.out.println("@@@session created with id " + Globals.mysession.id());
        Intent intent = new Intent(this, TeacherActivity_2_Pre_Lobby.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(CollabrifyException e) {
        Log.e("TA1","@@@create exception2",e);
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
