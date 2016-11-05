package in.ac.iiitd.madhur15030.mc_a4;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;



/**
 * Created by Madhur on 03/11/16.
 */

public class ToDoRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ToDo> toDos;
    private OnItemClickListener mListener;

    public ToDoRecordAdapter(ArrayList<ToDo> tds, OnItemClickListener listener) {
        toDos=tds;
        mListener = listener;
    }

    /**
     * Interface for receiving click events from cells.
     */
    public interface OnItemClickListener {
        void onClickToDo(View view, int position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(toDos.size()==0)
            return;
        final ToDoItemRecordViewHolder itemHolder = (ToDoItemRecordViewHolder) holder;
        ((ToDoItemRecordViewHolder) holder).titleTV.setText((toDos.get(position)).getTitle());
        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickToDo(view, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return toDos.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_todo, parent, false);
        return new ToDoItemRecordViewHolder(v);
    }

    public void updateData(ArrayList<ToDo> stRecords) {
        try {
            toDos.clear();
            toDos.addAll(stRecords);
            notifyDataSetChanged();

        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    public ToDo getItem(int position) {
        try {
            return toDos.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

}
