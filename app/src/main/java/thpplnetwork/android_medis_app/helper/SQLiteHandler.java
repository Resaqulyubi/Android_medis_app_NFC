/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package thpplnetwork.android_medis_app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import thpplnetwork.android_medis_app.model.Rekam;

public class SQLiteHandler extends SQLiteOpenHelper {

	private List<Rekam> rekamList = new ArrayList<Rekam>();

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 7;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// table name
	private static final String TABLE_USER = "user";
	private static final String TABLE_REKAM = "rekam";

	//  Table USER Columns names
	private static final String KEY_NIK = "nik";
	private static final String KEY_NAMA = "nama";
	private static final String KEY_JK= "jk";
	private static final String KEY_TGL_LAHIR= "tgl_lahir";
	private static final String KEY_UMUR= "umur";
	private static final String KEY_ALAMAT = "alamat";
	private static final String KEY_TELP = "telp";
	private static final String KEY_PASSWORD= "password";
	private static final String KEY_STATUS = "status";


	//Table REKAM MEDIS Column Names
	private static final String KEY_K_ID_REKAM= "ID_REKAM";
	private static final String KEY_K_NIK = "nik";
	private static final String KEY_K_ID_DOKTER= "id_dokter";
	private static final String KEY_K_TGL_PERIKSA = "tgl_periksa";
	private static final String KEY_K_ANJURAN = "anjuran";
	private static final String KEY_K_INDIKATOR = "indikator";
	private static final String KEY_K_HARI = "hari";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_NIK + " TEXT PRIMARY KEY,"
				+ KEY_NAMA+ " TEXT,"
				+ KEY_PASSWORD + " TEXT,"
				+ KEY_ALAMAT+ " TEXT,"
				+ KEY_JK+ " TEXT,"
				+ KEY_TELP+ " TEXT,"
				+ KEY_TGL_LAHIR+ " TEXT,"
				+ KEY_UMUR+ " TEXT,"
				+ KEY_STATUS + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);


		String CREATE_REKAM_TABLE = "CREATE TABLE " + TABLE_REKAM + "("
				+ KEY_K_ID_REKAM+ " TEXT PRIMARY KEY," + KEY_K_NIK+ " TEXT,"
				+ KEY_K_ID_DOKTER+ " TEXT," + KEY_K_TGL_PERIKSA+ " TEXT," + KEY_K_HARI+ " TEXT,"
				+ KEY_K_ANJURAN+ " TEXT," + KEY_K_INDIKATOR+ " TEXT "+")";
		db.execSQL(CREATE_REKAM_TABLE);



		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REKAM);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */


	public void addUser(String nik,String nama,String jk, String tgl_lahir, String umur,String alamat,String telp,String password,String status) {

		Log.d(TAG, "Masuk adduser");
		SQLiteDatabase db = this.getWritableDatabase();


		ContentValues values = new ContentValues();
		values.put(KEY_NIK, nik); // nama
		values.put(KEY_NAMA, nama); // nama
		values.put(KEY_JK, jk); // jk
		values.put(KEY_TGL_LAHIR, tgl_lahir); // tgl
		values.put(KEY_UMUR, umur); // password
		values.put(KEY_ALAMAT, alamat); // alamat
		values.put(KEY_TELP, telp); // telp
		values.put(KEY_PASSWORD, password); // password
		values.put(KEY_STATUS, status); // password


		// Inserting Row
		long id_ = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id_);
	}






	//Add REKAM
	public void addRekam(String id_rekam,String nik,String id_dokter, String tgl_periksa, String anjuran,String indikator,String hari) {

		Log.d(TAG, "Masuk addRekam");
		SQLiteDatabase db = this.getWritableDatabase();


		ContentValues values = new ContentValues();
		values.put(KEY_K_ID_REKAM,id_rekam); //
		values.put(KEY_K_NIK, nik); //
		values.put(KEY_K_ID_DOKTER, id_dokter); //
		values.put(KEY_K_TGL_PERIKSA, tgl_periksa); //
		values.put(KEY_K_ANJURAN, anjuran); //
		values.put(KEY_K_INDIKATOR, indikator); //
		values.put(KEY_K_HARI, hari); //


		// Inserting Row
		long id_ = db.insert(TABLE_REKAM, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New REKAM inserted into sqlite: " + id_);
	}



	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			Log.d(TAG, "Masuk fetching");
			user.put("nik", cursor.getString(0));
			user.put("nama", cursor.getString(1));
			user.put("password", cursor.getString(2));
			user.put("alamat", cursor.getString(3));
			user.put("jenis_kel", cursor.getString(4));
			user.put("telp", cursor.getString(5));
			user.put("tgl_lahir", cursor.getString(6));
			user.put("umur", cursor.getString(7));
		//	user.put("status", cursor.getString(8));


			//	user.put("nama", cursor.getString(9));

			//user.put("nama", cursor.getString());

		//	values.put(KEY_NIK,nik); // nama
		//	values.put(KEY_NAMA, nama); // nama
		//	values.put(KEY_JK, jk); // jk
		//	values.put(KEY_TGL_LAHIR, tgl_lahir); // tgl
		//	values.put(KEY_ALAMAT, alamat); // alamat
		//	values.put(KEY_TELP, telp); // telp
		//	values.put(KEY_PASSWORD, password); // password
		//	values.put(KEY_UMUR, umur); // password
		//	values.put(KEY_STATUS, status); // password

		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}





	/**
	 * Getting REKAM data from database
	 * */


	public HashMap<String, String> getRekamDetails() {
		HashMap<String, String> Rekam = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_REKAM;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {


			Log.d(TAG, "Masuk fetching REKAM");
			Rekam.put("id_rekam", cursor.getString(0));
			Rekam.put("nik", cursor.getString(1));
			Rekam.put("id_dokter", cursor.getString(2));
			Rekam.put("tgl_periksa", cursor.getString(3));
			Rekam.put("hari", cursor.getString(4));
			Rekam.put("anjuran", cursor.getString(5));
			Rekam.put("indikator", cursor.getString(6));



		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching REKAM from Sqlite: " + Rekam.toString());

		return Rekam;
	}


	//Select Max Date
	public HashMap<String, String> GetMaxDate() {
		HashMap<String, String> Rekam = new HashMap<String, String>();
		String selectQuery = "SELECT max(tgl_periksa) FROM " + TABLE_REKAM;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {


			Log.d(TAG, "Masuk fetching REKAM");
			Rekam.put("tgl_periksa", cursor.getString(0));



		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching REKAM from Sqlite: " + Rekam.toString());

		return Rekam;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

	public void deleteRekam() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_REKAM, null, null);
		db.close();

		Log.d(TAG, "Deleted all REKAM info from sqlite");
	}






}
