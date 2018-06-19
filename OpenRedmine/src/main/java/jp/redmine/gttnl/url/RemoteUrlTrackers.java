package jp.redmine.gttnl.url;

import android.net.Uri;

public class RemoteUrlTrackers extends RemoteUrl {

	@Override
	public Uri.Builder getUrl(String baseurl) {
		Uri.Builder url = convertUrl(baseurl);
		url.appendEncodedPath("trackers." + getExtension());
		return url;
	}
}
