package com.apkupdater.adapter;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.apkupdater.model.InstalledApp;
import com.apkupdater.updater.UpdaterOptions;
import com.apkupdater.util.InstalledAppUtil;
import com.apkupdater.view.InstalledAppView;
import com.apkupdater.view.InstalledAppView_;

import java.util.List;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class InstalledAppAdapter
	extends RecyclerView.Adapter<InstalledAppAdapter.InstalledAppViewHolder>
	implements View.OnLongClickListener
{
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private List<InstalledApp> mApps;
	private Context mContext;
	private RecyclerView mView;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public class InstalledAppViewHolder
		extends RecyclerView.ViewHolder
	{
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		private InstalledAppView mView;

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		InstalledAppViewHolder(
			InstalledAppView view
		) {
			super(view);
			mView = view;
		}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		public void bind(
			InstalledApp app
		) {
			mView.bind(app);
		}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public InstalledAppAdapter(
		Context context,
		RecyclerView view,
		List<InstalledApp> apps
	) {
		mContext = context;
		mView = view;
		mApps = InstalledAppUtil.sort(mContext, apps);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public InstalledAppViewHolder onCreateViewHolder(
		ViewGroup parent,
		int viewType
	) {
		InstalledAppView v = InstalledAppView_.build(mContext);
		v.setLayoutParams(new RecyclerView.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
		));
		v.setOnLongClickListener(this);
		return new InstalledAppViewHolder(v);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean onLongClick(
		View view
	) {
		// Get the ignore list from the options
		UpdaterOptions options = new UpdaterOptions(mContext);
		List<String> ignore_list = options.getIgnoreList();

		InstalledApp app = mApps.get(mView.getChildLayoutPosition(view));

		// If it's on the ignore remove, otherwise add it
		if (ignore_list.contains(app.getPname())) {
			ignore_list.remove(app.getPname());
		} else {
			ignore_list.add(app.getPname());
		}

		// Update the list
		options.setIgnoreList(ignore_list);

		// Sort it
		mApps = InstalledAppUtil.sort(mContext, mApps);

		notifyDataSetChanged();

		return true;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onBindViewHolder(
		InstalledAppViewHolder holder,
		int position
	) {
		holder.bind(mApps.get(position));
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int getItemCount(
	) {
		return mApps.size();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////