package com.coreictconsultancy.fourtune.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coreictconsultancy.blockish.SplashScreen;
import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.adapter.SubWalletAdapter;
import com.coreictconsultancy.fourtune.data.DataCache;
import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
import com.coreictconsultancy.fourtune.data.URLProvider;
import com.coreictconsultancy.fourtune.pojo.Res_Sub_Wallets;
import com.coreictconsultancy.solitaire.SolitaireCG;
import com.google.android.material.snackbar.Snackbar;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import jwtc.chess.start;

import static com.swarmconnect.Swarm.getApplicationContext;


public class HomeFragment extends Fragment implements View.OnClickListener {


    View root;
    protected DataCache mDataCache;
    String userId,savedPhoneNo,gamerName = null;
    public static SharedPreferences sharedpreferences;
    Dialog pick_Dialog_SubWallets = null;

    public  String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences("Fourtune", Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.fragment_home, container, false);

        userId =  GetPref(getContext(), "Pref_User_ID");
        savedPhoneNo = GetPref(getContext(), "Pref_Phone");
        gamerName = GetPref(getContext(), "Pref_Username");

        RelativeLayout bounce = (RelativeLayout) root.findViewById(R.id.bounce);
        bounce.setOnClickListener(this);

        RelativeLayout flash = (RelativeLayout) root.findViewById(R.id.flash);
        flash.setOnClickListener(this);

        RelativeLayout pulse = (RelativeLayout) root.findViewById(R.id.pulse);
        pulse.setOnClickListener(this);

        RelativeLayout rubber_band = (RelativeLayout) root.findViewById(R.id.rubber_band);
        rubber_band.setOnClickListener(this);

        RelativeLayout shake = (RelativeLayout) root.findViewById(R.id.shake);
        shake.setOnClickListener(this);

        RelativeLayout shake_band = (RelativeLayout) root.findViewById(R.id.shake_band);
        shake_band.setOnClickListener(this);

        RelativeLayout stand_up = (RelativeLayout) root.findViewById(R.id.stand_up);
        stand_up.setOnClickListener(this);

        RelativeLayout swing = (RelativeLayout) root.findViewById(R.id.swing);
        swing.setOnClickListener(this);

        RelativeLayout tada = (RelativeLayout) root.findViewById(R.id.tada);
        tada.setOnClickListener(this);

        return root;
    }




    public void animationStart(View v){

        int timeanimation = 2000;
        int negative_timeanimation = 500;

        if(v.getId() == (R.id.bounce)){
            StartSmartAnimation.startAnimation(root.findViewById(R.id.bounce), AnimationType.Bounce, timeanimation ,0 , false);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(getContext(), SolitaireCG.class);
                            startActivity(intent);

                            Snackbar.make(v, "Loading...Please wait", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    timeanimation-negative_timeanimation
            );
        }
        else if(v.getId() == (R.id.flash)){
            StartSmartAnimation.startAnimation(v, AnimationType.Flash, 2000 , 0 ,true);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), SplashScreen.class);
                            startActivity(intent);

                            Snackbar.make(v, "Loading...Please wait", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    timeanimation-negative_timeanimation
            );
        }
        else if(v.getId() == (R.id.pulse)){
            StartSmartAnimation.startAnimation(v, AnimationType.Pulse, 2000 , 0 ,false);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), com.coreictconsultancy.maze.MainActivity.class);
                            startActivity(intent);

                            Snackbar.make(v, "Loading...Please wait", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    timeanimation-negative_timeanimation
            );
        }
        else if(v.getId() == (R.id.rubber_band)){
            StartSmartAnimation.startAnimation(v, AnimationType.RubberBand, 2000 , 0 ,false);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(),com.coreictconsultancy.blockinger.activities.MainActivity.class);
                            startActivity(intent);

                            Snackbar.make(v, "Loading...Please wait", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    timeanimation-negative_timeanimation
            );
        }
        else if(v.getId() == (R.id.shake)){
            StartSmartAnimation.startAnimation(v, AnimationType.Shake, 2000 ,0 , false);

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(),com.coreictconsultancy.mario.mario.core.MarioGame.class);
                            startActivity(intent);

                            Snackbar.make(v, "Loading...Please wait", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    timeanimation-negative_timeanimation
            );

        }
        else if(v.getId() == (R.id.shake_band)){
            StartSmartAnimation.startAnimation(v, AnimationType.ShakeBand, 2000 , 0 ,false);

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(),com.coreictconsultancy.candycrush.Menu.class);
                            startActivity(intent);

                            Snackbar.make(v, "Loading...Please wait", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    timeanimation-negative_timeanimation
            );

        }
        else if(v.getId() == (R.id.stand_up)){
            StartSmartAnimation.startAnimation(v, AnimationType.StandUp, 2000 , 0 , false);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), start.class);
                            startActivity(intent);

                            Snackbar.make(v, "Loading...Please wait", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    },
                    timeanimation-negative_timeanimation
            );

        }
        else if(v.getId() == (R.id.swing)){
            StartSmartAnimation.startAnimation(v, AnimationType.Swing, 2000 , 0 , false);
        }
        else if(v.getId() == (R.id.tada)){
            StartSmartAnimation.startAnimation(v, AnimationType.Tada, 2000 , 0 , false);
        }
    }

    public void buyTokenPopUp(){
        //create a layout with the sub wallets
        pick_Dialog_SubWallets = null;
        TextView txtPopuplabel;

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
            txtPopuplabel = pick_Dialog_SubWallets.findViewById(R.id.txtPopuplabel);
            txtPopuplabel.setText("Unlock Fourtune Games");

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

    @Override
    public void onClick(View view) {
        String gametype = "";
        //check user to have atleast 1 token else direct to purchases
        if(savedPhoneNo.equals("")){
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
            Intent intent = new Intent(getContext(), Signin.class);
            startActivity(intent);

        }else {

            View v = view;
            int timeanimation = 2000;

            if(v.getId() == (R.id.bounce)){
                StartSmartAnimation.startAnimation(v, AnimationType.Bounce, timeanimation ,0 , false);
                gametype = "Spider Solitary";
            }
            else if(v.getId() == (R.id.flash)){
                StartSmartAnimation.startAnimation(v, AnimationType.Flash, 2000 , 0 ,true);
                gametype = "Blockish";
            }
            else if(v.getId() == (R.id.pulse)){
                StartSmartAnimation.startAnimation(v, AnimationType.Pulse, 2000 , 0 ,false);
                gametype = "Maze";
            }
            else if(v.getId() == (R.id.rubber_band)){
                StartSmartAnimation.startAnimation(v, AnimationType.RubberBand, 2000 , 0 ,false);
                gametype = "Tetris";
            }
            else if(v.getId() == (R.id.shake)){
                StartSmartAnimation.startAnimation(v, AnimationType.Shake, 2000 ,0 , false);
                gametype = "Super Mario";
            }
            else if(v.getId() == (R.id.shake_band)){
                StartSmartAnimation.startAnimation(v, AnimationType.ShakeBand, 2000 , 0 ,false);
                gametype = "Candy Crush";
            }
            else if(v.getId() == (R.id.stand_up)){
                StartSmartAnimation.startAnimation(v, AnimationType.StandUp, 2000 , 0 , false);
                gametype = "Chess";
            }
            else if(v.getId() == (R.id.swing)){
                StartSmartAnimation.startAnimation(v, AnimationType.Swing, 2000 , 0 , false);
                gametype = "";
            }
            else if(v.getId() == (R.id.tada)){
                StartSmartAnimation.startAnimation(v, AnimationType.Tada, 2000 , 0 , false);
                gametype = "";
            }



            LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                public void loadedData(String param1String) {
                    if (param1String != null) {
                        JSONObject jObjectDetails;
                        try {
                            jObjectDetails = new JSONObject(param1String);
                            if (jObjectDetails == null) {
                                Snackbar snackbar = Snackbar
                                        .make(view, "Please check your internet connection!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                String success = jObjectDetails.getString("success");

                                if (success.equals("true")) {
                                    String coinamount = jObjectDetails.getString("unlocktoken");
                                    Double camount = Double.parseDouble(coinamount);
                                    if(camount < 1){ // no enough tokens
                                       // Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_purchasetokens);
                                        Snackbar.make(view, jObjectDetails.getString("message"), Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        buyTokenPopUp();
                                    }else{
                                        animationStart(view);
                                    }

                                } else {
                                    Snackbar.make(view, jObjectDetails.getString("message"), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    buyTokenPopUp();
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
            nameValuePairs.add(new BasicNameValuePair("game",gametype));
            mDataCache = new DataCache(getContext());
            try {
                mDataCache.loadData(URLProvider.LiveURL+"/api/v1/check-token",URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)),loaderToUIListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

    }
}