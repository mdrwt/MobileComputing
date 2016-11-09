package in.ac.iiitd.mt14033.passwordmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.iiitd.mt14033.passwordmanager.model.MasterUser;

public class RegisterActivity extends AppCompatActivity {

    final private String TAG = "mt14033.PM.MainAct";
    final private String PASSWORD_MANAGER_PREF = "PASSWORD_MANAGER";
    // Database Name
    private static final String DATABASE_NAME = "SavedPassword.sqlitedb";
    private static TextView masterPassword;
    private static TextView hintForPassword;
    private static TextView reEnterPassword;
    private static DBHelper dbh;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Inside OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //retrieve the action toolbar (status bar)
        Toolbar apptoolbar = (Toolbar) findViewById(R.id.registerAccount_toolbar);
        setSupportActionBar(apptoolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.createAccount_toolbar_title));

        final TextView mEmailView = (TextView)findViewById(R.id.txt_email);
        email = getIntent().getExtras().getString("mEmail");
        mEmailView.setText(email);
        masterPassword = (TextView) findViewById(R.id.master_pswd);
        reEnterPassword = (TextView) findViewById(R.id.confirm_pswd);
        hintForPassword = (TextView) findViewById(R.id.pswd_hint);
        final Button createAccountButton = (Button) findViewById(R.id.create_account_btn);
        dbh=new DBHelper(this);

        createAccountButton.setEnabled(true);
    }

    public void registerMasterUserTapped(View view) {
        Log.v(TAG, "registerMasterUserTapped");
        if(!(masterPassword.getText().toString().equals(reEnterPassword.getText().toString()))) {
            Toast.makeText(getApplicationContext(), "Passwords donot match", Toast.LENGTH_SHORT).show();
        }
        else {
            MasterUser masterUser = new MasterUser(email, masterPassword.getText().toString(),
                    hintForPassword.getText().toString());
            boolean resp = dbh.addMasterPassword(masterUser);

            if(resp) {
                Toast.makeText(getApplicationContext(), "Master user registered successfully", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "An error occured", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
