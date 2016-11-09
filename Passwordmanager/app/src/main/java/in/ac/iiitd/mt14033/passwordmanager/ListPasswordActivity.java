package in.ac.iiitd.mt14033.passwordmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import in.ac.iiitd.mt14033.passwordmanager.model.SavedPassword;

public class ListPasswordActivity extends AppCompatActivity {

    final String TAG = "mt14033.ListPass";
    String[] fromColumns = {DBHelper.KEY_ID, DBHelper.KEY_USERNAME, DBHelper.KEY_NAME, DBHelper.KEY_URL, DBHelper.KEY_PASSWORD};
    int[] toViews = {R.id.id, R.id.user_id, R.id.url, R.id.password};

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private static String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_password);

        //retrieve the action toolbar (status bar)
        Toolbar apptoolbar = (Toolbar) findViewById(R.id.listpassword_toolbar);
        setSupportActionBar(apptoolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.listpassword_toolbar_title));

        mDrawerList = (ListView)findViewById(R.id.login_navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mEmail = getIntent().getExtras().getString("mEmail");
        addDrawerItems();
        setupDrawer();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);


        final DBHelper dbh = new DBHelper(this);

        Log.d(TAG, String.valueOf(dbh.getPasswordsCount()));
        Cursor cursor = dbh.getAllPasswords();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, fromColumns, toViews, 0);
        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "in LongClick Event, Id: "+String.valueOf(id));
                SavedPassword pm = new SavedPassword();
                pm.setID(Integer.parseInt(String.valueOf(id)));
                dbh.deletePassword(pm);
                Toast toast = Toast.makeText(getApplicationContext(), "Entry deleted from SqliteDB. Reload view to see updated password list.", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "in OnClick Event, Id: "+String.valueOf(id));
                Intent in = new Intent(ListPasswordActivity.this, EditPasswordActivity.class);
                in.putExtra("id", String.valueOf(id));
                startActivity(in);
            }
        });
    }

    private void addDrawerItems() {
        String[] osArray = { getResources().getString(R.string.navdrawer_item0) };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                /*super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()*/
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                /*super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()*/
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position){
        // Handle Nav Options
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(ListPasswordActivity.this, AddLoginActivity.class);
                intent.putExtra("mEmail", mEmail);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Inside OnBackPressed");
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setTitle("Why :.....(")
                .setMessage("Are you sure you want to leave?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
