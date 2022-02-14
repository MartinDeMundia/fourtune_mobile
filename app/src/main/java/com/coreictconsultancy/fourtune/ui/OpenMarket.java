package com.coreictconsultancy.fourtune.ui;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.HighLowDataEntry;
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Stock;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.core.stock.Plot;
import com.anychart.core.stock.series.Hilo;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.data.Table;
import com.anychart.data.TableMapping;
import com.anychart.enums.HoverMode;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.graphics.vector.SolidFill;
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

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpenMarket extends Fragment {


    TextView txtGamerName;
    public static SharedPreferences sharedpreferences;
    String userId,savedPhoneNo,gamerName = null;
    protected DataCache mDataCache;
    RelativeLayout mainLayout;
    TextView txtTokenValue;
    static AnyChartView anyChartView;

    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }



    public void creategraph(String token_purchases){
        List<DataEntry> data = new ArrayList<>();
        try {
            JSONArray j = new JSONArray(token_purchases);
            if (j != null) {
                for (int i = 0; i < j.length(); i++) {
                    JSONObject Obj;
                    Obj = j.getJSONObject(i);
                    String xaxis = Obj.getString("xaxis");
                    Double high = Double.parseDouble(Obj.getString("high"));
                    Double low = Double.parseDouble(Obj.getString("low"));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    Date inputDate;
                    try {
                        inputDate = simpleDateFormat.parse(xaxis);
                        data.add(new HighLowDataEntry(inputDate.getTime(),high,low));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Stock stock = AnyChart.stock();
        Plot plot = stock.plot(0);
        plot.yGrid(true).yMinorGrid(true);
        Table table = Table.instantiate("x");
        table.addData(data);
        TableMapping mapping = table.mapAs("{'high': 'high', 'low': 'low'}");
        Hilo hilo = plot.hilo(mapping);
        hilo.name("Fourtune Coin Trend");

        hilo.tooltip().format("Max: {%High}&deg;<br/>Min: {%Low}&deg;");
        stock.tooltip().useHtml(true);
        anyChartView.setChart(stock);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_openmarket, container, false);
        txtGamerName =  root.findViewById(R.id.txtGamerName);
        userId =  GetPref(getContext(), "Pref_User_ID");
        savedPhoneNo = GetPref(getContext(), "Pref_Phone");
        gamerName = GetPref(getContext(), "Pref_Username");
        txtGamerName.setText("Hi, "+gamerName+"!");
        mainLayout =  root.findViewById(R.id.mainLayout);
        txtTokenValue =  root.findViewById(R.id.txtTokenValue);

        LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
            public void loadedData(String param1String) {
                if(param1String != null) {
                    JSONObject jObjectDetails;
                    try {
                        jObjectDetails = new JSONObject(param1String);
                        if(jObjectDetails == null) {
                            Snackbar snackbar = Snackbar
                                    .make(mainLayout,"Please check your internet connection!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else {
                                txtTokenValue.setText("1 Fourtune token = "+jObjectDetails.getString("value")+" USD");
                                creategraph(jObjectDetails.getString("chart"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        mDataCache = new DataCache(getContext());
        try {
            mDataCache.loadData(URLProvider.LiveURL+"/api/v1/fetch-token-value",URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        anyChartView = root.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));

        return root;
    }

}
