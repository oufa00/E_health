package com.example.oufa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class doctor_main extends AppCompatActivity {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    SessionManager session;
    String idmedecin = "";
    ListView listeView;
    String result1 = "";
    ContactAdapter_notifier contactAdapter;
    Button accept,reject;
    TextView test,notification;
    int ct=0;
    View navview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        listeView =(ListView) findViewById(R.id.list_doc);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_med);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_med);
        session = new SessionManager(getApplicationContext());
        if (session.checkLogin())
            finish();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.patient:
                        session.createLoginSession(idmedecin);
                        Intent intent = new Intent(doctor_main.this, consult_patient.class);
                        startActivity(intent);
                        break;
                    case R.id.setting_medecin:
                        session.createLoginSession(idmedecin);
                        Intent intent1 = new Intent(doctor_main.this, setting_medecin.class);
                        startActivity(intent1);
                        break;
                    case R.id.log_out_medecin:
                        session.logoutUser();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);

        HashMap<String, String> user = session.getUserDetails();
        idmedecin = user.get(SessionManager.KEY_ID);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Background_consult_notification background_consult_notification = new Background_consult_notification(this);
        background_consult_notification.execute(idmedecin);
        navview= navigationView.getHeaderView(0);

    }
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(doctor_main.this);
        View view1 = getLayoutInflater().inflate(R.layout.diag_verif_diab, (ViewGroup)findViewById(R.id.verif));
        accept=view1.findViewById(R.id.confirm);
        reject=view1.findViewById(R.id.reject);
        test=view1.findViewById(R.id.test);
        builder1.setView(view1);
        test.setText("Voulez vous deconnecter");
        final AlertDialog dialog2=builder1.create();
        dialog2.show();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(doctor_main.this,sign_in.class);
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


    public void liste() {
        contactAdapter = new ContactAdapter_notifier(this, R.layout.row_layout_notifier);
        listeView.setAdapter(contactAdapter);
        try {

            JSONObject jsonObject = new JSONObject(result1);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            int count = jsonArray.length()-1;
            String nom = "", prenom = "",contenu="",date="",vu="";
            while (count >= 0) {
                JSONObject jo = jsonArray.getJSONObject(count);
                nom = jo.getString("nom");
                prenom = jo.getString("prenom");
                contenu=jo.getString("contenu");
                vu=jo.getString("vu");
                if(vu.equals("0")){
                    ct++;
                }
                date=jo.getString("date");
                Contactnotification contacts = new Contactnotification(nom, prenom, contenu,vu,date);
                contactAdapter.add(contacts);
                count--;

            }
        } catch (JSONException e) {

        }
        notification=(TextView) navview.findViewById(R.id.notif11);
        notification.setText("Notification : "+ct);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
public class ContactAdapter_notifier extends ArrayAdapter {
    List lis=new ArrayList();
    public ContactAdapter_notifier(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(Contactnotification object) {
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
        final ContactAdapter_notifier.Contactholder contactholder;
        if(row==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.row_layout_notifier,parent,false);
            contactholder=new ContactAdapter_notifier.Contactholder();
            contactholder.nom=(TextView)row.findViewById(R.id.nom);
            contactholder.prenom=(TextView)row.findViewById(R.id.prenom);
            contactholder.contenu=(TextView)row.findViewById(R.id.contenu);
            contactholder.accept=(Button) row.findViewById(R.id.accept);
            contactholder.accept1=(Button) row.findViewById(R.id.accept1);

            row.setTag(contactholder);

        }
        else {
            contactholder=(ContactAdapter_notifier.Contactholder)row.getTag();
        }
        final Contactnotification contacts=(Contactnotification) this.getItem(position);

        //mzl machafch
     if(contacts.getVu().equals("0")) {

         contactholder.nom.setText(contacts.getNom());
         contactholder.prenom.setText(contacts.getPrenom());
         contactholder.contenu.setText(contacts.getContenu());
         contactholder.accept1.setVisibility(View.VISIBLE);
         contactholder.accept.setVisibility(View.INVISIBLE);
     }
        if(contacts.getVu().equals("1")) {

            contactholder.nom.setText(contacts.getNom());
            contactholder.prenom.setText(contacts.getPrenom());
            contactholder.contenu.setText(contacts.getContenu());
            contactholder.accept1.setVisibility(View.INVISIBLE);
            contactholder.accept.setVisibility(View.VISIBLE);
        }
        contactholder.accept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Backgroundvu backgroundvu=new Backgroundvu(getApplicationContext());
                backgroundvu.execute(idmedecin,contacts.getNom(),contacts.getPrenom(),contacts.getContenu(),contacts.getDate());



            }
        });





row.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(contacts.getContenu().equals("ajouter")){
        Toast.makeText(getContext(),"ç'est "+contacts.getNom()+" "+contacts.getPrenom()+" qui a ajouté vous dans sa liste dans :"
                +contacts.getDate()+"",Toast.LENGTH_SHORT).show();}
        if(contacts.getContenu().equals("partage")){
            Toast.makeText(getContext(),"ç'est "+contacts.getNom()+" "+contacts.getPrenom()+
                    " qui a demandé de consulter ces test :"+contacts.getDate()+"",Toast.LENGTH_LONG).show();
        }
       /* AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        if(contacts.getContenu().equals("ajouter")){
            dialog.setMessage("hada "+contacts.getNom()+contacts.getNom()+"A ajouter vous dans sa liste dans :"
                    +contacts.getDate()+"");
            }
        if(contacts.getContenu().equals("partage")){
            dialog.setMessage("hada "+contacts.getNom()+contacts.getNom()+
                    "demande a vous de consulter ces test de :"+contacts.getDate()+"");
          //  .setPositiveButton();
            }
            AlertDialog dialog1=dialog.create();
            dialog1.setTitle("notification");
        dialog1.show();*/
    }
});
        Animation animation= AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        row.startAnimation(animation);

        return row;
    }
    public class Contactholder{
        TextView nom,prenom,contenu;
        Button accept,accept1;
    }
}
    public class Backgroundvu extends AsyncTask<String,Void,String> {
        Context context;


        Backgroundvu(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/modifier_vu.php";

            try {
                String id = params[0];
                String nom = params[1];
                String prenom = params[2];
                String contenu = params[3];
                String date = params[4];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id"
                        ,"UTF-8")+"="+URLEncoder.encode(id
                        ,"UTF-8")+"&"+URLEncoder.encode("prenom"
                        ,"UTF-8")+"="+URLEncoder.encode(prenom
                        ,"UTF-8")+"&"+URLEncoder.encode("contenu"
                        ,"UTF-8")+"="+URLEncoder.encode(contenu
                        ,"UTF-8")+"&"+URLEncoder.encode("date"
                        ,"UTF-8")+"="+URLEncoder.encode(date
                        ,"UTF-8")+"&"+URLEncoder.encode("nom"
                        ,"UTF-8")+"="+URLEncoder.encode(nom
                        ,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
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
            if (result == null) {
                Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
            } else {


                Background_consult_notification background_consult_notification = new Background_consult_notification(context);
                background_consult_notification.execute(idmedecin);
                session.createLoginSession(idmedecin);
                Intent intent = new Intent(doctor_main.this, consult_patient.class);
                startActivity(intent);

            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }

    public class Background_consult_notification extends AsyncTask<String,Void,String> {
        Context context;


        Background_consult_notification(Context cntx) {
            context = cntx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.43.33/consult_notification.php";

            try {
                String id = params[0];


                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
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
            if (result == null) {
                Toast.makeText(context, "erreur de connexion", Toast.LENGTH_SHORT).show();
            } else {

                result1 = result;
                    liste();

                }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }

}

