package jp.redmine.gttnl.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.sql.SQLException;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.db.cache.RedmineVersionModel;
import jp.redmine.gttnl.entity.RedmineProjectVersion;
import jp.redmine.gttnl.adapter.form.IMasterRecordForm;

public class VersionListAdapter extends RedmineBaseAdapter<RedmineProjectVersion> {
	private RedmineVersionModel model;
	protected Integer connection_id;
	protected Long project_id;
	public VersionListAdapter(DatabaseCacheHelper helper) {
		super();
		model = new RedmineVersionModel(helper);
	}

	public void setupParameter(int connection, long project){
		connection_id = connection;
		project_id = project;
	}

    @Override
	public boolean isValidParameter(){
		if(connection_id == null || project_id == null)
			return false;
		else
			return true;
	}

	@Override
	protected int getItemViewId() {
		return android.R.layout.simple_list_item_1;

	}

	@Override
	protected void setupView(View view, RedmineProjectVersion data) {
		IMasterRecordForm form;
		if(view.getTag() != null && view.getTag() instanceof IMasterRecordForm){
			form = (IMasterRecordForm)view.getTag();
		} else {
			form = new IMasterRecordForm(view);
		}
		form.setValue(data);
		TextView tv = (TextView) view.findViewById(android.R.id.text1);

		// Set the text color of TextView (ListView Item)
		tv.setTextColor(Color.parseColor("#006ebe"));
		//new VersionListAdapter (this, R.layout.list_view,android.R.id.text1,userList);
	}

	@Override
	protected int getDbCount() throws SQLException {
		return (int) model.countByProject(connection_id,project_id);
	}

	@Override
	protected RedmineProjectVersion getDbItem(int position) throws SQLException {
		return model.fetchItemByProject(connection_id,project_id,(long) position, 1L);
	}

	@Override
	protected long getDbItemId(RedmineProjectVersion item) {
		if(item == null){
			return -1;
		} else {
			return item.getId();
		}
	}

}
