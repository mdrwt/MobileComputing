package in.ac.iiitd.mt14033.passwordmanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ListPassword extends AppCompatActivity {

    final String TAG = "mt14033.ListPass";
    String[] fromColumns = {DBHelper.KEY_ID, DBHelper.KEY_USERID, DBHelper.KEY_URL, DBHelper.KEY_PASSWORD};
    int[] toViews = {R.id.id, R.id.user_id, R.id.url, R.id.password};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_password);

        final DBHelper dbh = new DBHelper(this);

        Log.d(TAG, String.valueOf(dbh.getPasswordsCount()));
        Cursor cursor = dbh.getAllPasswords();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, fromColumns, toViews, 0);
        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "in LongClick Event, Id: "+String.valueOf(id));
                PasswordManager pm = new PasswordManager();
                pm._id = Integer.parseInt(String.valueOf(id));
                dbh.deletePassword(pm);
                Toast toast = Toast.makeText(getApplicationContext(), "Entry deleted from SqliteDB. Reload view to see updated password list.", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "in OnClick Event, Id: "+String.valueOf(id));
                Intent in = new Intent(ListPassword.this, EditPasswordActivity.class);
                in.putExtra("id", String.valueOf(id));
                startActivity(in);
            }
        });
    }
}
