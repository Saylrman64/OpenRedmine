package jp.redmine.gttnl.adapter.form;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.entity.RedmineAttachment;
import jp.redmine.gttnl.form.helper.FormHelper;
import android.view.View;
import android.widget.TextView;

public class AttachmentForm extends FormHelper {
	public TextView textSubject;
	public TextView textSize;
	public TextView textCreated;
	protected String[] sizes = new String[]{"","k","M","G","E"};
	public AttachmentForm(View activity){
		this.setup(activity);
	}


	public void setup(View view){
		textSubject = (TextView)view.findViewById(R.id.textSubject);
		textSize = (TextView)view.findViewById(R.id.textSize);
		textCreated = (TextView)view.findViewById(R.id.textCreated);

	}
	public void setValue(RedmineAttachment rd){
		textSubject.setText(rd.getFilename());
		textSize.setText(getSizeString(rd.getFilesize()));
		setDate(textCreated, rd.getCreated());
	}
	
	protected String getSizeString(Integer size){
		if(size == null)
			return "";
		BigDecimal export = new BigDecimal(size);
		BigDecimal base = new BigDecimal(1024);
		String exportname = "";
		NumberFormat df = NumberFormat.getIntegerInstance();
		export.setScale(1, RoundingMode.HALF_UP);
		for(String sizename : sizes){
			exportname = sizename;
			if(export.longValue() < base.longValue())
				break;
			export = export.divide(base);
		}
		return  df.format(export) + exportname;
	}

}

