package in.ac.iiitd.madhur15030.mca3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import in.ac.iiitd.madhur15030.mca3.database.DBHelper;
import in.ac.iiitd.madhur15030.mca3.model.StudentRecord;

public class SqliteActivity extends AppCompatActivity implements StudentRecordAdapter.OnItemClickListener{

    private RecyclerView studentRecordList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<StudentRecord> studentRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        studentRecordList = (RecyclerView)findViewById(R.id.studentRecordList);
        mLayoutManager = new LinearLayoutManager(this);
        studentRecordList.setLayoutManager(mLayoutManager);
        studentRecords = new ArrayList<>();
        mAdapter = new StudentRecordAdapter(studentRecords, this);
        studentRecordList.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_add_record:
                Intent intent = new Intent(SqliteActivity.this, AddStudentRecordActivity.class);
                SqliteActivity.this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClickStudentRecord(View view, int position) {
        StudentRecord record = ((StudentRecordAdapter)mAdapter).getItem(position);
        Intent intent = new Intent(SqliteActivity.this, AddStudentRecordActivity.class);
        intent.putExtra(getString(R.string.key_student_record), record);
        SqliteActivity.this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<StudentRecord> studentRecords = getStudentRecords();
        StudentRecordAdapter adapter = (StudentRecordAdapter) studentRecordList.getAdapter();
        adapter.updateData(studentRecords);
    }

    public ArrayList<StudentRecord> getStudentRecords() {
        return DBHelper.getInstance().getAllStudentRecord(getApplicationContext());
    }

}
