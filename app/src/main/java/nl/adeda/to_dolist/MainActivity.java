package nl.adeda.to_dolist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DBManager dbManager;
    private ListView toDoList;
    private ListCursorAdapter cursorAdapter;
    private Button addTask;
    private EditText taskEditText;
    private TextView taskText;
    private int done;
    private TextView addNewTaskText;
    private int databaseLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskEditText = (EditText) findViewById(R.id.newTask);
        addTask = (Button) findViewById(R.id.addBtn);
        addNewTaskText = (TextView) findViewById(R.id.addNewTaskText);

        // Open database
        dbManager = new DBManager(this);
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = dbManager.fetch();
        cursorAdapter = new ListCursorAdapter(this, cursor);

        toDoList = (ListView) findViewById(R.id.todoList);
        toDoList.setAdapter(cursorAdapter);

        emptyListCheck();

        addTask.setOnClickListener(this);

        setItemListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }

    @Override
    public void onClick(View v) {
        String taskText = taskEditText.getText().toString();

        if (taskText.equals("")) {
            Toast.makeText(this, "Please enter a task.", Toast.LENGTH_SHORT).show();
            return;
        }

        dbManager.insert(taskText, 0);
        fetchCursor();

        // Empty textbox
        taskEditText.setText("");

        Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();

        // Check database length
        emptyListCheck();
    }

    public void fetchCursor() {
        Cursor cursor = dbManager.fetch();
        cursorAdapter.changeCursor(cursor);
    }

    public class ListCursorAdapter extends CursorAdapter {
        public ListCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView (Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        @Override
        public void bindView (View view, Context context, Cursor cursor) {
            taskText = (TextView) view.findViewById(R.id.taskText);
            String cursorText = cursor.getString(cursor.getColumnIndex("subject"));
            taskText.setText(cursorText);

            int done = cursor.getInt(cursor.getColumnIndex("done"));
            if (done == 1) {
                taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                taskText.setPaintFlags(0);
            }
        }
    }

    private void setItemListener() {
        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Delete record from list
                dbManager.delete(id);
                fetchCursor();
                emptyListCheck();
                Toast.makeText(MainActivity.this, "Task deleted.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) toDoList.getItemAtPosition(position);
                TextView selectedTask = (TextView) view.findViewById(R.id.taskText);
                String subject = cursor.getString(cursor.getColumnIndex("subject"));
                done = cursor.getInt(cursor.getColumnIndex("done"));
                if (done == 0) {
                    selectedTask.setPaintFlags(selectedTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    done = 1;
                } else {
                    selectedTask.setPaintFlags(0);
                    done = 0;
                }

                dbManager.update(id, subject, done);
                fetchCursor();
            }
        });
    }

    private void emptyListCheck() {
        databaseLength = dbManager.databaseLength;
        if (databaseLength == 0) {
            addNewTaskText.setVisibility(View.VISIBLE);
        } else {
            addNewTaskText.setVisibility(View.GONE);
        }
    }
}
