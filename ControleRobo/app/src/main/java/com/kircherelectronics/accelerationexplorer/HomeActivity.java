package com.kircherelectronics.accelerationexplorer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize home layout
        setContentView(R.layout.layout_home);

        // Initialize bluetooth button
        initButtonBluetooth();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initButtonBluetooth() {
        
        Button button = (Button) this.findViewById(R.id.button_bluetooth_mode);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,
                        BluetoothActivity.class);

                startActivity(intent);
            }
        });

    }
}
