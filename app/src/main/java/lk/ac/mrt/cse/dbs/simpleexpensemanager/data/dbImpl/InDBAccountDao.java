package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbImpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class InDBAccountDao implements AccountDAO {

    Context c;
    public InDBAccountDao(Context context) {
        c = context;
    }

    @Override
    public List<String> getAccountNumbersList() {
        String query = "SELECT account_no FROM account_table";
        DbHandler dbHandler = new DbHandler(c);
        Cursor cursor = dbHandler.read(query, null);

        List<String> accountNumbersList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            accountNumbersList.add(cursor.getString(cursor.getColumnIndex("account_no")));
        }
        cursor.close();
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        String query = "SELECT * FROM account_table";
        DbHandler dbHandler = new DbHandler(c);
        Cursor cursor = dbHandler.read(query, null);

        List<Account> accountNumbersList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Account account = new Account(
                    cursor.getString(cursor.getColumnIndex("account_no")),
                    cursor.getString(cursor.getColumnIndex("bank_name")),
                    cursor.getString(cursor.getColumnIndex("account_holder")),
                    cursor.getDouble(cursor.getColumnIndex("balance")));
            accountNumbersList.add(account);
        }
        cursor.close();
        return accountNumbersList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String query = "SELECT * FROM account_table WHERE account_no=?";
        String[] args = {accountNo};
        DbHandler dbHandler = new DbHandler(c);
        Cursor cursor = dbHandler.read(query, args);

        Account account = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            account = new Account(
                    cursor.getString(cursor.getColumnIndex("account_no")),
                    cursor.getString(cursor.getColumnIndex("bank_name")),
                    cursor.getString(cursor.getColumnIndex("account_holder")),
                    cursor.getDouble(cursor.getColumnIndex("balance")));
            break;
        }
        cursor.close();
        if (account == null) {
            String msg = "There's no such account with " + accountNo + " account number.";
            throw new InvalidAccountException(msg);
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        try {
            Object[] args = {account.getAccountNo(), account.getBankName(), account.getAccountHolderName(), account.getBalance()};
            String query = "INSERT INTO account_table (account_no, bank_name, account_holder, balance) VALUES(?, ?, ? ,?)";
            DbHandler dbHandler = new DbHandler(c);
            dbHandler.execute(query, args);
        } catch (Exception e){
            Log.e("Exception in adding ac", e.getMessage());
        }

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        Account account = getAccount(accountNo);
        try {
            Object[] args = {account.getAccountNo()};
            String query = "DELETE FROM account_table WHERE account_no=?";
            DbHandler dbHandler = new DbHandler(c);
            dbHandler.execute(query, args);
        } catch (Exception e) {
            Log.e("Exception in removing", e.getMessage());
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = getAccount(accountNo);

        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }

        try {
            String query = "UPDATE account_table SET balance=" + account.getBalance() + " WHERE account_no=\'" +
                    accountNo + "\'";
            DbHandler dbHandler = new DbHandler(c);
            dbHandler.execute(query);
        }  catch (Exception e){
            Log.e("exception in updating", e.getMessage());
        }

    }
}
