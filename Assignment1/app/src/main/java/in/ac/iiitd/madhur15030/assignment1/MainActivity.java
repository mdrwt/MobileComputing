package in.ac.iiitd.madhur15030.assignment1;

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

    private static final String DEBUG_TAG = "Assignment1";

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
        super.onSaveInstanceState(outState);
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

    public void nextTapped(View view) {
        randomInt = setRandomInteger();
    }

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
        return randInt;
    }


}
