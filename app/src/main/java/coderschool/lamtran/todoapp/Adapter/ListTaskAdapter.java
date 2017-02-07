package coderschool.lamtran.todoapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import coderschool.lamtran.todoapp.Model.TaskItem;
import coderschool.lamtran.todoapp.R;
import coderschool.lamtran.todoapp.Utils.Utils;

/**
 * Created by lamtran on 2/5/17.
 */

public class ListTaskAdapter extends ArrayAdapter<TaskItem> {

    private static class ViewHolder {
        TextView name;
        TextView priority;
    }

    ArrayList<TaskItem> arrTask;

    public ListTaskAdapter(Context context, ArrayList<TaskItem> tasks) {
        super(context, 0, tasks);

        arrTask = tasks;
    }

    @Nullable
    @Override
    public TaskItem getItem(int position) {
        return arrTask.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskItem task = getItem(position);

        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.item_task, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.tvtaskName);
            viewHolder.priority = (TextView) convertView.findViewById(R.id.tvPriority);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(task.name);
        viewHolder.priority.setText(Utils.getPriorityTitle(task.priority));

        return convertView;
    }


}
