package edu.umich.eecs.cooties;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import edu.umich.imlc.collabrify.client.CollabrifyClient;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        try {
////            Globals.myclient = CollabrifyClient.newClient(getApplicationContext(), "gmail", "display_name", "collabrify.tester@gmail.com", "4891981239025664", false);
////            Globals.myclient.requestSessionList();
//
//        }
//        catch (CollabrifyException a) {
//
//        }



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
        Intent intent = new Intent(this, TeacherActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String username = editText.getText().toString();


        if (username.equals("T")) {
            intent = new Intent(this, TeacherActivity.class);
        }
        else {
            intent = new Intent(this, StudentJoinActivity.class);
        }

        Globals.customer = username;

        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
    }

}
