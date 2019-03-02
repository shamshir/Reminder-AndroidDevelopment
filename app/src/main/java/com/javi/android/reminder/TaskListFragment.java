package com.javi.android.reminder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.javi.android.reminder.listeners.RecyclerScrollListener;
import com.javi.android.reminder.listeners.SwipeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskListFragment extends Fragment {

    private RecyclerView taskRecyclerView;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private String orderBy = "byPriority";
    private TaskAdapter taskAdapter;
    private boolean completedVisible;
    private TextView emptyText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.task_list_fragment_layout, container, false);

        taskRecyclerView = (RecyclerView) view.findViewById(R.id.taskListRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        orderBy = "byPriority";
                        updateUI(orderBy);
                        break;
                    case 1:
                        orderBy = "byDate";
                        updateUI(orderBy);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                TaskCollection.get(getActivity()).addTask(task);
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("taskId", task.getId());
                startActivity(intent);
            }
        });
        taskRecyclerView.addOnScrollListener(new RecyclerScrollListener(fab));

        emptyText = (TextView) view.findViewById(R.id.emptyText);

        if (savedInstanceState != null) {
            completedVisible = savedInstanceState.getBoolean("completedString");
        }

        updateUI(orderBy);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeListener(this.taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(orderBy);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("completedString", completedVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_task_list, menu);

        MenuItem completedItem = menu.findItem(R.id.showCompleted);
        if (completedVisible) {
            completedItem.setTitle(R.string.hide_completed);
        } else {
            completedItem.setTitle(R.string.show_completed);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.newTask:
                Task task = new Task();
                TaskCollection.get(getActivity()).addTask(task);
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("taskId", task.getId());
                startActivity(intent);
                return true;
            case R.id.showCompleted:
                completedVisible = !completedVisible;
                getActivity().invalidateOptionsMenu();
                updateCompleted();
                return true;
            case R.id.deleteCompletedTasks:
                TaskCollection.get(getActivity()).deleteCompletedTasks();
                updateUI(orderBy);
                return true;
            case R.id.createTestTasks:
                createTestTasks();
                updateUI(orderBy);
                return true;
            case R.id.deleteAllTasks:
                TaskCollection.get(getActivity()).deleteAllTasks();
                updateUI(orderBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateCompleted() {

        TaskCollection taskCollection = TaskCollection.get(getActivity());

        int totalTaskCount = taskCollection.getTasks(orderBy).size();
        int doneTaskCount = 0;
        for (Task task : taskCollection.getTasks(orderBy)) {
            if (task.isDone()) {
                doneTaskCount++;
            }
        }
        String completedString = doneTaskCount + " / " + totalTaskCount + " Tasks Completed";

        if (!completedVisible) {
            completedString = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(completedString);
    }

    private void updateUI(String orderBy) {

        TaskCollection taskCollection = TaskCollection.get(getActivity());
        List<Task> tasks = taskCollection.getTasks(orderBy);

        if (taskAdapter == null) {
            taskAdapter = new TaskAdapter(tasks);
            taskRecyclerView.setAdapter(taskAdapter);
        } else {
            taskAdapter.setTasks(tasks);
            taskAdapter.notifyDataSetChanged();
        }

        if (tasks.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }

        updateCompleted();
    }

    private void createTestTasks() {

        TaskCollection tasks = TaskCollection.get(getActivity());

        try {
            Task task1 = new Task();
            task1.setTitle("Lavar los platos");
            task1.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("20/02/2019"));
            task1.setPriority("ahigh");
            task1.setDone(false);
            Task task2 = new Task();
            task2.setTitle("Comer Pizza");
            task2.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("22/01/2019"));
            task2.setPriority("ahigh");
            task2.setDone(false);
            Task task3 = new Task();
            task3.setTitle("Hacer ejercicio");
            task3.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("22/01/2019"));
            task3.setPriority("bmedium");
            task3.setDone(false);
            Task task4 = new Task();
            task4.setTitle("Llamar a Bibiana");
            task4.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/01/2019"));
            task4.setPriority("bmedium");
            task4.setDone(true);
            Task task5 = new Task();
            task5.setTitle("Comprar cepillo de dientes");
            task5.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("20/01/2019"));
            task5.setPriority("bmedium");
            task5.setDone(false);
            Task task6 = new Task();
            task6.setTitle("Pasar apuntes a sucio");
            task6.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("03/01/2019"));
            task6.setPriority("clow");
            task6.setDone(false);
            Task task7 = new Task();
            task7.setTitle("Llevar a pasear al dragón");
            task7.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("22/01/2019"));
            task7.setPriority("clow");
            task7.setDone(false);
            Task task8 = new Task();
            task8.setTitle("Instalar una tarjeta gráfica peor");
            task8.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2019"));
            task8.setPriority("clow");
            task8.setDone(true);
            Task task9 = new Task();
            task9.setTitle("Arreglar la ducha");
            task9.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2019"));
            task9.setPriority("ahigh");
            task9.setDone(false);
            Task task10 = new Task();
            task10.setTitle("Comprar Robot");
            task10.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2019"));
            task10.setPriority("bmedium");
            task10.setDone(false);
            Task task11 = new Task();
            task11.setTitle("Estudiar Programación Android");
            task11.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2019"));
            task11.setPriority("clow");
            task11.setDone(false);

            tasks.addTask(task1);
            tasks.addTask(task2);
            tasks.addTask(task3);
            tasks.addTask(task4);
            tasks.addTask(task5);
            tasks.addTask(task6);
            tasks.addTask(task7);
            tasks.addTask(task8);
            tasks.addTask(task9);
            tasks.addTask(task10);
            tasks.addTask(task11);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /*##############################################################################################
    ##################################  TaskHolder Class  ##########################################
    ##############################################################################################*/

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Task task;
        private TextView titleTextView;
        private TextView dateTextView;
        private ImageView doneImageView;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.task_list_item_layout, parent, false));

            itemView.setOnClickListener(this);

            titleTextView = (TextView) itemView.findViewById(R.id.taskTitle);
            dateTextView = (TextView) itemView.findViewById(R.id.taskDate);
            doneImageView = (ImageView) itemView.findViewById(R.id.taskDone);
        }

        public void setItemData(Task rTask) {

            task = rTask;
            titleTextView.setText(task.getTitle());
            dateTextView.setText(task.getFormatedDate() + "    " + task.getFormatedTime());
            doneImageView.setVisibility(task.isDone() ? View.VISIBLE : View.GONE);
            switch (task.getPriority()) {
                case "ahigh":
                    titleTextView.setTextColor(Color.RED);
                    break;
                case "bmedium":
                    titleTextView.setTextColor(Color.YELLOW);
                    break;
                case "clow":
                    titleTextView.setTextColor(Color.GREEN);
                    break;
            }
        }

        @Override
        public void onClick(View view) {

            task.setDone(!task.isDone());
            TaskCollection.get(getContext()).updateTask(task);
            updateUI(orderBy);
        }
    }

    /*##############################################################################################
    ##################################  TaskAdapter Class  #########################################
    ##############################################################################################*/

    public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> tasks;
        private Task pendingDeleteTask;
        private int pendingDeleteTaskPosition;

        public TaskAdapter(List<Task> rTasks) {

            tasks = rTasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {

            Task task = tasks.get(position);
            holder.setItemData(task);
        }

        @Override
        public int getItemCount() {

            return tasks.size();
        }

        public void setTasks(List<Task> rTasks) {

            tasks = rTasks;
        }

        public Context getContext() {

            return getActivity();
        }

        public void editItem(int position) {

            Task task = tasks.get(position);
            Intent intent = new Intent(getActivity(), TaskActivity.class);
            intent.putExtra("taskId", task.getId());
            startActivity(intent);
        }

        public void deleteItem(int position) {

            pendingDeleteTask = tasks.get(position);
            pendingDeleteTaskPosition = position;
            tasks.remove(position);
            notifyItemRemoved(position);
            TaskCollection.get(getContext()).deleteTask(pendingDeleteTask);
            showUndoSnackbar();
        }

        private void showUndoSnackbar() {

            View view = getActivity().findViewById(R.id.taskListRecyclerView);
            Snackbar snackbar = Snackbar.make(view, R.string.snackbar_text, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.snackbar_action, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undoDelete();
                }
            });
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onShown(Snackbar snackbar) {
                    adjustFabMargin(64);
                }
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    adjustFabMargin(16);
                }
            });
            snackbar.show();
        }

        private void undoDelete() {

            tasks.add(pendingDeleteTaskPosition, pendingDeleteTask);
            notifyItemInserted(pendingDeleteTaskPosition);
            TaskCollection.get(getContext()).addTask(pendingDeleteTask);
        }

        private void adjustFabMargin(int dpMargin) {

            Resources r = getContext().getResources();
            int pxMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpMargin, r.getDisplayMetrics());

            ConstraintLayout.LayoutParams newParams = (ConstraintLayout.LayoutParams) fab.getLayoutParams();
            newParams.bottomMargin = pxMargin;
            fab.setLayoutParams(newParams);
        }
    }
}
