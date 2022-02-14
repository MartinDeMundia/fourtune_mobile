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

public class RecordsQuickGameFragment extends Fragment {

	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        
	        View view = inflater.inflate(R.layout.records_quickgame_tab, container, false);
	        
	        LevelsDB DB = new LevelsDB(getActivity());
			
			DB.openDataBase();
			
			ListView quickGameStatsLV = (ListView) view.findViewById(R.id.recordsQGListView);
			
			List<com.coreictconsultancy.maze.GameStats> stats = new ArrayList<com.coreictconsultancy.maze.GameStats>();
			
			DB.openDataBase();
			
			for(int i = 1 ; i <= 5 ; i++) {

				com.coreictconsultancy.maze.GameStats stat = DB.getQuickGameStats(i);
//				Log.d("stats level "+i,stat.toString());
				stats.add(stat);
					
			}
			
			DB.close();
			
			com.coreictconsultancy.maze.RecordsQuickGameListAdapter adapter = new com.coreictconsultancy.maze.RecordsQuickGameListAdapter(getActivity(),R.layout.records_quickgame_listitem, stats);
	        quickGameStatsLV.setAdapter(adapter);
	        
	        
	        return view;
	    }
}