package in.ac.iiitd.mt14033.passwordmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Created by veronica on 20/10/16.
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "mt14033.PM.ForgotPasswordAct";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //retrieve the action toolbar (status bar)
        Toolbar apptoolbar = (Toolbar) findViewById(R.id.forgotPassword_toolbar);
        setSupportActionBar(apptoolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.forgotPassword_toolbar_title));

    }
}
