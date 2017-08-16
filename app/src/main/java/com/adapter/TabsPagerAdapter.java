package com.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.Fragments.BaseFragment;

import java.util.ArrayList;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {	


	private ArrayList<Fragment> fragmentArrayList;
	private OnTabItemSelected   listener;
	private ViewPager viewPager;
	private boolean isFinish = false;


	/**
	 * @return the isFinish
	 */
	public boolean isFinish() {
		return isFinish;
	}

	/**
	 * @param isFinish the isFinish to set
	 */
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public interface OnTabItemSelected{
		public void onItemSelected(int position, BaseFragment baseFragment);
	}

	public TabsPagerAdapter(FragmentManager fragmentManager, Context  mContext, ArrayList<Fragment> fragmentArrayList, OnTabItemSelected listener, ViewPager viewPager) {		
		super(fragmentManager);	
		this.fragmentArrayList = fragmentArrayList;	
		this.listener          = listener;
		this.viewPager         = viewPager;
	}	

	@Override
	public int getCount() {
		return fragmentArrayList.size();
	} 	

	@Override
	public Fragment getItem(int position) {		
		return fragmentArrayList.get(position);
	}	

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
		if(!isFinish()){
			setFinish(true);
			listener.onItemSelected(viewPager.getCurrentItem(), (BaseFragment)fragmentArrayList.get(viewPager.getCurrentItem()));
		}
	}	
}

