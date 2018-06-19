package jp.redmine.gttnl.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.activity.pager.CorePage;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.fragment.ActivityInterface;
import jp.redmine.gttnl.fragment.ConnectionList;
import jp.redmine.gttnl.fragment.ProjectFavoriteList;
import jp.redmine.gttnl.fragment.RecentIssueList;

public class ConnectionListActivity extends TabActivity<DatabaseCacheHelper>
	implements ActivityInterface {
	public ConnectionListActivity(){
		super();
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(getSupportActionBar()!=null) {
			ActionBar actionBar = getSupportActionBar();
			actionBar.setTitle(R.string.title_home);
			actionBar.setIcon(R.drawable.ic_logo);
			actionBar.setDisplayShowHomeEnabled(true);
		}

	}
	@Override
	protected List<CorePage> getTabs(){

		List<CorePage> list = new ArrayList<CorePage>();
		list.add((new CorePage<Void>() {
					@Override
					public Fragment getRawFragment(Void param) {
						return ConnectionList.newInstance();
					}
				})
						.setParam(null)
						.setName(getString(R.string.connection))
						.setIcon(R.drawable.ic_domain)
		);

		list.add((new CorePage<Void>() {
					@Override
					public Fragment getRawFragment(Void param) {
						return ProjectFavoriteList.newInstance();
					}
				})
				.setParam(null)
				.setName(getString(R.string.favorite))
				.setIcon(R.drawable.ic_project_favorite)
		);
		list.add((new CorePage<Void>() {
					@Override
					public Fragment getRawFragment(Void param) {
						return RecentIssueList.newInstance();
					}
				})
						.setParam(null)
						.setName(getString(R.string.recent_issues))
						.setIcon(R.drawable.ic_recent)
		);
		return list;
	}
}
