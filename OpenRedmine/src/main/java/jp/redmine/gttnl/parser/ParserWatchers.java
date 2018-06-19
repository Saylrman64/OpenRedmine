package jp.redmine.gttnl.parser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.SQLException;

import jp.redmine.gttnl.entity.RedmineIssue;
import jp.redmine.gttnl.entity.RedmineUser;
import jp.redmine.gttnl.entity.RedmineWatcher;

public class ParserWatchers extends BaseParserInternal<RedmineIssue,RedmineWatcher> {

	@Override
	protected String getProveTagName() {
		return "user";
	}

	@Override
	protected RedmineWatcher getNewProveTagItem() {
		RedmineWatcher watcher = new RedmineWatcher();
		RedmineUser tk = new RedmineUser();
		setMasterRecord(tk);
		watcher.setUser(tk);
		return watcher;
	}
	@Override
	protected void parseInternal(RedmineIssue con, RedmineWatcher item)
			throws XmlPullParserException, IOException, SQLException{
	}

	@Override
	protected void onTagEnd(RedmineIssue con)
		throws XmlPullParserException, IOException,SQLException {
		// stop parse appears end of the tag.
		if(equalsTagName("watchers")){
			haltParse();
		} else {
			super.onTagEnd(con);
		}
	}
}
