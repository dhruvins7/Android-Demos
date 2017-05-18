package com.example.user.demoproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.user.demoproject.databinding.ActivityListTasksBinding;
import com.example.user.demoproject.databinding.ListTasksRecyclerBinding;
import com.example.user.demoproject.model_classes.TaskModel;

import java.util.ArrayList;


public class ListTasksActivity extends AppCompatActivity {
    ActivityListTasksBinding activityListTasksBinding;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    TestAdapter testAdapter;
    private int id_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs =  getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        id_user = prefs.getInt("id",0);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 1);
        activityListTasksBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_list_tasks);
        activityListTasksBinding.setListtask(this);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = activityListTasksBinding.recyclerListTask;
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(testAdapter);
        DbHelper dbHelper = new DbHelper(this);

        ArrayList<TaskModel> list1 = (ArrayList<TaskModel>) dbHelper.getAllLabelsTasks(id);
        TestAdapter mTestAdapter = new TestAdapter(list1);
        mRecyclerView.setAdapter(mTestAdapter);

    }

    private class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {
        public ArrayList<TaskModel> myList;

        public TestAdapter(ArrayList<TaskModel> list1) {


            this.myList = list1;
        }

        @Override
        public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.list_tasks_recycler, parent, false);


            return new TestViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TestViewHolder holder, int position) {
            final TaskModel task = myList.get(position);
            holder.binding.checkboxList.setText(myList.get(position).getTask());
            final CheckBox checkbox1 = holder.binding.checkboxList;
            boolean checked = task.getChecked();
            if (checked) {
                checkbox1.setChecked(true);

            }else{
                checkbox1.setChecked(false);
            }

            checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    task.setChecked(checkbox1.isChecked());
                    DbHelper dbHelper = new DbHelper(getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DbHelper.COLUMN_CHECKED, isChecked ? 1 : 0);
                    int update = sqLiteDatabase.update(DbHelper.TABLE_TASKS, contentValues, "id="
                            + task.getId(), null);
                    Log.d("onCheckedChanged: " + update, "");
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
    }

    private class TestViewHolder extends RecyclerView.ViewHolder {
        ListTasksRecyclerBinding binding;

        public TestViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
