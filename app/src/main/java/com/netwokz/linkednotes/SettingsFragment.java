package com.netwokz.linkednotes;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        ListPreference mThemeList = findPreference("theme");
        assert mThemeList != null;
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                mThemeList.setValue("MODE_NIGHT_YES");
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                mThemeList.setValue("MODE_NIGHT_NO");
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                mThemeList.setValue("MODE_NIGHT_FOLLOW_SYSTEM");
                break;
        }
    }
}
