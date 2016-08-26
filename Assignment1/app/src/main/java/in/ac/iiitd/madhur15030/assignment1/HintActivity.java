package in.ac.iiitd.madhur15030.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HintActivity extends AppCompatActivity {

    public TextView hintTV;
    public Button revealHintButton;
    private static final String STATE_HINT_SHOWN = "hintShown";
    private boolean hintShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hintTV = (TextView) findViewById(R.id.hintTV);
        revealHintButton = (Button)findViewById(R.id.revealHintButton);

        Intent intent = getIntent();
        hintShown = intent.getBooleanExtra(STATE_HINT_SHOWN, false);

        if(savedInstanceState!=null) {
            hintShown=savedInstanceState.getBoolean(STATE_HINT_SHOWN);
        }
        if(hintShown) {
            showHint();
        }

    }
    public void showHint() {
        hintTV.setText(getString(R.string.hint_text));
        hintShown=true;
        Intent intent = new Intent();
        intent.putExtra(STATE_HINT_SHOWN, true);
        setResult(RESULT_OK, intent);
        revealHintButton.setEnabled(false);
    }
    public void revealHintTapped(View view) {
        if(!hintShown) {
            showHint();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_HINT_SHOWN, hintShown);
        super.onSaveInstanceState(outState);
    }

}
