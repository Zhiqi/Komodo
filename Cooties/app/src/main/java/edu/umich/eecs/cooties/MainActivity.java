package edu.umich.eecs.cooties;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import edu.umich.imlc.collabrify.client.CollabrifyClient;

public class MainActivity extends Activity {

    public void clearGlobals(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Globals.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void login(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TeacherActivity_1_Initialize.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String username = editText.getText().toString();

        try {
            Globals.myclient = CollabrifyClient.newClient(getApplicationContext(), "NO@EMAIL.com", username, Globals.login_email, Globals.login_id, false);
            Globals.myclient.setSessionListener(Globals.model);
        }
        catch (Exception a) {
            Log.e("new Client", "new Collabrify Client Error");
        }

        if (username.equals(Globals.TEACHER_NAME)) {
            intent = new Intent(this, TeacherActivity_1_Initialize.class);
        }
        else {
            intent = new Intent(this, StudentJoinActivity.class);
        }

        Globals.username = username;
        startActivity(intent);
        finish();
    }

}
