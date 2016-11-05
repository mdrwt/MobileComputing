package in.ac.iiitd.mt14033.passwordmanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddLoginActivity extends AppCompatActivity implements DialogGeneratePassword.DialogGeneratePasswordListner{

    private EditText urlET;
    private EditText passwordET;
    private EditText usernameET;
    private Button saveLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_login);

        urlET = (EditText) findViewById(R.id.add_login_url_et);
        passwordET = (EditText) findViewById(R.id.add_login_password_et);
        saveLoginButton = (Button) findViewById(R.id.add_login_save_login_btn);
        usernameET = (EditText) findViewById(R.id.add_login_username_et);
        String packagename = getIntent().getExtras().getString(getString(R.string.matching_login_package_name));
        urlET.setText(packagename);

        final DBHelper dbh = new DBHelper(this);

        saveLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = urlET.getText().toString();
//                if (!URLUtil.isValidUrl(url)) {
//                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.ERROR_INVALID_URL), Toast.LENGTH_SHORT);
//                    toast.show();
//                    return;
//                }
                String password = passwordET.getText().toString();
                String username = usernameET.getText().toString();
                PasswordManager pm = new PasswordManager();
                pm.setPassword(password);
                pm.setUrl(url);
                pm.setUserId(username);
                dbh.addPassword(pm);
                Toast toast = Toast.makeText(getApplicationContext(), "Password saved in SqliteDB ", Toast.LENGTH_SHORT);
                toast.show();
                saveLoginButton.setEnabled(false);
            }

        });



    }

    @Override
    public void onBackPressed() {
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

    public void generatePasswordTapped(View view) {
        DialogGeneratePassword dialogGeneratePassword = new DialogGeneratePassword();
        dialogGeneratePassword.show(getSupportFragmentManager(), "generate_password");
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
