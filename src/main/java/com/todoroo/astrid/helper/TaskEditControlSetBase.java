/**
 * Copyright (c) 2012 Todoroo Inc
 *
 * See the file "LICENSE" for the full license governing this code.
 */
package com.todoroo.astrid.helper;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.todoroo.astrid.data.Task;
import com.todoroo.astrid.ui.EditDialogOkBackground;

import org.tasks.R;

import static org.tasks.preferences.ResourceResolver.getData;

// --- interface

/**
 * Interface for working with controls that alter task data
 */
public abstract class TaskEditControlSetBase implements TaskEditControlSet {

    protected final Activity activity;
    private final int viewLayout;
    private View view;
    protected Task model;
    protected boolean initialized = false;
    protected final int themeColor;
    protected final int unsetColor;

    public TaskEditControlSetBase(Activity activity, int viewLayout) {
        this.activity = activity;
        this.viewLayout = viewLayout;
        if (viewLayout == -1) {
            initialized = true;
        }

        themeColor = getData(activity, R.attr.asTextColor);
        unsetColor = getData(activity, R.attr.asTextColorHint);
    }

    @Override
    public View getView() {
        if (view == null && !initialized) {
            if (viewLayout != -1) {
                view = LayoutInflater.from(activity).inflate(viewLayout, null);
                afterInflate();
                setupOkButton(view);
            }
            if (model != null) {
                readFromTaskOnInitialize();
            }
            this.initialized = true;
        }
        return view;
    }

    @Override
    public View getDisplayView() {
        return getView();
    }

    /**
     * Read data from model to update the control set
     */
    @Override
    public void readFromTask(Task task) {
        this.model = task;
        if (initialized) {
            readFromTaskOnInitialize();
        }
    }


    /**
     * Called once to setup the ui with data from the task
     */
    protected abstract void readFromTaskOnInitialize();

    /**
     * Write data from control set to model
     */
    @Override
    public void writeToModel(Task task) {
        if (initialized) {
            writeToModelAfterInitialized(task);
        }
    }

    /**
     * Write to model, if initialization logic has been called
     */
    protected abstract void writeToModelAfterInitialized(Task task);

    /**
     * Called when views need to be inflated
     */
    protected abstract void afterInflate();

    /**
     * Sets up ok button background. Subclasses can override to customize look and feel
     */
    protected void setupOkButton(View view) {
        Button ok = (Button) view.findViewById(R.id.edit_dlg_ok);
        Theme theme = activity.getTheme();
        TypedValue themeColor = new TypedValue();
        theme.resolveAttribute(R.attr.asThemeTextColor, themeColor, false);
        TypedValue inverseColor = new TypedValue();
        theme.resolveAttribute(R.attr.asTextColorInverse, inverseColor, false);

        if (ok != null) {
            ok.setBackgroundDrawable(EditDialogOkBackground.getBg(activity.getResources().getColor(themeColor.data)));
            int[][] states = new int[2][];
            states[0] = new int[] { android.R.attr.state_pressed };
            states[1] = new int[] { android.R.attr.state_enabled };
            int[] colors = new int[] { inverseColor.data, activity.getResources().getColor(themeColor.data) };
            ColorStateList csl = new ColorStateList(states, colors);
            ok.setTextColor(csl);
        }
    }
}
