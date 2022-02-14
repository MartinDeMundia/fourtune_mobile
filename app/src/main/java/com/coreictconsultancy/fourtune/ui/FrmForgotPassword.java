package com.coreictconsultancy.fourtune.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.coreictconsultancy.fourtune.MainActivity;
import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;



public class FrmForgotPassword extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private EditText mEmailView;
    Button btnSendEmail;
    LinearLayout mainLayout;
    private FrmForgotPassword.SendEmailTask mAuthTask = null;
    public static SharedPreferences sharedpreferences;
    protected DataCache mDataCache;
    JSONObject jObjectPassResponse;

    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mEmailView = (MyEditText) findViewById(R.id.email);
        mainLayout =  (LinearLayout)findViewById(R.id.mainLayout);

        btnSendEmail =  (Button)findViewById(R.id.btnUpdate);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempSendEmail();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


    private void attempSendEmail() {
        if (mAuthTask != null) {
            return;
        }
        mEmailView.setError(null);
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Please enter an email address!");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("The email entered is invalid!");
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel){
            focusView.requestFocus();
        } else {
            showProgress(true);
            String userid = GetPref(FrmForgotPassword.this,"Pref_User_ID");
            mAuthTask = new FrmForgotPassword.SendEmailTask(email, userid);
            mAuthTask.execute((String) null);
        }
    }

    public class SendEmailTask extends AsyncTask<String, Void, JSONObject> {
        private final String mEmail;
        private final String mUserId;
        SendEmailTask(String email, String userid) {
            mEmail = email;
            mUserId = userid;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            InputStream is = null;
            LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                public void loadedData(String param1String) {
                    try {
                        if(param1String != null) {
                            jObjectPassResponse = new JSONObject(param1String);
                            if(jObjectPassResponse == null) {
                                Snackbar snackbar = Snackbar
                                        .make(mainLayout,"Please check your internet connection!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                btnSendEmail.invalidate();
                                btnSendEmail.refreshDrawableState();
                                mAuthTask = null;
                                showProgress(false);
                            }else {
                                String success = "false";
                                try {
                                    success = jObjectPassResponse.getString("success");
                                    mAuthTask = null;
                                    showProgress(false);
                                    String message = jObjectPassResponse.getString("message");
                                    if (success.equals("true")){
                                        Snackbar snackbar = Snackbar
                                                .make(mainLayout,message, Snackbar.LENGTH_LONG);
                                        snackbar.show();



                                        AlertDialog.Builder builder = new AlertDialog.Builder(FrmForgotPassword.this);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(FrmForgotPassword.this, Signin.class);
                                                startActivity(intent);
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.setMessage(message);
                                        dialog.setIcon(R.drawable.swarm_user);
                                        dialog.show();

                                    } else {
                                        mEmailView.setError(jObjectPassResponse.getString("message"));
                                        mEmailView.requestFocus();
                                        new AlertDialog.Builder(FrmForgotPassword.this).setTitle("Password Reset")
                                                .setMessage(message).setIcon(R.drawable.swarm_user).
                                                setNeutralButton("Close", null).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Snackbar snackbar = Snackbar
                                            .make(mainLayout,e.getMessage(), Snackbar.LENGTH_LONG);
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
            nameValuePairs.add(new BasicNameValuePair("email", mEmail));
            nameValuePairs.add(new BasicNameValuePair("id",mUserId));
            mDataCache = new DataCache(FrmForgotPassword.this);
            try {
                mDataCache.loadData(URLProvider.LiveURL+"/api/v1/fpassword-request",URLProvider.authenticateUserAccount(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return jObjectPassResponse;
        }
        @Override
        protected void onPostExecute(final JSONObject jsonObjects){
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
