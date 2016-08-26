package in.ac.iiitd.madhur15030.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 1000;

    private static final String STATE_RANDOM_INT = "randomInt";
    private static final String STATE_HINT_SHOWN = "hintShown";
    private static final String STATE_CHEAT_SHOWN = "cheatShown";
    private static final String DEBUG_TAG = "Assignment1";

    private boolean hintShownMain=false;
    private boolean cheatShownMain=false;

    public TextView primeNumberTV;
    private int randomInt;

    private boolean isPrime=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        primeNumberTV = (TextView)findViewById(R.id.prime_number);

        if(savedInstanceState!=null) {
            randomInt = savedInstanceState.getInt(STATE_RANDOM_INT);
            hintShownMain=savedInstanceState.getBoolean(STATE_HINT_SHOWN);
            cheatShownMain=savedInstanceState.getBoolean(STATE_CHEAT_SHOWN);
            primeNumberTV.setText(""+randomInt);
            isPrime = checkPrime(randomInt);
        }
        else {
            randomInt = setRandomInteger();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_RANDOM_INT, randomInt);
        outState.putBoolean(STATE_HINT_SHOWN, hintShownMain);
        outState.putBoolean(STATE_CHEAT_SHOWN, cheatShownMain);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if(resultCode==RESULT_OK) {
                boolean hintShown = data.getBooleanExtra(STATE_HINT_SHOWN, false);
                if(hintShown) {
                    if(!hintShownMain)
                        Toast.makeText(getApplicationContext(), getString(R.string.hint_used_toast_message), Toast.LENGTH_SHORT).show();
                    hintShownMain=hintShown;
                }
            }
        }
        else if(requestCode==2) {
            if(resultCode==RESULT_OK) {
                boolean cheatShown = data.getBooleanExtra(STATE_CHEAT_SHOWN, false);
                if(cheatShown) {
                    if(!cheatShownMain)
                        Toast.makeText(getApplicationContext(), getString(R.string.cheat_used_toast_message), Toast.LENGTH_SHORT).show();
                    cheatShownMain=cheatShown;
                }
            }
        }
    }

    public static int getRandomInt(int min, int max) {
        Random rand=new Random();

        int randInt = rand.nextInt((max-min)+1)+min;

        return randInt;

    }

    public void isPrimeTapped(View view) {
        if(isPrime) {
            Toast.makeText(getApplicationContext(), getString(R.string.result_correct), Toast.LENGTH_SHORT).show();
            Log.d(DEBUG_TAG, getString(R.string.result_correct));
            randomInt = setRandomInteger();
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.result_incorrect), Toast.LENGTH_SHORT).show();
            Log.d(DEBUG_TAG, getString(R.string.result_incorrect));
        }
    }

    public void isNotPrimeTapped(View view) {
        if(!isPrime) {
            Toast.makeText(getApplicationContext(), getString(R.string.result_correct), Toast.LENGTH_SHORT).show();
            Log.d(DEBUG_TAG, getString(R.string.result_correct));
            randomInt = setRandomInteger();
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.result_incorrect), Toast.LENGTH_SHORT).show();
            Log.d(DEBUG_TAG, getString(R.string.result_incorrect));
        }
    }

    public void showHintTapped(View view) {
        Intent intent = new Intent(MainActivity.this, HintActivity.class);
        intent.putExtra(STATE_HINT_SHOWN, hintShownMain);
        MainActivity.this.startActivityForResult(intent, 1);
    }



    public void showCheatTapped(View view) {
        Intent intent = new Intent(MainActivity.this, CheatActivity.class);
        intent.putExtra(STATE_CHEAT_SHOWN, cheatShownMain);
        intent.putExtra(STATE_RANDOM_INT, randomInt);
        MainActivity.this.startActivityForResult(intent, 2);
    }

    public void nextTapped(View view) {
        randomInt = setRandomInteger();
    }

    // From stackoverflow.com
    private boolean checkPrime(int n) {
        for(int i=2; 2*i<n; i++) {
            if(n%i==0) {
                return false;
            }
        }
        return true;
    }

    private int setRandomInteger() {
        int randInt = getRandomInt(MIN_LIMIT, MAX_LIMIT);
        primeNumberTV.setText(""+randInt);
        isPrime = checkPrime(randInt);
//        hintShownMain=false; //TODO: Confirm if hintshown is tobe reset on new number generation.
        cheatShownMain=false;
        return randInt;
    }


}
