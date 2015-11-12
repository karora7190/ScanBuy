package com.scanbuy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scanabook.database.BookDetails;
import com.scanabook.database.ScanABookDataSource;

import org.w3c.dom.Text;

public class SavedBooks extends AppCompatActivity {

    public ListView savedBooks;
    public ScanABookDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_books);
        savedBooks=(ListView)findViewById(R.id.listView2);
        try {
            dataSource = new ScanABookDataSource(SavedBooks.this);
            dataSource.open();
            ArrayAdapter<String> bookNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataSource.getAllBookNames());
            savedBooks.setAdapter(bookNames);
            dataSource.close();
            savedBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView bookDetails = (TextView)view;
                    String isbn = bookDetails.getText().toString().split(",")[1];
                    Intent intent=new Intent(SavedBooks.this,ViewBooks.class);
                    intent.putExtra("book_isbn",isbn);
                    startActivity(intent);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
