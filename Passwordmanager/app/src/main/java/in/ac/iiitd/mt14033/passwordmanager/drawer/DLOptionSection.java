package in.ac.iiitd.mt14033.passwordmanager.drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import in.ac.iiitd.mt14033.passwordmanager.R;
import in.ac.iiitd.mt14033.passwordmanager.model.DLOption;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Madhur on 09/11/16.
 */
public class DLOptionSection extends StatelessSection {
    private ArrayList<DLOption> mDataset;
    private OnItemClickListener mListener;
    private SectionedRecyclerViewAdapter sectionAdapter;
    /**
     * Interface for receiving click events from cells.
     */
    public interface OnItemClickListener {
        void onClickDLOption(View view, int position);
    }

    public DLOptionSection(OnItemClickListener listener,
                           SectionedRecyclerViewAdapter sectionAdap) {
        super(R.layout.drawer_list_header_option, R.layout.drawer_list_item_option);
        mDataset = new ArrayList<>();
        mDataset.add(new DLOption("Add Login", R.drawable.ic_add_login));
        mDataset.add(new DLOption("Generate Password", R.drawable.ic_generate));
        mDataset.add(new DLOption("Settings", R.drawable.ic_settings));
        mDataset.add(new DLOption("Logout", R.drawable.ic_logout));
        mListener = listener;
        sectionAdapter = sectionAdap;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return super.getHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        super.onBindHeaderViewHolder(holder);
    }

    @Override
    public int getContentItemsTotal() {
        return 4;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new DLOptionItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DLOptionItemViewHolder itemHolder = (DLOptionItemViewHolder) holder;
        ((DLOptionItemViewHolder) holder).optionTitleTV.setText((mDataset.get(position)).getOptionTitle());
        ((DLOptionItemViewHolder) holder).optionIV.setImageResource((mDataset.get(position)).getOptionImageResId());
        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickDLOption(view, position);
            }
        });
    }
}
