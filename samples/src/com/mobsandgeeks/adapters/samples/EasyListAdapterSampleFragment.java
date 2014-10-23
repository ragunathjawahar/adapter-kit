package com.mobsandgeeks.adapters.samples;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.birin.easylistviewadapters.EasyListAdapter;
import com.birin.easylistviewadapters.ListRowViewSetter;
import com.birin.easylistviewadapters.Row;
import com.birin.easylistviewadapters.utils.ChildViewsClickHandler;

/**
 * ListView based on {@link EasyListAdapter}
 * 
 * @author Biraj Patel
 * 
 */
public class EasyListAdapterSampleFragment extends Fragment {

	private ListView listview;
	private ArrayList<Item> dataList;
	private SimplestDemoAdadpter listadapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Context context = getActivity().getApplicationContext();
		listview = new ListView(context);
		generateSomeDummyDataList();
		listadapter = new SimplestDemoAdadpter(context);
		listadapter.addItemsInList(dataList);
		listview.setAdapter(listadapter);
		setupListFooter();
		return listview;
	}

	private final static String LINK = "https://github.com/birajpatel/EasyListViewAdapters";

	private void setupListFooter() {
		final Activity activity = getActivity();
		TextView textView = new Button(activity.getApplicationContext());
		textView.setText("Check full demo at " + LINK);
		textView.setPadding(20, 20, 20, 20);
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(LINK));
				activity.startActivity(browserIntent);
			}
		});
		listview.addFooterView(textView);
	}

	private void generateSomeDummyDataList() {
		dataList = new ArrayList<Item>();
		// Generate some dummy data.
		for (int i = 0; i < 51; i++) {
			if (i % 2 == 0) {
				dataList.add(new TextItem(i));
			} else {
				dataList.add(new ButtonItem(i));
			}
		}
	}
}

// Setup General data Classes

// Enum that will be used to specify various types of rows. Its best to use Enum
// values instead of hard-coded row-types

enum ItemRowTypes {
	TEXT_ITEM, BUTTON_ITEM
}

// Base data class that will be used to create data-list, into which any kind of
// child objects can be placed to be displayed

abstract class Item {
	private int position;

	protected Item(int position) {
		this.position = position;
	}

	public String getPositionText() {
		return "Position: " + position;
	}

	public abstract int getItemRowType();
}

// Child data class-1
class TextItem extends Item {

	protected TextItem(int position) {
		super(position);
	}

	@Override
	public int getItemRowType() {
		return ItemRowTypes.TEXT_ITEM.ordinal();
	}

}

// Child data class-2
class ButtonItem extends Item {

	protected ButtonItem(int position) {
		super(position);
	}

	@Override
	public int getItemRowType() {
		return ItemRowTypes.BUTTON_ITEM.ordinal();
	}

}

// Row View Holder classes, these should hold all child-view references of a
// given row so that this references can be Re-used to update views (Without
// having to call findViewById each time on list scroll.)

class TextItemViewHolder {
	TextView textview;
}

class ButtonItemViewHolder {
	Button button;
}

// Now that our data classes & ViewHolder classes are ready lets bind each data
// item to each row, for that purpose we define RowViewSetter classes for
// different row-types
//
// by implementing ListRowViewSetter<E, RVH> (For EasyListAdadpter)
// where E means your POJO data.
// & RVH means RowViewHolder for this row.
// OR
// by implementing CursorRowViewSetter<RVH> (For EasyCursorAdapter)
// where RVH means RowViewHolder for this row.
//
// By passing E & RVH you bind your RowViewSetter generically to specific
// objects so that type-cast are not needed & compile type object safety can be
// achieved, Just like using ArrayList<Integer> where only integer values only
// can be passed.

// There are 5 easy methods to be implemented in RowViewSetter interface
//
// 1. getRowType() here library will ask how it can identify your Row, return
// unique row-type for each RowViewSetter class.
//
// 2. getNewRow() here library will ask how your Row will look & child views you
// want to hold in ViewHolder?, return new Object of
// com.birin.easylistviewadapter.Row<RVH> class where RVH
// stands for RowViewHolder for this row. Row<RVH> takes two objects in
// constructor
// i. RowView : return new view inflating through layout or creating new View
// through Java.
// ii. RowViewHolder : ViewHolder class that should hold child views of given
// row, so that view-holder can be used to recycle on list scroll.
//
// 3. setRowView() here library will tell you to fill data into your views by
// using Data & ViewHolder.
//
// 4. registerChildrenViewClickEvents() here library will tell you to register
// children present in your RowViewHolder using ChildViewsClickHandler instance,
// registering your children will enable you to get click events in
// onChildViewClicked method, Using this is optional alternatively user can
// handle children ViewClicks in their own ways.
//
// 5. onChildViewClicked() here library will tell you to that any of registered
// child is clicked the advantage of registering is that library will provide
// data related to clicked view's row,
// child view can be registered using ChildViewsClickHandler instance which was
// passed in registerChildrenViewClickEvents() method.
//
//

// 1.TextItem RowViewSetter
class TextItemRowViewSetter implements
		ListRowViewSetter<TextItem, TextItemViewHolder> {

	private Context context;

	public TextItemRowViewSetter(Context context) {
		this.context = context;
	}

	@Override
	public int getRowType() {
		return ItemRowTypes.TEXT_ITEM.ordinal();
	}

	@Override
	public Row<TextItemViewHolder> getNewRow(ViewGroup parent) {
		// Create Row programmatically (can be created by XML as well.)
		TextView textView = new TextView(context);
		textView.setTextColor(Color.BLACK);
		textView.setPadding(10, 20, 10, 20);
		// Setting up row view holder.
		TextItemViewHolder viewHolder = new TextItemViewHolder();
		viewHolder.textview = textView;
		return new Row<TextItemViewHolder>(textView, viewHolder);
	}

	@Override
	public void setRowView(TextItem data, TextItemViewHolder rowViewHolder,
			int position) {
		rowViewHolder.textview.setText(data.getPositionText());
	}

	@Override
	public void registerChildrenViewClickEvents(
			TextItemViewHolder rowViewHolder,
			ChildViewsClickHandler childViewsClickHandler) {
		// Register for child-view clicks if required.
	}

	@Override
	public void onChildViewClicked(View clickedChildView, TextItem rowData,
			int eventId) {
		// no views registered for click event for demo please look other
		// tutorials.
	}

}

// 2.ButtonItem RowViewSetter
class ButtonItemRowViewSetter implements
		ListRowViewSetter<ButtonItem, ButtonItemViewHolder> {

	private Context context;

	public ButtonItemRowViewSetter(Context context) {
		this.context = context;
	}

	@Override
	public int getRowType() {
		return ItemRowTypes.BUTTON_ITEM.ordinal();
	}

	@Override
	public Row<ButtonItemViewHolder> getNewRow(ViewGroup parent) {
		Button button = new Button(context);
		ButtonItemViewHolder viewHolder = new ButtonItemViewHolder();
		viewHolder.button = button;
		return new Row<ButtonItemViewHolder>(button, viewHolder);
	}

	@Override
	public void setRowView(ButtonItem data, ButtonItemViewHolder rowViewHolder,
			int position) {
		rowViewHolder.button.setText(data.getPositionText());
	}

	@Override
	public void registerChildrenViewClickEvents(
			ButtonItemViewHolder rowViewHolder,
			ChildViewsClickHandler childViewsClickHandler) {
		// no views registered for click event for demo please look other
		// tutorials.
	}

	@Override
	public void onChildViewClicked(View clickedChildView, ButtonItem rowData,
			int eventId) {
		// no views registered for click event for demo please look other
		// tutorials.
	}
}

// Now that you have already defined your Data Classes, ViewHolder Classes &
// RowViewSetter Classes only one small step is left, ie extending
// EasyListAdapter<E>/EasyCursorAdapter & implement 2 small methods.
// When using EasyListAdapter you need to pass your POJO in place of 'E' so that
// you can get that object back as a parameter in getRowViewType() so in this
// Example we are dealing with ArrayList<Item> so we need to
// pass 'Item' in place of 'E'
//
// 1. getRowViewType() here library will give you your POJO/Cursor for a
// particular position, your job is to tell library about row type for a given
// POJO/Cursor
//
// 2.
// getListRowViewSetters for EasyListAdapter<E> class
// OR
// getCursorRowViewSetters for EasyCursorAdapter class
// here you have to create array of RowViewSetter Classes which you just defined
// above & tell it to adapter, so that library can use your RowViewSetter
// classes & can render rows.

class SimplestDemoAdadpter extends EasyListAdapter<Item> {

	protected SimplestDemoAdadpter(Context context) {
		super(context);
	}

	@Override
	public int getRowViewType(Item rowData) {
		return rowData.getItemRowType();
	}

	@Override
	protected ListRowViewSetter<?, ?>[] getListRowViewSetters() {
		Context context = getContext();
		ListRowViewSetter<?, ?>[] rows = { new TextItemRowViewSetter(context),
				new ButtonItemRowViewSetter(context) };
		return rows;
	}

}

//
//
//
// Feeling comfortable with basic library usage ?? Wait !! There are more demos
// in Sample Project like,
// 1.Usage of EasyCursorAdapter for handling DB data.
// 2.Using Auto load more feature of library
// 3 Usage of Tap to Load More
// 4 Limiting Auto Load More on max items reached
// 5 Handling click events of child views in a row in very easy way.
// 6 Binding data through ContentProvider/CursorLoader mechanism
// 7 Supporting fixed number of data items
// 8 supporting multiple/single type of rows depending upon number of
// RowViewSetters you pass to adapter.
// 9 Best approach to handle data & AysncTasks through rotation of screen.
//
// Find complete features Demo Project at
//
// https://github.com/birajpatel/EasyListViewAdapters
//
//
//

