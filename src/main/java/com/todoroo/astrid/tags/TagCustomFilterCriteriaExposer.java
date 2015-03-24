/**
 * Copyright (c) 2012 Todoroo Inc
 *
 * See the file "LICENSE" for the full license governing this code.
 */
package com.todoroo.astrid.tags;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

import com.todoroo.andlib.sql.Criterion;
import com.todoroo.andlib.sql.Join;
import com.todoroo.andlib.sql.Query;
import com.todoroo.astrid.api.AstridApiConstants;
import com.todoroo.astrid.api.CustomFilterCriterion;
import com.todoroo.astrid.api.MultipleSelectCriterion;
import com.todoroo.astrid.api.TextInputCriterion;
import com.todoroo.astrid.dao.MetadataDao;
import com.todoroo.astrid.dao.TaskDao;
import com.todoroo.astrid.data.Metadata;
import com.todoroo.astrid.data.RemoteModel;
import com.todoroo.astrid.data.TagData;
import com.todoroo.astrid.data.Task;

import org.tasks.R;
import org.tasks.injection.InjectingBroadcastReceiver;

import javax.inject.Inject;

public class TagCustomFilterCriteriaExposer extends InjectingBroadcastReceiver {

    private static int[] default_tag_images = new int[] {
            R.drawable.default_list_0,
            R.drawable.default_list_1,
            R.drawable.default_list_2,
            R.drawable.default_list_3
    };
    private static final String IDENTIFIER_TAG_IS = "tag_is"; //$NON-NLS-1$
    private static final String IDENTIFIER_TAG_CONTAINS = "tag_contains"; //$NON-NLS-1$

    @Inject TagService tagService;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Resources r = context.getResources();

        CustomFilterCriterion[] ret = new CustomFilterCriterion[2];
        int j = 0;

        // built in criteria: tags
        {
            TagData[] tags = tagService.getGroupedTags(TagService.GROUPED_TAGS_BY_SIZE,
                            TaskDao.TaskCriteria.activeAndVisible());
            String[] tagNames = new String[tags.length];
            for(int i = 0; i < tags.length; i++) {
                tagNames[i] = tags[i].getName();
            }
            ContentValues values = new ContentValues();
            values.put(Metadata.KEY.name, TaskToTagMetadata.KEY);
            values.put(TaskToTagMetadata.TAG_NAME.name, "?");
            CustomFilterCriterion criterion = new MultipleSelectCriterion(
                    IDENTIFIER_TAG_IS,
                    context.getString(R.string.CFC_tag_text),
                    Query.select(Metadata.TASK).from(Metadata.TABLE).join(Join.inner(
                                Task.TABLE, Metadata.TASK.eq(Task.ID))).where(Criterion.and(
                            TaskDao.TaskCriteria.activeAndVisible(),
                            MetadataDao.MetadataCriteria.withKey(TaskToTagMetadata.KEY),
                            TaskToTagMetadata.TAG_NAME.eq("?"), Metadata.DELETION_DATE.eq(0))).toString(),
                    values, tagNames, tagNames,
                    ((BitmapDrawable)r.getDrawable(getDefaultImageIDForTag(RemoteModel.NO_UUID))).getBitmap(),
                    context.getString(R.string.CFC_tag_name));
            ret[j++] = criterion;
        }

        // built in criteria: tags containing X
        {
            CustomFilterCriterion criterion = new TextInputCriterion(
                            IDENTIFIER_TAG_CONTAINS,
                            context.getString(R.string.CFC_tag_contains_text),
                            Query.select(Metadata.TASK).from(Metadata.TABLE).join(Join.inner(
                                    Task.TABLE, Metadata.TASK.eq(Task.ID))).where(Criterion.and(
                                            TaskDao.TaskCriteria.activeAndVisible(),
                                            MetadataDao.MetadataCriteria.withKey(TaskToTagMetadata.KEY),
                                            TaskToTagMetadata.TAG_NAME.like("%?%"), Metadata.DELETION_DATE.eq(0))).toString(),
                                            context.getString(R.string.CFC_tag_contains_name), "",
                                            ((BitmapDrawable)r.getDrawable(getDefaultImageIDForTag(RemoteModel.NO_UUID))).getBitmap(),
                                            context.getString(R.string.CFC_tag_contains_name));
            ret[j] = criterion;
        }

        // transmit filter list
        Intent broadcastIntent = new Intent(AstridApiConstants.BROADCAST_SEND_CUSTOM_FILTER_CRITERIA);
        broadcastIntent.putExtra(AstridApiConstants.EXTRAS_RESPONSE, ret);
        context.sendBroadcast(broadcastIntent, AstridApiConstants.PERMISSION_READ);

    }

    private static int getDefaultImageIDForTag(String nameOrUUID) {
        if (RemoteModel.NO_UUID.equals(nameOrUUID)) {
            int random = (int)(Math.random()*4);
            return default_tag_images[random];
        }
        return default_tag_images[(Math.abs(nameOrUUID.hashCode()))%4];
    }
}
