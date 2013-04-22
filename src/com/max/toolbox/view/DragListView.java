
package com.max.toolbox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @类名 DragListView 数据源不能使用数组
 * @作者 YULIANGMAX
 * @日期 2013-4-21
 * @版本 1.0
 */
public class DragListView extends ListView {

    // private static final String TAG = "DragListView";

    private static final int INVALID_POSITION = -1;
    private Bitmap mDragBitmap;
    private View mDragView;
    private Object srcContent;
    private int startPosition;
    private final Paint mPaint = new Paint();

    private float mLastMotionX;
    private float mLastMotionY;
    private float mTouchOffsetX;
    private float mTouchOffsetY;
    private float mBitmapOffsetX;
    private float mBitmapOffsetY;

    private boolean mDragging = false;

    private final Rect mDragRect = new Rect(); // 当拖动一个item的时候，记录拖动经过的区域
    private final int mScaledTouchSlop;// 是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
    private float mTopScrollBound;// 向上滑动超过这个边界的时候，上面的item向下滚动
    private float mBottomScrollBound;// 向下滑动超过这个边界的时候，下面的item开始向上滚动

    public DragListView(Context context) {
        this(context, null);
    }

    public DragListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 当某个item被拖拽时，将其颜色改变
        final int srcColor = context.getResources().getColor(Color.GREEN);
        mPaint.setColorFilter(new PorterDuffColorFilter(srcColor, PorterDuff.Mode.SRC_ATOP));
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mDragging && mDragBitmap != null) {
            final int scrollX = getScrollX();
            final int scrollY = getScrollY();
            float left = scrollX + mLastMotionX - mTouchOffsetX - mBitmapOffsetX;
            float top = scrollY + mLastMotionY - mTouchOffsetY - mBitmapOffsetY;
            canvas.drawBitmap(mDragBitmap, left, top, mPaint);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        /**
         * 判断当前用户按下事件所在的位置 如果该位置在右边30dip之内，就进行拖拽
         */
        int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        // if (getWidth() - x > 30) {
        // return super.onInterceptTouchEvent(event);// 不位于拖拽区域，直接返回
        // }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            default:
                break;
        }
        // Log.e(TAG, "touchSlop:" + mScaledTouchSlop);
        // 计算滚动边界
        /**
         * 这里当向上拖拽到1/3屏幕高度的时候就滚动， 但是这里如果开始拖拽的item就是位于这1/3屏幕内位置的 则，取当前小的一个.当然你也就可以设置成1/3的屏幕
         */
        mTopScrollBound = Math.min(y - mScaledTouchSlop, getHeight() / 3);
        mBottomScrollBound = Math.min(y + mScaledTouchSlop, getHeight() * 2 / 3);

        // mTopScrollBound = getHeight()/3;
        // mBottomScrollBound = getHeight()*2/3;

        // Log.e(TAG, "bound scroll:" + mTopScrollBound + "," +
        // mBottomScrollBound);

        int position = this.pointToPosition((int) x, (int) y);// 获取当前事件坐标所在的item项,ListView中拖动是上下拖动的,所以,和x坐标关系不大

        if (position == INVALID_POSITION) {
            return super.onInterceptTouchEvent(event);
        }

        startPosition = position;

        /**
         * 这里获取当前被拖拽的item,注意,因为ListView中并不是每个item都是一个View，这个View是重用的
         */
        mDragView = getChildAt(position - getFirstVisiblePosition());
        srcContent = getItemAtPosition(position);

        if (mDragView != null) {
            mDragBitmap = createViewBitmap(mDragView);
            // 当item被拖拽后,删除原来位置上的item,其实就是将Adapter中该位置的内容给删掉
            /**
             * 注意，该ListView用的Adapter是ArrayAdapter，其有remove(Object item)方法
             * 但是，经常处理ListView显示数据的应该知道，ArrayList的remove(object)方法实现了，
             * 但是我们使用ArrayAdatper的时候，如果我们的数据传递到Adapter用的是数组，那么ArrayAdapter内
             * 默认使用Arrays.asList(Object[])方法将数组转换为List对象，这样，当我们调用ArrayList的
             * removeItem方法的时候 ，就会出现java.lang.UnsupportedOperationException异常。这是因为
             * Arrays.asList( Object[])转换后的List是Arrays.ArrayList对象，而不是java.utils.ArrayList
             * Arrays.ArrayList并没有实现remove方法，所以，执行父类AbstractList默认的remove方法，而
             * AbstractList的remove方法就是抛出一个UnsupportedOperationException异常
             */
            removeItem(position);
            mDragging = true;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void removeItem(int position) {
        ArrayAdapter<Object> adapter = (ArrayAdapter<Object>) this.getAdapter();
        adapter.remove(adapter.getItem(position));
    }

    @SuppressWarnings("unchecked")
    private void addItem(int position) {
        ArrayAdapter<Object> adapter = (ArrayAdapter<Object>) this.getAdapter();
        adapter.insert(srcContent, position);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (!mDragging) {
            return false;
        }
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                final int scrollX = getScrollX();
                final int scrollY = getScrollY();

                int left = (int) (scrollX + mLastMotionX - mTouchOffsetX - mBitmapOffsetX);
                int top = (int) (scrollY + mLastMotionY - mTouchOffsetY - mBitmapOffsetY);

                final int width = mDragBitmap.getWidth();
                final int height = mDragBitmap.getHeight();

                mDragRect.set(left - 1, top - 1, width + left + 1, top + height + 1);
                // Log.e(TAG, "每次move的距离:" + (y - mLastMotionY));
                // mLastMotionX = x;
                mLastMotionY = y;

                left = (int) (scrollX + mLastMotionX - mTouchOffsetX - mBitmapOffsetX);
                top = (int) (scrollY + mLastMotionY - mTouchOffsetY - mBitmapOffsetY);

                mDragRect.union(left - 1, top - 1, width + left + 1, top + height + 1);

                invalidate(mDragRect);

                int scrollHeight = 0;

                if (y < mTopScrollBound) {
                    /**
                     * 如果当前move到的y位置为mTopScrollBound之上，则下面获取当前位置的item,
                     * 并将该item在当前位置的基础上，向下移动15个dip的偏移量，这样,当向上drag的时候 后面的item向下在滚动
                     * 向下拖拽的时候同理
                     */
                    scrollHeight = 15;
                } else if (y > mBottomScrollBound) {
                    scrollHeight = -15;
                }

                if (scrollHeight != 0) {
                    // 调用Listview方法实现滚动
                    int position = this.pointToPosition((int) x, (int) y);
                    if (position != INVALID_POSITION) {
                        /**
                         * 将当前位置的item向上或者向下移动scrollHeight个单位的距离
                         */
                        setSelectionFromTop(position, getChildAt(position - getFirstVisiblePosition()).getTop() + scrollHeight);
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                int position = this.pointToPosition((int) x, (int) y);
                if (position == INVALID_POSITION) {
                    position = startPosition;
                }
                float listTop = getChildAt(0).getTop();
                float listBottom = getChildAt(getChildCount() - 1).getBottom(); // 这个是ListView可视区域的最下方，但是不一定数据集中的最后一项的位置
                if (y < listTop) {
                    position = 0;
                } else if (y > listBottom) {
                    /**
                     * 因为向下拖动的时候，ListView是向下滑动的 这里当item被往下拖到最下面时,将其添加到数据集中最后一个位置
                     * 这里注意是getCount()而不是getCount()-1。是向最后插入一个
                     */
                    position = getAdapter().getCount();
                }
                stopDrag(position);
                break;
        }
        return true;
    }

    private void stopDrag(int newPosition) {
        mDragging = false;
        if (mDragBitmap != null) {
            mDragBitmap.recycle();
        }
        // 将拖动的item加入新的位置
        addItem(newPosition);
        invalidate();
    }

    /**
     * 用当前View的绘制缓冲区创建一个Bitmap 同时进行一定的缩放
     * 
     * @param view
     * @return
     */
    private Bitmap createViewBitmap(View view) {
        final int left = view.getLeft();
        final int top = view.getTop();
        // 当前按住的位置相对于View本身的偏移量
        mTouchOffsetX = mLastMotionX - left;
        mTouchOffsetY = mLastMotionY - top;

        view.clearFocus();
        view.setPressed(false);

        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        // 使用一个简单的Scale缩放
        Matrix matrix = new Matrix();
        float scaleFactorH = view.getHeight();
        float scaleFactorW = view.getWidth();
        scaleFactorH = (scaleFactorH + 100) / scaleFactorH;
        scaleFactorW = (scaleFactorW + 0) / scaleFactorW;
        matrix.setScale(scaleFactorW, scaleFactorH);

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);

        // 创建的缩放后的Bitmap和原Bitmap的padding
        mBitmapOffsetX = (newBitmap.getWidth() - width) / 2;
        mBitmapOffsetY = (newBitmap.getHeight() - height) / 2;

        return newBitmap;
    }

}
