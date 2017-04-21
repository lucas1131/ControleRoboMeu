package kircherelectronics.com.controlerobomestre;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomeActivity extends Activity implements Runnable {

    private Client client;
    private TextView response;
    private EditText editTextAddress, editTextPort;

    public Button toggleButton;
    public HomeActivity self;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Button buttonConnect, buttonClear;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        editTextAddress = (EditText) findViewById(R.id.addressEditText);
        // editTextPort = (EditText) findViewById(R.id.portEditText);
        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttonClear = (Button) findViewById(R.id.clearButton);
        toggleButton = (Button) findViewById(R.id.toggleTiraTampa);
        response = (TextView) findViewById(R.id.responseTextView);

        toggleButton.setActivated(false);
        self = this;
        buttonConnect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                client = new Client(editTextAddress.getText().toString(), response);
                toggleButton.setActivated(true);
                client.execute();
                Thread thread = new Thread(self);
                thread.start();
            }
        });

        buttonClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                response.setText("");
            }
        });

        toggleButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                client.clientOut.println("toggle");
            }
        });
    }

    @Override
    public void run() {
        while(true){

            if(client == null || client.errorFlag)
                toggleButton.setActivated(false);
            else toggleButton.setActivated(true);
        }
    }
}