package com.posimplicity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.Beans.NavDrawerItemModel;
import com.adapter.NavDrawerListAdapter;
import com.Fragments.FunctionFragment;

import org.json.JSONArray;

import java.util.ArrayList;

public class FunctionDrawerActivity extends BaseActivity implements OnItemClickListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;	
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navMenuTitles = {"Add CustomerParent","Time Clock","TenderCard","Drawer","PayOuts","Add Table","Add Clerk","Dejavoo Tip Adjustment","Dejavoo Return Adjustment","TSYS Tip Adjustment","TSYS Return Adjustment","Exit"};
	private ArrayList<NavDrawerItemModel> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private boolean isDrawerOpen = true;
	private Fragment fragment    = null;
	private ActionBarDrawerToggle mDrawerToggle = null;
	public static final String PAGE_POSITION = "pagePosition";
	public static final int FUNCTION_ADD_CUSTOMER = 0;
	public static final int FUNCTION_TIME_CLOCK   = 1;	
	public static final int FUNCTION_TENDER_CARD  = 2;	
	public static final int FUNCTION_DRAWER       = 3;	
	public static final int FUNCTION_PAYOUTS      = 4;	
	public static final int FUNCTION_ADD_TABLE    = 5;	
	public static final int FUNCTION_ADD_CLERK    = 6;	
	public static final int FUNCTION_DEJAVOO_TIP  = 7;
	public static final int FUNCTION_DEJAVOO_RETURN = 8;
	public static final int FUNCTION_TSYS_TIP       = 9;
	public static final int FUNCTION_TSYS_RETURN    = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);
		setContentView(R.layout.activity_function_drawer);

		onInitViews();
		onListenerRegister();

		for(int index = 0; index < navMenuTitles.length ; index++)		
			navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[index], android.R.color.transparent));


		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			displayView(0);
		}
	}

	private void displayView(int position) {

		fragment      = new FunctionFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(PAGE_POSITION, position);

		if (fragment != null) {

			fragment.setArguments(bundle);
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();	
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			if (isDrawerOpen) {
				mDrawerLayout.openDrawer(mDrawerList);
				isDrawerOpen = !isDrawerOpen;
			}
			else		
				mDrawerLayout.closeDrawer(mDrawerList);
		}			
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onInitViews() {

		mTitle = mDrawerTitle = "Function";
		mDrawerLayout       = (DrawerLayout) findViewById(R.id.activity_home_drawer);
		mDrawerList         = (ListView) findViewById(R.id.list_slidermenu);
		navDrawerItems      = new ArrayList<NavDrawerItemModel>();
		mDrawerToggle       = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer,R.string.app_name,R.string.app_name)
		{
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};

	}

	@Override
	public void onListenerRegister() {
		mDrawerList.setOnItemClickListener(this);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position <=9 )
			displayView(position);
		else
			finish();
	}

	@Override
	public void onDataRecieved(JSONArray arry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocketStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100 && resultCode == -1 && fragment != null ){
			try{
				FunctionFragment u = (FunctionFragment)fragment;
				if (u.tipAdjusment != null) {
					u.tipAdjusment.dismiss();
				}
			}
			 catch(Exception ex){
				 ex.printStackTrace();
			 }
		}
		
	}
}
