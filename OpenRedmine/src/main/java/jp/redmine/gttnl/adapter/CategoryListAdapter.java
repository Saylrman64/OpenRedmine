package jp.redmine.gttnl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.entity.RedmineProjectCategory;
import jp.redmine.gttnl.adapter.form.IMasterRecordForm;

public class CategoryListAdapter extends RedmineDaoAdapter<RedmineProjectCategory, Long, DatabaseCacheHelper> {
	protected Integer connection_id;
	protected Long project_id;
	public CategoryListAdapter(DatabaseCacheHelper helper, Context context) {
		super(helper, context, RedmineProjectCategory.class);
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
	protected void setupView(View view, RedmineProjectCategory data) {
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
	}

	@Override
	protected QueryBuilder<RedmineProjectCategory, Long> getQueryBuilder() throws SQLException {
		QueryBuilder<RedmineProjectCategory, Long> builder = dao.queryBuilder();
		builder
				.orderBy(RedmineProjectCategory.NAME, true)
				.where()
				.eq(RedmineProjectCategory.CONNECTION, connection_id)
				.and()
				.eq(RedmineProjectCategory.PROJECT_ID, project_id)
		;
		return builder;
	}

	@Override
	protected long getDbItemId(RedmineProjectCategory item) {
		if(item == null){
			return -1;
		} else {
			return item.getId();
		}
	}

}
