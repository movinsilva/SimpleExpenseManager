package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbImpl;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class InDBTransactionDao implements TransactionDAO {

    Context c;

    public InDBTransactionDao(Context context){
        c = context;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        try {
            SimpleDateFormat date_formatter = new SimpleDateFormat("E, dd MMM yyyy");
            Object[] args = {date_formatter.format(date), expenseType.toString(), amount, accountNo};
            String query = "INSERT INTO transaction_table (date, type, amount, account_no) VALUES(?, ?, ?, ?)";
            DbHandler dbHandler = new DbHandler(c);
            dbHandler.execute(query, args);
        } catch (Exception e) {
            Log.e("Exception in logging", e.getMessage());
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        String query = "SELECT * FROM transaction_table";
        DbHandler dbHandler = new DbHandler(c);
        Cursor cursor = dbHandler.read(query, null);

        List<Transaction> transactions = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String dateString = cursor.getString(cursor.getColumnIndex("date"));
            try {
                SimpleDateFormat date_formatter = new SimpleDateFormat("E, dd MMM yyyy");
                Date date = date_formatter.parse(dateString);

                Transaction transaction = new Transaction(
                        date,
                        cursor.getString(cursor.getColumnIndex("account_no")),
                        ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex("type"))),
                        cursor.getDouble(cursor.getColumnIndex("amount")));
                transactions.add(transaction);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        String query = "SELECT * FROM transaction_table";
        DbHandler dbHandler = new DbHandler(c);
        Cursor cursor = dbHandler.read(query, null);

        List<Transaction> transactions = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String dateString = cursor.getString(cursor.getColumnIndex("date"));
            try {
                Date date = new SimpleDateFormat("E, dd MMM yyyy").parse(dateString);

                Transaction transaction = new Transaction(
                        date,
                        cursor.getString(cursor.getColumnIndex("account_no")),
                        ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex("type"))),
                        cursor.getDouble(cursor.getColumnIndex("amount")));
                transactions.add(transaction);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return transactions;
    }
}
