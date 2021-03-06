package com.rui.android_client.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.tasks.AuthorUserTask;

public class HomeActivity extends FragmentActivity implements
		OnTabChangeListener {

	private static String[] tabSpec = new String[] { "INDEX", "TYPE", "SEARCH",
			"MANAGER", "MORE" };
	private static String[] tabIndicator = new String[] { "首页", "类目", "搜索",
			"管理", "更多" };
	private static Class[] tabFragments = new Class[] { IndexFragment.class,
			CategoryFragment.class, SearchFragment.class,
			ManagerFragment.class, MoreFragment.class };

	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 首次登录获取userId
		RuiApp ruiApp = (RuiApp) getApplication();
		ruiApp.asyInvoke(new AuthorUserTask(ruiApp));

		setContentView(R.layout.activity_home);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.setOnTabChangedListener(this);
		mTabHost.getTabWidget().setDividerDrawable(null);

		for (int i = 0; i < tabSpec.length; i++) {
			mTabHost.addTab(
					mTabHost.newTabSpec(tabSpec[i]).setIndicator(
							getTabView(tabIndicator[i])), tabFragments[i], null);
		}

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	@Override
	public void onTabChanged(String tabId) {

	}

	private View getTabView(String tabName) {
		View view = View.inflate(this, R.layout.layout_bottombar_tab, null);
		TextView text = (TextView) view.findViewById(R.id.tab_name);
		text.setText(tabName);
		return view;
	}

}
