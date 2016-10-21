package in.ac.iiitd.madhur15030.testproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public EditText username_ET;
    public EditText password_ET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_ET = (EditText)findViewById(R.id.email_et);
        password_ET = (EditText)findViewById(R.id.password_et);


    }

    public void loginTapped(View view) {
//        Toast.makeText(getApplicationContext(), password_ET.getText(), Toast.LENGTH_SHORT).show();
        Log.v("LoginActivity", "Login tapped");
    }

}
