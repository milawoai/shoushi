package com.learn.shoushi.aChangeView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;
import com.learn.shoushi.R;
import com.learn.shoushi.Utils.AppMethods;
import com.learn.shoushi.Utils.Methods;
import com.learn.shoushi.fragment.ListViewState;

import java.util.ArrayList;

/**
 * Created by a0153-00401 on 15/11/12.
 */
public class XListView  extends ListView implements AbsListView.OnScrollListener {

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener {
        public void onRefresh();
        public void onLoadMore();
        public void onDeleteItem(final int index);
    }

    protected float mLastY = -1; // save event y
    protected float mLastX = -1; // save event x
    protected float mFirstY = -1; // save event y
    protected float mFirstX = -1; // save event x
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    protected IXListViewListener mListViewListener;

    // -- header view
    protected XListViewHeader mHeaderView;


    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    protected int mHeaderViewHeight; // header view's height
    protected boolean mEnablePullRefresh = true;
    protected boolean mPullRefreshing = false; // is refreashing.

    private ArrayList<String> mHeaderViewHashList = new ArrayList<String>();
    // -- footer view
    protected XListViewFooter mFooterView;
    protected boolean mEnablePullLoad;
    protected boolean mPullLoading;
    protected boolean mSlideEnable = false;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    protected int mTotalItemCount;
    protected int firstVisibleItem;
    private int mAheadPullUpCount = 0; // 提前调用pullup的提前量
    private int mLastVisibleIndex = -1;
    private int mLastItemCount = -1;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    protected final static int SCROLL_DURATION = 400; // scroll back duration
    protected final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    protected final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.
    protected int mFlipDirection = 0;//0-vertical 1:horizon
    protected int mTouchSlop;
    protected int selectedItemPosition = -1;
    protected boolean isDeleteBtnShown = false;
    protected boolean isSkip = false;

    /* 滑动时出现的按钮 */
    private View btnDelete;
    private int widthBtnDelete;

    /* listView的每一个item的布局 */
    private ViewGroup viewGroup;
    private View mEmptyErrorView;

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    @SuppressWarnings("deprecation")
    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new XListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView
                .findViewById(R.id.xlistview_header_content);
        mHeaderTimeView = (TextView) mHeaderView
                .findViewById(R.id.xlistview_header_time);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new XListViewFooter(context);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
        ViewConfiguration config = ViewConfiguration.get(context);
//		mTouchSlop = config.getScaledTouchSlop();
        mTouchSlop = AppMethods.computePixelsWithDensity(7);
//		mTouchSlop= 0;
        mEmptyErrorView = new EmptyErrorView(context);
        View btnReload = mEmptyErrorView.findViewById(R.id.list_network_error_reload_tv);
        btnReload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mListViewListener != null) {
                    mListViewListener.onRefresh();
                }
            }
        });
    }

//	public void setXListViewHeaderStyle(int style) {
//		if(style == 1) {
//			mHeaderView.setBackgroundResource(R.color.common_orange_text);
//			mHeaderView.setXListViewHeaderStyle(style);
//		}
//	}

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);
    }

    @Override
    public void addHeaderView(View v) {
        int viewHash = v.hashCode();
        boolean isAdded = false;
        if(mHeaderViewHashList != null && mHeaderViewHashList.size() > 0) {
            for(String hash : mHeaderViewHashList) {
                if(hash != null && hash.equals(String.valueOf(viewHash))) {
                    isAdded = true;
                    return;
                }
            }
        }
        mHeaderViewHashList.add(String.valueOf(viewHash));
        super.addHeaderView(v);
    }

    @Override
    public boolean removeHeaderView(View v) {
        int viewHash = v.hashCode();
        boolean isAdded = false;
        if(mHeaderViewHashList != null && mHeaderViewHashList.size() > 0) {
            for(String hash : mHeaderViewHashList) {
                if(hash != null && hash.equals(String.valueOf(viewHash))) {
                    isAdded = true;
                    break;
                }
            }
            if(isAdded) {
                mHeaderViewHashList.remove(String.valueOf(viewHash));
            }
        }
        return super.removeHeaderView(v);
    }


    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
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
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
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
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
    }

    public void setSlideEnable(boolean isEnable) {
        mSlideEnable = isEnable;
        if(mSlideEnable) {
            mFlipDirection = -1;
        } else {
            mFlipDirection = 0;
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    protected void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    protected void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta
                + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    protected void resetHeaderHeight() {
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

    protected void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
                // more.
                mFooterView.setState(XListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
        }
        mFooterView.setBottomMargin(height);

//		setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    protected void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
                    SCROLL_DURATION);
            invalidate();
        }
    }

    protected void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(XListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getY();
        }
        if (mLastX == -1) {
            mLastX = ev.getX();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstX = ev.getX();
                mFirstY = ev.getY();
                mLastX = ev.getX();
                mLastY = ev.getY();
                isSkip = false;
                if(mSlideEnable) {
                    mFlipDirection = -1;
                    if(isDeleteBtnShown) {
                        Rect rect = new Rect();
                        btnDelete.getGlobalVisibleRect(rect);
                        if(rect != null) {
                            widthBtnDelete = rect.right - rect.left;
                        }
                        int rawX = (int) ev.getRawX();
                        int rawY = (int) ev.getRawY();
                        if(rect != null && rect.contains(rawX, rawY)) {
                            Log.i("changxin", "onclick " + selectedItemPosition);
                            isSkip = true;
                            btnDelete.performClick();
                            return true;
                        } else {
                            buttonHide(btnDelete);
                            viewGroup.removeView(btnDelete);
                            isDeleteBtnShown = false;
                            isSkip = true;
                        }
                        return true;
                    } else {
                        selectedItemPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                    }
                } else {
                    mFlipDirection = 0;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mSlideEnable && isSkip) {
                    return true;
                }
                if(mSlideEnable && mFlipDirection == -1) {
                    if(Math.abs(ev.getY() - mFirstY) > mTouchSlop) {
                        mFlipDirection = 0;
                    } else if(Math.abs(ev.getX() - mFirstX) > mTouchSlop) {
                        mFlipDirection = 1;
                    }
//				if(Math.abs(ev.getY() - mFirstY) > 0 || Math.abs(ev.getX() - mFirstX) > 0) {
//					if(Math.abs(ev.getY() - mFirstY) >= Math.abs(ev.getX() - mFirstX)) {
//						mFlipDirection = 0;
//					} else {
//						mFlipDirection = 1;
//					}
//				}
                }
                if(mFlipDirection == 0) {
                    final float deltaX = ev.getX() - mLastX;
                    mLastX = ev.getX();
                    final float deltaY = ev.getY() - mLastY;
                    mLastY = ev.getY();
                    if (getFirstVisiblePosition() == 0
                            && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                        // the first item is showing, header has shown or pull down.
                        updateHeaderHeight(deltaY / OFFSET_RADIO);
                        invokeOnScrolling();
                    } else if (mEnablePullLoad && getLastVisiblePosition() == mTotalItemCount - 1
                            && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                        // last item, already pulled up or want to pull up.
                        updateFooterHeight(-deltaY / OFFSET_RADIO);
                    }
                } else if(mFlipDirection == 1) {
                    if(selectedItemPosition < this.getHeaderViewsCount()
                            || selectedItemPosition >= this.getCount() - this.getFooterViewsCount()) {
                        return super.dispatchTouchEvent(ev);
                    }
                    if(ev.getX() - mFirstX >= 0) {
                        if(isDeleteBtnShown) {
                            Log.i("changxin", "右滑");
                            buttonHide(btnDelete);
                            viewGroup.removeView(btnDelete);
                            isDeleteBtnShown = false;
                        }
                    } else {
                        if(!isDeleteBtnShown) {
//						Log.i("changxin", "左滑");
                            if (mListViewListener != null) {
                                btnDelete = LayoutInflater.from(getContext()).inflate(R.layout.slide_delete_button, null);
                                btnDelete.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        viewGroup.removeView(btnDelete);
                                        btnDelete = null;
                                        isDeleteBtnShown = false;
                                        mListViewListener.onDeleteItem(selectedItemPosition);

                                    }
                                });
                                final HorizontalScrollView view = (HorizontalScrollView) getChildAt(
                                        selectedItemPosition - getFirstVisiblePosition());
                                viewGroup = (LinearLayout) view.findViewById(R.id.list_item_optional_operation_layout);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                                layoutParams.gravity = Gravity.RIGHT;
                                btnDelete.setLayoutParams(layoutParams);
                                viewGroup.addView(btnDelete);
                                if(widthBtnDelete <= 0) {
                                    widthBtnDelete = Methods.computePixelsWithDensity(65);
                                }
                                Animation.AnimationListener listener = new Animation.AnimationListener() {

                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        view.postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                Log.i("changxin", "widthBtnDelete: " + widthBtnDelete);
                                                view.smoothScrollTo(widthBtnDelete, 0);
                                            }
                                        }, 20);
                                    }
                                };
                                buttonShow(btnDelete, listener);
                                isDeleteBtnShown = true;
                            }
                        }
                    }
                    return false;
                }
                break;
            default:
                mLastX = -1; // reset
                mLastY = -1; // reset
                mFirstX = -1;
                mFirstY = -1;
                if(mSlideEnable && isSkip) {
                    return true;
                }
                if(!mSlideEnable || mFlipDirection != 1) {
                    if (getFirstVisiblePosition() == 0) {
                        // invoke refresh
                        if (mEnablePullRefresh
                                && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                            mPullRefreshing = true;
                            mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                            if (mListViewListener != null) {
                                mListViewListener.onRefresh();
                            }
                        }
                        resetHeaderHeight();
                    } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                        // invoke load more.
                        if (mEnablePullLoad
                                && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
                                && !mPullLoading) {
                            startLoadMore();
                        }
                        resetFooterHeight();
                    }
                }
                if(mSlideEnable && mFlipDirection == 1) {
                    Log.i("changxin", "横向滑动结束");
                    mFlipDirection = -1;
                    return true;
                }
                mFlipDirection = -1;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void buttonShow(View v, Animation.AnimationListener listener) {
        Log.i("changxin", "buttonShow");
        if(v != null) {
            v.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(
                    getContext(), R.anim.slide_delete_button_show);
            if(listener != null) {
                anim.setAnimationListener(listener);
            }
            v.startAnimation(anim);
        }
    }

    protected void buttonHide(final View v) {
        Log.i("changxin", "buttonHide");
        if(v != null) {
            Animation anim = AnimationUtils.loadAnimation(
                    getContext(),R.anim.slide_delete_button_hide);
            anim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            });
            v.startAnimation(anim);
        }
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
        if(state!=null){
            if(scrollState == SCROLL_STATE_IDLE ){
                state.listViewState(getLastVisiblePosition(),true);
            }else{
                state.listViewState(getLastVisiblePosition(),false);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
        if(mEnablePullLoad) {
            this.firstVisibleItem = firstVisibleItem;
            int lastVisibleItem = firstVisibleItem + visibleItemCount;
            // 距离底部mAheadPullUpCount调用加载更多，或滑动到底部也掉加载更多(为了解决加载出来的总数小于mAheadPullUpCount，导致不自动加载)
            if ((lastVisibleItem == totalItemCount && lastVisibleItem != mLastVisibleIndex)
                    || (lastVisibleItem + mAheadPullUpCount >= totalItemCount && mLastVisibleIndex + mAheadPullUpCount < mLastItemCount)) {
                // 在最后一个item内移动时，不要触发loadmore
                if (!mEnablePullLoad || mPullLoading)
                    return;
                // 数量充满屏幕才触发
                if (isFillScreenItem()) {
                    startLoadMore();
                }
            }
            mLastVisibleIndex = lastVisibleItem;
            mLastItemCount = totalItemCount;
        }
    }

    /**
     * 条目是否填满整个屏幕
     */
    private boolean isFillScreenItem() {
        final int firstVisiblePosition = getFirstVisiblePosition();
        final int lastVisiblePostion = getLastVisiblePosition() - getFooterViewsCount();
        final int visibleItemCount = lastVisiblePostion - firstVisiblePosition + 1;
        final int totalItemCount = getCount() - getFooterViewsCount();
        if (visibleItemCount < totalItemCount)
            return true;
        return false;
    }


    public void setState(ListViewState state) {
        this.state = state;
    }

    private ListViewState state;
    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }



    public void setFooterViewVisible(boolean flag) {
        if (mEnablePullLoad && !flag) {
            mFooterView.hide();
        } else if (mEnablePullLoad && flag) {
            mFooterView.show();
        }
    }

    public boolean isPullRefreshing() {
        return mPullRefreshing;
    }


    public void setFooterText(String content){
        if(mFooterView!=null){
            mFooterView.show();
            this.setFooterViewVisible(true);
            mFooterView.setFooterText(content);
        }
    }

    public View getFooterView(){
        return mFooterView;
    }

    public void showNetError() {
//    	this.post(new Runnable() {
//
//			@Override
//			public void run() {
        ListAdapter adapter = this.getAdapter();
        int count = adapter.getCount();
        if(count > this.getHeaderViewsCount() + this.getFooterViewsCount()) {
            setFooterViewVisible(true);
            this.removeHeaderView(mEmptyErrorView);
        } else {
            setFooterViewVisible(false);
            this.addHeaderView(mEmptyErrorView);
        }
//			}
//		});
    }

}
