package in.ac.iiitd.mt14033.passwordmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import in.ac.iiitd.mt14033.passwordmanager.model.SavedPassword;

public class MainActivity extends AppCompatActivity {

    final private String TAG = "mt14033.PM.MainAct";
    final private String PASSWORD_MANAGER_PREF = "PASSWORD_MANAGER";
    // Database Name
    private static final String DATABASE_NAME = "SavedPassword.sqlitedb";

    private EditText passwordLength;
    private EditText passwordUrl;
    private TextView generatedPassword;
    private Button generatePasswordButton;
    private Button savePasswordButton;
    private Button savePreferredPasswordLengthButton;
    private Button backupButton;
    private Button passwordListButton;

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
        setContentView(R.layout.activity_main);

        final EditText passwordLength = (EditText) findViewById(R.id.password_length);
        final EditText passwordUrl = (EditText) findViewById(R.id.password_url);
        final TextView generatedPassword = (TextView) findViewById(R.id.generated_password);
        final Button generatePasswordButton = (Button) findViewById(R.id.generate_password_button);
        final Button savePasswordButton = (Button) findViewById(R.id.save_generated_password_button);
        final Button saveLengthPreferenceButton = (Button) findViewById(R.id.save_preferred_password_length_button);
        final Button backupButton = (Button) findViewById(R.id.backup_button);
        //final Button passwordListButton = (Button) findViewById(R.id.password_list_button);

        generatePasswordButton.setEnabled(true);
        saveLengthPreferenceButton.setEnabled(true);
        backupButton.setEnabled(true);
        savePasswordButton.setEnabled(false);

        //retrieve the action toolbar (status bar)
        Toolbar apptoolbar = (Toolbar) findViewById(R.id.generatePassword_toolbar);
        setSupportActionBar(apptoolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_login_toolbar_title));

        int preferred_password_length = get_preferred_password_length();
        passwordLength.setText(Integer.toString(preferred_password_length));
        Toast toast = Toast.makeText(getApplicationContext(), "Preferred Password Length loaded from SharedPreferences ", Toast.LENGTH_SHORT);
        toast.show();

        final DBHelper dbh = new DBHelper(this);

        /*passwordListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "in passwordListButton");
                Intent list_password = new Intent(getApplicationContext(),ListPasswordActivity.class);
                startActivity(list_password);
            }
        });*/
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
                Log.d(TAG, "in generatePasswordButton");

                int length = Integer.parseInt(passwordLength.getText().toString());
                Log.d(TAG, "Length:"+ String.valueOf(length));
                String password = generate_password(length);
                Log.d(TAG, "Password:"+ password);

                generatedPassword.setText(password);
                savePasswordButton.setEnabled(true);
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
                SavedPassword pm = new SavedPassword();

                pm.setPassword(password);
                pm.setUrl(url);
                pm.setUserId(userid);

                dbh.addPassword(pm);
                Toast toast = Toast.makeText(getApplicationContext(), "Password saved in SqliteDB ", Toast.LENGTH_SHORT);
                toast.show();
                savePasswordButton.setEnabled(false);
            }

        });

        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "in backupButton");
                final String inFileName = "/data/data/in.ac.iiitd.mt14033.passwordmanager/databases/"+DATABASE_NAME;
                File dbFile = new File(inFileName);
                FileInputStream fis;
                OutputStream output;
                try {
                    fis = new FileInputStream(dbFile);
                } catch (FileNotFoundException e) {
                    Log.d(TAG,"DB File not found during backup.");
                    Toast toast = Toast.makeText(getApplicationContext(), "DB file not found in location", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                String outFileName = Environment.getExternalStorageDirectory()+"/"+DATABASE_NAME;
                Log.d(TAG, "DB outFileName: "+outFileName);

                // Open the empty db as the output stream
                try {
                    output = new FileOutputStream(outFileName);
                } catch (FileNotFoundException e) {
                    Log.d(TAG,"Cannot create db file for backup at external location."+e.toString());
                    Toast toast = Toast.makeText(getApplicationContext(), "Cannot create db file for backup at external location."+e.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                try {
                    while ((length = fis.read(buffer))>0){
                        output.write(buffer, 0, length);
                    }

                    // Close the streams
                    output.flush();
                    output.close();
                    fis.close();
                    Toast toast = Toast.makeText(getApplicationContext(), "Backup taken successfully.", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (java.io.IOException e) {
                    Log.d(TAG,"Error: "+e.toString());
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: "+e.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                backupButton.setEnabled(true);
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

        // code to save data
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
