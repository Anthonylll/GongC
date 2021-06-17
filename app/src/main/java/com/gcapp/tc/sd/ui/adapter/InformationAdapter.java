package com.gcapp.tc.sd.ui.adapter;

import java.util.List;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.Information;
import com.gcapp.tc.utils.ImageLoader;
import com.gcapp.tc.R;

/**
 * 彩票资讯Adapter
 * 
 * @author SLS003
 */
public class InformationAdapter extends BaseAdapter {
	
	private Context context;
	// 装图片的集合
	private List<Information> list;
	public ImageLoader imageLoader;

	public InformationAdapter(Context context, List<Information> list) {
		this.context = context;
		this.list = list;
		imageLoader = new ImageLoader(context.getApplicationContext());
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.information_items, null);
			// 得到控件
			holder.txtNewType = (TextView) view.findViewById(R.id.info_newType);
			holder.txtNewTitle = (TextView) view
					.findViewById(R.id.info_newTitle);
			holder.img_info = (ImageView) view.findViewById(R.id.img_info);// 图片

			holder.txtDateTime = (TextView) view
					.findViewById(R.id.info_newDateTime);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Information information = list.get(position);
		// 给控件赋值
		if (information != null) {
			holder.txtNewType.setText(information.getNewType());

			imageLoader.DisplayImage(information.getImg_url(), holder.img_info);// 加载图片

			if (information.getColor().equals("red")) {
				holder.txtNewTitle.setText(Html
						.fromHtml("<font color='#e3393c'>"
								+ information.getTitle().trim() + "</font>"));
			} else if (information.getColor().equals("black")) {
				holder.txtNewTitle.setText(Html
						.fromHtml("<font color='#000000'>"
								+ information.getTitle().trim() + "</font>"));
			} else {
				holder.txtNewTitle.setText(information.getTitle());
			}
			holder.txtDateTime.setText(information.getDateTime());
		}
		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		TextView txtNewType;
		TextView txtNewTitle;
		TextView txtDateTime;
		ImageView img_info;
	}

}
