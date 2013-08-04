package com.jhonny.mpopular;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;


public class AyudaActivity extends Activity {
	
	private ViewFlipper vf;
	private final GestureDetector detector = new GestureDetector(new MyGestureDetector());
	private Context context;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ayuda);
		
		try{
			context = this;
			vf = (ViewFlipper)findViewById(R.id.vfShow);
			vf.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					try{
						detector.onTouchEvent(event);
					}catch(Exception ex){
						ex.printStackTrace();
						return false;
					}
					return true;
				}
			});
			vf.addView(addImageView(R.drawable.ayuda_img1));
			vf.addView(addImageView(R.drawable.ayuda_img2));
			vf.addView(addImageView(R.drawable.ayuda_img3));
			vf.addView(addImageView(R.drawable.ayuda_img4));
			vf.addView(addImageView(R.drawable.ayuda_img5));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ayuda, menu);
		return true;
	}
	
	
	private View addImageView(int resId){
		ImageView iv = new ImageView(this);
		iv.setImageResource(resId);
		return iv;
	}
	
	
	
	
	
	private class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
			try{
				// right to left swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
					vf.setInAnimation(AnimationUtils.loadAnimation(context, R.drawable.left_in));
					vf.setOutAnimation(AnimationUtils.loadAnimation(context, R.drawable.left_out));
					vf.showNext();
					return true;
					
				}else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) >SWIPE_THRESHOLD_VELOCITY){
					vf.setInAnimation(AnimationUtils.loadAnimation(context, R.drawable.right_in));
					vf.setOutAnimation(AnimationUtils.loadAnimation(context, R.drawable.right_out));
					vf.showPrevious();
					return true;
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return false;
		}
	}
}
