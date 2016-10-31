package in.ac.iiitd.mt14033.passwordmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class AddLoginActivity extends AppCompatActivity {

    final private String TAG = "mt14033.PM.MainAct";
    final private String PASSWORD_MANAGER_PREF = "PASSWORD_MANAGER";
    // Database Name
    private static final String DATABASE_NAME = "PasswordManager.sqlitedb";

    private EditText passwordLength;
    private EditText passwordUrl;
    private TextView generatedPassword;
    private Button generatePasswordButton;
    private Button savePasswordButton;
    private Button savePreferredPasswordLengthButton;

    private int get_preferred_password_length() {
        Log.d(TAG, "in RestorePreferencePasswordLength");

        SharedPreferences prefs = getSharedPreferences(PASSWORD_MANAGER_PREF, MODE_PRIVATE);
        int preferredPasswordLength = prefs.getInt("Preferred_Password_Length", 6);

        Log.d(TAG, "Preferred_Password_Length: "+String.valueOf(preferredPasswordLength));
        return preferredPasswordLength;
    }

    private String generate_password(int length) {
        char[] chars1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%*^".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        String random_string;
        Random random1 = new Random();
        for (int i = 0; i < length; i++)
        {
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }
        random_string = sb1.toString();
        return random_string;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Inside OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_login);

        final EditText passwordLength = (EditText) findViewById(R.id.password_length);
        final EditText passwordUrl = (EditText) findViewById(R.id.add_login_url);
        final TextView generatedPassword = (TextView) findViewById(R.id.generated_password);
        final ImageButton generatePasswordButton = (ImageButton) findViewById(R.id.generate_password_button);
        final Button savePasswordButton = (Button) findViewById(R.id.save_generated_password_button);
        final Button saveLengthPreferenceButton = (Button) findViewById(R.id.save_preferred_password_length_button);


        generatePasswordButton.setEnabled(true);
        saveLengthPreferenceButton.setEnabled(true);

        savePasswordButton.setEnabled(false);

        int preferred_password_length = get_preferred_password_length();
        passwordLength.setText(Integer.toString(preferred_password_length));
        Toast toast = Toast.makeText(getApplicationContext(), "Preferred Password Length loaded from SharedPreferences ", Toast.LENGTH_SHORT);
        toast.show();

        final DBHelper dbh = new DBHelper(this);

        saveLengthPreferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "in saveLengthPreferenceButton");

                int length = Integer.parseInt(passwordLength.getText().toString());
                Log.d(TAG, "Length:"+ String.valueOf(length));

                SharedPreferences.Editor editor = getSharedPreferences(PASSWORD_MANAGER_PREF, MODE_PRIVATE).edit();
                editor.putInt("Preferred_Password_Length", length);
                editor.apply();

                Toast toast = Toast.makeText(getApplicationContext(), "Preferred Password Length saved in SharedPreferences ", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        generatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d(TAG, "in generatePasswordButton");
//
//                int length = Integer.parseInt(passwordLength.getText().toString());
//                Log.d(TAG, "Length:"+ String.valueOf(length));
//                String password = generate_password(length);
//                Log.d(TAG, "Password:"+ password);
//
//                generatedPassword.setText(password);
//                savePasswordButton.setEnabled(true);
            }
        });

        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "in savePasswordButton");

                String url = passwordUrl.getText().toString();
                if (!URLUtil.isValidUrl(url)) {
                    Log.d(TAG, "invalid URL");
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid URL.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                String password = generatedPassword.getText().toString();
                Log.d(TAG, "Url:"+ url);
                Log.d(TAG, "Password:"+ password);

                String userid = getIntent().getExtras().getString("mEmail");

                // code to add in sqlite db
                //dbh.getWritableDatabase();
                PasswordManager pm = new PasswordManager();

                pm.setPassword(password);
                pm.setUrl(url);
                pm.setUserId(userid);

                dbh.addPassword(pm);
                Toast toast = Toast.makeText(getApplicationContext(), "Password saved in SqliteDB ", Toast.LENGTH_SHORT);
                toast.show();
                savePasswordButton.setEnabled(false);
            }

        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // code to deal with result data
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "Inside OnSaveInstanceState");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "Inside OnRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);

        // code to restore data
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

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "Inside OnRestart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Inside OnStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Inside OnDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Inside OnStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Inside OnPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Inside OnResume");
    }

}
