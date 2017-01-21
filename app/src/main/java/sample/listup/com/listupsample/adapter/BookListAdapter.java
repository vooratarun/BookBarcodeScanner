package sample.listup.com.listupsample.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import sample.listup.com.listupsample.R;
import sample.listup.com.listupsample.models.Book;
import sample.listup.com.listupsample.utils.AppController;

/**
 * Created by admin on 20-Jan-17.
 */

public class BookListAdapter extends BaseAdapter {

    //Class Variables
    private Activity activity;
    private LayoutInflater inflater;
    private List<Book> bookItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public BookListAdapter(Activity activity,List<Book> bookItems) {
        this.activity = activity;
        this.bookItems = bookItems;
    }

    @Override
    public int getCount() {
        return bookItems.size();
    }

    @Override
    public Object getItem(int i) {
        return bookItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) view
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView author = (TextView) view.findViewById(R.id.author);
        TextView amazonPrice = (TextView) view.findViewById(R.id.amazon_price);
        TextView userPrice = (TextView) view.findViewById(R.id.user_price);

        // getting book data for the row
        Book book = bookItems.get(i);

        // setting values
        thumbNail.setImageUrl(book.getImage(), imageLoader);
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        amazonPrice.setText("Amazon price : " + book.getAmazonPrice());
        userPrice.setText("User Price : " + String.valueOf(book.getUserPrice()));

        return view;
    }
}
