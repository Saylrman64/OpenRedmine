package jp.redmine.gttnl.activity.handler;

public interface ConnectionActionInterface {
	public void onConnectionSelected(int connectionid);
	public void onConnectionEdit(int connectionid);
	public void onConnectionAdd();
	public void onConnectionSaved();
}