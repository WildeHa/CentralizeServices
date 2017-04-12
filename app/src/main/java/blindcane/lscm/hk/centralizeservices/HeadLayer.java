package blindcane.lscm.hk.centralizeservices;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by LSCM on 2017/4/5.
 */

public class HeadLayer extends View {

    private Context context;
    private FrameLayout frameLayout;
    private WindowManager windowManager;

    public HeadLayer(Context context) {
        super(context);

        this.context = context;
        frameLayout = new FrameLayout(context);

        addWindowManager();

    }

    private void addWindowManager() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(

                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP;

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(frameLayout, params);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.head_layout, frameLayout);

        final ImageView imageView = (ImageView) frameLayout.findViewById(R.id.imageView);
        imageView.setOnTouchListener(new OnTouchListener() {

            private int initX, initY;
            private int initTouchX, initTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        initX = params.x;
                        initY = params.y;
                        initTouchX = x;
                        initTouchY = y;
                        return true;
                    case MotionEvent.ACTION_UP:

                        if ((Math.abs(initTouchX - event.getRawX()) < 10) && (Math.abs(initTouchY - event.getRawY()) < 10)) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            Log.i("HeadLayer", "wake up MainActivity");
                        }

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initX + (x - initTouchX);
                        params.y = initY + (y - initTouchY);

                        windowManager.updateViewLayout(frameLayout, params);
                        return true;

                }

                return false;
            }
        });

    }

    public void destroy() {
        windowManager.removeView(frameLayout);
    }
}
