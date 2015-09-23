package com.example.diego.animatedcustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    MyLoadingView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = (MyLoadingView) findViewById(R.id.myloading);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.set) {
            myView.setData(random());
            return true;
        }

        if (id == R.id.reload) {
            myView.setLoading();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public float random() {
        return (float) Math.random();
    }
}
