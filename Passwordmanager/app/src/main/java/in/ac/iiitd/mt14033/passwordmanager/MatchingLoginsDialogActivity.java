package in.ac.iiitd.mt14033.passwordmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import in.ac.iiitd.mt14033.passwordmanager.model.MatchingLogin;


public class MatchingLoginsDialogActivity extends AppCompatActivity implements MatchingLoginsAdapter.OnItemClickListener{

    private RecyclerView matchingLoginsList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DBHelper dbh;

    private ArrayList<MatchingLogin> studentRecords;
    private String packagename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_logins_dialog);

        matchingLoginsList = (RecyclerView)findViewById(R.id.matching_login_list);
        mLayoutManager = new LinearLayoutManager(this);
        matchingLoginsList.setLayoutManager(mLayoutManager);
        studentRecords = new ArrayList<>();
        mAdapter = new MatchingLoginsAdapter(studentRecords, this);
        matchingLoginsList.setAdapter(mAdapter);
        packagename = getIntent().getExtras().getString(getString(R.string.matching_login_package_name));
        dbh = new DBHelper(this);
        if(packagename.length()==0) {
            Log.v(getString(R.string.VTAG), "Package name is null");
        }
        this.setFinishOnTouchOutside(false);
    }

    public void addLoginButtonTapped(View view) {
        Log.v(getString(R.string.VTAG), "addLoginButtonTapped");
        finish();
    }

    public void cancelButtonTapped(View view) {
        Log.v(getString(R.string.VTAG), "cancelButtonTapped");
        finish();
    }

    @Override
    public void onClickMatchingLogin(View view, int position) {
        MatchingLogin matchingLogin = ((MatchingLoginsAdapter)mAdapter).getItem(position);
        MyAccessibilityService accessibilityService = MyAccessibilityService.getInstance();
        if(accessibilityService!=null) {
            accessibilityService.onSelectMatchingLogin(matchingLogin);
        }
        else {
            Log.v(getString(R.string.VTAG), "Error. Accessibility Service not avaialble");
        }
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<MatchingLogin> matchingLogins = new ArrayList<>();
        if(packagename.length()!=0) {
            matchingLogins = dbh.getPasswordsForPackagename(packagename);
            Log.v(getString(R.string.VTAG), "Found matches: "+matchingLogins.size());
        }
        MatchingLoginsAdapter adapter = (MatchingLoginsAdapter)matchingLoginsList.getAdapter();
        adapter.updateData(matchingLogins);


    }

}
