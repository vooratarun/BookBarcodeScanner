package sample.listup.com.listupsample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sample.listup.com.listupsample.R;
import sample.listup.com.listupsample.adapter.BookListAdapter;
import sample.listup.com.listupsample.models.Book;
import sample.listup.com.listupsample.utils.AppController;
import sample.listup.com.listupsample.utils.Helper;

public class ListAllBookActivity extends AppCompatActivity {

    //Variabls
    private ListView  booksListView;
    private BookListAdapter bookListAdapter;
    private List<Book> bookList = new ArrayList<Book>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_book);

        // Setting Listview and adapter.
        booksListView = (ListView) findViewById(R.id.books_list_view);
        bookListAdapter = new BookListAdapter(this,bookList);
        booksListView.setAdapter(bookListAdapter);

        // It is API fetches books
        getBooks();
    }

    // It fetches all books stored..
    private void getBooks() {

        //GET request to fetch books
        JsonArrayRequest booksRequest =
                new JsonArrayRequest(Request.Method.GET, Helper.GET_BOOKS_URL,
                        new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Toast.makeText(ListAllBookActivity.this, "List is loading..", Toast.LENGTH_SHORT).show();
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String bookImage = object.getString("image");
                        String bookTitle = object.getString("title");
                        String bookPrice = object.getString("amazonPrice");
                        int userPrice = object.getInt("userPrice");
                        String author = object.getString("author");
                        Book b  = new Book(bookTitle,author,bookImage,bookPrice,userPrice);
                        bookList.add(b);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("books_size",bookList.size()+" ");
                bookListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(booksRequest);
    }

}
