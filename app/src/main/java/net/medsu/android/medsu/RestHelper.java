package net.medsu.android.medsu;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by prabhjot on 6/17/2016.
 */
public class RestHelper extends AsyncTask<Void, Void, String>
{
    public interface RestListner{
        public void callback(String json);
    }

    void setRestListner(RestListner r)
    {
        restListner = r;
    }
    RestListner restListner;


    String url;
    HttpClient client;
    HttpPost post;
    List<NameValuePair> lParams;
    public RestHelper(String url, HashMap params, HashMap header)
    {
        this.url = url;

        client = new DefaultHttpClient();
        post = new HttpPost(url);

        //header
        if(header != null) {
            Set st = header.entrySet();
            Iterator i = st.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                post.setHeader(me.getKey().toString(), me.getValue().toString());

            }
        }

        //params
        if(params != null) {
            lParams = new ArrayList<>();
            Set set = params.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry me = (Map.Entry) iterator.next();
                lParams.add(new BasicNameValuePair(me.getKey().toString(), me.getValue().toString()));
            }
        }

    }

    @Override
    protected void onPostExecute(String s) {

        restListner.callback(s);
    }

    @Override
    protected String doInBackground(Void... params)
    {
        try {
            if(lParams != null)
                post.setEntity(new UrlEncodedFormEntity(lParams, HTTP.DEFAULT_CONTENT_CHARSET));

            HttpResponse response = client.execute(post);

            int responseCode = response.getStatusLine().getStatusCode();
            if(responseCode == 200)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }

                return stringBuilder.toString();
            }
            else
            {
                throw new Exception("ResponseCode: " + responseCode);
            }

        }
        catch (Exception e)
        {
            Log.e("medsuErr", "RestHelper" + e.getMessage());

        }
        return null;
    }
}


