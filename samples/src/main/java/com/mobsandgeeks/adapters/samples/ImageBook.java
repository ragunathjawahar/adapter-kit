package com.mobsandgeeks.adapters.samples;

import com.mobsandgeeks.adapters.InstantImage;
import com.mobsandgeeks.adapters.InstantText;

public class ImageBook {
    protected String bookName;
    protected String bookAuthor;
    protected String bookCategory;

    @InstantText(viewId = R.id.bookName)
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @InstantText(viewId = R.id.bookAuthorName)
    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }
    private String bookImageUrl;

    public ImageBook(String bookName, String bookAuthor, String bookCategory, String bookImageUrl) {
        this.setBookAuthor(bookAuthor);
        this.setBookCategory(bookCategory);
        this.setBookName(bookName);
        this.setBookImageUrl(bookImageUrl);
    }

    @InstantImage(viewId = R.id.image)
    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }
}
