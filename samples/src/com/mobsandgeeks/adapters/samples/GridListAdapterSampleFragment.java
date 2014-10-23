package com.mobsandgeeks.adapters.samples;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.birin.gridlistviewadapters.Card;
import com.birin.gridlistviewadapters.ListGridAdapter;
import com.birin.gridlistviewadapters.dataholders.CardDataHolder;
import com.birin.gridlistviewadapters.utils.ChildViewsClickHandler;

/**
 * ListView based on {@link ListGridAdapter}
 * 
 * @author Biraj Patel
 * 
 */
@SuppressLint("InflateParams")
public class GridListAdapterSampleFragment extends Fragment {

	private ListView listview;
	private ArrayList<CardItem> dataList;
	private SimplestGridAdadpter listadapter;

	private final static String LINK = "https://github.com/birajpatel/GridListViewAdapters";
	private final int MAX_CARDS = 3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Context context = getActivity().getApplicationContext();
		listview = new ListView(context);
		listview.setDivider(null);
		generateSomeDummyDataList();
		listadapter = new SimplestGridAdadpter(context, MAX_CARDS);
		listadapter.addItemsInGrid(dataList);
		addHeaderFooters();
		listview.setAdapter(listadapter);
		return listview;
	}

	private void addHeaderFooters() {
		final int cardSpacing = listadapter.getCardSpacing();

		// Header View
		View headerView = getHeaderFooterView("HEADER", cardSpacing);
		headerView.setPadding(cardSpacing, cardSpacing, cardSpacing, 0);
		listview.addHeaderView(headerView);

		// Footer View
		View footerView = getHeaderFooterView("Check full demo at " + LINK,
				cardSpacing);
		footerView.setPadding(cardSpacing, 0, cardSpacing, cardSpacing);
		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(LINK));
				getActivity().startActivity(browserIntent);
			}
		});
		listview.addFooterView(footerView);
	}

	@SuppressLint("InflateParams")
	private View getHeaderFooterView(String text, int cardSpacing) {
		// New footer/header view.
		View view = listadapter.getLayoutInflater().inflate(
				R.layout.simplest_header_footer_layout, null);

		// Header-Footer card sizing.
		View headerFooterView = view.findViewById(R.id.card_main_parent);
		int headerFooterWidth = listadapter.getDeviceWidth()
				- (2 * cardSpacing); // Left-right so *2
		int headerFooterHeight = listadapter.getCardWidth(MAX_CARDS);
		headerFooterView.getLayoutParams().width = headerFooterWidth;
		headerFooterView.getLayoutParams().height = headerFooterHeight;

		// Setting text value
		((TextView) view.findViewById(R.id.name)).setText(text);
		return view;
	}

	private void generateSomeDummyDataList() {
		dataList = new ArrayList<CardItem>();
		// Generate some dummy data.
		for (int i = 0; i < 51; i++) {
			dataList.add(new CardItem(i));
		}
	}
}

// Data class to be used for ListGridAdapter demo.

class CardItem {
	private int position;

	protected CardItem(int position) {
		this.position = position;
	}

	public String getPositionText() {
		return Integer.toString(position);
	}
}

// Card View Holder class, these should hold all child-view references of a
// given card so that this references can be Re-used to update views (Without
// having to call findViewById each time on list scroll.)

class ViewHolder {
	TextView textView;
}

// Now that our data class & ViewHolder class are ready lets bind each data
// CardItem to each card, for that purpose we extend
// ListGridAdapter<E,CVH>/CursorGridAdapter<CVH> & implement few easy methods.
//
// When using ListGridAdapter you need to pass your POJO in place of 'E' so that
// you can get that object back as a parameters in various methods so in this
// Example we are dealing with ArrayList<CardItem> so we need to
// pass 'CardItem' in place of 'E'
//
// CVH means CardViewHolder for our card.
//
// By passing E & CVH you bind your adapter class generically to specific
// objects so that type-cast are not needed & compile type object safety can be
// achieved, Just like using ArrayList<Integer> where only integer values only
// can be passed.

// There are 5 easy methods to be implemented in grid-adapters
//
// 1. getNewCard() here library will ask how your Card will look & child views
// you want to hold in CardViewHolder ?, return new Object of
// com.birin.gridlistviewadapters.Card<CVH> class where CVH stands for
// CardViewHolder for this Card. Card<CVH> takes two objects in constructor
// i. CardView : return new view inflating through layout or creating new View
// through Java.
// ii. CardViewHolder : ViewHolder class that should hold child views of given
// cardView, so that view-holder can be used to recycle on list scroll.
// All the card & element sizing should be done in this method.
//
// 2. setCardView() here library will tell you to fill data into your views by
// using Data & CardViewHolder.
//
// 3. onCardClicked() here library give you callback of card-click event with
// card's data.
//
// 4. registerChildrenViewClickEvents() here library will tell you to register
// children present in your CardViewHolder using ChildViewsClickHandler
// instance, registering your children will enable you to get click events in
// onChildViewClicked method, Using this is optional alternatively user can
// handle children ViewClicks in their own ways.
//
// 5. onChildViewClicked() here library will tell you to that any of registered
// child is clicked the advantage of registering is that library will provide
// data related to clicked view's row & you do not have to find your data,
// child view can be registered using ChildViewsClickHandler instance which was
// passed in registerChildrenViewClickEvents() method.
//
//

class SimplestGridAdadpter extends ListGridAdapter<CardItem, ViewHolder> {

	public SimplestGridAdadpter(Context context, int totalCardsInRow) {
		super(context, totalCardsInRow);
	}

	@Override
	@SuppressLint("InflateParams")
	protected Card<ViewHolder> getNewCard(int cardwidth) {
		// Create card through XML (can be created programmatically as well.)
		View cardView = getLayoutInflater().inflate(
				R.layout.simplest_card_layout, null);
		cardView.setMinimumHeight(cardwidth);

		// Now create card view holder.
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.textView = (TextView) cardView.findViewById(R.id.name);

		return new Card<ViewHolder>(cardView, viewHolder);
	}

	@Override
	protected void setCardView(CardDataHolder<CardItem> cardDataHolder,
			ViewHolder cardViewHolder) {
		CardItem CardItem = cardDataHolder.getData();
		cardViewHolder.textView.setText(CardItem.getPositionText());
	}

	@Override
	protected void onCardClicked(CardItem cardData) {
		Toast.makeText(getContext(),
				"Card click " + cardData.getPositionText(), Toast.LENGTH_LONG)
				.show();
	}

	private final int TEXT_VIEW_CLICK_ID = 0;

	@Override
	protected void registerChildrenViewClickEvents(ViewHolder cardViewHolder,
			ChildViewsClickHandler childViewsClickHandler) {
		childViewsClickHandler.registerChildViewForClickEvent(
				cardViewHolder.textView, TEXT_VIEW_CLICK_ID);
	}

	@Override
	protected void onChildViewClicked(View clickedChildView, CardItem cardData,
			int eventId) {
		if (eventId == TEXT_VIEW_CLICK_ID) {
			Toast.makeText(getContext(),
					"TextView click " + cardData.getPositionText(),
					Toast.LENGTH_LONG).show();
		}
	}

	// OPTIONAL SETUP

	@Override
	public int getCardSpacing() {
		return (2 * super.getCardSpacing());
	}

	@Override
	protected void setRowView(View rowView, int arg1) {
		rowView.setBackgroundColor(getContext().getResources().getColor(
				R.color.simplest_list_background));
	}

}

//
//
//
// Feeling comfortable with basic library usage ?? Wait !! There are more demos
// in this Sample Project like,
// 1.Usage of CursorGridAdapter for handling DB data.
// 2.Usage of PositionCalculator class to maintain correct position
// after rotation.
// 3.Handling card click events.
// 4.Handling click events of child views in a card in very easy way.
// 5.Usage of Tap to Load More functionality
// 6.Using Auto load more feature of library
// 7.Limiting Auto Load More on max items reached
// 8.Binding data through ContentProvider/CursorLoader mechanism
// 9.Supporting fixed number of data items
// 10.Best approach to handle data & AysncTasks through rotation of screen.
//
// Find complete features Demo Project at
//
// https://github.com/birajpatel/GridListViewAdapters
//
//
//
