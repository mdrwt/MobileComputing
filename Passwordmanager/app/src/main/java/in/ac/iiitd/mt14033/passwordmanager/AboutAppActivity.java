package in.ac.iiitd.mt14033.passwordmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Created by veronica on 21/10/16.
 */

public class AboutAppActivity extends AppCompatActivity {

    private static final String TAG = "mt14033.PM.aboutappAct";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        //retrieve the action toolbar (status bar)
        Toolbar apptoolbar = (Toolbar) findViewById(R.id.AboutApp_toolbar);
        setSupportActionBar(apptoolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.aboutapp_toolbar_title));
    }
}
