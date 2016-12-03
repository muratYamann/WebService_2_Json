package com.yamankod.webservice_2_json;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;

    import org.apache.http.HttpResponse;
    import org.apache.http.client.HttpClient;
    import org.apache.http.client.methods.HttpPost;
    import org.apache.http.impl.client.DefaultHttpClient;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.widget.TextView;

/**
 *  Hocam Web Service1 çalışmıyor . yeni bir link verilirse çalısacaktır . 
 */


public class MainActivity extends Activity {
    private String URL = "http://10.2.10.10/webservice1/";
    private static final String TAG_USER = "user";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    JSONArray user = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyAsyncTask task= new MyAsyncTask();
        task.execute(URL);

    }
    private class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog mProgressDialog;
        private TextView textView = (TextView) findViewById(R.id.textView1);
        private String id;
        private String name;
        private String mail;
        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(MainActivity.this,
                    "Yukleniyor...", "veri Yuklenmesi İslemi Devam Ediyor...");
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject object = new JSONObject();
            try {
                String jsonResult = getUrlString(params[0]);
                object = new JSONObject(jsonResult);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return object;
        }
        @Override
        protected void onPostExecute(JSONObject result) {
            mProgressDialog.dismiss();
            try {
                if(result==null)
                {
                    textView.setText("Sunucu bağlantı hatası");
                }
                else{
                    user = result.getJSONArray(TAG_USER);
                    JSONObject c = user.getJSONObject(0);
                    id = c.getString(TAG_ID);
                    name = c.getString(TAG_NAME);
                    mail = c.getString(TAG_EMAIL);
                    textView.setText(id +" - " + name + " - " + mail);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private String getUrlString(String is) throws IllegalStateException, IOException {
        String ret = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                is);
        HttpResponse response = httpclient.execute(httppost);
        InputStream jsonResult = response.getEntity().getContent();
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(jsonResult,"UTF-8"));
        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine +"\n");
            }
            ret = answer.toString();
            jsonResult.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}












