package com.mobsandgeeks.adapters.samples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobsandgeeks.adapters.instantadapters.InstantAdapter;
import com.mobsandgeeks.adapters.simplesectionadapter.Sectionizer;
import com.mobsandgeeks.adapters.simplesectionadapter.SimpleSectionAdapter;

/**
 * ListView based on {@link SimpleSectionAdapter}
 * 
 * @author Kapil
 * 
 */
public class SectionedListFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_fragment, container, false);

		/**
		 * SectionAdapter needs the list to be sorted based on category or
		 * section
		 */
		ArrayList<Book> books = new ArrayList<Book>(BooksDAO.getAllBooks());
		Collections.sort(books, new Comparator<Book>() {

			@Override
			public int compare(Book lhs, Book rhs) {
				return lhs.getBookCategory().compareTo(rhs.getBookCategory());
			}
		});

		InstantAdapter<Book> bookListAdapter = new InstantAdapter<Book>(
				this.getActivity(), R.layout.list_layout, Book.class, books);

		SimpleSectionAdapter<Book> sectionedAdapter = new SimpleSectionAdapter<Book>(
				getActivity(), bookListAdapter, R.layout.section_header,
				R.id.section_text, new Sectionizer<Book>() {

					@Override
					public String getSectionTitleForItem(Book instance) {
						return instance.getBookCategory();
					}
				});

		ListView bookListView = (ListView) view.findViewById(R.id.list);
		bookListView.setAdapter(sectionedAdapter);

		return view;
	}
}
