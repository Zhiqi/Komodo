package edu.umich.eecs.cooties;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.umich.imlc.collabrify.client.CollabrifyListener;
import edu.umich.imlc.collabrify.client.CollabrifyParticipant;
import edu.umich.imlc.collabrify.client.exceptions.CollabrifyException;




//Session now created - if stopped (window closed), session will be ended
public class TeacherActivity_2_Pre_Lobby extends Activity implements AdapterView.OnItemSelectedListener, CollabrifyListener.CollabrifyLeaveSessionListener {

    private static final String TAG = "TeacherActivity_2";


    public long incubation_time = 0;
    public long hide_health = 1;
    final public ArrayList<Long> infected_users_list = new ArrayList<Long>();

    @Override
    public void onStop(){
        super.onStop();
        end_Session();

    }

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
        incubation_time = 0;
        // Another interface callback
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_activity_2);


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


    //show health check box
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.show_infection_checkBox:
                if (checked)
                    hide_health = 1;
                else
                    hide_health = 0;
                break;
        }
    }

    public void infected_user_selection(View view){

        ArrayList<String> names = new ArrayList<String>();

        final ArrayList<CollabrifyParticipant> participants = new ArrayList(Globals.mysession.participants());

        for (int i = 0; i<participants.size(); i++){

            //remove teacher
            if(participants.get(i).getId() == Globals.myclient.currentSessionParticipantId()){
                participants.remove(i);
                break;
            }
        }


        if(participants.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Nobody has joined your session yet!", Toast.LENGTH_LONG)
                    .show();
            return;

        }

        //build array of strings for popup
        for(CollabrifyParticipant a : participants){
            names.add(a.getDisplayName());
        }
        final String[] names_ar = names.toArray(new String[names.size()]);


        //this will hold names of selected users
        final ArrayList<Integer> selList=new ArrayList();

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Select 1 or More Infected Users");
        ad.setMultiChoiceItems(names_ar, null, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                if(arg2)
                {
                    // If user select a item then add it in selected items
                    selList.add(arg1);
                }
                else if (selList.contains(arg1))
                {
                    // if the item is already selected then remove it
                    selList.remove(Integer.valueOf(arg1));
                }
            }
        });

        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //reset infected users list in case of multiple attempts
                infected_users_list.clear();

                for(int a : selList){
                    System.out.println("Adding name: "+participants.get(a).getDisplayName()+" id:"+participants.get(a).getId() + "as infected user user");
                    infected_users_list.add(participants.get(a).getId());
                }

                String msg="";
                for (int i = 0; i < infected_users_list.size(); i++) {
                    msg=msg+"\n"+ infected_users_list.get(i);
                }
                Toast.makeText(getApplicationContext(),
                        "Total "+ infected_users_list.size() +" Items Selected.\n"+ msg , Toast.LENGTH_LONG)
                        .show();

                selList.clear();

            }
        });

        ad.show();





    }




    //function for button - sends out basefile depending on options and then starts
    public void startSimulation(View view) {

        //require selection of infected users - no randoms now
        if(infected_users_list.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Please Select 1 or more users to initially infect!" , Toast.LENGTH_LONG)
                    .show();
            return;
        }

        //determine major based on gamesession -> send as int
        Globals.major = (int)(Globals.mysession.id() % Short.MAX_VALUE);
        if(Globals.major == 0){
            Globals.major += 1;
        }


        BaseFileMessage msg = new BaseFileMessage();


        //not currently working
        try{
            msg.initWithInfectedUser(infected_users_list,incubation_time,Globals.major,hide_health);
        }
        catch(Exception e){
            Log.e(TAG, "initializing base msg",e);
        }


        Globals.myclient.broadcast(msg.outputBuffer(), "initialSettings", Globals.model);

        Intent intent = new Intent(this, TeacherActivity_3_In_Game_Lobby.class);
        startActivity(intent);

        //end this activity
        finish();

    }


    //makes teacher leave session, destroying the collabrify session
    //callback is onDisconnect
    public void end_Session(){
        try{
            Globals.myclient.leaveSession(true, this);
        }
        catch(Exception e){

        }

    }

    //button for cancel
    public void cancelSimulation(View view) {
        end_Session();
    }


    //callback for end_session leaveSession which will destroy since we are in teacher
    @Override
    public void onDisconnect() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Session destroyed", Toast.LENGTH_SHORT).show();
            }
        });

        System.out.println("destroy session complete");
        finish();
    }

    @Override
    public void onError(CollabrifyException e) {
        System.out.println("destroy session error");
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
