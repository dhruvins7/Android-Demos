package com.example.user.demoproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.demoproject.databinding.ActivityListBinding;
import com.example.user.demoproject.databinding.ListRecyclerBinding;
import com.example.user.demoproject.databinding.TodoRecyclerBinding;
import com.example.user.demoproject.model_classes.ListModel;
import com.example.user.demoproject.model_classes.TaskModel;

import java.util.ArrayList;

/**
 * Created by user on 11-05-2017.
 */

public class ListActivity extends AppCompatActivity {
    ActivityListBinding activityListBinding;
    RecyclerView mRecyclerView;
    TestAdapter testAdapter;
    LinearLayoutManager mLinearLayoutManager;
    private int id_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        activityListBinding.setList(this);
        SharedPreferences prefs =  getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        id_user = prefs.getInt("id",0);

        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = activityListBinding.recyclerList;
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(testAdapter);
        DbHelper dbHelper = new DbHelper(this);
        ArrayList<ListModel> list1 = (ArrayList<ListModel>) dbHelper.getAllLabels(id_user);
        TestAdapter mTestAdapter = new TestAdapter(list1);
        mRecyclerView.setAdapter(mTestAdapter);

    }

    public void submit() {

    }

    private class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {
        public ArrayList<ListModel> myList;

        public TestAdapter(ArrayList<ListModel> lists) {
            this.myList = lists;

        }

        @Override
        public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.list_recycler, parent, false);


            return new TestViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(TestViewHolder holder, int position) {
            final ListModel list = myList.get(position);
            holder.binding.setList(list);
            final int id = list.getId();
            holder.binding.textList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListActivity.this, ListTasksActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return myList.size();
        }
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        ListRecyclerBinding binding;

        public TestViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }
    }
}
