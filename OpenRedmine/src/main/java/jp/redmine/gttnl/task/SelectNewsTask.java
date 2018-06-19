package jp.redmine.gttnl.task;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.db.cache.RedmineNewsModel;
import jp.redmine.gttnl.entity.RedmineConnection;
import jp.redmine.gttnl.entity.RedmineNews;
import jp.redmine.gttnl.entity.RedmineProject;
import jp.redmine.gttnl.parser.DataCreationHandler;
import jp.redmine.gttnl.parser.ParserNews;
import jp.redmine.gttnl.url.RemoteUrlNews;

public class SelectNewsTask extends SelectDataTask<Void,RedmineProject> {
	protected DatabaseCacheHelper helper;
	protected RedmineConnection connection;
	public SelectNewsTask(DatabaseCacheHelper helper, RedmineConnection con){
		this.helper = helper;
		this.connection = con;
	}


	public SelectNewsTask() {
	}

	@Override
	protected Void doInBackground(RedmineProject... params) {
		final RedmineNewsModel model = new RedmineNewsModel(helper);
		final ParserNews parser = new ParserNews();
		parser.registerDataCreation(new DataCreationHandler<RedmineProject,RedmineNews>() {
			public void onData(RedmineProject con,RedmineNews data) throws SQLException {
				data.setProject(con);
				model.refreshItem(connection,data);
			}
		});

		SelectDataTaskRedmineConnectionHandler client = new SelectDataTaskRedmineConnectionHandler(connection);
		RemoteUrlNews url = new RemoteUrlNews();
		for(final RedmineProject item : params){
			SelectDataTaskDataHandler handler = new SelectDataTaskDataHandler() {
				@Override
				public void onContent(InputStream stream)
						throws XmlPullParserException, IOException, SQLException {
					helperSetupParserStream(stream,parser);
					parser.parse(item);
				}
			};
			url.setProject(item);
			fetchData(client, url, handler);
		}
		return null;
	}

	@Override
	protected void onErrorRequest(int statuscode) {

	}

	@Override
	protected void onProgress(int max, int proc) {

	}

}
