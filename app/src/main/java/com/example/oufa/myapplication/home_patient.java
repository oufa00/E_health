package com.example.oufa.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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

public class home_patient extends AppCompatActivity {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    SessionManager session;
    String idpatient="",idcompte="";
    RadioGroup groupe,groupe1,groupe2;
    RadioButton buttonradio,t1,t2,t3;
    RadioButton avant,apres;
    int buttonradion=1,buttonradion1=1;
    String status="apres";
    Button adddiab,addtens;
    EditText glyc;
    String glyce="";
    String systolique="",diastolique="";
    TextView test;
    Button accept,reject;
    String type="";
    TextView diabet,tension;
    View navview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        session = new SessionManager(getApplicationContext());
        groupe=(RadioGroup)findViewById(R.id.groupbutton);

        if(session.checkLogin())
            finish();
       drawer =(DrawerLayout) findViewById(R.id.drawer_layout_pat);
        HashMap<String, String> user=session.getUserDetails();
        idpatient=user.get(SessionManager.KEY_ID);
        idcompte=user.get(SessionManager.KEY_IDCOMPTE);
       NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_doct:
                        session.createLoginSession(idpatient);
                        Intent intent=new Intent(home_patient.this,add_medecin.class);
                        startActivity(intent);
                        break;
                    case R.id.consulte_doct:
                        session.createLoginSession(idpatient);
                        Intent intent1=new Intent(home_patient.this,consult_medecin.class);
                        startActivity(intent1);
                        break;
                    case R.id.settings:
                        session.createLoginSession1(idpatient,idcompte);
                        Intent intent2=new Intent(home_patient.this,setting_patient.class);
                        startActivity(intent2);
                        break;
                    case R.id.addm:
                        if((type.equals("diabet=1 et tension=1"))|(type.equals("diabet=2 et tension=1"))|(type.equals("diabet=3 et tension=1"))){
                            Toast.makeText(getApplicationContext(),"Aucun maladie A ajouter",Toast.LENGTH_LONG).show();
                        }
                        if((type.equals("diabet=1 et tension=0"))|(type.equals("diabet=2 et tension=0"))|(type.equals("diabet=3 et tension=0"))){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(home_patient.this);
                            View view1 = getLayoutInflater().inflate(R.layout.diag_mal, (ViewGroup)findViewById(R.id.maladiee));
                            TextView verification=(TextView)view1.findViewById(R.id.verifier);
                            verification.setVisibility(View.VISIBLE);
                            verification.setText("Vous êtes sur d'ajouter la tension");
                            Button conf=view1.findViewById(R.id.conf);
                            Button ref=view1.findViewById(R.id.ref);
                            builder1.setView(view1);
                            final AlertDialog dialog1=builder1.create();
                            dialog1.show();
                            conf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Background_add_tension background_add_tension=new Background_add_tension(getApplicationContext());
                                    background_add_tension.execute(idpatient);
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    dialog1.cancel();
                                }
                            });
                            ref.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.cancel();
                                }
                            });

                        }
                        if(type.equals("diabet=0 et tension=1")){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(home_patient.this);
                            View view1 = getLayoutInflater().inflate(R.layout.diag_mal, (ViewGroup)findViewById(R.id.maladiee));
                            LinearLayout l1=view1.findViewById(R.id.type);
                            l1.setVisibility(View.VISIBLE);
                            groupe1=(RadioGroup)view1.findViewById(R.id.groupbutton1);
                            t1=(RadioButton)view1.findViewById(R.id.t1);
                            t2=(RadioButton)view1.findViewById(R.id.t2);
                            t3=(RadioButton)view1.findViewById(R.id.t3);
                            Button conf=view1.findViewById(R.id.conf);
                            Button ref=view1.findViewById(R.id.ref);
                            builder1.setView(view1);
                            final AlertDialog dialog1=builder1.create();
                            dialog1.show();
                            groupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    if(t1.isChecked()) {
                                        buttonradion1=1;}
                                    if(t2.isChecked()) {
                                        buttonradion1=2;}
                                    if(t3.isChecked()) {
                                        buttonradion1=3;}
                                }
                            });
                            conf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Background_add_diabet background_add_diabet=new Background_add_diabet(getApplicationContext());
                                    background_add_diabet.execute(idpatient,buttonradion1+"");
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    dialog1.cancel();

                                }
                            });
                            ref.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.cancel();
                                }
                            });

                        }
                        break;
                    case R.id.log_out:
                        session.logoutUser();

                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        toggle=new ActionBarDrawerToggle(this ,drawer ,R.string.open ,R.string.close);



        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        backgroundmaladie backgroundmaladie=new backgroundmaladie(this);
        backgroundmaladie.execute(idpatient);
        navview= navigationView.getHeaderView(0);


    }
    public void nav(){
        diabet=(TextView) navview.findViewById(R.id.diabet_pat11);
        tension=(TextView) navview.findViewById(R.id.tension_pat11);
        if (type.equals("diabet=1 et tension=0")) { diabet.setText("Diabete : type 1");  tension.setText("Tension : non");}
        if (type.equals("diabet=2 et tension=0")) { diabet.setText("Diabete : type 2");  tension.setText("Tension : non");}
        if (type.equals("diabet=3 et tension=0")) { diabet.setText("Diabete : type 3");  tension.setText("Tension : non");}

        if (type.equals("diabet=1 et tension=1")) { diabet.setText("Diabete : type 1"); tension.setText("Tension : oui");}
        if (type.equals("diabet=2 et tension=1")) { diabet.setText("Diabete : type 2"); tension.setText("Tension : oui");}
        if (type.equals("diabet=3 et tension=1")) { diabet.setText("Diabete : type 3"); tension.setText("Tension : oui");}

        if(type.equals("diabet=0 et tension=1"))  { diabet.setText("Diabete : non"); tension.setText("Tension : oui");}

    }
    public void onBackPressed() {
        Intent intent=new Intent(home_patient.this,patient_main.class);
        session.createLoginSession(idcompte);
        startActivity(intent);
    }
    public void control(View v){
        if(buttonradion==1){
            AlertDialog.Builder builder = new AlertDialog.Builder(home_patient.this);
            View view1 = getLayoutInflater().inflate(R.layout.diag_diabete, (ViewGroup)findViewById(R.id.diab));
            adddiab = (Button) view1.findViewById(R.id.add);
            glyc = (EditText) view1.findViewById(R.id.glycimie);
            groupe2=(RadioGroup)view1.findViewById(R.id.groupbutton1);
            avant=(RadioButton)view1.findViewById(R.id.av);
            apres=(RadioButton)view1.findViewById(R.id.ap);

            groupe2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(avant.isChecked()) {
                        status="avant";}
                    if(apres.isChecked()) {
                        status="apres";}
                }
            });
            builder.setView(view1);
            final AlertDialog dialog1=builder.create();
            dialog1.show();
            adddiab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(glyc.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(),"saisaie le champ",Toast.LENGTH_SHORT).show();}
                    else{
                        glyce = glyc.getText().toString();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(home_patient.this);
                        View view1 = getLayoutInflater().inflate(R.layout.diag_verif_diab, (ViewGroup)findViewById(R.id.verif));
                        accept=view1.findViewById(R.id.confirm);
                        reject=view1.findViewById(R.id.reject);
                        test=view1.findViewById(R.id.test);
                        builder1.setView(view1);
                        final AlertDialog dialog2=builder1.create();
                        dialog2.show();
                        if(Double.parseDouble(glyce)>5.0){
                            test.setText("glyc = "+glyce+" sur?????????!!!! exemple : 0.7 g/l");
                            test.setTextColor(Color.RED);
                        }else {
                            //diabet 1
                            if ((type.equals("diabet=1 et tension=1")) | (type.equals("diabet=1 et tension=0"))) {
                                if ((Double.parseDouble(glyce) >= 0.7) && (Double.parseDouble(glyce) <= 1.2) && (status == "avant")) {
                                    test.setText("glyc = " + glyce + " bien");
                                    test.setTextColor(Color.parseColor("#00ff00"));

                                }
                                if (((Double.parseDouble(glyce) < 0.7) |(Double.parseDouble(glyce) > 1.2) )&& (status == "avant")) {
                                    test.setText("glyc = " + glyce + " mauvais");
                                    test.setTextColor(Color.GREEN);

                                }
                                if ((Double.parseDouble(glyce) >= 0.7) && (Double.parseDouble(glyce) <= 1.6) && (status == "apres")) {
                                    test.setText("glyc = " + glyce + " bien");
                                    test.setTextColor(Color.GREEN);
                                }
                                if (((Double.parseDouble(glyce) < 0.7) | (Double.parseDouble(glyce) > 1.6)) && ((status == "apres") || (status == "avant"))) {
                                    test.setText("glyc = " + glyce + " mauvais");
                                    test.setTextColor(Color.RED);
                                }
                            }
                            //diabet 2
                            if ((type.equals("diabet=2 et tension=1")) | (type.equals("diabet=2 et tension=0"))) {
                                if ((Double.parseDouble(glyce) >= 0.7) && (Double.parseDouble(glyce) <= 1.2) && (status == "avant")) {
                                    test.setText("glyc = " + glyce + " bien");
                                    test.setTextColor(Color.GREEN);
                                }
                                if ((Double.parseDouble(glyce) >= 0.7) && (Double.parseDouble(glyce) <= 1.8) && (status == "apres")) {
                                    test.setText("glyc = " + glyce + " bien");
                                    test.setTextColor(Color.GREEN);
                                }
                                if (((Double.parseDouble(glyce) < 0.7) | (Double.parseDouble(glyce) > 1.8)) && ((status == "apres") || (status == "avant"))) {
                                    test.setText("glyc = " + glyce + " mauvais");
                                    test.setTextColor(Color.RED);
                                }
                                if (((Double.parseDouble(glyce) < 0.7) | (Double.parseDouble(glyce) > 1.2)) && (status == "avant")) {
                                    test.setText("glyc = " + glyce + " mauvais");
                                    test.setTextColor(Color.RED);
                                }
                            }
                            //diabet3
                            if ((type.equals("diabet=3 et tension=1")) | (type.equals("diabet=3 et tension=0"))) {
                                if ((Double.parseDouble(glyce) < 0.95) && (status == "avant")) {
                                    test.setText("glyc = " + glyce + " bien");
                                    test.setTextColor(Color.GREEN);
                                }
                                if ((Double.parseDouble(glyce) < 1.2) && (status == "apres")) {
                                    test.setText("glyc = " + glyce + " bien");
                                    test.setTextColor(Color.GREEN);
                                }
                                if (((Double.parseDouble(glyce) >= 0.95) && (status == "avant"))) {
                                    test.setText("glyc = " + glyce + " mauvais");
                                    test.setTextColor(Color.RED);
                                }
                                if (((Double.parseDouble(glyce) >= 1.2) && (status == "apres"))) {
                                    test.setText("glyc = " + glyce + " mauvais");
                                    test.setTextColor(Color.RED);
                                }
                            }
                        }

                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                diabet(glyce,status);
                                dialog2.cancel();
                                dialog1.cancel();
                            }
                        });
                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.cancel();
                                glyc.setText("");
                            }
                        });


                    }
                }
            });
        }
        if(buttonradion==0){
            final AlertDialog.Builder builder = new AlertDialog.Builder(home_patient.this);
            View view1 = getLayoutInflater().inflate(R.layout.diag_tension, (ViewGroup)findViewById(R.id.tension));
            setMin(view1);
            setMax(view1);
            builder.setView(view1);
            final AlertDialog dialog1=builder.create();
            dialog1.show();
            addtens=(Button)view1.findViewById(R.id.validtension);
            addtens.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(home_patient.this);
                    View view1 = getLayoutInflater().inflate(R.layout.diag_verif_diab, (ViewGroup)findViewById(R.id.verif));
                    accept=view1.findViewById(R.id.confirm);
                    reject=view1.findViewById(R.id.reject);
                    test=view1.findViewById(R.id.test);
                    builder1.setView(view1);
                    final AlertDialog dialog2=builder1.create();
                    dialog2.show();
                    int diastoliquei=Integer.parseInt(diastolique);
                    int systoliquei=Integer.parseInt(systolique);
                    if((systoliquei<13)&&(diastoliquei<9)){
                        test.setText("Systolique = " + systolique + " Diastolique ="+diastolique+"  ");

                    }
                    if((systoliquei<12)&&(diastoliquei<8)){
                        test.setText("Systolique = " + systolique + " Diastolique ="+diastolique+"  ");
                    }

                    if((systoliquei==13)||(systoliquei==14)||(diastoliquei==9)){
                        test.setText("Systolique = " + systolique + " Diastolique ="+diastolique+"  ");

                    }
                    if((systoliquei==15)||(diastoliquei==10)){
                        test.setText("Systolique = " + systolique + " Diastolique ="+diastolique+" ");

                    }
                    if((systoliquei==16)||(systoliquei==17)||(diastoliquei==11)){
                        test.setText("Systolique = " + systolique + " Diastolique ="+diastolique+" ");

                    }
                    if((systoliquei>=18)||(diastoliquei>=12)){
                        test.setText("Systolique = " + systolique + " Diastolique ="+diastolique+" ");

                    }
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backgroundaddtension backgroundaddtension=new backgroundaddtension(home_patient.this);
                            backgroundaddtension.execute(diastolique,systolique,idpatient);
                            dialog2.cancel();
                            dialog1.cancel();
                        }
                    });
                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.cancel();
                        }
                    });

                }
            });
        }
    }
    public void setMax(View view){

        Spinner max=(Spinner)view.findViewById(R.id.listmax);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,R.array.max,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        max.setAdapter(adapter1);
        max.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //najbad le max mn la liste spinner
                systolique=parent.getItemAtPosition(position)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void setMin(View view){

        Spinner min=(Spinner)view.findViewById(R.id.listmin);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,R.array.min,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        min.setAdapter(adapter1);
        min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //najbad le min mn la liste spinner
                diastolique=parent.getItemAtPosition(position)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void diabet(String gyc,String status){
        if(!gyc.isEmpty()){
            diabetadd diabeteadd=new diabetadd(this);
            diabeteadd.execute(gyc,idpatient,status);

        }
    }
    public void consult(View v){
        if(buttonradion==1){
            session.createLoginSession(idpatient);
            Intent intent=new Intent(home_patient.this,consult_diabet.class);
            startActivity(intent);
        }
        if(buttonradion==0){
            session.createLoginSession(idpatient);
            Intent intent=new Intent(home_patient.this,consult_tension.class);
            startActivity(intent);
        }
    }

    public void check1(View v){

        int radioid=groupe.getCheckedRadioButtonId();
        buttonradio=(RadioButton)findViewById(radioid);
        //patient
        if(buttonradio.getText().equals("Diabete")){
            buttonradion=1;
        }
        //medecin
        if(buttonradio.getText().equals("Tension")){
            buttonradion=0;
        }
    }
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)){
        }
return true;
    }
    public class backgroundmaladie extends AsyncTask<String,Void,String> {
        Context context;


        backgroundmaladie(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/maladie.php";

            try {

                String id=params[0];

                URL url=new URL(login_url);
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
            type = result;
            if (result == null) {
                Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
            } else{ nav();
                if ((result.equals("diabet=1 et tension=1")) || (result.equals("diabet=2 et tension=1")) || (result.equals("diabet=3 et tension=1"))) {
                groupe.setVisibility(View.VISIBLE);
            } else {

                if ((result.equals("diabet=1 et tension=0")) || (result.equals("diabet=2 et tension=0")) || (result.equals("diabet=3 et tension=0"))) {
                    groupe.setVisibility(View.INVISIBLE);
                    buttonradion = 1;
                }
                if (result.equals("diabet=0 et tension=1")) {
                    groupe.setVisibility(View.INVISIBLE);
                    buttonradion = 0;
                }
            }

        }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
    public class Background_add_tension extends AsyncTask<String,Void,String> {
        Context context;


        Background_add_tension(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/add_tension.php";

            try {

                String id=params[0];

                URL url=new URL(login_url);
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
            type=result;
            if (result == null) {
                Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "succes", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
    public class Background_add_diabet extends AsyncTask<String,Void,String> {
        Context context;


        Background_add_diabet(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/add_diabet.php";

            try {

                String id=params[0];
                String type=params[1];

                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("id","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8")+"&"
                        +URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");
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
            type=result;
            if (result == null) {
                Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
