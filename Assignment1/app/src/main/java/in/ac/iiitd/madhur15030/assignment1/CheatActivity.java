package in.ac.iiitd.madhur15030.assignment1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    public TextView cheatTV;
    public Button revealCheatButton;

    private static final String STATE_CHEAT_SHOWN = "cheatShown";
    private static final String STATE_RANDOM_INT = "randomInt";
    private boolean cheatShown;
    private int randomInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cheatTV = (TextView) findViewById(R.id.cheatTV);
        revealCheatButton = (Button)findViewById(R.id.revealCheatButton);

        Intent intent = getIntent();
        cheatShown = intent.getBooleanExtra(STATE_CHEAT_SHOWN, false);
        randomInt = intent.getIntExtra(STATE_RANDOM_INT, -1);

        if(savedInstanceState!=null) {
            randomInt = savedInstanceState.getInt(STATE_RANDOM_INT);
            cheatShown=savedInstanceState.getBoolean(STATE_CHEAT_SHOWN); //TODO: Check if resetting cheatshown here is correct
        }
        if(cheatShown) {
            showCheat();
        }
        else {
            String msg = getString(R.string.cheat_text_noshown1)+" "+randomInt+" "+getString(R.string.cheat_text_noshown2);
            cheatTV.setText(msg);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_CHEAT_SHOWN, cheatShown);
        outState.putInt(STATE_RANDOM_INT, randomInt);
        super.onSaveInstanceState(outState);
    }

    public void showCheat() {
        boolean isPrime = checkPrime(randomInt);
        if(isPrime) {
            cheatTV.setText(String.valueOf(randomInt)+" "+getString(R.string.cheat_answer_is_prime));
        }
        else {
            cheatTV.setText(String.valueOf(randomInt)+" "+getString(R.string.cheat_answer_is_not_prime));
        }
        cheatShown=true;
        Intent intent = new Intent();
        intent.putExtra(STATE_CHEAT_SHOWN, true);
        revealCheatButton.setEnabled(false);
        revealCheatButton.setTextColor(Color.parseColor("#80bebebe"));
        setResult(RESULT_OK, intent);
    }

    public void revealCheatTapped(View view) {
        if(!cheatShown) {
            showCheat();

        }
    }

    private boolean checkPrime(int n) {
        for(int i=2; 2*i<n; i++) {
            if(n%i==0) {
                return false;
            }
        }
        return true;
    }

}
