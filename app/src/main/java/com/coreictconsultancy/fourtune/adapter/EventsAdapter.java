package com.coreictconsultancy.fourtune.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.coreictconsultancy.fourtune.R;
import com.coreictconsultancy.fourtune.pojo.Res_Events;
import com.coreictconsultancy.fourtune.ui.EventDetails;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class EventsAdapter extends BaseAdapter {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private Context mContext;
    private ArrayList<Res_Events> mData;
    private LayoutInflater mInflater;
    private int mType = 0;
    DisplayImageOptions options;

    public EventsAdapter(Context paramContext, ArrayList<Res_Events> paramArrayList) {
        this.mContext = paramContext;
        this.mData = paramArrayList;
        this.mType = 0;
        this.mInflater = LayoutInflater.from(this.mContext);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this.mContext));
        this.options = (new DisplayImageOptions.Builder()).showImageOnLoading(R.drawable.events).showImageForEmptyUri(R.drawable.events).showImageOnFail(R.drawable.events).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public int getCount() {
        ArrayList<Res_Events> arrayList = this.mData;
        return (arrayList != null) ? arrayList.size() : 0;
    }

    public ArrayList<Res_Events> getData() {
        return this.mData;
    }
    public Object getItem(int paramInt) {
        return null;
    }
    public Res_Events getItemAtPosition(int paramInt) {
        return this.mData.get(paramInt);
    }
    public long getItemId(int paramInt) {
        return 0L;
    }
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        ViewHolder viewHolder;
        if (paramView == null) {
            paramView = this.mInflater.inflate(R.layout.events_category, paramViewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.thumb = (ImageView)paramView.findViewById(R.id.image);
            viewHolder.title = (TextView)paramView.findViewById(R.id.txtName);
            paramView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)paramView.getTag();
        }
        Integer event_id = Integer.valueOf(((Res_Events)this.mData.get(paramInt)).getId());
        viewHolder.title.setText(((Res_Events)this.mData.get(paramInt)).getEventname());
        this.imageLoader.displayImage(((Res_Events)this.mData.get(paramInt)).getEventfeatured_image(), viewHolder.thumb, this.options, null);
        viewHolder.thumb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Bundle args = new Bundle();
               args.putInt("event_id",event_id);
               Navigation.findNavController((AppCompatActivity)  mContext, R.id.nav_host_fragment).navigate(R.id.nav_fourtune_events_details,args);
            }
        });
        return paramView;
    }

    public void removeData() {
        this.mData.clear();
        notifyDataSetChanged();
    }

    public void setType(int paramInt) {
        this.mType = paramInt;
    }
    public void setTypeAds(int paramInt) {}
    public void updateData(ArrayList<Res_Events> paramArrayList) {
        this.mData.addAll(paramArrayList);
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView thumb;
        TextView title;
    }
}
