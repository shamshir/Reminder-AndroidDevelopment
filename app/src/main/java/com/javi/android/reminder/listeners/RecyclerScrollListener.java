package com.javi.android.reminder.listeners;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

public class RecyclerScrollListener extends RecyclerView.OnScrollListener {

    private FloatingActionButton fab;

    public RecyclerScrollListener(FloatingActionButton fab) {

        super();
        this.fab = fab;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        switch (newState) {

            case RecyclerView.SCROLL_STATE_IDLE:
                // Not Scrolling
                fab.show();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                // Scrolling
                fab.hide();
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                // List Running
                break;
        }
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        if (dx > 0) {
            // Scrolled Right
        } else if (dx < 0) {
            // Scrolled Left
        } else {
            // No Horizontal Scrolled
        }

        if (dy > 0) {
            // Scrolled Down
        } else if (dy < 0) {
            // Scrolled Up
        } else {
            // No Vertical Scrolled
        }
    }
}
