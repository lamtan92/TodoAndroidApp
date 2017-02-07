package coderschool.lamtran.todoapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import coderschool.lamtran.todoapp.Model.TaskItem;
import coderschool.lamtran.todoapp.R;
import coderschool.lamtran.todoapp.Utils.DatabaseHelper;

/**
 * Created by lamtran on 2/7/17.
 */

public class EditTaskFragment extends DialogFragment {

    private EditText etTaskName;

    private TextView tvDueTask;

    private Button btnSave, btnChangeDate;
    private Date currentDate;

    private Spinner spinnerPriority;
    private ArrayAdapter<String> adapterPriority;

    DatabaseHelper databaseHelper;
    TaskItem task;

    SimpleDateFormat dateFormat;

    public EditTaskFragment() {

    }

    public static EditTaskFragment newInstance(int idTask) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle arg = new Bundle();
        arg.putInt("idTask", idTask);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit_item_activty, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        databaseHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnChangeDate = (Button) view.findViewById(R.id.btnChangeDate);

        etTaskName = (EditText) view.findViewById(R.id.etTaskName);
        tvDueTask = (TextView) view.findViewById(R.id.etDueDate);

        List<String> listPriority = new ArrayList<>();
        listPriority.add("HIGHT");
        listPriority.add("MEDIUM");
        listPriority.add("LOW");

        spinnerPriority = (Spinner) view.findViewById(R.id.spinnerPriority);
        adapterPriority = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listPriority);

        spinnerPriority.setAdapter(adapterPriority);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        int idTask = getArguments().getInt("idTask");
        if (idTask > 0) {
            task = databaseHelper.getTaskByID(idTask);
            etTaskName.setText(task.name);
            tvDueTask.setText(task.dueDate);
            spinnerPriority.setSelection(task.priority);

            try {
                currentDate = dateFormat.parse(task.dueDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            task = new TaskItem();
            btnSave.setText("Save");
            currentDate = new Date();
            tvDueTask.setText(dateFormat.format(currentDate));
        }
    }

}
