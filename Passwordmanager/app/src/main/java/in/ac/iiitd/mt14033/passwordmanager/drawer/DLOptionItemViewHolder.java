package in.ac.iiitd.mt14033.passwordmanager.drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.iiitd.mt14033.passwordmanager.R;


/**
 * Created by Madhur on 04/06/16.
 */
public class DLOptionItemViewHolder  extends RecyclerView.ViewHolder{
    final TextView optionTitleTV;
    final ImageView optionIV;
    final View rootView;

    public DLOptionItemViewHolder(View itemView) {
        super(itemView);

        rootView = itemView;
        optionTitleTV = (TextView) itemView.findViewById(R.id.optionTitleTV);
        optionIV = (ImageView) itemView.findViewById(R.id.optionIV);

    }
}
