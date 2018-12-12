package com.example.jamesalexander.myapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jamesalexander.myapplication.Model.Event;
import com.example.jamesalexander.myapplication.Model.EventMember;
import com.example.jamesalexander.myapplication.Model.GetEvent;
import com.example.jamesalexander.myapplication.Model.GetEventMember;
import com.example.jamesalexander.myapplication.Model.PostPutDelEventMember;
import com.example.jamesalexander.myapplication.Rest.ApiClient;
import com.example.jamesalexander.myapplication.Rest.ApiInterface;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsentActivity extends AppCompatActivity implements View.OnClickListener{

    ApiInterface mApiInterface;
    private GPSTracker gpsTracker;
    private Button buttonscan, buttonLoc;
    private TextView textViewhadir, tvLoc;
    //qr code scanner object
    private IntentIntegrator intentIntegrator;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent);

        //check session
        SharedPreferences myPrefs = getSharedPreferences(RegistActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        username = myPrefs.getString("userid",null);
        if(username == null)
        {
            Intent intent = new Intent(AbsentActivity.this, RegistActivity.class);
            startActivity(intent);
        }

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        textViewhadir = (TextView) findViewById(R.id.textViewHadir);
        tvLoc = (TextView) findViewById(R.id.tvLoc);
        buttonLoc = (Button) findViewById(R.id.buttonLoc);
        buttonLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation(v);
            }
        });
        buttonscan = (Button) findViewById(R.id.buttonScans);
        buttonscan.setOnClickListener(this);
        //untuk tombol back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    textViewhadir.setText("Status Hadir Anda : "+object.getString("status_hadir"));
                    GetData(object.getString("id_event"));

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

    //action tombol back
    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(AbsentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void UpdateData(final String id_member, final String id_event, final String status_hadir) {
        if(id_member.isEmpty() || id_event.isEmpty() || status_hadir.contains("TIDAK ADA"))
        {
            Toast.makeText(AbsentActivity.this, "Oops..You Have To Absent Using QR Code!",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            Call<PostPutDelEventMember> EventMemberCalls = mApiInterface.putEventMember(id_member, id_event, status_hadir);
            int i = 0;
            EventMemberCalls.enqueue(new Callback<PostPutDelEventMember>() {
                @Override
                public void onResponse(Call<PostPutDelEventMember> call, Response<PostPutDelEventMember> response) {
                    Toast.makeText(AbsentActivity.this, "You Already Absent Today. Enjoy Your Event and Thank You!",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<PostPutDelEventMember> call, Throwable t) {
                    Toast.makeText(AbsentActivity.this, "Cannot Update Data Because You're Offline Now",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void GetData(final String id_event) {
        if(id_event.isEmpty())
        {
            Toast.makeText(AbsentActivity.this, "Sorry..No Event Is Available",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            Call<GetEvent> EventMemberCalls = mApiInterface.getEvent(id_event);
            final int i = 0;
            EventMemberCalls.enqueue(new Callback<GetEvent>() {
                @Override
                public void onResponse(Call<GetEvent> call, Response<GetEvent> response) {
                    List<Event> EventList = response.body().getListEvent();
                    String[] parts = EventList.get(i).getAlamat_event().toLowerCase().split(" ");
                    int trues = 0;
                    for (String strPart : parts) {
                        if(tvLoc.getText().toString().toLowerCase().contains(strPart))
                        {
                            trues++;
                        }
                    }
                    if(trues > 0)
                    {
                        UpdateData(username, id_event, "YA");
                    }
                    else
                    {
                        Toast.makeText(AbsentActivity.this, "Oops..Your Location Isn't Match! Please Check your QR location",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GetEvent> call, Throwable t) {
                    Toast.makeText(AbsentActivity.this, "Cannot Get Data Because You're Offline Now",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void getLocation(View view){
        gpsTracker = new GPSTracker(AbsentActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            //get exact address from long-lat
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(AbsentActivity.this);

                if (latitude != 0 || longitude != 0) {
                    addresses = geocoder.getFromLocation(latitude ,
                            longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getAddressLine(1);
                    String country = addresses.get(0).getAddressLine(2);
                    tvLoc.setText(address);
                }
                else {
                    Toast.makeText(this, "Sorry your location isn't found",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }else{
            gpsTracker.showSettingsAlert();
        }
    }
}
