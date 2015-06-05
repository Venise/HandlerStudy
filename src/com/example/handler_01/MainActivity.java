package com.example.handler_01;

import android.R.integer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView textView;
	private Button button;
	private ImageView imageView;
	private int index;//Í¼Æ¬µÄË÷ÒýÎ»ÖÃ
	private int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img3,
			R.drawable.img4, R.drawable.img5, R.drawable.img6};
	private Handler handler = new Handler();
	private MyRunnable myRunnable = new MyRunnable();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView = (TextView) findViewById(R.id.textView);
//        new Thread(){
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(1000);
//					handler.post(new Runnable() {
//						
//						@Override
//						public void run() {
//							textView.setText("update thread");
//						}
//					});
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//        	
//        }.start();
        
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.button);
        handler.postDelayed(myRunnable, 1000);
        
    }
    
    class MyRunnable implements Runnable {
		public void run() {
			index++;
			index = index%6;
			imageView.setImageResource(images[index]);
			handler.postDelayed(myRunnable, 1000);
		}
	}
}
