package jp.redmine.gttnl.activity;

import android.os.Bundle;

import com.j256.ormlite.android.apptools.OrmLiteFragmentActivity;

import jp.redmine.gttnl.activity.helper.ActivityHelper;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.fragment.ActivityInterface;
import jp.redmine.gttnl.fragment.TimeEntryEdit;
import jp.redmine.gttnl.param.TimeEntryArgument;

public class TimeEntryActivity extends OrmLiteFragmentActivity<DatabaseCacheHelper>
	implements ActivityInterface {
	private static final String TAG = TimeEntryActivity.class.getSimpleName();
	public TimeEntryActivity(){
		super();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		ActivityHelper.setupTheme(this);
		super.onCreate(savedInstanceState);

		/**
		 * Add fragment on first view only
		 * On rotate, this method would be called with savedInstanceState.
		 */
		if(savedInstanceState != null)
			return;

		TimeEntryArgument arg = new TimeEntryArgument();
		arg.setArgument();
		{
			TimeEntryArgument intent = new TimeEntryArgument();
			intent.setIntent(getIntent());
			arg.importArgument(intent);
		}

		getSupportFragmentManager().beginTransaction()
				.replace(android.R.id.content, TimeEntryEdit.newInstance(arg))
				.commit();
	}

	@SuppressWarnings("unchecked")
	public <T> T getHandler(Class<T> cls){
		return null;
	}
}
