package com.arkhm.freshlist;

import com.arkhm.freshlist.models.ItemList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class ServicesActivity extends Activity {

	static CheckBox checkgroc, checkhouse, checkchild, checkbills;
	Button sendBut;
	ItemList itemlist = new ItemList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chose_services_layout);
		setTitle("Freshlist Services");
		initialise();
	}

	private void initialise() {

		checkgroc = (CheckBox) findViewById(R.id.checkBox1);
		checkhouse = (CheckBox) findViewById(R.id.checkBox2);
		checkchild = (CheckBox) findViewById(R.id.checkBox3);
		checkbills = (CheckBox) findViewById(R.id.checkBox4);
		sendBut = (Button) findViewById(R.id.sendfromservice);
		checkgroc.setOnClickListener(new listeners());
		checkhouse.setOnClickListener(new listeners());
		checkchild.setOnClickListener(new listeners());
		checkbills.setOnClickListener(new listeners());
		sendBut.setOnClickListener(new listeners());
	}

	class listeners implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.checkBox1:
				try {
					Class<?> togo = Class
							.forName("com.arkhm.freshlist.GroceryAndShopingActivity");
					Intent intent = new Intent(ServicesActivity.this, togo);
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case R.id.checkBox2:
				try {
					Class<?> togo = Class
							.forName("com.arkhm.freshlist.HouseHoldActivity");
					Intent intent = new Intent(ServicesActivity.this, togo);
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case R.id.checkBox3:
				break;
			case R.id.checkBox4:
				break;
			case R.id.sendfromservice:
				try {
					Class<?> togo = Class
							.forName("com.arkhm.freshlist.ContactInfoActivity");
					Intent intent = new Intent(ServicesActivity.this, togo);
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}

}
