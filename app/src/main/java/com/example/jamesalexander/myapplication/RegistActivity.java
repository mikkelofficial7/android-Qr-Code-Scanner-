package com.example.jamesalexander.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jamesalexander.myapplication.Model.GetNewMember;
import com.example.jamesalexander.myapplication.Model.NewMember;
import com.example.jamesalexander.myapplication.Model.PostPutDelNewMember;
import com.example.jamesalexander.myapplication.Rest.ApiClient;
import com.example.jamesalexander.myapplication.Rest.ApiInterface;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistActivity extends AppCompatActivity {
    private int progressStatus = 0;
    ApiInterface mApiInterface;
    String emailRegEx;
    Pattern pattern;
    Matcher matchers;
    private Handler handler = new Handler();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Id = "userid";
    public static final String Name = "username";
    public static final String Phone = "userphone";
    public static final String Email = "useremail";
    public static final String Workplace = "userworkplace";
    public static final String Occupation = "useroccupation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        final Button button_register = (Button)findViewById(R.id.button_register);
        final EditText edt1 = (EditText)findViewById(R.id.editText);
        final EditText edt2 = (EditText)findViewById(R.id.editText2);
        final EditText edt3 = (EditText)findViewById(R.id.editText3);
        final EditText edt4 = (EditText)findViewById(R.id.editText4);
        final EditText edt5 = (EditText)findViewById(R.id.editText5);

        edt1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    GetData(edt1.getText().toString());
                }
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt1.getText().toString().isEmpty() || edt2.getText().toString().isEmpty() || edt4.getText().toString().isEmpty() || edt5.getText().toString().isEmpty())
                {
                    Toast.makeText(RegistActivity.this, "Field Cannot Be Blank!",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Regex for a valid email address
                    emailRegEx = "^[A-Za-z0-9+_.-]+@(.+)$";
                    // Compare the regex with the email address
                    pattern = Pattern.compile(emailRegEx);
                    matchers = pattern.matcher(edt2.getText().toString());

                    if(!matchers.matches())
                    {
                        Toast.makeText(RegistActivity.this, "Wrong Email Format!",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        // Initialize a new instance of progress dialog
                        final ProgressDialog pd = new ProgressDialog(RegistActivity.this);

                        // Set progress dialog style spinner
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                        // Set the progress dialog title and message
                        pd.setTitle("Registering");
                        pd.setMessage("Please Wait.........");

                        // Set the progress dialog background color
                        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));

                        pd.setIndeterminate(false);

                        // Finally, show the progress dialog
                        pd.show();

                        // Set the progress status zero on each button click
                        progressStatus = 0;

                        // Start the lengthy operation in a background thread
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String workplace;
                                if (edt3.length() == 0) {
                                    edt3.setText("other");
                                    workplace = edt3.getText().toString();
                                } else {
                                    workplace = edt3.getText().toString();
                                }
                                String phone = edt1.getText().toString();
                                String mail = edt2.getText().toString();
                                String occupation = edt4.getText().toString();
                                String name = edt5.getText().toString();
                                String id = "MEM-" + edt1.getText().toString();

                                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString(Id, id);
                                editor.putString(Name, name);
                                editor.putString(Phone, phone);
                                editor.putString(Email, mail);
                                editor.putString(Workplace, workplace);
                                editor.putString(Occupation, occupation);
                                editor.commit();

                                InsertData(id, name, occupation, workplace, mail, phone);
                                while (progressStatus < 100) {
                                    // Update the progress status
                                    progressStatus += 1;

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
                                            pd.setProgress(progressStatus);
                                            // If task execution completed
                                            if (progressStatus == 100) {
                                                Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                // Dismiss/hide the progress dialog
                                                pd.dismiss();
                                            }
                                        }
                                    });
                                }
                            }
                        }).start(); // Start the operation
                    }
                }
            }
        });
    }
    public void InsertData(String id_member, String nama, String pekerjaan, String tempat_kerja, String email, String phone) {
        Call<PostPutDelNewMember> NewMemberCall = mApiInterface.postNewMember(id_member, nama, pekerjaan, tempat_kerja, email, phone);
        NewMemberCall.enqueue(new Callback<PostPutDelNewMember>() {
            @Override
            public void onResponse(Call<PostPutDelNewMember> call, Response<PostPutDelNewMember> response) {
            }

            @Override
            public void onFailure(Call<PostPutDelNewMember> call, Throwable t) {
                Toast.makeText(RegistActivity.this, "Cannot Insert Data!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    public void GetData(String id_member) {
        final Call<GetNewMember> NewMemberCall = mApiInterface.getNewMember("MEM-"+id_member);
        NewMemberCall.enqueue(new Callback<GetNewMember>() {
            @Override
            public void onResponse(Call<GetNewMember> call, Response<GetNewMember> response) {
                List<NewMember> NewMemberList = response.body().getListNewMember();
                if(NewMemberList.size() > 0) {
                    Toast.makeText(RegistActivity.this, "You Phone Already Registered! Redirecting...",
                            Toast.LENGTH_LONG).show();
                    int i = 0;

                    String workplace = NewMemberList.get(i).getTempat_kerja();
                    String phone = NewMemberList.get(i).getPhone();
                    String mail = NewMemberList.get(i).getEmail();
                    String occupation = NewMemberList.get(i).getPekerjaan();
                    String name = NewMemberList.get(i).getNama();
                    String id = NewMemberList.get(i).getId_member();

                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(Id, id);
                    editor.putString(Name, name);
                    editor.putString(Phone, phone);
                    editor.putString(Email, mail);
                    editor.putString(Workplace, workplace);
                    editor.putString(Occupation, occupation);
                    editor.commit();

                    Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<GetNewMember> call, Throwable t) {
                Toast.makeText(RegistActivity.this, "Oops..You're Offline Now. Please Turn On The Connection!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
