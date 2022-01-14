package org.o7planning.kulkagra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.*;
import android.content.Intent;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                Intent i = new Intent(HomeActivity.this, GameActivity.class);
                startActivity(i);

                finish();
            }
        }, 5000);

    }

}
