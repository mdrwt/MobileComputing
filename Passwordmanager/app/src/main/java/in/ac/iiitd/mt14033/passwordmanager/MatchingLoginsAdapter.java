package in.ac.iiitd.mt14033.passwordmanager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.ac.iiitd.mt14033.passwordmanager.model.MatchingLogin;

/**
 * Created by madhur rawat on 10/25/16.
 */


public class MatchingLoginsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MatchingLogin> matchingLogins;
    private OnItemClickListener mListener;

    public MatchingLoginsAdapter(ArrayList<MatchingLogin> matchLogins, OnItemClickListener listener) {
        matchingLogins=matchLogins;
        mListener = listener;
    }

    /**
     * Interface for receiving click events from cells.
     */
    public interface OnItemClickListener {
        void onClickMatchingLogin(View view, int position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(matchingLogins.size()==0)
            return;
        final MatchingLoginItemViewHolder itemHolder = (MatchingLoginItemViewHolder) holder;

        ((MatchingLoginItemViewHolder) holder).nameTV.setText((matchingLogins.get(position)).getUsername());
        ((MatchingLoginItemViewHolder) holder).packagename.setText((matchingLogins.get(position)).getPackagename());

        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickMatchingLogin(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return matchingLogins.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("DEBUG", "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_matching_login, parent, false);
        return new MatchingLoginItemViewHolder(v);
    }

    public void updateData(ArrayList<MatchingLogin> stRecords) {
        try {
            matchingLogins.clear();
            matchingLogins.addAll(stRecords);
            notifyDataSetChanged();

        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    public MatchingLogin getItem(int position) {
        try {
            return matchingLogins.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
