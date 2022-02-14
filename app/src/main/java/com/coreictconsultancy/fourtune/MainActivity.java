package com.coreictconsultancy.fourtune;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.coreictconsultancy.fourtune.ui.Signin;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private AppBarConfiguration mAppBarConfiguration;
    String userId,savedPhoneNo,gamerName = null;
    public static SharedPreferences sharedpreferences;
    protected DataCache mDataCache;

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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId =  GetPref(MainActivity.this, "Pref_User_ID");
                savedPhoneNo = GetPref(MainActivity.this, "Pref_Phone");
                gamerName = GetPref(MainActivity.this, "Pref_Username");
                if(savedPhoneNo.equals("")){
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_home);
                    Intent intent = new Intent(MainActivity.this, Signin.class);
                    startActivity(intent);
                }else {
                    LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                        public void loadedData(String param1String) {
                            if(param1String != null) {
                                JSONObject jObjectDetails;
                                try {
                                    jObjectDetails = new JSONObject(param1String);
                                    if(jObjectDetails == null){
                                    }else {
                                        String success = jObjectDetails.getString("success");
                                        if (success.equals("true")){

                                            new AlertDialog.Builder(MainActivity.this).setTitle("Total Fourtune Token")
                                                    .setMessage(""+jObjectDetails.getString("totalcoins")).setIcon(R.drawable.coins).
                                                    setNeutralButton("Close", null).show();
                                        }else{
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("userid",userId));
                    mDataCache = new DataCache(MainActivity.this);
                    try {
                        mDataCache.loadData(URLProvider.LiveURL+"/api/v1/gamer-profile",URLProvider.purchaseToken(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
               // Snackbar.make(view, "TO DO (Display coins earned pop up)", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });

        findViewById(R.id.coinsearned).setOnClickListener(this);
        findViewById(R.id.purchasetokens).setOnClickListener(this);
        findViewById(R.id.transfertokens).setOnClickListener(this);
        findViewById(R.id.openmarket).setOnClickListener(this);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_coinsearned, R.id.nav_purchasetokens,R.id.nav_transfertokens,R.id.nav_openmarket,R.id.nav_about,R.id.nav_myaccount,R.id.nav_exit)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Signin.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.coinsearned:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_coinsearned);
            break;
            case R.id.purchasetokens:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_purchasetokens);
                break;
            case R.id.transfertokens:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_transfertokens);
                break;
            case R.id.openmarket:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_openmarket);
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, Signin.class);
                startActivity(intent);
                break;
        }

    }
}