package com.example.oufa.myapplication;

import android.content.Context;
import android.os.AsyncTask;
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
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by oufa on 24/04/2018.
 */

public class diabetadd extends AsyncTask<String,Void,String> {
    Context context;

    diabetadd(Context cntx){
        context=cntx;
    }
    @Override
    protected String doInBackground(String... params) {
        String login_url="http://192.168.43.33/diabadd.php";
        try {
            String glycem=params[0];
            String id_malade=params[1];
            String status=params[2];
            URL url=new URL(login_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data= URLEncoder.encode("glycem","UTF-8")+"="+URLEncoder.encode(glycem,"UTF-8")+"&"
                    +URLEncoder.encode("id_malade","UTF-8")+"="+URLEncoder.encode(id_malade,"UTF-8")+"&"
                    +URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8");
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
        else{

        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}


