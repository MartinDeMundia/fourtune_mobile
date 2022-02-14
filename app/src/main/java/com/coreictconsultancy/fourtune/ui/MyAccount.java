package com.coreictconsultancy.fourtune.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.TransactionType;
import com.coreictconsultancy.fourtune.MainActivity;
import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.swarmconnect.Swarm.getApplicationContext;

public class MyAccount extends Fragment {

    Dialog pick_Dialog;
    EditText edtWithdrawableAmount;
    TextView txtTotalAccountAmount,txtTotalAccountAmountReceived;
    String totalwithdrawamount,withdrawtype;
    ImageButton btnmpesa,btnvisa;
    protected DataCache mDataCache;
    String userId,savedPhoneNo,gamerName = null;
    public static SharedPreferences sharedpreferences;
    LinearLayout mainLayout;
    String totalaccountamount,totalaccountamountreceiving;
    static Daraja daraja;
    private RadioButton mEarnings;
    private RadioButton mReceivings;


    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myaccount, container, false);
        totalwithdrawamount = "0";
        withdrawtype = "";

        daraja = Daraja.with(getString(R.string.consumerKey), getString(R.string.consumerSecret), Env.PRODUCTION, new DarajaListener<AccessToken>(){
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
            }
            @Override
            public void onError(String error){
            }
        });


        LinearLayout linearLayoutMyAccount = (LinearLayout) root.findViewById(R.id.linearlaymyAccount);
        linearLayoutMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(getActivity(), MyAccount.class); //change to account view
               // startActivity(intent);
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_myprofile);
            }
        });

        LinearLayout linearlaylogout = (LinearLayout) root.findViewById(R.id.linearlaylogout);
        linearlaylogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Signin.class); //change to account view
                startActivity(intent);
            }
        });

        LinearLayout linearlaypurchasetoken = (LinearLayout) root.findViewById(R.id.linearlaypurchasetoken);
        linearlaypurchasetoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.nav_purchasetokens);
            }
        });

        LinearLayout lineartransfertoken = (LinearLayout) root.findViewById(R.id.lineartransfertoken);
        lineartransfertoken.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_transfertokens);
            }
        });

        mainLayout = (LinearLayout) root.findViewById(R.id.linearLayout);

        LinearLayout linearwithdraw = (LinearLayout) root.findViewById(R.id.linearwithdraw);
        linearwithdraw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

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


        });


        return root;
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



}
