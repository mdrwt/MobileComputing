package in.ac.iiitd.madhur15030.mca3;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.ac.iiitd.madhur15030.mca3.model.StudentRecord;

/**
 * Created by Madhur on 30/09/16.
 */
public class StudentRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<StudentRecord> studentRecords;
    private OnItemClickListener mListener;

    public StudentRecordAdapter(ArrayList<StudentRecord> stRecords, OnItemClickListener listener) {
        studentRecords=stRecords;
        mListener = listener;
        Log.i("DEBUG", "came here.................................3."+mListener);
    }

    /**
     * Interface for receiving click events from cells.
     */
    public interface OnItemClickListener {
        void onClickStudentRecord(View view, int position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(studentRecords.size()==0)
            return;
        final StudentRecordItemViewHolder itemHolder = (StudentRecordItemViewHolder) holder;

        ((StudentRecordItemViewHolder) holder).rollNo.setText((studentRecords.get(position)).getRollno());
        ((StudentRecordItemViewHolder) holder).nameTV.setText((studentRecords.get(position)).getName());
        ((StudentRecordItemViewHolder) holder).currsemTV.setText("Current Sem: "+(studentRecords.get(position)).getCurrsem());

        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickStudentRecord(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentRecords.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("DEBUG", "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_student_record, parent, false);
        return new StudentRecordItemViewHolder(v);
    }

    public void updateData(ArrayList<StudentRecord> stRecords) {
        try {
            studentRecords.clear();
            studentRecords.addAll(stRecords);
            notifyDataSetChanged();

        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    public StudentRecord getItem(int position) {
        try {
            return studentRecords.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
