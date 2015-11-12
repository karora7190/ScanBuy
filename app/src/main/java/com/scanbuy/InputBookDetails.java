package com.scanbuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scanabook.database.BookDetails;
import com.scanabook.database.ScanABookDataSource;

import org.w3c.dom.Text;

public class InputBookDetails extends AppCompatActivity {

    public TextView book_isbn;
    public EditText book_name;
    public EditText author_name;
    public EditText total_pages;
    public CheckBox book_read;
    public Button addABook;
    public ScanABookDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_book_details);
        final String bookIsbn = getIntent().getExtras().getString("book_isbn");
        book_isbn = (TextView)findViewById(R.id.textView18);
        book_name=(EditText)findViewById(R.id.editText2);
        author_name=(EditText)findViewById(R.id.editText3);
        total_pages=(EditText)findViewById(R.id.editText4);
        book_read=(CheckBox)findViewById(R.id.checkBox3);
        addABook=(Button)findViewById(R.id.button3);
        book_isbn.setText(bookIsbn);
        try {
            dataSource = new ScanABookDataSource(InputBookDetails.this);
            dataSource.open();

            addABook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookDetails bookDetails=new BookDetails();
                    bookDetails.setBookISBN(bookIsbn);
                    bookDetails.setBookName(book_name.getText().toString());
                    bookDetails.setAuthorName(author_name.getText().toString());
                    bookDetails.setNumOfPages(Integer.parseInt(total_pages.getText().toString()));
                    bookDetails.setBookRead(book_read.isSelected());
                    long id=dataSource.AddABook(bookDetails);
                    Toast.makeText(getBaseContext(),"The book is added with ID:"+id,Toast.LENGTH_LONG).show();
                    dataSource.close();
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
        getMenuInflater().inflate(R.menu.menu_input_book_details, menu);
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
