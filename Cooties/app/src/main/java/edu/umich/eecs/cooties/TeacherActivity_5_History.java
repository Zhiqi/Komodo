package edu.umich.eecs.cooties;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class TeacherActivity_5_History extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_5_history);

        ArrayList<String> historyList = new ArrayList<String>();
        if(Globals.historyList.isEmpty()){
            historyList.add("No Meetings Yet");
        }
        else{
            for (HistoryItem k : Globals.historyList){
                historyList.add(k.firstUser.name  + " and " + k.secondUser.name +" meet at " + k.timestamp); // + " ; participant id is " + String.valueOf(k));
            }

        }

        ArrayAdapter<String> historyArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historyList);
        ListView codeLearnLessons = (ListView)findViewById(R.id.historyListView);
        codeLearnLessons.setAdapter(historyArrayAdapter);
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
