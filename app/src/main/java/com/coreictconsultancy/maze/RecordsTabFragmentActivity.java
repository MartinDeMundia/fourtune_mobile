package com.coreictconsultancy.maze;
import android.os.Bundle;
import android.view.Window;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;

import com.coreictconsultancy.fourtune.R;


public class RecordsTabFragmentActivity extends FragmentActivity {
	
	private FragmentTabHost mTabHost;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.records_fragment);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("QuickGame").setIndicator("Quick Game"),
            com.coreictconsultancy.maze.RecordsQuickGameFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("NormalGame").setIndicator("Normal Game"),
            com.coreictconsultancy.maze.RecordsLevelsFragment.class, null);
        		
    }
		
	

}
