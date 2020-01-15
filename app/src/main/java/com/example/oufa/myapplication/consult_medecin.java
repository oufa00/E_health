package com.example.oufa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class consult_medecin extends AppCompatActivity {
    ListView listview;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ContactAdaptermesmedecin contactAdapter;
    String result1="";
    String idpatient="";
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_medecin);
        listview = (ListView) findViewById(R.id.list_med1);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user=session.getUserDetails();
        idpatient=user.get(SessionManager.KEY_ID);
        backgroundmesmedecin backgroundmedecin = new backgroundmesmedecin(this);
        backgroundmedecin.execute(idpatient);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(consult_medecin.this,home_patient.class);
        session.createLoginSession(idpatient);
        startActivity(intent);
    }

    public void liste(){
        contactAdapter = new ContactAdaptermesmedecin(this, R.layout.row_layoutmesmedecin);
        listview.setAdapter(contactAdapter);
        try {

            jsonObject = new JSONObject(result1);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String nom = "", prenom = "";
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                nom = jo.getString("nom");
                prenom = jo.getString("prenom");

                Contactspatient contacts = new Contactspatient(nom,prenom,"");
                contactAdapter.add(contacts);
                count++;

            }
        } catch (JSONException e) {

        }
    }
    public class backgroundmesmedecin extends AsyncTask<String,Void,String> {
        Context context;


        backgroundmesmedecin(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/mes_medecin.php";

            try {
                String id=params[0];


                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String  result="";
                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    result+=line;
                    System.out.println(result);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                result1=result;
                liste();
            }
            if(result==null){
                Toast.makeText(context,"erreur de connexion",Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
    public class ContactAdaptermesmedecin extends ArrayAdapter {
        List lis=new ArrayList();
        public ContactAdaptermesmedecin(@NonNull Context context, int resource) {
            super(context, resource);
        }


        public void add(Contactspatient object) {
            super.add(object);
            lis.add(object);
        }

        @Override
        public int getCount() {
            return lis.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return lis.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row;
            row=convertView;
            final ContactAdaptermesmedecin.Contactholder contactholder;
            if(row==null){
                LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=layoutInflater.inflate(R.layout.row_layoutmesmedecin,parent,false);
                contactholder=new ContactAdaptermesmedecin.Contactholder();
                contactholder.tx_nom=(TextView)row.findViewById(R.id.tx_nom);
                contactholder.tx_prenom=(TextView)row.findViewById(R.id.tx_prenom);
                contactholder.add=(Button) row.findViewById(R.id.bt_add);
                contactholder.partage=(Button) row.findViewById(R.id.bt_partage);
                row.setTag(contactholder);

            }
            else {
                contactholder=(ContactAdaptermesmedecin.Contactholder)row.getTag();
            }
            Contactspatient contacts=(Contactspatient) this.getItem(position);
            contactholder.tx_nom.setText(contacts.getNom());
            contactholder.tx_prenom.setText(contacts.getPrenom());

            contactholder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Backgrounddelete backgrounddelete=new  Backgrounddelete(getContext());
                    backgrounddelete.execute(idpatient,contactholder.tx_nom.getText().toString(),contactholder.tx_prenom.getText().toString());
                }
            });
            contactholder.partage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Backgroundpartage backgroundpartage=new  Backgroundpartage(getContext());
                    backgroundpartage.execute(idpatient,contactholder.tx_nom.getText().toString(),contactholder.tx_prenom.getText().toString());
                }
            });
            return row;
        }
        public class Contactholder{
            TextView tx_nom,tx_prenom;
            Button add,partage;
        }

    }
    public class Backgrounddelete extends AsyncTask<String,Void,String> {
        Context context;


        Backgrounddelete(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/medecin_del.php";

            try {
                String id=params[0];
                String nom=params[1];
                String prenom=params[2];

                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("nom","UTF-8")+"="+ URLEncoder.encode(nom,"UTF-8")+"&"
                        +URLEncoder.encode("prenom","UTF-8")+"="+URLEncoder.encode(prenom,"UTF-8")+"&"
                        +URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String  result="";
                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    result+=line;
                    System.out.println(result);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
            }else if (result.equals("medecin n'existe pas"))
                    Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
             else {

                    backgroundmesmedecin backgroundmedecin = new backgroundmesmedecin(context);
                    backgroundmedecin.execute(idpatient);

            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
    public class Backgroundpartage extends AsyncTask<String,Void,String> {
        Context context;


        Backgroundpartage(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/medecin_partage.php";

            try {
                String id=params[0];
                String nom=params[1];
                String prenom=params[2];

                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("nom","UTF-8")+"="+ URLEncoder.encode(nom,"UTF-8")+"&"
                        +URLEncoder.encode("prenom","UTF-8")+"="+URLEncoder.encode(prenom,"UTF-8")+"&"
                        +URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String  result="";
                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    result+=line;
                    System.out.println(result);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
            }else if (result.equals("no"))
                Toast.makeText(context, "aucune analyse enregistrée", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(context, "succèss", Toast.LENGTH_SHORT).show();

            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}


