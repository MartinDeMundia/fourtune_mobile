package com.coreictconsultancy.fourtune.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import com.coreictconsultancy.tazama.ui.FrmForgotPassword;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.coreictconsultancy.fourtune.MainActivity;
import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
public class Signin extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private static final int REQUEST_READ_CONTACTS = 0;
    private UserLoginTask mAuthTask = null;
    private EditText mButton;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    ImageView sback;
    Button btnlogin,btnRegister;
    LinearLayout mainLayout;
    TextView btnForgot;
    protected DataCache mDataCache;
    JSONObject jObjectUserDetails;
    public SharedPreferences sharedpreferences;


    public void SetPref(Context con, String Key, String Value) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(Key, Value);
        edit.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SetPref(this, "Pref_User_ID","");
        SetPref(this, "Pref_Username", "");
        SetPref(this, "Pref_Phone", "");
        SetPref(this, "Pref_Email", "");

        mainLayout =  (LinearLayout)findViewById(R.id.mainLayout);
        btnForgot =  (TextView)findViewById(R.id.btnForgot);
        btnForgot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent it = new Intent(Signin.this, FrmForgotPassword.class);
                // startActivity(it);
            }
        });
        btnlogin =  (Button)findViewById(R.id.btnUpdate);
        btnlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        btnRegister =  (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Signin.this,Signup.class);
                startActivity(it);
            }
        });
        mEmailView = (MyEditText) findViewById(R.id.email);        //populateAutoComplete();
        mPasswordView = (MyEditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        btnForgot =  (TextView)findViewById(R.id.btnForgot);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Signin.this, FrmForgotPassword.class);
                startActivity(it);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView,"Permission needed!", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("Invalid password");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Email address is required!");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("Email entered is invalid!");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((String) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Signin.this, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
       // mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, JSONObject> {
        private final String mEmail;
        private final String mPassword;
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
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
                                        .make(mainLayout,"Please check your internet connection!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                btnlogin.invalidate();
                                btnlogin.refreshDrawableState();
                                mAuthTask = null;
                                showProgress(false);
                            }else {
                                String success = "false";
                                try {
                                    success = jObjectUserDetails.getString("success");
                                    mAuthTask = null;
                                    showProgress(false);
                                    if (success.equals("true")){
                                        JSONObject Obj = new JSONObject(jObjectUserDetails.getString("data"));
                                        SetPref(Signin.this, "Pref_User_ID", Obj.getString("id"));
                                        SetPref(Signin.this, "Pref_Username", Obj.getString("name"));
                                        SetPref(Signin.this, "Pref_Phone", Obj.getString("phone"));
                                        SetPref(Signin.this, "Pref_Email", Obj.getString("email"));
                                        Snackbar snackbar = Snackbar
                                                .make(mainLayout,jObjectUserDetails.getString("message"), Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        Intent intent = new Intent(Signin.this, MainActivity.class); //change to account view
                                        startActivity(intent);
                                    } else {
                                        mPasswordView.setError(jObjectUserDetails.getString("message"));
                                        mPasswordView.requestFocus();
                                        Snackbar snackbar = Snackbar
                                                .make(mainLayout,jObjectUserDetails.getString("message"), Snackbar.LENGTH_LONG);
                                        snackbar.show();
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
            nameValuePairs.add(new BasicNameValuePair("username", mEmail));
            nameValuePairs.add(new BasicNameValuePair("passwd", mPassword));
            mDataCache = new DataCache(Signin.this);
            try {
                mDataCache.loadData(URLProvider.LiveURL+"/api/v1/authenticate-user",URLProvider.authenticateUserAccount(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return jObjectUserDetails;
        }
        public  SharedPreferences sharedpreferences;
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

        @Override
        protected void onPostExecute(final JSONObject jsonObjects) {
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

