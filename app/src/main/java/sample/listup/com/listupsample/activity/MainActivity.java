package sample.listup.com.listupsample.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import sample.listup.com.listupsample.R;
import sample.listup.com.listupsample.models.Book;
import sample.listup.com.listupsample.utils.AppController;
import sample.listup.com.listupsample.utils.Helper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    //Buttons
    private Button barcodeBtn;
    private Button ISBNBtn;
    private Button allBooksBtn;
    private Button addBookBtn;

    //EditText fields
    private EditText ISBNEditText;
    private EditText priceEditText;

    // Layouts
    private LinearLayout priceLayout;
    private TextView bookNameTextview;

    //Variables
    private Book insertingBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setting Fields and listeners on buttons
        barcodeBtn = (Button) findViewById(R.id.barcode_scan);
        ISBNBtn = (Button) findViewById(R.id.isbn_code);
        allBooksBtn = (Button) findViewById(R.id.all_books);
        addBookBtn = (Button) findViewById(R.id.add_book);
        addBookBtn.setOnClickListener(this);
        barcodeBtn.setOnClickListener(this);
        ISBNBtn.setOnClickListener(this);
        allBooksBtn.setOnClickListener(this);

        // setting Layouts
        priceLayout = (LinearLayout) findViewById(R.id.price_layout);

        //Setting Text fields
        ISBNEditText = (EditText) findViewById(R.id.isbn_text);
        priceEditText = (EditText) findViewById(R.id.price);
        bookNameTextview = (TextView) findViewById(R.id.bookname_detected);
    }

    @Override
    public void onClick(View view) {

        // Onclick on buttons. opens the scan activity
        switch (view.getId()){
            case R.id.barcode_scan :
                // It uses Already pre installed app to scan barcode . it asks to install one app from playstore
                scanBarWithPreinstalledApp();
                 // It uses the barcode scanner library to get results. This library included in build.gradle.
                // scanBarcodeUsingLibrary();

                break;

            // To get All the books
            case R.id.all_books :
                Intent intent = new Intent(this,ListAllBookActivity.class);
                startActivity(intent);

            // To get book details from ISBN code
            case R.id.isbn_code :
                    if(ISBNEditText.getText().toString().length() > 0) {
                        Toast.makeText(this, ISBNEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                        bookDetailsByISBN(ISBNEditText.getText().toString());
                    } else {
                        Toast.makeText(this, "Please Enter code.", Toast.LENGTH_SHORT).show();
                    }
                break;

            // It  asks for price and inserts book object into database.
            case R.id.add_book :
                String priceText = priceEditText.getText().toString();
                if(priceText.length() > 0 ){
                    insertingBook.setUserPrice(Integer.parseInt(priceText));
                    insertBookInDB(insertingBook);
                } else {
                    Toast.makeText(this, "Please Enter priceEditText..", Toast.LENGTH_SHORT).show();
                }
                break;

            // General TestCase


            default:
                break;
        }
    }

    // product barcode mode. It scans barcode on Product mode
    // If no scanner available It downloads one from Google play store.. and We can use thirdParty library also
    public void scanBarWithPreinstalledApp() {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(MainActivity.this, "No Scanner Found",
                    "Download a scanner code App? or use inbuilt app ?", "APP", "INBUILT").show();
        }
    }

    // using library
    public void scanBarcodeUsingLibrary() {
        new IntentIntegrator(this).initiateScan();
    }

    private void bookDetailsByISBN(String bookISBN) {

        String url = Helper.BOOK_DETAILS + bookISBN;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                boolean hasResponse = false;
                try {
                    hasResponse = response.getBoolean("response");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(hasResponse) {
                    String title = "Missed";
                    String image = "";
                    String author = "Missed";
                    String amazonPrice = "Missed";

                    try {
                         title = response.getString("title");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                         image = response.getString("imageUrl");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                         author = response.getString("author");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                         amazonPrice = response.getString("amazonPrice");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    priceLayout.setVisibility(View.VISIBLE);
                    addBookBtn.setVisibility(View.VISIBLE);
                    bookNameTextview.setText(title + " , " + author + " , " + amazonPrice);
                    insertingBook = new Book(title, author, image, amazonPrice, 0);
                }
                else {
                    Toast.makeText(MainActivity.this, "Book Not found. Please Enter some Indian book ISBN",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    // Insert the book into database with created JSON object.
    private void insertBookInDB(Book book){

        Toast.makeText(this, "inserting book", Toast.LENGTH_SHORT).show();

        //Making Json Object from book data
        JSONObject object = new JSONObject();
        try {
            object.put("image",book.getImage());
            object.put("title",book.getTitle());
            object.put("amazonPrice",book.getAmazonPrice());
            object.put("userPrice",book.getUserPrice());
            object.put("author",book.getAuthor());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Posting bookdata object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Helper.INSERT_BOOK_URL,
                object,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(MainActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                    priceLayout.setVisibility(View.GONE);
                    addBookBtn.setVisibility(View.GONE);
                    ISBNEditText.setText("");
                    priceEditText.setText("");
                    bookNameTextview.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    //alert dialog for downloadDialog, It will execute if No Scanner found, It installs one.
    private  AlertDialog showDialog(final Activity act, CharSequence title,
                                    CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        final AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(act, "Use Scanbar library", Toast.LENGTH_SHORT).show();
                scanBarcodeUsingLibrary();
            }
        });
        return downloadDialog.show();
    }


    //on ActivityResult method. We got a product after scanned

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Scan Completed..", Toast.LENGTH_SHORT).show();
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d("ScanResult",contents);

                // We got product ISBN number and so get googlebook details
                bookDetailsByISBN(contents);
            }
        } else {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if(result != null) {
                if(result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("MainActivity", "Scanned");
                    String scanContent = result.getContents();
                    String scanFormat = result.getFormatName();

                    if(scanContent != null && scanFormat != null && scanFormat.equalsIgnoreCase("EAN_13")){
                        Toast.makeText(this, "Scanned: " + scanContent, Toast.LENGTH_LONG).show();
                        bookDetailsByISBN(scanContent);
                    } else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Not a valid scan!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, intent);
            }
        }
    }
}


