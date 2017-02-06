package coderschool.lamtran.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import coderschool.lamtran.todoapp.Adapter.ListTaskAdapter;
import coderschool.lamtran.todoapp.Model.TaskItem;
import coderschool.lamtran.todoapp.Utils.TodoItemDatabase;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;

    ArrayList<TaskItem> tasks;
    ListTaskAdapter taskAdapter;

    ListView lvItems;
    TodoItemDatabase databaseHelper;

    private final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load list item when app start
        databaseHelper = TodoItemDatabase.getInstance(this);
        lvItems = (ListView) findViewById(R.id.lvItems);

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

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
                databaseHelper.removeTask(taskAdapter.getItem(position));
                taskAdapter.remove(taskAdapter.getItem(position));
                taskAdapter.notifyDataSetChanged();
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

    // Read item from file
    private void readItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");

        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    //Write item to File
    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
