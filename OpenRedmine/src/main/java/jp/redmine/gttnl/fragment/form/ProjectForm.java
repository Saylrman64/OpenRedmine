package jp.redmine.gttnl.fragment.form;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.activity.handler.WebviewActionInterface;
import jp.redmine.gttnl.entity.RedmineConnection;
import jp.redmine.gttnl.entity.RedmineProject;
import jp.redmine.gttnl.form.helper.FormHelper;
import jp.redmine.gttnl.form.helper.TextViewHelper;
import jp.redmine.gttnl.form.helper.WebViewHelper;

public class ProjectForm extends FormHelper {
	public TextView textProject;
	public TextView textStatus;
	public TextView textHomepage;
	public TextView textCreated;
	public TextView textModified;
	public TextViewHelper textViewHelper = new TextViewHelper();
	private WebViewHelper webViewHelper = new WebViewHelper();
	private WebView webView;
	public ProjectForm(View activity){
		this.setup(activity);
		this.setupEvents();
	}

	public void setupWebView(WebviewActionInterface act){
		textViewHelper.setup(textHomepage);
		textViewHelper.setAction(act);
		webViewHelper.setup(webView);
		webViewHelper.setAction(act);
	}

	public void setup(View view){
		textProject = (TextView)view.findViewById(R.id.textProject);
		textStatus = (TextView)view.findViewById(R.id.textStatus);
		textHomepage = (TextView)view.findViewById(R.id.textHomepage);
		textCreated = (TextView)view.findViewById(R.id.textCreated);
		textModified = (TextView)view.findViewById(R.id.textModified);
		webView = (WebView) view.findViewById(R.id.webView);
	}

	public void setupEvents(){

	}

	public void setValue(RedmineConnection con, RedmineProject rd){
		setMasterName(textProject, rd);
		textStatus.setText(textStatus.getContext().getString(rd.getStatus().getResourceId()));
		textViewHelper.setContent(textHomepage, rd.getConnectionId(), rd.getId(), nvl(rd.getHomepage()));
		webViewHelper.setContent(webView, con.getWikiType(), rd.getConnectionId(), rd.getId(), nvl(rd.getDescription()));
		setDateTime(textCreated, rd.getCreated());
		setDateTime(textModified, rd.getModified());
	}

	protected String nvl(String input){
		if(TextUtils.isEmpty(input))
			return "";
		return input;
	}


}

