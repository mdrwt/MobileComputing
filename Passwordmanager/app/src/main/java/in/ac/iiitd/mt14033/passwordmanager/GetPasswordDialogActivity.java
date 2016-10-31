package in.ac.iiitd.mt14033.passwordmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class GetPasswordDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password_dialog);
    }

    public void addLoginButtonTapped(View view) {
        Log.v(getString(R.string.VTAG) , "addLoginButtonTapped");
    }

    public void cancelButtonTapped(View view) {
        Log.v(getString(R.string.VTAG), "cancelButtonTapped");
        finish();
    }

}
