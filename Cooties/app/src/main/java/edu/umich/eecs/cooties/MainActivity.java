package edu.umich.eecs.cooties;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import edu.umich.imlc.collabrify.client.CollabrifyClient;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    Globals global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Intent intent = new Intent(this, TeacherActivity_Initialize.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String username = editText.getText().toString();

        try {
            Globals.myclient = CollabrifyClient.newClient(getApplicationContext(), "NO@EMAIL.com", username,  "imlcteam@gmail.com", "2468", false);
        }
        catch (Exception a) {
            Log.e("new Client", "new Collabrify Client Error");
        }

        Globals.myclient.setSessionListener(Globals.model);
        if (username.equals("T")) {
            intent = new Intent(this, TeacherActivity_Initialize.class);
        }
        else {
            intent = new Intent(this, StudentJoinActivity.class);
        }


        intent.putExtra(EXTRA_MESSAGE,username);

        startActivity(intent);
        finish();
    }

}
