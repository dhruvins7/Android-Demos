package com.example.user.demoproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.demoproject.model_classes.ListModel;
import com.example.user.demoproject.model_classes.TaskModel;
import com.example.user.demoproject.model_classes.UserModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 08-05-2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "toDoDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_LISTS = "lists";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_LIST_ID = "list_id";
    public static final String COLUMN_LIST = "list";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_CHECKED = "checked";
    private String listCat;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//
//    @Override
//    public void onConfigure(SQLiteDatabase db) {
//        super.onConfigure(db);
//        db.setForeignKeyConstraintsEnabled(true);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = "CREATE TABLE " + TABLE_TASKS + " ( "
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK + " TEXT, "
                + COLUMN_CHECKED + " INTEGER " + " DEFAULT '0', "
                + COLUMN_LIST_ID + " INTEGER, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_USER_EMAIL + " TEXT " + " );";

        db.execSQL(CreateTable);

        String CreateListTable = "CREATE TABLE " + TABLE_LISTS + " ( "
                + COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LIST + " TEXT, "
                + COLUMN_USER_ID + " INTEGER " + " ); ";
        db.execSQL(CreateListTable);
        String CreateUserTable = " CREATE TABLE " + TABLE_USERS + " ( "
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_EMAIL + " TEXT " + " ); ";

        db.execSQL(CreateUserTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);

    }

    public long addUser(UserModel userModel) {
        SQLiteDatabase db = getWritableDatabase();
        int id = 0;
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.COLUMN_USER_NAME, userModel.getUserName());
            contentValues.put(DbHelper.COLUMN_USER_EMAIL, userModel.getUserEmail());
            id = (int) db.insert(TABLE_USERS, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "addUser: NOT SUCCESSFUL");
        } finally {
            db.endTransaction();
        }

        return id;
    }

    public int checkUser(String email) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE "
                + COLUMN_USER_EMAIL + " = ?"
                , new String[] {email} );

         if(cursor.getCount()>0){
             while (cursor.moveToNext()){
                 int id_user= cursor.getColumnIndex(COLUMN_USER_ID);
                 int id = cursor.getInt(id_user);
                 return id;

             }
             return 0;
         }else {
             return 0;

         }
    }

    public void addTask(TaskModel taskModel) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues content = new ContentValues();
            content.put(DbHelper.COLUMN_TASK, taskModel.getTask());
            content.put(DbHelper.COLUMN_LIST_ID, taskModel.getIdList());
            content.put(DbHelper.COLUMN_USER_ID, taskModel.getUserid());
            db.insert(DbHelper.TABLE_TASKS, null, content);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "addTask: ");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteData() {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            db.delete(TABLE_LISTS, null, null);
            db.delete(TABLE_TASKS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "deleteData: " + "NOT SUCCESSFUL");
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public ArrayList<TaskModel> getTasks(int userid_email) {
        ArrayList<TaskModel> taskList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(TABLE_TASKS,
//                new String[]{DbHelper.COLUMN_TASK_ID,
//                        DbHelper.COLUMN_TASK,
//                        DbHelper.COLUMN_CHECKED,
//                        DbHelper.COLUMN_LIST_ID,
//                        DbHelper.COLUMN_USER_ID,
//                        DbHelper.COLUMN_USER_EMAIL},
//                "user_id=?", new String []{String.valueOf(userid_email)}, null, null, null);
        Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_TASKS + " WHERE "
                + COLUMN_USER_ID + " = ? ", new String[]{String.valueOf(userid_email)});

        int idPos = cursor.getColumnIndex(DbHelper.COLUMN_TASK_ID);
        int namePos = cursor.getColumnIndex(DbHelper.COLUMN_TASK);
        int checkBoxStatus = cursor.getColumnIndex(DbHelper.COLUMN_CHECKED);
        int listIdPos = cursor.getColumnIndex(DbHelper.COLUMN_LIST_ID);
        int userid = cursor.getColumnIndex(DbHelper.COLUMN_USER_ID);
        int idEmail = cursor.getColumnIndex(DbHelper.COLUMN_USER_EMAIL);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(idPos);
            String task = cursor.getString(namePos);
            int checkStatus = cursor.getInt(checkBoxStatus);
            int idList = cursor.getInt(listIdPos);
            int idUser = cursor.getInt(userid);
            String emailUser = cursor.getString(idEmail);
            Cursor cursor_listname = db.rawQuery(" SELECT * FROM " + TABLE_LISTS
                    + " WHERE " + COLUMN_LIST_ID + " = "
                    + idList, null);
            TaskModel taskModel = new TaskModel();
            taskModel.setId(id);
            taskModel.setTask(task);
            if (checkStatus == 1) {
                taskModel.setChecked(true);
            }
            if (cursor_listname.moveToFirst()) {
                listCat = cursor_listname.getString(cursor_listname.getColumnIndex(COLUMN_LIST));
            }
            taskModel.setIdList(idList);
            taskModel.setList(listCat);
            Log.d(TAG, "getTasks: task: " + taskModel);
            Log.d(TAG, "getTasks: " + listCat);
            taskList.add(taskModel);

        }
        return taskList;
    }

    public List<ListModel> getAllLabels(int id) {
        List<ListModel> arrayList = new ArrayList<ListModel>();

        String selectQuery = " SELECT * FROM " + TABLE_LISTS + " WHERE " + COLUMN_USER_ID + " = " + id + "  ; ";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        int idPos = cursor.getColumnIndex(COLUMN_LIST_ID);
        int idUser = cursor.getColumnIndex(COLUMN_USER_ID);
        int listPos = cursor.getColumnIndex(COLUMN_LIST);
        while (cursor.moveToNext()) {
            ListModel listModel = new ListModel();
            int idList = cursor.getInt(idPos);
            String list = cursor.getString(listPos);
            int idUs = cursor.getInt(idUser);
            listModel.setId(idList);
            listModel.setList(list);
            listModel.setUser_id(idUs);
            arrayList.add(listModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayList;
    }

    public List<TaskModel> getAllLabelsTasks(int id) {
        List<TaskModel> arrayList = new ArrayList<>();

        String selectQuery = " SELECT * FROM " + TABLE_TASKS + " WHERE " +
                COLUMN_LIST_ID + " = " + id + "  ; ";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        int idPos = cursor.getColumnIndex(COLUMN_LIST_ID);
        int listPos = cursor.getColumnIndex(COLUMN_TASK);
        int namePos = cursor.getColumnIndex(COLUMN_TASK);
        int checkBoxStatus = cursor.getColumnIndex(COLUMN_CHECKED);
        while (cursor.moveToNext()) {
            TaskModel taskModel = new TaskModel();
            int idList = cursor.getInt(idPos);
            String list = cursor.getString(listPos);
            String task = cursor.getString(namePos);

            taskModel.setId(idList);
            taskModel.setTask(list);
            taskModel.setChecked(cursor.getInt(checkBoxStatus) == 1);

            arrayList.add(taskModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return arrayList;
    }

    public void addList(TaskModel taskModel) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues content = new ContentValues();
            content.put(DbHelper.COLUMN_LIST, taskModel.getTask());
            content.put(DbHelper.COLUMN_USER_ID, taskModel.getUserid());
            db.insert(DbHelper.TABLE_LISTS, null, content);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "addList: ");
        } finally {
            db.endTransaction();
        }
    }
}
