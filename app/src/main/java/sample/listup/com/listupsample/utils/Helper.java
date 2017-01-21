package sample.listup.com.listupsample.utils;


/**
 * Created by admin on 21-Jan-17.
 */

public class Helper {

    //base url
    public static final String URL = "http://52.74.62.47:3000";

    // handing Endpoints on baseURl
    public static final String GET_BOOKS_URL = URL + "/books";
    public static final String INSERT_BOOK_URL = URL + "/create/book";
    public static final String BOOK_DETAILS = URL + "/book/";

    //Googlebooks api url
    public static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=isbn:";


}
