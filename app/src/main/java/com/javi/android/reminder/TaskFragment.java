package com.javi.android.reminder;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TaskFragment extends Fragment {

    private Task task;
    private EditText titleField;
    private EditText dateField;
    private EditText timeField;
    private RadioGroup priorityField;
    private CheckBox doneField;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID taskId = (UUID) getActivity().getIntent().getSerializableExtra("taskId");
        task = TaskCollection.get(getActivity()).getTask(taskId);
    }

    @Override
    public void onPause() {

        super.onPause();

        if (task.getTitle() == null) {
            TaskCollection.get(getActivity()).deleteTask(task);
        } else {
            TaskCollection.get(getActivity()).updateTask(task);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.task_fragment_layout, container, false);

        titleField = (EditText) v.findViewById(R.id.taskTitleField);
        titleField.setText(task.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {

                task.setTitle(cs.toString());
            }

            @Override
            public void afterTextChanged(Editable e) {

            }
        });

        dateField = (EditText) v.findViewById(R.id.taskDateField);
        updateDate();
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                FragmentManager manager = getFragmentManager();
                DatePickerDialog dialog = DatePickerDialog.newInstance(task.getDate());
                dialog.setTargetFragment(TaskFragment.this, 0);
                dialog.show(manager, "dialogDate");
            }
        });

        timeField = (EditText) v.findViewById(R.id.taskTimeField);
        updateTime();
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

                        task.setTime(selectedHour, selectedMinute);
                        updateTime();
                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        priorityField = (RadioGroup) v.findViewById(R.id.taskPriorityAnswers);
        switch (task.getPriority()) {
            case "ahigh":
                priorityField.check(R.id.taskPriorityHigh);
                break;
            case "bnormal":
                priorityField.check(R.id.taskPriorityNormal);
                break;
            case "clow":
                priorityField.check(R.id.taskPriorityLow);
                break;
        }
        priorityField.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                hideKeyboard();

                switch (checkedId) {
                    case R.id.taskPriorityHigh:
                        task.setPriority("ahigh");
                        break;
                    case R.id.taskPriorityNormal:
                        task.setPriority("bnormal");
                        break;
                    case R.id.taskPriorityLow:
                        task.setPriority("clow");
                        break;
                }
            }
        });

        doneField = (CheckBox) v.findViewById(R.id.taskDoneField);
        doneField.setChecked(task.isDone());
        doneField.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                hideKeyboard();

                task.setDone(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 0) {

            Date date = (Date) data.getSerializableExtra("date");
            task.setDate(date);
            updateDate();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_task, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteTask:
                TaskCollection.get(getActivity()).deleteTask(task);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateDate() {

        dateField.setText(task.getFormatedDate());
    }

    private void updateTime() {

        timeField.setText(task.getFormatedTime());
    }

    private void hideKeyboard() {

        Activity activity = getActivity();
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
