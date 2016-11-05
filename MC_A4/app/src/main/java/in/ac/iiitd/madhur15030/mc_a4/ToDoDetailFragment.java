package in.ac.iiitd.madhur15030.mc_a4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.ac.iiitd.madhur15030.mc_a4.database.DBHelper;

/**
 * Created by Madhur on 04/11/16.
 */

public class ToDoDetailFragment extends Fragment {

    public static final String ARG_PAGE = "page";

    private int mPageNumber;

    public static ToDoDetailFragment create(int todoNo) {
        ToDoDetailFragment fragment = new ToDoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, todoNo);
        fragment.setArguments(args);
        return fragment;
    }
    public ToDoDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        ToDo toDo = DBHelper.getInstance().getToDoRecordWithIndex(mPageNumber, getContext());

        ((TextView)rootView.findViewById(R.id.todo_detail_title_tv)).setText(toDo.getTitle());
        ((TextView)rootView.findViewById(R.id.todo_detail_detail_tv)).setText(toDo.getDetail());

        return rootView;
    }
    public int getPageNumber() {
        return mPageNumber;
    }
}
