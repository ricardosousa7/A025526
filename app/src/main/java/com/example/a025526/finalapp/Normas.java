package com.example.a025526.finalapp;

import android.app.ProgressDialog;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Thread.sleep;

/**
 * Created by A025526 on 20/01/2018.
 */

public class Normas extends EmpresasActivity implements View.OnClickListener{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normas);

        ImageView imageView = (ImageView) findViewById(R.id.pdf1);
        imageView.setOnClickListener(this);
        ImageView imageView2 = (ImageView) findViewById(R.id.pdf2);
        imageView2.setOnClickListener(this);
        ImageView imageView3 = (ImageView) findViewById(R.id.pdf3);
        imageView3.setOnClickListener(this);
        ImageView imageView4 = (ImageView) findViewById(R.id.pdf4);
        imageView4.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.pdf1:
                descarregarPDF();
                break;
            case R.id.pdf2:
                descarregarPDF();
                break;
            case R.id.pdf3:
                descarregarPDF();
                break;
            case R.id.pdf4:
                descarregarPDF();
                break;
        }
    }

    void descarregarPDF(){
        String urlDescarregar = "http://cosi.centimfe.com/apresentacoes/DECSIS-ISO27001.pdf";
        ProgressDialog progressDialog =  new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("A descarregar PDF ...");
        new DescarregarPDFAsyncTask(progressDialog).execute(urlDescarregar);
    }

    class DescarregarPDFAsyncTask extends AsyncTask<String ,Integer, String>{

        ProgressDialog progressDialog;
        DescarregarPDFAsyncTask(ProgressDialog progressDialog){
            this.progressDialog = progressDialog;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.show();
        }
        protected String doInBackground(String... urlPDF){
            String urlDescarregar = urlPDF[0];

            HttpURLConnection conexion = null;
            InputStream input = null;
            OutputStream output = null;


            String FicheiroGuardado = null;
            try {
                URL url = new URL(urlDescarregar);
                conexion = (HttpURLConnection) url.openConnection();
                conexion.connect();

                if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Fail";
                }

                input = conexion.getInputStream();
                FicheiroGuardado = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/DECSIS-ISO27001.pdf";

                int tamanhoFicheiro = conexion.getContentLength();

                output = new FileOutputStream(FicheiroGuardado);
                byte[] data = new byte[1024];
                int total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    sleep(10);
                    output.write(data, 0, count);
                    total += count;
                    publishProgress((int)(total + 100 / tamanhoFicheiro));
                }
            }
                catch(MalformedURLException e){
                    e.printStackTrace();
                    return "Erro:"+e.getMessage();
                } catch(IOException e){
                    e.printStackTrace();
                    return "Erro:"+e.getMessage();
                } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                    try {
                        if (input!=null) input.close();
                        if (output!=null) output.close();
                        if (conexion!=null) conexion.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            return "Sucesso";

        }
        //Toast.makeText(Normas.this, "YYY: " + FicheiroGuardado, Toast.LENGTH_LONG).show();

        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(values[0]);
        }
        protected void onPostExecute(String mensagem){
            super.onPostExecute(mensagem);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), mensagem,Toast.LENGTH_LONG).show();
        }
    }
}
