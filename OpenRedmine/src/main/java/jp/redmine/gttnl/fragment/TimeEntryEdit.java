package jp.redmine.gttnl.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteFragment;

import java.sql.SQLException;
import java.util.Calendar;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.activity.helper.ActivityHelper;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.db.cache.RedmineTimeEntryModel;
import jp.redmine.gttnl.entity.RedmineConnection;
import jp.redmine.gttnl.entity.RedmineTimeEntry;
import jp.redmine.gttnl.fragment.form.TimeEntryEditForm;
import jp.redmine.gttnl.model.ConnectionModel;
import jp.redmine.gttnl.param.TimeEntryArgument;
import jp.redmine.gttnl.task.SelectTimeEntriesPost;

public class TimeEntryEdit extends OrmLiteFragment<DatabaseCacheHelper> {
	private TimeEntryEditForm form;
	private SwipeRefreshLayout mSwipeRefreshLayout;

	public TimeEntryEdit(){
		super();
	}


	static public TimeEntryEdit newInstance(TimeEntryArgument intent){
		TimeEntryEdit instance = new TimeEntryEdit();
		instance.setArguments(intent.getArgument());
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.input_timeentry, container, false);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layoutSwipeRefresh);
		mSwipeRefreshLayout.setEnabled(false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		form = new TimeEntryEditForm(getView());
		form.setupDatabase(getHelper());

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onStart() {
		super.onStart();
		onRefresh(true);
	}


	protected void onRefresh(boolean isFetch){
		TimeEntryArgument intent = new TimeEntryArgument();
		intent.setArgument(getArguments());
		int connectionid = intent.getConnectionId();

		form.setupParameter(connectionid, 0);

		RedmineTimeEntry timeentry = new RedmineTimeEntry();
		RedmineTimeEntryModel model = new RedmineTimeEntryModel(getHelper());

		if(intent.getTimeEntryId() != -1){
			try {
				timeentry = model.fetchById(connectionid, intent.getTimeEntryId());
			} catch (SQLException e) {
				Log.e("SelectDataTask","ParserIssue",e);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			timeentry.setSpentsOn(cal.getTime());
		}
		form.setValue(timeentry);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate( R.menu.edit, menu );
        super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch ( item.getItemId() )
		{
			case R.id.menu_save:
			{
				if(!form.Validate())
					return true;
				TimeEntryArgument intent = new TimeEntryArgument();
				intent.setArgument(getArguments());
				int connectionid = intent.getConnectionId();
				RedmineConnection connection = ConnectionModel.getItem(getActivity(), connectionid);

				RedmineTimeEntry timeentry = new RedmineTimeEntry();
				RedmineTimeEntryModel model = new RedmineTimeEntryModel(getHelper());

				if(intent.getTimeEntryId() != -1){
					try {
						timeentry = model.fetchById(connectionid, intent.getTimeEntryId());
					} catch (SQLException e) {
						Log.e("SelectDataTask","ParserIssue",e);
					}
				}
				form.getValue(timeentry);
				SelectTimeEntriesPost post = new SelectTimeEntriesPost(getHelper(), connection){
					private boolean isSuccess = true;
					@Override
					protected void onError(Exception lasterror) {
						isSuccess = false;
						ActivityHelper.toastRemoteError(getActivity(), ActivityHelper.ERROR_APP);
						super.onError(lasterror);
					}
					@Override
					protected void onErrorRequest(int statuscode) {
						isSuccess = false;
						ActivityHelper.toastRemoteError(getActivity(), statuscode);
						super.onErrorRequest(statuscode);
					}
					@Override
					protected void onPreExecute() {
						if(mSwipeRefreshLayout != null) {
							mSwipeRefreshLayout.setEnabled(true);
							mSwipeRefreshLayout.setRefreshing(true);
						}
						super.onPreExecute();
					}
					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if(mSwipeRefreshLayout != null) {
							mSwipeRefreshLayout.setEnabled(false);
							mSwipeRefreshLayout.setRefreshing(false);
						}
						if(isSuccess){
							if(getActivity() != null)
								Toast.makeText(getActivity().getApplicationContext(), R.string.remote_saved, Toast.LENGTH_LONG).show();
							getFragmentManager().popBackStack();
						}
					}
				};
				if(timeentry.getId() == null){
					timeentry.setIssueId(intent.getIssueId());
				}
				post.execute(timeentry);
				return true;
			}
			case R.id.menu_delete:
			{

				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

}
