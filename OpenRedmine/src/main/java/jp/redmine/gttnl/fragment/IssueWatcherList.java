package jp.redmine.gttnl.fragment;

import android.os.Bundle;

import com.j256.ormlite.android.apptools.OrmLiteListFragment;

import jp.redmine.gttnl.adapter.IssueWatcherListAdapter;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.param.IssueArgument;

public class IssueWatcherList extends OrmLiteListFragment<DatabaseCacheHelper> {
	private static final String TAG = IssueWatcherList.class.getSimpleName();
	private IssueWatcherListAdapter adapter;

	public IssueWatcherList(){
		super();
	}

	static public IssueWatcherList newInstance(IssueArgument arg){
		IssueWatcherList fragment = new IssueWatcherList();
		fragment.setArguments(arg.getArgument());
		return fragment;
	}

	@Override
	public void onDestroyView() {
		setListAdapter(null);
		super.onDestroyView();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setFastScrollEnabled(true);

		adapter = new IssueWatcherListAdapter(getHelper(), getActivity());
		IssueArgument intent = new IssueArgument();
		intent.setArgument(getArguments());
		adapter.setupParameter(intent.getConnectionId(),intent.getIssueId());
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onResume() {
		super.onResume();
		if(adapter != null)
			adapter.notifyDataSetChanged();
	}
}
