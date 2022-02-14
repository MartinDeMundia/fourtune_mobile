package com.coreictconsultancy.fourtune.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.coreictconsultancy.fourtune.MainActivity;
import com.coreictconsultancy.fourtune.adapter.CoinsEarnedAdapter;
import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.coreictconsultancy.fourtune.pojo.Res_Coins_Earned;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PostEarnings extends MainActivity {

    //private final String userid;
    protected DataCache mDataCache;
    private String gmLevel = "";
    private String gmType = "";
    private String userId = "";
    private Context context;

    public PostEarnings(String gmlevel, String gmtype, Context context){
        this.gmLevel = gmlevel;
        this.gmType = gmtype;
        this.context = context;
    }
    public static SharedPreferences sharedpreferences;
    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    public String  getLoggedUser(){
        return  userId =  GetPref(this.context, "Pref_User_ID");
    }
    protected LoaderToUIListener loaderToUIListener = new LoaderToUIListener(){
        public void loadedData(String param1String) {
            JSONObject jObject = new JSONObject();
            try {
                if(param1String != null) {
                    jObject = new JSONObject(param1String);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void executeEarnings(String userId){
        this.mDataCache = new DataCache(this.context);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userid",userId));
            nameValuePairs.add(new BasicNameValuePair("gmType",this.gmType));
            nameValuePairs.add(new BasicNameValuePair("gmLevel",this.gmLevel));
            this.mDataCache.loadData(URLProvider.LiveURL + "/api/v1/post-earnings", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), this.loaderToUIListener);
        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        }
      }


    public void executeSignIn(Context dialogcontext){
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogcontext);
        builder.setTitle("Error").setMessage("Please sign in to bank your earnings!");
    }




}
