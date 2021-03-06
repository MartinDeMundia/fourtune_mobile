package com.coreictconsultancy.maze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.coreictconsultancy.fourtune.R;

import java.util.List;


public class LevelsListAdapter extends ArrayAdapter<String>{

	
	Context context;
	List<String> levels;
	com.coreictconsultancy.maze.LevelsDB DB;
	public LevelsListAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		this.context=context;
		levels = objects;
		DB = new com.coreictconsultancy.maze.LevelsDB(context);
	}
	
	private class ViewHolder {
		TextView tv;
	}
	
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		
		String level  =  getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
		final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.levels_list_item, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.levelTV);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        
        holder.tv.setText(level);
        DB.openDataBase();
		if(DB.checkCompletion(Integer.parseInt(holder.tv.getText().toString())))
		{
//			Log.d("Level completed",holder.tv.getText().toString());
			holder.tv.setBackgroundColor(Color.GREEN);
		}
		else {
//			Log.d("Level not completed",holder.tv.getText().toString());
			holder.tv.setBackgroundColor(Color.TRANSPARENT);
		}
		DB.close();
			
        holder.tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				((Activity)context).finish();
				Intent intent = new Intent(context, com.coreictconsultancy.maze.GameActivity.class);
				intent.putExtra("level", Integer.parseInt(holder.tv.getText().toString()));
				intent.putExtra("mode", 1);
				((Activity)context).startActivity(intent);
				
			}
		});
		return convertView;
	}

}