package jp.redmine.gttnl.adapter.form;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.entity.RedmineProject;
import jp.redmine.gttnl.form.helper.FormHelper;

public class ProjectForm extends FormHelper {
	public TextView textSubject;
    public CheckBox ratingBar;
	public ProjectForm(View activity){
		this.setup(activity);
	}


	public void setup(View view){
		textSubject = (TextView)view.findViewById(R.id.textSubject);
		ratingBar = (CheckBox)view.findViewById(R.id.checkStar);
		ratingBar.setFocusable(false);
	}


	public void setValue(RedmineProject rd){
		performSetEnabled(textSubject, rd.getStatus().isUpdateable());
		textSubject.setText(rd.getName());
		ratingBar.setChecked(rd.getFavorite() > 0);

	}
	public void getValue(RedmineProject rd){
		rd.setFavorite(ratingBar.isChecked() ? 1 : 0 );
	}

}

