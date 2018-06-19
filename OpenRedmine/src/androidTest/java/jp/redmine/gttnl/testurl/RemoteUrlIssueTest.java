package jp.redmine.gttnl.testurl;

import android.test.AndroidTestCase;

import jp.redmine.gttnl.url.RemoteUrl.requests;
import jp.redmine.gttnl.url.RemoteUrlIssue;


public class RemoteUrlIssueTest extends AndroidTestCase {

	private final String testurl = "http://test/";
	public RemoteUrlIssueTest() {
	}

	public void testRaw(){
		RemoteUrlIssue issue = new RemoteUrlIssue();
		issue.setupRequest(requests.xml);
		assertEquals(testurl + "issues.xml",
				issue.getUrl(testurl).toString());
	}
}
