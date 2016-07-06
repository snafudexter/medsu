package net.medsu.android.medsu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements Button.OnClickListener, RestHelper.RestListner{

    RestHelper restHelper;

    EditText mail, pass;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = (EditText) findViewById(R.id.txtMail);
        pass = (EditText) findViewById(R.id.txtPassword);
        login = (Button) findViewById(R.id.btnLogin);

        //prepare for login on click
        HashMap hashMap = new HashMap();
        hashMap.put("mail", mail.getText());
        hashMap.put("pass", pass.getText());
        restHelper = new RestHelper(getString(R.string.domain)+getString(R.string.login),hashMap);
        restHelper.setRestListner(this);

    }

    @Override
    public void callback(String json)
    {
        //prepare to get token
    }

    @Override
    public void onClick(View v) {

    }

}
