package com.scanbuy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.scanabook.database.BookDetails;
import com.scanabook.database.ScanABookDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class BookScanner extends AppCompatActivity {

    public Button bookScanner;
    public EditText bookIsbn;
    public TextView bookLabel;
    public TextView authorLabel;
    public TextView pageLabel;
    public TextView bookName;
    public TextView authorName;
    public TextView totalPages;
    public CheckBox read;
    public Button addABook;
    public ScanABookDataSource dataSource;
    final String apiKey="AIzaSyAJRgIoeeLtvzq5Yew75EEJDX0BBM03uE0";
    private static final int RC_BARCODE_CAPTURE = 9001;
    final String URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_scanner);
        try{
            dataSource=new ScanABookDataSource(BookScanner.this);
            dataSource.open();
            bookScanner = (Button)findViewById(R.id.button);
            bookLabel=(TextView)findViewById(R.id.textView2);
            authorLabel=(TextView)findViewById(R.id.textView4);
            pageLabel=(TextView)findViewById(R.id.textView6);
            bookIsbn = (EditText)findViewById(R.id.editText);
            bookName = (TextView)findViewById(R.id.textView3);
            authorName = (TextView)findViewById(R.id.textView5);
            totalPages = (TextView)findViewById(R.id.textView7);
            read = (CheckBox)findViewById(R.id.checkBox);
            addABook=(Button)findViewById(R.id.button2);
            bookScanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookScanner.this, BarcodeCaptureActivity.class);
                    //intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
                    //intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
                    startActivityForResult(intent, RC_BARCODE_CAPTURE);

                }
            });
            addABook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookDetails bookDetails=new BookDetails();
                    bookDetails.setBookISBN(bookIsbn.getText().toString());
                    bookDetails.setBookName(bookName.getText().toString());
                    bookDetails.setAuthorName(authorName.getText().toString());
                    bookDetails.setNumOfPages(Integer.parseInt(totalPages.getText().toString()));
                    bookDetails.setBookRead(read.isChecked());
                    long addedId=dataSource.AddABook(bookDetails);
                    Toast.makeText(getBaseContext(),"The Book is added to the database with Id:"+addedId,Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_book_scanner, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {

                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Toast.makeText(getBaseContext(),R.string.barcode_success,Toast.LENGTH_LONG).show();
                    bookIsbn.setText(barcode.displayValue);
                    BookDetails bookDetails=dataSource.findABook(barcode.displayValue);
                    if(bookDetails==null) {
                        InvokeBookService invokeBookService = new InvokeBookService();
                        invokeBookService.execute(URL + bookIsbn.getText().toString() + "&key=" + apiKey);
                        Log.d("Read successfully", "Barcode read: " + barcode.displayValue);
                    }
                    else{
                        bookLabel.setVisibility(View.VISIBLE);
                        authorLabel.setVisibility(View.VISIBLE);
                        pageLabel.setVisibility(View.VISIBLE);
                        bookName.setVisibility(View.VISIBLE);
                        authorName.setVisibility(View.VISIBLE);
                        totalPages.setVisibility(View.VISIBLE);
                        bookName.setText(bookDetails.getBookName());
                        authorName.setText(bookDetails.getAuthorName());
                        totalPages.setText(bookDetails.getNumOfPages().toString());
                        read.setVisibility(View.VISIBLE);
                        addABook.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getBaseContext(),R.string.barcode_failure,Toast.LENGTH_LONG).show();
                    Log.d("Error", "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(getBaseContext(),String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)),Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
    private class InvokeBookService extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            HttpsURLConnection connection=null;
            String myResult = null;
            String myJson = "";
            try{
                URL myURL = new URL(urls[0]);
                connection= (HttpsURLConnection)myURL.openConnection();
                BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((myResult = br.readLine())!=null){
                    myJson += myResult;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return myJson;

        }

        protected void onPostExecute(String result){
            try {
                JSONObject jsonObject = new JSONObject(result);
                if(Integer.parseInt(jsonObject.get("totalItems").toString())>0) {
                    JSONArray items = jsonObject.getJSONArray("items");
                    bookLabel.setVisibility(View.VISIBLE);
                    authorLabel.setVisibility(View.VISIBLE);
                    pageLabel.setVisibility(View.VISIBLE);
                    bookName.setVisibility(View.VISIBLE);
                    authorName.setVisibility(View.VISIBLE);
                    totalPages.setVisibility(View.VISIBLE);
                    bookName.setText(items.getJSONObject(0).getJSONObject("volumeInfo").get("title").toString());
                    authorName.setText(items.getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("authors").get(0).toString());
                    totalPages.setText(items.getJSONObject(0).getJSONObject("volumeInfo").get("pageCount").toString());
                    read.setVisibility(View.VISIBLE);
                    addABook.setVisibility(View.VISIBLE);
                }
                else{
                    Intent intent=new Intent(BookScanner.this,InputBookDetails.class);
                    intent.putExtra("book_isbn",bookIsbn.getText().toString());
                    startActivity(intent);
                    BookScanner.this.finish();
                }
                //System.out.println(jsonObject);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
