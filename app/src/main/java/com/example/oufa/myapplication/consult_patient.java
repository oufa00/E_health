package com.example.oufa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class consult_patient extends AppCompatActivity {
    SessionManager session;
    String idmedecin="";
    ListView listView;
    String result1="";
    ContactAdapter_consult_patient contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_patient);
        session = new SessionManager(getApplicationContext());
        listView=(ListView) findViewById(R.id.listview_patient);

        HashMap<String, String> user=session.getUserDetails();
        idmedecin=user.get(SessionManager.KEY_ID);
        Background_consult background_consult_patient=new Background_consult(this);
        background_consult_patient.execute(idmedecin);
    }

    @Override
    public void onBackPressed() {
        session.createLoginSession(idmedecin);
        Intent intent=new Intent(consult_patient.this,doctor_main.class);
        startActivity(intent);
    }

    public  void consulter(){
        contactAdapter=new ContactAdapter_consult_patient(this,R.layout.row_layout_consult_patient);
        listView.setAdapter(contactAdapter);


        try {
            JSONObject jsonObject = new JSONObject(result1);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String  nom, prenom,diabet,tension,type;
            while (count < jsonArray.length()) {
                Contactspatient contacts;
                JSONObject jo = jsonArray.getJSONObject(count);
                nom = jo.getString("nom");
                prenom = jo.getString("prenom");
                diabet=jo.getString("diabet");
                tension=jo.getString("tension");
                type=diabet+" et "+tension;
                contacts = new Contactspatient(nom, prenom,type);
                contactAdapter.add(contacts);
                count++;


            }
        } catch(JSONException e){
            e.printStackTrace();
        }



    }
    public class ContactAdapter_consult_patient extends ArrayAdapter {
        List lis=new ArrayList();
        public ContactAdapter_consult_patient(@NonNull Context context, int resource) {
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
            final ContactAdapter_consult_patient.Contactholder contactholder;
            if(row==null){
                LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=layoutInflater.inflate(R.layout.row_layout_consult_patient,parent,false);
                contactholder=new ContactAdapter_consult_patient.Contactholder();
                contactholder.tx_nom=(TextView)row.findViewById(R.id.tx_nom);
                contactholder.tx_prenom=(TextView)row.findViewById(R.id.tx_prenom);
                contactholder.consult_diab=(Button)row.findViewById(R.id.cons_diab);
                contactholder.consult_tension=(Button)row.findViewById(R.id.cons_tens);
                contactholder.consult_3afssa=(Button)row.findViewById(R.id.cons_kach_3afssa);
                row.setTag(contactholder);

            }
            else {
                contactholder=(ContactAdapter_consult_patient.Contactholder)row.getTag();
            }
            Contactspatient contacts=(Contactspatient) this.getItem(position);
            contactholder.tx_nom.setText(contacts.getNom());
            contactholder.tx_prenom.setText(contacts.getPrenom());
            contactholder.consult_diab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Background_get_id_patient background_get_id_patient=new Background_get_id_patient(getApplication(),"1");
                    background_get_id_patient.execute(contactholder.tx_nom.getText().toString(),contactholder.tx_prenom.getText().toString(),"1");

                }
            });
            contactholder.consult_tension.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Background_get_id_patient background_get_id_patient=new Background_get_id_patient(getApplication(),"2");
                    background_get_id_patient.execute(contactholder.tx_nom.getText().toString(),contactholder.tx_prenom.getText().toString(),"2");


                }
            });

            contactholder.consult_3afssa.setVisibility(View.VISIBLE);
            contactholder.consult_3afssa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Background_send_mail background_send_mail=new Background_send_mail(getApplication());
                    background_send_mail.execute(contactholder.tx_nom.getText().toString(),contactholder.tx_prenom.getText().toString());
                }
            });
            if(contacts.getAge().equals("non et oui")){
                     contactholder.consult_diab.setVisibility(View.INVISIBLE);
                     contactholder.consult_tension.setVisibility(View.VISIBLE);
            }
            if((contacts.getAge().equals("1 et non"))|(contacts.getAge().equals("2 et non"))|(contacts.getAge().equals("3 et non"))){
                contactholder.consult_diab.setVisibility(View.VISIBLE);
                contactholder.consult_tension.setVisibility(View.INVISIBLE);
            }
            if((contacts.getAge().equals("1 et oui"))|(contacts.getAge().equals("2 et oui"))|(contacts.getAge().equals("3 et oui"))){
                contactholder.consult_diab.setVisibility(View.VISIBLE);
                contactholder.consult_tension.setVisibility(View.VISIBLE);
            }
            return row;
        }
        public class Contactholder{
            TextView tx_nom,tx_prenom;
            Button consult_diab,consult_tension,consult_3afssa;
        }
    }
    public class Background_consult extends AsyncTask<String,Void,String> {
        Context context;


        Background_consult(Context cntx) {
            context = cntx;

        }

        @Override
        protected String doInBackground(String... params) {

            String jsonurl = "http://192.168.43.33/patient_consult.php";

            try {

                String id=params[0];

                URL url=new URL(jsonurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("id","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8");
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
            } else if(result.equals("no")) {
                Toast.makeText(context, "aucun patient exist", Toast.LENGTH_SHORT).show();
               }
            else{
                result1=result;
                consulter();
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
    public class Background_get_id_patient extends AsyncTask<String,Void,String> {
        Context context;
        String type;


        Background_get_id_patient(Context cntx,String type) {
            context = cntx;
            this.type=type;

        }

        @Override
        protected String doInBackground(String... params) {

            String jsonurl = "http://192.168.43.33/id_patient_nom+prenom.php";


            try {

                String nom=params[0];
                String prenom=params[1];


                URL url=new URL(jsonurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("nom","UTF-8")+"="+ URLEncoder.encode(nom,"UTF-8")+"&"
                        +URLEncoder.encode("prenom","UTF-8")+"="+URLEncoder.encode(prenom,"UTF-8");
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
            } else if(result.equals("no")) {
            }
            else{
                if(type=="1"){
                session.createLoginSession(result);
                Intent intent=new Intent(consult_patient.this,consult_diabet.class);
                startActivity(intent);}
                if(type=="2"){
                    session.createLoginSession(result);
                    Intent intent=new Intent(consult_patient.this,consult_tension.class);
                    startActivity(intent);
                }

            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
    public class Background_send_mail extends AsyncTask<String,Void,String> {
        Context context;



        Background_send_mail(Context cntx) {
            context = cntx;


        }

        @Override
        protected String doInBackground(String... params) {

            String jsonurl = "http://192.168.43.33/send_mail.php";


            try {

                String nom=params[0];
                String prenom=params[1];


                URL url=new URL(jsonurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("nom","UTF-8")+"="+ URLEncoder.encode(nom,"UTF-8")+"&"
                        +URLEncoder.encode("prenom","UTF-8")+"="+URLEncoder.encode(prenom,"UTF-8");
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
            } else if(result.equals("")) {

            }
            else{
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                String[] to={result};
                intent.putExtra(Intent.EXTRA_EMAIL,to);
                Toast.makeText(context,""+result,Toast.LENGTH_SHORT).show();
                intent.putExtra(Intent.EXTRA_SUBJECT,"hi,im your doctor");
                intent.putExtra(Intent.EXTRA_TEXT,"");
                intent.setType("message/rfc822");
                Intent chooser = Intent.createChooser(intent, "Send email");
                startActivity(chooser);
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}


