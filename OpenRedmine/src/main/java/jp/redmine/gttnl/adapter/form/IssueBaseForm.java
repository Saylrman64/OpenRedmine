package jp.redmine.gttnl.adapter.form;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.entity.RedmineIssue;
import jp.redmine.gttnl.form.helper.FormHelper;

abstract class IssueBaseForm extends FormHelper {
	public TextView textStatus;
	public TextView textAssignedTo;
	public TextView textTracker;
	public TextView textPriority;
	public TextView textDateFrom;
	public TextView textDateTo;
	public TextView textVersion;
	public TextView textModified;
	public ProgressBar progressBar;
	public IssueBaseForm(View activity){
		this.setup(activity);
	}


	public void setup(View view){
		textStatus = (TextView)view.findViewById(R.id.textStatus);
		textAssignedTo = (TextView)view.findViewById(R.id.textAssignedTo);
		progressBar = (ProgressBar)view.findViewById(R.id.progressissue);
		textTracker = (TextView)view.findViewById(R.id.textTracker);
		textPriority = (TextView)view.findViewById(R.id.textPriority);
		textDateFrom = (TextView)view.findViewById(R.id.textDateFrom);
		textDateTo = (TextView)view.findViewById(R.id.textDateTo);
		textVersion = (TextView)view.findViewById(R.id.textVersion);
		textModified = (TextView)view.findViewById(R.id.textModified);
	}


	public void setValue(RedmineIssue rd){
		setDate(textDateFrom,rd.getDateStart());
		setDate(textDateTo,rd.getDateDue());
		setDateTime(textModified, rd.getModified());
		progressBar.setMax(100);
		progressBar.setProgress(rd.getProgressRate());
		setMasterName(textAssignedTo, rd.getAssigned());
		setMasterName(textStatus, rd.getStatus());
		setMasterName(textTracker, rd.getTracker());
		setMasterName(textStatus,rd.getStatus());
		setMasterName(textVersion,rd.getVersion());
		setMasterName(textPriority,rd.getPriority());
		setProgress(rd.getProgressRate(),rd.getDoneRate());

	}

	public void setProgress(Short progress,Short donerate){
		progressBar.setMax(100);
		progressBar.setProgress(progress == null ? 0 : progress);
		progressBar.setSecondaryProgress(donerate == null ? 0 : donerate);
	}
}

