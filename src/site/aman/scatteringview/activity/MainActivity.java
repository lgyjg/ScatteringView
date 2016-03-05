package site.aman.scatteringview.activity;
/**
 * scattering View demo
 *
 * @author yangjg
 * @version 1.0.0
 * @since 2016-03-05
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import site.aman.scatteringview.R;
import site.aman.scatteringview.view.ScatteringView;
import site.aman.scatteringview.view.ScatteringView.RippleOnTouchListener;

public class MainActivity extends Activity {

    private ScatteringView mScatteringView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScatteringView = (ScatteringView) findViewById(R.id.ScatteringView);
        // 你也可以通过xml定义值
        // mScatteringView.setText("按住录音");
        // mScatteringView.setTextSize(20);
        mScatteringView.setRippleOnTouchListener(new RippleOnTouchListener() {

            @Override
            public boolean onTouchEvent(View v, MotionEvent event) {
                Log.d("YJG", "ontouch");
                return false;
            }

            @Override
            public void onStart() {
                Log.d("YJG", "onStart");
            }

            @Override
            public void onStop() {
                Log.d("YJG", "onStop");
            }
        });
    }

}
