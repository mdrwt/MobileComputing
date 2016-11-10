package in.ac.iiitd.mt14033.passwordmanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.ac.iiitd.mt14033.passwordmanager.model.SavedPassword;

/**
 * Created by Madhur on 09/11/16.
 */

public class ListPasswordRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SavedPassword> savedPasswords;
    private OnItemClickListener mListener;

    public ListPasswordRecordAdapter(ArrayList<SavedPassword> saveds, OnItemClickListener listener) {
        savedPasswords=saveds;
        mListener = listener;
    }

    /**
     * Interface for receiving click events from cells.
     */
    public interface OnItemClickListener {
        void onClickPassword(View view, int position);
        void onLongClickToDo(View view, int position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(savedPasswords.size()==0)
            return;
        final SavedPasswordItemViewHolder itemHolder = (SavedPasswordItemViewHolder) holder;
        ((SavedPasswordItemViewHolder) holder).nameTV.setText((savedPasswords.get(position)).getName());
        ((SavedPasswordItemViewHolder) holder).packagenameTV.setText((savedPasswords.get(position)).getUrl());
        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickPassword(view, position);
            }
        });
        itemHolder.rootView.setLongClickable(true);
        itemHolder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onLongClickToDo(v, position);
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return savedPasswords.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_saved_password, parent, false);
        return new SavedPasswordItemViewHolder(v);
    }

    public void updateData(ArrayList<SavedPassword> stRecords) {
        try {
            savedPasswords.clear();
            savedPasswords.addAll(stRecords);
            notifyDataSetChanged();

        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    public SavedPassword getItem(int position) {
        try {
            return savedPasswords.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
