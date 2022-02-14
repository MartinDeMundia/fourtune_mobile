package com.coreictconsultancy.maze;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.coreictconsultancy.fourtune.R;

import java.util.ArrayList;
import java.util.List;

public class RecordsLevelsFragment extends Fragment {

	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {

	        View view = inflater.inflate(R.layout.records_levels_tab, container, false);

	        com.coreictconsultancy.maze.LevelsDB DB = new com.coreictconsultancy.maze.LevelsDB(getActivity());
			
			DB.openDataBase();
			int totalLevels = DB.getTotalLevels();
			DB.close();
			
			ListView levelsListView = (ListView) view.findViewById(R.id.recordsLevelsTab);
			List<String> levels = new ArrayList<String>();
			List<String> time = new ArrayList<String>();
			List<String> moves = new ArrayList<String>();
			DB.openDataBase();
			for(int i=1;i<=totalLevels;i++) {

					levels.add(i+"");
					time.add(DB.getTimeTaken(i));
					moves.add(DB.getMoves(i)+"");
					
			}
			
			DB.close();
			
			com.coreictconsultancy.maze.RecordsLevelsListAdapter adapter = new com.coreictconsultancy.maze.RecordsLevelsListAdapter(getActivity(),R.layout.records_list_item, levels,time,moves);
	        levelsListView.setAdapter(adapter);
	        
	        return view;
	    }
	 
	 
	 	
}