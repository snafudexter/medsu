package net.medsu.android.medsu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements Button.OnClickListener, RestHelper.RestListner{

    String sessid, session_name, token;

    RestHelper restHelper;
    ProgressDialog progressDialog;

    EditText mail, pass;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = (EditText) findViewById(R.id.txtMail);
        pass = (EditText) findViewById(R.id.txtPassword);
        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);

        //prepare for login on click
        HashMap hashMap = new HashMap();
        hashMap.put("mail", mail.getText());
        hashMap.put("pass", pass.getText());

        HashMap header = new HashMap();
        header.put("content-type", "application/x-www-form-urlencoded");

        restHelper = new RestHelper("https://" + getString(R.string.domain)+getString(R.string.login),hashMap, null);
        restHelper.setRestListner(this);

    }

    @Override
    public void callback(String json)
    {

        try
        {
            //get session id and name
            JSONObject jsonObject = new JSONObject(json);
            sessid = jsonObject.getString("sessid");
            session_name = jsonObject.getString("session_name");

            //prepare to get token
            HashMap header = new HashMap();
            header.put("Cookie", session_name + "=" + sessid);
            restHelper = new RestHelper("http://" +getString(R.string.domain) + getString(R.string.token), null, header);
            restHelper.restListner = new RestHelper.RestListner() {
                @Override
                public void callback(String json) {

                    token = json.trim();
                    Log.i("medsuInfo", "token: " + token);
                    if(progressDialog != null)
                    {
                        progressDialog.dismiss();
                    }


                    Bundle b = new Bundle();
                    b.putString("sessid", sessid);
                    b.putString("session_name", session_name);
                    b.putString("token", token);

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtras(b);
                    startActivity(i);
                    finish();

                }
            };
            restHelper.execute();
        }
        catch (Exception e)
        {
            Log.e("medsuErr", "LoginActivity_callback " + e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        login.setEnabled(false);
        restHelper.execute();
        progressDialog = ProgressDialog.show(this, "Please wait...", "Logging In!", true);
    }

}
