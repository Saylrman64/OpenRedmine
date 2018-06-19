package jp.redmine.gttnl.url;

import android.net.Uri;

public class RemoteUrlStatus extends RemoteUrl {

	@Override
	public Uri.Builder getUrl(String baseurl) {
		Uri.Builder url = convertUrl(baseurl);
		url.appendEncodedPath("issue_statuses." + getExtension());
		return url;
	}
}
