package coderschool.lamtran.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import coderschool.lamtran.todoapp.Model.TaskItem;
import coderschool.lamtran.todoapp.Utils.TodoItemDatabase;

public class EditItemActivty extends AppCompatActivity {

    private EditText etTaskName;

    private TextView tvDueTask;

    private Button btnSave, btnChangeDate;
    private Date currentDate;

    private Spinner spinnerPriority;
    private ArrayAdapter<String> adapterPriority;

    TodoItemDatabase databaseHelper;
    TaskItem task;

    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_activty);

        getSupportActionBar().show();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = TodoItemDatabase.getInstance(getApplicationContext());
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        btnSave = (Button) findViewById(R.id.btnSave);
        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

        etTaskName = (EditText) findViewById(R.id.etTaskName);
        tvDueTask = (TextView) findViewById(R.id.etDueDate);

        List<String> listPriority = new ArrayList<>();
        listPriority.add("HIGHT");
        listPriority.add("MEDIUM");
        listPriority.add("LOW");

        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        adapterPriority = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPriority);

        spinnerPriority.setAdapter(adapterPriority);

        Intent intent = getIntent();
        String action = intent.getAction();

        switch (action) {
            case "AddTask":
                getSupportActionBar().setTitle("Add new task");
                task = new TaskItem();
                btnSave.setText("Save");
                currentDate = new Date();
                tvDueTask.setText(dateFormat.format(currentDate));
                break;

            case "ViewTask":
                btnSave.setText("Update");
                btnSave.setVisibility(View.GONE);
                btnChangeDate.setVisibility(View.GONE);
                spinnerPriority.setEnabled(false);
                etTaskName.setFocusable(false);

                long id = intent.getLongExtra("TaskID", -1);

                task = databaseHelper.getTaskByID(id);
                etTaskName.setText(task.name);
                tvDueTask.setText(task.dueDate);
                spinnerPriority.setSelection(task.priority);

                try {
                    currentDate = dateFormat.parse(task.dueDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                getSupportActionBar().setTitle(task.name);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_action, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                btnSave.setVisibility(View.VISIBLE);
                btnChangeDate.setVisibility(View.VISIBLE);
                etTaskName.setFocusable(true);
                etTaskName.setFocusableInTouchMode(true);
                spinnerPriority.setEnabled(true);
                return true;

            case android.R.id.home:
                setResult(RESULT_OK, null);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSave(View v) {

        task.name = etTaskName.getText().toString();
        task.dueDate = tvDueTask.getText().toString();
        task.priority = spinnerPriority.getSelectedItemPosition();

        switch (btnSave.getText().toString()) {
            case "Save":
                databaseHelper.insertTaskItem(task);
                setResult(RESULT_OK, null);
                finish();
                break;

            case "Update":
                databaseHelper.updateTask(task);
                btnSave.setVisibility(View.GONE);
                btnChangeDate.setVisibility(View.GONE);
                etTaskName.setFocusable(false);
                getSupportActionBar().setTitle(task.name);
                spinnerPriority.setEnabled(false);
                break;
        }
    }

    public void onChangeDate(View v) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                currentDate = calendar.getTime();
                tvDueTask.setText(dateFormat.format(currentDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.show();
    }
}
