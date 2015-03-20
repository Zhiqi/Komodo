package edu.umich.eecs.cooties;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.umich.imlc.collabrify.client.CollabrifyParticipant;


public class TeacherActivity_List extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_activity_list);

        ArrayList<String> playerList = new ArrayList<String>();
        //for (CollabrifyParticipant player : Globals.mysession.participants()) {
            //System.out.println(player.toString());
            //if (player.getId() != Globals.selfId && Globals.playerInfo.containsKey(player.getId()))
              //  playerList.add(Globals.playerInfo.get(player.getId()).name);


        //    playerList.add(String.valueOf(player.getId()));
        //}
        for (long k : Globals.playerInfo.keySet()){
            playerList.add(Globals.playerInfo.get(k).name); // + " ; participant id is " + String.valueOf(k));
        }

        ArrayAdapter<String> codeLearnArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerList);
        ListView codeLearnLessons = (ListView)findViewById(R.id.listView);
        codeLearnLessons.setAdapter(codeLearnArrayAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher_activity__lobby, menu);
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
