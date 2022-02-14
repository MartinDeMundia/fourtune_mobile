package com.coreictconsultancy.fourtune.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.adapter.CoinsEarnedAdapter;
import com.coreictconsultancy.fourtune.adapter.EventsAdapter;
import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.coreictconsultancy.fourtune.pojo.Res_Coins_Earned;
import com.coreictconsultancy.fourtune.pojo.Res_Events;

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

public class FourtuneEvents extends Fragment {

    protected DataCache mDataCache;
    ArrayList<Res_Events> EventsDAO;
    EventsAdapter events_adapter;
    //RecyclerView recycler_events;
    public GridView mList;
    View mListContainer;
    View mEmptyContainer;
    View mProgressContainer;

    protected LoaderToUIListener loaderToUIListener = new LoaderToUIListener(){
        public void loadedData(String param1String) {
            EventsDAO.clear();
            JSONObject jObject = new JSONObject();
            try {
                if(param1String != null) {
                    jObject = new JSONObject(param1String);
                    JSONObject jSONObject = jObject.getJSONObject("data");
                    JSONObject jSONObject_CoinsEarned = jSONObject.getJSONObject("data_object");
                    JSONArray j = jSONObject_CoinsEarned.getJSONArray("dbrows");
                    if (j != null) {
                        for (int i = 0; i < j.length(); i++) {
                            JSONObject Obj;
                            Obj = j.getJSONObject(i);
                            Res_Events temp = new Res_Events();
                            temp.setId(Obj.getString("id"));
                            temp.setEventname(Obj.getString("event_name"));
                            temp.setEventfeatured_image(Obj.getString("event_featured_image"));
                            EventsDAO.add(temp);
                        }
                        EventsAdapter lazyAdapter = new EventsAdapter(getActivity(),EventsDAO);
                        lazyAdapter.notifyDataSetChanged();
                        mList.setAdapter(lazyAdapter);
                        setListShown(true,false);
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

    public static boolean isTablet(Context paramContext) {
        return (((paramContext.getResources().getConfiguration()).screenLayout & 0xF) >= 3);
    }
    public int getGridPading(Configuration paramConfiguration, Activity paramActivity) {
        return this.isTablet((Context)paramActivity) ? 20 : 10;
    }

    public int getColumeMovieList(Configuration paramConfiguration, Activity paramActivity) {
        return this.isTablet((Context)paramActivity) ? ((paramConfiguration.orientation == 2) ? 6 : 4) : ((paramConfiguration.orientation == 2) ? 5 : 3);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fourtune_events, container, false);

        this.mList = (GridView)root.findViewById(R.id.myGrid);
        Configuration configuration = getResources().getConfiguration();
        this.mList.setNumColumns(this.getColumeMovieList(configuration, (Activity)getActivity()));
        int i = this.getGridPading(configuration, (Activity)getActivity());
        this.mList.setVerticalSpacing(i);
        this.mList.setHorizontalSpacing(i);
        GridView gridView = this.mList;
        int j = i / 2;
        gridView.setPadding(i, j, i, j);

        this.mListContainer = root.findViewById(R.id.listContainer);
        this.mProgressContainer = root.findViewById(R.id.progressContainer);
        this.mEmptyContainer = root.findViewById(R.id.emptyContainer);

        this.mList.setAdapter((ListAdapter)this.events_adapter);

        EventsDAO = new ArrayList<Res_Events>();
        this.mDataCache = new DataCache(getContext());
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userid",userId));
            setListShown(false,true);
            this.mDataCache.loadData(URLProvider.LiveURL + "/api/v1/fetch-events", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), this.loaderToUIListener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return root;
    }


    @SuppressLint("WrongConstant")
    public void setListShown(boolean paramBoolean1, boolean paramBoolean2) {
        if (paramBoolean1) {
            if (paramBoolean2) {
                this.mEmptyContainer.setVisibility(0);
                this.mListContainer.setVisibility(8);
            } else {
                this.mListContainer.setVisibility(0);
                this.mEmptyContainer.setVisibility(8);
            }
            this.mProgressContainer.setVisibility(8);
            return;
        }
        this.mEmptyContainer.setVisibility(8);
        this.mProgressContainer.setVisibility(0);
        this.mListContainer.setVisibility(8);
    }
}
