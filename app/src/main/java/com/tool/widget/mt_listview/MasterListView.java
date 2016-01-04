package com.tool.widget.mt_listview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;


public class MasterListView extends ListView implements OnScrollListener {

	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private OnRefreshListener mListViewListener;

	// -- header view
	private MasterListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private MasterListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;
	
	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.
	
	private Context contex;
	private SharedPreferences  sharedPreferences;
	private final String PREF_TAG = "master_listview_refresh_time";

	private int tagId;
	/**
	 * @param context
	 */
	public MasterListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public MasterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public MasterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		
		this.contex = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		//
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new MasterListViewHeader(context);
		mHeaderViewContent = mHeaderView.getHeaderViewContent();//(RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = mHeaderView.getHeaderTimeView();//(TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new MasterListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
	}
	
	
	//---------------
	//--------------
	//--------------
	/**
	 * 保存刷新时间MainActivity.java
     */
	public void saveRefreshStrTime(){
		Editor editor = sharedPreferences.edit();
		editor.putString(PREF_TAG, DateUtil.currentTime());
		editor.commit();
	}

	/**
	 * 获取刷新时间
	 * @return
	 */
	public String getRefreshStrTime(){
		return sharedPreferences.getString(PREF_TAG, null);
	}

	//--------------------
	//--------------------
	//--------------------

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * enable or disable pull down refresh feature.
	 *
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
//		if (!mEnablePullRefresh) { // disable, hide the content
//			mHeaderViewContent.setVisibility(View.INVISIBLE);
//		} else {
//			mHeaderViewContent.setVisibility(View.VISIBLE);
//		}
	}

	public void startRotateProgress(){
		mHeaderView.startRotateProgress();
	}

	/**
	 * enable or disable pull up load more feature.
	 *
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(MasterListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * auto refresh with first in
	 */
	public void startRefresh(){
		mPullRefreshing = true ;
		mScroller.startScroll(0, 0, 0, mHeaderViewHeight,SCROLL_DURATION);
		invalidate();
		setPullRefreshEnable(false);
		mHeaderView.startRotateProgress();
		mHeaderView.setState(MasterListViewHeader.STATE_REFRESHING);
	}

	/**
	 * stop refresh, resetAll header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}

		mHeaderView.stopRotateProgress();
		Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				setPullRefreshEnable(true);
			}
		};
		//
		mHandler.sendEmptyMessageDelayed(0, SCROLL_DURATION);
	}

	/**
	 * stop load more, resetAll footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(MasterListViewFooter.STATE_NORMAL);
		}

		mFooterView.setEnabled(true);
	}

	private void setRefreshTime(String timeStr){
		mHeaderTimeView.setText(timeStr);
	}

	/**
	 * 刷新之前调用
	 * @param context
	 */
	public void setRefreshTime(Context context){
		if(!TextUtils.isEmpty(getRefreshStrTime())){
			String strDate = DateUtil.getIntervalOfTwoTime(getRefreshStrTime(), DateUtil.currentTime());
			setRefreshTime("更新时间:" + strDate);
		}else{
			setRefreshTime("更新时间:从未");
		}
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnMasterScrollListener) {
			OnMasterScrollListener l = (OnMasterScrollListener) mScrollListener;
			l.onMasterScrolling(this);
		}
	}

	/**
	 * 更新Header位置
	 * @param delta
	 */
	private void updateHeaderHeight(float delta) {
		doCalculateAndSet();
		mHeaderView.setProgress((int) delta + mHeaderView.getVisiableHeight(), mHeaderViewHeight);
		mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(MasterListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(MasterListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * resetAll header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(MasterListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(MasterListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

//		setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	public void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(MasterListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore(tagId);
		}
		mFooterView.setEnabled(false);
        Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                MasterListView.this.stopLoadMore();
            }
        };
        mHandler.sendEmptyMessageDelayed(0, 4000);
	}

	/**计算刷新时间*/
	private boolean calculatetime = false;

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			calculatetime = false;
//			System.out.println("恢复calculate");
		}
	};

	/**
	 * 计算更新时间
	 */
	private void doCalculateAndSet(){
		if(!calculatetime){
//			System.out.println("计算刷新时间");
			calculatetime = true;
			
			//
			setRefreshTime(contex);
			
			//
			mHandler.sendEmptyMessageDelayed(0, 2 * 1000);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(mEnablePullRefresh){
			if (mLastY == -1) {
				mLastY = ev.getRawY();
			}

			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
					// the first item is showing, header has shown or pull down.
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
					// last item, already pulled up or want to pull up.
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
				break;
			default:
				mLastY = -1; // resetAll
				if (getFirstVisiblePosition() == 0) {
					// invoke refresh
					if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
						mPullRefreshing = true;
						mHeaderView.setState(MasterListViewHeader.STATE_REFRESHING);
						if (mListViewListener != null) {
							mListViewListener.onRefresh(tagId);
	                        Handler mHandler = new Handler(){
	                            @Override
	                            public void handleMessage(Message msg) {
	                                super.handleMessage(msg);
	                                MasterListView.this.stopRefresh();
	                            }
	                        };
	                        mHandler.sendEmptyMessageDelayed(0, 4000);
						}
					}
					resetHeaderHeight();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
						if(mFooterView.isEnabled()) {
							startLoadMore();
						}
					}
					resetFooterHeight();
				}
				break;
			}
		}
		
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,totalItemCount);
		}
	}

	public void setOnRefreshListener(OnRefreshListener l,int id) {
		mListViewListener = l;
		this.tagId = id;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnMasterScrollListener extends OnScrollListener {
		public void onMasterScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface OnRefreshListener {
		public void onRefresh(int id);

		public void onLoadMore(int id);
	}
}
