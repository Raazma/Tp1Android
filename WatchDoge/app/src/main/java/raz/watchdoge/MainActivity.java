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

    String adresse ;
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
        //met la variable isStarted a true et prend les valeurs des champs et par le thread du asynctask
        isSuspended = false;//la suspension est a false

        if(!isStarted && isValid()) {//verifie que le scan n'est pas deja lancer et verifie les champs de texte des entrées
            isStarted = true;
            ((TextView) findViewById(R.id.adresseView)).setText("");//set le text de du textView a vide pour effacer le dernier scan
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
   //met seulement la boolean de suspension a true
      isSuspended = true;

    }
    private boolean isValid()
    {
        boolean verif = false;
        try {
            //tente de parser les zone de texte si invalide lance une execption et verifie que la plage de debut est plus petite que la plage de fin et que les zone de texte de son pas vide
           if( Integer.parseInt(((EditText) findViewById(R.id.Nport)).getText().toString())  > 0 &&Integer.parseInt(((EditText) findViewById(R.id.Nport)).getText().toString()) < 65636) {
               if (Integer.parseInt(((EditText) findViewById(R.id.Dplage)).getText().toString()) < Integer.parseInt(((EditText) findViewById(R.id.Fplage)).getText().toString()) && !(((EditText) findViewById(R.id.sousReseau)).getText().toString()).trim().isEmpty() && !(((EditText) findViewById(R.id.Nport)).getText().toString()).trim().isEmpty())
                   verif = true;
               else
                   Toast.makeText(this, "Invalide plage ou Sous Reseau!", Toast.LENGTH_LONG).show();// la plage est invalide la plage de depart est plus grande que la plage de fin ou ladresse de sous reseau est vide

           }
            else
               Toast.makeText(this, "Invalide plage ou Sous Reseau!", Toast.LENGTH_LONG).show();// la plage est invalide la plage de depart est plus grande que la plage de fin ou ladresse de sous reseau est vide
        }
         catch (NumberFormatException e){
         Toast.makeText(this, "Invalide plage de Réseau ou Invalide port !",Toast.LENGTH_LONG).show();

        }

        return verif;

    }
    private  class ZeBigCalcul extends AsyncTask<Void, Integer , Void> {


        Socket socket = null;
            @Override
            protected Void doInBackground(Void... params) {

                //boucle a partir de la plage de depart et de la plage de fin si la connection a reussi envoie seulement la valeur du i
                //si la connection a echouer on envoie le i et -1 pour en informer doInprogress
                for (int i = Dplage; i <= Fplage; i++) {
                    try {

                        while(isSuspended)//boucle aussi longtemp que la boolean de suspension n'est pas remis a false
                            Thread.sleep(500);

                        socket = new Socket();
                        socket.connect(new InetSocketAddress((adresse + "." + i).toString(), port), 500); //tente une connection avec un timeout dune demi seconde

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
            if(values.length != 2) //si je n,au pas recu de -1 jaffiche ladresse la connection a reussi
            ( (TextView)findViewById(R.id.adresseView)).append(adresse +"." + values[0] +"\n");

            //active la progression de la progressbar
            theBar.setProgress(((values[0] - Dplage) * 100) / (Fplage - Dplage));
        }
        @Override
        protected void onPostExecute(Void Result)
        {
            //set les valeur de defaut pouvoir relancer un nouveau Scan
            theBar.setProgress(0);
            isStarted = false;
            Toast thatToast = Toast.makeText(getApplicationContext(), "la Watch est fini!",Toast.LENGTH_LONG);
            thatToast.show();
        }


    }
}
