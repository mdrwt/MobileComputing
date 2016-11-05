package in.ac.iiitd.madhur15030.mc_a4;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Madhur on 03/11/16.
 */

public class ToDoItemRecordViewHolder extends RecyclerView.ViewHolder {
    final View rootView;
    final TextView titleTV;

    public ToDoItemRecordViewHolder(View itemView) {
        super(itemView);

        rootView = itemView;
        titleTV = (TextView) itemView.findViewById(R.id.titleTV);

        Log.i("DEBUG", titleTV.toString());
    }
}
