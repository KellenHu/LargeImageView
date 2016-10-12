package andy.com.largeimageview;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * 项目名:   LargeImageView
 * 包名：    andy.com.largeimageview
 * 文件名：  LargeImageView
 * 创建者：  Andy
 * 创建时间：2016/10/9 11:55
 * 签名：    用风雅的态度看世界，用痞子的风格过日子
 * E-mail：  717616019@qq.com
 * GitHub：  https://github.com/KellenHu
 * CSDN：    http://my.csdn.net/westdeco
 */
public class LargeImageView extends View{

    public String TAG = getClass().getName();

    /**
     * 主要工具类对象，用于分割图片
     */
    BitmapRegionDecoder regionDecoder;

    /**
     * 图片的实际宽高
     */
    private int mImageWidth;
    private int mImageHeight;


    private MoveGestureDetector gestureDetector;


    /**
     * 绘制的区域
     */
    private volatile Rect mRect = new Rect();

    private static BitmapFactory.Options mOptions = new BitmapFactory.Options();

    static {
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public LargeImageView(Context context){
        this(context,null,0);
        init();
    }

    /**
     * 了解对方的圈子风格
     * @param context
     * @param attrs
     */
    public LargeImageView(Context context, AttributeSet attrs){
        this(context,attrs,0);
        init();
    }

    public LargeImageView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }



    private void init(){
        gestureDetector = new MoveGestureDetector(getContext(),new MoveGestureDetector.SimpleOnMoveGrstureListener(){
            @Override
            public boolean onMove(MoveGestureDetector gestureDetector){

                int moveX = (int) gestureDetector.getMoveX();
                int moveY = (int) gestureDetector.getMoveY();

                if (mImageWidth > getWidth()){
                    mRect.offset(-moveX,0);
                    checkWidth();
                    invalidate();
                }

                if (mImageHeight > getHeight())
                {
                    mRect.offset(0, -moveY);
                    checkHeight();
                    invalidate();
                }
                return true;
            }
        });
    }

    private void checkWidth(){

        Rect rect = mRect;

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.right > imageWidth){
            rect.left = imageWidth - getWidth();
            rect.right = imageWidth;
        }

        if (rect.left < 0){
            rect.left = 0;
            rect.right = getWidth();
        }
    }


    private void checkHeight(){

        Rect rect = mRect;

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.bottom > imageHeight){
            rect.top = imageHeight - getHeight();
            rect.bottom = imageHeight;
        }

        if (rect.top < 0){
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }

    /**
     * 设置流
     */
    public void setInputStream(InputStream inputStream){

        try {
            regionDecoder = BitmapRegionDecoder.newInstance(inputStream,false);

            //测量宽高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(inputStream,null,options);

            mImageWidth = options.outWidth;
            mImageHeight = options.outHeight;

            invalidate();

        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,"setInputStream Exception");
        }finally {
            //释放输入流
                try {
                    if (inputStream != null)
                    inputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas){
        Log.v("LargeImageView","LargeImageView onDraw----------------------------------------------->");
        try {
            Bitmap bitmap = regionDecoder.decodeRegion(mRect,mOptions);
            canvas.drawBitmap(bitmap,0,0,null);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,"onDraw  hasException ----------------------------------------------->");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        double main = 1.3;

        //默认直接显示图片的中心区域，可以自己去调节
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;
    }
}