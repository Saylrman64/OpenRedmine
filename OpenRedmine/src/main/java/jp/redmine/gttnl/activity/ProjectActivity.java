package jp.redmine.gttnl.activity;

import android.support.v4.app.Fragment;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.activity.pager.CorePage;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.db.cache.RedmineFilterModel;
import jp.redmine.gttnl.db.cache.RedmineProjectModel;
import jp.redmine.gttnl.db.cache.RedmineUserModel;
import jp.redmine.gttnl.entity.RedmineFilter;
import jp.redmine.gttnl.entity.RedmineFilterSortItem;
import jp.redmine.gttnl.entity.RedmineProject;
import jp.redmine.gttnl.entity.RedmineUser;
import jp.redmine.gttnl.fragment.ActivityInterface;
import jp.redmine.gttnl.fragment.CategoryList;
import jp.redmine.gttnl.fragment.IssueList;
import jp.redmine.gttnl.fragment.NewsList;
import jp.redmine.gttnl.fragment.ProjectDetail;
import jp.redmine.gttnl.fragment.VersionList;
import jp.redmine.gttnl.fragment.WikiList;
import jp.redmine.gttnl.param.FilterArgument;
import jp.redmine.gttnl.param.ProjectArgument;


public class ProjectActivity extends TabActivity<DatabaseCacheHelper>
	implements ActivityInterface {
	private static final String TAG = ProjectActivity.class.getSimpleName();
	public ProjectActivity(){
		super();
	}

	@Override
	protected List<CorePage> getTabs(){

		ProjectArgument intent = new ProjectArgument();
		intent.setIntent(getIntent());

		// setup navigation
		try {
			RedmineProjectModel mProject = new RedmineProjectModel(getHelper());
			RedmineProject proj = mProject.fetchById(intent.getProjectId());
			if(proj.getId() != null)
				setTitle(proj.getName());
		} catch (SQLException e) {
			Log.e(TAG, "getTabs", e);
		}

		List<CorePage> list = new ArrayList<CorePage>();

		// project detail
		ProjectArgument argProject = new ProjectArgument();
		argProject.setArgument();
		argProject.importArgument(intent);
		list.add((new CorePage<ProjectArgument>() {
					@Override
					public Fragment getRawFragment(ProjectArgument param) {
						return ProjectDetail.newInstance(param);
					}
				})
						.setParam(argProject)
						.setName(getString(R.string.ticket_project))
						.setIcon(R.drawable.ic_project)
		);

             // version
		ProjectArgument argVersion = new ProjectArgument();
		argVersion.setArgument();
		argVersion.importArgument(intent);
		list.add((new CorePage<ProjectArgument>() {
					@Override
					public Fragment getRawFragment(ProjectArgument param) {
						return VersionList.newInstance(param);
					}
				})
						.setParam(argVersion)
						.setName(getString(R.string.ticket_version))
						.setIcon(R.drawable.ic_version)
		);

		// Project list
		ProjectArgument argList = new ProjectArgument();
		argList.setArgument();
		argList.importArgument(intent);
		list.add((new CorePage<ProjectArgument>() {
					@Override
					public Fragment getRawFragment(ProjectArgument param) {
						return IssueList.newInstance(param);
					}
				})
				.setParam(argList)
				.setName(getString(R.string.ticket_issue))
				.setIcon(R.drawable.ic_issue)
		);



		// current user
		RedmineUserModel mUserModel = new RedmineUserModel(getHelper());
		RedmineFilterModel mFilter = new RedmineFilterModel(getHelper());
		RedmineProjectModel mProjectModel = new RedmineProjectModel(getHelper());
		try {
			RedmineProject project = mProjectModel.fetchById(intent.getProjectId());
			final RedmineUser user = mUserModel.fetchCurrentUser(intent.getConnectionId());
			if(user != null){
				//setup parameter
				RedmineFilter filter = new RedmineFilter();
				filter.setConnectionId(intent.getConnectionId());
				filter.setAssigned(user);
				filter.setProject(project);
				filter.setSort(RedmineFilterSortItem.getFilter(RedmineFilterSortItem.KEY_MODIFIED, false));
				RedmineFilter target = mFilter.getSynonym(filter);
				if (target == null) {
					mFilter.insert(filter);
					target = filter;
				}

				FilterArgument argUser = new FilterArgument();
				argUser.setArgument();
				argUser.importArgument(intent);
				argUser.setFilterId(target.getId());
				list.add((new CorePage<FilterArgument>() {
							@Override
							public Fragment getRawFragment(FilterArgument param) {
								return IssueList.newInstance(param);
							}
						})
						.setParam(argUser)
						.setName(getString(R.string.my_issue))
						.setIcon(R.drawable.ic_action_user)
				);
			}
		} catch (SQLException e) {
			Log.e(TAG,"fetchCurrentUser", e);
		}







		// category
		ProjectArgument argCategory = new ProjectArgument();
		argCategory.setArgument();
		argCategory.importArgument(intent);
		list.add((new CorePage<ProjectArgument>() {
					@Override
					public Fragment getRawFragment(ProjectArgument param) {
						return CategoryList.newInstance(param);
					}
				})
				.setParam(argCategory)
				.setName(getString(R.string.ticket_category))
				.setIcon(R.drawable.ic_category)
		);

		// wiki
		ProjectArgument argWiki = new ProjectArgument();
		argWiki.setArgument();
		argWiki.importArgument(intent);
		list.add((new CorePage<ProjectArgument>() {
					@Override
					public Fragment getRawFragment(ProjectArgument param) {
						return WikiList.newInstance(param);
					}
				})
						.setParam(argWiki)
						.setName(getString(R.string.wiki))
						.setIcon(R.drawable.ic_text_fields)
		);

		// news
		ProjectArgument argNews = new ProjectArgument();
		argNews.setArgument();
		argNews.importArgument(intent);
		list.add((new CorePage<ProjectArgument>() {
					@Override
					public Fragment getRawFragment(ProjectArgument param) {
						return NewsList.newInstance(param);
					}
				})
				.setParam(argNews)
				.setName(getString(R.string.news))
				.setIcon(R.drawable.ic_news)
		);


		return list;
	}

}
