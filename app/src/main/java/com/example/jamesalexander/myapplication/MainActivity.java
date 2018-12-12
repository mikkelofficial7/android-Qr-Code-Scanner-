package com.example.jamesalexander.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jamesalexander.myapplication.Model.Event;
import com.example.jamesalexander.myapplication.Model.EventMember;
import com.example.jamesalexander.myapplication.Model.GetEvent;
import com.example.jamesalexander.myapplication.Model.GetEventMember;
import com.example.jamesalexander.myapplication.Model.NewMember;
import com.example.jamesalexander.myapplication.Model.PostPutDelEventMember;
import com.example.jamesalexander.myapplication.Model.PostPutDelNewMember;
import com.example.jamesalexander.myapplication.Rest.ApiClient;
import com.example.jamesalexander.myapplication.Rest.ApiInterface;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // View Object
    private GPSTracker gpsTracker;
    ApiInterface mApiInterface;
    private Button buttonScan, buttonSubmit, buttonLocation, buttonScanAbsen;
    private TextView textViewEventName, textViewOrganizer, textViewEventAddress, textViewEventID, textViewPICEmail, textViewPICPhone, textView1, textView2, textView3, textView4, textView5, textView6, tvLocation;

    //qr code scanner object
    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check session
        SharedPreferences myPrefs = getSharedPreferences(RegistActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String username = myPrefs.getString("userid",null);
        if(username == null)
        {
            Intent intent = new Intent(MainActivity.this, RegistActivity.class);
            startActivity(intent);
        }

        // initialize object
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScanAbsen = (Button) findViewById(R.id.buttonScanAbsent);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonLocation = (Button) findViewById(R.id.buttonGetLocation);
        textViewEventName = (TextView) findViewById(R.id.textViewEventName);
        textViewOrganizer = (TextView) findViewById(R.id.textViewOrganizer);
        textViewEventAddress = (TextView) findViewById(R.id.textViewEventAddress);
        textViewEventID = (TextView) findViewById(R.id.textViewEventID);
        textViewPICEmail = (TextView) findViewById(R.id.textViewPicEmail);
        textViewPICPhone= (TextView) findViewById(R.id.textViewEventPICPhone);
        textView1= (TextView) findViewById(R.id.textView);
        textView2= (TextView) findViewById(R.id.textView7);
        textView3= (TextView) findViewById(R.id.textView8);
        textView4= (TextView) findViewById(R.id.textView9);
        textView5= (TextView) findViewById(R.id.textView10);
        textView6= (TextView) findViewById(R.id.textView11);
        tvLocation= (TextView) findViewById(R.id.textViewUserLocation);

        textView1.setText(myPrefs.getString("userid",null));
        textView2.setText(myPrefs.getString("username",null));
        textView3.setText(myPrefs.getString("userphone",null));
        textView4.setText(myPrefs.getString("useroccupation",null));
        textView5.setText(myPrefs.getString("useremail",null));
        textView6.setText(myPrefs.getString("userworkplace",null));

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        // attaching onclickListener
        buttonScan.setOnClickListener(this);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation(v);
            }
        });

        buttonScanAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AbsentActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("yyyy-dd-MM", Locale.getDefault()).format(new Date());
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                InsertData(textView1.getText().toString(), textViewEventID.getText().toString(), textViewEventName.getText().toString(), date+" "+hour+":"+minute+":"+second, tvLocation.getText().toString(), "TIDAK");
            }
        });
        try
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
            {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getLocation(View view){
        gpsTracker = new GPSTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            //get exact address from long-lat
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MainActivity.this);

                if (latitude != 0 || longitude != 0) {
                    addresses = geocoder.getFromLocation(latitude ,
                            longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getAddressLine(1);
                    String country = addresses.get(0).getAddressLine(2);
                    tvLocation.setText(address);
                }
                else {
                    progressdialog("Sorry! Your Location isn't found");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    // Mendapatkan hasil scan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show();
            }else{
                // jika qrcode berisi data
                try{
                    // converting the data json
                    JSONObject object = new JSONObject(result.getContents());
                    // atur nilai ke textviews
                    textViewEventName.setText(object.getString("nama_event"));
                    textViewOrganizer.setText(object.getString("organizer"));
                    textViewEventAddress.setText(object.getString("alamat_event"));
                    textViewEventID.setText(object.getString("id_event"));
                    textViewPICEmail.setText(object.getString("PIC_email"));
                    textViewPICPhone.setText(object.getString("PIC_phone"));
                }catch (JSONException e){
                    e.printStackTrace();
                    // jika format encoded tidak sesuai maka hasil
                    // ditampilkan ke toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        // inisialisasi IntentIntegrator(scanQR)
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences sharedpreferences = getSharedPreferences(RegistActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                intent = new Intent(MainActivity.this, RegistActivity.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void InsertData(final String id_member, final String id_event, final String nama_event, final String tgl_daftar, final String alamat, final String status_hadir) {
        if(id_member.equals("none") || id_event.equals("none") || nama_event.equals("none") || tgl_daftar.isEmpty() || alamat.equals("none") || status_hadir.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Sorry! You Have to Scan QR or Get Location First!",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            Call<GetEvent> EventMemberCalls = mApiInterface.getEvent(id_event);
            final int i = 0;
            EventMemberCalls.enqueue(new Callback<GetEvent>() {
                @Override
                public void onResponse(Call<GetEvent> call, Response<GetEvent> response) {
                    List<Event> Events = response.body().getListEvent();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-M hh:mm:ss");
                    try
                    {
                        Date dateNow = sdf.parse(tgl_daftar);
                        Date dateStartReg = sdf.parse(Events.get(i).getStart_registration());
                        Date dateEndReg = sdf.parse(Events.get(i).getEnd_registration());
                        if (dateNow.getTime() <= dateEndReg.getTime() && dateNow.getTime() >= dateStartReg.getTime())
                        {
                            Call<GetEventMember> EventMemberCalls = mApiInterface.getEventMember(id_member, id_event);
                            int i = 0;
                            EventMemberCalls.enqueue(new Callback<GetEventMember>() {
                                @Override
                                public void onResponse(Call<GetEventMember> call, Response<GetEventMember> response) {
                                    List<EventMember> EventMemberList = response.body().getListNewMember();
                                    if (EventMemberList.size() == 0) {
                                        Call<PostPutDelEventMember> EventMemberCall = mApiInterface.postEventMember(id_member, id_event, tgl_daftar, alamat, status_hadir);
                                        EventMemberCall.enqueue(new Callback<PostPutDelEventMember>() {
                                            @Override
                                            public void onResponse(Call<PostPutDelEventMember> call, Response<PostPutDelEventMember> response) {

                                                progressdialog("You are registered to " + nama_event + " event. Thank you!");
                                            }

                                            @Override
                                            public void onFailure(Call<PostPutDelEventMember> call, Throwable t) {
                                                Toast.makeText(MainActivity.this, "Oops..You're Offline Now. Please Turn On The Connection!",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        progressdialog("You cannot register twice!");
                                    }
                                }

                                @Override
                                public void onFailure(Call<GetEventMember> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Oops..You're Offline Now. Please Turn On The Connection!",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else {
                            progressdialog("Oops..Date and time is expired! ");
                        }
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetEvent> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Oops..You're Offline Now. Please Turn On The Connection!",
                            Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    public void progressdialog(final String word)
    {
        final Toast ts = Toast.makeText(MainActivity.this, word, Toast.LENGTH_LONG);
        final int[] progressStatus = new int[1];
        final Handler handler = new Handler();
        // Initialize a new instance of progress dialog
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);

        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        pd.setTitle("Loading");
        pd.setMessage("Please Wait.........");

        // Set the progress dialog background color
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));

        pd.setIndeterminate(false);

        // Finally, show the progress dialog
        pd.show();

        // Set the progress status zero on each button click
        progressStatus[0] = 0;

        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus[0] < 100) {
                    // Update the progress status
                    progressStatus[0] += 1;

                    // Try to sleep the thread for 20 milliseconds
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Update the progress status
                            pd.setProgress(progressStatus[0]);
                            // If task execution completed
                            if (progressStatus[0] >= 100) {
                                // Dismiss/hide the progress dialog
                                pd.dismiss();
                                ts.show();
                            }
                        }
                    });
                }
            }
        }).start(); // Start the operation
    }
}