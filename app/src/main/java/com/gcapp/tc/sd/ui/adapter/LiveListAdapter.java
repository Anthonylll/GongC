package com.gcapp.tc.sd.ui.adapter;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.gcapp.tc.dataaccess.LiveMatch;
import com.gcapp.tc.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author dm
 * @version 5.5.0
 * @Description
 */
@SuppressLint("NewApi") public class LiveListAdapter extends BaseAdapter {

	private int resourceId;
	private Context mContext = null;
	private List<LiveMatch> mlist = null;
	private LruCache<String, BitmapDrawable> mMemoryCache;
//	private Bitmap hLoadingBitmap;
//	private Bitmap gLoadingBitmap;
	private Bitmap hBasketBitmap;
	private Bitmap gBasketBitmap;

	public LiveListAdapter(Context context, int textViewResourceId,
			List<LiveMatch> list) {
		this.mContext = context;
		this.resourceId = textViewResourceId;
		this.mlist = list;

		hBasketBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.basketball_host_image);
		gBasketBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.basketball_guest_image);
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		// 将下载的图片保存着此缓存中
		mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, BitmapDrawable drawable) {
				if (drawable.getBitmap() != null) {
					return drawable.getBitmap().getByteCount();
				} else {
					return 0;
				}
			}
		};
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LiveMatch liveMatch = (LiveMatch) getItem(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(resourceId,
					null);
			viewHolder.child_session_text = (TextView) convertView
					.findViewById(R.id.child_session_text);
			viewHolder.host_team_text = (TextView) convertView
					.findViewById(R.id.host_team_text);
			viewHolder.guest_team_text = (TextView) convertView
					.findViewById(R.id.guest_team_text);
			viewHolder.match_state = (TextView) convertView
					.findViewById(R.id.match_state);
			viewHolder.host_team_grade = (TextView) convertView
					.findViewById(R.id.host_team_grade);
			viewHolder.guest_team_grade = (TextView) convertView
					.findViewById(R.id.guest_team_grade);
			viewHolder.real_match_time = (TextView) convertView
					.findViewById(R.id.real_match_time);
			viewHolder.half_layout = (LinearLayout) convertView
					.findViewById(R.id.half_layout);
			viewHolder.vs_text = (TextView) convertView
					.findViewById(R.id.vs_text);
			viewHolder.grade_text = (TextView) convertView
					.findViewById(R.id.grade_text);
			viewHolder.host_team_img = (ImageView) convertView
					.findViewById(R.id.host_team_img);
			viewHolder.guest_team_img = (ImageView) convertView
					.findViewById(R.id.guest_team_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (liveMatch != null) {
			String number = liveMatch.getMatchNumber();
			viewHolder.child_session_text.setText(number + " "
					+ liveMatch.getMatchOrganization() + " "
					+ liveMatch.getMatchTime().substring(5, 16));
			// 篮球展示时为客队VS主队
			viewHolder.host_team_text.setText(liveMatch.getHostTeam());
			viewHolder.guest_team_text.setText(liveMatch.getGuestTeam());
			viewHolder.match_state.setText(liveMatch.getMatchState());
			String hostFirstGrade = liveMatch.getHostFirstGrade();
			String guestFirstGrade = liveMatch.getGuestFirstGrade();
			if (liveMatch.getMatchState().equals("未开始")// 未开赛
					|| liveMatch.getMatchState().equals("改期")
					|| liveMatch.getMatchState().equals("腰斩")
					|| liveMatch.getMatchState().equals("待定")
					|| liveMatch.getMatchState().equals("取消")
					|| liveMatch.getMatchState().equals("中断")) {
				viewHolder.host_team_grade.setVisibility(View.GONE);
				viewHolder.guest_team_grade.setVisibility(View.GONE);
				viewHolder.real_match_time.setVisibility(View.GONE);
				viewHolder.half_layout.setVisibility(View.GONE);
				viewHolder.vs_text.setVisibility(View.VISIBLE);
			} else if (liveMatch.getMatchState().equals("完场")) {// 已完场
				viewHolder.host_team_grade.setVisibility(View.VISIBLE);
				viewHolder.guest_team_grade.setVisibility(View.VISIBLE);
				viewHolder.half_layout.setVisibility(View.VISIBLE);
				viewHolder.real_match_time.setVisibility(View.GONE);
				viewHolder.vs_text.setVisibility(View.GONE);
				if (hostFirstGrade.contains("-")) {
					viewHolder.grade_text.setText(toHalfScore(hostFirstGrade)
							+ ":" + toHalfScore(guestFirstGrade));
				} else {
					viewHolder.grade_text.setText(liveMatch.getHostFirstGrade()
							+ ":" + liveMatch.getGuestFirstGrade());
				}
				viewHolder.host_team_grade.setText(liveMatch.getHostGrade());
				viewHolder.guest_team_grade.setText(liveMatch.getGuestGrade());
			} else {// 进行中
				viewHolder.host_team_grade.setVisibility(View.VISIBLE);
				viewHolder.guest_team_grade.setVisibility(View.VISIBLE);
				viewHolder.half_layout.setVisibility(View.VISIBLE);
				viewHolder.real_match_time.setVisibility(View.VISIBLE);
				viewHolder.vs_text.setVisibility(View.GONE);
				if (hostFirstGrade.contains("-")) {
					viewHolder.grade_text.setText(toHalfScore(hostFirstGrade)
							+ ":" + toHalfScore(guestFirstGrade));
				} else {
					viewHolder.grade_text.setText(liveMatch.getHostFirstGrade()
							+ ":" + liveMatch.getGuestFirstGrade());
				}
				viewHolder.host_team_grade.setText(liveMatch.getHostGrade());
				viewHolder.guest_team_grade.setText(liveMatch.getGuestGrade());
				if (liveMatch.getMatchDate() != null) {
					viewHolder.real_match_time.setText(liveMatch.getMatchDate()
							+ "'");
				}
			}
		}

		String hostUrl = liveMatch.getHostTeamImg();
//		String hostUrl = "http://116.62.66.117/ClientSoft/teamsignnew_6563.png";
		String guestUrl = liveMatch.getGuestTeamImg();
		Log.d("zhang","hostUrl = "+hostUrl);
		Log.d("zhang","guestUrl = "+guestUrl);
		BitmapDrawable hostDrawable = getBitmapFromMemoryCache(hostUrl);
		BitmapDrawable guestDrawable = getBitmapFromMemoryCache(guestUrl);
		if (hostDrawable != null) {
			// 如果已经加载了就设置图片
			viewHolder.host_team_img.setImageDrawable(hostDrawable);
		} else if (cancelPotentialWork(hostUrl, viewHolder.host_team_img)) {
			// 未存在的潜在任务时，就使用启动异步加载
			BitmapWorkerTask task = new BitmapWorkerTask(
					viewHolder.host_team_img);
			AsyncDrawable asyncDrawable;
			asyncDrawable = new AsyncDrawable(mContext.getResources(),
					hBasketBitmap, task);
			viewHolder.host_team_img.setImageDrawable(asyncDrawable);
			task.execute(hostUrl);
		}

		if (guestDrawable != null) {
			// 如果已经加载了就设置图片
			viewHolder.guest_team_img.setImageDrawable(guestDrawable);
		} else if (cancelPotentialWork(guestUrl, viewHolder.guest_team_img)) {
			// 未存在的潜在任务时，就使用启动异步加载
			BitmapWorkerTask task = new BitmapWorkerTask(				
					viewHolder.guest_team_img);
			AsyncDrawable asyncDrawable;
			asyncDrawable = new AsyncDrawable(mContext.getResources(),
					gBasketBitmap, task);
			viewHolder.guest_team_img.setImageDrawable(asyncDrawable);
			task.execute(guestUrl);
		}

		return convertView;
	}

	class ViewHolder {
		TextView child_session_text;
		TextView host_team_text;
		TextView guest_team_text;
		TextView match_state;
		TextView host_team_grade;
		TextView guest_team_grade;
		TextView real_match_time;
		LinearLayout half_layout;
		TextView grade_text;
		TextView vs_text;
		ImageView host_team_img;
		ImageView guest_team_img;
	}

	@Override
	public int getCount() {
		return (mlist == null ? 0 : mlist.size());
	}

	@Override
	public Object getItem(int position) {
		return (mlist == null ? null : mlist.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 截取并计算篮球半场分数
	 */
	private String toHalfScore(String score) {
		String[] strs = score.split("-");
		if (strs.length > 1) {
			int halfScore = Integer.parseInt(strs[0])
					+ Integer.parseInt(strs[1]);
			return halfScore + "";
		} else {
			return strs[0];
		}
	}
	
	/** 自定义的一个Drawable，让这个Drawable持有BitmapWorkerTask的弱引用。 */
	class AsyncDrawable extends BitmapDrawable {
		private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference(bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	// 获取传入的ImageView它所对应的BitmapWorkerTask。
	private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * 取消掉后台的潜在任务，当认为当前ImageView存在着一个另外图片请求任务时 ，则把它取消掉并返回true，否则返回false。
	 */
	public boolean cancelPotentialWork(String url, ImageView imageView) {
		BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null) {
			String imageUrl = bitmapWorkerTask.imageUrl;
			if (imageUrl == null || !imageUrl.equals(url)) {
				bitmapWorkerTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param drawable
	 *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。
	 */
	public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, drawable);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的BitmapDrawable对象，或者null。
	 */
	public BitmapDrawable getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}

	/**
	 * 异步下载图片的任务。
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

		String imageUrl;
		private WeakReference imageViewReference;

		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference(imageView);
		}

		@Override
		protected BitmapDrawable doInBackground(String... params) {
			imageUrl = params[0];
			// 在后台开始下载图片
			Bitmap bitmap = downloadBitmap(imageUrl);
			BitmapDrawable drawable = new BitmapDrawable(
					mContext.getResources(), bitmap);
			addBitmapToMemoryCache(imageUrl, drawable);
			return drawable;
		}

		@Override
		protected void onPostExecute(BitmapDrawable drawable) {
			ImageView imageView = getAttachedImageView();
			if (imageView != null && drawable != null) {
				imageView.setImageDrawable(drawable);
			}
		}

		/**
		 * 获取当前BitmapWorkerTask所关联的ImageView。
		 */
		private ImageView getAttachedImageView() {
			ImageView imageView = (ImageView) imageViewReference.get();
			BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
			if (this == bitmapWorkerTask) {
				return imageView;
			}
			return null;
		}

		/**
		 * 建立HTTP请求，并获取Bitmap对象。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 解析后的Bitmap对象
		 */
		private Bitmap downloadBitmap(String imageUrl) {
			Bitmap bitmap = null;
			HttpURLConnection con = null;
			try {
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(10 * 1000);
				bitmap = BitmapFactory.decodeStream(con.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
			return bitmap;
		}

	}

}
