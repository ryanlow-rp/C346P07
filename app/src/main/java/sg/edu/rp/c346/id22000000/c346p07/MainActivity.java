package sg.edu.rp.c346.id22000000.c346p07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button btnAdd;
    Button btnClear;
    Button btnDelete;
    ListView listView;
    Spinner spinner;

    ArrayList<String> items;
    ArrayAdapter adapter;

    void hideKeyboard() {
        View tempView = getCurrentFocus();
        if (tempView != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(tempView.getWindowToken(), 0);
        }
    }

    void deleteItem(int index) {
        items.remove(index);
        adapter.notifyDataSetChanged();
        if (items.size() == 0) {
            btnDelete.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        btnAdd = findViewById(R.id.btnAdd);
        btnClear = findViewById(R.id.btnClear);
        btnDelete = findViewById(R.id.btnDelete);
        listView = findViewById(R.id.listView);
        spinner = findViewById(R.id.spinner);

        items = new ArrayList<String>();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        btnDelete.setEnabled(false);

        registerForContextMenu(listView);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.clear();
                adapter.notifyDataSetChanged();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String text = editText.getText().toString();
                editText.setText("");
                items.add(text);
                adapter.notifyDataSetChanged();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                int index = 0;
                try {
                    index = Integer.parseInt(editText.getText().toString());
                    editText.setText("");
                } catch (NumberFormatException nfe) {
                    editText.setText("");
                    CharSequence text = "Index needs to be a number";
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                    return;
                }

                if (index < 0 || index > (items.size() - 1)) {
                    CharSequence text = "Invalid index";
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                } else {
                    deleteItem(index);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        editText.setHint("Type in a new task here");
                        btnDelete.setEnabled(false);
                        btnAdd.setEnabled(true);
                        break;
                    case 1:
                        editText.setHint("Type in the index of the task to be removed");
                        btnDelete.setEnabled(true);
                        btnAdd.setEnabled(false);
                        if (items.size() == 0) {
                            btnDelete.setEnabled(false);
                            CharSequence text = "You don't have any task to remove";
                            Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0,0,"Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                deleteItem(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}