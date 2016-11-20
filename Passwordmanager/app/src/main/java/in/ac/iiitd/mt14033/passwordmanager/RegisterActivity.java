package in.ac.iiitd.mt14033.passwordmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.ac.iiitd.mt14033.passwordmanager.model.MasterUser;

public class RegisterActivity extends AppCompatActivity {

    final private String TAG = "mt14033.PM.MainAct";
    final private String PASSWORD_MANAGER_PREF = "PASSWORD_MANAGER";
    // Database Name
    private static final String DATABASE_NAME = "SavedPassword.sqlitedb";
    private EditText master_email;
    private EditText masterPassword;
    private EditText hintForPassword;
    private EditText reEnterPassword;
    private static DBHelper dbh;
//    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Inside OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        master_email = (EditText) findViewById(R.id.master_email);
        masterPassword = (EditText) findViewById(R.id.master_pswd);
        reEnterPassword = (EditText) findViewById(R.id.confirm_pswd);
        hintForPassword = (EditText) findViewById(R.id.pswd_hint);
        final Button createAccountButton = (Button) findViewById(R.id.create_account_btn);
        dbh=new DBHelper(this);

        createAccountButton.setEnabled(true);

        //retrieve the action toolbar (status bar)
        Toolbar apptoolbar = (Toolbar) findViewById(R.id.registerAccount_toolbar);
        setSupportActionBar(apptoolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.createAccount_toolbar_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
    }

    public void registerMasterUserTapped(View view) {
        Log.v(TAG, "registerMasterUserTapped");
        String masteremail = master_email.getText().toString();
        if(!readEmail(masteremail)) return;
        if(!(masterPassword.getText().toString().equals(reEnterPassword.getText().toString()))) {
            Toast.makeText(getApplicationContext(), "Passwords donot match", Toast.LENGTH_SHORT).show();
        }
        else {
            MasterUser masterUser = new MasterUser(
                    masteremail,
                    masterPassword.getText().toString(),
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
    public Boolean readEmail(String email)
    {
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            master_email.setError(getString(R.string.error_field_required));
            Context context = getApplicationContext();
            CharSequence text = "Please fill Email..!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return false;
        } else if (!isEmailValid(email)) {
            master_email.setError(getString(R.string.error_invalid_email));
            return false;
        } else{
            return true;
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
}
