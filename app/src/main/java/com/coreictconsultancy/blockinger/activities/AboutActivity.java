/*
 * Copyright 2013 Simon Willeke
 * contact: hamstercount@hotmail.com
 */



package com.coreictconsultancy.blockinger.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import com.coreictconsultancy.fourtune.R;


public class AboutActivity extends PreferenceActivity {

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.about_menu);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	        ActionBar actionBar = getActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true);
	    }
		

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        Preference pref = findPreference("pref_license");
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				String url = getResources().getString(R.string.license_url);
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				return true;
			}
		});
        
        pref = findPreference("pref_license_music");
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				String url = getResources().getString(R.string.music_url);
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				return true;
			}
		});
        
        pref = findPreference("pref_version");
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				String url = getResources().getString(R.string.repository_url);
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				return true;
			}
		});
        
        pref = findPreference("pref_author");
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
		        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.pref_author_url)});
		        emailIntent.setType("plain/text");
		        startActivity(Intent.createChooser(emailIntent, "Send email..."));
				return true;
			}
		});
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
	
}
