/**
 * @项目 Android_Demo_PieChart
 * @包名 com.demo.piechart.view
 * @文件 PieChart.java
 * @描述
 * @日期 2013-4-22
 * @版本 1.0
 */

package com.max.toolbox.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.max.toolbox.R;

/**
 * 实现的过程中主要用到一些数学计算来实现角度和屏幕位置坐标的计算。计算任意两个点相对于中心坐标的角度的时候，首先计算每个点相对于x轴正方向的角度，
 * 可以将其转化为计算直角三角形的内角计算问题，再将两次计算的角度进行减法运算，就得到了任意两个点相对于中心坐标的角度。
 */
public class PieChartView extends View {

    public static final String TAG = "PieChart";

    /**
     * 饼图透明度
     */
    public static final int ALPHA = 150;
    /**
     * 初始动画持续时间，数值越大越慢
     */
    public static final int ANIMATION_DURATION = 800;
    /**
     * 动画状态-绘制中
     */
    public static final int ANIMATION_STATE_RUNNING = 1;
    /**
     * 动画状态-绘制完成
     */
    public static final int ANIMATION_STATE_DOWN = 2;

    /**
     * 饼图占据的矩形区域，正圆形饼图应占据正方形。left、top 矩形左上角点的坐标；right、bottom矩形右下角点的坐标。正方形边长 =
     * right - left = bottom - top
     */
    private static RectF OVAL;

    /**
     * 每部分的颜色（饼块颜色）
     */
    private int[] colors;
    /**
     * 每部分的大小（饼块各区）
     */
    private int[] values;
    /**
     * 根据每部分大小值计算出在饼内每块的角度
     */
    private int[] degrees;
    /**
     * 每部分的内容（饼块对应文字）
     */
    private String[] titles;

    /**
     * 画底色
     */
    private Paint paint;
    /**
     * 画蒙版
     */
    private Paint maskPaint;
    /**
     * 画文字
     */
    private Paint textPaint;

    /**
     * 上一次事件的点
     */
    private Point lastEventPoint;
    /**
     * 饼图中心位置
     */
    private Point center;
    /**
     * 事件距离饼图中心的距离
     */
    private int eventRadius = 0;

    /**
     * 当前目标索引位置
     */
    private int currentTargetIndex = -1;

    /**
     * 蒙版位图对象
     */
    private Bitmap mask;

    /**
     * 让初始时圆饼从箭头位置开始画出
     */
    private int startDegree = 90;
    /**
     * 动画状态
     */
    private int animState = ANIMATION_STATE_DOWN;
    /**
     * 启动动画开关
     */
    private boolean animEnabled = false;
    /**
     * 动画启动时间
     */
    private long animStartTime;

    /**
     * 上下文
     */
    private Context context;

    /**
     * view宽度
     */
    private int width = 0;
    /**
     * view高度
     */
    private int height = 0;

    /**
     * 字体大小
     */
    private final float textSize = 20.0f;

    public PieChartView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);

        // textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setAlpha(ALPHA);
        textPaint.setTextSize(textSize);

        values = new int[] {
                60,
                90,
                30,
                50,
                70
        };

        titles = new String[] {
                "王尼玛王尼玛王尼玛王尼玛",
                "赵泥煤赵泥煤赵泥煤赵泥煤",
                "秦寿生秦寿生秦寿生秦寿生",
                "史珍香史珍香史珍香史珍香",
                "沈京兵沈京兵沈京兵沈京兵"
        };

        colors = new int[] {
                Color.argb(ALPHA, 249, 64, 64),
                Color.argb(ALPHA, 0, 255, 0),
                Color.argb(ALPHA, 255, 0, 255),
                Color.argb(ALPHA, 255, 255, 0),
                Color.argb(ALPHA, 0, 255, 255)
        };

        degrees = getDegrees();
        mask = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
        animEnabled = true; // 启动动画
    }

    /**
     * 根据每部分所占的比例，来计算每个区域在整个圆中所占的角度
     * 
     * @return
     */
    private int[] getDegrees() {
        int sum = this.sum(values);
        int[] degrees = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            degrees[i] = (int) Math.floor((double) values[i] / (double) sum * 360);
        }
        int angleSum = this.sum(degrees);
        if (angleSum != 360) {// 上面的计算可能导致和小于360
            int c = 360 - angleSum;
            degrees[values.length - 1] += c; // 缺失数分给最后一个值
        }
        return degrees;
    }

    /**
     * 计算总和
     * 
     * @param values
     * @return
     */
    private int sum(int[] values) {
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

    /**
     * 重写这个方法来画出整个界面
     */
    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        float sx = (float) width / mask.getWidth();
        OVAL = new RectF(18 * sx, 48 * sx, 302 * sx, 332 * sx);
        textPaint.setTextSize(textSize * sx);
        if (animEnabled) { // 是否初始启动

            if (animState == ANIMATION_STATE_DOWN) {
                animStartTime = SystemClock.uptimeMillis();
                animState = ANIMATION_STATE_RUNNING;
            }
            final long currentTimeDiff = SystemClock.uptimeMillis() - animStartTime;
            int currentMaxDegree = (int) ((float) currentTimeDiff / ANIMATION_DURATION * 360f);// 当前画出的度数
            // Log.e(TAG, "当前最大的度数为:" + currentMaxDegree);
            if (currentMaxDegree >= 360) {// 动画结束状态,停止绘制
                currentMaxDegree = 360;
                animState = ANIMATION_STATE_DOWN;
                animEnabled = false;
            }

            int startAngle = this.startDegree;
            int maxIndex = getEventPart(currentMaxDegree);// 获取当前时刻最大可以旋转的角度所位于的区域
            for (int i = 0; i <= maxIndex; i++) { // 根据不同的颜色画饼图
                int currentDegree = degrees[i];
                if (i == maxIndex) {
                    currentDegree = getOffsetOfPartStart(currentMaxDegree, maxIndex);// 对于当前最后一个绘制区域，可能只是一部分，需要获取其偏移量
                }
                if (i > 0) {
                    startAngle += degrees[i - 1]; // 注意，每次画饼图，记得计算startAngle
                }
                paint.setColor(colors[i]);
                canvas.drawArc(OVAL, startAngle, currentDegree, true, paint);
            }

            if (animState == ANIMATION_STATE_DOWN) {
                onStop(); // 如果动画结束了，则调整当前箭头位于所在区域的中心方向
            } else {
                postInvalidate();
            }
        } else {
            int startAngle = this.startDegree;
            /**
             * 每个区域的颜色不同，但是这里只要控制好每个区域的角度就可以了，整个是个圆
             */
            for (int i = 0; i < values.length; i++) {
                paint.setColor(colors[i]);
                if (i > 0) {
                    startAngle += degrees[i - 1];
                }
                canvas.drawArc(OVAL, startAngle, degrees[i], true, paint);
            }
        }

        matrix.postScale(sx, sx); // 长和宽放大缩小的比例
        canvas.drawBitmap(mask, matrix, maskPaint);// 画出饼图后画蒙版图片，蒙版位于饼图之上

        /**
         * 根据当前计算得到的箭头所在区域显示该区域代表的信息
         */
        if (currentTargetIndex >= 0) {
            String title = titles[currentTargetIndex];
            textPaint.setColor(colors[currentTargetIndex]);
            canvas.drawText(title, OVAL.centerX() - title.length() * textSize * sx / 2, 388 * sx, textPaint);// 简单作个计算,让文字居中显示
        }

    }

    /**
     * 判断角度为degree坐落在饼图的哪个部分 注意，这里的角度一定是正值，而且不是相对于x轴正方向，而是相对于startAngle
     * 返回当前部分的索引
     * 
     * @param degree
     * @return
     */
    private int getEventPart(int degree) {
        int currentSum = 0;
        for (int i = 0; i < degrees.length; i++) {
            currentSum += degrees[i];
            if (currentSum >= degree) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在已经得知了当前degree位于targetIndex区域的情况下，计算angle相对于区域targetIndex起始位置的偏移量
     * 
     * @param degree
     * @param targetIndex
     * @return
     */
    private int getOffsetOfPartStart(int degree, int targetIndex) {
        int currentSum = 0;
        for (int i = 0; i < targetIndex; i++) {
            currentSum += degrees[i];
        }
        int offset = degree - currentSum;
        return offset;
    }

    /**
     * 当停止旋转时，如当前下方箭头位于某个区域的非中心位置，则计算偏移量，并将箭头指向中心位置
     */
    private void onStop() {
        int targetAngle = getTargetDegree();
        currentTargetIndex = getEventPart(targetAngle);
        int offset = getOffsetOfPartCenter(targetAngle, currentTargetIndex);
        /**
         * offset>0,说明当前箭头位于中心位置右边，则所有区域沿着顺时针旋转offset大小的角度 offset<0,正好相反
         */
        startDegree += offset;
        postInvalidateDelayed(200);
    }

    /**
     * 获取当前下方箭头位置相对于startDegree的角度值 注意，下方箭头相对于x轴正方向是90度
     * 
     * @return
     */
    private int getTargetDegree() {
        int targetDegree = -1;
        int tmpStart = startDegree;

        /**
         * 如果当前startAngle为负数，则直接+360，转换为正值
         */
        if (tmpStart < 0) {
            tmpStart += 360;
        }
        if (tmpStart < 90) {
            /**
             * 如果startAngle小于90度（可能为负数）
             */
            targetDegree = 90 - tmpStart;
        } else {
            /**
             * 如果startAngle大于90，由于在每次计算startAngle的时候，限定了其最大为360度，所以 直接可以按照如下公式计算
             */
            targetDegree = 360 + 90 - tmpStart;
        }
        // Log.e(TAG, "Taget Angle:"+targetDegree+"startAngle:"+startAngle);
        return targetDegree;
    }

    /**
     * 在已经得知了当前degree位于targetIndex区域的情况下，计算angle相对于区域targetIndex中心位置的偏移量
     * 这个是当我们停止旋转的时候，通过计算偏移量，来使得箭头指向当前区域的中心位置
     * 
     * @param degree
     * @param targetIndex
     * @return
     */
    private int getOffsetOfPartCenter(int degree, int targetIndex) {
        int currentSum = 0;
        for (int i = 0; i <= targetIndex; i++) {
            currentSum += degrees[i];
        }
        int offset = degree - (currentSum - degrees[targetIndex] / 2);
        // 超过一半,则offset>0；未超过一半,则offset<0
        return offset;
    }

    /**
     * 处理饼图的转动
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animEnabled && animState == ANIMATION_STATE_RUNNING) {
            return super.onTouchEvent(event);
        }
        Point eventPoint = getEventAbsoluteLocation(event);
        computeCenter(); // 计算中心坐标

        // 计算当前位置相对于x轴正方向的角度
        // 在下面这个方法中计算了eventRadius的
        int newAngle = getEventAngle(eventPoint, center);

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastEventPoint = eventPoint;
                if (eventRadius > getRadius()) {
                    /**
                     * 只有点在饼图内部才需要处理转动,否则直接返回
                     */
                    // Log.e(TAG, "当前位置超出了半径：" + eventRadius + ">" +
                    // getRadius());
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 这里处理滑动
                rotate(eventPoint, newAngle);
                // 处理之后，记得更新lastEventPoint
                lastEventPoint = eventPoint;
                break;
            case MotionEvent.ACTION_UP:
                onStop();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获取当前事件event相对于屏幕的坐标
     * 
     * @param event
     * @return
     */
    private Point getEventAbsoluteLocation(MotionEvent event) {
        int[] location = new int[2];
        this.getLocationOnScreen(location); // 当前控件在屏幕上的位置
        int x = (int) event.getX();
        int y = (int) event.getY();
        x += location[0];
        y += location[1]; // 这样x,y就代表当前事件相对于整个屏幕的坐标
        Point p = new Point(x, y);
        // Log.v(TAG, "事件坐标：" + p.toString());
        return p;
    }

    /**
     * 获取当前饼图的中心坐标，相对于屏幕左上角
     */
    private void computeCenter() {
        if (center == null) {
            center = new Point();
        }
        // view与屏幕左边距离+饼图占据的矩形的X轴方向的中点距view左边缘的距离
        center.x = (int) (getLeft() + OVAL.centerX());
        // 通知栏与标题栏的总高度+view上边距标题栏的距离+饼图占据的矩形的Y轴方向的中点距view上边的距离
        center.y = (int) (((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop() + getTop() + OVAL.centerY());
        // Log.v(TAG, "中心坐标：" + center.toString());
    }

    /**
     * 获取事件坐标相对于饼图的中心x轴正方向的角度 这里就是坐标系的转换，使用饼图的中心作为坐标中心，是正常坐标系。
     * 但是涉及到圆的转动，相对于x正方向顺时针来计算某个事件在坐标系中的位置
     * 
     * @param eventPoint
     * @param center
     * @return
     */
    private int getEventAngle(Point eventPoint, Point center) {
        int x = eventPoint.x - center.x;// x轴方向的偏移量
        int y = eventPoint.y - center.y; // y轴方向的偏移量
        // Log.v(TAG, "直角三角形两直边长度："+x+","+y);

        double z = Math.hypot(Math.abs(x), Math.abs(y)); // 求直角三角形斜边的长度
        // Log.v(TAG, "斜边长度："+z);

        eventRadius = (int) z;
        double sinA = Math.abs(y) / z;
        // Log.v(TAG, "sinA="+sinA);

        double asin = Math.asin(sinA); // 求反正玄，得到当前点和x轴的角度,是最小的那个
        // Log.v(TAG, "当前相对偏移角度的反正弦："+asin);

        int degree = (int) (asin / 3.14f * 180f);
        // Log.v(TAG, "当前相对偏移角度："+angle);

        // 下面就需要根据x,y的正负，来判断当前点和x轴的正方向的夹角
        int realDegree = 0;
        if (x <= 0 && y <= 0) {
            realDegree = 180 + degree; // 左上方，返回180+angle
        } else if (x >= 0 && y <= 0) {
            realDegree = 360 - degree;// 右上方，返回360-angle
        } else if (x <= 0 && y >= 0) {
            realDegree = 180 - degree; // 左下方，返回180-angle
        } else {
            realDegree = degree; // 右下方,直接返回
        }
        // Log.v(TAG, "当前事件相对于中心坐标x轴正方形的顺时针偏移角度为："+realAngle);
        return realDegree;
    }

    /**
     * 获取半径
     */
    private int getRadius() {
        int radius = (int) ((OVAL.right - OVAL.left) / 2f);
        // Log.v(TAG, "半径："+radius);
        return radius;
    }

    private void rotate(Point eventPoint, int newDegree) {
        // 计算上一个位置相对于x轴正方向的角度
        int lastDegree = getEventAngle(lastEventPoint, center);
        /**
         * 其实转动就是不断的更新画圆弧时候的起始角度，这样，每次从新的起始角度重画圆弧就形成了转动的效果
         */
        startDegree += newDegree - lastDegree;
        // 转多圈的时候，限定startAngle始终在-360-360度之间
        if (startDegree >= 360) {
            startDegree -= 360;
        } else if (startDegree <= -360) {
            startDegree += 360;
        }
        // Log.e(TAG, "当前startAngle：" + startDegree);
        // 获取当前下方箭头所在的区域，这样在onDraw的时候就会转到不同区域显示的是当前区域对应的信息
        int targetDegree = getTargetDegree();
        currentTargetIndex = getEventPart(targetDegree);
        // 请求重新绘制界面，调用onDraw方法
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            width = mask.getWidth();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = mask.getHeight();
        }
        setMeasuredDimension(width, height);
    }

    private class Point {

        public int x;
        public int y;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return new StringBuilder("[").append(x).append(",").append(y).append("]").toString();
        }

    }
}
