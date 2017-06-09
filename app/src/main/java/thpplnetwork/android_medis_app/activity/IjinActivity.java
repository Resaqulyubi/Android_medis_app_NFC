/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package thpplnetwork.android_medis_app.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import thpplnetwork.android_medis_app.R;
import thpplnetwork.android_medis_app.app.AppConfig;
import thpplnetwork.android_medis_app.app.AppController;
import thpplnetwork.android_medis_app.helper.SQLiteHandler;
import thpplnetwork.android_medis_app.helper.SessionManager;

public class IjinActivity   extends Activity {
    private static final String TAG = IjinActivity.class.getSimpleName();
    private Button btnsend;
    private Button btnkembali;

    private EditText txttgltglmulai;
    private EditText txttglselesai;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    Calendar myCalendar = Calendar.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijin);

        txttglselesai = (EditText) findViewById(R.id.txttglselesai);
        txttgltglmulai= (EditText) findViewById(R.id.txttglmulai);
        btnsend= (Button) findViewById(R.id.btnsend);
         btnkembali = (Button) findViewById(R.id.btnkembali);


        // Progress dialog
      pDialog = new ProgressDialog(this);
       pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

        };



        txttgltglmulai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(IjinActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txttglselesai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(IjinActivity.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // Register Button Click event
        btnsend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String tglmulai = txttgltglmulai.getText().toString().trim();
                String tglselesai = txttglselesai.getText().toString().trim();

                if (!tglmulai.isEmpty() && !tglselesai.isEmpty() ) {

                    // HashMap<String, String> user = db.getUserDetails();
                    HashMap<String, String> user = db.getUserDetails();

                    final String nik = user.get("nik");

                    final String nama = user.get("nama");


                    //Get User detail from SQLLITE
                    Log.d(TAG, "nik dan nama: " + nik+nama);


                    insertIJIN(nik, nama, tglmulai,tglselesai);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
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
}

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void insertIJIN(final String nik, final String nama,
                              final String tgl_cuti ,final String tgl_selesai) {
        // Tag used to cancel the request
        String tag_string_req = "req_insert";

        pDialog.setMessage("Sending ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_IJIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                       // String id_cuti = jObj.getString("id_cuti");
                       // String nik= jObj.getString("nik");
                      //  JSONObject ijin = jObj.getJSONObject("ijin");
                      //  String name  = ijin.getString("name");
                      //  String email = ijin.getString("email");
                    //    String created_at = ijin.getString("created_at");

                        // Inserting row in users table
                        //  db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "Ijin Sudah Terkirim !", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                IjinActivity.this,
                                MainActivity.class);
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
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
                params.put("nama", nama);
                params.put("tgl_cuti", tgl_cuti);
                params.put("tgl_selesai", tgl_selesai);
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


    private void updateLabel1() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        txttgltglmulai.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        txttglselesai.setText(sdf.format(myCalendar.getTime()));
    }
}
