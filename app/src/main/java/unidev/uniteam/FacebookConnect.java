package unidev.uniteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FacebookConnect extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button projectButton;

    protected void onStart() {
        super.onStart();
        if (AccessToken.getCurrentAccessToken() != null) {
            loginButton.setVisibility(View.GONE);
            projectButton.setVisibility(View.VISIBLE);
        }
    }

    protected void onRestart() {
        super.onRestart();
        if (AccessToken.getCurrentAccessToken() != null) {
            loginButton.setVisibility(View.GONE);
            projectButton.setVisibility(View.VISIBLE);
        }
    }

    protected void onResume() {
        super.onResume();
        if (AccessToken.getCurrentAccessToken() != null) {
            loginButton.setVisibility(View.GONE);
            projectButton.setVisibility(View.VISIBLE);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook_connect);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        projectButton = (Button) findViewById(R.id.project_button);
        projectButton.setVisibility(View.INVISIBLE);
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent ProjectList = new Intent(FacebookConnect.this, ProjectList.class);
                startActivity(ProjectList);
            }

            @Override
            public void onCancel() {
                // TODO...
            }

            @Override
            public void onError(FacebookException e) {
                // TODO...
            }
        });

        projectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProjectList = new Intent(FacebookConnect.this, ProjectList.class);
                startActivity(ProjectList);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
