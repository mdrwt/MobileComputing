package in.ac.iiitd.mt14033.passwordmanager.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

import in.ac.iiitd.mt14033.passwordmanager.R;

import static android.content.Context.MODE_PRIVATE;

public class DialogGeneratePassword extends DialogFragment {

    TextView generatedTV;
    TextView passwordLenTV;
    final private String PASSWORD_MANAGER_PREF = "PASSWORD_MANAGER";

    DialogGeneratePasswordListner mListener;
    public interface DialogGeneratePasswordListner {
        void onSaveToDoClick(DialogFragment dialog);
        void onCancelClick(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_generate_password, null);
        builder.setView(view);
        generatedTV = (TextView) view.findViewById(R.id.dialog_genpass_generated_password);
        passwordLenTV = (TextView) view.findViewById(R.id.dialog_genpass_passlen);
        final SeekBar seekbar = (SeekBar) view.findViewById(R.id.dialog_genpass_seek_plen);
        final ImageButton regenerate_btn = (ImageButton) view.findViewById(R.id.dialog_genpass_regen_btn);
        regenerate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final SeekBar seekbar = (SeekBar) view.findViewById(R.id.dialog_genpass_seek_plen);
                int progress = seekbar.getProgress();
                generatedTV.setText(generate_password(progress));

            }
        });

        int saved_len = getActivity().getSharedPreferences(PASSWORD_MANAGER_PREF, MODE_PRIVATE).getInt(getString(R.string.PREFFERED_PASSWORD_LENGTH), 8);
        seekbar.setProgress(saved_len);
        generatedTV.setText(generate_password(saved_len));
        String plen_text = getString(R.string.dialog_genpass_hint_passlen)+String.valueOf(saved_len);
        passwordLenTV.setText(plen_text);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.v(getString(R.string.VTAG), String.valueOf(progress)+" seekvalue");

                generatedTV.setText(generate_password(progress));
                passwordLenTV.setText(getString(R.string.dialog_genpass_hint_passlen)+String.valueOf(progress));

                int length = Integer.parseInt(String.valueOf(progress));

                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PASSWORD_MANAGER_PREF, MODE_PRIVATE).edit();
                editor.putInt(getString(R.string.PREFFERED_PASSWORD_LENGTH), length);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onSaveToDoClick(DialogGeneratePassword.this);
            }
        })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCancelClick(DialogGeneratePassword.this);
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
            mListener = (DialogGeneratePasswordListner)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private String generate_password(int length) {
        char[] chars1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%*^".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        String random_string;
        Random random1 = new Random();
        for (int i = 0; i < length; i++)
        {
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }
        random_string = sb1.toString();
        return random_string;
    }
}
