package jp.redmine.gttnl.adapter.form;

import android.view.View;
import android.widget.TextView;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.activity.handler.WebviewActionInterface;
import jp.redmine.gttnl.entity.RedmineNews;
import jp.redmine.gttnl.form.helper.FormHelper;
import jp.redmine.gttnl.form.helper.TextViewHelper;

public class NewsForm extends FormHelper {
	public TextView textView;
	public TextView textSubject;
	public TextViewHelper textViewHelper;
	public NewsForm(View activity){
		this.setup(activity);
	}


	public void setup(View view){
		textView = (TextView)view.findViewById(R.id.textView);
		textSubject = (TextView)view.findViewById(R.id.textSubject);
	}

	public void setupWebView(WebviewActionInterface act){
		textViewHelper = new TextViewHelper();
		textViewHelper.setup(textView);
		textViewHelper.setAction(act);
	}

	public void setValue(RedmineNews jr){
		if (textViewHelper != null)
			textViewHelper.setContent(textView, jr.getConnectionId(), jr.getProject().getId(), jr.getDescription());
		textSubject.setText(jr.getTitle());
	}

}

