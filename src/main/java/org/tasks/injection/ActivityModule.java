package org.tasks.injection;

import android.app.Activity;

import com.todoroo.astrid.actfm.TagSettingsActivity;
import com.todoroo.astrid.actfm.TagSettingsActivityTablet;
import com.todoroo.astrid.activity.BeastModePreferences;
import com.todoroo.astrid.activity.FilterShortcutActivity;
import com.todoroo.astrid.activity.ShareLinkActivity;
import com.todoroo.astrid.activity.TaskEditActivity;
import com.todoroo.astrid.activity.TaskListActivity;
import com.todoroo.astrid.calls.MissedCallActivity;
import com.todoroo.astrid.core.CustomFilterActivity;
import com.todoroo.astrid.core.DefaultsPreferences;
import com.todoroo.astrid.core.DeleteFilterActivity;
import com.todoroo.astrid.core.OldTaskPreferences;
import com.todoroo.astrid.files.AACRecordingActivity;
import com.todoroo.astrid.gcal.CalendarAlarmListCreator;
import com.todoroo.astrid.gcal.CalendarReminderActivity;
import com.todoroo.astrid.gtasks.GtasksPreferences;
import com.todoroo.astrid.gtasks.auth.GtasksLoginActivity;
import com.todoroo.astrid.reminders.ReminderPreferences;
import com.todoroo.astrid.tags.DeleteTagActivity;
import com.todoroo.astrid.tags.RenameTagActivity;
import com.todoroo.astrid.widget.WidgetConfigActivity;

import org.tasks.activities.ClearAllDataActivity;
import org.tasks.activities.ClearGtaskDataActivity;
import org.tasks.activities.DeleteAllCalendarEventsActivity;
import org.tasks.activities.DeleteCompletedActivity;
import org.tasks.activities.DeleteCompletedEventsActivity;
import org.tasks.activities.DonationActivity;
import org.tasks.activities.ExportTaskActivity;
import org.tasks.activities.ImportTaskActivity;
import org.tasks.activities.PurgeDeletedActivity;
import org.tasks.preferences.AppearancePreferences;
import org.tasks.preferences.BasicPreferences;
import org.tasks.preferences.HelpAndFeedbackActivity;
import org.tasks.preferences.MiscellaneousPreferences;
import org.tasks.reminders.SnoozeActivity;
import org.tasks.voice.VoiceCommandActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(addsTo = TasksModule.class,
        injects = {
                TaskListActivity.class,
                TaskEditActivity.class,
                ShareLinkActivity.class,
                TagSettingsActivity.class,
                TagSettingsActivityTablet.class,
                CustomFilterActivity.class,
                MissedCallActivity.class,
                CalendarAlarmListCreator.class,
                DeleteFilterActivity.class,
                CalendarReminderActivity.class,
                DeleteTagActivity.class,
                RenameTagActivity.class,
                VoiceCommandActivity.class,
                GtasksLoginActivity.class,
                WidgetConfigActivity.class,
                BasicPreferences.class,
                GtasksPreferences.class,
                OldTaskPreferences.class,
                FilterShortcutActivity.class,
                BeastModePreferences.class,
                DefaultsPreferences.class,
                AACRecordingActivity.class,
                SnoozeActivity.class,
                MiscellaneousPreferences.class,
                ImportTaskActivity.class,
                ExportTaskActivity.class,
                ClearAllDataActivity.class,
                PurgeDeletedActivity.class,
                DeleteCompletedActivity.class,
                DeleteCompletedEventsActivity.class,
                DeleteAllCalendarEventsActivity.class,
                ClearGtaskDataActivity.class,
                ReminderPreferences.class,
                HelpAndFeedbackActivity.class,
                DonationActivity.class,
                AppearancePreferences.class
        })
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Singleton
    @Provides
    public Activity getActivity() {
        return activity;
    }
}
