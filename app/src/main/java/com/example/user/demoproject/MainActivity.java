package com.example.user.demoproject;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.user.demoproject.databinding.ActivityMainBinding;
import com.example.user.demoproject.databinding.TodoRecyclerBinding;
import com.example.user.demoproject.model_classes.TaskModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SharedPreferences sharedPreferences;
    RecyclerView mRecyclerView;
    DbHelper dbHelper;
    LinearLayoutManager mLinearLayoutManager;
    TestAdapter testAdapter;
    private AppCompatActivity activity;
    ArrayList<TaskModel> arrayList = new ArrayList<>();

    //    private LoginModel loginModel;
    private ActivityMainBinding activityMainBinding;
    private int id_user;
    private String id_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setMain(this);

        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = activityMainBinding.recyclerTodo;
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id", -2);
        id_email = sharedPreferences.getString("email", null);
        mRecyclerView.setAdapter(testAdapter);
        dbHelper = new DbHelper(this);
        ArrayList<TaskModel> tasks = dbHelper.getTasks(id_user);
        TestAdapter mTestAdapter = new TestAdapter(tasks);
        mRecyclerView.setAdapter(mTestAdapter);
    }


    public void submit() {
        Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
        startActivityForResult(intent, 101);
    }

    public void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("All the user Related data and Database entries would be deleted ")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        sharedPreferences.edit().remove("id").apply();
                        startActivity(intent);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void launch() {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                DbHelper dbHelper = new DbHelper(this);
                ArrayList<TaskModel> tasks = dbHelper.getTasks(id_user);
                TestAdapter mTestAdapter = new TestAdapter(tasks);
                mRecyclerView.setAdapter(mTestAdapter);


            }
        }
    }


    private class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
        ArrayList<TaskModel> myList;

        public TestAdapter(ArrayList<TaskModel> list) {
            this.myList = list;

        }


        @Override
        public TestAdapter.TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.todo_recycler, parent, false);
            return new TestViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TestAdapter.TestViewHolder holder, int position) {
            final TaskModel task = myList.get(position);
            holder.binding.setTask(task);
            final CheckBox checkbox1 = holder.binding.checkbox;
            checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    task.setChecked(checkbox1.isChecked());
                    DbHelper dbHelper = new DbHelper(getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DbHelper.COLUMN_CHECKED, isChecked ? 1 : 0);
                    int update = sqLiteDatabase.update(DbHelper.TABLE_TASKS, contentValues, "id=" + task.getId(), null);
                    Log.d(TAG, "onCheckedChanged: " + update);
//                        checkbox1.setChecked(true);
//                        myList.remove(task);
//                        notifyDataSetChanged();

                }
            });

        }

        @Override
        public int getItemCount() {
            return myList.size();
        }

        public class TestViewHolder extends RecyclerView.ViewHolder {
            TodoRecyclerBinding binding;

            public TestViewHolder(View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);

            }
        }
    }

    @Override
    protected void onResume() {
        DbHelper dbHelper = new DbHelper(this);
        ArrayList<TaskModel> tasks = dbHelper.getTasks(id_user);
        TestAdapter mTestAdapter = new TestAdapter(tasks);
        mRecyclerView.setAdapter(mTestAdapter);
        super.onResume();
    }
}
