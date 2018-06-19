package jp.redmine.gttnl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.j256.ormlite.android.apptools.OrmLiteFragmentActivity;

import jp.redmine.gttnl.activity.handler.ConnectionActionInterface;
import jp.redmine.gttnl.activity.handler.ConnectionListHandler;
import jp.redmine.gttnl.activity.handler.Core;
import jp.redmine.gttnl.activity.helper.ActivityHelper;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.fragment.ActivityInterface;
import jp.redmine.gttnl.fragment.ConnectionEdit;
import jp.redmine.gttnl.param.ConnectionArgument;

public class ConnectionEditActivity extends OrmLiteFragmentActivity<DatabaseCacheHelper>
		implements ActivityInterface {
	private static final String TAG = ConnectionEditActivity.class.getSimpleName();
	public ConnectionEditActivity(){
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ActivityHelper.setupTheme(this);
		super.onCreate(savedInstanceState);
		getSupportActionBar();

		ConnectionArgument intent = new ConnectionArgument();
		intent.setIntent(getIntent());

		ConnectionArgument arg = new ConnectionArgument();
		arg.setArgument();
		arg.importArgument(intent);
		FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
		tran.replace(android.R.id.content, ConnectionEdit.newInstance(arg));
		tran.commit();

	}
	@SuppressWarnings("unchecked")
	public <T> T getHandler(Class<T> cls){
		Core.ActivityRegistry registry = new Core.ActivityRegistry(){

			@Override
			public FragmentManager getFragment() {
				return getSupportFragmentManager();
			}

			@Override
			public Intent getIntent(Class<?> activity) {
				return new Intent(getApplicationContext(),activity);
			}

			@Override
			public void kickActivity(Intent intent) {
				startActivity(intent);
			}

		};
		if(cls.equals(ConnectionActionInterface.class))
			return (T) new ConnectionListHandler(registry){
				@Override
				public void onConnectionSaved() {
					super.onConnectionSaved();
					finish();
				}
			};
		return null;
	}
}
