package thpplnetwork.android_medis_app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thpplnetwork.android_medis_app.R;
import thpplnetwork.android_medis_app.adapter.CustomListAdapter;
import thpplnetwork.android_medis_app.app.AppConfig;
import thpplnetwork.android_medis_app.helper.SQLiteHandler;
import thpplnetwork.android_medis_app.helper.SessionManager;
import thpplnetwork.android_medis_app.model.Rekam;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtName;
    private TextView txtEmail;

    private SQLiteHandler db;
    private SessionManager session;
    SwipeRefreshLayout mSwipeRefreshLayout;

    //FAB inisialisasi
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab0,fabijin;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;


    //listView

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // ListView json url
    private ProgressDialog pDialog;
    private List<Rekam> rekamList = new ArrayList<Rekam>();
    private ListView listView1;
    private CustomListAdapter adapter;
    final String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
      //  mSwipeRefreshLayout.setDistanceToTriggerSync(20);// in dips
        //mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used



        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();


        }else{
         // ReceiveselectALL();
           // ReceiveselectUpdate();
         //   ReceiveselectUpdate();

         //   SelectAllRekamSQL();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();


//    Log.e("CHECKKKKKK JK","jenis_kel:"+user.get("jenis_kel")+";");

       //final String nik_sql = user.get("nik");
       // String email = user.get("tgl_lahir");

        // Displaying the user details on the screen
        //   txtName.setText(name);
        //  txtEmail.setText(email);


        //instansiasi Widget
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab0 = (FloatingActionButton) findViewById(R.id.fab0);
        fabijin = (FloatingActionButton) findViewById(R.id.fabijin);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab0.setOnClickListener(this);
        fabijin.setOnClickListener(this);


        //List View

        listView1 = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, rekamList);
        listView1.setAdapter(adapter);


       mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
           }
       });

        listView1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    //    mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0);

                int topRowVerticalPosition = (listView1 == null || listView1.getChildCount() == 0) ? 0 : listView1.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });


        //RECEIVE DATA

        // Add to array list
        //ReceiveselectUpdate();
        //ReceiveselectUpdate();
        ReceiveselectALL();

    }


    void refreshItems() {
        // Load items
        // ...
        ReceiveselectUpdate();
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
      mSwipeRefreshLayout.setRefreshing(false);
    }
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        db.deleteRekam();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toUser() {

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        startActivity(intent);
        finish();
    }

    private void toijin() {

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, IjinActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab1:

                Log.d("Raj", "Fab 1");
                // Fetching user details from SQLite
             //   HashMap<String, String> user = db.getRekamDetails();
               // HashMap<String, String> user = db.GetMaxDate();
                ReceiveselectUpdate();

               SelectAllRekamSQL();
             //  ReceiveselectALL();

                break;
            case R.id.fabijin:

                Log.d("Raj", "Fab ijin");

                // Launching the IJIN activity
              toijin();
                finish();

                break;
            case R.id.fab2:

                Log.d("Raj", "Fab 2");
                // Launching the login activity
                toUser();
                finish();

                break;
            case R.id.fab0:

                Log.d("Raj", "Fab 0");
                logoutUser();
                break;

        }
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab0.startAnimation(fab_close);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fabijin.startAnimation(fab_close);
            fab0.setClickable(false);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fabijin.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab0.startAnimation(fab_open);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fabijin.startAnimation(fab_open);
            fab0.setClickable(true);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fabijin.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }




    //CustomRequestJson Update Rekam


    public class CustomJsonRequestupdate extends Request {

        HashMap<String, String> user = db.getUserDetails();
        HashMap<String, String> Rekam = db.GetMaxDate();

        final String nik_sql = user.get("nik");
        final String tgl_periksa = Rekam.get("tgl_periksa");


        Map<String, String> params2;
        private Response.Listener listener;

        public CustomJsonRequestupdate(int requestMethod, String url, Map<String, String> params2,
                                  Response.Listener responseListener2, Response.ErrorListener errorListener) {

            super(requestMethod, url, errorListener);


            params2 = new HashMap<String, String>();
            params2.put("nik", nik_sql);
            params2.put("tgl_periksa", tgl_periksa);


            Log.d("nik", nik_sql);
            Log.d("tgl_periksa", tgl_periksa);

            this.params2 = params2;
            this.listener = responseListener2;
        }

        @Override
        protected void deliverResponse(Object response) {
            listener.onResponse(response);

        }

        @Override
        public Map<String, String> getParams() throws AuthFailureError {

            return params2;
        }

        @Override
        protected Response parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }



    public void SelectAllRekamSQL(){
      //  db.deleteRekam();
        rekamList.clear();

        SQLiteDatabase db = this.db.getReadableDatabase();

        Cursor cur = db.rawQuery("SELECT * FROM " + "rekam"+" ORDER BY tgl_periksa DESC" , null);

        if(cur.getCount() != 0){
            cur.moveToFirst();

            do{
                String row_values = "";

                for(int i = 0 ; i < cur.getColumnCount(); i++){

                    row_values = " || "+"indikator :" + cur.getString(6)
                            +"anjuran :" + cur.getString(5)
                    ;


                }

                Rekam Rekam = new Rekam();

                Rekam.setAnjuran(cur.getString(5));
                Rekam.setNik(cur.getString(3));
                Rekam.setIndikator(cur.getString(6));
                Rekam.setTgl_periksa(cur.getString(4));

                // Rekam.setTgl_periksa(obj.getString("nama_dokter"));
                Log.d("aaaaaaaaa", "masuk loop");
                rekamList.add(Rekam);

                Log.d("LOG_TAG_HERE", row_values);

            }while (cur.moveToNext());

        }
        adapter.notifyDataSetChanged();
    }








    public void ReceiveselectUpdate(){

        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Check and GET Newest Data...");
        pDialog.show();

        Volley.newRequestQueue(MainActivity.this).add(
                new CustomJsonRequestupdate(Request.Method.POST, AppConfig.URL_UPDATEBYDATE_REKAM, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                hidePDialog();


                                //  JSONArray jaLocalstreams = response.getJSONArray("localstreams");
                                //parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {


                                        JSONObject obj = response.getJSONObject(i);
                                        //    JSONObject user = obj.getJSONObject("rekam");
                                        //   Rekam Rekam = new Rekam();
                                        //   Rekam.setAnjuran(obj.getString("anjuran"));
                                        // Rekam.setNik(obj.getString("tgl_periksa"));
                                        // Rekam.setIndikator(obj.getString("indikator"));
                                        // Rekam.setTgl_periksa(obj.getString("hari"));

                                        // Rekam.setTgl_periksa(obj.getString("nama_dokter"));
                                        Log.d("aaaaaaaaa", "masuk loop");
                                        //    rekamList.add(Rekam);

                                        db.addRekam(obj.getString("id_rekam"), obj.getString("nik"), obj.getString("id_dokter"), obj.getString("tgl_periksa"), obj.getString("anjuran"), obj.getString("indikator"), obj.getString("hari"));
//db.addRekam(obj.getString("id_rekam"),"b","c","d","e","f","g");
                                        // Inserting row in users table
                                        //    db.addRekam(obj.getString("id_rekam"),obj.getString("nik"),obj.getString("id_dokter"),obj.getString("id"), umur, alamat, telp,password,status);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                //   adapter.notifyDataSetChanged();
                                SelectAllRekamSQL();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " + error.getMessage());
                        hidePDialog();
                    }
                }) {


                    @Override
                    protected Response<JSONArray> parseNetworkResponse(
                            NetworkResponse response) {
                        try {
                            String jsonString = new String(response.data,
                                    HttpHeaderParser
                                            .parseCharset(response.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                });



    }

    public void ReceiveselectALL(){

        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Receiving Data...");
         pDialog.show();

        Volley.newRequestQueue(MainActivity.this).add(
                new CustomJsonRequest(Request.Method.POST, AppConfig.URL_SELECT_REKAM, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                hidePDialog();


                                //  JSONArray jaLocalstreams = response.getJSONArray("localstreams");
                                //parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {


                                        JSONObject obj = response.getJSONObject(i);
                                        //    JSONObject user = obj.getJSONObject("rekam");
                                        //   Rekam Rekam = new Rekam();
                                        //   Rekam.setAnjuran(obj.getString("anjuran"));
                                        // Rekam.setNik(obj.getString("tgl_periksa"));
                                        // Rekam.setIndikator(obj.getString("indikator"));
                                        // Rekam.setTgl_periksa(obj.getString("hari"));

                                        // Rekam.setTgl_periksa(obj.getString("nama_dokter"));
                                        Log.d("aaaaaaaaa", "masuk loop");
                                        //    rekamList.add(Rekam);

                                        db.addRekam(obj.getString("id_rekam"), obj.getString("nik"), obj.getString("id_dokter"), obj.getString("tgl_periksa"), obj.getString("anjuran"), obj.getString("indikator"), obj.getString("hari"));
//db.addRekam(obj.getString("id_rekam"),"b","c","d","e","f","g");
                                        // Inserting row in users table
                                        //    db.addRekam(obj.getString("id_rekam"),obj.getString("nik"),obj.getString("id_dokter"),obj.getString("id"), umur, alamat, telp,password,status);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                //   adapter.notifyDataSetChanged();
                                //  rekamList.clear();
                                 SelectAllRekamSQL();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " + error.getMessage());
                        hidePDialog();

                    }
                }) {


                    @Override
                    protected Response<JSONArray> parseNetworkResponse(
                            NetworkResponse response) {
                        try {
                            String jsonString = new String(response.data,
                                    HttpHeaderParser
                                            .parseCharset(response.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                });



    }

    public class CustomJsonRequest extends Request {

        HashMap<String, String> user = db.getUserDetails();

        final String nik_sql = user.get("nik");


        Map<String, String> params;
        private Response.Listener listener;

        public CustomJsonRequest(int requestMethod, String url, Map<String, String> params,
                                 Response.Listener responseListener, Response.ErrorListener errorListener) {

            super(requestMethod, url, errorListener);


            params = new HashMap<String, String>();
            params.put("nik", nik_sql);
            this.params = params;
            this.listener = responseListener;
        }

        @Override
        protected void deliverResponse(Object response) {
            listener.onResponse(response);

        }

        @Override
        public Map<String, String> getParams() throws AuthFailureError {

            return params;
        }

        @Override
        protected Response parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }


}

