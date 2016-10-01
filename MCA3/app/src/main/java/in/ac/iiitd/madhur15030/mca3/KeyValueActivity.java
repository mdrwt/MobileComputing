package in.ac.iiitd.madhur15030.mca3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class KeyValueActivity extends AppCompatActivity {

    public EditText et1;
    public EditText et2;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String KEY_VALUE_KEY1="keyValueKey1";
    private static final String KEY_VALUE_KEY2="keyValueKey2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_value);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et1 = (EditText)findViewById(R.id.etv1);
        et2 = (EditText)findViewById(R.id.etv2);
        sharedPref = KeyValueActivity.this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        String value1 = sharedPref.getString(KEY_VALUE_KEY1, null);
        String value2 = sharedPref.getString(KEY_VALUE_KEY2, null);
        if(value1!=null) et1.setText(value1);
        if(value2!=null) et2.setText(value2);
    }

    public void editvalue1Tapped(View view) {
        et1.setEnabled(true);
    }
    public void editvalue2Tapped(View view) {
        et2.setEnabled(true);
    }

    public void saveKeyValueTapped(View view) {
        String str1 = et1.getText().toString();
        String str2 = et2.getText().toString();
        if(str1!=null) editor.putString(KEY_VALUE_KEY1, str1);
        if(str2!=null) editor.putString(KEY_VALUE_KEY2, str2);
        editor.commit();

    }
}
