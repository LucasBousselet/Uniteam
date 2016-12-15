package unidev.uniteam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

public class FacebookConnect extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button projectButton;
    private String facebookUserID;

    /*
    * Shouldn't be necessary, to delete WHEN SURE !
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
    */

    protected void onResume() {
        super.onResume();
        if (AccessToken.getCurrentAccessToken() != null) {
            // TODO uncomment when ok
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
        loginButton.setReadPermissions("email");

        projectButton = (Button) findViewById(R.id.project_button);
        // TODO put back at the end of the Project
        projectButton.setVisibility(View.INVISIBLE);
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    JSONObject newUser = new JSONObject();

                                    String userId = object.getString("id");
                                    facebookUserID = userId;

                                    newUser.put("facebook", userId);
                                    newUser.put("token", AccessToken.getCurrentAccessToken());
                                    newUser.put("nom", object.getString("last_name"));
                                    newUser.put("prenom", object.getString("first_name"));
                                    newUser.put("email", object.getString("email"));
                                    newUser.put("created_at", null);
                                    newUser.put("updated_at", null);

                                    // TODO Uncomment when usng DB

                                    CreateNewUser("utilisateurs", newUser);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();

                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.connection_successful),
                        Toast.LENGTH_SHORT).show();

                Intent ProjectList = new Intent(FacebookConnect.this, ProjectList.class);
                startActivity(ProjectList);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.cancel_connection),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.connection_error),
                        Toast.LENGTH_SHORT).show();
            }
        });

        projectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProjectList = new Intent(FacebookConnect.this, ProjectList.class);
                ProjectList.putExtra("CurrentUser", facebookUserID);
                startActivity(ProjectList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_about) {
            Intent AboutPage = new Intent(FacebookConnect.this, AboutUs.class);
            startActivity(AboutPage);
        }
        return super.onOptionsItemSelected(item);
    }

    private void CreateNewUser(String url, JSONObject jsonObject) {
        DatabasePost ddbPost = new DatabasePost();
        ddbPost.execute(url, jsonObject.toString());
    }

    /**
     * Used to handle FacebookLogin Button callback
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
