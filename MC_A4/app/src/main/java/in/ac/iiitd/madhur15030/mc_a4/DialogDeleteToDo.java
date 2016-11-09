package in.ac.iiitd.madhur15030.mc_a4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Madhur on 09/11/16.
 */

public class DialogDeleteToDo extends DialogFragment {

    DialogDeleteToDoDialogListner mListener;
    public interface DialogDeleteToDoDialogListner {
        public void onDeleteClick(DialogFragment dialog);
        public void onCancelClick(DialogFragment dialog);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_deltodo_message)
                .setTitle(R.string.dialog_deltodo_title);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDeleteClick(DialogDeleteToDo.this);
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCancelClick(DialogDeleteToDo.this);
                    }
                });
        return builder.create();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogDeleteToDo.DialogDeleteToDoDialogListner)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
