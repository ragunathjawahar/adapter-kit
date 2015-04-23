package com.mobsandgeeks.adapters.samples;

import com.mobsandgeeks.adapters.InstantText;

public class Book {
	private String bookName;
	private String bookAuthor;
	private String bookCategory;

	public Book(String bookName, String bookAuthor, String bookCategory) {
		this.setBookAuthor(bookAuthor);
		this.setBookCategory(bookCategory);
		this.setBookName(bookName);
	}

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

}
