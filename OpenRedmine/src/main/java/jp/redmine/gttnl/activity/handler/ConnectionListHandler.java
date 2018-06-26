package jp.redmine.gttnl.activity.handler;

import android.content.Intent;

import jp.redmine.gttnl.activity.ConnectionActivity;
import jp.redmine.gttnl.activity.ConnectionEditActivity;
import jp.redmine.gttnl.param.ConnectionArgument;
import jp.redmine.gttnl.param.ProjectArgument;

public class ConnectionListHandler extends Core implements ConnectionActionInterface {

	public ConnectionListHandler(ActivityRegistry manager) {
		super(manager);
	}

	@Override
	public void onConnectionSelected(final int connectionid) {
		kickActivity(ConnectionActivity.class, new IntentFactory() {
			@Override
			public void generateIntent(Intent intent) {
				ProjectArgument arg = new ProjectArgument();
				arg.setIntent(intent);
				arg.setConnectionId(connectionid);
			}
		});
	}

	@Override
	public void onConnectionEdit(final int connectionid) {
		kickActivity(ConnectionEditActivity.class, new IntentFactory() {
			@Override
			public void generateIntent(Intent intent) {
				ConnectionArgument arg = new ConnectionArgument();
				arg.setIntent(intent);
				arg.setConnectionId(connectionid);
			}
		});

	}

	@Override
	public void onConnectionAdd() {
		onConnectionEdit(-1);
	}

	@Override
	public void onConnectionSaved() {

	}

}
