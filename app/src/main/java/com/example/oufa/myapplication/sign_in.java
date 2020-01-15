package com.example.oufa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

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
import java.util.List;

public class sign_in extends AppCompatActivity {
    @NotEmpty
    @Length(min = 4,max =8)
    @Password(scheme = Password.Scheme.ALPHA_NUMERIC)
    EditText pass;
    @NotEmpty
    @Email
    EditText email;
    RadioGroup groupe;
    RadioButton buttonradio;
    int buttonradion=1;
    SessionManager session;
    boolean valider=false;
    com.mobsandgeeks.saripaar.Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.password);
        session = new SessionManager(getApplicationContext());
        validator =new com.mobsandgeeks.saripaar.Validator(this);
        validator.setValidationListener(new com.mobsandgeeks.saripaar.Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                valider=true;
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for(ValidationError error :errors){
                    View view=error.getView();
                    String message=error.getCollatedErrorMessage(getApplicationContext());
                    if(view instanceof  EditText){
                        ((EditText) view).setError(message);
                    }else{
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void signin(View v){
        String mail=email.getText().toString();
        String passw=pass.getText().toString();
        validator.validate();
       //patient
     if(valider==true){
        if(buttonradion==1){

            passw=mail+passw;
            passw=passw.hashCode()+"";
            backgroundpat background=new backgroundpat(this);
            background.execute(mail,passw);}
            //medecin
        if(buttonradion==0){
            passw=mail+passw;
            passw=passw.hashCode()+"";
            backgroundmed background1=new backgroundmed(this);
            background1.execute(mail,passw);}
        }
    }
    public void signup(View v){
              Intent intent=new Intent(sign_in.this,sign_up.class);
              startActivity(intent);
    }
    public void check(View v){
        groupe=(RadioGroup)findViewById(R.id.groupbutton);
        int radioid=groupe.getCheckedRadioButtonId();
        buttonradio=(RadioButton)findViewById(radioid);
        //patient
        if(buttonradio.getText().equals("Patient")){
            buttonradion=1;
        }
        //medecin
        if(buttonradio.getText().equals("Medecin")){
            buttonradion=0;
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }

    }

    //db patient
    public class backgroundpat extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        String  result="";
        String emaill;

        backgroundpat(Context cntx){
            context=cntx;
        }
        @Override
        protected String doInBackground(String... params) {
            String login_url="http://192.168.43.33/patient_auth.php";

                try {
                    emaill=params[0];
                    String password=params[1];
                    URL url=new URL(login_url);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emaill
                            ,"UTF-8")+"&"+URLEncoder.encode("password"
                            ,"UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    result="";
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


            if(result==null){
                Toast.makeText(context,"erreur de connexion",Toast.LENGTH_SHORT).show();
            }
else{

                if (result.equals("no")) {
                    Toast.makeText(context,"erreur le compte n'existe pas",Toast.LENGTH_LONG).show();
                } else {

                        //yadkhol ll activite patient

                        session.createLoginSession(result);
                        Intent intent = new Intent(sign_in.this, patient_main.class);
                        startActivity(intent);
                        finish();


                }
                }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }

//medecin db

    public class backgroundmed extends AsyncTask<String,Void,String> {
        Context context;
        String  result="";
        String emaill;

        backgroundmed(Context cntx){
            context=cntx;
        }
        @Override
        protected String doInBackground(String... params) {
            String login_url="http://192.168.43.33/med_auth.php";

            try {
                emaill=params[0];
                String password=params[1];
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(emaill,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                result="";
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


            if(result==null){
                Toast.makeText(context,"erreur de connexion",Toast.LENGTH_LONG).show();
            }
            else {

                if (result.equals("no")) {
                    Toast.makeText(context,"erreur le compte n'existe pas",Toast.LENGTH_LONG).show();
                } else {

                    //yadkhol ll activite medecin

                        session.createLoginSession(result);
                        Intent intent=new Intent(sign_in.this,doctor_main.class);
                        startActivity(intent);
                        finish();



                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }
}
