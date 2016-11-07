package in.ac.iiitd.mt14033.passwordmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CreateAccountActivity extends AppCompatActivity {

    final private String FILENAME = "MasterPass.dat";
    final private String TAG = "mt14033.PM.NewAccount";

    // UI references.
    private TextView mEmailView;
    String email;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        // Set up the login form.

        ArrayList<String> datax = new ArrayList<String>();

        try {
            FileInputStream fIn = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader buffreader = new BufferedReader(isr);

            String readString = buffreader.readLine();
            while (readString != null) {
                datax.add(readString);
                readString = buffreader.readLine();
            }
            isr.close();
        } catch (IOException e) {
            Log.d(TAG, "Error in reading from file. " + e.getMessage());
        }

        for (String credential : datax) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(email)) {
                // Account already exists: Email already registered
                Log.d(TAG, "Email already registered.");
            }
        }

        Button next = (Button) findViewById(R.id.button_save);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailView = (TextView) findViewById(R.id.etxt_text);
                email = mEmailView.getText().toString();
                mEmailView.setError(null);
                if(readEmail() == true) {
                    Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                    intent.putExtra("mEmail", email);
                    startActivity(intent);
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public Boolean readEmail()
    {
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            Context context = getApplicationContext();
            CharSequence text = "Please fill Email..!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return false;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            return false;
        } else{
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CreateAccount Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
