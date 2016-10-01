package in.ac.iiitd.madhur15030.mca3;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class StudentRecordItemViewHolder extends RecyclerView.ViewHolder{

    final View rootView;
    final TextView nameTV;
    final TextView rollNo;
    final TextView currsemTV;

    public StudentRecordItemViewHolder(View itemView) {
        super(itemView);

        rootView = itemView;
        nameTV = (TextView) itemView.findViewById(R.id.nameTV);
        rollNo = (TextView) itemView.findViewById(R.id.rollnoTV);
        currsemTV = (TextView) itemView.findViewById(R.id.currsemTV);
        Log.i("DEBUG", rollNo.toString());
    }
}
