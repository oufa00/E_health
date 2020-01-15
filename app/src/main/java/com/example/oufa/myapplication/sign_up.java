package com.example.oufa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.service.autofill.Validator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class sign_up extends AppCompatActivity  {
    LinearLayout maladie,type;
    @NotEmpty
            EditText nom,prenom,adress;
    @NotEmpty
    @Length(min = 4,max =8)
    @Password(scheme = Password.Scheme.ALPHA_NUMERIC)
    EditText password;
    @NotEmpty
    @Email
            EditText email;
    @NotEmpty
    @Length(min = 10,max =10)
            EditText telphone;
    @NotEmpty
    @Length(min = 1 ,max =2)
            EditText age;

CheckBox patient,doctor;
    CheckBox diabet,tension;
    RadioGroup groupe;
    RadioButton buttonradio;
    int buttonradion=1;
    String result;
    boolean valider=false;
    SessionManager session;
    com.mobsandgeeks.saripaar.Validator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        session = new SessionManager(getApplicationContext());
        nom=(EditText)findViewById(R.id.nom);
        prenom=(EditText)findViewById(R.id.prenom);
        email=(EditText)findViewById(R.id.emailin);
        adress=(EditText)findViewById(R.id.adress);
        password=(EditText)findViewById(R.id.passwordin);
        //repassword=(EditText)findViewById(R.id.repassword);
        age=(EditText)findViewById(R.id.age);
        telphone=(EditText)findViewById(R.id.telephone);
        patient=(CheckBox)findViewById(R.id.patientcheck);
        doctor=(CheckBox)findViewById(R.id.doctorcheck);
        diabet=(CheckBox)findViewById(R.id.diabetcheck);
        tension=(CheckBox)findViewById(R.id.tensioncheck);
        maladie=(LinearLayout)findViewById(R.id.maladie);
        type=(LinearLayout)findViewById(R.id.type);
        doctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if((doctor.isChecked())&&(!patient.isChecked())){maladie.setVisibility(View.GONE);
                    type.setVisibility(View.GONE);
                }
                if ((patient.isChecked()) && (!doctor.isChecked())) maladie.setVisibility(View.VISIBLE);
                if ((patient.isChecked()) && (doctor.isChecked())) maladie.setVisibility(View.VISIBLE);

            }

        });
patient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if ((patient.isChecked()) && (!doctor.isChecked())) maladie.setVisibility(View.VISIBLE);
        if ((patient.isChecked()) && (doctor.isChecked())) maladie.setVisibility(View.VISIBLE);

        if((doctor.isChecked())&&(!patient.isChecked())){maladie.setVisibility(View.GONE);
            type.setVisibility(View.GONE);
            diabet.setChecked(false);
            tension.setChecked(false);
        }
        if((!doctor.isChecked())&&(!patient.isChecked())){maladie.setVisibility(View.GONE);
            type.setVisibility(View.GONE);
            diabet.setChecked(false);
            tension.setChecked(false);
        }
    }
});
diabet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!diabet.isChecked()) type.setVisibility(View.GONE);
        if((tension.isChecked())&&(!diabet.isChecked()))type.setVisibility(View.GONE);
        if((diabet.isChecked())&&(!tension.isChecked()))type.setVisibility(View.VISIBLE);
        if((diabet.isChecked())&&(tension.isChecked()))type.setVisibility(View.VISIBLE);
    }
});

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
    public void create(View v){
        String prenomm=prenom.getText().toString();
        String nomm=nom.getText().toString();
        String emaill=email.getText().toString();
        String passwordd=password.getText().toString();
        String agee=age.getText().toString();
        String telephonee=telphone.getText().toString();
        String adresse=adress.getText().toString();
        validator.validate();
        if (valider == true) {
        if(passwordd.contains(" ")){
            password.setError("le caractère 'espace' est invalide");
        }
        else {
            passwordd = emaill + passwordd;
            passwordd = passwordd.hashCode() + "";



                if ((!patient.isChecked()) && (!doctor.isChecked())) {
                    Toast.makeText(getApplicationContext(), "cocher le mode d'inscription", Toast.LENGTH_LONG).show();
                }
                if ((patient.isChecked()) && (!doctor.isChecked())) {
                    maladie.setVisibility(View.VISIBLE);
                    // tension + diabete
                    if ((!diabet.isChecked()) && (!tension.isChecked())) {
                        Toast.makeText(getApplicationContext(), "selectioner votre maladie", Toast.LENGTH_LONG).show();
                    }
                    if ((diabet.isChecked()) && (tension.isChecked())) {
                        type.setVisibility(View.VISIBLE);
                        background backgroundpatientadd = new background(this);
                        backgroundpatientadd.execute(nomm, prenomm, agee, emaill, telephonee, adresse, passwordd, buttonradion + "", "oui");
                    }
                    //tension not diabete
                    if ((tension.isChecked()) && (!diabet.isChecked())) {
                        type.setVisibility(View.INVISIBLE);
                        background backgroundpatientadd = new background(this);
                        backgroundpatientadd.execute(nomm, prenomm, agee, emaill, telephonee, adresse, passwordd, "non", "oui");
                    }
                    //diabete not tension
                    if ((diabet.isChecked()) && (!tension.isChecked())) {
                        type.setVisibility(View.VISIBLE);
                        background backgroundpatientadd = new background(this);
                        backgroundpatientadd.execute(nomm, prenomm, agee, emaill, telephonee, adresse, passwordd, buttonradion + "", "non");
                    }
                }
                if ((doctor.isChecked()) && (!patient.isChecked())) {

                    Backgroundmedecinadd backgroundmedecinadd = new Backgroundmedecinadd(this);
                    backgroundmedecinadd.execute(nomm, prenomm, agee, emaill, telephonee, adresse, passwordd);
                }
                if ((patient.isChecked()) && (doctor.isChecked())) {
                    maladie.setVisibility(View.VISIBLE);
                    // tension + diabete
                    if ((diabet.isChecked()) && (tension.isChecked())) {
                        type.setVisibility(View.VISIBLE);
                        Backgroundmedecinpatadd backgroundmedecinpatadd = new Backgroundmedecinpatadd(this);
                        backgroundmedecinpatadd.execute(nomm, prenomm, agee, emaill, telephonee, adresse, passwordd, buttonradion + "", "oui");
                    }
                    if ((!diabet.isChecked()) && (!tension.isChecked())) {
                        Toast.makeText(getApplicationContext(), "selectioner votre maladie", Toast.LENGTH_LONG).show();
                    }
                    //tension not diabete
                    if ((tension.isChecked()) && (!diabet.isChecked())) {
                        type.setVisibility(View.GONE);
                        Backgroundmedecinpatadd backgroundmedecinpatadd = new Backgroundmedecinpatadd(this);
                        backgroundmedecinpatadd.execute(nomm, prenomm, agee, emaill, telephonee, adresse, passwordd, buttonradion + "", "oui");
                    }
                    //diabete not tension
                    if ((diabet.isChecked()) && (!tension.isChecked())) {
                        type.setVisibility(View.VISIBLE);
                        Backgroundmedecinpatadd backgroundmedecinpatadd = new Backgroundmedecinpatadd(this);
                        backgroundmedecinpatadd.execute(nomm, prenomm, agee, emaill, telephonee, adresse, passwordd, buttonradion + "", "non");
                    }

                }
            }
        }

    }
    public void check1(View v){
        groupe=(RadioGroup)findViewById(R.id.groupbutton1);
        int radioid=groupe.getCheckedRadioButtonId();
        buttonradio=(RadioButton)findViewById(radioid);
        //type 1
        if(buttonradio.getText().equals("Type 1")){
            buttonradion=1;
        }
        //type 2
        if(buttonradio.getText().equals("Type 2")){
            buttonradion=2;
        }
        //type 3
        if(buttonradio.getText().equals("Type 3")){
            buttonradion=3;
        }
    }
    public class background extends AsyncTask<String,Void,String> {
        Context context;

        background(Context cntx) {
            context = cntx;}
            @Override
            protected String doInBackground (String...params){
                String login_url ="http://192.168.43.33/createpat.php";


                try {
                    String nom = params[0];
                    String prenom = params[1];
                    String age = params[2];
                    String email = params[3];
                    String tel = params[4];
                    String address = params[5];
                    String password = params[6];
                    String diabet=params[7];
                    String tension=params[8];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data = URLEncoder.encode("nom"
                            ,"UTF-8")+"="+URLEncoder.encode(nom
                            ,"UTF-8")+"&"+URLEncoder.encode("prenom"
                            ,"UTF-8")+"="+URLEncoder.encode(prenom
                            ,"UTF-8")+"&"+URLEncoder.encode("adress"
                            ,"UTF-8")+"="+URLEncoder.encode(address
                            ,"UTF-8")+"&"+URLEncoder.encode("email"
                            ,"UTF-8")+"="+URLEncoder.encode(email
                            ,"UTF-8")+"&"+URLEncoder.encode("telephone"
                            ,"UTF-8")+"="+URLEncoder.encode(tel
                            ,"UTF-8")+"&"+URLEncoder.encode("password"
                            ,"UTF-8")+"="+URLEncoder.encode(password
                            ,"UTF-8")+"&"+URLEncoder.encode("age"
                            ,"UTF-8")+"="+URLEncoder.encode(age
                            ,"UTF-8")+"&"+URLEncoder.encode("diabet"
                            ,"UTF-8")+"="+URLEncoder.encode(diabet
                            ,"UTF-8")+"&"+URLEncoder.encode("tension"
                            ,"UTF-8")+"="+URLEncoder.encode(tension,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    result="";
                    String line = "";
                    while ((line=bufferedReader.readLine())!= null) {
                        result += line;
                        System.out.println(result);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute () {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute (String result) {
                if (result == null)
                    Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
                else {
                    if (result.equals("user existe deja"))
                        Toast.makeText(context, "user existe deja", Toast.LENGTH_SHORT).show();
                    else {

                        Toast.makeText(context, "succes", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(sign_up.this, sign_in.class);
                        startActivity(intent);
                    }
                }
            }
            @Override
            protected void onProgressUpdate (Void...values){
                super.onProgressUpdate(values);
            }

    }


    public class Backgroundmedecinadd extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        Backgroundmedecinadd(Context cntx){
            context=cntx;
        }
        @Override
        protected String doInBackground(String... params) {

            String login_url="http://192.168.43.33/createmed.php";

            try {
                String nom=params[0];
                String prenom=params[1];
                String age=params[2];
                String email=params[3];
                String tel=params[4];
                String address=params[5];
                String password=params[6];
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("nom","UTF-8")+"="+ URLEncoder.encode(nom,"UTF-8")+"&"
                        +URLEncoder.encode("prenom","UTF-8")+"="+URLEncoder.encode(prenom,"UTF-8")+"&"
                        +URLEncoder.encode("adress","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("telephone","UTF-8")+"="+URLEncoder.encode(tel,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8");
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
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            //alertDialog=new AlertDialog.Builder(context).create();
            //alertDialog.setTitle("create");
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null)
                Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
            else {
                if (result.equals("user existe deja"))
                    Toast.makeText(context, "user existe deja", Toast.LENGTH_SHORT).show();
                 else {
                    Toast.makeText(context, "succes", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(sign_up.this,sign_in.class);
                    startActivity(intent);
                }
            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    public class Backgroundmedecinpatadd extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        Backgroundmedecinpatadd(Context cntx){
            context=cntx;
        }
        @Override
        protected String doInBackground(String... params) {

            String login_url="http://192.168.43.33/createmedpat.php";

            try {
                String nom=params[0];
                String prenom=params[1];
                String age=params[2];
                String email=params[3];
                String tel=params[4];
                String address=params[5];
                String password=params[6];
                String diabet=params[7];
                String tension=params[8];
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("nom","UTF-8")+"="+ URLEncoder.encode(nom,"UTF-8")+"&"
                        +URLEncoder.encode("prenom","UTF-8")+"="+URLEncoder.encode(prenom,"UTF-8")+"&"
                        +URLEncoder.encode("adress","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("telephone","UTF-8")+"="+URLEncoder.encode(tel,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"+URLEncoder.encode("diabet"
                        ,"UTF-8")+"="+URLEncoder.encode(diabet
                        ,"UTF-8")+"&"+URLEncoder.encode("tension"
                        ,"UTF-8")+"="+URLEncoder.encode(tension,"UTF-8");
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

            if (result == null) {
                Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
            } else {
                if (result.equals("user existe deja"))
                    Toast.makeText(context, "user existe deja", Toast.LENGTH_SHORT).show();
                else   {
                    Toast.makeText(context, "succes", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(sign_up.this,sign_in.class);
                    startActivity(intent);

                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
