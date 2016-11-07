package in.ac.iiitd.mt14033.passwordmanager;

import android.support.v7.app.AppCompatActivity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;

/**
 * Created by veronica on 20/10/16.
 */

public class SettingsActivity extends ListActivity{

    private static final String TAG = "mt14033.PM.settingsAct";

    private ListView settingsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsListView = (ListView) findViewById(android.R.id.list);
        String values[] = new String[]{
                getResources().getString(R.string.settings_listViewItem1),
                //getResources().getString(R.string.settings_listviewItem2)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        settingsListView.setAdapter(adapter);

        // ListView Item Click Listener
        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                if (position == 0) {
                    Log.d(TAG, "clicked Advanced list view");
                    /*Intent advancedSettingsIntent = new Intent(SettingsActivity.this, AdvancedSetttingsActivity.class);
                    startActivity(advancedSettingsIntent);*/
                }

                /*if (position == 1) {
                    Log.d(TAG, "clicked About PasswordWallet list view");

                    // ListView Clicked item index
                    int itemPosition = position;

                    // ListView Clicked item value
                    String itemValue = (String) settingsListView.getItemAtPosition(position);

                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();
                }*/
            }
        });
    }
}
