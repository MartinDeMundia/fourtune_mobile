package com.coreictconsultancy.fourtune.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.coreictconsultancy.fourtune.MainActivity;
import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.coreictconsultancy.fourtune.pojo.Res_Coins_Earned;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Signup extends AppCompatActivity
{
    private View mProgressView;
    private View mSignupFormView;
    private EditText mFullname;
    private EditText mMobilenumber;
    private EditText mEmail;
    private RadioButton mMale;
    private RadioButton mFemale;
    private DatePicker selectedDate;
    private EditText mPassword;
    private EditText mcPassword;
    LinearLayout registerLayout;
    private UserRegisterTask mRegisterTask = null;
    Button btnRegister;
    CheckBox chkreadlicence;
    Spinner packagesSpinner;
    TextView login_link;
    protected DataCache mDataCache;
    JSONObject jObjectUserDetails;
    public SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mSignupFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.login_progress);
        registerLayout =  findViewById(R.id.registerLayout);
        mFullname = (MyEditText) findViewById(R.id.fullname);
        mMobilenumber = (MyEditText) findViewById(R.id.mobilenumber);
        mEmail = (MyEditText) findViewById(R.id.email);
       /// mNickname = (com.coreictconsultancy.tazama.main.MyEditText) findViewById(R.id.nickname);
        selectedDate = (DatePicker) findViewById(R.id.selectedDate);
        mPassword = (MyEditText) findViewById(R.id.password);
        mcPassword = (MyEditText) findViewById(R.id.cpassword);
        chkreadlicence = (CheckBox) findViewById(R.id.chkreadlicence);
        mMale = (RadioButton) findViewById(R.id.radiomale);
        mFemale = (RadioButton) findViewById(R.id.radiofemale);
        login_link = (TextView) findViewById(R.id.login_link);
        login_link.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Signin.class);
                startActivity(intent);
            }
        });




       TextView licTxt = (TextView) findViewById(R.id.licTxt);
        licTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), FrmEULA.class);
                //startActivity(intent);
            }
        });

        btnRegister =  (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    private void attemptRegister() {
        mFullname.setError(null);
        mMobilenumber.setError(null);
        mEmail.setError(null);
        mFemale.setError(null);
        mMale.setError(null);
        //selectedDate.setError(null);
        mPassword.setError(null);
        mcPassword.setError(null);
        String fullname= mFullname.getText().toString();
        String mobilenumber = mMobilenumber.getText().toString();
        String email = mEmail.getText().toString();
        String dateofbirth =   selectedDate.getDayOfMonth()+"-"+ (selectedDate.getMonth() + 1)+"-"+selectedDate.getYear();
        String password = mPassword.getText().toString();
        String cpassword = mcPassword.getText().toString();
        String gender = "";
        boolean cancel = false;
        View focusView = null;


       if(mMale.isChecked()){
            gender = "Male";
            mMale.setChecked(true);
            mFemale.setChecked(false);
            mMale.setTextColor(Color.WHITE);
        }else if(mFemale.isChecked()){
            gender = "Female";
            mFemale.setChecked(true);
            mMale.setTextColor(Color.WHITE);
            mFemale.setChecked(false);
        }else{
            mMale.setError("Please select gender!");
            focusView = mMale;
            cancel = true;
        }

        if (TextUtils.isEmpty(fullname)) {
            mFullname.setError("Please enter your name!");
            focusView = mFullname;
            cancel = true;
        }

        if (TextUtils.isEmpty(mobilenumber)) {
            mMobilenumber.setError("Please enter your phone number!");
            focusView = mMobilenumber;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError("Please enter your password!");
            focusView = mPassword;
            cancel = true;
        }


        if (!password.equals(cpassword)) {
            mcPassword.setError("Passwords do not match!");
            focusView = mcPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email field cannot be left empty!");
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError("Email entered is invalid!");
            focusView = mEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (chkreadlicence.isChecked()){
                chkreadlicence.setTextColor(Color.WHITE);
                showProgress(true);
                mRegisterTask = new Signup.UserRegisterTask(fullname,mobilenumber,email,gender,dateofbirth,password,"000");
                mRegisterTask.execute((String) null);
            }else{
                Snackbar snackbar = Snackbar
                        .make(registerLayout,"Please read the Terms of Service!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    public void SetPref(Context con, String Key, String Value) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(Key, Value);
        edit.commit();
    }

    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    private boolean isEmailValid(String email){
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignupFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public class UserRegisterTask extends AsyncTask<String, Void, JSONObject> {
        private final String mFullname;
        private final String mMobilenumber;
        private final String mEmail;
        private final String mGender;
        private final String mAge;
        private final String mPassword;
        private final String mPackage;

        UserRegisterTask(String fullname,String mobilenumber,String email,String gender,String age,String password , String actualPositionOfMySpinner) {
            mFullname = fullname;
            mMobilenumber = mobilenumber;
            mEmail = email;
            mGender = gender;
            mAge = age;
            mPassword = password;
            mPackage = actualPositionOfMySpinner;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            InputStream is = null;
            LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {

                public void loadedData(String param1String) {
                    try {
                        if(param1String != null) {
                            jObjectUserDetails = new JSONObject(param1String);

                            if(jObjectUserDetails == null) {
                                        Snackbar snackbar = Snackbar
                                                .make(registerLayout,"Please check your internet connection!", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        showProgress(false);
                            }else {
                                        String success = "false";
                                        try {
                                            success = jObjectUserDetails.getString("success");
                                            showProgress(false);
                                            if (success.equals("true")){
                                                JSONObject Obj = new JSONObject(jObjectUserDetails.getString("data"));
                                                SetPref(Signup.this, "Pref_User_ID", Obj.getString("id"));
                                                SetPref(Signup.this, "Pref_Username", Obj.getString("name"));
                                                SetPref(Signup.this, "Pref_Phone", Obj.getString("phone"));
                                                SetPref(Signup.this, "Pref_Email", Obj.getString("email"));
                                                Snackbar snackbar = Snackbar
                                                        .make(registerLayout,jObjectUserDetails.getString("message"), Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                                Intent intent = new Intent(Signup.this, MainActivity.class); 
                                                startActivity(intent);
                                            } else {
                                                Snackbar snackbar = Snackbar
                                                        .make(registerLayout,jObjectUserDetails.getString("message"), Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Snackbar snackbar = Snackbar
                                                    .make(registerLayout,e.getMessage(), Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }

                               }

                            }
                        } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }

            };

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("fullname", mFullname));
            nameValuePairs.add(new BasicNameValuePair("mobilenumber", mMobilenumber));
            nameValuePairs.add(new BasicNameValuePair("email", mEmail));
            nameValuePairs.add(new BasicNameValuePair("gender", mGender));
            nameValuePairs.add(new BasicNameValuePair("age", mAge));
            nameValuePairs.add(new BasicNameValuePair("passwd", mPassword));

            mDataCache = new DataCache(Signup.this);
            try {
                mDataCache.loadData(URLProvider.LiveURL+"/api/v1/create-user",URLProvider.createUserAccount(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return  null;
        }

  /*      public void SetPref(Context con, String Key, String Value) {
            sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedpreferences.edit();
            edit.putString(Key, Value);
            edit.commit();
        }

        public  String GetPref(Context con, String Key) {
            sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
            String Value = "";
            if (sharedpreferences.contains(Key)) {
                Value = sharedpreferences.getString(Key, "");
            }
            return Value;
        }*/

        @Override
        protected void onPostExecute(final JSONObject jsonObjects) {
        }
        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }


}
