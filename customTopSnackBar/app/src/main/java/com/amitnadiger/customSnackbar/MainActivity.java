package com.amitnadiger.customSnackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amitnadiger.customTopSnackbar.Style;
import com.amitnadiger.customTopSnackbar.TopSnackbar;

public class MainActivity extends AppCompatActivity {
    protected TopSnackbar mTopSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTopSnackBar = TopSnackbar.makeText(this,"Om Shree Ganeshaya Namah", Style.INFO);
        mTopSnackBar.show();

    }
}
