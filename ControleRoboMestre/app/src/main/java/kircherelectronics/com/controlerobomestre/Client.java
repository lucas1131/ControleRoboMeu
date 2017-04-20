package kircherelectronics.com.controlerobomestre;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends AsyncTask<Void, Void, Void> {

    private String ip;
    private int port;
    private Socket socket;

    private TextView serverResponse;
    public String response = "";

    public Scanner clientIn;
    public PrintStream clientOut;

    public boolean errorFlag = false;

    Client(String addr, int port, TextView serverResponse) {
        this.ip = addr;
        this.port = port;
        this.serverResponse = serverResponse;
    }

    Client(String addr, TextView serverResponse) {
        this.ip = addr;
        this.port = 8080;
        this.serverResponse = serverResponse;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        this.socket = null;

        try {
            this.socket = new Socket(ip, port);

            this.clientIn = new Scanner(socket.getInputStream());
            this.clientOut = new PrintStream(socket.getOutputStream());

            while(clientIn.hasNextLine())
                this.response = this.clientIn.nextLine();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
            errorFlag = true;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
            errorFlag = true;

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    errorFlag = true;
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        this.serverResponse.setText(response);
        super.onPostExecute(result);
    }
}