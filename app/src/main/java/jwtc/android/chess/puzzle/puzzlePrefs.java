package jwtc.android.chess.puzzle;

import android.os.Bundle;

import com.coreictconsultancy.fourtune.R;

import jwtc.android.chess.MyPreferenceActivity;


public class puzzlePrefs extends MyPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.puzzleprefs);

    }
}

