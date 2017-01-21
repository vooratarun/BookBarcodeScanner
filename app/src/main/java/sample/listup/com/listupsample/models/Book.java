package sample.listup.com.listupsample.models;

/**
 * Created by admin on 20-Jan-17.
 */

public class Book {

    private String title;
    private String amazonPrice;
    private String image;
    private String author;
    private int userPrice;


    public Book(String title,String author, String image,String amazonPrice,int userPrice){

        this.title = title;
        this.amazonPrice = amazonPrice;
        this.author = author;
        this.image = image;
        this.userPrice = userPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmazonPrice() {
        return amazonPrice;
    }

    public void setAmazonPrice(String  amazonPrice) {
        this.amazonPrice = amazonPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getUserPrice() {
        return userPrice;
    }

    public void setUserPrice(int userPrice) {
        this.userPrice = userPrice;
    }
}
