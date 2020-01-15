package com.example.oufa.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.List;

public class setting_medecin extends AppCompatActivity {
    LinearLayout opass,npass;
    @NotEmpty
    EditText nom,prenom,adress;
    @NotEmpty
    @Email
    EditText email;

    @NotEmpty
    @Length(min = 10,max =10)
    EditText telphone;
    @NotEmpty
    @Length(min = 1 ,max =2)
    EditText age;

    CheckBox pass;
    String idmedecin="",result1="",password1="";
    @NotEmpty
    @Length(min = 4,max =8)
    @Password(scheme = Password.Scheme.ALPHA_NUMERIC)
    EditText opassword,npassword;
    com.mobsandgeeks.saripaar.Validator validator;
    boolean valider=false;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_medecin);
        nom=(EditText)findViewById(R.id.nom);
        prenom=(EditText)findViewById(R.id.prenom);
        email=(EditText)findViewById(R.id.emailin);
        adress=(EditText)findViewById(R.id.adress);
        opassword=(EditText)findViewById(R.id.opass);
        npassword=(EditText)findViewById(R.id.npass);
        age=(EditText)findViewById(R.id.age);
        telphone=(EditText)findViewById(R.id.telephone);
        opass=(LinearLayout)findViewById(R.id.oldpass);
        npass=(LinearLayout)findViewById(R.id.newpass);
        pass=(CheckBox)findViewById(R.id.kaka);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        idmedecin = user.get(SessionManager.KEY_ID);
        pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    opass.setVisibility(View.VISIBLE);
                    npass.setVisibility(View.VISIBLE);
                }
                else{
                    opassword.setText("");
                    npassword.setText("");
                    opass.setVisibility(View.GONE);
                    npass.setVisibility(View.GONE);
                }
            }
        });
        background_setting background_setting=new background_setting(this);
        background_setting.execute(idmedecin);
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
    public void liste(){
        try {
            JSONObject jsonObject = new JSONObject(result1);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String nom1 = "", prenom1 = "",email1="",adress1="",age1="",telephone1="";
                JSONObject jo = jsonArray.getJSONObject(count);
                nom1 = jo.getString("nom");
                prenom1 = jo.getString("prenom");
                email1 = jo.getString("email");
                adress1 = jo.getString("adress");
                age1 = jo.getString("age");
                telephone1 = jo.getString("telephone");
                password1 = jo.getString("password");
                nom.setText(nom1);
                prenom.setText(prenom1);
                email.setText(email1);
                adress.setText(adress1);
                age.setText(age1);
                telphone.setText(telephone1);
        } catch (JSONException e) {
        }
    }

    public void editmed(View view) {
        String prenomm=prenom.getText().toString();
        String nomm=nom.getText().toString();
        String emaill=email.getText().toString();
        String agee=age.getText().toString();
        String telephonee=telphone.getText().toString();
        String adresse=adress.getText().toString();
        validator.validate();
        if(valider==true) {
            if(pass.isChecked()){
                String opasswordd1=opassword.getText().toString();
                String npasswordd1=npassword.getText().toString();
                if(npasswordd1.isEmpty()){
                    npassword.setError("invalid lenght");
                }
                else {
                    opasswordd1=emaill+opasswordd1;
                    opasswordd1=opasswordd1.hashCode()+"";
                    if (password1.equals(opasswordd1)) {
                        if(npasswordd1.contains(" ")){
                            npassword.setError("le caractère 'espace' est invalide");
                        }
                        else{
                        npasswordd1=emaill+npasswordd1;
                        npasswordd1=npasswordd1.hashCode()+"";
                        background_setting_mede background_setting_mede = new background_setting_mede(getApplicationContext());
                        background_setting_mede.execute(nomm, prenomm, agee, emaill, telephonee, adresse, npasswordd1, idmedecin);
                        pass.setChecked(false);
                        opassword.setText("");
                        npassword.setText("");
                        opass.setVisibility(View.GONE);
                        npass.setVisibility(View.GONE);}

                    } else {
                        opassword.setError("wrong password");
                    }
                }
            }
            else{
                background_setting_mede background_setting_mede=new background_setting_mede(getApplicationContext());
                background_setting_mede.execute(nomm,prenomm,agee,emaill,telephonee,adresse,password1,idmedecin);
            }
        }
    }
    public class background_setting_mede extends AsyncTask<String,Void,String> {
        Context context;
        background_setting_mede(Context cntx){
            context=cntx;
        }
        @Override
        protected String doInBackground(String... params) {

            String login_url="http://192.168.43.33/setting_med.php";

            try {
                String nom=params[0];
                String prenom=params[1];
                String age=params[2];
                String email=params[3];
                String tel=params[4];
                String address=params[5];
                String password=params[6];
                String id=params[7];
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
                        +URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"
                        +URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
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

                    Toast.makeText(context, "Vos changement ont été enregistrés", Toast.LENGTH_SHORT).show();
                background_setting background_setting=new background_setting(context);
                background_setting.execute(idmedecin);
            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class background_setting extends AsyncTask<String,Void,String> {
        Context context;


        background_setting(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String jsonurl = "http://192.168.43.33/get_info.php";


            try {
                String id=params[0];

                URL url=new URL(jsonurl);
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
                String result = "";
                String line="";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
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
}
