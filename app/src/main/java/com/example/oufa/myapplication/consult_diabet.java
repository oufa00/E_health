package com.example.oufa.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.HashMap;

public class consult_diabet extends AppCompatActivity {
    ListView listview;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ContactAdapterdiabete contactAdapter;
    TextView date,glyc,message,mindatee,maxdatee,status;
    String result1="";
    String idpatient="";
    SessionManager sessionManager;
    DatePickerDialog.OnDateSetListener mindate,maxdate;
    String datemin="",datemax="";
    boolean verifier1=false,verifier2=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_diabet);
        date=(TextView)findViewById(R.id.date);
        glyc=(TextView)findViewById(R.id.glyc);
        status=(TextView)findViewById(R.id.status);
        message=(TextView)findViewById(R.id.message);
        listview=(ListView) findViewById(R.id.listview);
        mindatee=(TextView)findViewById(R.id.datemin);
        maxdatee=(TextView)findViewById(R.id.datemax);
        //bach nafficher l calendrier de min date
        mindatee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(consult_diabet.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mindate,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mindate=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                datemin=year+"-"+month+"-"+dayOfMonth+" 00:00:00";
                String dateminset=year+"-"+month+"-"+dayOfMonth;
                mindatee.setText(dateminset);
                mindatee.setBackgroundResource(0);
                verifier1=true;
                if((verifier1==true)&&(verifier2==true)){
                    backgroundiabetres backgroundiabetres=new backgroundiabetres(getApplication());
                    backgroundiabetres.execute(idpatient,datemin,datemax);
                    verifier1=false;}

            }
        };
        maxdatee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(consult_diabet.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,maxdate,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
          maxdate=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                int envdayOfMonth=dayOfMonth+1;
                datemax=year+"-"+month+"-"+envdayOfMonth+" 00:00:00";
                String datemaxset=year+"-"+month+"-"+dayOfMonth;
                maxdatee.setText(datemaxset);
                verifier2=true;
                maxdatee.setBackgroundResource(0);
                if((verifier1==true)&&(verifier2==true)){
                    backgroundiabetres backgroundiabetres=new backgroundiabetres(getApplication());
                    backgroundiabetres.execute(idpatient,datemin,datemax);
                    verifier2=false;}

            }
        };
        sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.checkLogin())
            finish();
        HashMap<String, String> user=sessionManager.getUserDetails();
        idpatient=user.get(SessionManager.KEY_ID);
          backgroundiabetres backgroundiabetres=new backgroundiabetres(getApplication());
          backgroundiabetres.execute(idpatient,"","");

    }
    /*public void onBackPressed() {
        Intent intent=new Intent(consult_diabet.this,home_patient.class);
        sessionManager.createLoginSession(idpatient);
        startActivity(intent);
    }*/
    public  void consulter(){
        contactAdapter=new ContactAdapterdiabete(this,R.layout.row_layoutdiabete);
        listview.setAdapter(contactAdapter);
        date.setVisibility(View.VISIBLE);
        glyc.setVisibility(View.VISIBLE);
        status.setVisibility(View.VISIBLE);


        try {
            jsonObject = new JSONObject(result1);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String  date_ajoute="", glyc="",status="";
            while (count < jsonArray.length()) {
                Contactsdiabete contacts;
                JSONObject jo = jsonArray.getJSONObject(count);
                date_ajoute = jo.getString("date_ajoute");
                glyc = jo.getString("glyc");
                status = jo.getString("status");
                    contacts = new Contactsdiabete(date_ajoute, glyc,status);
                contactAdapter.add(contacts);
                count++;


            }
        } catch(JSONException e){
            e.printStackTrace();
        }



    }
    public class backgroundiabetres extends AsyncTask<String,Void,String> {
        Context context;


        backgroundiabetres(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String jsonurl = "http://192.168.43.33/diabete_consulter.php";

            try {

                String id=params[0];
                String datemin=params[1];
                String datemax=params[2];
                URL url=new URL(jsonurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("datemin","UTF-8")+"="+ URLEncoder.encode(datemin,"UTF-8")+"&"
                        +URLEncoder.encode("datemax","UTF-8")+"="+URLEncoder.encode(datemax,"UTF-8")+"&"
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
            } else if(result.equals("no")) {
                date.setVisibility(View.INVISIBLE);
                glyc.setVisibility(View.INVISIBLE);
                message.setVisibility(View.VISIBLE);
                status.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.INVISIBLE);}
            else{
                result1=result;
                message.setVisibility(View.INVISIBLE);
                consulter();
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
