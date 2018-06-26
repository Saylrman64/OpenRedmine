package jp.redmine.gttnl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.j256.ormlite.android.apptools.OrmLiteFragment;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.activity.handler.IssueActionInterface;
import jp.redmine.gttnl.adapter.FavoriteProjectListAdapter;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.entity.RedmineProject;
import jp.redmine.gttnl.fragment.helper.ActivityHandler;
import jp.redmine.gttnl.param.ConnectionArgument;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ProjectFavoriteList extends OrmLiteFragment<DatabaseCacheHelper> {
	private static final String TAG = ProjectFavoriteList.class.getSimpleName();
	private IssueActionInterface mListener;
	private StickyListHeadersListView list;

	public ProjectFavoriteList(){
		super();
	}

	static public ProjectFavoriteList newInstance(){
		return new ProjectFavoriteList();
	}
	static public ProjectFavoriteList newInstance(ConnectionArgument arg){
		ProjectFavoriteList instance = new ProjectFavoriteList();
		instance.setArguments(arg.getArgument());
		return instance;
	}

	@Override
	public void onDestroyView() {
		if(list  != null)
			list.setAdapter(null);
		super.onDestroyView();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mListener = ActivityHandler.getHandler(getActivity(), IssueActionInterface.class);
		list.setFastScrollEnabled(true);

		FavoriteProjectListAdapter adapter = new FavoriteProjectListAdapter(getHelper(), getActivity());
		ConnectionArgument arg = new ConnectionArgument();
		arg.setArgument(getArguments());
		if(arg.getConnectionId() != -1)
			adapter.setupParameter(arg.getConnectionId());

		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				Object item =  adapterView.getItemAtPosition(i);
				if(item == null || !(item instanceof RedmineProject))
					return false;
				RedmineProject project = (RedmineProject)item;
				mListener.onKanbanList(project.getConnectionId(), project.getId());
				return true;
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View current = inflater.inflate(R.layout.page_stickylistheaderslist, container, false);
		list = (StickyListHeadersListView)current.findViewById(R.id.list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Object item =  adapterView.getItemAtPosition(position);
				if(item == null || !(item instanceof RedmineProject))
					return;
				RedmineProject project = (RedmineProject)item;
				mListener.onIssueList(project.getConnectionId(), project.getId());
			}
		});
		return current;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(list.getAdapter() != null && list.getAdapter() instanceof  BaseAdapter)
			((BaseAdapter)list.getAdapter()).notifyDataSetChanged();

	}
}
