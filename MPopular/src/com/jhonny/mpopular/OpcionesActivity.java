package com.jhonny.mpopular;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;


public class OpcionesActivity extends Activity {
	
	private Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opciones);
		
		try{
			this.context = (Context)this;
			
			TextView opc_tv1 = (TextView)findViewById(R.id.opc_textView1);
			opc_tv1.setTypeface(Util.getRoboto7(context));
			
			TextView opc_tv2 = (TextView)findViewById(R.id.opc_textView2);
			opc_tv2.setTypeface(Util.getRoboto7(context));
			
			TextView opc_tv3 = (TextView)findViewById(R.id.opc_textView3);
			opc_tv3.setTypeface(Util.getRoboto7(context));
			
			TextView opc_tv4 = (TextView)findViewById(R.id.opc_textView4);
			opc_tv4.setTypeface(Util.getRoboto7(context));
			
			TextView opc_tv5 = (TextView)findViewById(R.id.opc_textView5);
			opc_tv5.setTypeface(Util.getRoboto7(context));
			
			TextView opc_tv6 = (TextView)findViewById(R.id.opc_textView6);
			opc_tv6.setTypeface(Util.getRoboto7(context));
			
			TextView opc_tv7 = (TextView)findViewById(R.id.opc_textView7);
			opc_tv7.setTypeface(Util.getRoboto7(context));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_opciones, menu);
		return true;
	}
}
