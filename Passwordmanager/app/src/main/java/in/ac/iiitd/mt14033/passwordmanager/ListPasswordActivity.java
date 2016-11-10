package in.ac.iiitd.mt14033.passwordmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import in.ac.iiitd.mt14033.passwordmanager.model.SavedPassword;

public class ListPasswordActivity extends AppCompatActivity implements ListPasswordRecordAdapter.OnItemClickListener{

    final String TAG = "mt14033.ListPass";
    String[] fromColumns = {DBHelper.KEY_ID, DBHelper.KEY_USERNAME, DBHelper.KEY_NAME, DBHelper.KEY_URL, DBHelper.KEY_PASSWORD};
    int[] toViews = {R.id.id, R.id.user_id, R.id.url, R.id.password};

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> dAdapter;
    private static String mEmail;
    private ListView mDrawerList;

    private RecyclerView savedPasswordList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<SavedPassword> savedPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_password);

        //retrieve the action toolbar (status bar)
        Toolbar apptoolbar = (Toolbar) findViewById(R.id.listpassword_toolbar);
        setSupportActionBar(apptoolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.listpassword_toolbar_title));


        savedPasswordList = (RecyclerView)findViewById(R.id.saved_password_list);
        mLayoutManager = new LinearLayoutManager(this);
        savedPasswordList.setLayoutManager(mLayoutManager);
        savedPasswords = new ArrayList<>();
        mAdapter = new ListPasswordRecordAdapter(savedPasswords, this);
        savedPasswordList.setAdapter(mAdapter);


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


    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<SavedPassword> savedPasswords = new ArrayList<>();
        final DBHelper dbh = new DBHelper(getApplicationContext());
        savedPasswords = dbh.getAllPasswords();

        ListPasswordRecordAdapter adapter = (ListPasswordRecordAdapter) savedPasswordList.getAdapter();
        adapter.updateData(savedPasswords);


    }

    private void addDrawerItems() {
        String[] osArray = { getResources().getString(R.string.navdrawer_item0) };
        dAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(dAdapter);
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

    /**
     * Implement protocol methods of Saved Password Adapter
     */
    @Override
    public void onClickPassword(View view, int position) {

    }

    @Override
    public void onLongClickToDo(View view, int position) {

    }
}
