
package com.coreictconsultancy.fourtune.adapter;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.opengl.Visibility;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.navigation.Navigation;
        import androidx.recyclerview.widget.RecyclerView;

        import com.coreictconsultancy.fourtune.R;
        import com.coreictconsultancy.fourtune.data.DataCache;
        import com.coreictconsultancy.fourtune.data.LoaderToUIListener;
        import com.coreictconsultancy.fourtune.data.URLProvider;
        import com.coreictconsultancy.fourtune.pojo.Res_Sub_Wallets;
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

public class SubWalletAdapter extends RecyclerView.Adapter<SubWalletAdapter.MyViewHolder> {
    private ArrayList<Res_Sub_Wallets> listcoinsearned;
    Activity act;
    Context mContext;
    protected DataCache mDataCache;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtState,txtGame,txtLevel,txtCoins;
        Button btnUnlockGame;
        ImageView imageView8;
        RelativeLayout mainLayout;

        public MyViewHolder(View view) {
            super(view);
            txtState = view.findViewById(R.id.txtState);
            txtGame = view.findViewById(R.id.txtGame);
            txtCoins = view.findViewById(R.id.txtCoins);
            btnUnlockGame = view.findViewById(R.id.btnUnlockGame);
            imageView8 = view.findViewById(R.id.imageView8);
            mainLayout = view.findViewById(R.id.mainLayout);
        }
    }
    public SubWalletAdapter(Activity act, ArrayList<Res_Sub_Wallets> listcoinsearned) {
        this.act = act;
        this.listcoinsearned = listcoinsearned;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()) .inflate(R.layout.sub_wallet_item, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Sub_Wallets list = listcoinsearned.get(position);
        holder.txtState.setText(list.getState());
        holder.txtGame.setText(list.getGame());
        holder.txtCoins.setText(list.getCoins());

        if(list.getState().equals("Locked")){
            holder.btnUnlockGame.setVisibility(View.VISIBLE);
            holder.txtState.setVisibility(View.GONE);
            holder.imageView8.setImageDrawable(act.getResources().getDrawable(R.drawable.padlockclosed));
        }else{
            holder.btnUnlockGame.setVisibility(View.GONE);
            holder.txtState.setVisibility(View.VISIBLE);
            holder.imageView8.setImageDrawable(act.getResources().getDrawable(R.drawable.padlockopen));
        }

        holder.btnUnlockGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                LoaderToUIListener loaderToUIListener = new LoaderToUIListener() {
                    public void loadedData(String param1String) {
                        if (param1String != null) {
                            JSONObject jObjectDetails;
                            try {
                                jObjectDetails = new JSONObject(param1String);
                                if (jObjectDetails == null) {
                                    Snackbar snackbar = Snackbar
                                            .make(holder.mainLayout, "Please check your internet connection!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    String success = jObjectDetails.getString("success");
                                    if (success.equals("true")) {
                                        holder.btnUnlockGame.setVisibility(View.GONE);
                                        holder.txtState.setVisibility(View.VISIBLE);
                                        holder.imageView8.setImageDrawable(act.getResources().getDrawable(R.drawable.padlockopen));
                                        holder.txtCoins.setText(jObjectDetails.getString("deposit"));
                                        holder.txtState.setText("Unlocked");
                                        Toast toast = Toast. makeText(mContext,"Successfully unlocked "+ list.getGame() + "!", Toast. LENGTH_SHORT);
                                        toast. show();
                                    } else {
                                        Snackbar snackbar = Snackbar
                                                .make(holder.mainLayout, jObjectDetails.getString("message"), Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                        builder1.setMessage(jObjectDetails.getString("message")+"\n\n Do you wish to Buy tokens?");
                                        builder1.setCancelable(true);
                                        builder1.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Bundle args = new Bundle();
                                                        Navigation.findNavController((AppCompatActivity)act, R.id.nav_host_fragment).navigate(R.id.nav_purchasetokens,args);
                                                    }
                                                });
                                        builder1.setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("game",list.getGame().toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("userid",list.getUserId()));
                mDataCache = new DataCache(mContext);
                try {
                    mDataCache.loadData(URLProvider.LiveURL + "/api/v1/unlock-game", URLProvider.getApi(new UrlEncodedFormEntity(nameValuePairs)), loaderToUIListener);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }




            }
        });


    }
    @Override
    public int getItemCount() {
        return listcoinsearned.size();
    }

}









