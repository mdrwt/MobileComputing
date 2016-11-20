package in.ac.iiitd.mt14033.passwordmanager.drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import in.ac.iiitd.mt14033.passwordmanager.R;
import in.ac.iiitd.mt14033.passwordmanager.model.UserProfile;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Madhur on 09/11/16.
 */
public class DLUserProfileSection extends StatelessSection {

    private DLUserProfileHeaderViewHolder dlUserProfileHeaderViewHolder;

    public DLUserProfileSection() {
        super(R.layout.drawer_list_header_user_profile, R.layout.drawer_list_header_user_profile);
    }



    @Override
    public int getContentItemsTotal() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        dlUserProfileHeaderViewHolder = new DLUserProfileHeaderViewHolder(view);
        return dlUserProfileHeaderViewHolder;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindHeaderViewHolder(holder);
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return null;
    }
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {

    }

    public void updateData(UserProfile userProfile) {
        if(userProfile==null)
            return;
        dlUserProfileHeaderViewHolder.emailTV.setText(userProfile.getEmail());

//        CircleDrawable circle = new CircleDrawable(userProfile.pp_bitmap,true);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//            dlUserProfileHeaderViewHolder.userProfileIV.setBackground(circle);
//        else
//            dlUserProfileHeaderViewHolder.userProfileIV.setBackgroundDrawable(circle);
    }
}
