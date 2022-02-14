package com.coreictconsultancy.fourtune.ui;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.coreictconsultancy.fourtune.R;
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

import static com.swarmconnect.Swarm.getApplicationContext;

public class CoinsEarned extends Fragment {

    protected DataCache mDataCache;
    ArrayList<Res_Coins_Earned> CoinsEarnedDAO;
    CoinsEarnedAdapter coins_earned_adapter;
    RecyclerView recycler_coins_earned;


    protected LoaderToUIListener loaderToUIListener = new LoaderToUIListener(){
        public void loadedData(String param1String) {
            CoinsEarnedDAO.clear();
            JSONObject jObject = new JSONObject();
            try {
                if(param1String != null) {
                    jObject = new JSONObject(param1String);

                    JSONObject jSONObject = jObject.getJSONObject("data");
                    JSONObject jSONObject_CoinsEarned = jSONObject.getJSONObject("coinsdata");
                    JSONArray j = jSONObject_CoinsEarned.getJSONArray("coinsearned");
                    if (j != null) {
                        for (int i = 0; i < j.length(); i++) {
                            JSONObject Obj;
                            Obj = j.getJSONObject(i);
                            Res_Coins_Earned temp = new Res_Coins_Earned();
                            temp.setId(Obj.getString("id"));
                            temp.setDate(Obj.getString("date"));
                            temp.setGame(Obj.getString("game"));
                            temp.setLevel(Obj.getString("level"));
                            temp.setCoins(Obj.getString("coins"));
                            CoinsEarnedDAO.add(temp);
                        }
                        CoinsEarnedAdapter lazyAdapter = new CoinsEarnedAdapter(getActivity(), CoinsEarnedDAO);
                        lazyAdapter.notifyDataSetChanged();
                        recycler_coins_earned.setAdapter(lazyAdapter);
                    }
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    public static SharedPreferences sharedpreferences;
    String userId,savedPhoneNo,gamerName = null;
    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coinsearned, container, false);
        userId =  GetPref(getContext(), "Pref_User_ID");
        savedPhoneNo = GetPref(getContext(), "Pref_Phone");
        gamerName = GetPref(getContext(), "Pref_Username");
        if(savedPhoneNo.equals("")){
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
                Intent intent = new Intent(getContext(), Signin.class);
                startActivity(intent);
        }else {
                recycler_coins_earned = root.findViewById(R.id.recycler_events);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recycler_coins_earned.setLayoutManager(mLayoutManager);
                recycler_coins_earned.setAdapter(coins_earned_adapter);
                CoinsEarnedDAO = new ArrayList<Res_Coins_Earned>();
                this.mDataCache = new DataCache(getContext());
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("userid",userId));
                    this.mDataCache.loadData(URLProvider.LiveURL + "/api/v1/coins-earned", URLProvider.getCoinsEarned(new UrlEncodedFormEntity(nameValuePairs)), this.loaderToUIListener);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

        }
        return root;
    }
}
