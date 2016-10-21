package in.ac.iiitd.mt14033.passwordmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class GetPasswordDialogActivity extends AppCompatActivity {

    static final String TAG="GetPasswordDialog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password_dialog);
    }

    public void fillButtonTapped(View view) {
        Log.v(TAG, "fillButtonTapped");
        finish();
    }

    public void cancelButtonTapped(View view) {
        Log.v(TAG, "cancelButtonTapped");
        finish();
    }

}
