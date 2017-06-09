/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package thpplnetwork.android_medis_app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import thpplnetwork.android_medis_app.R;
import thpplnetwork.android_medis_app.app.AppConfig;
import thpplnetwork.android_medis_app.app.AppController;
import thpplnetwork.android_medis_app.helper.SQLiteHandler;
import thpplnetwork.android_medis_app.helper.SessionManager;

public class UserActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnkembali;
    private Button btnsimpan;
    private EditText inputnik;
    private EditText inputpasswordbaru;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        inputnik = (EditText) findViewById(R.id.txtnik);
        inputPassword = (EditText) findViewById(R.id.txtpassword);
        inputpasswordbaru = (EditText) findViewById(R.id.txtpasswordbaru);
        btnsimpan = (Button) findViewById(R.id.btnsimpan);
        btnkembali = (Button) findViewById(R.id.btnkembali);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String nik = user.get("nik");
       final   String password_db = user.get("password");

        // Displaying the user details on the screen
          inputnik.setText(nik);
         // txtEmail.setText(email);
        // Check if user is already logged in or not

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
         //   Intent intent = new Intent(UserActivity.this,
          //          MainActivity.class);
           // startActivity(intent);C
          //  finish();
        }

        // Register Button Click event
        btnsimpan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                String nik = inputnik.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String passwordbaru = inputpasswordbaru.getText().toString().trim();

                if (!nik.isEmpty() && !password.isEmpty() && !passwordbaru.isEmpty()) {


                    if (password_db.equals(password)) {

                        updateuser(nik, passwordbaru);

                    }else{
                        Log.e(TAG, "Updating Error: " + password_db);
                        Toast.makeText(getApplicationContext(),
                                "Password lama tidak sama !", Toast.LENGTH_LONG)
                                .show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Masih ada input yang kosong !", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Main Activity
        btnkembali.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        Button btnkeNFC = (Button)findViewById(R.id.btnsimpannfc);
        btnkeNFC.setOnClickListener(new View.OnClickListener(){
            // @Override
            public void onClick(View arg0) {
                startActivity(new Intent(UserActivity.this, Nfcdispatch.class));
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void updateuser(final String nik,final String passwordbaru) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_UPDATE_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        //Delete Data sql
                        db.deleteUsers();

                        session.setLogin(false);


                        // Now store the user in SQLite
                        String nik = jObj.getString("nik");

                        JSONObject user = jObj.getJSONObject("user");
                        String jk = user.getString("jk");
                        String tgl_lahir = user.getString("tgl_lahir");
                        String umur = user.getString("umur");
                        String alamat = user.getString("alamat");
                        String telp = user.getString("telp");
                        String password = user.getString("password");
                        String nama = user.getString("password");
                        String status= user.getString("password");

                        // Inserting row in users table
                        db.addUser(nik, nama, jk, tgl_lahir, umur, alamat, telp,password,status);


                        Toast.makeText(getApplicationContext(), "User successfully Update. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                UserActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Updating Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", nik);
                params.put("password", passwordbaru);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
