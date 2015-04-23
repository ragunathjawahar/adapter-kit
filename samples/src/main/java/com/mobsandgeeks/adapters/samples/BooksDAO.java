package com.mobsandgeeks.adapters.samples;

import java.util.ArrayList;
import java.util.List;

public class BooksDAO {

	private static ArrayList<Book> books;
    private static ArrayList<ImageBook> booksImages;

	/**
	 * Initializing sample data
	 */
	static {
		books = new ArrayList<Book>();
		books.add(new Book("War of the worlds", "H.G. Wells", "Science Fiction"));
		books.add(new Book("Hackers and Painters", "Paul Graham", "Computers and Internet"));
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

        booksImages = new ArrayList<ImageBook>();
        booksImages.add(new ImageBook("War of the worlds", "H.G. Wells", "Science Fiction", "https://lovecraftianscience.files.wordpress.com/2014/06/war_cgfewston-files-wordpress-com.jpg"));
        booksImages.add(new ImageBook("Hackers and Painters", "Paul Graham", "Computers and Internet", "http://www.brainpickings.org/wp-content/uploads/2014/06/hackersandpainters.jpeg"));
        booksImages.add(new ImageBook("Wool", "Hugh Howey", "Science Fiction", "http://1.bp.blogspot.com/-YYlZaW2vwcg/UoFS3q60cYI/AAAAAAAAArQ/M03JlX4C5hQ/s1600/wool-hugh-howey.jpg"));
        booksImages.add(new ImageBook("The Time Machine", "H.G. Wells", "Science Fiction", "http://www.book-review-circle.com/images/timemachine.jpg"));
        booksImages.add(new ImageBook("Zero Day", "Mark Russinovich",
                "Computers and Internet", "http://i.neoseeker.com/boxshots/Qm9va3MvVGhyaWxsZXI=/zero_day_frontcover_large_MIWpQkbyL7fqMsj.jpg"));
        booksImages.add(new ImageBook("The Sign of Four", "Sir Arthur Conan Doyle",
                "Thriller", "http://ecx.images-amazon.com/images/I/51Jwso-0L9L.jpg"));
        booksImages.add(new ImageBook("Hackers:Heros of Computer Revolution",
                "Steven Levy", "Computers and Internet", "http://www.stevenlevy.com/wp-content/uploads/2006/09/steven-levy-hackers-heroes-of-the-computer-revolution.jpg"));
        booksImages.add(new ImageBook("Thinking, Fast and Slow", "Daniel Kahneman",
                "Nonfiction", "http://ecx.images-amazon.com/images/I/41RtytNpsfL._SY344_BO1,204,203,200_.jpg"));
        booksImages.add(new ImageBook("Linear Algebra and its Applications",
                "Paul Strang", "Math", "http://www.jblearning.com/covers/large/1449613527.jpg"));
	}

	public static List<Book> getAllBooks() {
		return books;
	}

    public static List<ImageBook> getAllBooksWithImages() {
        return booksImages;
    }

}
