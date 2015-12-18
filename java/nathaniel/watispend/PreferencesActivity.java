package nathaniel.watispend;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * Created by Nathaniel on 12/15/2015.
 */
// Preferences Activity java file.
public class PreferencesActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new preferenceFragment()).commit();
    }

    public static class preferenceFragment extends PreferenceFragment {

        // We have two preferences that we save between app launches that we don't want the user to edit
        // This method hides them from the preferences menu.
        private void hiddenPreferences(){
            Preference studentNumPref = (Preference)findPreference("studentNumEncrypted");
            Preference studentPinPref = (Preference)findPreference("studentPinEncrypted");
            PreferenceScreen screen = getPreferenceScreen();
            screen.removePreference(studentNumPref);
            screen.removePreference(studentPinPref);
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            hiddenPreferences();
        }
    }
}
