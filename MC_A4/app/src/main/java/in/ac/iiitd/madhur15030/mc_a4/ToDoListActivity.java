package in.ac.iiitd.madhur15030.mc_a4;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import in.ac.iiitd.madhur15030.mc_a4.database.DBHelper;

public class ToDoListActivity extends AppCompatActivity implements AddToDoDialog.AddToDoDialogListner, ToDoRecordAdapter.OnItemClickListener{

    private RecyclerView toDoList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<ToDo> toDos;
    private final String TODO_INDEX="toindex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        toDoList = (RecyclerView)findViewById(R.id.todoList);
        mLayoutManager = new LinearLayoutManager(this);
        toDoList.setLayoutManager(mLayoutManager);
        toDos = new ArrayList<>();
        mAdapter = new ToDoRecordAdapter(toDos, this);
        toDoList.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }
    public void reloadList() {
        ArrayList<ToDo> studentRecords = DBHelper.getInstance().getAllToDoRecord(getApplicationContext());
        ToDoRecordAdapter adapter = (ToDoRecordAdapter) toDoList.getAdapter();
        adapter.updateData(studentRecords);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do_list_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add_todo).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_todo:
                AddToDoDialog newAddTodoDialogFragment = new AddToDoDialog();
                newAddTodoDialogFragment.show(getSupportFragmentManager(), "missiles");
                break;
            default:
        }
        return true;
    }

    /**
     * Implementation for listeners of AddToDoDialog
     */
    public void onAddToDoClick(DialogFragment dialog) {
        Dialog dlg = dialog.getDialog();
        EditText title_et = (EditText)dlg.findViewById(R.id.dialog_title_tv);
        EditText detail_et = (EditText)dlg.findViewById(R.id.dialog_detail_tv);
        DBHelper.getInstance().insertToDoRecord(title_et.getText().toString(), detail_et.getText().toString(),
                getApplicationContext());
        reloadList();

    }
    public void onCancelClick(DialogFragment dialog) {

    }

    /**
     * Implemetaton for listeners of ToDoAdapter
     */
    @Override
    public void onClickToDo(View view, int position) {
        Intent intent = new Intent(ToDoListActivity.this, ScreenSlideActivity.class);
        intent.putExtra(TODO_INDEX, position);
        startActivity(intent);
    }
}
