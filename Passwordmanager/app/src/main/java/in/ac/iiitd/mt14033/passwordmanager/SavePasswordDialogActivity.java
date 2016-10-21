package in.ac.iiitd.mt14033.passwordmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.View;

public class SavePasswordDialogActivity extends AppCompatActivity {

    static final String TAG="SavePasswordDialog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
    }
    public void saveButtonTapped(View view) {
        Log.v(TAG, "saveButtonTapped");

        finish();
    }
    public void neverButtonTapped(View view) {

        Log.v(TAG, "neverButtonTapped");
    }
    public void laterButtonTapped(View view) {

        Log.v(TAG, "neverButtonTapped");
    }

}
