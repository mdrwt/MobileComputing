package in.ac.iiitd.madhur15030.mc_a5;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private static final String URL = "https://www.iiitd.ac.in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            final TextView tv = (TextView) findViewById(R.id.data_tv);
            tv.setText(savedInstanceState.getString("KEY_DATA"), null);
        }
    }

    public void getDataTapped(View view) {
        PostRequestTask postRequestTask = new PostRequestTask();
        postRequestTask.delegate = this;
        postRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL);

    }

    @Override
    public void handleResponse(String resp) {
        final TextView tv = (TextView) findViewById(R.id.data_tv);
        tv.setText(resp);
    }

    private class PostRequestTask extends AsyncTask<String, Void, String> {
        public AsyncResponse delegate = null;

        @Override
        protected void onPostExecute(String s) {
            delegate.handleResponse(s);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            if (android.os.Debug.isDebuggerConnected()) // To enable debugging breakpoints in background thread.
                android.os.Debug.waitForDebugger();
            String resp = null;
            try {
                resp = getData(URL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resp;
        }
    }

    private String getData(String murl) throws IOException {

        Document doc = Jsoup.connect(murl).get();

        String title = doc.title();
        Log.v("TAG", doc.text());
        return title;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        final TextView tv = (TextView) findViewById(R.id.data_tv);
        String data = tv.getText().toString();
        outState.putString("KEY_DATA", data);
    }
}
