package in.ac.iiitd.mt14033.passwordmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import in.ac.iiitd.mt14033.passwordmanager.drawer.DLOptionSection;
import in.ac.iiitd.mt14033.passwordmanager.drawer.DLUserProfileSection;
import in.ac.iiitd.mt14033.passwordmanager.model.SavedPassword;
import in.ac.iiitd.mt14033.passwordmanager.model.UserProfile;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
/**
 * Created by Madhur on 09/11/16.
 */
public class ListPasswordActivity extends AppCompatActivity implements ListPasswordRecordAdapter.OnItemClickListener,
        DLOptionSection.OnItemClickListener{

    final String TAG = "mt14033.ListPass";
    String[] fromColumns = {DBHelper.KEY_ID, DBHelper.KEY_USERNAME, DBHelper.KEY_NAME, DBHelper.KEY_URL, DBHelper.KEY_PASSWORD};
    int[] toViews = {R.id.id, R.id.user_id, R.id.url, R.id.password};


    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> dAdapter;
    private static String mEmail;
    private RecyclerView mDrawerList;

    private RecyclerView savedPasswordList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<SavedPassword> savedPasswords;

    private DLOptionSection dlOptionSection;
    private DLUserProfileSection dlUserProfileSection;

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


        mDrawerList = (RecyclerView) findViewById(R.id.login_navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mEmail = getIntent().getExtras().getString(CommonContants.LOGGED_IN_USER);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        // Use sectioned listview instead of default listview
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        dlUserProfileSection = new DLUserProfileSection();
        sectionAdapter.addSection(dlUserProfileSection);
        dlOptionSection = new DLOptionSection(this, sectionAdapter);
        sectionAdapter.addSection(dlOptionSection);
        mDrawerList.setAdapter(sectionAdapter);

        setupDrawer();

        //API>21 change the status bar color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));


        String matching_login_packagename = getIntent().getExtras().getString(CommonContants.MATCHING_LOGIN_PACKAGE_NAME, null);
        if(matching_login_packagename!=null) {
            /**
             * user came here from add login dialog.
             * present user with add new login screen with details filled.
              */
            Intent intent = new Intent(this, AddLoginActivity.class);
            intent.putExtra(CommonContants.MATCHING_LOGIN_PACKAGE_NAME, matching_login_packagename);
            startActivity(intent);
        }
    }

    public void gotoLoginScreen() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<SavedPassword> savedPasswords = new ArrayList<>();
        final DBHelper dbh = new DBHelper(getApplicationContext());
        savedPasswords = dbh.getAllPasswords(mEmail);

        ListPasswordRecordAdapter adapter = (ListPasswordRecordAdapter) savedPasswordList.getAdapter();
        adapter.updateData(savedPasswords);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.password_list_menu, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search_password).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.action_search_password).setVisible(true);
//        return super.onPrepareOptionsMenu(menu);
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_password:
                break;
            default:
        }
        return true;
    }

    private void setupDrawer() {
        Toolbar apptoolbar = (Toolbar) findViewById(R.id.listpassword_toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                /*super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");*/
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                UserProfile userProfile = new UserProfile(mEmail);
                dlUserProfileSection.updateData(userProfile);
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                /*super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);*/
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.syncState();
        apptoolbar.setNavigationIcon(R.drawable.ic_drawer);
        apptoolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v(getString(R.string.VTAG), "drawer tapped");
                boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
                if(drawerOpen) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


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

    /**
     * Implement protocols for Drawer option selection
     */
    @Override
    public void onClickDLOption(View view, int position) {
        mDrawerLayout.closeDrawers();
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(ListPasswordActivity.this, AddLoginActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(ListPasswordActivity.this, AddLoginActivity.class);
                intent.putExtra(CommonContants.OPEN_GEN_PASS, true);
                startActivity(intent);
                break;
            case 2:
                break;
            case 3:
                logout();
                break;

        }

    }

    private void logout() {
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;
        sharedPref = getApplicationContext().getSharedPreferences(CommonContants.preference_file_key, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.remove(CommonContants.LOGGED_IN_USER);
        editor.apply();

        Toast.makeText(getApplicationContext(), getString(R.string.logout_message), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ListPasswordActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}
