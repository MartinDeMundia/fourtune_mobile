package com.coreictconsultancy.fourtune.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.pojo.Res_Coins_Earned;
import java.util.ArrayList;

public class CoinsEarnedAdapter extends RecyclerView.Adapter<CoinsEarnedAdapter.MyViewHolder> {
    private ArrayList<Res_Coins_Earned> listcoinsearned;
    Activity act;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate,txtGame,txtLevel,txtCoins;
        public MyViewHolder(View view) {
            super(view);
            txtDate = view.findViewById(R.id.txtState);
            txtGame = view.findViewById(R.id.txtGame);
            txtLevel = view.findViewById(R.id.txtLevel);
            txtCoins = view.findViewById(R.id.txtCoins);
        }
    }
    public CoinsEarnedAdapter(Activity act, ArrayList<Res_Coins_Earned> listcoinsearned) {
        this.act = act;
        this.listcoinsearned = listcoinsearned;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()) .inflate(R.layout.coins_earned_item, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Coins_Earned list = listcoinsearned.get(position);
        holder.txtDate.setText(list.getDate());
        holder.txtGame.setText(list.getGame());
        holder.txtLevel.setText(list.getLevel());
        holder.txtCoins.setText(list.getCoins());
    }
    @Override
    public int getItemCount() {
        return listcoinsearned.size();
    }

}









