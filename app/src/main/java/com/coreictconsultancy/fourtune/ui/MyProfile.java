package com.coreictconsultancy.fourtune.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.TransactionType;
import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.adapter.SubWalletAdapter;
import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.coreictconsultancy.fourtune.pojo.Res_Sub_Wallets;
import com.google.android.material.snackbar.Snackbar;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.swarmconnect.Swarm.getApplicationContext;
import static java.lang.Math.round;

public class MyProfile extends Fragment {

    Dialog pick_Dialog;
    RelativeLayout relativeSend;
    EditText edtTokenAmount,edtTokenQty;
    protected DataCache mDataCache;
    private SaveTask mSaveTask = null;
    RelativeLayout mainLayout;
    String userId,savedPhoneNo,gamerName = null;
    public static SharedPreferences sharedpreferences;
    TextView txtUserTokenAmount,txtGamerName,lbl_account_date, txtEarnings , txtReceivedTokens , lblemail , lblphone , lblcreated_at;

    ImageButton btnmpesa , btnvisa;
    String strTokenAmount , strTokenQty , strPaytype = "";
    static Daraja daraja;
    double amountpurchased = 0.00;
    String tokenvalue,dollar_value = "0.5";

    ImageView settings_main_account,settings_earnings_account,settings_received_account;
    final int CONTEXT_SEND_TO_EARNINGS = 1;
    final int CONTEXT_BUY_TOKENS = 2;
    final int CONTEXT_TRANSFER_TOKENS = 3;
    final int CONTEXT_WITHDRAW_TOKENS_FROM_EARNINGS = 4;
    final int CONTEXT_TOKENS_TO_MAIN_FROM_EARNINGS = 5;
    final int CONTEXT_TOKENS_TO_MAIN_FROM_RECEIVED = 6;
    final int CONTEXT_WITHDRAW_TOKENS_FROM_RECEIVED = 7;
    final int CONTEXT_BUY_TICKETS = 8;
    final int CONTEXT_UNLOCK_GAMES = 9;

    Dialog pick_Dialog_to_earnings,pick_Dialog_SubWallets = null;
    EditText edtTransferQty;

    TextView txtTotalAccountAmount,txtTotalAccountAmountReceived;
    String totalaccountamount,totalaccountamountreceiving;
    EditText edtWithdrawableAmount;
    String totalwithdrawamount,withdrawtype;
    private RadioButton mEarnings;
    private RadioButton mReceivings;


    public static String GetPrefDefault(Context con, String Key, String Default) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = Default;
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, Default);
        }
        return Value;
    }

    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    private void mpesaWithdrawal(Double userBalance,Double witdrawAmount,String withdrawtype,String userId, String savedPhoneNo){
        //send for approval
        LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
            public void loadedData(String param1String) {
                if (param1String != null) {
                    JSONObject jObjectDetails;
                    try {
                        jObjectDetails = new JSONObject(param1String);
                        if (jObjectDetails == null) {
                            Snackbar snackbar = Snackbar
                                    .make(mainLayout, "Please check your internet connection!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            String success = jObjectDetails.getString("success");
                            if (success.equals("true")){
                                new AlertDialog.Builder(getContext()).setTitle("Withdraw request")
                                        .setMessage("You withdraw request of $"+witdrawAmount+" has been sent,pls wait for confirmation.").setIcon(R.drawable.navigation_accept).
                                        setNeutralButton("Close", null).show();
                                txtTotalAccountAmount.setText("$" + jObjectDetails.getString("newbal"));
                                pick_Dialog.dismiss();
                            } else {
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("accountamount",userBalance.toString()));
        nameValuePairs.add(new BasicNameValuePair("amount", witdrawAmount.toString()));
        nameValuePairs.add(new BasicNameValuePair("wtype", withdrawtype.toString()));
        nameValuePairs.add(new BasicNameValuePair("userid", userId));
        nameValuePairs.add(new BasicNameValuePair("phone", savedPhoneNo));
        mDataCache = new DataCache(getContext());
        try {
            mDataCache.loadData(URLProvider.LiveURL + "/api/v1/withdraw-request", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), loaderToUIListener);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onContextItemSelected (MenuItem item){
        pick_Dialog_to_earnings = null;
        pick_Dialog = null;

        switch (item.getItemId()) {
            case CONTEXT_SEND_TO_EARNINGS:{
                if (pick_Dialog_to_earnings == null) {
                    pick_Dialog_to_earnings = new Dialog(getContext());
                    pick_Dialog_to_earnings.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pick_Dialog_to_earnings.setCancelable(true);
                    pick_Dialog_to_earnings.setCanceledOnTouchOutside(true);
                    pick_Dialog_to_earnings.setContentView(R.layout.pop_sent_to_earnings);
                    pick_Dialog_to_earnings.show();
                    edtTransferQty = pick_Dialog_to_earnings.findViewById(R.id.edtTokenQty);

                    TextView btnExecute = pick_Dialog_to_earnings.findViewById(R.id.sendBtn);
                    btnExecute.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            if (edtTransferQty.getText().toString().trim().equals("")) {
                                Toast.makeText(getActivity(), "Please enter the valid amount", Toast.LENGTH_LONG).show();
                            } else {
                                LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                                    public void loadedData(String param1String) {
                                        if (param1String != null) {
                                            JSONObject jObjectDetails;
                                            try {
                                                jObjectDetails = new JSONObject(param1String);
                                                if (jObjectDetails == null) {
                                                    Snackbar snackbar = Snackbar
                                                            .make(mainLayout, "Please check your internet connection!", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                } else {
                                                    String success = jObjectDetails.getString("success");
                                                    if (success.equals("true")) {
                                                        pick_Dialog_to_earnings.dismiss();
                                                        gamerProfile();
                                                    } else {
                                                        Snackbar snackbar = Snackbar
                                                                .make(mainLayout, jObjectDetails.getString("message"), Snackbar.LENGTH_LONG);
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
                                nameValuePairs.add(new BasicNameValuePair("tokenqty",edtTransferQty.getText().toString().trim()));
                                nameValuePairs.add(new BasicNameValuePair("userid", userId));
                                mDataCache = new DataCache(getContext());
                                try {
                                    mDataCache.loadData(URLProvider.LiveURL + "/api/v1/send-to-earnings", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), loaderToUIListener);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else if (pick_Dialog_to_earnings.isShowing()) {
                    pick_Dialog_to_earnings.dismiss();
                } else {
                    pick_Dialog_to_earnings = null;
                }


            }
            break;
            case CONTEXT_BUY_TOKENS:{
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_purchasetokens);
            }
            break;
            case CONTEXT_TRANSFER_TOKENS:{
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_transfertokens);
            }
            break;
            case CONTEXT_WITHDRAW_TOKENS_FROM_EARNINGS:{

                pick_Dialog = null;
                if (pick_Dialog == null) {
                    pick_Dialog = new Dialog(getActivity());
                    pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pick_Dialog.setCancelable(true);
                    pick_Dialog.setCanceledOnTouchOutside(true);
                    pick_Dialog.setContentView(R.layout.pop_withdraw);
                    pick_Dialog.show();
                    edtWithdrawableAmount = pick_Dialog.findViewById(R.id.edtWithdrawableAmount);
                    txtTotalAccountAmount = pick_Dialog.findViewById(R.id.txtTotalAccountAmount);
                    txtTotalAccountAmountReceived = pick_Dialog.findViewById(R.id.txtTotalAccountAmountReceived);
                    edtWithdrawableAmount.setFocusable(true);

                    //totalaccountamount = txtTotalAccountAmount.getText().toString().trim();
                    mEarnings = (RadioButton) pick_Dialog.findViewById(R.id.radioearnings);
                    mReceivings = (RadioButton) pick_Dialog.findViewById(R.id.radioreceivings);

                    //RelativeLayout radiobuttons = pick_Dialog.findViewById(R.id.radiobuttons);
                    //radiobuttons.setVisibility(View.INVISIBLE);

                    RadioGroup radioGroup = (RadioGroup) pick_Dialog.findViewById(R.id.radio_group);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId){
                            // checkedId is the RadioButton selected
                            if(checkedId == R.id.radioearnings){
                                // is checked
                                totalwithdrawamount = totalaccountamount;
                                withdrawtype = "Earnings";
                            }
                            else if(checkedId == R.id.radioreceivings){
                                // not checked
                                totalwithdrawamount = totalaccountamountreceiving;
                                withdrawtype = "Receivings";
                            }else{
                                totalwithdrawamount = "0";
                                withdrawtype = "";
                            }
                        }
                    });

                    userId =  GetPref(getContext(), "Pref_User_ID");
                    savedPhoneNo = GetPref(getContext(), "Pref_Phone");
                    gamerName = GetPref(getContext(), "Pref_Username");

                    if(savedPhoneNo.equals("")){
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
                        Intent intent = new Intent(getContext(), Signin.class);
                        startActivity(intent);
                    }else {
                        LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                            public void loadedData(String param1String) {
                                if (param1String != null) {
                                    JSONObject jObjectDetails;
                                    try {
                                        jObjectDetails = new JSONObject(param1String);
                                        if (jObjectDetails == null) {
                                            Snackbar snackbar = Snackbar
                                                    .make(mainLayout, "Please check your internet connection!", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        } else {
                                            String success = jObjectDetails.getString("success");
                                            if (success.equals("true")) {
                                                txtTotalAccountAmount.setText("" + jObjectDetails.getString("totalearnings"));
                                                txtTotalAccountAmountReceived.setText("" + jObjectDetails.getString("totalreceived"));

                                                totalaccountamount = "" +jObjectDetails.getString("totalearnings");
                                                totalaccountamountreceiving = "" +jObjectDetails.getString("totalreceived");
                                            } else{
                                                totalaccountamount = "0";
                                                totalaccountamountreceiving = "0";
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    totalwithdrawamount = totalaccountamount;
                                    withdrawtype = "Earnings";
                                }
                            }
                        };
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("userid", userId));
                        mDataCache = new DataCache(getContext());
                        try {
                            mDataCache.loadData(URLProvider.LiveURL + "/api/v1/gamer-profile", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), loaderToUIListener);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }



                    btnvisa = pick_Dialog.findViewById(R.id.btnvisa);
                    btnvisa.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            String strTokenAmount = edtWithdrawableAmount.getText().toString().trim();
                            if (strTokenAmount.equals("")) {
                                Toast.makeText(getActivity(), "Please enter the valid amount", Toast.LENGTH_LONG).show();
                            } else {
                                //process withdraw
                                Double userBalance = Double.parseDouble(totalwithdrawamount);
                                Double witdrawAmount = Double.parseDouble(strTokenAmount);
                                if(userBalance <  witdrawAmount){
                                    new AlertDialog.Builder(getContext()).setTitle("Withdraw request")
                                            .setMessage("Your request of "+witdrawAmount+" tokens is more than the available "+withdrawtype+" account balance of "+ userBalance).setIcon(R.drawable.indicator_error).
                                            setNeutralButton("Close", null).show();
                                }else{
                                    mpesaWithdrawal(userBalance,witdrawAmount,withdrawtype,userId,savedPhoneNo);
                                }

                            }
                        }
                    });


                    btnmpesa = pick_Dialog.findViewById(R.id.btnmpesa);
                    btnmpesa.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            String strTokenAmount = edtWithdrawableAmount.getText().toString().trim();
                            if (strTokenAmount.equals("")) {
                                Toast.makeText(getActivity(), "Please enter the valid amount", Toast.LENGTH_LONG).show();
                            } else {
                                //process withdraw
                                Double userBalance = Double.parseDouble(totalwithdrawamount);
                                Double witdrawAmount = Double.parseDouble(strTokenAmount);
                                if(userBalance <  witdrawAmount){
                                    new AlertDialog.Builder(getContext()).setTitle("Withdraw request")
                                            .setMessage("Your request of "+witdrawAmount+" tokens is more than the available "+withdrawtype+" account balance of "+ userBalance).setIcon(R.drawable.indicator_error).
                                            setNeutralButton("Close", null).show();
                                }else{
                                    mpesaWithdrawal(userBalance,witdrawAmount,withdrawtype,userId,savedPhoneNo);
                                }

                            }
                        }
                    });

                } else if (pick_Dialog.isShowing()) {
                    pick_Dialog.dismiss();
                } else {
                    pick_Dialog = null;
                }

            }
            break;

            case CONTEXT_TOKENS_TO_MAIN_FROM_EARNINGS:{

                if (pick_Dialog_to_earnings == null) {
                    pick_Dialog_to_earnings = new Dialog(getContext());
                    pick_Dialog_to_earnings.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pick_Dialog_to_earnings.setCancelable(true);
                    pick_Dialog_to_earnings.setCanceledOnTouchOutside(true);
                    pick_Dialog_to_earnings.setContentView(R.layout.pop_sent_to_earnings);
                    pick_Dialog_to_earnings.show();
                    edtTransferQty = pick_Dialog_to_earnings.findViewById(R.id.edtTokenQty);

                    TextView btnExecute = pick_Dialog_to_earnings.findViewById(R.id.sendBtn);
                    btnExecute.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            if (edtTransferQty.getText().toString().trim().equals("")) {
                                Toast.makeText(getActivity(), "Please enter the valid amount", Toast.LENGTH_LONG).show();
                            } else {
                                LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                                    public void loadedData(String param1String) {
                                        if (param1String != null) {
                                            JSONObject jObjectDetails;
                                            try {
                                                jObjectDetails = new JSONObject(param1String);
                                                if (jObjectDetails == null) {
                                                    Snackbar snackbar = Snackbar
                                                            .make(mainLayout, "Please check your internet connection!", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                } else {
                                                    String success = jObjectDetails.getString("success");
                                                    if (success.equals("true")) {
                                                        pick_Dialog_to_earnings.dismiss();
                                                        gamerProfile();
                                                    } else {
                                                        Snackbar snackbar = Snackbar
                                                                .make(mainLayout, jObjectDetails.getString("message"), Snackbar.LENGTH_LONG);
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
                                nameValuePairs.add(new BasicNameValuePair("tokenqty",edtTransferQty.getText().toString().trim()));
                                nameValuePairs.add(new BasicNameValuePair("userid", userId));
                                mDataCache = new DataCache(getContext());
                                try {
                                    mDataCache.loadData(URLProvider.LiveURL + "/api/v1/send-to-main-from-earnings", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), loaderToUIListener);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else if (pick_Dialog_to_earnings.isShowing()) {
                    pick_Dialog_to_earnings.dismiss();
                } else {
                    pick_Dialog_to_earnings = null;
                }


            }
            break;

            case CONTEXT_TOKENS_TO_MAIN_FROM_RECEIVED:{

                if (pick_Dialog_to_earnings == null) {
                    pick_Dialog_to_earnings = new Dialog(getContext());
                    pick_Dialog_to_earnings.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pick_Dialog_to_earnings.setCancelable(true);
                    pick_Dialog_to_earnings.setCanceledOnTouchOutside(true);
                    pick_Dialog_to_earnings.setContentView(R.layout.pop_sent_to_earnings);
                    pick_Dialog_to_earnings.show();
                    edtTransferQty = pick_Dialog_to_earnings.findViewById(R.id.edtTokenQty);

                    TextView btnExecute = pick_Dialog_to_earnings.findViewById(R.id.sendBtn);
                    btnExecute.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            if (edtTransferQty.getText().toString().trim().equals("")) {
                                Toast.makeText(getActivity(), "Please enter the valid amount", Toast.LENGTH_LONG).show();
                            } else {
                                LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                                    public void loadedData(String param1String) {
                                        if (param1String != null) {
                                            JSONObject jObjectDetails;
                                            try {
                                                jObjectDetails = new JSONObject(param1String);
                                                if (jObjectDetails == null) {
                                                    Snackbar snackbar = Snackbar
                                                            .make(mainLayout, "Please check your internet connection!", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                } else {
                                                    String success = jObjectDetails.getString("success");
                                                    if (success.equals("true")) {
                                                        pick_Dialog_to_earnings.dismiss();
                                                        gamerProfile();
                                                    } else {
                                                        Snackbar snackbar = Snackbar
                                                                .make(mainLayout, jObjectDetails.getString("message"), Snackbar.LENGTH_LONG);
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
                                nameValuePairs.add(new BasicNameValuePair("tokenqty",edtTransferQty.getText().toString().trim()));
                                nameValuePairs.add(new BasicNameValuePair("userid", userId));
                                mDataCache = new DataCache(getContext());
                                try {
                                    mDataCache.loadData(URLProvider.LiveURL + "/api/v1/send-to-main-from-received", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), loaderToUIListener);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else if (pick_Dialog_to_earnings.isShowing()) {
                    pick_Dialog_to_earnings.dismiss();
                } else {
                    pick_Dialog_to_earnings = null;
                }


            }
            break;
            case CONTEXT_WITHDRAW_TOKENS_FROM_RECEIVED:{

                pick_Dialog = null;

                if (pick_Dialog == null) {
                    pick_Dialog = new Dialog(getActivity());
                    pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pick_Dialog.setCancelable(true);
                    pick_Dialog.setCanceledOnTouchOutside(true);
                    pick_Dialog.setContentView(R.layout.pop_withdraw);
                    pick_Dialog.show();
                    edtWithdrawableAmount = pick_Dialog.findViewById(R.id.edtWithdrawableAmount);
                    txtTotalAccountAmount = pick_Dialog.findViewById(R.id.txtTotalAccountAmount);
                    txtTotalAccountAmountReceived = pick_Dialog.findViewById(R.id.txtTotalAccountAmountReceived);
                    edtWithdrawableAmount.setFocusable(true);

                    //totalaccountamount = txtTotalAccountAmount.getText().toString().trim();
                    mEarnings = (RadioButton) pick_Dialog.findViewById(R.id.radioearnings);
                    mReceivings = (RadioButton) pick_Dialog.findViewById(R.id.radioreceivings);

                    //RelativeLayout radiobuttons = pick_Dialog.findViewById(R.id.radiobuttons);
                    //radiobuttons.setVisibility(View.INVISIBLE);

                    RadioGroup radioGroup = (RadioGroup) pick_Dialog.findViewById(R.id.radio_group);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId){
                            // checkedId is the RadioButton selected
                            if(checkedId == R.id.radioearnings){
                                // is checked
                                totalwithdrawamount = totalaccountamount;
                                withdrawtype = "Earnings";
                            }
                            else if(checkedId == R.id.radioreceivings){
                                // not checked
                                totalwithdrawamount = totalaccountamountreceiving;
                                withdrawtype = "Receivings";
                            }else{
                                totalwithdrawamount = "0";
                                withdrawtype = "";
                            }
                        }
                    });

                    userId =  GetPref(getContext(), "Pref_User_ID");
                    savedPhoneNo = GetPref(getContext(), "Pref_Phone");
                    gamerName = GetPref(getContext(), "Pref_Username");

                    if(savedPhoneNo.equals("")){
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
                        Intent intent = new Intent(getContext(), Signin.class);
                        startActivity(intent);
                    }else {
                        LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                            public void loadedData(String param1String) {
                                if (param1String != null) {
                                    JSONObject jObjectDetails;
                                    try {
                                        jObjectDetails = new JSONObject(param1String);
                                        if (jObjectDetails == null) {
                                            Snackbar snackbar = Snackbar
                                                    .make(mainLayout, "Please check your internet connection!", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        } else {
                                            String success = jObjectDetails.getString("success");
                                            if (success.equals("true")) {
                                                txtTotalAccountAmount.setText("" + jObjectDetails.getString("totalearnings"));
                                                txtTotalAccountAmountReceived.setText("" + jObjectDetails.getString("totalreceived"));

                                                totalaccountamount = "" +jObjectDetails.getString("totalearnings");
                                                totalaccountamountreceiving = "" +jObjectDetails.getString("totalreceived");
                                            } else{
                                                totalaccountamount = "0";
                                                totalaccountamountreceiving = "0";
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    totalwithdrawamount = totalaccountamountreceiving;
                                    withdrawtype = "Receivings";
                                }
                            }
                        };
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("userid", userId));
                        mDataCache = new DataCache(getContext());
                        try {
                            mDataCache.loadData(URLProvider.LiveURL + "/api/v1/gamer-profile", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), loaderToUIListener);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    btnvisa = pick_Dialog.findViewById(R.id.btnvisa);
                    btnvisa.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            String strTokenAmount = edtWithdrawableAmount.getText().toString().trim();
                            if (strTokenAmount.equals("")) {
                                Toast.makeText(getActivity(), "Please enter the valid amount", Toast.LENGTH_LONG).show();
                            } else {
                                //process withdraw
                                Double userBalance = Double.parseDouble(totalwithdrawamount);
                                Double witdrawAmount = Double.parseDouble(strTokenAmount);
                                if(userBalance <  witdrawAmount){
                                    new AlertDialog.Builder(getContext()).setTitle("Withdraw request")
                                            .setMessage("Your request of "+witdrawAmount+" tokens is more than the available "+withdrawtype+" account balance of "+ userBalance).setIcon(R.drawable.indicator_error).
                                            setNeutralButton("Close", null).show();
                                }else{
                                    mpesaWithdrawal(userBalance,witdrawAmount,withdrawtype,userId,savedPhoneNo);
                                }

                            }
                        }
                    });


                    btnmpesa = pick_Dialog.findViewById(R.id.btnmpesa);
                    btnmpesa.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            String strTokenAmount = edtWithdrawableAmount.getText().toString().trim();
                            if (strTokenAmount.equals("")) {
                                Toast.makeText(getActivity(), "Please enter the valid amount", Toast.LENGTH_LONG).show();
                            } else {
                                //process withdraw
                                Double userBalance = Double.parseDouble(totalwithdrawamount);
                                Double witdrawAmount = Double.parseDouble(strTokenAmount);
                                if(userBalance <  witdrawAmount){
                                    new AlertDialog.Builder(getContext()).setTitle("Withdraw request")
                                            .setMessage("Your request of "+witdrawAmount+" tokens is more than the available "+withdrawtype+" account balance of "+ userBalance).setIcon(R.drawable.indicator_error).
                                            setNeutralButton("Close", null).show();
                                }else{
                                    mpesaWithdrawal(userBalance,witdrawAmount,withdrawtype,userId,savedPhoneNo);
                                }

                            }
                        }
                    });

                } else if (pick_Dialog.isShowing()) {
                    pick_Dialog.dismiss();
                } else {
                    pick_Dialog = null;
                }

            }
            break;
            case CONTEXT_BUY_TICKETS:{
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_fourtune_events);
            }
            break;
            case CONTEXT_UNLOCK_GAMES:{
                //create a layout with the sub wallets
                pick_Dialog_SubWallets = null;

                ArrayList<Res_Sub_Wallets> WalletsEarnedDAO = new ArrayList<Res_Sub_Wallets>();
                SubWalletAdapter coins_earned_adapter =  new SubWalletAdapter(getActivity(), WalletsEarnedDAO);
                RecyclerView recycler_coins_earned;


                if (pick_Dialog_SubWallets == null) {
                    pick_Dialog_SubWallets = new Dialog(getContext());
                    pick_Dialog_SubWallets.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pick_Dialog_SubWallets.setCancelable(true);
                    pick_Dialog_SubWallets.setCanceledOnTouchOutside(true);
                    pick_Dialog_SubWallets.setContentView(R.layout.pop_sub_wallets);
                    pick_Dialog_SubWallets.show();

                    recycler_coins_earned = pick_Dialog_SubWallets.findViewById(R.id.recycler_events);

                    LoaderToUIListener loaderToUIListener = new LoaderToUIListener(){
                        public void loadedData(String param1String) {
                            WalletsEarnedDAO.clear();
                            JSONObject jObject = new JSONObject();
                            try {
                                if(param1String != null) {
                                    jObject = new JSONObject(param1String);

                                    JSONObject jSONObject = jObject.getJSONObject("data");
                                    JSONObject jSONObject_CoinsEarned = jSONObject.getJSONObject("dbdata");
                                    JSONArray j = jSONObject_CoinsEarned.getJSONArray("walletdetails");
                                    if (j != null) {
                                        for (int i = 0; i < j.length(); i++) {
                                            JSONObject Obj;
                                            Obj = j.getJSONObject(i);
                                            Res_Sub_Wallets temp = new Res_Sub_Wallets();
                                            temp.setId(Obj.getString("id"));
                                            temp.setState(Obj.getString("state"));
                                            temp.setGame(Obj.getString("game"));
                                            temp.setCoins(Obj.getString("coins"));
                                            temp.setUserId(Obj.getString("user_id"));
                                            WalletsEarnedDAO.add(temp);
                                        }
                                        SubWalletAdapter lazyAdapter = new SubWalletAdapter(getActivity(), WalletsEarnedDAO);
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

                    userId =  GetPref(getContext(), "Pref_User_ID");
                    savedPhoneNo = GetPref(getContext(), "Pref_Phone");
                    gamerName = GetPref(getContext(), "Pref_Username");
                    if(savedPhoneNo.equals("")){
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
                        Intent intent = new Intent(getContext(), Signin.class);
                        startActivity(intent);
                    }else {
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recycler_coins_earned.setLayoutManager(mLayoutManager);
                        recycler_coins_earned.setAdapter(coins_earned_adapter);
                        this.mDataCache = new DataCache(getContext());
                        try {
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("userid",userId));
                            this.mDataCache.loadData(URLProvider.LiveURL + "/api/v1/sub-wallets", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                    ImageButton btnclose = pick_Dialog_SubWallets.findViewById(R.id.btnclose);
                    btnclose.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            if (pick_Dialog_SubWallets.isShowing()) {
                                pick_Dialog_SubWallets.dismiss();
                            } else {
                                pick_Dialog_SubWallets = null;
                            }
                        }
                    });

                } else if (pick_Dialog_SubWallets.isShowing()) {
                    pick_Dialog_SubWallets.dismiss();
                } else {
                    pick_Dialog_SubWallets = null;
                }




            }
            break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // you can set menu header with title icon etc
        if(v.getId() == R.id.settings_main_account ){
            menu.setHeaderTitle("Main Account");
            ///menu.add(0, CONTEXT_SEND_TO_EARNINGS, 0, "Send tokens to earnings acc.");
            menu.add(0, CONTEXT_BUY_TOKENS , 0, "Buy tokens");//CONTEXT_BUY_TICKETS
            menu.add(0, CONTEXT_UNLOCK_GAMES, 0, "Unlock games");
            menu.add(0, CONTEXT_BUY_TICKETS , 0, "Buy event tickets");
            menu.add(0, CONTEXT_TRANSFER_TOKENS, 0, "Transfer tokens");

        }else if(v.getId() == R.id.settings_earnings_account ){
            menu.setHeaderTitle("Earnings Account");
            menu.add(0, CONTEXT_TOKENS_TO_MAIN_FROM_EARNINGS, 0, "Send tokens to main account");
            menu.add(0, CONTEXT_WITHDRAW_TOKENS_FROM_EARNINGS, 0, "Withdraw tokens");
        }else if(v.getId() == R.id.settings_received_account ){
            menu.setHeaderTitle("Receiving Account");
            menu.add(0, CONTEXT_TOKENS_TO_MAIN_FROM_RECEIVED, 0, "Send tokens to main account");
            menu.add(0, CONTEXT_WITHDRAW_TOKENS_FROM_RECEIVED, 0, "Withdraw tokens");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);
        mainLayout =  root.findViewById(R.id.mainLayout);
        txtUserTokenAmount =  root.findViewById(R.id.txtUserTokenAmount);
        txtEarnings  =  root.findViewById(R.id.txtEarnings);
        txtReceivedTokens =  root.findViewById(R.id.txtReceivedTokens);
        settings_main_account = root.findViewById(R.id.settings_main_account);


        totalwithdrawamount = "0";
        withdrawtype = "";

        lblemail = root.findViewById(R.id.lblemail);
        lblphone = root.findViewById(R.id.lblphone);
        lblcreated_at = root.findViewById(R.id.lblcreated_at);

        registerForContextMenu(settings_main_account);
        settings_main_account.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                settings_main_account.showContextMenu();
            }
        });

        settings_earnings_account = root.findViewById(R.id.settings_earnings_account);
        registerForContextMenu(settings_earnings_account);
        settings_earnings_account.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                settings_earnings_account.showContextMenu();
            }
        });

        settings_received_account = root.findViewById(R.id.settings_received_account);
        registerForContextMenu(settings_received_account);
        settings_received_account.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                settings_received_account.showContextMenu();
            }
        });


        txtGamerName =  root.findViewById(R.id.txtGamerName);
        lbl_account_date =  root.findViewById(R.id.lbl_account_date);
        daraja = Daraja.with(getString(R.string.consumerKey), getString(R.string.consumerSecret), Env.PRODUCTION, new DarajaListener<AccessToken>(){
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
            }
            @Override
            public void onError(String error){
            }
        });

        userId =  GetPref(getContext(), "Pref_User_ID");
        savedPhoneNo = GetPref(getContext(), "Pref_Phone");
        gamerName = GetPref(getContext(), "Pref_Username");

        if(savedPhoneNo.equals("")){
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
            Intent intent = new Intent(getContext(), Signin.class);
            startActivity(intent);

        }else {
            txtUserTokenAmount.setText("0.00");
            txtEarnings.setText("0.00");
            txtReceivedTokens.setText("0.00");

            txtGamerName.setText("Hi, "+gamerName+"!");



            LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                public void loadedData(String param1String){
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
                                    txtEarnings.setText(""+jObjectDetails.getString("totalearnings"));
                                    txtReceivedTokens.setText(""+jObjectDetails.getString("totalreceived"));

                                    lbl_account_date.setText(jObjectDetails.getString("account_notification"));
                                    tokenvalue = jObjectDetails.getString("token_price");
                                    dollar_value = jObjectDetails.getString("dollar_value");

                                    String uDetails = jObjectDetails.getString("gamer");
                                    JSONArray jsonArr = new JSONArray(uDetails);
                                    JSONObject gamerObject = jsonArr.getJSONObject(0);
                                    String uname = "Phone : "+gamerObject.getString("phone");
                                    String uemail = "Email : "+gamerObject.getString("email");
                                    String ucreated_at = "Registration Date : "+gamerObject.getString("created_at");
                                    lblemail.setText(uname);
                                    lblphone.setText(uemail);
                                    lblcreated_at.setText(ucreated_at);

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


        }
        return root;
    }



    private void gamerProfile(){
        LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
            public void loadedData(String param1String){
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
                                txtEarnings.setText(""+jObjectDetails.getString("totalearnings"));
                                txtReceivedTokens.setText(""+jObjectDetails.getString("totalreceived"));

                                lbl_account_date.setText(jObjectDetails.getString("account_notification"));
                                tokenvalue = jObjectDetails.getString("token_price");
                                dollar_value = jObjectDetails.getString("dollar_value");

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
    }


    private void mpesaPayments(String TokenCost, String userId, String savedPhoneNo, String TokenQty){
        LNMExpress lnmExpress = new LNMExpress(
                getString(R.string.businessShortcode),
                getString(R.string.passKey),
                TransactionType.CustomerPayBillOnline,
                TokenCost,
                savedPhoneNo,
                getString(R.string.businessTillnumber),
                savedPhoneNo,
                getString(R.string.callBackURL),
                getString(R.string.businessShortcode),
                "Fourtune Purchase"
        );
        daraja.requestMPESAExpress(lnmExpress,new DarajaListener<LNMResult>() {
            @Override
            public void onResult(@NonNull LNMResult lnmResult) {
                if (lnmResult.ResponseCode.equals("0")) {
                    mSaveTask = new SaveTask(TokenQty.toString(), userId , lnmResult.MerchantRequestID , lnmResult.ResponseDescription);
                    mSaveTask.execute((String) null);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(mainLayout, lnmResult.ResponseDescription, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
            @Override
            public void onError(String error){
                new AlertDialog.Builder(getContext()).setTitle("Fourtune Purchases")
                        .setMessage("An error occured ("+error+"),pls try again").setIcon(R.drawable.indicator_error).
                        setNeutralButton("Close", null).show();
                Snackbar snackbar = Snackbar
                        .make(mainLayout, "An error occured,please try again!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    mSaveTask = new SaveTask(strTokenQty, userId,"","paypal");
                    mSaveTask.execute((String) null);
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    public String cardPayments(String amount, String qty,String userid){
        PayPalConfiguration config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION) //ENVIRONMENT_NO_NETWORK
                .clientId(getString(R.string.payPalClientId));
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);

        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", "Fourtune Token Purchase", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intentPay = new Intent(getActivity(), PaymentActivity.class);
        // send the same configuration for restart resiliency
        intentPay.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intentPay.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        intentPay.putExtra(PayPalPayment.PAYMENT_INTENT_SALE,userid);
        intentPay.putExtra(PayPalPayment.PAYMENT_INTENT_SALE,amount);
        startActivityForResult(intentPay, 0);
        return "";
    }

    public class SaveTask extends AsyncTask<String, Void, JSONObject> {
        private final String mTokenQty;
        private final String mUserID;
        private final String mMrequestId;
        private final String mResponseDesc;

        SaveTask(String TokenQty,String mUserID , String mMRequestId, String  mResponseDesc) {
            this.mTokenQty = TokenQty;
            this.mUserID = mUserID;
            this.mMrequestId = mMRequestId;
            this.mResponseDesc = mResponseDesc;
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

                                    if(strPaytype.equals("visa")) {
                                        new AlertDialog.Builder(getContext()).setTitle("Token Purchase")
                                                .setMessage(jObjectDetails.getString("message")).setIcon(R.drawable.indicator_ok).
                                                setNeutralButton("Close", null).show();
                                        Snackbar snackbar = Snackbar
                                                .make(mainLayout, jObjectDetails.getString("message"), Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }

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
            nameValuePairs.add(new BasicNameValuePair("tokenqty", this.mTokenQty));
            nameValuePairs.add(new BasicNameValuePair("userid", this.mUserID));
            nameValuePairs.add(new BasicNameValuePair("merchantid", this.mMrequestId));
            nameValuePairs.add(new BasicNameValuePair("merchdesc", this.mResponseDesc));

            mDataCache = new DataCache(getContext());
            try {
                mDataCache.loadData(URLProvider.LiveURL+"/api/v1/purchase-token",URLProvider.purchaseToken(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
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


























}
