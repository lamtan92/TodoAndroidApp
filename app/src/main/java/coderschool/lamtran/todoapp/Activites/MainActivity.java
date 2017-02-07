package coderschool.lamtran.todoapp.Activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import coderschool.lamtran.todoapp.Adapter.ListTaskAdapter;
import coderschool.lamtran.todoapp.Model.TaskItem;
import coderschool.lamtran.todoapp.R;
import coderschool.lamtran.todoapp.Utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    ArrayList<TaskItem> tasks;
    ListTaskAdapter taskAdapter;

    ListView lvItems;
    DatabaseHelper databaseHelper;

    private final int REQUEST_CODE = 10;

    public int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load list item when app start
        databaseHelper = DatabaseHelper.getInstance(this);
        lvItems = (ListView) findViewById(R.id.lvItems);

        tasks = databaseHelper.getAllItem();
        taskAdapter = new ListTaskAdapter(getApplicationContext(), tasks);

        lvItems.setAdapter(taskAdapter);

        setUpEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            tasks = databaseHelper.getAllItem();
            taskAdapter.clear();
            taskAdapter.addAll(tasks);
            taskAdapter.notifyDataSetChanged();
        }
    }

    private void setUpEvent() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Do you want delete " + taskAdapter.getItem(position).name + " ?")
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        databaseHelper.removeTask(taskAdapter.getItem(selectedPosition));
                                        taskAdapter.remove(taskAdapter.getItem(selectedPosition));
                                        taskAdapter.notifyDataSetChanged();
                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .create()
                        .show();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditItemActivty.class);
                intent.setAction("ViewTask");
                intent.putExtra("TaskID", taskAdapter.getItem(position).id);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.floatingAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditItemActivty.class);
                intent.setAction("AddTask");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

}
