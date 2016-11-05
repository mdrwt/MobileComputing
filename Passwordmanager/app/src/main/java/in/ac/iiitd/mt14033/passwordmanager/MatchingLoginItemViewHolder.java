package in.ac.iiitd.mt14033.passwordmanager;

/**
 * Created by madhur rawat on 10/25/16.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MatchingLoginItemViewHolder extends RecyclerView.ViewHolder{
    final View rootView;
    final TextView nameTV;
    final TextView packagename;

    public MatchingLoginItemViewHolder(View itemView) {
        super(itemView);

        rootView = itemView;
        nameTV = (TextView) itemView.findViewById(R.id.nameTV);
        packagename = (TextView) itemView.findViewById(R.id.packagenameTV);

    }

}
