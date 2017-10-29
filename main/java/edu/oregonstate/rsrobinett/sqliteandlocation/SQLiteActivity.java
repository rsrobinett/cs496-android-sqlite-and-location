package edu.oregonstate.rsrobinett.sqliteandlocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static android.util.Log.*;

public class SQLiteActivity extends AppCompatActivity {

    private SQLiteDb mSQLiteDb;
    private SQLiteDatabase mSQLDB;
    private Cursor mCursor;
    private SimpleCursorAdapter mSQLCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        mSQLiteDb = new SQLiteDb(this);
        mSQLDB = mSQLiteDb.getWritableDatabase();

        boolean isSaveEvent = getIntent().getBooleanExtra("save", false);

        if(isSaveEvent) {
            Double newLongitude = getIntent().getDoubleExtra("longitude", Constants.DEFAULT_LONGITUDE);
            Double newLatitude = getIntent().getDoubleExtra("latitude", Constants.DEFAULT_LATITUDE);
            String newText = getIntent().getStringExtra("text");

            ContentValues valuesToSave = new ContentValues();
            valuesToSave.put(String.valueOf(DBContract.TextAndLocation.LATITUDE), newLatitude);
            valuesToSave.put(String.valueOf(DBContract.TextAndLocation.LONGITUDE), newLongitude);
            valuesToSave.put(DBContract.TextAndLocation.TEXT, newText);
            try {
                mSQLDB.insert(DBContract.TextAndLocation.TABLE_NAME, null, valuesToSave);
            } catch (Exception e) {
                d("DB Error", "Error Inserting In the Database");
            }
        }

        try {
            if(mSQLCursorAdapter != null && mSQLCursorAdapter.getCursor() != null)
            {
                if(!mSQLCursorAdapter.getCursor().isClosed()){
                    mSQLCursorAdapter.getCursor().close();
                }
            }
            mCursor = mSQLDB.query(DBContract.TextAndLocation.TABLE_NAME,
                    new String[]{DBContract.TextAndLocation._ID, DBContract.TextAndLocation.LATITUDE, DBContract.TextAndLocation.LONGITUDE, DBContract.TextAndLocation.TEXT},
                    null,
                    null,
                    null,
                    null,
                    DBContract.TextAndLocation._ID + " DESC");

            ListView SQLListView = (ListView) findViewById(R.id.sql_list_view);
            mSQLCursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.sql_item,
                    mCursor,
                    new String[]{DBContract.TextAndLocation.LATITUDE, DBContract.TextAndLocation.LONGITUDE, DBContract.TextAndLocation.TEXT},
                    new int[]{R.id.latitude_column, R.id.longitude_column, R.id.text_column},
                    0);
            SQLListView.setAdapter(mSQLCursorAdapter);
        }catch (Exception e){
            d("DB Error","Error loading data from Database");
        }
    }
}

final class DBContract {
    private DBContract(){};

    public final class TextAndLocation implements BaseColumns {
        public static final String DB_NAME = "sqlite";
        public static final String TABLE_NAME = "location";
        public static final String TEXT = "text";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final int DB_VERSION = 6;


        public static final String CREATE_TEXT_AND_LOCATION_TABLE = "CREATE TABLE " +
                TextAndLocation.TABLE_NAME + "(" + TextAndLocation._ID + " INTEGER PRIMARY KEY NOT NULL," +
                TextAndLocation.TEXT + " VARCHAR(255)," +
                TextAndLocation.LONGITUDE + " REAL," +
                TextAndLocation.LATITUDE + " REAL);";

        public  static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TextAndLocation.TABLE_NAME;
    }
}

class SQLiteDb extends SQLiteOpenHelper {

    public SQLiteDb(Context context) {
        super(context, DBContract.TextAndLocation.DB_NAME, null, DBContract.TextAndLocation.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.TextAndLocation.CREATE_TEXT_AND_LOCATION_TABLE);
/*
        ContentValues testValues = new ContentValues();
        testValues.put(String.valueOf(DBContract.TextAndLocation.LATITUDE), 42.00);
        testValues.put(String.valueOf(DBContract.TextAndLocation.LONGITUDE), -120.00);
        testValues.put(DBContract.TextAndLocation.TEXT, "Hello SQLite");
        db.insert(DBContract.TextAndLocation.TABLE_NAME,null,testValues);
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.TextAndLocation.SQL_DROP_TABLE);
        onCreate(db);
    }
}
