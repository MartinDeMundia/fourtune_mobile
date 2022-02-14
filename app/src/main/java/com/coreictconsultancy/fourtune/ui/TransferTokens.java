package com.coreictconsultancy.fourtune.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian3d;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
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

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TransferTokens extends Fragment {

    Dialog pick_Dialog;
    RelativeLayout mainLayout;
    String userId,savedPhoneNo,gamerName = null;
    public static SharedPreferences sharedpreferences;
    TextView txtUserTokenAmount,txtGamerName,lbl_account_date,txtPhoneNumber,txtTokenAmount;
    protected DataCache mDataCache;
    private SaveTask mSaveTask = null;
    RelativeLayout relativeSend;
    static AnyChartView anyChartView;

    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    public static void creategraph(String token_purchases){
        Cartesian3d column3d = AnyChart.column3d();
        column3d.yScale().stackMode(ScaleStackMode.VALUE);
        column3d.animation(true);
        column3d.title("Visualisation of your account (Monthly)");
        column3d.title().padding(0d, 0d, 15d, 0d);
        List<DataEntry> seriesData = new ArrayList<>();

        JSONObject jObject = new JSONObject();
        try {
            JSONArray j = new JSONArray(token_purchases);
            if (j != null) {
                for (int i = 0; i < j.length(); i++) {
                    JSONObject Obj;
                    Obj = j.getJSONObject(i);
                    String monthname =  Obj.getString("monthname");
                    Integer amount = Integer.valueOf(Obj.getString("amount"));
                    seriesData.add(new CustomDataEntry(monthname, amount, null, null, null, null, null));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Column3d series1 = column3d.column(series1Data);
        series1.name("Token Amount");
        series1.fill(new SolidFill("#3e2723", 1d));
        series1.stroke("1 #f7f3f3");
        series1.hovered().stroke("3 #f7f3f3");

        column3d.legend().enabled(true);
        column3d.legend().fontSize(13d);
        column3d.legend().padding(0d, 0d, 20d, 0d);

        //column3d.yScale().ticks("[0, 10, 20, 30, 40, 50,60,70,80,90,100]");

        column3d.xAxis(0).stroke("1 #a18b7e");
        column3d.xAxis(0).labels().fontSize(6d);
        column3d.yAxis(0).stroke("1 #a18b7e");
        column3d.yAxis(0).labels().fontColor("#a18b7e");
        column3d.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        column3d.yAxis(0).title().enabled(true);
        column3d.yAxis(0).title().text("Token Amount");
        column3d.yAxis(0).title().fontColor("#a18b7e");

        column3d.interactivity().hoverMode(HoverMode.BY_X);

        column3d.tooltip()
                .displayMode(TooltipDisplayMode.UNION)
                .format("{%Value} {%SeriesName}");

        column3d.yGrid(0).stroke("#a18b7e", 1d, null, null, null);
        column3d.xGrid(0).stroke("#a18b7e", 1d, null, null, null);
        anyChartView.setChart(column3d);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transfertokens, container, false);


        mainLayout =  root.findViewById(R.id.mainLayout);
        txtUserTokenAmount =  root.findViewById(R.id.txtUserTokenAmount);
        txtGamerName =  root.findViewById(R.id.txtGamerName);
        lbl_account_date =  root.findViewById(R.id.lbl_account_date);

        anyChartView = root.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));

        txtPhoneNumber =  root.findViewById(R.id.txtPhoneNumber);
        txtTokenAmount =  root.findViewById(R.id.txtTokenAmount);


        userId =  GetPref(getContext(), "Pref_User_ID");
        savedPhoneNo = GetPref(getContext(), "Pref_Phone");
        gamerName = GetPref(getContext(), "Pref_Username");


        if(savedPhoneNo.equals("")){

            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
            Intent intent = new Intent(getContext(), Signin.class);
            startActivity(intent);

        }else {

            txtUserTokenAmount.setText("0");
            txtGamerName.setText("Hi, "+gamerName+"!");
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
                                String success = jObjectDetails.getString("success");
                                if (success.equals("true")){
                                    txtUserTokenAmount.setText(""+jObjectDetails.getString("totalcoins"));
                                    lbl_account_date.setText(jObjectDetails.getString("account_notification"));
                                    creategraph(jObjectDetails.getString("token_purchases"));
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
            mDataCache = new DataCache(getContext());
            try {
                mDataCache.loadData(URLProvider.LiveURL+"/api/v1/gamer-profile",URLProvider.purchaseToken(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }



            if (pick_Dialog == null) {
                pick_Dialog = new Dialog(this.getContext());
                pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                pick_Dialog.setCancelable(true);
                pick_Dialog.setCanceledOnTouchOutside(true);
                pick_Dialog.setContentView(R.layout.pop_transfer_coins);
                pick_Dialog.show();

                relativeSend = pick_Dialog.findViewById(R.id.relativeSend);
                txtPhoneNumber = pick_Dialog.findViewById(R.id.txtPhoneNumber);
                txtTokenAmount = pick_Dialog.findViewById(R.id.txtTokenAmount);
                relativeSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strToPhoneNumber = txtPhoneNumber.getText().toString().trim();
                        String strTokenAmount = txtTokenAmount.getText().toString().trim();
                        if (strToPhoneNumber.equals("")) {
                            Toast.makeText(getActivity(), "Please a valid phone number!", Toast.LENGTH_LONG).show();
                        } else if (strTokenAmount.equals("")){
                            Toast.makeText(getActivity(), "Please enter the valid amount!", Toast.LENGTH_LONG).show();
                        }else{
                            mSaveTask = new SaveTask(strToPhoneNumber, userId,strTokenAmount);
                            mSaveTask.execute((String) null);
                        }
                    }
                });


            } else if (pick_Dialog.isShowing()) {
                pick_Dialog.dismiss();
            } else {
                pick_Dialog = null;
            }




        }
        return root;
    }





    public class SaveTask extends AsyncTask<String, Void, JSONObject> {
        private final String mTokenQty;
        private final String mPhoneno;
        private final String mUserID;
        SaveTask(String mPhoneno ,String mUserID,String TokenQty ) {
            this.mTokenQty = TokenQty;
            this.mUserID = mUserID;
            this.mPhoneno = mPhoneno;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            InputStream is = null;
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
                                String success = jObjectDetails.getString("success");
                                if (success.equals("true")){
                                    pick_Dialog.dismiss();
                                    txtUserTokenAmount.setText(""+jObjectDetails.getString("totalcoins"));
                                    Snackbar snackbar = Snackbar
                                            .make(mainLayout,jObjectDetails.getString("message"), Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                }else{
                                    Snackbar snackbar = Snackbar
                                            .make(mainLayout,jObjectDetails.getString("message"), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
            };
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("phone", this.mPhoneno));
            nameValuePairs.add(new BasicNameValuePair("userid", this.mUserID));
            nameValuePairs.add(new BasicNameValuePair("amount", this.mTokenQty));
            mDataCache = new DataCache(getContext());
            try {
                mDataCache.loadData(URLProvider.LiveURL+"/api/v1/transfer-token",URLProvider.purchaseToken(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return  null;
        }
        public SharedPreferences sharedpreferences;
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
            //showProgress(false);
        }
    }























    private static class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2, Number value3, Number value4, Number value5, Number value6) {
            super(x, value);
            setValue(x, value);
            //setValue("value3", value3);
            // setValue("value4", value4);
            // setValue("value5", value5);
            //setValue("value6", value6);
        }
    }
}
