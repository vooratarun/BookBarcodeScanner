package sample.listup.com.listupsample.models;

import java.util.TimerTask;

/**
 * Created by admin on 20-Jan-17.
 */

public class Book {

    private String bookTitle;
    private int bookPrice;
    private String bookImage;

    public Book(String bookTitle,int bookPrice, String bookImage){

        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
        this.bookImage = bookImage;

    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(int bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
}
