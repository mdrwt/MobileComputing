package in.ac.iiitd.madhur15030.mca3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void keyValueTapped(View view) {
        Log.i(getString(R.string.key_debug), "keyValueTapped");
        Intent intent = new Intent(MainActivity.this, KeyValueActivity.class);
        MainActivity.this.startActivity(intent);
    }
    public void storageTapped(View view) {
        Log.i(getString(R.string.key_debug), "storageTapped");
        Intent intent = new Intent(MainActivity.this, StorageActivty.class);
        MainActivity.this.startActivity(intent);
    }
    public void sqliteTapped(View view) {
        Log.i(getString(R.string.key_debug), "sqliteTapped");
        Intent intent = new Intent(MainActivity.this, SqliteActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
