package in.ac.iiitd.mt14033.passwordmanager.drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.iiitd.mt14033.passwordmanager.R;

/**
 * Created by Madhur on 09/11/16.
 */
public class DLUserProfileHeaderViewHolder extends RecyclerView.ViewHolder{
    final TextView fnameTV;
    final ImageView userProfileIV;
    final TextView emailTV;

    final View rootView;

    public DLUserProfileHeaderViewHolder(View itemView) {
        super(itemView);

        rootView = itemView;
        fnameTV = (TextView) itemView.findViewById(R.id.userProfileFnameTV);
        userProfileIV = (ImageView) itemView.findViewById(R.id.userProfileIV);

        emailTV = (TextView) itemView.findViewById(R.id.userProfileEmailTV);
    }
}
