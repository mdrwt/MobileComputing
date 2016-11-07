package in.ac.iiitd.mt14033.passwordmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditPasswordActivity extends AppCompatActivity {

    final String TAG = "mt14033.EditPass";
    private EditText id;
    private EditText user_id;
    private EditText url;
    private EditText password;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        final EditText id = (EditText) findViewById(R.id.id1);
        final EditText user_id = (EditText) findViewById(R.id.user_id);
        final EditText url = (EditText) findViewById(R.id.url);
        final EditText password = (EditText) findViewById(R.id.password);
        final Button button = (Button) findViewById(R.id.save_button);

        final DBHelper dbh = new DBHelper(this);

        String record_id = getIntent().getExtras().getString("id");

        PasswordManager pm = new PasswordManager();

        pm = dbh.getPassword(Integer.parseInt(record_id));

        Log.d(TAG,"UserId1: "+pm.getUserId());
        Log.d(TAG,"Url1: "+pm.getUrl());
        Log.d(TAG,"Password1: "+pm.getPassword());

        id.setText(String.valueOf(pm.getID()));
        user_id.setText(pm.getUserId());
        url.setText(pm.getUrl());
        password.setText(pm.getPassword());

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PasswordManager pm1 = new PasswordManager();

                pm1.setID(Integer.parseInt(id.getText().toString()));
                pm1.setUserId(user_id.getText().toString());
                pm1.setUrl(url.getText().toString());
                pm1.setPassword(password.getText().toString());

                dbh.updatePassword(pm1);

                Log.d(TAG,"UserId2: "+pm1.getUserId());
                Log.d(TAG,"Url2: "+pm1.getUrl());
                Log.d(TAG,"Password2: "+pm1.getPassword());

                Toast toast = Toast.makeText(getApplicationContext(), "Record Updated in sqlitedb", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
