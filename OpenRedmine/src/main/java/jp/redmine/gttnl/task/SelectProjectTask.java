package jp.redmine.gttnl.task;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.db.cache.RedminePriorityModel;
import jp.redmine.gttnl.db.cache.RedmineProjectModel;
import jp.redmine.gttnl.db.cache.RedmineStatusModel;
import jp.redmine.gttnl.db.cache.RedmineTimeActivityModel;
import jp.redmine.gttnl.db.cache.RedmineTrackerModel;
import jp.redmine.gttnl.db.cache.RedmineUserModel;
import jp.redmine.gttnl.entity.RedmineConnection;
import jp.redmine.gttnl.entity.RedminePriority;
import jp.redmine.gttnl.entity.RedmineProject;
import jp.redmine.gttnl.entity.RedmineStatus;
import jp.redmine.gttnl.entity.RedmineTimeActivity;
import jp.redmine.gttnl.entity.RedmineTracker;
import jp.redmine.gttnl.entity.RedmineUser;
import jp.redmine.gttnl.parser.DataCreationHandler;
import jp.redmine.gttnl.parser.ParserEnumerationIssuePriority;
import jp.redmine.gttnl.parser.ParserEnumerationTimeEntryActivity;
import jp.redmine.gttnl.parser.ParserProject;
import jp.redmine.gttnl.parser.ParserStatus;
import jp.redmine.gttnl.parser.ParserTracker;
import jp.redmine.gttnl.parser.ParserUser;
import jp.redmine.gttnl.url.RemoteUrlEnumerations;
import jp.redmine.gttnl.url.RemoteUrlEnumerations.EnumerationType;
import jp.redmine.gttnl.url.RemoteUrlProjects;
import jp.redmine.gttnl.url.RemoteUrlStatus;
import jp.redmine.gttnl.url.RemoteUrlTrackers;
import jp.redmine.gttnl.url.RemoteUrlUsers;

public class SelectProjectTask extends SelectDataTask<Void,RedmineConnection> {

	protected DatabaseCacheHelper helper;
	public SelectProjectTask(DatabaseCacheHelper helper){
		this.helper = helper;
	}


	public SelectProjectTask() {
	}

	@Override
	protected Void doInBackground(RedmineConnection... params) {
		for(RedmineConnection connection : params ){
			int limit = 20;
			int offset = 0;
			int count = 0;
			SelectDataTaskRedmineConnectionHandler client = new SelectDataTaskRedmineConnectionHandler(connection);
			fetchStatus(connection,client);
			fetchUsers(connection,client);
			fetchCurrentUser(connection,client);
			fetchTracker(connection,client);
			fetchPriority(connection,client);
			fetchTimeEntryActivity(connection,client);
			do {
				List<RedmineProject> projects = fetchProject(connection,client,offset,limit);
				count = projects.size();
				//TODO
				notifyProgress(0, 0);
				if(offset != 0){
					//sleep for server
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						publishError(e);
					}
				}
				offset += limit;
			} while(count >= limit);
		}
		return null;
	}

	protected List<RedmineProject> fetchProject(final RedmineConnection connection, SelectDataTaskRedmineConnectionHandler client, int offset, int limit){
		final RedmineProjectModel model =
			new RedmineProjectModel(helper);
		RemoteUrlProjects url = new RemoteUrlProjects();
		final List<RedmineProject> projects = new ArrayList<RedmineProject>();
		url.filterLimit(limit);
		if(offset != 0)
			url.filterOffset(offset);
		fetchData(client, url, new SelectDataTaskDataHandler() {
			@Override
			public void onContent(InputStream stream)
					throws XmlPullParserException, IOException, SQLException {
				ParserProject parser = new ParserProject();
				parser.registerDataCreation(new DataCreationHandler<RedmineConnection,RedmineProject>() {
					public void onData(RedmineConnection con,RedmineProject data) throws SQLException {
						model.refreshItem(con,data);
						projects.add(data);
					}
				});
				helperSetupParserStream(stream,parser);
				parser.parse(connection);
			}
		});
		return projects;
	}
	protected void fetchStatus(final RedmineConnection connection, SelectDataTaskRedmineConnectionHandler client){
		final RedmineStatusModel model = new RedmineStatusModel(helper);
		RemoteUrlStatus url = new RemoteUrlStatus();

		fetchData(client, url, new SelectDataTaskDataHandler() {
			@Override
			public void onContent(InputStream stream)
					throws XmlPullParserException, IOException, SQLException {
				ParserStatus parser = new ParserStatus();
				parser.registerDataCreation(new DataCreationHandler<RedmineConnection,RedmineStatus>() {
					public void onData(RedmineConnection con,RedmineStatus data) throws SQLException {
						model.refreshItem(con,data);
					}
				});
				helperSetupParserStream(stream,parser);
				parser.parse(connection);
			}
		});
	}
	protected void fetchTracker(final RedmineConnection connection, SelectDataTaskRedmineConnectionHandler client){
		final RedmineTrackerModel model = new RedmineTrackerModel(helper);
		RemoteUrlTrackers url = new RemoteUrlTrackers();

		fetchData(client, url, new SelectDataTaskDataHandler() {
			@Override
			public void onContent(InputStream stream)
					throws XmlPullParserException, IOException, SQLException {
				ParserTracker parser = new ParserTracker();
				parser.registerDataCreation(new DataCreationHandler<RedmineConnection,RedmineTracker>() {
					public void onData(RedmineConnection con,RedmineTracker data) throws SQLException {
						model.refreshItem(con,data);
					}
				});
				helperSetupParserStream(stream,parser);
				parser.parse(connection);
			}
		});
	}
	protected void fetchPriority(final RedmineConnection connection, SelectDataTaskRedmineConnectionHandler client){
		final RedminePriorityModel model = new RedminePriorityModel(helper);
		RemoteUrlEnumerations url = new RemoteUrlEnumerations();
		url.setType(EnumerationType.IssuePriorities);

		fetchData(client, url, new SelectDataTaskDataHandler() {
			@Override
			public void onContent(InputStream stream)
					throws XmlPullParserException, IOException, SQLException {
				ParserEnumerationIssuePriority parser = new ParserEnumerationIssuePriority();
				parser.registerDataCreation(new DataCreationHandler<RedmineConnection,RedminePriority>() {
					public void onData(RedmineConnection con,RedminePriority data) throws SQLException {
						model.refreshItem(con,data);
					}
				});
				helperSetupParserStream(stream,parser);
				parser.parse(connection);
			}
		});
	}
	protected void fetchTimeEntryActivity(final RedmineConnection connection, SelectDataTaskRedmineConnectionHandler client){
		final RedmineTimeActivityModel model = new RedmineTimeActivityModel(helper);
		RemoteUrlEnumerations url = new RemoteUrlEnumerations();
		url.setType(EnumerationType.TimeEntryActivities);

		fetchData(client, url, new SelectDataTaskDataHandler() {
			@Override
			public void onContent(InputStream stream)
					throws XmlPullParserException, IOException, SQLException {
				ParserEnumerationTimeEntryActivity parser = new ParserEnumerationTimeEntryActivity();
				parser.registerDataCreation(new DataCreationHandler<RedmineConnection,RedmineTimeActivity>() {
					public void onData(RedmineConnection con,RedmineTimeActivity data) throws SQLException {
						model.refreshItem(con,data);
					}
				});
				helperSetupParserStream(stream,parser);
				parser.parse(connection);
			}
		});
	}
	protected void fetchUsers(final RedmineConnection connection, SelectDataTaskRedmineConnectionHandler client){
		final RedmineUserModel model = new RedmineUserModel(helper);
		RemoteUrlUsers url = new RemoteUrlUsers();

		fetchData(client, url, new SelectDataTaskDataHandler() {
			@Override
			public void onContent(InputStream stream)
					throws XmlPullParserException, IOException, SQLException {
				ParserUser parser = new ParserUser();
				parser.registerDataCreation(new DataCreationHandler<RedmineConnection,RedmineUser>() {
					public void onData(RedmineConnection con,RedmineUser data) throws SQLException {
						data.setupNameFromSeparated();
						model.refreshItem(con,data);
					}
				});
				helperSetupParserStream(stream,parser);
				parser.parse(connection);
			}
		});
	}
	protected void fetchCurrentUser(final RedmineConnection connection, SelectDataTaskRedmineConnectionHandler client){
		final RedmineUserModel model = new RedmineUserModel(helper);
		RemoteUrlUsers url = new RemoteUrlUsers();
		url.filterCurrentUser();

		fetchData(client, url, new SelectDataTaskDataHandler() {
			@Override
			public void onContent(InputStream stream)
					throws XmlPullParserException, IOException, SQLException {
				ParserUser parser = new ParserUser();
				parser.registerDataCreation(new DataCreationHandler<RedmineConnection,RedmineUser>() {
					public void onData(RedmineConnection con,RedmineUser data) throws SQLException {
						data.setupNameFromSeparated();
						model.refreshCurrentUser(con,data);
					}
				});
				helperSetupParserStream(stream,parser);
				parser.parse(connection);
			}
		});
	}

	@Override
	protected void onErrorRequest(int statuscode) {
	}

	@Override
	protected void onProgress(int max, int proc) {
	}

}
