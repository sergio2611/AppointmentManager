package com.example.sergio.apptmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.text.format.DateFormat;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

import static com.example.sergio.apptmanager.ApptList.mDatabase;

public class insertAppt extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    TextView tvDateTime;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    Button btnAdd,btnCancel,btnShow;
    Spinner TType,TProv;
    EditText Location, Doctor, Address;
    CheckBox Wheel;
    String LocationName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_appt);

        Location = (EditText) findViewById(R.id.txtLocation);
        Doctor = (EditText) findViewById(R.id.txtDoctor);
        Address = (EditText) findViewById(R.id.txtAddress);
        Wheel = (CheckBox) findViewById(R.id.checkBox);
///getting date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String strDate = sdf.format(cal.getTime());
        tvDateTime = (TextView) findViewById(R.id.txtDateTime);
        tvDateTime.setText(strDate);


        ///end getting date
/*
        ////autocomplete google.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Address.setText(place.getAddress());
                LocationName = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.

            }
        });
        ////

*/

        //the spinners
        TType = (Spinner) findViewById(R.id.spinnerTransportationType);
        TProv = (Spinner) findViewById(R.id.spinnerTransportationProvider);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.TransportationType, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.transportationProvider, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // Apply the adapter to the spinner
        TType.setAdapter(adapter);
        TProv.setAdapter(adapter2);

        //END SPINNERS
        ///datetime dialog on a textbox

        tvDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(insertAppt.this, insertAppt.this, year, month, day);
                datePickerDialog.show();
            }
        });

        ///buttons save and cancel

        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnCancel = (Button) findViewById(R.id.btnBack);
        btnShow = (Button) findViewById(R.id.btnopen);

        btnShow.setOnClickListener(new View.OnClickListener() {

            public void  onClick(View view){

                String address = Address.getText().toString();
                if(address.equals(""))
                {
                    Toasty.info(insertAppt.this, "You need to enter the address before browsing the maps!", Toast.LENGTH_SHORT, true).show();
                }
                else {
                    String uri = String.format(Locale.ENGLISH, "geo:0,0?q=" + Address.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    insertAppt.this.startActivity(intent);
                }
            }
        });

        btnAdd .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save the app on the textFile and close, send a notification to the view to reload data
                Intent resultIntent = new Intent();

                //get all data and create an appointment

                String loc  = Location.getText().toString();//LocationName ;//Location.getText().toString();
                String doc  = Doctor.getText().toString();
                String addr = Address.getText().toString();
               // loc = "11552sw 17485";
                if(TextUtils.isEmpty(loc)) {
                    Toasty.error(insertAppt.this, "You need to enter Location!", Toast.LENGTH_SHORT, true).show();

                }
                else if(TextUtils.isEmpty(doc))
                {
                    Toasty.error(insertAppt.this, "You need to enter the Doctor!", Toast.LENGTH_SHORT, true).show();

                }
                else if (TextUtils.isEmpty(addr))
                {
                    Toasty.error(insertAppt.this, "You need to enter the address!", Toast.LENGTH_SHORT, true).show();

                }
                else
                {
                    Boolean hasWheel = Wheel.isChecked();
                    String provider = TProv.getSelectedItem().toString();
                    String type = TType.getSelectedItem().toString();
                    String Time = tvDateTime.getText().toString();
                    Random r = new Random();
                    String id = UUID.randomUUID().toString();

                    Appointment app = new Appointment(id,loc,Time,addr,type,provider,hasWheel,doc);
                    ApptList.Appt.add(app);

                    try {
                        mDatabase.child("appt").child(String.valueOf(app.getAppId())).setValue(app);
                        FileOutputStream fileOutputStream = openFileOutput("Appointments", Context.MODE_APPEND);
                        fileOutputStream.write(app.toString().getBytes());
                        fileOutputStream.write("\n".getBytes());
                        fileOutputStream.close();
                    }
                    catch (FileNotFoundException f){

                        f.printStackTrace(); //help with debugging
                    }
                    catch (IOException i){

                    }
                    boolean insert = true;
                    resultIntent.putExtra("inserted",  insert);
                    setResult(ApptList.RESULT_OK, resultIntent);
                    finish();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(ApptList.RESULT_CANCELED, resultIntent);
                finish();
            }
        });



    }

    //helper to insert non duplicated ID
  /*  public boolean isInList(int ID)
    {
        boolean found = false;
        int count = ApptList.Appt.size();
        int i = 0;
        while( !found && i < count)
        {
            if(ApptList.Appt.get(i).getAppId() == ID)
                found = true;

            i++;
        }

        return found;
    }*/



    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 +1;
        dayFinal = i2;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(insertAppt.this, insertAppt.this, hour, minute,false);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        hourFinal = i;
        minuteFinal = i1;

        tvDateTime.setText(monthFinal + "/" + dayFinal + "/" + yearFinal +" " + hourFinal + ":" + minuteFinal);
    }
}
