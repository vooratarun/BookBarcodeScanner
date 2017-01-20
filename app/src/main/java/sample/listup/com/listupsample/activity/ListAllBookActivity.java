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

public class ListAllBookActivity extends AppCompatActivity {

    private ListView  booksListView;
    private BookListAdapter bookListAdapter;
    private List<Book> bookList = new ArrayList<Book>();
    private static final String BOOKS_URL = "http://52.74.62.47:3000/books";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_book);

        booksListView = (ListView) findViewById(R.id.books_list_view);
        bookListAdapter = new BookListAdapter(this,bookList);
        booksListView.setAdapter(bookListAdapter);
        Book book = new Book("El monje que vendió su ferrari",100,
                "http://books.google.com/books/content?id=B1DzOQAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api");
        bookList.add(book);
        getBooks();

    }

    private void getBooks() {

        JsonArrayRequest booksRequest =
                new JsonArrayRequest(Request.Method.GET, BOOKS_URL,
                        new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Toast.makeText(ListAllBookActivity.this, "I got response", Toast.LENGTH_SHORT).show();
                for(int i=0;i<response.length();i++){

                    try {
                        JSONObject object = response.getJSONObject(i);
                        String bookImage = object.getString("bookImage");
                        String bookTitle = object.getString("bookName");
                        int bookPrice = object.getInt("bookPrice");
                        Book b  = new Book(bookTitle,bookPrice,bookImage);
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
