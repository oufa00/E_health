package com.example.oufa.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

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


public class patient_main extends AppCompatActivity {
    ListView listview;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ContactAdapterpatient contactAdapter;
    @NotEmpty
    EditText nom,prenom;
    @NotEmpty
    @Length(min = 1 ,max =2)
    EditText age;
    String result1="";
    String idcompte="";
    SessionManager session;
    LinearLayout type;
    CheckBox diabet,tension;
    RadioGroup groupe;
    RadioButton t1,t2,t3;
    int buttonradion=1;
    Button accept,reject;
    TextView test;
    boolean valider1=false;
    com.mobsandgeeks.saripaar.Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        listview = (ListView) findViewById(R.id.list_pat);
        session = new SessionManager(getApplicationContext());

        if(session.checkLogin())
            finish();
        HashMap<String, String> user=session.getUserDetails();
        idcompte=user.get(SessionManager.KEY_ID);
        backgroundpatient backgroundpatient = new backgroundpatient(this);
        backgroundpatient.execute(idcompte);
        validator =new com.mobsandgeeks.saripaar.Validator(this);
        validator.setValidationListener(new com.mobsandgeeks.saripaar.Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                valider1=true;
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(patient_main.this);
        View view1 = getLayoutInflater().inflate(R.layout.diag_verif_diab, (ViewGroup)findViewById(R.id.verif));
        accept=view1.findViewById(R.id.confirm);
        reject=view1.findViewById(R.id.reject);
        test=view1.findViewById(R.id.test);
        builder1.setView(view1);
        final AlertDialog dialog2=builder1.create();
        test.setText("Voulez vous deconnecter");
        dialog2.show();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(patient_main.this,sign_in.class);
                startActivity(intent);
                dialog2.cancel();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
            }
        });

    }

    public void liste(){
        contactAdapter = new ContactAdapterpatient(this, R.layout.row_layoutpatient);
        listview.setAdapter(contactAdapter);
        try {

            jsonObject = new JSONObject(result1);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String nom = "", prenom = "",age="";
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                nom = jo.getString("nom");
                prenom = jo.getString("prenom");
                age=jo.getString("age");
                Contactspatient contacts = new Contactspatient(nom,prenom,age);
                contactAdapter.add(contacts);
                count++;

            }
        } catch (JSONException e) {


        }
    }
    public class backgroundpatient extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;

        backgroundpatient(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String jsonurl = "http://192.168.43.33/liste_patient.php";

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
 /*   public void check11(View v){
        groupe=(RadioGroup)v.findViewById(R.id.groupbutton1);
        int radioid=groupe.getCheckedRadioButtonId();
        buttonradio=(RadioButton)v.findViewById(radioid);
        //type 1
        if(buttonradio.getText().equals("Type 1")){
            Toast.makeText(getApplicationContext(),"type 1",Toast.LENGTH_SHORT).show();
            buttonradion=1;
        }
        //type 2
        if(buttonradio.getText().equals("Type 2")){
            Toast.makeText(getApplicationContext(),"type 2",Toast.LENGTH_SHORT).show();
            buttonradion=2;
        }
        //type 3
        if(buttonradio.getText().equals("Type 3")){
            Toast.makeText(getApplicationContext(),"type 3",Toast.LENGTH_SHORT).show();
            buttonradion=3;
        }
    }*/
    public void fab(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(patient_main.this);
        View view1 = getLayoutInflater().inflate(R.layout.nv_patient, (ViewGroup)findViewById(R.id.nvpatient));
        builder.setView(view1);
        final AlertDialog dialog1=builder.create();
        dialog1.show();
        Button valider;
        nom=(EditText)view1.findViewById(R.id.nom);
        prenom=(EditText)view1.findViewById(R.id.prenom);
        age=(EditText)view1.findViewById(R.id.age);
        valider=(Button)view1.findViewById(R.id.valide) ;
        diabet=(CheckBox)view1.findViewById(R.id.diabetcheck);
        tension=(CheckBox)view1.findViewById(R.id.tensioncheck);
        type=(LinearLayout)view1.findViewById(R.id.type);

    // check11(view1);
    diabet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!diabet.isChecked()) {
                type.setVisibility(View.GONE);
                t1.setChecked(false);
                t2.setChecked(false);
                t3.setChecked(false);
            }
            if ((tension.isChecked()) && (!diabet.isChecked())) type.setVisibility(View.GONE);
            if ((diabet.isChecked()) && (!tension.isChecked())) type.setVisibility(View.VISIBLE);
            if ((diabet.isChecked()) && (tension.isChecked())) type.setVisibility(View.VISIBLE);

        }
    });
    groupe = (RadioGroup) view1.findViewById(R.id.groupbutton1);
    t1 = (RadioButton) view1.findViewById(R.id.t1);
    t2 = (RadioButton) view1.findViewById(R.id.t2);
    t3 = (RadioButton) view1.findViewById(R.id.t3);
    groupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (t1.isChecked()) {
                buttonradion = 1;
            }
            if (t2.isChecked()) {
                buttonradion = 2;
            }
            if (t3.isChecked()) {
                buttonradion = 3;
            }
        }
    });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

                if (valider1 == true) {
                    if ((!diabet.isChecked()) && (!tension.isChecked())){
                        Toast.makeText(getApplicationContext(),"selectioner votre maladie",Toast.LENGTH_LONG).show();
                    }
                    else {

                        final String nnom = nom.getText().toString();
                        final String pprenom = prenom.getText().toString();
                        final String agee = age.getText().toString();
                        // hna bach yverifier ida rah sur m nom w prenom ta3 patient
                        AlertDialog.Builder alertDialog;
                        alertDialog = new AlertDialog.Builder(patient_main.this);
                        alertDialog.setMessage("nom: " + nnom + " prenom: " + pprenom + " age: " + agee).setCancelable(false)
                                .setPositiveButton("confirme", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Backgroundaddpatient backgroundaddpatient = new Backgroundaddpatient(patient_main.this);
                                        if ((diabet.isChecked()) && (tension.isChecked())) {
                                            backgroundaddpatient.execute(nnom, pprenom, agee, idcompte, buttonradion + "", "oui");
                                        }
                                        if ((diabet.isChecked()) && (!tension.isChecked())) {
                                            backgroundaddpatient.execute(nnom, pprenom, agee, idcompte, buttonradion + "", "non");
                                        }
                                        if ((!diabet.isChecked()) && (tension.isChecked())) {
                                            backgroundaddpatient.execute(nnom, pprenom, agee, idcompte, "non", "oui");
                                        }
                                        //zid fonction check11 ida kan sayade madar 7ta check f type ta3 diabete
                                        backgroundpatient backgroundpatient = new backgroundpatient(patient_main.this);
                                        backgroundpatient.execute(idcompte);
                                        dialog.cancel();
                                        dialog1.cancel();


                                    }
                                })
                                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        prenom.setText("");
                                        nom.setText("");
                                        age.setText("");
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alertDialog1 = alertDialog.create();
                        alertDialog1.setTitle("verification");
                        alertDialog1.show();
                    }
                }
            }
        });


    }
    public class ContactAdapterpatient extends ArrayAdapter {
        List lis=new ArrayList();
        public ContactAdapterpatient(@NonNull Context context, int resource) {
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
            final ContactAdapterpatient.Contactholder contactholder;
            if(row==null){
                LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=layoutInflater.inflate(R.layout.row_layoutpatient,parent,false);
                contactholder=new Contactholder();
                contactholder.tx_nom=(TextView)row.findViewById(R.id.tx_nom);
                contactholder.tx_prenom=(TextView)row.findViewById(R.id.tx_prenom);
                contactholder.tx_age=(TextView)row.findViewById(R.id.tx_age);
                row.setTag(contactholder);

            }
            else {
                contactholder=(ContactAdapterpatient.Contactholder)row.getTag();
            }
            Contactspatient contacts=(Contactspatient) this.getItem(position);
            contactholder.tx_nom.setText(contacts.getNom());
            contactholder.tx_prenom.setText(contacts.getPrenom());
            contactholder.tx_age.setText(contacts.getAge());
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Backgroundhomme backgroundhomme=new Backgroundhomme(patient_main.this);
                    backgroundhomme.execute(contactholder.tx_nom.getText().toString(),contactholder.tx_prenom.getText().toString());

                }
            });
            Animation animation= AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
            row.startAnimation(animation);
            return row;
        }
        public class Contactholder{
            TextView tx_nom,tx_prenom,tx_age;
        }
    }
    //hna bach najbade l id ta3 patient w ki nro7 bih l home patient
    public class Backgroundhomme extends AsyncTask<String,Void,String> {
        Context context;


        Backgroundhomme(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/id_patient_nom+prenom.php";

            try {

                String nom=params[0];
                String prenom=params[1];
                URL url=new URL(login_url);
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
            } else if(result.equals("no")) {}
            else{
                //hna nro7 home patient b l id ta3 patient
                session.createLoginSession1(result,idcompte);
                Intent i = new Intent(getApplicationContext(), home_patient.class);
                startActivity(i);
                finish();
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
    public class Backgroundaddpatient extends AsyncTask<String,Void,String> {
        Context context;


        Backgroundaddpatient(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/patientadd.php";

            try {

                String nom=params[0];
                String prenom=params[1];
                String age=params[2];
                String id=params[3];
                String diabet=params[4];
                String tension=params[5];
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("nom","UTF-8")+"="+ URLEncoder.encode(nom,"UTF-8")+"&"
                        +URLEncoder.encode("prenom","UTF-8")+"="+URLEncoder.encode(prenom,"UTF-8")+"&"
                        +URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"
                        +URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"
                        +URLEncoder.encode("diabet","UTF-8")+"="+URLEncoder.encode(diabet,"UTF-8")+"&"
                        +URLEncoder.encode("tension","UTF-8")+"="+URLEncoder.encode(tension,"UTF-8");
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
            } else {
                if (result.equals("ok")) {
                    Toast.makeText(context, "le patient est bien ajouter", Toast.LENGTH_SHORT).show();
                } else if (result.equals("patient existe deja"))
                    Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}


