package com.example.handler_01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView textView;
	private Button button;
	private ImageView imageView;
	private int index;//图片的索引位置
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
        handler.postDelayed(myRunnable, 2000);//5秒之后开始轮徇图片，更新UI
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, HandlerMessageActivity.class);
				startActivity(intent);
			}
		});
    }
    
    class MyRunnable implements Runnable {
		public void run() {
			index++;
			index = index%6;
			imageView.setImageResource(images[index]);
			handler.postDelayed(myRunnable, 1000);//图片每隔1秒轮徇，延时操作（递归调用线程？）
		}
	}
}
