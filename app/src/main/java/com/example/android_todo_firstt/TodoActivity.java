package com.example.android_todo_firstt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TodoActivity extends AppCompatActivity {

    private static final int IS_SUCCESS = 0;
    private String[] mTodos;
    private int mTodoIndex = 0;
    int orientation;
    public static final String TAG = "TodoActivity";

    /* map or name, value pair to be returned in an intent */
    private static final String IS_TODO_COMPLETE = "com.example.isTodoComplete";

    /** In case of state change, due to rotating the phone
     * store the mTodoIndex to display the same mTodos element post state change
     * N.B. small amounts of data, typically IDs can be stored as key, value pairs in a Bundle
     * the alternative is to abstract view data to a ViewModel which can be in scope in all
     * Activity states and more suitable for larger amounts of data */

    private static final String TODO_INDEX = "com.example.todoIndex";

    /* override to write the value of mTodoIndex into the Bundle with TODO_INDEX as its key */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* check for saved state due to changes such as rotation
        and restore any saved state such as the TODO_INDEX */
        if (savedInstanceState != null){
            mTodoIndex = savedInstanceState.getInt(TODO_INDEX, 0);
        }

      /* call the super class onCreate to complete the creation
         of activity with state changes */
        super.onCreate(savedInstanceState);

        // set the user interface layout for this Activity
        setContentView(R.layout.activity_todo);

        Log.d(TAG, " **** Just to say the PC is in onCreate!");
        orientation = getResources().getConfiguration().orientation;

        // initialize member TextView so we can manipulate it later
        final TextView TodoTextView;
        TodoTextView = (TextView) findViewById(R.id.textViewTodo);

        // read the todo array from res/values/strings.xml
        Resources res = getResources();
        mTodos = res.getStringArray(R.array.todos);

        // display the first task from mTodo array in the TodoTextView
        TodoTextView.setText(mTodos[mTodoIndex]);

        Button buttonNext;
        Button buttonPrevious;
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonPrevious = (Button) findViewById(R.id.buttonPrev);

        // OnClick listener for the  Next button
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTodoIndex = (mTodoIndex + 1) % mTodos.length;
                TodoTextView.setText(mTodos[mTodoIndex]);
                setTextViewComplete("");
            }
        });

        Button buttonTodoDetail = (Button) findViewById(R.id.buttonTodoDetail);
        buttonTodoDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                /* Note, the child class being called has a static method determining the parameter
                   to be passed to it in the intent object */
                Intent intent = TodoDetailActivity.newIntent(TodoActivity.this, mTodoIndex);

                /* second param requestCode identifies the call as there could be many "intents" */
                startActivityForResult(intent, IS_SUCCESS);

                /* The result will return through
                   onActivityResult(requestCode, resultCode, Intent) method */

            }
        });



        // OnClick listener for the  Next button
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTodoIndex = (mTodoIndex - 1) % mTodos.length;

                if(mTodoIndex<0)
                {
                    mTodoIndex = mTodos.length-1;
                }
                TodoTextView.setText(mTodos[mTodoIndex]);
            }
        });
    }


    /* In case of state change, such as rotating the phone,
   store the mTodoIndex */

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(TODO_INDEX, mTodoIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == IS_SUCCESS ){
            if (intent != null) {
                // data in intent from child activity
                boolean isTodoComplete = intent.getBooleanExtra(IS_TODO_COMPLETE, false);
                updateTodoComplete(isTodoComplete);
            } else {
                Toast.makeText(this, R.string.back_button_pressed, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.request_code_mismatch,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void updateTodoComplete(boolean is_todo_complete) {

        final TextView textViewTodo;
        textViewTodo = (TextView) findViewById(R.id.textViewTodo);

        if (is_todo_complete) {
            textViewTodo.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.backgroundSuccess));
            textViewTodo.setTextColor(
                    ContextCompat.getColor(this, R.color.colorSuccess));

            setTextViewComplete("\u2713");
        }

    }

    private void setTextViewComplete( String message ){
        final TextView textViewComplete;

        textViewComplete = (TextView) findViewById(R.id.textViewComplete);
        textViewComplete.setText(message);

    }


    public void onCheckboxIsCompleteClicked(View view) {
    }
}