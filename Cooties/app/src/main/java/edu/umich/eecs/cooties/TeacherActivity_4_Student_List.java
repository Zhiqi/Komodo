package edu.umich.eecs.cooties;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import edu.umich.imlc.collabrify.client.CollabrifyParticipant;


public class TeacherActivity_4_Student_List extends Activity {

    ArrayList<String> active_players = new ArrayList<String>();
    private ListView players_list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_activity_4);
        players_list = (ListView)findViewById(R.id.listView);
        for(String a : Globals.active_players.values()){
            active_players.add(a);
        }
        System.out.println("Found " + Globals.active_players.size() + " students");
        ArrayAdapter<String> active_players_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, active_players);
        players_list.setAdapter(active_players_adapter);

//        ArrayList<String> playerList = new ArrayList<String>();

//        for (long k : Globals.playerInfo.keySet()){
//            playerList.add(Globals.playerInfo.get(k).name); // + " ; participant id is " + String.valueOf(k));
//        }


    }

    protected void onStop(){
        super.onStop();
        finish();


    }

//    public void add_additional_player(String new_player){
//        active_players.add(new_player);
//    }

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
