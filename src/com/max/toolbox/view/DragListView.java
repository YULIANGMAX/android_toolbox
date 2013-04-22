
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
 * @���� DragListView ����Դ����ʹ������
 * @���� YULIANGMAX
 * @���� 2013-4-21
 * @�汾 1.0
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

    private final Rect mDragRect = new Rect(); // ���϶�һ��item��ʱ�򣬼�¼�϶�����������
    private final int mScaledTouchSlop;// ��һ�����룬��ʾ������ʱ���ֵ��ƶ�Ҫ�����������ſ�ʼ�ƶ��ؼ���
    private float mTopScrollBound;// ���ϻ�����������߽��ʱ�������item���¹���
    private float mBottomScrollBound;// ���»�����������߽��ʱ�������item��ʼ���Ϲ���

    public DragListView(Context context) {
        this(context, null);
    }

    public DragListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // ��ĳ��item����קʱ��������ɫ�ı�
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
         * �жϵ�ǰ�û������¼����ڵ�λ�� �����λ�����ұ�30dip֮�ڣ��ͽ�����ק
         */
        int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        // if (getWidth() - x > 30) {
        // return super.onInterceptTouchEvent(event);// ��λ����ק����ֱ�ӷ���
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
        // ��������߽�
        /**
         * ���ﵱ������ק��1/3��Ļ�߶ȵ�ʱ��͹����� �������������ʼ��ק��item����λ����1/3��Ļ��λ�õ� ��ȡ��ǰС��һ��.��Ȼ��Ҳ�Ϳ������ó�1/3����Ļ
         */
        mTopScrollBound = Math.min(y - mScaledTouchSlop, getHeight() / 3);
        mBottomScrollBound = Math.min(y + mScaledTouchSlop, getHeight() * 2 / 3);

        // mTopScrollBound = getHeight()/3;
        // mBottomScrollBound = getHeight()*2/3;

        // Log.e(TAG, "bound scroll:" + mTopScrollBound + "," +
        // mBottomScrollBound);

        int position = this.pointToPosition((int) x, (int) y);// ��ȡ��ǰ�¼��������ڵ�item��,ListView���϶��������϶���,����,��x�����ϵ����

        if (position == INVALID_POSITION) {
            return super.onInterceptTouchEvent(event);
        }

        startPosition = position;

        /**
         * �����ȡ��ǰ����ק��item,ע��,��ΪListView�в�����ÿ��item����һ��View�����View�����õ�
         */
        mDragView = getChildAt(position - getFirstVisiblePosition());
        srcContent = getItemAtPosition(position);

        if (mDragView != null) {
            mDragBitmap = createViewBitmap(mDragView);
            // ��item����ק��,ɾ��ԭ��λ���ϵ�item,��ʵ���ǽ�Adapter�и�λ�õ����ݸ�ɾ��
            /**
             * ע�⣬��ListView�õ�Adapter��ArrayAdapter������remove(Object item)����
             * ���ǣ���������ListView��ʾ���ݵ�Ӧ��֪����ArrayList��remove(object)����ʵ���ˣ�
             * ��������ʹ��ArrayAdatper��ʱ��������ǵ����ݴ��ݵ�Adapter�õ������飬��ôArrayAdapter��
             * Ĭ��ʹ��Arrays.asList(Object[])����������ת��ΪList���������������ǵ���ArrayList��
             * removeItem������ʱ�� ���ͻ����java.lang.UnsupportedOperationException�쳣��������Ϊ
             * Arrays.asList( Object[])ת�����List��Arrays.ArrayList���󣬶�����java.utils.ArrayList
             * Arrays.ArrayList��û��ʵ��remove���������ԣ�ִ�и���AbstractListĬ�ϵ�remove��������
             * AbstractList��remove���������׳�һ��UnsupportedOperationException�쳣
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
                // Log.e(TAG, "ÿ��move�ľ���:" + (y - mLastMotionY));
                // mLastMotionX = x;
                mLastMotionY = y;

                left = (int) (scrollX + mLastMotionX - mTouchOffsetX - mBitmapOffsetX);
                top = (int) (scrollY + mLastMotionY - mTouchOffsetY - mBitmapOffsetY);

                mDragRect.union(left - 1, top - 1, width + left + 1, top + height + 1);

                invalidate(mDragRect);

                int scrollHeight = 0;

                if (y < mTopScrollBound) {
                    /**
                     * �����ǰmove����yλ��ΪmTopScrollBound֮�ϣ��������ȡ��ǰλ�õ�item,
                     * ������item�ڵ�ǰλ�õĻ����ϣ������ƶ�15��dip��ƫ����������,������drag��ʱ�� �����item�����ڹ���
                     * ������ק��ʱ��ͬ��
                     */
                    scrollHeight = 15;
                } else if (y > mBottomScrollBound) {
                    scrollHeight = -15;
                }

                if (scrollHeight != 0) {
                    // ����Listview����ʵ�ֹ���
                    int position = this.pointToPosition((int) x, (int) y);
                    if (position != INVALID_POSITION) {
                        /**
                         * ����ǰλ�õ�item���ϻ��������ƶ�scrollHeight����λ�ľ���
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
                float listBottom = getChildAt(getChildCount() - 1).getBottom(); // �����ListView������������·������ǲ�һ�����ݼ��е����һ���λ��
                if (y < listTop) {
                    position = 0;
                } else if (y > listBottom) {
                    /**
                     * ��Ϊ�����϶���ʱ��ListView�����»����� ���ﵱitem�������ϵ�������ʱ,������ӵ����ݼ������һ��λ��
                     * ����ע����getCount()������getCount()-1������������һ��
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
        // ���϶���item�����µ�λ��
        addItem(newPosition);
        invalidate();
    }

    /**
     * �õ�ǰView�Ļ��ƻ���������һ��Bitmap ͬʱ����һ��������
     * 
     * @param view
     * @return
     */
    private Bitmap createViewBitmap(View view) {
        final int left = view.getLeft();
        final int top = view.getTop();
        // ��ǰ��ס��λ�������View�����ƫ����
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

        // ʹ��һ���򵥵�Scale����
        Matrix matrix = new Matrix();
        float scaleFactorH = view.getHeight();
        float scaleFactorW = view.getWidth();
        scaleFactorH = (scaleFactorH + 100) / scaleFactorH;
        scaleFactorW = (scaleFactorW + 0) / scaleFactorW;
        matrix.setScale(scaleFactorW, scaleFactorH);

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);

        // ���������ź��Bitmap��ԭBitmap��padding
        mBitmapOffsetX = (newBitmap.getWidth() - width) / 2;
        mBitmapOffsetY = (newBitmap.getHeight() - height) / 2;

        return newBitmap;
    }

}
