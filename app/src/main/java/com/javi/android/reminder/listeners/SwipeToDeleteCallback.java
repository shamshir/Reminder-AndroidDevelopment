package com.javi.android.reminder.listeners;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.javi.android.reminder.R;
import com.javi.android.reminder.TaskListFragment.TaskAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private TaskAdapter tAdapter;
    private Drawable icon;
    private ColorDrawable background;

    public SwipeToDeleteCallback(TaskAdapter adapter) {

        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.tAdapter = adapter;
        this.icon = ContextCompat.getDrawable(tAdapter.getContext(), R.drawable.ic_paperbin);
        this.background = new ColorDrawable(Color.RED);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();
        if (direction == 8) {
            tAdapter.editItem(position);
        } else if (direction == 4) {
            tAdapter.deleteItem(position);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 0;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + iconMargin;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Right Swipe

            this.icon = ContextCompat.getDrawable(tAdapter.getContext(), R.drawable.ic_edit);
            this.background = new ColorDrawable(Color.GREEN);

            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = iconLeft + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(
                    itemView.getLeft(),
                    itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom()
            );

        } else if (dX < 0) { // Left Swipe

            this.icon = ContextCompat.getDrawable(tAdapter.getContext(), R.drawable.ic_paperbin);
            this.background = new ColorDrawable(Color.RED);

            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(
                    itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom()
            );

        } else { // No Swipe

            background.setBounds(0, 0, 0, 0);

        }
        background.draw(c);
        icon.draw(c);
    }
}
