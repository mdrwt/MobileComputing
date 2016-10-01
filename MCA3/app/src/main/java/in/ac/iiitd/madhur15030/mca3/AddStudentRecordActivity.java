package in.ac.iiitd.madhur15030.mca3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import in.ac.iiitd.madhur15030.mca3.database.DBHelper;
import in.ac.iiitd.madhur15030.mca3.model.StudentRecord;

public class AddStudentRecordActivity extends AppCompatActivity {

    private EditText rollnoEditTV;
    private EditText nameEditTV;
    private EditText currsemEditTV;

    private StudentRecord studentRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rollnoEditTV=  (EditText)findViewById(R.id.rollnoEditTV);
        nameEditTV = (EditText)findViewById(R.id.nameEditTV);
        currsemEditTV = (EditText)findViewById(R.id.currsemEditTV);

        Intent intent = getIntent();
        studentRecord = (StudentRecord) intent.getSerializableExtra(getString(R.string.key_student_record));

        if(studentRecord!=null) {
            rollnoEditTV.setText(studentRecord.getRollno());
            nameEditTV.setText(studentRecord.getName());
            currsemEditTV.setText(studentRecord.getCurrsem());
        }
        if(savedInstanceState!=null) {
            StudentRecord savedRecord = (StudentRecord )savedInstanceState.getSerializable(getString(R.string.key_saved_student_record));
            rollnoEditTV.setText(savedRecord.getRollno());
            nameEditTV.setText(savedRecord.getName());
            currsemEditTV.setText(savedRecord.getCurrsem());
        }
    }

    public void addUpdateRecordTapped(View view) {
        String rollno = rollnoEditTV.getText().toString();
        String name = nameEditTV.getText().toString();
        String currsem = currsemEditTV.getText().toString();

        long resid = DBHelper.getInstance().InsertStudentRecord(rollno, name, currsem, getApplicationContext());
        if(-1!=resid)
            Toast.makeText(getApplicationContext(), getString(R.string.toast_record_added), Toast.LENGTH_SHORT).show();
    }

    public void deleteRecordTapped(View view) {
        String rollno = rollnoEditTV.getText().toString();
        int resid = DBHelper.getInstance().deleteStudentRecord(rollno, getApplicationContext());
        Log.i(getString(R.string.key_debug), resid+" is resid");
        if(resid!=0) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_record_deleted), Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_record_notfound), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String rollno = rollnoEditTV.getText().toString();
        String name = nameEditTV.getText().toString();
        String currsem = currsemEditTV.getText().toString();
        StudentRecord savedRecord = new StudentRecord(rollno, name, currsem);
        outState.putSerializable(getString(R.string.key_saved_student_record), savedRecord);
        super.onSaveInstanceState(outState);
    }

}
