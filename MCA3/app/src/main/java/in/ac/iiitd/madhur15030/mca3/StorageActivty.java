package in.ac.iiitd.madhur15030.mca3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class StorageActivty extends AppCompatActivity {

    private EditText fnameInternalEditText;
    private EditText contentInterEditText;
    private EditText fnameExternalEditText;
    private EditText contentExternalEditText;
    private CheckBox extPrivateCheckbox;

    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fnameInternalEditText = (EditText)findViewById(R.id.fnameinternalEditText);
        contentInterEditText = (EditText)findViewById(R.id.contentInternalEditText);
        extPrivateCheckbox = (CheckBox)findViewById(R.id.extPrivateCheckBox);
        fnameExternalEditText = (EditText)findViewById(R.id.fnameExternalEditText);
        contentExternalEditText = (EditText)findViewById(R.id.contentExternalEditText);

        if(savedInstanceState!=null) {
            fnameInternalEditText.setText(savedInstanceState.getString(getString(R.string.key_int_storage_fname)));
            contentInterEditText.setText(savedInstanceState.getString(getString(R.string.key_int_storage_content)));
            fnameExternalEditText.setText(savedInstanceState.getString(getString(R.string.key_key_ext_storage_fname)));
            contentExternalEditText.setText(savedInstanceState.getString(getString(R.string.key_ext_storage_content)));
            extPrivateCheckbox.setChecked(savedInstanceState.getBoolean(getString(R.string.key_ext_storage_isprivate)));
        }

    }

    public void saveToInternalStorageTapped(View view) {
        String filename = fnameInternalEditText.getText().toString();
        String str = contentInterEditText.getText().toString();
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(str.getBytes());
            outputStream.close();
            Toast.makeText(getApplicationContext(), getString(R.string.toast_file_write_success), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFromInternalStorageTapped(View view) {
        String filename = fnameInternalEditText.getText().toString();
        String filePath = getApplicationContext().getFilesDir()+"/"+filename;
        File outFile = new File(filePath);

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(outFile));
            String line=br.readLine();
            while(line!=null) {
                sb.append(line);
                sb.append("\n");
                line=br.readLine();
            }
            br.close();
            contentInterEditText.setText(sb.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.toast_file_read_from)+outFile.toString(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_file_read_error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        finally {

        }
    }

    public void saveToExternalStorageTapped(View view) {
        if(isExternalStorageWritable()) {
            Log.i(getString(R.string.key_debug), getString(R.string.act_st_perm_ext_store_writable));
        }

        boolean savePrivate = extPrivateCheckbox.isChecked();
        String fileName = fnameExternalEditText.getText().toString();
        String str = contentExternalEditText.getText().toString();


        boolean hasPermission = (ContextCompat.checkSelfPermission(StorageActivty.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        Log.i(getString(R.string.key_debug), "Permission not granted");
        if (!hasPermission) {
            ActivityCompat.requestPermissions(StorageActivty.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);

            if (ActivityCompat.shouldShowRequestPermissionRationale(StorageActivty.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(StorageActivty.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
        }

        if(savePrivate) {
            File file = new File(getApplicationContext().getExternalFilesDir(
                    Environment.DIRECTORY_DOCUMENTS), fileName);
            Log.i(getString(R.string.key_debug), file.toString());
            file.getParentFile().mkdirs();

            try
            {
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(str);
                myOutWriter.close();
                fOut.flush();
                fOut.close();
                Toast.makeText(getApplicationContext(), getString(R.string.toast_file_write_to)+file.toString(), Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                Log.e(getString(R.string.key_exception), "File write failed: " + e.toString());
            }
        }
        else {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            root.mkdirs();
            File file = new File(root.getAbsolutePath(), fileName);
            Log.i(getString(R.string.key_debug), file.toString());

            try
            {
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(str);
                myOutWriter.close();
                fOut.flush();
                fOut.close();
                Toast.makeText(getApplicationContext(), getString(R.string.toast_file_write_to)+file.toString(), Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_file_write_error), Toast.LENGTH_SHORT).show();
                Log.e(getString(R.string.key_exception), getString(R.string.toast_file_read_error) + e.toString());
            }
        }
    }
    public void getFromExternalStorageTapped(View view) {
        boolean savePrivate = extPrivateCheckbox.isChecked();
        String fileName = fnameExternalEditText.getText().toString();
        File outFile;

        if(savePrivate) {
            outFile = new File(getApplicationContext().getExternalFilesDir(
                    Environment.DIRECTORY_DOCUMENTS), fileName);
            Log.i(getString(R.string.key_debug), outFile.toString());
        }
        else {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            root.mkdirs();
            outFile = new File(root.getAbsolutePath(), fileName);
            Log.i(getString(R.string.key_debug), outFile.toString());
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(outFile));
            String line=br.readLine();
            while(line!=null) {
                sb.append(line);
                sb.append("\n");
                line=br.readLine();
            }
            br.close();
            contentExternalEditText.setText(sb.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.toast_file_read_from)+outFile.toString(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_file_read_error), Toast.LENGTH_SHORT).show();
            Log.e(getString(R.string.key_exception), getString(R.string.toast_file_read_error) + e.toString());
            e.printStackTrace();
        }
        finally {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     *
     * https://developer.android.com/training/basics/data-storage/files.html
     */
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String fnameint = fnameInternalEditText.getText().toString();
        String contentint = contentInterEditText.getText().toString();
        String fnameext = fnameExternalEditText.getText().toString();
        String contentext = contentExternalEditText.getText().toString();
        boolean isprivate = extPrivateCheckbox.isChecked();

        outState.putString(getString(R.string.key_int_storage_fname), fnameint);
        outState.putString(getString(R.string.key_int_storage_content), contentint);
        outState.putString(getString(R.string.key_key_ext_storage_fname), fnameext);
        outState.putString(getString(R.string.key_ext_storage_content), contentext);
        outState.putBoolean(getString(R.string.key_ext_storage_isprivate), isprivate);

        super.onSaveInstanceState(outState);
    }

}
