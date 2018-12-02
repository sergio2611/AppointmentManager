package com.example.sergio.apptmanager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import es.dmoral.toasty.Toasty;

public class ApptList extends AppCompatActivity {

    static ArrayList<Appointment> Appt;
    private RecyclerView recyclerView;
    String dateSelected ;
    private MyAdapter adapter;
    static DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appt_list);

        this.setTitle("Appointment Manager");
        ///LOAD ALL APPOINTMENTS
        Appt = new ArrayList<Appointment>();

        ///

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);


        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                //do something on change date



                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int month =cal.get(Calendar.MONTH) +1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String dateToCompare = month+"/"+day+"/" +year;
                dateSelected = dateToCompare;
                //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                //String strDate = sdf.format(date.toString());

                refreshAdapter(dateSelected);
                if(Appt.size() == 0)
                    Toasty.success(ApptList.this, "No Appointments for this day, Please insert one" , Toast.LENGTH_SHORT, true).show();
            }
        });


        Toasty.Config.getInstance().apply();

        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime());
        int year = cal.get(Calendar.YEAR);
        int month =cal.get(Calendar.MONTH) +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dateToCompare = month+"/"+day+"/" +year;
        FirebaseApp.initializeApp(this);
        //mAuth = FirebaseAuth.getInstance();
        //FirebaseUser currentUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        dateSelected = dateToCompare;
        ////read data
        //ReadData(dateSelected);

        ////////////////////

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("appt");
/*
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Appt.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Appointment post = postSnapshot.getValue(Appointment.class);
                    Appt.add(post);
                }
                //adapter.Notify(Appt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        ////////////////////
        ////
*/

        ///set the recycler view

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        adapter = new MyAdapter(Appt, this);
        //we have the adapter
        recyclerView.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addNewApp) {

            Intent i = new Intent(this, insertAppt.class);
            startActivityForResult(i,1);


        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Boolean insert = (Boolean) data.getSerializableExtra("inserted");
                Context context = getApplicationContext();
                int a = Appt.size();
                refreshAdapter(dateSelected);
                Toasty.success(this, "New App Inserted!", Toast.LENGTH_LONG, true).show();
            }
            else
            Toasty.success(this, "nothing inserted!", Toast.LENGTH_LONG, true).show();
        }
    }


    public void ReadData(String date)
    {
        Appt = new ArrayList<Appointment>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("appt");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
            Appointment post = postSnapshot.getValue(Appointment.class);
            String dat = post.startTime.substring(0,10);
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                    try {
                        Date apptdate = format.parse(dat);
                        Date datselect = format.parse(dateSelected);
                        if(apptdate.equals(datselect))
                            Appt.add(post);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                adapter.Notify(Appt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



        /*
        StringBuffer stringBuffer = new StringBuffer(); //append is useful with StringBuffer

        try {
            FileInputStream fileInputStream = openFileInput("Appointments");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String data;
            while((data = bufferedReader.readLine()) != null){
                String[] arr = data.split(";");
                String ID = arr[0];
                String Location = arr[1];
                String Start = arr[2];
                String dat = Start.substring(0,10);
                String Address = arr[3];
                String TType = arr[4];
                String TPro = arr[5];
                String Wheel = arr[6];
                String Doctor = arr[7];
                Appointment app = new Appointment(ID,Location,Start,Address,TType,TPro,Boolean.parseBoolean(Wheel),Doctor);
                if(dat.equals(date))
                  Appt.add(app);

            }
        }
        catch (FileNotFoundException f){
            f.printStackTrace();

        }
        catch (IOException i){

        }*/
    }

    public void refreshAdapter(String Date)
    {

        Appt.clear();
        ReadData(Date);
        adapter.Notify(Appt);
    }
}
