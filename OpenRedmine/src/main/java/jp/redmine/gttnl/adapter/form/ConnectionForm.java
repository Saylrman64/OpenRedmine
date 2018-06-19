package jp.redmine.gttnl.adapter.form;

import jp.redmine.gttnl.entity.RedmineConnection;
import jp.redmine.gttnl.form.helper.FormHelper;
import android.view.View;
import android.widget.TextView;

public class ConnectionForm extends FormHelper {
	public TextView textSubject;
	public ConnectionForm(View activity){
		this.setup(activity);
	}


	public void setup(View view){
		textSubject = (TextView)view.findViewById(android.R.id.text1);
	}


	public void setValue(RedmineConnection rd){
		textSubject.setText(rd.getName());

	}

}

