package com.scanabook.database;

/**
 * Created by kshitiz on 11/8/15.
 */
public class BookDetails {

    private String bookISBN;
    private String bookName;
    private String authorName;
    private Integer numOfPages;
    private boolean bookRead;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(Integer numOfPages) {
        this.numOfPages = numOfPages;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public boolean isBookRead() {
        return bookRead;
    }

    public void setBookRead(boolean bookRead) {
        this.bookRead = bookRead;
    }

    public String getBookISBN() {

        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }
}
