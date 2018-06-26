package jp.redmine.gttnl.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteFragment;

import java.sql.SQLException;

import jp.redmine.gttnl.R;
import jp.redmine.gttnl.db.cache.DatabaseCacheHelper;
import jp.redmine.gttnl.db.cache.RedmineAttachmentModel;
import jp.redmine.gttnl.entity.RedmineAttachment;
import jp.redmine.gttnl.entity.RedmineConnection;
import jp.redmine.gttnl.fragment.form.DownloadForm;
import jp.redmine.gttnl.model.ConnectionModel;
import jp.redmine.gttnl.param.AttachmentArgument;
import jp.redmine.gttnl.provider.Attachment;

public class FileDownload extends OrmLiteFragment<DatabaseCacheHelper> {
	static private final String TAG = FileDownload.class.getSimpleName();
	private static final int PERMISSION_CALLBACK_CONSTANT = 101;
	private static final int REQUEST_PERMISSION_SETTING = 102;
	private SharedPreferences permissionStatus;
	private boolean sentToSettings = false;
	private DownloadForm form;


	public FileDownload(){
		super();
	}

	static public FileDownload newInstance(AttachmentArgument intent){
		FileDownload instance = new FileDownload();
		instance.setArguments(intent.getArgument());
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.page_download, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		permissionStatus = getActivity().getSharedPreferences("permissionStatus",getActivity().MODE_PRIVATE);
		form = new DownloadForm(getView());
		form.buttonDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AttachmentArgument instance = new AttachmentArgument();
				instance.setArgument(getArguments());
				RedmineAttachmentModel modelAttachemnt = new RedmineAttachmentModel(getHelper());
				try {
					RedmineAttachment attachment = modelAttachemnt.fetchById(instance.getConnectionId(), instance.getAttachmentId());

					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri uri = Attachment.getUrl(attachment.getId());
					intent.setData(uri);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					getActivity().startActivity(intent);

				} catch (ActivityNotFoundException e) {
					Toast.makeText(getActivity(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
				} catch (SQLException e) {
					Log.e(TAG,"onClick",e);
				}

			}
		});
		form.buttonBrowser.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@TargetApi(Build.VERSION_CODES.GINGERBREAD)
			@Override
			public void onClick(View v) {
				if(ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
					if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
						//Show Information about why you need the permission
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("Need Storage Permission");
						builder.setMessage("This app needs phone permission.");
						builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CALLBACK_CONSTANT);
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						builder.show();
					} else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
						//Previously Permission Request was cancelled with 'Dont Ask Again',
						// Redirect to Settings after showing Information about why you need the permission
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("Need Storage Permission");
						builder.setMessage("This app needs storage permission.");
						builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								sentToSettings = true;
								Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
								intent.setData(uri);
								startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
								Toast.makeText(getActivity(), "Go to Permissions to Grant Phone", Toast.LENGTH_LONG).show();
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						builder.show();
					}  else {
						//just request the permission
						requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CALLBACK_CONSTANT);
					}


					SharedPreferences.Editor editor = permissionStatus.edit();
					editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
					editor.commit();
				} else {
					//You already have the permission, just go ahead.
					proceedAfterPermission();
				}
			}

		});
	}



	@Override
	public void onStart() {
		super.onStart();

		AttachmentArgument intent = new AttachmentArgument();
		intent.setArgument(getArguments());
		int connectionid = intent.getConnectionId();

		RedmineAttachmentModel model = new RedmineAttachmentModel(getHelper());
		RedmineAttachment attachment = new RedmineAttachment();
		try {
			attachment = model.fetchById(connectionid, intent.getAttachmentId());
		} catch (SQLException e) {
			Log.e(TAG,"onStart",e);
		}

		form.setValue(attachment);
	}


	private void proceedAfterPermission() {

		Toast.makeText(getActivity(), "We got All Permissions", Toast.LENGTH_LONG).show();
		AttachmentArgument instance = new AttachmentArgument();
		instance.setArgument(getArguments());
		RedmineAttachmentModel modelAttachemnt = new RedmineAttachmentModel(getHelper());
		try {
			RedmineAttachment attachment = modelAttachemnt.fetchById(instance.getConnectionId(), instance.getAttachmentId());
			RedmineConnection connection = ConnectionModel.getItem(getActivity(), attachment.getConnectionId());

			Uri uri = Uri.parse(attachment.getContentUrl());
			DownloadManager.Request r = new DownloadManager.Request(uri);
			r.setTitle(connection.getName() + " - " + attachment.getFilename());
			r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, attachment.getFilename());
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				r.allowScanningByMediaScanner();
				r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			}
			if (!TextUtils.isEmpty(connection.getToken()))
				r.addRequestHeader("X-Redmine-API-Key",connection.getToken());
			if (connection.isAuth()) {
				String auth = connection.getAuthId() + ":" + connection.getAuthPasswd();
				String base64 = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
				r.addRequestHeader("Authorization", "Basic " + base64);
			}

			DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
			dm.enqueue(r);

		} catch (SQLException e) {
			Log.e(TAG,"onClick",e);
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == PERMISSION_CALLBACK_CONSTANT){
			//check if all permissions are granted
			boolean allgranted = false;
			for(int i=0;i<grantResults.length;i++){
				if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
					allgranted = true;
				} else {
					allgranted = false;
					break;
				}
			}

			if(allgranted){
				proceedAfterPermission();
			} else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Need Storage Permission");
				builder.setMessage("This app needs phone permission.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CALLBACK_CONSTANT);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
			} else {
				Toast.makeText(getActivity(),"Unable to get Permission",Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_PERMISSION_SETTING) {
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				//Got Permission
				proceedAfterPermission();
			}
		}
	}


	@Override
	public void onResume() {
		super.onResume();

		if (sentToSettings) {
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				//Got Permission
				proceedAfterPermission();

			}
		}
	}

}

