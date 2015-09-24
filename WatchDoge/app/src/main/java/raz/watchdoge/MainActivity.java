package raz.watchdoge;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.*;
import java.net.SocketAddress;

import static java.net.InetAddress.*;

public class MainActivity extends AppCompatActivity {
    String adresse = "206.167.212";
    int Dplage;
    int Fplage;
    int port;
    boolean isSuspended = false;
    boolean isStarted = false;
    ProgressBar theBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        theBar = (ProgressBar)findViewById(R.id.barre);
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
        isSuspended = false;

        if(!isStarted && isValid()) {
            isStarted = true;
            ((TextView) findViewById(R.id.adresseView)).setText("");
            adresse = ((EditText) findViewById(R.id.sousReseau)).getText().toString();
            Dplage = Integer.parseInt(((EditText) findViewById(R.id.Dplage)).getText().toString());
            Fplage = Integer.parseInt(((EditText) findViewById(R.id.Fplage)).getText().toString());
            port = Integer.parseInt(((EditText) findViewById(R.id.Nport)).getText().toString());
            ZeBigCalcul z = new ZeBigCalcul();
            z.execute();
        }


    }
    public void Suspendre(View v)
    {

      isSuspended = true;


    }
    private boolean isValid()
    {
        boolean verif = false;
        try {
            Integer.parseInt(((EditText) findViewById(R.id.Nport)).getText().toString());
            if(Integer.parseInt(((EditText) findViewById(R.id.Dplage)).getText().toString()) < Integer.parseInt(((EditText) findViewById(R.id.Fplage)).getText().toString()) && !(((EditText) findViewById(R.id.sousReseau)).getText().toString()).trim().isEmpty()&& !(((EditText) findViewById(R.id.Nport)).getText().toString()).trim().isEmpty() )
                verif = true;
            else
                Toast.makeText(this, "Invalide plage ou Sous Reseau!",Toast.LENGTH_LONG).show();

        }
         catch (NumberFormatException e){
         Toast.makeText(this, "Invalide plage de Réseau ou Invalide port !",Toast.LENGTH_LONG).show();

        }

        return verif;

    }
    private  class ZeBigCalcul extends AsyncTask<Void, Integer , Void> {


        Socket socket = null;
      public  ZeBigCalcul()
        {



        }
            @Override
            protected Void doInBackground(Void... params) {

                for (int i = Dplage; i <= Fplage; i++) {
                    try {

                        while(isSuspended)
                            Thread.sleep(500);

                        socket = new Socket();
                        socket.connect(new InetSocketAddress((adresse + "." + i).toString(), port), 500);

                        publishProgress(i);

                    }
                    catch (InterruptedException e){}
                    catch (IOException e) {

                        publishProgress(i ,-1);
                    }
                   finally{
                        try {
                            socket.close();
                        }
                        catch (IOException e){}
                    }
                }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer...values)
        {
            if(values.length != 2)
            ( (TextView)findViewById(R.id.adresseView)).append(adresse +"." + values[0] +"\n");


            theBar.setProgress(((values[0] - Dplage) * 100) / (Fplage - Dplage));
        }
        @Override
        protected void onPostExecute(Void Result)
        {
            //theBar.setProgress(0);
            isStarted = false;
            Toast thatToast = Toast.makeText(getApplicationContext(), "la Watch est fini!",Toast.LENGTH_LONG);
            thatToast.show();
        }


    }
}
