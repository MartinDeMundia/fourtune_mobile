package jwtc.android.chess;

import android.os.Bundle;

import com.coreictconsultancy.fourtune.R;

public class mainPrefs extends MyPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.mainprefs);

    }
}

