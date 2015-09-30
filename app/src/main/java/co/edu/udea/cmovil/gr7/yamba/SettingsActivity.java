package co.edu.udea.cmovil.gr7.yamba;

import android.os.Bundle;

/**
 * Created by davigofr on 2015/09/30.
 */
public class SettingsActivity extends SubActivity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Check if this activity was created before
        if (savedInstanceState == null) {
            // Create a fragment
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }
    }
}
