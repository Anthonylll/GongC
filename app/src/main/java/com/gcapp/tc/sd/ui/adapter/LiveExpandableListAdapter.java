package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

import com.gcapp.tc.dataaccess.LiveMatch;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author dm
 * @version 5.5.0
 * @Description 比分直播的适配器
 */
public class LiveExpandableListAdapter extends BaseExpandableListAdapter {

	private List<String> grouplist;
	private List<List<LiveMatch>> childlist;
	private Context mContext;
	private String kflag;
//	private LruCache<String, BitmapDrawable> mMemoryCache;
//	private Bitmap hLoadingBitmap;
//	private Bitmap gLoadingBitmap;
//	private Bitmap hBasketBitmap;
//	private Bitmap gBasketBitmap;

	public LiveExpandableListAdapter(Context context, List<String> grouplist,
			List<List<LiveMatch>> childlist, String kflag) {
		super();
		this.mContext = context;
		this.grouplist = grouplist;
		this.childlist = childlist;
		this.kflag = kflag;

		// hLoadingBitmap =
		// BitmapFactory.decodeResource(context.getResources(),R.drawable.host_team_image);
		// gLoadingBitmap =
		// BitmapFactory.decodeResource(context.getResources(),R.drawable.guest_team_image);
//		hBasketBitmap = BitmapFactory.decodeResource(context.getResources(),
//				R.drawable.basketball_host_image);
//		gBasketBitmap = BitmapFactory.decodeResource(context.getResources(),
//				R.drawable.basketball_guest_image);
//		int maxMemory = (int) Runtime.getRuntime().maxMemory();
//		int cacheSize = maxMemory / 8;
//		// 将下载的图片保存着此缓存中
//		mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
//			@SuppressLint("NewApi")
//			@Override
//			protected int sizeOf(String key, BitmapDrawable drawable) {
//				if (drawable.getBitmap() != null) {
//					return drawable.getBitmap().getByteCount();
//				} else {
//					return 0;
//				}
//			}
//		};
	}

	@Override
	public int getGroupCount() {
		return grouplist.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childlist.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return grouplist.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childlist.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder groupViewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.activity_live_group_item, parent, false);
			groupViewHolder = new GroupViewHolder();
			groupViewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.group_text);
			groupViewHolder.group_image = (ImageView) convertView
					.findViewById(R.id.group_image);
			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		groupViewHolder.tvTitle.setText(grouplist.get(groupPosition) + " "
				+ childlist.get(groupPosition).size() + "场比赛");

		if (childlist.get(groupPosition).size() == 0) {
			groupViewHolder.group_image.setVisibility(View.GONE);
		} else {
			groupViewHolder.group_image.setVisibility(View.VISIBLE);
			if (isExpanded) {
				groupViewHolder.group_image
						.setBackgroundResource(R.drawable.win_lottery_detail_up);
			} else {
				groupViewHolder.group_image
						.setBackgroundResource(R.drawable.win_lottery_detail_down);
			}
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder;
		if (convertView == null) {
			if (kflag.equals("foot")) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.activity_live_child_item, parent, false);
			} else {
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.activity_live_basket_child_item,
								parent, false);
			}
			childViewHolder = new ChildViewHolder();
			childViewHolder.child_session_text = (TextView) convertView
					.findViewById(R.id.child_session_text);
			childViewHolder.host_team_text = (TextView) convertView
					.findViewById(R.id.host_team_text);
			childViewHolder.guest_team_text = (TextView) convertView
					.findViewById(R.id.guest_team_text);
			childViewHolder.match_state = (TextView) convertView
					.findViewById(R.id.match_state);
			childViewHolder.host_team_grade = (TextView) convertView
					.findViewById(R.id.host_team_grade);
			childViewHolder.guest_team_grade = (TextView) convertView
					.findViewById(R.id.guest_team_grade);
			childViewHolder.real_match_time = (TextView) convertView
					.findViewById(R.id.real_match_time);
			childViewHolder.half_layout = (LinearLayout) convertView
					.findViewById(R.id.half_layout);
			childViewHolder.vs_text = (TextView) convertView
					.findViewById(R.id.vs_text);
			childViewHolder.grade_text = (TextView) convertView
					.findViewById(R.id.grade_text);
			childViewHolder.host_team_img = (ImageView) convertView
					.findViewById(R.id.host_team_img);
			childViewHolder.guest_team_img = (ImageView) convertView
					.findViewById(R.id.guest_team_img);
			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		LiveMatch liveMatch = childlist.get(groupPosition).get(childPosition);
//		String number = toWeek(liveMatch.getMatchNumber().substring(0, 1),
//				liveMatch.getMatchNumber().substring(1, 4));
		String number = liveMatch.getMatchNumber();
		childViewHolder.child_session_text.setText(number + " "
				+ liveMatch.getMatchOrganization() + " "
				+ liveMatch.getMatchTime().substring(5, 16));
		//篮球展示时为客队VS主队
		childViewHolder.host_team_text.setText(liveMatch.getHostTeam());
		childViewHolder.guest_team_text.setText(liveMatch.getGuestTeam());
		childViewHolder.match_state.setText(liveMatch.getMatchState());
		String hostFirstGrade = liveMatch.getHostFirstGrade();
		String guestFirstGrade = liveMatch.getGuestFirstGrade();
		if (liveMatch.getMatchState().equals("未开始")// 未开赛
				|| liveMatch.getMatchState().equals("改期")
				|| liveMatch.getMatchState().equals("腰斩")
				|| liveMatch.getMatchState().equals("待定")
				|| liveMatch.getMatchState().equals("取消")
				|| liveMatch.getMatchState().equals("中断")) {
			childViewHolder.host_team_grade.setVisibility(View.GONE);
			childViewHolder.guest_team_grade.setVisibility(View.GONE);
			childViewHolder.real_match_time.setVisibility(View.GONE);
			childViewHolder.half_layout.setVisibility(View.GONE);
			childViewHolder.vs_text.setVisibility(View.VISIBLE);
		} else if (liveMatch.getMatchState().equals("完场")) {// 已完场
			childViewHolder.host_team_grade.setVisibility(View.VISIBLE);
			childViewHolder.guest_team_grade.setVisibility(View.VISIBLE);
			childViewHolder.half_layout.setVisibility(View.VISIBLE);
			childViewHolder.real_match_time.setVisibility(View.GONE);
			childViewHolder.vs_text.setVisibility(View.GONE);
			if(hostFirstGrade.contains("-")){
				childViewHolder.grade_text.setText(toHalfScore(hostFirstGrade)
						+ ":" + toHalfScore(guestFirstGrade));
			}else{
				childViewHolder.grade_text.setText(liveMatch.getHostFirstGrade()
						+ ":" + liveMatch.getGuestFirstGrade());
			}
			childViewHolder.host_team_grade.setText(liveMatch.getHostGrade());
			childViewHolder.guest_team_grade.setText(liveMatch.getGuestGrade());
		} else {// 进行中
			childViewHolder.host_team_grade.setVisibility(View.VISIBLE);
			childViewHolder.guest_team_grade.setVisibility(View.VISIBLE);
			childViewHolder.half_layout.setVisibility(View.VISIBLE);
			childViewHolder.real_match_time.setVisibility(View.VISIBLE);
			childViewHolder.vs_text.setVisibility(View.GONE);
			if(hostFirstGrade.contains("-")){
				childViewHolder.grade_text.setText(toHalfScore(hostFirstGrade)
						+ ":" + toHalfScore(guestFirstGrade));
			}else{
				childViewHolder.grade_text.setText(liveMatch.getHostFirstGrade()
						+ ":" + liveMatch.getGuestFirstGrade());
			}
			childViewHolder.host_team_grade.setText(liveMatch.getHostGrade());
			childViewHolder.guest_team_grade.setText(liveMatch.getGuestGrade());
			if(liveMatch.getMatchDate() != null) {
				childViewHolder.real_match_time.setText(liveMatch.getMatchDate()
						+ "'");
			}
		}

//		if (!kflag.equals("foot")) {
//			String hostUrl = liveMatch.getHostTeamImg();
//			String guestUrl = liveMatch.getGuestTeamImg();
//			BitmapDrawable hostDrawable = getBitmapFromMemoryCache(hostUrl);
//			BitmapDrawable guestDrawable = getBitmapFromMemoryCache(guestUrl);
//			if (hostDrawable != null) {
//				// 如果已经加载了就设置图片
//				childViewHolder.host_team_img.setImageDrawable(hostDrawable);
//			} else if (cancelPotentialWork(hostUrl,
//					childViewHolder.host_team_img)) {
//				// 未存在的潜在任务时，就使用启动异步加载
//				BitmapWorkerTask task = new BitmapWorkerTask(
//						childViewHolder.host_team_img);
//				AsyncDrawable asyncDrawable;
//				asyncDrawable = new AsyncDrawable(mContext.getResources(),
//						hBasketBitmap, task);
//				childViewHolder.host_team_img.setImageDrawable(asyncDrawable);
//				task.execute(hostUrl);
//			}
//
//			if (guestDrawable != null) {
//				// 如果已经加载了就设置图片
//				childViewHolder.guest_team_img.setImageDrawable(guestDrawable);
//			} else if (cancelPotentialWork(guestUrl,
//					childViewHolder.guest_team_img)) {
//				// 未存在的潜在任务时，就使用启动异步加载
//				BitmapWorkerTask task = new BitmapWorkerTask(
//						childViewHolder.guest_team_img);
//				AsyncDrawable asyncDrawable;
//				asyncDrawable = new AsyncDrawable(mContext.getResources(),
//						gBasketBitmap, task);
//				childViewHolder.guest_team_img.setImageDrawable(asyncDrawable);
//				task.execute(guestUrl);
//			}
//		}
		return convertView;
	}

	static class GroupViewHolder {
		TextView tvTitle;
		ImageView group_image;
	}

	static class ChildViewHolder {
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
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	/**
	 * 截取并计算篮球半场分数
	 */
	private String toHalfScore(String score) {
		String[] strs = score.split("-");
		if(strs.length >1) {
			int halfScore = Integer.parseInt(strs[0])+Integer.parseInt(strs[1]);
			return halfScore+"";
		}else{
			return strs[0];
		}
	}

	/**
	 * 将数字转换为星期
	 */
//	private String toWeek(String week, String Number) {
//		if (week.equals("1")) {
//			return "周一" + Number;
//		} else if (week.equals("2")) {
//			return "周二" + Number;
//		} else if (week.equals("3")) {
//			return "周三" + Number;
//		} else if (week.equals("4")) {
//			return "周四" + Number;
//		} else if (week.equals("5")) {
//			return "周五" + Number;
//		} else if (week.equals("6")) {
//			return "周六" + Number;
//		} else {
//			return "周日" + Number;
//		}
//	}

	/** 自定义的一个Drawable，让这个Drawable持有BitmapWorkerTask的弱引用。 */
//	class AsyncDrawable extends BitmapDrawable {
//		private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
//
//		public AsyncDrawable(Resources res, Bitmap bitmap,
//				BitmapWorkerTask bitmapWorkerTask) {
//			super(res, bitmap);
//			bitmapWorkerTaskReference = new WeakReference(bitmapWorkerTask);
//		}
//
//		public BitmapWorkerTask getBitmapWorkerTask() {
//			return bitmapWorkerTaskReference.get();
//		}
//	}
//
//	// 获取传入的ImageView它所对应的BitmapWorkerTask。
//	private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
//		if (imageView != null) {
//			Drawable drawable = imageView.getDrawable();
//			if (drawable instanceof AsyncDrawable) {
//				AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
//				return asyncDrawable.getBitmapWorkerTask();
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 取消掉后台的潜在任务，当认为当前ImageView存在着一个另外图片请求任务时 ，则把它取消掉并返回true，否则返回false。
//	 */
//	public boolean cancelPotentialWork(String url, ImageView imageView) {
//		BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
//		if (bitmapWorkerTask != null) {
//			String imageUrl = bitmapWorkerTask.imageUrl;
//			if (imageUrl == null || !imageUrl.equals(url)) {
//				bitmapWorkerTask.cancel(true);
//			} else {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	/**
//	 * 将一张图片存储到LruCache中。
//	 * 
//	 * @param key
//	 *            LruCache的键，这里传入图片的URL地址。
//	 * @param drawable
//	 *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。
//	 */
//	public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
//		if (getBitmapFromMemoryCache(key) == null) {
//			mMemoryCache.put(key, drawable);
//		}
//	}
//
//	/**
//	 * 从LruCache中获取一张图片，如果不存在就返回null。
//	 * 
//	 * @param key
//	 *            LruCache的键，这里传入图片的URL地址。
//	 * @return 对应传入键的BitmapDrawable对象，或者null。
//	 */
//	public BitmapDrawable getBitmapFromMemoryCache(String key) {
//		return mMemoryCache.get(key);
//	}
//
//	/**
//	 * 异步下载图片的任务。
//	 */
//	class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {
//
//		String imageUrl;
//		private WeakReference imageViewReference;
//
//		public BitmapWorkerTask(ImageView imageView) {
//			imageViewReference = new WeakReference(imageView);
//		}
//
//		@Override
//		protected BitmapDrawable doInBackground(String... params) {
//			imageUrl = params[0];
//			// 在后台开始下载图片
//			Bitmap bitmap = downloadBitmap(imageUrl);
//			BitmapDrawable drawable = new BitmapDrawable(
//					mContext.getResources(), bitmap);
//			addBitmapToMemoryCache(imageUrl, drawable);
//			return drawable;
//		}
//
//		@Override
//		protected void onPostExecute(BitmapDrawable drawable) {
//			ImageView imageView = getAttachedImageView();
//			if (imageView != null && drawable != null) {
//				imageView.setImageDrawable(drawable);
//			}
//		}
//
//		/**
//		 * 获取当前BitmapWorkerTask所关联的ImageView。
//		 */
//		private ImageView getAttachedImageView() {
//			ImageView imageView = (ImageView) imageViewReference.get();
//			BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
//			if (this == bitmapWorkerTask) {
//				return imageView;
//			}
//			return null;
//		}
//
//		/**
//		 * 建立HTTP请求，并获取Bitmap对象。
//		 * 
//		 * @param imageUrl
//		 *            图片的URL地址
//		 * @return 解析后的Bitmap对象
//		 */
//		private Bitmap downloadBitmap(String imageUrl) {
//			Bitmap bitmap = null;
//			HttpURLConnection con = null;
//			try {
//				URL url = new URL(imageUrl);
//				con = (HttpURLConnection) url.openConnection();
//				con.setConnectTimeout(5 * 1000);
//				con.setReadTimeout(10 * 1000);
//				bitmap = BitmapFactory.decodeStream(con.getInputStream());
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (con != null) {
//					con.disconnect();
//				}
//			}
//			return bitmap;
//		}
//
//	}

}
