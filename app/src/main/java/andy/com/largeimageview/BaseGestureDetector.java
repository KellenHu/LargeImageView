package andy.com.largeimageview;

import android.content.Context;
import android.view.MotionEvent;

/**
 * 项目名:   LargeImageView
 * 包名：    andy.com.largeimageview
 * 文件名：  BaseGestureDetector
 * 创建者：  Andy
 * 创建时间：2016/10/10 15:48
 * 签名：    用风雅的态度看世界，用痞子的风格过日子
 * E-mail：  717616019@qq.com
 * GitHub：  https://github.com/KellenHu
 * CSDN：    http://my.csdn.net/westdeco
 */
public abstract class BaseGestureDetector {

    protected boolean mGestrueInProgress;

    protected MotionEvent mPreMotionEvent;

    protected MotionEvent mCurrentMotionEvent;

    protected Context mContext;


    public BaseGestureDetector(Context mContext) {
        this.mContext = mContext;
    }

    public boolean onTouchEvent(MotionEvent event){

        if (!mGestrueInProgress){
            handleStartProgressEvent(event);
        }else {
            handleInProgressEvent(event);
        }

        return true;
    }

    protected abstract void handleStartProgressEvent(MotionEvent event);

    protected abstract void handleInProgressEvent(MotionEvent event);

    protected abstract void updateStateByEvent(MotionEvent event);

    protected void resetState(){

        if (mPreMotionEvent != null){
            mPreMotionEvent.recycle();
            mPreMotionEvent = null;
        }

        if (mCurrentMotionEvent != null){
            mCurrentMotionEvent.recycle();
            mCurrentMotionEvent = null;
        }

        mGestrueInProgress = false;
    }
}