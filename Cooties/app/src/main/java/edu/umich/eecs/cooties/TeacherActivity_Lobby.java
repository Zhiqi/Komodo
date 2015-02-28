package edu.umich.eecs.cooties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;


public class TeacherActivity_Lobby extends Activity implements AdapterView.OnItemSelectedListener, CollabrifyListener.CollabrifyLeaveSessionListener {

    public int incubation_time = 0;
    public boolean show_infection_status = true;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        switch (pos){
            case 0: //0
                incubation_time = 0;
                break;
            case 1: //10 sec
                incubation_time = 10;
                break;
            case 2: //30 sec
                incubation_time = 30;

                break;
            case 3: //1 mim
                incubation_time = 60;

                break;
            case 4: //2 min
                incubation_time = 120;

                break;
            case 5: //5 minute
                incubation_time = 300;

                break;
            default:
                incubation_time = 0;

                break;

        }

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_activity_lobby);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.incubation_time, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.show_infection_checkBox:
                if (checked)
                    show_infection_status = true;
                else
                    show_infection_status = false;
                break;
        }
    }


    public void startSimulation(View view) {

        Intent intent = new Intent(this, TeacherActivity_In_Game_Lobby.class);
        startActivity(intent);
        finish();
//       Globals.myclient.leaveSession();
//
//    Globals.mysession.endSession();

        // Is the view now checked?
    }

    public void cancelSimulation(View view) {
        try{
            Globals.myclient.leaveSession(true, this);

        }

        catch(Exception e){

        }
        // Is the view now checked?
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

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onError(CollabrifyException e) {

    }
}
