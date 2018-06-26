package jp.redmine.gttnl.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.util.List;

import jp.redmine.gttnl.parser.BaseParser;
import jp.redmine.gttnl.url.RemoteUrl;

public abstract class SelectDataTask<T,P> extends AsyncTask<P, Integer, T> {
	/**
	 * Notify error request on UI thread
	 * @param statuscode http response code
	 */
	abstract protected void onErrorRequest(int statuscode);
	/**
	 * Notify progress on UI thread
	 * @param max total count of the items
	 * @param proc current count of the items
	 */
	abstract protected void onProgress(int max,int proc);

	/**
	 * Store the last exception (reference by UI thread)
	 */
	private volatile Exception lasterror;

	interface ProgressKind{
		public int progress = 1;
		public int error = 2;
		public int unknown = 3;
	}

	@Override
	protected final void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		switch(values[0]){
		case ProgressKind.progress:
			onProgress(values[1],values[2]);
			break;
		case ProgressKind.error:
			onErrorRequest(values[1]);
			break;
		case ProgressKind.unknown:
			onError(lasterror);
			break;
		default:
		}
	}
	protected void onError(Exception lasterror){
		Log.e("SelectDataTask", "background", lasterror);
	}

	protected void notifyProgress(int max,int proc){
		super.publishProgress(ProgressKind.progress,max,proc);
	}

	protected void publishErrorRequest(int status){
		super.publishProgress(ProgressKind.error,status);
	}
	protected void publishError(Exception e){
		lasterror = e;
		super.publishProgress(ProgressKind.unknown);
	}

	protected void helperAddItems(ArrayAdapter<T> listAdapter,List<T> items){
		if(items == null)
			return;
		listAdapter.notifyDataSetInvalidated();
		for (T i : items){
			listAdapter.add(i);
		}
		listAdapter.notifyDataSetChanged();
	}

	protected void helperSetupParserStream(InputStream stream,BaseParser<?,?> parser) throws XmlPullParserException{
		Fetcher.setupParserStream(stream, parser);
	}

	protected boolean fetchData(SelectDataTaskRedmineConnectionHandler connectionhandler, RemoteUrl url,SelectDataTaskDataHandler handler){
		return Fetcher.fetchData(connectionhandler, getErrorHandler(), connectionhandler.getUrl(url), handler);
	}
	protected boolean fetchData(SelectDataTaskConnectionHandler connectionhandler,String url,SelectDataTaskDataHandler handler){
		return Fetcher.fetchData(connectionhandler, getErrorHandler(), url, handler);
	}
	protected boolean putData(SelectDataTaskRedmineConnectionHandler connectionhandler,RemoteUrl url,SelectDataTaskDataHandler handler, SelectDataTaskPutHandler puthandler){
		return Fetcher.putData(connectionhandler, getErrorHandler(), connectionhandler.getUrl(url), handler, puthandler);
	}
	protected boolean postData(SelectDataTaskRedmineConnectionHandler connectionhandler,RemoteUrl url,SelectDataTaskDataHandler handler, SelectDataTaskPutHandler puthandler){
		return Fetcher.postData(connectionhandler, getErrorHandler(), connectionhandler.getUrl(url), handler, puthandler);
	}

	protected Fetcher.ContentResponseErrorHandler getErrorHandler(){
		return new Fetcher.ContentResponseErrorHandler() {
			@Override
			public void onErrorRequest(int status) {
				publishErrorRequest(status);
			}

			@Override
			public void onError(Exception e) {
				publishError(e);
			}
		};
	}
}
