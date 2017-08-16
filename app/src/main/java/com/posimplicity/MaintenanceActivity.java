package com.posimplicity;

import java.util.ArrayList;

import org.json.JSONArray;

import com.Beans.NavDrawerItemModel;
import com.Beans.RoleInfo;
import com.adapter.NavDrawerListAdapter;
import com.CustomControls.ProgressHUD;
import com.posimplicity.database.local.RoleTable;
import com.posimplicity.database.local.SecurityTable;
import com.Fragments.BaseFragment;
import com.Fragments.MaintFragmentAbout;
import com.Fragments.MaintFragmentAdmin;
import com.Fragments.MaintFragmentCCAdmin;
import com.Fragments.MaintFragmentCurrentRewards;
import com.Fragments.MaintFragmentOtherSetting;
import com.Fragments.MaintFragmentPrinterSetting;
import com.Fragments.MaintFragmentSupport;
import com.Fragments.MaintFragmentSync;
import com.Fragments.MaintFragmentTender1;
import com.Fragments.SecurityFragment;
import com.utils.MyPreferences;
import com.utils.StartAndroidActivity;
import com.utils.ToastUtils;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MaintenanceActivity extends BaseActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItemModel> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private boolean isDrawerOpen = true;
	private BaseFragment fragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);
		setContentView(R.layout.activity_maintenance);

		mTitle = mDrawerTitle = "Maintenance";

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_drawer);
		mDrawerList   = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItemModel>();

		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));		
		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
		navDrawerItems.add(new NavDrawerItemModel(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item			
			new UiUpdate(0).execute();
		}
	}	

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			new UiUpdate(position).execute();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {

		case R.id.PayGrade:
			RoleInfo roleInfo          = new RoleTable(mContext).getSingleInfoFromTableByRoleName(MyPreferences.getMyPreference(SECURITY_LOGIN_USER_Id, mContext));
			if(roleInfo.getRoleName().equals(SecurityTable.Admin))			
				StartAndroidActivity.onActivityStart(false, mContext, AddClerkPaygradeActivity.class);
			else
				ToastUtils.showOwnToast(mContext, "Please Contact Super Admin To Unlock 'PayGrade' Functionality");
			return true;

		case R.id.Comment:
			StartAndroidActivity.onActivityStart(false, mContext, CommentActivity.class);
			return true;

		case R.id.PayoutDesc:
			StartAndroidActivity.onActivityStart(false, mContext, AddDescriptionActivity.class);
			return true;

		case R.id.ClerkSales:
			RoleInfo roleInfo1          = new RoleTable(mContext).getSingleInfoFromTableByRoleName(MyPreferences.getMyPreference(SECURITY_LOGIN_USER_Id, mContext));
			if(roleInfo1.getRoleName().equals(SecurityTable.Admin))			
				StartAndroidActivity.onActivityStart(false, mContext, AddClerkSalesActivity.class);
			else
				ToastUtils.showOwnToast(mContext, "Please Contact Super Admin To Unlock 'Clerk Sales' Functionality");

			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		globalApp.setVisibleFragment(null);
	}


	private void displayView(int position) {


		// update the main content by replacing fragments

		switch (position) {		
		case 8:
			fragment = new MaintFragmentAdmin();
			break;		
		case 2:
			fragment = new MaintFragmentSupport();			
			break;

		case 3:
			fragment = new MaintFragmentSync();
			break;

		case 4:
			fragment = new MaintFragmentCurrentRewards();
			break;

		case 0:
			fragment = new MaintFragmentPrinterSetting();
			break;

		case 1:
			fragment = new MaintFragmentCCAdmin();
			break;		
		case 5:
			fragment = new SecurityFragment();
			break;
		case 6:
			fragment = new MaintFragmentOtherSetting();
			break;

		case 7:
			fragment = new MaintFragmentTender1();
			break;

		case 9:
			fragment = new MaintFragmentAbout();
			break;

		case 10:			
			finish();
			globalApp.setVisibleFragment(null);
			break;	
		default:
			break;
		}

		if (fragment != null) {

			globalApp.setVisibleFragment(fragment);
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();	

		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
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


	class UiUpdate extends AsyncTask<Void, Integer, Void> implements OnCancelListener
	{
		int position;
		ProgressHUD progressHud;		

		public UiUpdate(int position) {
			super();
			this.position = position;
		}

		@Override
		protected void onPreExecute() {
			progressHud = ProgressHUD.show(MaintenanceActivity.this,"", true, true, this);
			super.onPreExecute();
		}


		@Override
		protected Void doInBackground(Void... params) {		
			publishProgress(1);
			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			if(values[0]==1)
			{
				displayView(position);
			}
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(Void result) {
			progressHud.dismiss();
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			if (isDrawerOpen) {
				mDrawerLayout.openDrawer(mDrawerList);
				isDrawerOpen = !isDrawerOpen;
			}
			else
			{
				mDrawerLayout.closeDrawer(mDrawerList);
			}			
			super.onPostExecute(result);
		}


		@Override
		public void onCancel(DialogInterface dialog) {
			progressHud.dismiss();			
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onInitViews() {}

	@Override
	public void onListenerRegister() {}

	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}

}