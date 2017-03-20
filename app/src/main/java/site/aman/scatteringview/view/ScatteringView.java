
package site.aman.scatteringview.view;

import site.aman.scatteringview.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * scattering View demo
 *
 * @author yangjg
 * @version 1.0.0
 * @since 2016-03-05
 */
public class ScatteringView extends View {
    private final int DEFALT_TEXT_SIZE = 20;
    private Paint mRipplePaint = new Paint();
    // view 的宽高
    private int mViewHeight;
    private int mViewWeight;
    // 位图的宽高
    private int mBitmapWidth;
    private int mBitmapHeight;
    // 开始的状态
    private boolean isStartRipple;
    // 半径大小
    private int rippleRadius;
    private int rippleFirstRadius;
    private int rippleSecendRadius;
    private int rippleThirdRadius;
    // 文字画笔
    private Paint textPaint = new Paint();
    private String mText;
    private float mTextSize = DEFALT_TEXT_SIZE;

    private RippleOnTouchListener mRippleOnTouchListener = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            invalidate();
            if (isStartRipple) {
                rippleFirstRadius++;
                if (rippleFirstRadius > 100) {
                    rippleFirstRadius = 0;
                }
                rippleSecendRadius++;
                if (rippleSecendRadius > 100) {
                    rippleSecendRadius = 0;
                }
                rippleThirdRadius++;
                if (rippleThirdRadius > 100) {
                    rippleThirdRadius = 0;
                }
                sendEmptyMessageDelayed(0, 20);
            }
        }
    };

    public ScatteringView(Context context) {
        super(context);
        init();
    }

    public ScatteringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int resourceId = -1;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScatteringView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ScatteringView_text:
                    resourceId = typedArray.getResourceId(R.styleable.ScatteringView_text, 0);
                    this.setText((String) (resourceId > 0 ? typedArray.getResources().getText(
                            resourceId) : typedArray.getString(R.styleable.ScatteringView_text)));
                    break;
                case R.styleable.ScatteringView_text_size:
                    resourceId = typedArray.getResourceId(R.styleable.ScatteringView_text_size, 0);
                    this.setTextSize(resourceId > 0 ? typedArray.getResources().getDimension(
                            resourceId) : typedArray.getInt(R.styleable.ScatteringView_text_size,
                            DEFALT_TEXT_SIZE));
                    break;
            }
        }
        typedArray.recycle();
        init();
    }

    public ScatteringView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.d("YJG", "init");
        // 波形的半径
        rippleFirstRadius = 0;
        rippleSecendRadius = -33;
        rippleThirdRadius = -66;
        // 设置水波纹画笔的颜色
        mRipplePaint.setColor(Color.rgb(255, 38, 110));
        mRipplePaint.setAntiAlias(true);
        mRipplePaint.setStyle(Paint.Style.FILL);
        // 文字大小
        textPaint.setTextSize(mTextSize);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        // 文字颜色
        textPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("YJG", "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mViewWeight = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        Log.d("YJG", "mViewWeight" + mViewWeight);
        Log.d("YJG", "mViewHeight" + mViewHeight);
        mBitmapWidth = getMeasuredWidth();
        mBitmapHeight = getMeasuredHeight();
        rippleRadius = Math.min(mBitmapHeight, mBitmapWidth) * 3 / 10;
        if (mBitmapWidth < 2 * mBitmapHeight) {
            mBitmapWidth = (2 * mBitmapHeight);
        }
        setMeasuredDimension(mBitmapWidth, mBitmapHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 透明度
        mRipplePaint.setAlpha(255);
        // 圆的大小：x，y坐标、半径、画布
        canvas.drawCircle(mBitmapWidth / 2, mBitmapHeight / 2, rippleRadius, mRipplePaint);
        if (isStartRipple) {
            // 半径占的较短边的比例
            float f1 = 2 * Math.min(mBitmapHeight, mBitmapWidth) / 1000.0F;
            // 第一个扩散波纹的绘制,当前画笔的透明度，和半径相关
            int i1 = (int) (220.0F - (220.0F - 0.0F) / 100.0F * rippleFirstRadius);
            mRipplePaint.setAlpha(i1);
            canvas.drawCircle(mBitmapWidth / 2, mBitmapHeight / 2, rippleRadius + f1
                    * rippleFirstRadius, mRipplePaint);

            if (rippleSecendRadius >= 0) {
                int secendAlpha = (int) (220.0F - (220.0F - 0.0F) / 100.0F * rippleSecendRadius);
                mRipplePaint.setAlpha(secendAlpha);
                canvas.drawCircle(mBitmapWidth / 2, mBitmapHeight / 2, rippleRadius + f1
                        * rippleSecendRadius, mRipplePaint);
            }

            if (rippleThirdRadius >= 0) {
                int thirdAlpha = (int) (220.0F - (220.0F - 0.0F) / 100.0F * rippleThirdRadius);
                mRipplePaint.setAlpha(thirdAlpha);
                canvas.drawCircle(mBitmapWidth / 2, mBitmapHeight / 2, rippleRadius + f1
                        * rippleThirdRadius, mRipplePaint);
            }
        } else {
            // 绘制文字
            if (mText != null) {
                float length = textPaint.measureText(mText);
                // why do this mesure ? for more info visit
                // http://blog.csdn.net/linghu_java/article/details/46404081
                float textYCoordinate = textPaint.getFontMetricsInt().bottom;
                canvas.drawText(mText, (mBitmapWidth - length) / 2, mBitmapHeight / 2
                        + textYCoordinate, textPaint);
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        confirmSize();
        invalidate();
    }

    private void confirmSize() {
        //do nothing
    }

    private void stratRipple() {
        isStartRipple = true;
        handler.sendEmptyMessage(0);
    }

    private void stopRipple() {
        isStartRipple = false;
        rippleFirstRadius = 0;
        rippleSecendRadius = -33;
        rippleThirdRadius = -66;
        invalidate();
        handler.removeMessages(0);
    }

    /**
     * <p>
     * Title: setRippleOnTouchListener
     * </p>
     * <p>
     * Description:
     * </p>
     * if you need listen the button status, you can set a listener use this
     * method
     * 
     * @param mOnTouchListener
     * @see site.aman.scatteringview.view.ScatteringView.RippleOnTouchListener
     */
    public void setRippleOnTouchListener(RippleOnTouchListener mOnTouchListener) {
        this.mRippleOnTouchListener = mOnTouchListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int mAction = event.getAction();
        float x = event.getX();
        float y = event.getY();

        if (mAction == MotionEvent.ACTION_DOWN) {
            // 点击的点落在圆内，尚未完全适配到圆
            if (x > (mBitmapWidth - rippleRadius) / 2 && x < (mBitmapWidth + rippleRadius) / 2) {
                Log.d("YJG", "在圆内" + x + ":" + y);
                stratRipple();
                if (mRippleOnTouchListener != null) {
                    mRippleOnTouchListener.onTouchEvent(this, event);
                    mRippleOnTouchListener.onStart();
                }
                return true;
            } else {
                Log.d("YJG", "在圆外" + x + ":" + y);
                return false;
            }
        } else if (mAction == MotionEvent.ACTION_UP) { // 放开时停止动画
            stopRipple();
            mRippleOnTouchListener.onStop();
            return true;
        }
        return false;
    }

    /**
     * set the view text in button center
     * 
     * @param mText the text to display
     */
    public void setText(String mText) {
        this.mText = mText;
    }

    /**
     * set the view text size
     * 
     * @param mTextSize the int value for text size
     */
    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        textPaint.setTextSize(mTextSize);
    }
    
    public interface RippleOnTouchListener {
        public boolean onTouchEvent(View v, MotionEvent event);

        public void onStart();

        public void onStop();
    }
}
