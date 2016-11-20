package in.ac.iiitd.mt14033.passwordmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.iiitd.mt14033.passwordmanager.dialog.DialogGeneratePassword;
import in.ac.iiitd.mt14033.passwordmanager.model.SavedPassword;
/**
 * Created by Madhur on 09/11/16.
 */
public class AddLoginActivity extends AppCompatActivity implements DialogGeneratePassword.DialogGeneratePasswordListner{

    private EditText urlET;
    private EditText nameET;
    private EditText passwordET;
    private EditText usernameET;
    private Button saveLoginButton;
    private boolean hasUnsavedChanges=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_login);

        nameET = (EditText) findViewById(R.id.add_login_name_et);
        urlET = (EditText) findViewById(R.id.add_login_url_et);
        passwordET = (EditText) findViewById(R.id.add_login_password_et);
        saveLoginButton = (Button) findViewById(R.id.add_login_save_login_btn);
        usernameET = (EditText) findViewById(R.id.add_login_username_et);
        String packagename = getIntent().getStringExtra(CommonContants.MATCHING_LOGIN_PACKAGE_NAME);
        urlET.setText(packagename);
        nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasUnsavedChanges=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        urlET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasUnsavedChanges=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasUnsavedChanges=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        usernameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasUnsavedChanges=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boolean open_gen_pass = getIntent().getBooleanExtra(CommonContants.OPEN_GEN_PASS, false);

        final DBHelper dbh = new DBHelper(this);

        saveLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CommonContants.preference_file_key, Context.MODE_PRIVATE);
                String logged_user = sharedPref.getString(CommonContants.LOGGED_IN_USER, null);
                String url = urlET.getText().toString();
//                if (!URLUtil.isValidUrl(url)) {
//                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.ERROR_INVALID_URL), Toast.LENGTH_SHORT);
//                    toast.show();
//                    return;
//                }
                String password = passwordET.getText().toString();
                String username = usernameET.getText().toString();
                String name = nameET.getText().toString();
                SavedPassword pm = new SavedPassword();
                pm.setMaster_email(logged_user);
                pm.setPassword(password);
                pm.setName(name);
                pm.setUrl(url);
                pm.setUsername(username);
                boolean res = dbh.addPassword(pm);
                if (res) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Password saved in SqliteDB ", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.ERROR_GENERIC1), Toast.LENGTH_LONG).show();
                }
                saveLoginButton.setEnabled(false);
            }

        });

        //retrieve the action toolbar (status bar)
        Toolbar apptoolbar = (Toolbar) findViewById(R.id.generatePassword_toolbar);
        setSupportActionBar(apptoolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_login_toolbar_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        if(open_gen_pass) {
            generatePasswordTapped(null);
        }
    }

    @Override
    public void onBackPressed() {
        /**
         * Show confirmation dialog only when user made some possible edit in some fields.
         */
        if(hasUnsavedChanges) {
            new AlertDialog.Builder(this)
                    .setTitle("Exit?")
                    .setMessage("Are you sure you want to leave?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Log.v(getString(R.string.VTAG), "Home button pressed");
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void generatePasswordTapped(View view) {
        DialogGeneratePassword dialogGeneratePassword = new DialogGeneratePassword();
        dialogGeneratePassword.show(getSupportFragmentManager(), CommonContants.DIALOG_GENERATE_PASSWORD);
    }

    /**
     * Generate password dialog callback implementation
     */
    public void onSaveToDoClick(DialogFragment dialog) {
        Dialog dlg = dialog.getDialog();
        TextView dlg_generatedTV = (TextView) dlg.findViewById(R.id.dialog_genpass_generated_password);
        String generatedPassword = dlg_generatedTV.getText().toString();
        passwordET.setText(generatedPassword);
    }
    public void onCancelClick(DialogFragment dialog) {

    }

}
