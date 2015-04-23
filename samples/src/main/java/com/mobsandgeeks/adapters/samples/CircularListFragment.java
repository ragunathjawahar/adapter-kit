package com.mobsandgeeks.adapters.samples;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobsandgeeks.adapters.CircularListAdapter;
import com.mobsandgeeks.adapters.InstantAdapter;

/**
 * ListView based on {@link CircularListAdapter}
 * 
 * @author Kapil
 * 
 */
public class CircularListFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_fragment, container, false);

		InstantAdapter<Book> bookListAdapter = new InstantAdapter<Book>(
				this.getActivity(), R.layout.list_layout, Book.class,
				BooksDAO.getAllBooks());

		ListView bookListView = (ListView) view.findViewById(R.id.list);
		bookListView.setAdapter(new CircularListAdapter(bookListAdapter));

		return view;
	}
}
