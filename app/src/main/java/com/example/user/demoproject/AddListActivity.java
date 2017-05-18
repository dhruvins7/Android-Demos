package com.example.user.demoproject;

import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.user.demoproject.DbHelper;
import com.example.user.demoproject.R;
import com.example.user.demoproject.databinding.ActivityAddListBinding;
import com.example.user.demoproject.model_classes.TaskModel;

/**
 * Created by user on 09-05-2017.
 */

public class AddListActivity extends AppCompatActivity {
    ActivityAddListBinding activityAddListBinding;
    DbHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private int id_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id", 0);
        activityAddListBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_list);
        activityAddListBinding.setAddlist(this);
        dbHelper = new DbHelper(this);

    }
    public void submit(){
        String list = activityAddListBinding.editList.getText().toString();
        TaskModel taskModel = new TaskModel();
        taskModel.setTask(list);
        taskModel.setUserid(id_user);
        dbHelper.addList(taskModel);
        setResult(RESULT_OK);
        finish();
    }
}
