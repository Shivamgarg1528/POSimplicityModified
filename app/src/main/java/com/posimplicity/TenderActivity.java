package com.posimplicity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.Fragments.BarFragment;
import com.Fragments.BaseFragment;
import com.Fragments.CashFragment;
import com.Fragments.CheckFragment;
import com.Fragments.CreditFragment;
import com.Fragments.Custom1Fragment;
import com.Fragments.Custom2Fragment;
import com.Fragments.MaintFragmentTender1;
import com.Fragments.ManualFragment;
import com.Fragments.PendingFragment;
import com.Fragments.TenderCardFragment;
import com.utils.CalculateWidthAndHeigth;
import com.utils.LocationOfPosApp;
import com.utils.TenderEnable;
import com.adapter.TabsPagerAdapter;
import com.adapter.TabsPagerAdapter.OnTabItemSelected;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class TenderActivity extends BaseActivity implements ActionBar.TabListener, OnPageChangeListener, OnTabItemSelected {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private android.app.FragmentManager fragmentManager;
	private List<String> tabsName;
	public  ArrayList<Fragment> fragmentArrayList;
	public  boolean leavedThisFragment = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);	
		tabsName          = new ArrayList<>();
		fragmentArrayList = new ArrayList<Fragment>();

		if(TenderEnable.isCreditTenderEnable(mContext)){
			tabsName.add("Credit");
			fragmentArrayList.add(new CreditFragment());
		}

		if(TenderEnable.isCashTenderEnable(mContext)){
			tabsName.add("Cash");
			fragmentArrayList.add(new CashFragment());
		}

		if(TenderEnable.isCheckTenderEnable(mContext)){
			tabsName.add("Check");
			fragmentArrayList.add(new CheckFragment());
		}

		if(TenderEnable.isCustom1TenderEnable(mContext)){
			tabsName.add(MaintFragmentTender1.getCustom1Name(mContext));
			fragmentArrayList.add(new Custom1Fragment());
		}

		if(TenderEnable.isCustom2TenderEnable(mContext))
		{
			tabsName.add(MaintFragmentTender1.getCustom2Name(mContext));
			fragmentArrayList.add(new Custom2Fragment());
		}
		
		if(TenderEnable.isTenderTenderEnable(mContext)){
			tabsName.add("Rewards");
			fragmentArrayList.add(new TenderCardFragment());
		}
		
		if(TenderEnable.isUnRecordedTenderEnable(mContext)){
			tabsName.add("Unrecorded");
			fragmentArrayList.add(new ManualFragment());
		}	
		
		tabsName.add("Pending");

		if(LocationOfPosApp.isRestraActive(this))
			fragmentArrayList.add(new PendingFragment());
		else if(LocationOfPosApp.isBarActive(this))
			fragmentArrayList.add(new BarFragment());

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		getWindow().setFlags(LayoutParams.FLAG_KEEP_SCREEN_ON, LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		int width   = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceWidth() ,61);//61
		int height  = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceHeight(),85);//85		
		LayoutParams params = getWindow().getAttributes();
		params.width  = width; //  fixed width   61%
		params.height = height; // fixed height  85%		
		params.alpha  = 1.0f;	
		params.x = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceWidth(),16);	
		params.y = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceHeight(),3);
		getWindow().setAttributes(params);
		setContentView(R.layout.tab_activity);

		viewPager = (ViewPager) findViewById(R.id.pager);
		fragmentManager = getFragmentManager();
		mAdapter = new TabsPagerAdapter(fragmentManager,this,fragmentArrayList,this,viewPager);		
		actionBar = getActionBar();		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		for(int index = 0 ; index < tabsName.size() ;index ++){
			if(tabsName.get(index).equalsIgnoreCase("Pending")){
				if(!LocationOfPosApp.isRetailsIsActive(this) && !LocationOfPosApp.isQuickIsActive(this)){					
					actionBar.addTab(actionBar.newTab().setText(tabsName.get(index)).setTabListener(this));
				}
			}
			else
				actionBar.addTab(actionBar.newTab().setText(tabsName.get(index)).setTabListener(this));		
		}

		viewPager.setAdapter(mAdapter);		
		viewPager.setOnPageChangeListener(this);
		viewPager.setOffscreenPageLimit(1);	

	}

	@Override
	protected void onDestroy() {		
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {	

	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {	

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {		
		viewPager.setCurrentItem(tab.getPosition());
	
		if(mAdapter.isFinish())
			onTabFragmentSelected((BaseFragment)getFragment(viewPager));
	
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}



	@Override
	public void onPageSelected(int position) {
		actionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	public Fragment getFragment(ViewPager pager){   
		Fragment theFragment = fragmentArrayList.get(pager.getCurrentItem());
		return theFragment;
	}

	public void onCashFragmentClick(View view){

		Fragment fragment = getFragment(viewPager);
		if(fragment instanceof CashFragment ){
			CashFragment theFragment = (CashFragment)fragment;
			theFragment.onCashFragmentClick(view);
		}
		else {
			TenderCardFragment theFragment = (TenderCardFragment)fragment;
			theFragment.onCashFragmentClick(view);
		}
	}

	public void onCreditFragmentClick(View view){
		CreditFragment theFragment = (CreditFragment)getFragment(viewPager);
		theFragment.onCreditFragmentClick(view);
	}

	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}

	@Override
	public void onInitViews() {}

	@Override
	public void onListenerRegister() {}

	@Override
	public void onItemSelected(int position, BaseFragment tempFragment) {
		onTabFragmentSelected(tempFragment);
	}

	private void onTabFragmentSelected(BaseFragment tempFragment) {
		if(tempFragment instanceof TenderCardFragment){
			TenderCardFragment theFrag = (TenderCardFragment) tempFragment;		   
			theFrag.showTenderFragmentOptions();
		}
		else if(tempFragment instanceof CreditFragment){
			CreditFragment thefrag = (CreditFragment) tempFragment;				
			thefrag.onUpadte();
		}		
		else if(tempFragment instanceof CheckFragment){
			CheckFragment thefrag = (CheckFragment) tempFragment;
			thefrag.onChequeAmountUpdate();
		}
		else if( tempFragment instanceof Custom1Fragment){
			Custom1Fragment thFragment = (Custom1Fragment) tempFragment;
			thFragment.onCustom1AmountUpdate();
		}
		else if( tempFragment instanceof Custom2Fragment){
			Custom2Fragment thFragment = (Custom2Fragment) tempFragment;
			thFragment.onCustom2AmountUpdate();
		}
	}
}