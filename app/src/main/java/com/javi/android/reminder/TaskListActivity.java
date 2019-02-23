package com.javi.android.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TaskListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        return new TaskListFragment();
    }
}
