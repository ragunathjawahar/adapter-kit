/**
 * Copyright 2014-present Biraj Patel
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.birin.gridlistviewadapters;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.birin.gridlistviewadapters.dataholders.CardDataHolder;
import com.birin.gridlistviewadapters.dataholders.CardPositionInfo;
import com.birin.gridlistviewadapters.dataholders.RowDataHolder;
import com.birin.gridlistviewadapters.utils.ChildViewsClickHandler;
import com.birin.gridlistviewadapters.utils.GridDataStructure;
import com.birin.gridlistviewadapters.utils.OnLoadMoreRequestListener;
import com.birin.gridlistviewadapters.utils.ViewHolderPositionTagger;

/**
 * The Class BaseGridAdapter is a "GRID" wrapper around
 * android.widget.BaseAdapter so that user has to perform minimum operations of
 * 1) Recycling row & card views & holders 2) Conversion from linear data to
 * grid-data 3) mapping various card-views to card-data 4) dispatching load-more
 * events 5)browsing data through data-structure 6) Handling children views with
 * click handling. 7) alignment of cards & rows depending upon spacing. This
 * class has been written with 'default' access so that only library knows about
 * this class, To use this class's functionality use the direct children which
 * are available with public access modifier, eg. ListGridAdapter,
 * CursorGridAdapter.
 * 
 * @param <E>
 *            The POJO class with which this adapter is operating.
 * @param <T>
 *            The CardViewHolder class with which this adapter is operating
 */
abstract class BaseGridAdapter<E, CVH> extends BaseAdapter {

	private final int DEVICE_WT;
	private final int DEVICE_HT;
	private final int CARD_SPACING;

	private final float SPACING_BETWEEN_CARDS_SCREEN_PERCENT = 0.02f;
	private final int CARD_CLICK_EVENT_ID = -99;

	private Context context = null;
	private LayoutInflater inflater = null;
	private OnLoadMoreRequestListener loadMoreRequestListener;
	private List<RowDataHolder> gridStructuredData = null;
	private GridDataStructure dataStructureCreator = null;
	private ChildViewsClickHandler childViewsClickHandler;
	private ViewHolderPositionTagger positionTagger;

	public BaseGridAdapter(Context context, int totalCardsInRow) {
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.dataStructureCreator = getDataStructureManager(totalCardsInRow);
		this.gridStructuredData = new ArrayList<RowDataHolder>();
		DisplayMetrics displaymetrics = context.getResources()
				.getDisplayMetrics();
		DEVICE_WT = displaymetrics.widthPixels;
		DEVICE_HT = displaymetrics.heightPixels;
		CARD_SPACING = getCardSpacing();
		positionTagger = getPositionTagger();
		childViewsClickHandler = new ChildViewsClickHandler(
				childrenOnClickListener);
	}

	/**
	 * Gets the data structure manager class which is responsible for creating
	 * grid-structure of {@link CardPositionInfo}
	 * 
	 * @param totalCardsInRow
	 *            the total cards in row
	 * @return the grid data structure manager
	 */
	protected GridDataStructure getDataStructureManager(int totalCardsInRow) {
		return new GridDataStructure(totalCardsInRow);
	}

	/**
	 * Gets the position tagger instance, this class helps us tag our current
	 * card position to views present in user's ViewHolder class in each
	 * getView() call which will help us build memory efficient click mechanism
	 * by using only single instance of view click listener.
	 * 
	 * @return the position tagger instance.
	 * @see ViewHolderPositionTagger
	 */
	protected ViewHolderPositionTagger getPositionTagger() {
		return new ViewHolderPositionTagger();
	}

	/**
	 * Gets total number of rows present in grid list.If you are looking for
	 * total number of card then use {@link #getAbsoluteCardsCount()} method.
	 */
	@Override
	public int getCount() {
		if (null != gridStructuredData) {
			return gridStructuredData.size();
		}
		return 0;
	}

	/**
	 * Gets row present at given position which returns instance of
	 * {@link RowDataHolder} which internally holds list of
	 * {@link CardPositionInfo}
	 * 
	 * @see RowDataHolder
	 */
	@Override
	public RowDataHolder getItem(int position) {
		if (null != gridStructuredData && position < gridStructuredData.size()) {
			return gridStructuredData.get(position);
		}
		return null;
	}

	/**
	 * Gets row position Id.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		RowViewHolder<CVH> rowViewHolder = null;
		RowDataHolder rowDataHolder = getItem(position);
		int totalCardsInRow = rowDataHolder.getCardPositionInfos().size();
		// Set Views & ViewHolders
		if (rowView == null) {
			rowView = getNewRowView();
			rowViewHolder = getNewRowViewHolder();
			addNewCardsInRow((LinearLayout) rowView, rowViewHolder,
					totalCardsInRow);
			rowView.setTag(rowViewHolder);
		} else {
			rowViewHolder = getTaggedRowViewHolder(rowView);
		}
		// Set views
		setCardViews(rowView, rowViewHolder, rowDataHolder);
		setRowView(rowView, position);
		setRowSpacing(rowView, position);
		setRowViewProperties(rowView);
		checkAndDispatchLoadMoreEvent(position);
		return rowView;
	}

	/**
	 * Gets the tagged RowViewHolder from rowView.
	 * 
	 * @param rowView
	 *            the row view for which ViewHolder is needed.
	 * @return the tagged row view holder
	 */
	@SuppressWarnings("unchecked")
	private RowViewHolder<CVH> getTaggedRowViewHolder(View rowView) {
		return (RowViewHolder<CVH>) rowView.getTag();
	}

	/**
	 * Gets the new Row View, into which cards will be added horizontally.
	 * 
	 * @return the new row view
	 */
	private View getNewRowView() {
		return new LinearLayout(context);
	}

	/**
	 * Gets the new RowViewHolder, which internally holds CVH list.
	 * 
	 * @return the new row view holder
	 * @see RowViewHolder<CVH> where CVH stands for CardViewHolders
	 */
	private RowViewHolder<CVH> getNewRowViewHolder() {
		return new RowViewHolder<CVH>();
	}

	/**
	 * Check and dispatch load more event.
	 * 
	 * @param position
	 *            If its a last position & listener is also set then dispatch
	 *            the event.
	 */
	private void checkAndDispatchLoadMoreEvent(int position) {
		if (position == getCount() - 1 && isLoadMoreRequestListenerSet()) {
			dispatchLoadMoreRequest();
		}
	}

	private void dispatchLoadMoreRequest() {
		loadMoreRequestListener.onLoadMoreRequested();
	}

	/**
	 * Here we apply some properties to RowView container which helps us stop
	 * any user touch events on container,since this touch event should only be
	 * intended on cards & not on row.
	 * 
	 * @param rowView
	 *            the row view for which properties needs to be applied.
	 */
	protected void setRowViewProperties(View rowView) {
		rowView.setClickable(true);
		rowView.setEnabled(false);
		rowView.setFocusable(false);
	}

	/**
	 * Sets the row spacing, applies spacing from left,right,top & if its last
	 * row applies spacing at bottom as well.
	 * 
	 * @param rowView
	 *            the row view for which spacing needs to be applied.
	 * @param position
	 *            the position of the row.
	 */
	private void setRowSpacing(View rowView, int position) {
		final int topPadding = getRowSpacing();
		final int bottomPadding;
		boolean isLastRow = (position == getCount() - 1); // 0 based positions
		if (isLastRow == true) {
			bottomPadding = topPadding;
		} else {
			bottomPadding = 0;
		}
		rowView.setPadding(0, topPadding, 0, bottomPadding);
	}

	/**
	 * Set up all the cards present in a row.
	 * 
	 * @param rowView
	 *            the row view whose cards need to be setup.
	 * @param rowViewHolder
	 *            the RowViewHolder which internally holds CardViewHolders of
	 *            all cards.
	 * @param rowDataHolder
	 *            the row data holder which internally holds CardDataHolders of
	 *            all cards.
	 */
	private void setCardViews(View rowView, RowViewHolder<CVH> rowViewHolder,
			RowDataHolder rowDataHolder) {
		int totalCardsInRow = rowDataHolder.getCardPositionInfos().size();
		for (int positionInRow = 0; positionInRow < totalCardsInRow; positionInRow++) {
			View cardView = rowView.findViewWithTag(positionInRow);
			CardPositionInfo cardPositionInfo = rowDataHolder
					.getCardPositionInfos().get(positionInRow);
			CVH cardViewHolder = rowViewHolder.getCardViewHolders().get(
					positionInRow);
			setSingleCardView(cardView, cardViewHolder, cardPositionInfo);
		}
	}

	/**
	 * Sets the single card view.
	 * 
	 * @param cardView
	 *            the card view which needs to be updated depending upon card
	 *            type (Empty/Content)
	 * @param cardViewHolder
	 *            the card view holder to be passed to user.
	 * @param cardPositionInfo
	 *            the card position info which holds card data type & its
	 *            absolute position.
	 */
	private void setSingleCardView(View cardView, CVH cardViewHolder,
			CardPositionInfo cardPositionInfo) {
		if (cardPositionInfo.isEmptyCard() == true) {
			hideCardView(cardView);
		} else {
			setVisibleCardViews(cardView, cardViewHolder, cardPositionInfo);
		}
	}

	/**
	 * creates void space in a row by hiding this card view.
	 * 
	 * @param cardView
	 *            the card view that needs to be hidden
	 */
	private void hideCardView(View cardView) {
		cardView.setVisibility(View.GONE);
	}

	/**
	 * Sets the visible card views, also scan the CardViewHolder to check if any
	 * of child views were registered for click events.
	 * 
	 * This method is also responsible for registering for
	 * {@link #onCardClicked(Object)}
	 * 
	 * @param cardView
	 *            the card view itself.
	 * @param cardViewHolder
	 *            the CardViewHolder which needs to be scanned or passed to User
	 *            to set view values.
	 * @param cardPositionInfo
	 *            the card positional info like its type, absolute-position etc.
	 */
	private void setVisibleCardViews(View cardView, CVH cardViewHolder,
			CardPositionInfo cardPositionInfo) {
		cardView.setVisibility(View.VISIBLE);
		final int absoluteCardPosition = cardPositionInfo
				.getAbsolutePositionValue();
		positionTagger.scanAndTagViewsWithPositionValue(cardViewHolder,
				absoluteCardPosition);
		E cardData = getCardData(absoluteCardPosition);
		CardDataHolder<E> cardDataHolder = new CardDataHolder<E>(cardData,
				cardPositionInfo);
		setCardView(cardDataHolder, cardViewHolder);
		registerCardForClickEvent(cardView, absoluteCardPosition);
	}

	/**
	 * Register card for click event with its absolute position &
	 * {@link #CARD_CLICK_EVENT_ID}
	 * 
	 * @param cardView
	 *            the card view which needs to be registered.
	 * @param absoluteCardPosition
	 *            the absolute card position in grid.
	 */
	private void registerCardForClickEvent(View cardView,
			int absoluteCardPosition) {
		ChildViewsClickHandler.tagPositionValueToView(cardView,
				absoluteCardPosition);
		ChildViewsClickHandler.tagEventIdValueToView(cardView,
				CARD_CLICK_EVENT_ID);
		cardView.setOnClickListener(childrenOnClickListener);
	}

	/**
	 * Checks if its a card click event.
	 * 
	 * @param eventId
	 *            the event id for which view click occurred.
	 * @return true, if is card click event
	 */
	private boolean isCardClickEvent(int eventId) {
		return (eventId == CARD_CLICK_EVENT_ID);
	}

	/**
	 * Adds the new cards in a new RowView by calling {@link #getNewCard(int)}
	 * for each card.
	 * 
	 * @param rowView
	 *            the row view
	 * @param rowViewHolder
	 *            the row view holder
	 * @param totalCardsInRow
	 *            the total cards in row
	 */
	private void addNewCardsInRow(LinearLayout rowView,
			RowViewHolder<CVH> rowViewHolder, int totalCardsInRow) {
		int cardwidth = getCardWidth(totalCardsInRow);
		LinearLayout.LayoutParams layoutParams = null;
		CVH cardViewHolder = null;
		for (int positionInRow = 0; positionInRow < totalCardsInRow; positionInRow++) {
			boolean isLastCardInaRow = positionInRow == (totalCardsInRow - 1);
			Card<CVH> card = getNewCard(cardwidth);
			View cardView = card.getCardView();
			cardView.setTag(positionInRow);
			layoutParams = getCardLayoutParams(cardwidth, isLastCardInaRow);
			rowView.addView(cardView, layoutParams);
			cardViewHolder = card.getCardViewHolder();
			rowViewHolder.getCardViewHolders().add(cardViewHolder);
			registerChildrenViewClickEvents(cardViewHolder,
					childViewsClickHandler);
		}
	}

	/**
	 * Gets the card layout params, ie it adjust Width & left-right spacing of a
	 * card.
	 * 
	 * @param cardwidth
	 *            the cardWidth to be applied.
	 * @param isLastCardInaRow
	 *            boolean to check if this is last card in a row
	 * @return the card layout params
	 */
	private LayoutParams getCardLayoutParams(int cardwidth,
			boolean isLastCardInaRow) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				cardwidth, LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = CARD_SPACING;
		if (isLastCardInaRow) {
			layoutParams.rightMargin = CARD_SPACING;
		}
		return layoutParams;
	}

	/**
	 * Update grid with new size.
	 * 
	 * @param totalDataSize
	 *            the total data size means total number of cards in new
	 *            Structure.
	 */
	void updateGridWithNewSize(int totalDataSize) {
		if (totalDataSize > 0) {
			dataStructureCreator.updateStructure(gridStructuredData,
					totalDataSize);
		}
		notifyDataSetChanged();
	}

	/**
	 * Invalidate structure, ie delete existing data in grid-structure.
	 */
	void invalidateStructure() {
		gridStructuredData.clear();
	}

	/**
	 * @return the spacing between two rows by default its equal to spacing
	 *         between cards,can be changed by overriding this method.
	 * @see #getCardSpacing()
	 */
	public int getRowSpacing() {
		return getCardSpacing();
	}

	/**
	 * Gets the default card spacing, which is
	 * {@link #SPACING_BETWEEN_CARDS_SCREEN_PERCENT} multiplied by device width,
	 * this spacing can be changed by over-riding this method.
	 * 
	 * @return the card spacing
	 */
	public int getCardSpacing() {
		final int minDeviceDimention = DEVICE_HT > DEVICE_WT ? DEVICE_WT
				: DEVICE_HT;
		return (int) (SPACING_BETWEEN_CARDS_SCREEN_PERCENT * minDeviceDimention);
	}

	/**
	 * Gets the card width for a single card.
	 * 
	 * @param totalCardsInRow
	 *            the total cards in row
	 * @return the single card width
	 */
	public int getCardWidth(int totalCardsInRow) {
		int cardwidth = (DEVICE_WT - ((totalCardsInRow + 1) * CARD_SPACING))
				/ totalCardsInRow;
		return cardwidth;
	}

	public Context getContext() {
		return context;
	}

	public LayoutInflater getLayoutInflater() {
		return inflater;
	}

	public int getDeviceWidth() {
		return DEVICE_WT;
	}

	public int getDeviceHeight() {
		return DEVICE_HT;
	}

	/**
	 * Sets the row view background etc.
	 * 
	 * @param rowView
	 *            the row view
	 * @param position
	 *            the position of row.
	 */
	protected void setRowView(View rowView, int position) {
		rowView.setBackgroundColor(Color.LTGRAY);
	}

	/**
	 * Sets the on load more request listener to get events of load more on
	 * scroll to last item.
	 * 
	 * @param loadMoreRequestListener
	 *            the new on load more request listener
	 * @see OnLoadMoreRequestListener
	 */
	public void setOnLoadMoreRequestListener(
			OnLoadMoreRequestListener loadMoreRequestListener) {
		this.loadMoreRequestListener = loadMoreRequestListener;
	}

	private boolean isLoadMoreRequestListenerSet() {
		return (this.loadMoreRequestListener != null);
	}

	/**
	 * The children on click listener, this is a single object which will be
	 * registered for all children so in turn it becomes very memory efficient.
	 */
	private OnClickListener childrenOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			int absoluteCardPosition = ChildViewsClickHandler
					.getPositionFromViewTag(view);
			int eventId = ChildViewsClickHandler.getEventIdFromViewTag(view);
			E cardData = getCardData(absoluteCardPosition);
			if (isCardClickEvent(eventId) == true) {
				onCardClicked(cardData);
			} else {
				onChildViewClicked(view, cardData, eventId);
			}
		}
	};

	/**
	 * Gets the card data for given card position.
	 * 
	 * @param absoluteCardPosition
	 *            the absolute card position within grid-structure (in your
	 *            linear structure.)
	 * @return the card data present at given position.
	 */
	public abstract E getCardData(int absoluteCardPosition);

	/**
	 * Gets the absolute cards count, ie total number of cards present in full
	 * grid.
	 * 
	 * @return the absolute cards count
	 */
	public abstract int getAbsoluteCardsCount();

	/**
	 * Validates if given row position is between data size range or throws
	 * exception.
	 * 
	 * @param position
	 *            the position to be verified.
	 */
	public abstract void validatePositionOrThrow(final int position);

	/**
	 * Gets the new card View & its ViewHolder bundled inside {@link Card<CVH>}
	 * where CVH stands for Card's ViewHolder.
	 * 
	 * @param cardwidth
	 *            the cardWidth is passed to user so that other view sizing can
	 *            be done in this method's implementation.
	 * @return the new card
	 * @see Card<CVH>
	 */
	protected abstract Card<CVH> getNewCard(int cardwidth);

	/**
	 * Bind the card view's ViewHolder to its Data.
	 * 
	 * @param cardDataHolder
	 *            the card data holder of a single card.
	 * @param cardViewHolder
	 *            the card view holder of a single card.
	 */
	protected abstract void setCardView(CardDataHolder<E> cardDataHolder,
			CVH cardViewHolder);

	/**
	 * Call-back which user receives when he any of the card gets clicked, the
	 * advantage of this call back is data of that card is also passed so that
	 * user can take appropriate actions on that data.
	 * 
	 * @param cardData
	 *            the data for card which was clicked.
	 */
	protected abstract void onCardClicked(final E cardData);

	/**
	 * Register children view click events present in a card-view, Registering
	 * for such child view click helps get click events in
	 * {@link #onChildViewClicked(View, Object, int)}
	 * 
	 * <p>
	 * NOTE: Using this is functionality to optional, alternatively user can
	 * write their own code to handle clicks for child views present in a row.
	 * </p>
	 * 
	 * @param cardViewHolder
	 *            the card view holder which holds all children views with a
	 *            card-view.
	 * @param childViewsClickHandler
	 *            This class enables you to register children view present in
	 *            your row by registering only single {@link OnClickListener}
	 *            listener for all children click events, & at runtime it will
	 *            be determined for which row & which child event occurred, for
	 *            detailed description check {@link ChildViewsClickHandler}, All
	 *            registered views will get onClick Events in
	 *            {@link #onChildViewClicked} method for this class.
	 */
	protected abstract void registerChildrenViewClickEvents(CVH cardViewHolder,
			ChildViewsClickHandler childViewsClickHandler);

	/**
	 * On event of user-click, for any of children view which were registered in
	 * {@link #registerChildrenViewClickEvents(CVH, ChildViewsClickHandler)}
	 * this method will be called & user get few input params like,
	 * 
	 * @param clickedChildView
	 *            Registered child-view which was clicked.
	 * @param data
	 *            Data for given row position, the data present at given
	 *            position is retrieved by library & passed to your class so
	 *            that, library users do NOT have to look for their data on
	 *            child-view click events.
	 * @param eventId
	 *            Event Id for clicked view, Pass unique Event ID for all
	 *            children who are registering for ClickEvents this enables you
	 *            to identify click event based on Integer id, instead of
	 *            setting up different onClick handlers for each children.
	 */
	protected abstract void onChildViewClicked(final View clickedChildView,
			final E cardData, final int eventId);

}
