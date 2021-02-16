package com.motomistry.motomistry.View;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.motomistry.motomistry.R;


public class CurveImageView extends ImageView {
    Context mContext;
    Path mClipPath;
    int width = 0;
    int height = 0;
    Paint tintPaint;
    Paint shaderPaint;
    int gravity = Gravity.TOP;
    int curvatureHeight = 50;
    int tintAmount = 0;
    int tintMode = TintMode.MANUAL;
    int tintColor = 0;
    int gradientDirection = 0;
    int gradientStartColor = Color.TRANSPARENT;
    int gradientEndColor = Color.TRANSPARENT;
    int curvatureDirection = CurvatureDirection.OUTWARD;
    static public class Gravity {
        static final int TOP = 0;
        static final int BOTTOM = 1;
    }
    static public class TintMode {
        static final int AUTOMATIC = 0;
        static final int MANUAL = 1;
    }
    static public class Gradient {
        static final int TOP_TO_BOTTOM = 0;
        static final int BOTTOM_TO_TOP = 1;
        static final int LEFT_TO_RIGHT = 2;
        static final int RIGHT_TO_LEFT = 3;
    }

    static public class CurvatureDirection {
        static final int OUTWARD = 0;
        static final int INWARD = 1;
    }

    Paint mPaint;
    private PorterDuffXfermode porterDuffXfermode;
    private String TAG = "CURV_IMAGE_VIEW";

    public CurveImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CurveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);

        shaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mClipPath = new Path();

        TypedArray styledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.CrescentoImageView, 0, 0);
        if (styledAttributes.hasValue(R.styleable.CrescentoImageView_curvature)) {
            curvatureHeight = (int) styledAttributes.getDimension(R.styleable.CrescentoImageView_curvature, getDpForPixel(curvatureHeight));
        }

        if (styledAttributes.hasValue(R.styleable.CrescentoImageView_crescentoTintAlpha)) {
            if (styledAttributes.getInt(R.styleable.CrescentoImageView_crescentoTintAlpha, 0) <= 255
                    && styledAttributes.getInt(R.styleable.CrescentoImageView_crescentoTintAlpha, 0) >= 0) {
                tintAmount = styledAttributes.getInt(R.styleable.CrescentoImageView_crescentoTintAlpha, 0);
            }
        }

        if (styledAttributes.hasValue(R.styleable.CrescentoImageView_gravity)) {
            if (styledAttributes.getInt(R.styleable.CrescentoImageView_gravity, 0) == Gravity.BOTTOM) {
                gravity = Gravity.BOTTOM;
            } else {
                gravity = Gravity.TOP;
            }
        }

        if (styledAttributes.hasValue(R.styleable.CrescentoImageView_crescentoTintMode)) {
            if (styledAttributes.getInt(R.styleable.CrescentoImageView_crescentoTintMode, 0) == TintMode.AUTOMATIC) {
                tintMode = TintMode.AUTOMATIC;
            } else {
                tintMode = TintMode.MANUAL;
            }
        }

        if (styledAttributes.hasValue(R.styleable.CrescentoImageView_gradientDirection)) {
            gradientDirection = styledAttributes.getInt(R.styleable.CrescentoImageView_gradientDirection, 0);
        }
        gradientStartColor = styledAttributes.getColor(R.styleable.CrescentoImageView_gradientStartColor, Color.TRANSPARENT);
        gradientEndColor = styledAttributes.getColor(R.styleable.CrescentoImageView_gradientEndColor, Color.TRANSPARENT);

        if (styledAttributes.hasValue(R.styleable.CrescentoImageView_crescentoTintColor)) {
            tintColor = styledAttributes.getColor(R.styleable.CrescentoImageView_crescentoTintColor, 0);
        }
        curvatureDirection = styledAttributes.getInt(R.styleable.CrescentoImageView_curvatureDirection, 0);

        styledAttributes.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        mClipPath = PathProvider.getClipPath(width, height, curvatureHeight, curvatureDirection, gravity);

        ViewCompat.setElevation(this, ViewCompat.getElevation(this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                setOutlineProvider(getOutlineProvider());
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ViewOutlineProvider getOutlineProvider() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                try {
                    outline.setConvexPath(PathProvider.getOutlinePath(width, height, curvatureHeight, curvatureDirection, gravity));
                } catch (Exception e) {
                    Log.d("Outline Path", e.getMessage());
                }
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
        mPaint.setXfermode(porterDuffXfermode);
        if (tintPaint != null) {
            canvas.drawColor(tintPaint.getColor());
        }

        Shader mShader = GradientProvider.getShader(gradientStartColor, gradientEndColor, gradientDirection, canvas.getWidth(), canvas.getHeight());
        shaderPaint.setShader(mShader);
        canvas.drawPaint(shaderPaint);

        canvas.drawPath(mClipPath, mPaint);
        canvas.restoreToCount(saveCount);
        mPaint.setXfermode(null);
    }

    private int getDpForPixel (int pixel) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, mContext.getResources().getDisplayMetrics());
    }

    public void setCurvature(int height) {
        curvatureHeight = getDpForPixel(height);
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    public void setTintMode(int tintMode) {
        this.tintMode = tintMode;
    }

    public void setTintAmount(int tintAmount) {
        this.tintAmount = tintAmount;
    }

    public void setGradientDirection(int direction) {
        this.gradientDirection = direction;
    }

    public void setGradientStartColor(int startColor) {
        this.gradientStartColor = startColor;
    }

    public void setGradientEndColor(int endColor) {
        this.gradientEndColor = endColor;
    }

    public void setCurvatureDirection(int direction) {
        this.curvatureDirection = direction;
    }
}
