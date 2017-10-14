package com.com.tool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/7/10.
 */
public class CustomsTextView extends android.support.v7.widget.AppCompatTextView {
    private int mViewWidth = 0;
    private TextPaint mPaint;
    private LinearGradient mLinearGradient; //线性渲染
    private Matrix mGradientMatrix; //矩阵
    private int mTranslate = 0;

    public CustomsTextView(Context context) {
        this(context,null,0);
    }
    public CustomsTextView(Context context, AttributeSet attrs) {
        this(context, attrs ,0 );
    }
    public CustomsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientMatrix != null){  //计算平移的距离
            mTranslate += mViewWidth /7 ;
            if (mTranslate > 2*mViewWidth){ //如果平移到头了
                mTranslate = -mViewWidth;  //取相反的方向
            }
            mGradientMatrix.setTranslate(mTranslate ,0); //矩阵平移
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(50);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mViewWidth == 0){
            mViewWidth = getMeasuredWidth(); //获取到文字的宽度
            if (mViewWidth > 0 ){
                mPaint = getPaint();
                //Shader.TileMode.MIRROR   镜子，反射，反映
                //Shader.TileMode.REPEAT  重复    //设置布局的类型
                mLinearGradient = new LinearGradient(0,0,mViewWidth,0,new int[]{Color.RED,Color.GREEN,Color.CYAN,0xffffffff,Color.BLUE},null, Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);  //设置着色和外观
                mGradientMatrix = new Matrix();

            }
        }
    }
}