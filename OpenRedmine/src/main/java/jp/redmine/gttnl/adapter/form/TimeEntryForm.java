package jp.redmine.gttnl.adapter.form;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.entity.RedmineTimeEntry;
import jp.redmine.gttnl.form.helper.FormHelper;
import android.view.View;
import android.widget.TextView;

public class TimeEntryForm extends FormHelper {
	public TextView textAuthor;
	public TextView textActivity;
	public TextView textSpentsOn;
	public TextView textTimeEntry;
	public TimeEntryForm(View activity){
		this.setup(activity);
	}


	public void setup(View view){
		textAuthor = (TextView)view.findViewById(R.id.textAuthor);
		textActivity = (TextView)view.findViewById(R.id.textActivity);
		textSpentsOn = (TextView)view.findViewById(R.id.textSpentsOn);
		textTimeEntry = (TextView)view.findViewById(R.id.textTimeEntry);
	}


	public void setValue(RedmineTimeEntry rd){
		setUserName(textAuthor, rd.getUser());
		setMasterName(textActivity, rd.getActivity());
		setDate(textSpentsOn,rd.getSpentsOn());
		setTime(textTimeEntry,R.string.ticket_time_estimate,rd.getHours().doubleValue());

	}


}

