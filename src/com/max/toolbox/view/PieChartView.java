/**
 * @��Ŀ Android_Demo_PieChart
 * @���� com.demo.piechart.view
 * @�ļ� PieChart.java
 * @����
 * @���� 2013-4-22
 * @�汾 1.0
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
 * ʵ�ֵĹ�������Ҫ�õ�һЩ��ѧ������ʵ�ֽǶȺ���Ļλ������ļ��㡣���������������������������ĽǶȵ�ʱ�����ȼ���ÿ���������x��������ĽǶȣ�
 * ���Խ���ת��Ϊ����ֱ�������ε��ڽǼ������⣬�ٽ����μ���ĽǶȽ��м������㣬�͵õ��������������������������ĽǶȡ�
 */
public class PieChartView extends View {

    public static final String TAG = "PieChart";

    /**
     * ��ͼ͸����
     */
    public static final int ALPHA = 150;
    /**
     * ��ʼ��������ʱ�䣬��ֵԽ��Խ��
     */
    public static final int ANIMATION_DURATION = 800;
    /**
     * ����״̬-������
     */
    public static final int ANIMATION_STATE_RUNNING = 1;
    /**
     * ����״̬-�������
     */
    public static final int ANIMATION_STATE_DOWN = 2;

    /**
     * ��ͼռ�ݵľ���������Բ�α�ͼӦռ�������Ρ�left��top �������Ͻǵ�����ꣻright��bottom�������½ǵ�����ꡣ�����α߳� =
     * right - left = bottom - top
     */
    private static RectF OVAL;

    /**
     * ÿ���ֵ���ɫ��������ɫ��
     */
    private int[] colors;
    /**
     * ÿ���ֵĴ�С�����������
     */
    private int[] values;
    /**
     * ����ÿ���ִ�Сֵ������ڱ���ÿ��ĽǶ�
     */
    private int[] degrees;
    /**
     * ÿ���ֵ����ݣ������Ӧ���֣�
     */
    private String[] titles;

    /**
     * ����ɫ
     */
    private Paint paint;
    /**
     * ���ɰ�
     */
    private Paint maskPaint;
    /**
     * ������
     */
    private Paint textPaint;

    /**
     * ��һ���¼��ĵ�
     */
    private Point lastEventPoint;
    /**
     * ��ͼ����λ��
     */
    private Point center;
    /**
     * �¼������ͼ���ĵľ���
     */
    private int eventRadius = 0;

    /**
     * ��ǰĿ������λ��
     */
    private int currentTargetIndex = -1;

    /**
     * �ɰ�λͼ����
     */
    private Bitmap mask;

    /**
     * �ó�ʼʱԲ���Ӽ�ͷλ�ÿ�ʼ����
     */
    private int startDegree = 90;
    /**
     * ����״̬
     */
    private int animState = ANIMATION_STATE_DOWN;
    /**
     * ������������
     */
    private boolean animEnabled = false;
    /**
     * ��������ʱ��
     */
    private long animStartTime;

    /**
     * ������
     */
    private Context context;

    /**
     * view���
     */
    private int width = 0;
    /**
     * view�߶�
     */
    private int height = 0;

    /**
     * �����С
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
                "������������������������",
                "����ú����ú����ú����ú",
                "������������������������",
                "ʷ����ʷ����ʷ����ʷ����",
                "�򾩱��򾩱��򾩱��򾩱�"
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
        animEnabled = true; // ��������
    }

    /**
     * ����ÿ������ռ�ı�����������ÿ������������Բ����ռ�ĽǶ�
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
        if (angleSum != 360) {// ����ļ�����ܵ��º�С��360
            int c = 360 - angleSum;
            degrees[values.length - 1] += c; // ȱʧ���ָ����һ��ֵ
        }
        return degrees;
    }

    /**
     * �����ܺ�
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
     * ��д���������������������
     */
    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        float sx = (float) width / mask.getWidth();
        OVAL = new RectF(18 * sx, 48 * sx, 302 * sx, 332 * sx);
        textPaint.setTextSize(textSize * sx);
        if (animEnabled) { // �Ƿ��ʼ����

            if (animState == ANIMATION_STATE_DOWN) {
                animStartTime = SystemClock.uptimeMillis();
                animState = ANIMATION_STATE_RUNNING;
            }
            final long currentTimeDiff = SystemClock.uptimeMillis() - animStartTime;
            int currentMaxDegree = (int) ((float) currentTimeDiff / ANIMATION_DURATION * 360f);// ��ǰ�����Ķ���
            // Log.e(TAG, "��ǰ���Ķ���Ϊ:" + currentMaxDegree);
            if (currentMaxDegree >= 360) {// ��������״̬,ֹͣ����
                currentMaxDegree = 360;
                animState = ANIMATION_STATE_DOWN;
                animEnabled = false;
            }

            int startAngle = this.startDegree;
            int maxIndex = getEventPart(currentMaxDegree);// ��ȡ��ǰʱ����������ת�ĽǶ���λ�ڵ�����
            for (int i = 0; i <= maxIndex; i++) { // ���ݲ�ͬ����ɫ����ͼ
                int currentDegree = degrees[i];
                if (i == maxIndex) {
                    currentDegree = getOffsetOfPartStart(currentMaxDegree, maxIndex);// ���ڵ�ǰ���һ���������򣬿���ֻ��һ���֣���Ҫ��ȡ��ƫ����
                }
                if (i > 0) {
                    startAngle += degrees[i - 1]; // ע�⣬ÿ�λ���ͼ���ǵü���startAngle
                }
                paint.setColor(colors[i]);
                canvas.drawArc(OVAL, startAngle, currentDegree, true, paint);
            }

            if (animState == ANIMATION_STATE_DOWN) {
                onStop(); // ������������ˣ��������ǰ��ͷλ��������������ķ���
            } else {
                postInvalidate();
            }
        } else {
            int startAngle = this.startDegree;
            /**
             * ÿ���������ɫ��ͬ����������ֻҪ���ƺ�ÿ������ĽǶȾͿ����ˣ������Ǹ�Բ
             */
            for (int i = 0; i < values.length; i++) {
                paint.setColor(colors[i]);
                if (i > 0) {
                    startAngle += degrees[i - 1];
                }
                canvas.drawArc(OVAL, startAngle, degrees[i], true, paint);
            }
        }

        matrix.postScale(sx, sx); // ���Ϳ�Ŵ���С�ı���
        canvas.drawBitmap(mask, matrix, maskPaint);// ������ͼ���ɰ�ͼƬ���ɰ�λ�ڱ�ͼ֮��

        /**
         * ���ݵ�ǰ����õ��ļ�ͷ����������ʾ������������Ϣ
         */
        if (currentTargetIndex >= 0) {
            String title = titles[currentTargetIndex];
            textPaint.setColor(colors[currentTargetIndex]);
            canvas.drawText(title, OVAL.centerX() - title.length() * textSize * sx / 2, 388 * sx, textPaint);// ����������,�����־�����ʾ
        }

    }

    /**
     * �жϽǶ�Ϊdegree�����ڱ�ͼ���ĸ����� ע�⣬����ĽǶ�һ������ֵ�����Ҳ��������x�������򣬶��������startAngle
     * ���ص�ǰ���ֵ�����
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
     * ���Ѿ���֪�˵�ǰdegreeλ��targetIndex���������£�����angle���������targetIndex��ʼλ�õ�ƫ����
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
     * ��ֹͣ��תʱ���統ǰ�·���ͷλ��ĳ������ķ�����λ�ã������ƫ������������ͷָ������λ��
     */
    private void onStop() {
        int targetAngle = getTargetDegree();
        currentTargetIndex = getEventPart(targetAngle);
        int offset = getOffsetOfPartCenter(targetAngle, currentTargetIndex);
        /**
         * offset>0,˵����ǰ��ͷλ������λ���ұߣ���������������˳ʱ����תoffset��С�ĽǶ� offset<0,�����෴
         */
        startDegree += offset;
        postInvalidateDelayed(200);
    }

    /**
     * ��ȡ��ǰ�·���ͷλ�������startDegree�ĽǶ�ֵ ע�⣬�·���ͷ�����x����������90��
     * 
     * @return
     */
    private int getTargetDegree() {
        int targetDegree = -1;
        int tmpStart = startDegree;

        /**
         * �����ǰstartAngleΪ��������ֱ��+360��ת��Ϊ��ֵ
         */
        if (tmpStart < 0) {
            tmpStart += 360;
        }
        if (tmpStart < 90) {
            /**
             * ���startAngleС��90�ȣ�����Ϊ������
             */
            targetDegree = 90 - tmpStart;
        } else {
            /**
             * ���startAngle����90��������ÿ�μ���startAngle��ʱ���޶��������Ϊ360�ȣ����� ֱ�ӿ��԰������¹�ʽ����
             */
            targetDegree = 360 + 90 - tmpStart;
        }
        // Log.e(TAG, "Taget Angle:"+targetDegree+"startAngle:"+startAngle);
        return targetDegree;
    }

    /**
     * ���Ѿ���֪�˵�ǰdegreeλ��targetIndex���������£�����angle���������targetIndex����λ�õ�ƫ����
     * ����ǵ�����ֹͣ��ת��ʱ��ͨ������ƫ��������ʹ�ü�ͷָ��ǰ���������λ��
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
        // ����һ��,��offset>0��δ����һ��,��offset<0
        return offset;
    }

    /**
     * �����ͼ��ת��
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animEnabled && animState == ANIMATION_STATE_RUNNING) {
            return super.onTouchEvent(event);
        }
        Point eventPoint = getEventAbsoluteLocation(event);
        computeCenter(); // ������������

        // ���㵱ǰλ�������x��������ĽǶ�
        // ��������������м�����eventRadius��
        int newAngle = getEventAngle(eventPoint, center);

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastEventPoint = eventPoint;
                if (eventRadius > getRadius()) {
                    /**
                     * ֻ�е��ڱ�ͼ�ڲ�����Ҫ����ת��,����ֱ�ӷ���
                     */
                    // Log.e(TAG, "��ǰλ�ó����˰뾶��" + eventRadius + ">" +
                    // getRadius());
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // ���ﴦ����
                rotate(eventPoint, newAngle);
                // ����֮�󣬼ǵø���lastEventPoint
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
     * ��ȡ��ǰ�¼�event�������Ļ������
     * 
     * @param event
     * @return
     */
    private Point getEventAbsoluteLocation(MotionEvent event) {
        int[] location = new int[2];
        this.getLocationOnScreen(location); // ��ǰ�ؼ�����Ļ�ϵ�λ��
        int x = (int) event.getX();
        int y = (int) event.getY();
        x += location[0];
        y += location[1]; // ����x,y�ʹ���ǰ�¼������������Ļ������
        Point p = new Point(x, y);
        // Log.v(TAG, "�¼����꣺" + p.toString());
        return p;
    }

    /**
     * ��ȡ��ǰ��ͼ���������꣬�������Ļ���Ͻ�
     */
    private void computeCenter() {
        if (center == null) {
            center = new Point();
        }
        // view����Ļ��߾���+��ͼռ�ݵľ��ε�X�᷽����е��view���Ե�ľ���
        center.x = (int) (getLeft() + OVAL.centerX());
        // ֪ͨ������������ܸ߶�+view�ϱ߾�������ľ���+��ͼռ�ݵľ��ε�Y�᷽����е��view�ϱߵľ���
        center.y = (int) (((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop() + getTop() + OVAL.centerY());
        // Log.v(TAG, "�������꣺" + center.toString());
    }

    /**
     * ��ȡ�¼���������ڱ�ͼ������x��������ĽǶ� �����������ϵ��ת����ʹ�ñ�ͼ��������Ϊ�������ģ�����������ϵ��
     * �����漰��Բ��ת���������x������˳ʱ��������ĳ���¼�������ϵ�е�λ��
     * 
     * @param eventPoint
     * @param center
     * @return
     */
    private int getEventAngle(Point eventPoint, Point center) {
        int x = eventPoint.x - center.x;// x�᷽���ƫ����
        int y = eventPoint.y - center.y; // y�᷽���ƫ����
        // Log.v(TAG, "ֱ����������ֱ�߳��ȣ�"+x+","+y);

        double z = Math.hypot(Math.abs(x), Math.abs(y)); // ��ֱ��������б�ߵĳ���
        // Log.v(TAG, "б�߳��ȣ�"+z);

        eventRadius = (int) z;
        double sinA = Math.abs(y) / z;
        // Log.v(TAG, "sinA="+sinA);

        double asin = Math.asin(sinA); // ���������õ���ǰ���x��ĽǶ�,����С���Ǹ�
        // Log.v(TAG, "��ǰ���ƫ�ƽǶȵķ����ң�"+asin);

        int degree = (int) (asin / 3.14f * 180f);
        // Log.v(TAG, "��ǰ���ƫ�ƽǶȣ�"+angle);

        // �������Ҫ����x,y�����������жϵ�ǰ���x���������ļн�
        int realDegree = 0;
        if (x <= 0 && y <= 0) {
            realDegree = 180 + degree; // ���Ϸ�������180+angle
        } else if (x >= 0 && y <= 0) {
            realDegree = 360 - degree;// ���Ϸ�������360-angle
        } else if (x <= 0 && y >= 0) {
            realDegree = 180 - degree; // ���·�������180-angle
        } else {
            realDegree = degree; // ���·�,ֱ�ӷ���
        }
        // Log.v(TAG, "��ǰ�¼��������������x�������ε�˳ʱ��ƫ�ƽǶ�Ϊ��"+realAngle);
        return realDegree;
    }

    /**
     * ��ȡ�뾶
     */
    private int getRadius() {
        int radius = (int) ((OVAL.right - OVAL.left) / 2f);
        // Log.v(TAG, "�뾶��"+radius);
        return radius;
    }

    private void rotate(Point eventPoint, int newDegree) {
        // ������һ��λ�������x��������ĽǶ�
        int lastDegree = getEventAngle(lastEventPoint, center);
        /**
         * ��ʵת�����ǲ��ϵĸ��»�Բ��ʱ�����ʼ�Ƕȣ�������ÿ�δ��µ���ʼ�Ƕ��ػ�Բ�����γ���ת����Ч��
         */
        startDegree += newDegree - lastDegree;
        // ת��Ȧ��ʱ���޶�startAngleʼ����-360-360��֮��
        if (startDegree >= 360) {
            startDegree -= 360;
        } else if (startDegree <= -360) {
            startDegree += 360;
        }
        // Log.e(TAG, "��ǰstartAngle��" + startDegree);
        // ��ȡ��ǰ�·���ͷ���ڵ�����������onDraw��ʱ��ͻ�ת����ͬ������ʾ���ǵ�ǰ�����Ӧ����Ϣ
        int targetDegree = getTargetDegree();
        currentTargetIndex = getEventPart(targetDegree);
        // �������»��ƽ��棬����onDraw����
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
