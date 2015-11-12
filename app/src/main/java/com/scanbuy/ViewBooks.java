package com.scanbuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.scanabook.database.BookDetails;
import com.scanabook.database.ScanABookDataSource;

import org.w3c.dom.Text;

public class ViewBooks extends AppCompatActivity {

    TextView bookName;
    TextView authorName;
    TextView totalPages;
    CheckBox read;
    ScanABookDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);
        String myBook = getIntent().getExtras().getString("book_isbn");
        Toast.makeText(ViewBooks.this,myBook,Toast.LENGTH_LONG).show();
        bookName=(TextView)findViewById(R.id.textView11);
        authorName=(TextView)findViewById(R.id.textView13);
        totalPages=(TextView)findViewById(R.id.textView15);
        read=(CheckBox)findViewById(R.id.checkBox2);
        try{
            dataSource=new ScanABookDataSource(ViewBooks.this);
            dataSource.open();
            BookDetails bookDetails = dataSource.findABook(myBook);
            bookName.setText(bookDetails.getBookName());
            authorName.setText(bookDetails.getAuthorName());
            totalPages.setText(bookDetails.getNumOfPages().toString());
            read.setChecked(bookDetails.isBookRead());
            dataSource.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_books, menu);
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
