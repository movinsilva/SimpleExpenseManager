package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbImpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DbHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "expenseDB";
    private static final String TABLE_NAME_ACCOUNT = "account_table";
    private static final String TABLE_NAME_TRANSACTION = "transaction_table";

    //column names of accounts table
    private static final String ID = "id";
    private static final String ACCOUNT_NO = "account_no";
    private static final String BANK_NAME = "bank_name";
    private static final String ACCOUNT_HOLDER = "account_holder";
    private static final String BALANCE = "balance";

    //column names of transactions table
    //ID is same as in accounts table
    private static final String DATE = "date";
    private static final String TYPE = "type";
    private static final String AMOUNT = "amount";
    //ACCOUNT_NO is same as in accounts table



    public DbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String table_account_create_query = "CREATE TABLE " + TABLE_NAME_ACCOUNT + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ACCOUNT_NO + " TEXT, " +
                BANK_NAME + " TEXT, " +
                ACCOUNT_HOLDER + " TEXT, " +
                BALANCE + " REAL);";

        String table_transaction_create_query = "CREATE TABLE " + TABLE_NAME_TRANSACTION + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE + " INTEGER, " +
                TYPE + " TEXT, " +
                AMOUNT + " REAL, " +
                ACCOUNT_NO + " TEXT, " +
                "FOREIGN KEY(" + ACCOUNT_NO + ") REFERENCES " + TABLE_NAME_ACCOUNT + "(" + ACCOUNT_NO + "));";

        db.execSQL(table_account_create_query);
        db.execSQL(table_transaction_create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String drop_table_query = "DROP TABLE IF EXISTS" ;

    }

    public void execute(String query, Object[] args){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query, args);
        db.close();
    }

    public Cursor read(String query, String[] args) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, args);
        db.close();
        return cursor;
    }



}
