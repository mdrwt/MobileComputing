package in.ac.iiitd.mt14033.passwordmanager.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.ac.iiitd.mt14033.passwordmanager.CommonContants;
import in.ac.iiitd.mt14033.passwordmanager.DBHelper;
import in.ac.iiitd.mt14033.passwordmanager.MatchingLoginsAdapter;
import in.ac.iiitd.mt14033.passwordmanager.R;
import in.ac.iiitd.mt14033.passwordmanager.model.MatchingLogin;

public class DialogMatchingLogin extends DialogFragment implements MatchingLoginsAdapter.OnItemClickListener{

    private RecyclerView matchingLoginsList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DBHelper dbh;
    private ArrayList<MatchingLogin> matchingLogins;
    private String requesting_package;

    DialogMatchingLogin.DialogMatchingLoginListner mListener;
    public interface DialogMatchingLoginListner {
        void onDialogMatchingAddLoginClick(DialogFragment dialog);
        void onDialogMatchingLoginClick(DialogFragment dialog, MatchingLogin matchingLogin);
        void onDialogMatchingCancelClick();
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_matching_login, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.add_login_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogMatchingAddLoginClick(DialogMatchingLogin.this);
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogMatchingCancelClick();
                    }
                });
        final AlertDialog mDialog = builder.create();
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });

        matchingLoginsList = (RecyclerView)view.findViewById(R.id.matching_login_list);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        matchingLoginsList.setLayoutManager(mLayoutManager);
        matchingLogins = new ArrayList<>();
        mAdapter = new MatchingLoginsAdapter(matchingLogins, this);
        matchingLoginsList.setAdapter(mAdapter);

        requesting_package = getArguments().getString(CommonContants.MATCHING_LOGIN_PACKAGE_NAME);
        dbh = new DBHelper(getActivity().getApplicationContext());
        if(requesting_package.length()==0) {
            Log.v(getString(R.string.VTAG), "Package name is null");
        }

        ArrayList<MatchingLogin> matchingLogins = new ArrayList<>();
        if(requesting_package.length()!=0) {
            matchingLogins = dbh.getPasswordsForPackagename(requesting_package);
            Log.v(getString(R.string.VTAG), "Found matches: "+matchingLogins.size());
        }
        MatchingLoginsAdapter adapter = (MatchingLoginsAdapter)matchingLoginsList.getAdapter();
        adapter.updateData(matchingLogins);



        return mDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogMatchingLogin.DialogMatchingLoginListner)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onClickMatchingLogin(View view, int position) {
        MatchingLogin matchingLogin = ((MatchingLoginsAdapter)mAdapter).getItem(position);
        if(mListener!=null) {
            mListener.onDialogMatchingLoginClick(DialogMatchingLogin.this, matchingLogin);
        }
        else {

        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onDialogMatchingCancelClick();
    }
}
