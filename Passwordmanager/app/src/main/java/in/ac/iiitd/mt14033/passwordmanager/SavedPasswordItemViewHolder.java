package in.ac.iiitd.mt14033.passwordmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Madhur on 09/11/16.
 */

public class SavedPasswordItemViewHolder  extends RecyclerView.ViewHolder{
    final View rootView;
    final TextView nameTV;
    final TextView packagenameTV;

    public SavedPasswordItemViewHolder(View itemView) {
        super(itemView);

        rootView = itemView;
        nameTV = (TextView) itemView.findViewById(R.id.saved_pass_name_tv);
        packagenameTV = (TextView) itemView.findViewById(R.id.saved_pass_package_tv);

    }
}
