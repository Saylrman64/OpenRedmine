package jp.redmine.gttnl.adapter.form;

import android.view.View;
import android.widget.TextView;

import jp.redmine.gttnl.entity.RedmineWiki;
import jp.redmine.gttnl.form.helper.FormHelper;

public class WikiForm extends FormHelper {
	public TextView textSubject;
	public WikiForm(View activity){
		this.setup(activity);
	}


	public void setup(View view){
		textSubject = (TextView)view.findViewById(android.R.id.text1);
	}


	public void setValue(RedmineWiki rd){
		textSubject.setText(rd.getTitle());

	}

}

