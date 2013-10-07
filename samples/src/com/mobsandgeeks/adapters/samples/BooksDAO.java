package com.mobsandgeeks.adapters.samples;

import java.util.ArrayList;
import java.util.List;

public class BooksDAO {

	private static ArrayList<Book> books;

	/**
	 * Initializing sample data
	 */
	static {
		books = new ArrayList<Book>();
		books.add(new Book("War of the worlds", "H.G. Wells", "Science Fiction"));
		books.add(new Book("Hackers and Painters", "Paul Graham",
				"Computers and Internet"));
		books.add(new Book("Wool", "Hugh Howey", "Science Fiction"));
		books.add(new Book("The Time Machine", "H.G. Wells", "Science Fiction"));
		books.add(new Book("Zero Day", "Mark Russinovich",
				"Computers and Internet"));
		books.add(new Book("The Sign of Four", "Sir Arthur Conan Doyle",
				"Thriller"));
		books.add(new Book("Hackers:Heros of Computer Revolution",
				"Steven Levy", "Computers and Internet"));
		books.add(new Book("Thinking, Fast and Slow", "Daniel Kahneman",
				"Nonfiction"));
		books.add(new Book("Linear Algebra and its Applications",
				"Paul Strang", "Math"));
	}

	public static List<Book> getAllBooks() {
		return books;
	}

}
