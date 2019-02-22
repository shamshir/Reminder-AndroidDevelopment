package com.javi.android.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TaskListActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_layout_with_fab);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainerFab);

        if (fragment == null) {
            fragment = new TaskListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerFab, fragment)
                    .commit();
        }

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fab);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Task task = new Task();
        TaskCollection.get(this).addTask(task);
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("taskId", task.getId());
        startActivity(intent);
    }
}
