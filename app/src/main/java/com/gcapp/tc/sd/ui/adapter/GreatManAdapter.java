package com.gcapp.tc.sd.ui.adapter;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gcapp.tc.dataaccess.GreatMan;
import com.gcapp.tc.sd.ui.GreatManActivity;
import com.jauker.widget.BadgeView;
import com.gcapp.tc.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 大神列表adapter
 */
public class GreatManAdapter extends BaseAdapter {

	private Context context;
	private List<GreatMan> greatmanlist = new ArrayList<GreatMan>();
	private Bitmap mLoadingBitmap;
	private ViewHolder viewHolder = null;
	private LruCache<String, BitmapDrawable> mMemoryCache;

	public GreatManAdapter(Context context, List<GreatMan> list) {
		super();
		this.context = context;
		this.greatmanlist = list;
		mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.center_circle_imageview);
		int maxMemory = (int) Runtime.getRuntime().maxMemory(); 
		int cacheSize = maxMemory / 8; 
		// 将下载的图片保存着此缓存中 
		mMemoryCache = new LruCache< String, BitmapDrawable>(cacheSize) { 
			@SuppressLint("NewApi") @Override 
			protected int sizeOf(String key, BitmapDrawable drawable) {
				if(drawable.getBitmap() != null){
					return drawable.getBitmap().getByteCount();
				}else{
					return 0;
				}
			}
		};
	}

	@Override
	public int getCount() {
		return (greatmanlist != null) ? greatmanlist.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return greatmanlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.great_man_item, null);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.great_man_img);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.great_man_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		GreatMan greatman = greatmanlist.get(position);
		viewHolder.title.setText(greatman.getName());
		if(!greatman.getOrderCount().equals("0")) {
			BadgeView badgeView = new BadgeView(context);
			badgeView.setTargetView(viewHolder.image);
			badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
			badgeView.setBadgeCount(Integer.parseInt(greatman.getOrderCount()));
		}
		
		loadingPicture(greatman);
		initListener(greatman);
		return convertView;
	}
	
	/**
	 * 加载头像
	 */
	private void loadingPicture(GreatMan greatman) {
		String url = greatman.getImg();
		BitmapDrawable drawable = getBitmapFromMemoryCache(url); 
		if (drawable != null) { 
			//如果已经加载了就设置图片 
			viewHolder.image.setImageDrawable(drawable); 
		} else if (cancelPotentialWork(url, viewHolder.image)) {
			//未存在的潜在任务时，就使用启动异步加载 
			BitmapWorkerTask task = new BitmapWorkerTask(viewHolder.image); 
			AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), mLoadingBitmap, task); 
			viewHolder.image.setImageDrawable(asyncDrawable); 
			task.execute(url); 
		}
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener(final GreatMan greatman) {
		viewHolder.title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = greatman.getManID();
				String name = greatman.getName();
				toManProgramme(name,id);
			}
		});
		viewHolder.image.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				String id = greatman.getManID();
				String name = greatman.getName();
				toManProgramme(name,id);
			}
		});
	}
	
	/**
	 * 跳转至牛人方案列表
	 */
	private void toManProgramme(String name,String id) {
		Intent intent = new Intent(context,GreatManActivity.class);
		intent.putExtra("manID",id);
		intent.putExtra("greatmanName", name);
		context.startActivity(intent);
	}

	/**自定义的一个Drawable，让这个Drawable持有BitmapWorkerTask的弱引用。*/ 
	class AsyncDrawable extends BitmapDrawable { 
		private WeakReference< BitmapWorkerTask > bitmapWorkerTaskReference; 
		public AsyncDrawable(Resources res, Bitmap bitmap, 
				BitmapWorkerTask bitmapWorkerTask) { 
			super(res, bitmap); 
			bitmapWorkerTaskReference = new WeakReference(bitmapWorkerTask); 
		} 
		
		public BitmapWorkerTask getBitmapWorkerTask() { 
			return bitmapWorkerTaskReference.get(); 
		} 
	} 
	
	//获取传入的ImageView它所对应的BitmapWorkerTask。 
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

	取消掉后台的潜在任务，当认为当前ImageView存在着一个另外图片请求任务时
	，则把它取消掉并返回true，否则返回false。 
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
	将一张图片存储到LruCache中。
	@param key
	LruCache的键，这里传入图片的URL地址。
	@param drawable
	LruCache的值，这里传入从网络上下载的BitmapDrawable对象。 
	*/ 
	public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) { 
		if (getBitmapFromMemoryCache(key) == null) { 
			mMemoryCache.put(key, drawable); 
		} 
	}
	
	/**
	从LruCache中获取一张图片，如果不存在就返回null。
	@param key
	LruCache的键，这里传入图片的URL地址。
	@return 对应传入键的BitmapDrawable对象，或者null。 
	*/ 
	public BitmapDrawable getBitmapFromMemoryCache(String key) { 
		return mMemoryCache.get(key); 
	}
	
	/**
	异步下载图片的任务。
	*/ 
	class BitmapWorkerTask extends AsyncTask< String, Void, BitmapDrawable> {

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
			BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap); 
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
	获取当前BitmapWorkerTask所关联的ImageView。 
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
	建立HTTP请求，并获取Bitmap对象。
	@param imageUrl
	图片的URL地址
	@return 解析后的Bitmap对象 
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

	class ViewHolder {
		public TextView title;
		public ImageView image;
	}

}
