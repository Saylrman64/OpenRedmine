package jp.redmine.gttnl.model;

import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.db.store.DatabaseHelper;
import android.content.Context;

abstract public class Connector {
	protected DatabaseHelper helperStore;
	protected DatabaseCacheHelper helperCache;
	public Connector(Context context){
		helperCache = new DatabaseCacheHelper(context);
		helperStore = new DatabaseHelper(context);
	}

	public void close() {
		if(helperCache != null){
			helperCache.close();
			helperCache = null;
		}
		if(helperStore != null){
			helperStore.close();
			helperStore = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
}
