package edu.umich.eecs.cooties;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class TeacherActivity_4_Student_List extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<String> active_players = new ArrayList<String>();
        ListView players_list = null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_4_current_users);


        players_list = (ListView)findViewById(R.id.listView);

        if(Globals.playerInfo.isEmpty()){
            active_players.add("No Playerinfo Received");

        }
        else{
            for(Long a : Globals.playerInfo.keySet()){


                if(Globals.infected_status_by_player.get(a) == true){
                    active_players.add(Globals.playerInfo.get(a).name + ":\tINFECTED");
                }
                else{
                    active_players.add(Globals.playerInfo.get(a).name+":\tNOT INFECTED");
                }
            }

        }
        Toast.makeText(getApplicationContext(), "Found " + Globals.playerInfo.size() + " students", Toast.LENGTH_LONG ).show();
        System.out.println("Found " + Globals.playerInfo.size() + " students");
        ArrayAdapter<String> active_players_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, active_players);
        players_list.setAdapter(active_players_adapter);


    }

    //close this activity
    protected void onStop(){
        super.onStop();
//        finish();
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
