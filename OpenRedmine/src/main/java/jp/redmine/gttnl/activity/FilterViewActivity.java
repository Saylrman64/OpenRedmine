package jp.redmine.gttnl.activity;


import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteFragmentActivity;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.activity.helper.ActivityHelper;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.db.cache.RedmineFilterModel;
import jp.redmine.gttnl.entity.RedmineFilter;
import jp.redmine.gttnl.entity.RedmineProject;
import jp.redmine.gttnl.form.RedmineIssueFilter;
import jp.redmine.gttnl.param.ProjectArgument;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class FilterViewActivity extends OrmLiteFragmentActivity<DatabaseCacheHelper> {
	public FilterViewActivity(){
		super();
	}

	private RedmineIssueFilter form;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityHelper.setupTheme(this);
		setContentView(R.layout.page_issuefilter);
		getSupportActionBar();

		form = new RedmineIssueFilter();
		form.setup(this,getHelper());

	}

	@Override
	protected void onStart() {
		ProjectArgument intent = new ProjectArgument();
		intent.setIntent(getIntent());
		final int connectionid = intent.getConnectionId();
		final long projectid = intent.getProjectId();
		form.setupEvents();
		form.setupParameter(connectionid, projectid);
		form.refresh();
		form.setFilter(connectionid, projectid);
		form.buttonSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RedmineFilter filter = form.getFilter(null);
				RedmineProject project = new RedmineProject();
				project.setConnectionId(connectionid);
				project.setId(projectid);
				filter.setProject(project);
				filter.setConnectionId(connectionid);

				RedmineFilterModel model = new RedmineFilterModel(getHelper());
				try {
					model.updateSynonym(filter);
				} catch (SQLException e) {
					Log.e("FilterViewActivity","buttonSave.OnClick",e);
				}
				finish();
			}
		});
		super.onStart();
	}
}
