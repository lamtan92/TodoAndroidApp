package coderschool.lamtran.todoapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import coderschool.lamtran.todoapp.Model.TaskItem;

/**
 * Created by lamtran on 2/5/17.
 */

public class TodoItemDatabase extends SQLiteOpenHelper {

    private static TodoItemDatabase sInstance;
    public static final String DATABASE_NAME = "TodoItem.db";

    public static synchronized TodoItemDatabase getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new TodoItemDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    private TodoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table Task " +
                        "(id integer primary key, name text, dueDate text, priority integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Task");
        onCreate(db);
    }

    public long insertTaskItem(TaskItem taskItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        long taskID = -1;
        db.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", taskItem.name);
            contentValues.put("dueDate", taskItem.dueDate);
            contentValues.put("priority", taskItem.priority);
            taskID = db.insertOrThrow("Task", null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("Error:", "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }

        return taskID;
    }

    public void updateTask(TaskItem taskItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", taskItem.name);
        contentValues.put("dueDate", taskItem.dueDate);
        contentValues.put("priority", taskItem.priority);

        db.update("Task", contentValues, "id= ?", new String[]{Long.toString(taskItem.id)});

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public TaskItem getTaskByID(long taskID) {
        SQLiteDatabase db = this.getReadableDatabase();
        TaskItem newTask = new TaskItem();

        Cursor res = db.rawQuery("select * from Task where id=" + taskID + "", null);
        res.moveToFirst();

        if (res.getCount() > 0) {
            newTask.id = res.getInt(res.getColumnIndex("id"));
            newTask.name = res.getString(res.getColumnIndex("name"));
            newTask.dueDate = res.getString(res.getColumnIndex("dueDate"));
            newTask.priority = res.getInt(res.getColumnIndex("priority"));
        }

        return newTask;
    }

    public ArrayList<TaskItem> getAllItem() {
        ArrayList<TaskItem> arrTask = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Task Order By priority asc", null);

        try {
            if (res.moveToFirst()) {
                do {
                    TaskItem task = new TaskItem();
                    task.id = res.getInt(res.getColumnIndex("id"));
                    task.name = res.getString(res.getColumnIndex("name"));
                    task.dueDate = res.getString(res.getColumnIndex("dueDate"));
                    task.priority = res.getInt(res.getColumnIndex("priority"));
                    arrTask.add(task);
                } while (res.moveToNext());
            }
        } catch (Exception e) {
            Log.d("Error database", "Error while trying to get posts from database");
        } finally {
            if (res != null && !res.isClosed()) {
                res.close();
            }
        }

        return arrTask;
    }

    public Integer removeTask(TaskItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("Task", "id = ? ", new String[]{Long.toString(item.id)});
    }

}
