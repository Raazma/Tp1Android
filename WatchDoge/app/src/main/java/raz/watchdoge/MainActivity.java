package raz.watchdoge;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.*;
import java.net.SocketAddress;

import static java.net.InetAddress.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

  public  void Start (View v)
    {
        ZeBigCalcul z = new ZeBigCalcul();
        z.execute();


    }
    private  class ZeBigCalcul extends AsyncTask<Void, String , Void> {


        Socket socket = new Socket();
        String adresse = "206.167.212";
        @Override
        protected Void doInBackground(Void... params) {

            for (int i = 75; i < 150; i++) {
                try {
                    publishProgress(adresse + "." +i);

                    socket.connect(new InetSocketAddress((adresse + "." + i).toString(), 80), 5000);

                    publishProgress(adresse + "." + i);

                } catch (IOException e) {
                    publishProgress(e.toString());

                }

            }
            try {
                socket.close();
            }
            catch (IOException e){}
            return null;
        }

        @Override
        protected void onProgressUpdate(String...values)
        {
            ( (TextView)findViewById(R.id.adresseView)).setText(values[0]);
        }
        @Override
        protected void onPostExecute(Void Result)
        {


        }


    }
}
