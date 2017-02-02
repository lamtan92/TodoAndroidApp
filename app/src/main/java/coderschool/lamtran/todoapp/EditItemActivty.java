package coderschool.lamtran.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivty extends AppCompatActivity {

    private String taskName = "";
    private int taskPosition = -1;

    private EditText etTaskName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_activty);

        taskName = getIntent().getStringExtra("TaskName");
        taskPosition = getIntent().getIntExtra("TaskPosition", -1);
        etTaskName = (EditText) findViewById(R.id.etEditItem);
        etTaskName.setText(taskName);
    }

    public void onSave(View v) {
        taskName = etTaskName.getText().toString();

        Intent data = new Intent();
        data.putExtra("TaskName", taskName);
        data.putExtra("TaskPosition", taskPosition);

        setResult(RESULT_OK, data);
        finish();
    }
}
