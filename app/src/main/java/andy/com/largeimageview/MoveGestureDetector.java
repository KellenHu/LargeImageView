package andy.com.largeimageview;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 项目名:   LargeImageView
 * 包名：    andy.com.largeimageview
 * 文件名：  MoveGestureDetector
 * 创建者：  Andy
 * 创建时间：2016/10/10 15:48
 * 签名：    用风雅的态度看世界，用痞子的风格过日子
 * E-mail：  717616019@qq.com
 * GitHub：  https://github.com/KellenHu
 * CSDN：    http://my.csdn.net/westdeco
 */
public class MoveGestureDetector extends BaseGestureDetector{

    public String TAG = getClass().getName();

    private PointF mCurrentPointer;
    private PointF mPrePointer;

    //仅仅为了减少内存
    private PointF mDeltePointer = new PointF();

    //用于记录最终结果
    private PointF mExtenalPointer = new PointF();

    private OnMoveGestureListener mListener;


    public MoveGestureDetector(Context mContext,OnMoveGestureListener onMoveGestureListener) {
        super(mContext);
        this.mListener = onMoveGestureListener;
    }

    @Override
    protected void handleStartProgressEvent(MotionEvent event) {
        int actionCode = event.getAction() & MotionEvent.ACTION_MASK;

        switch (actionCode){
            case MotionEvent.ACTION_DOWN:
                resetState();//放置没有接收到Cancle or Up,为了保险
                mPreMotionEvent = MotionEvent.obtain(event);
                updateStateByEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mGestrueInProgress = mListener.onMoveBegin(this);
                break;
        }


    }

    @Override
    protected void handleInProgressEvent(MotionEvent event) {
        int actionCode = event.getAction() & MotionEvent.ACTION_MASK;

        switch (actionCode){
            case MotionEvent.ACTION_CANCEL:
                resetState();
            case MotionEvent.ACTION_UP:
                mListener.onMoveEnd(this);
                resetState();
                break;
            case MotionEvent.ACTION_DOWN:
                updateStateByEvent(event);
                boolean update = mListener.onMove(this);

                if (update){
                    mPreMotionEvent.recycle();
                    mPreMotionEvent = MotionEvent.obtain(event);
                }
                break;
        }
    }

    @Override
    protected void updateStateByEvent(MotionEvent event) {
        final MotionEvent prev = mPreMotionEvent;

        mPrePointer = caculateFocalPointer(prev);
        mCurrentPointer = caculateFocalPointer(event);

        Log.v(TAG,"PrePointer---->" + mPrePointer.toString() + "\tCurrentPointer---->" + mCurrentPointer.toString());

        boolean mSkipThisMotionEvent = prev.getPointerCount() != event.getPointerCount();

        Log.v(TAG,"mSkipThisMotionEvent---->" + mSkipThisMotionEvent);

        mExtenalPointer.x = mSkipThisMotionEvent ? 0 : mCurrentPointer.x - mPrePointer.x;
        mExtenalPointer.y = mSkipThisMotionEvent ? 0 : mCurrentPointer.y - mPrePointer.y;
    }

    /**
     *  根据event计算多指操作中心点
     * @param event
     * @return
     */
    private PointF caculateFocalPointer(MotionEvent event) {
        //触点个数
        final int count = event.getPointerCount();
        float x = 0,y = 0;

        for (int i = 0; i < count;i ++){
            x += event.getX(i);
            y += event.getY(i);
        }
        //求平均值
        x /= count;
        y /= count;

        return new PointF(x,y);
    }


    public float getMoveX(){
        return mExtenalPointer.x;
    }

    public float getMoveY(){
        return mExtenalPointer.y;
    }

    public interface OnMoveGestureListener{

        public boolean onMoveBegin(MoveGestureDetector gestureDetector);

        public boolean onMove(MoveGestureDetector gestureDetector);

        public void onMoveEnd(MoveGestureDetector gestureDetector);
    }

    public static class SimpleOnMoveGrstureListener implements OnMoveGestureListener{

        @Override
        public boolean onMoveBegin(MoveGestureDetector gestureDetector) {
            return true;
        }

        @Override
        public boolean onMove(MoveGestureDetector gestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(MoveGestureDetector gestureDetector) {

        }
    }

}
