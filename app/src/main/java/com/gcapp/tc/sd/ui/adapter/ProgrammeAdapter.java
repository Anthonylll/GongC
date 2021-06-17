package com.gcapp.tc.sd.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.sd.ui.FollowInfoActivity;
import com.gcapp.tc.sd.ui.FollowInfo_jc_Activity;
import com.gcapp.tc.sd.ui.GreatManActivity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.ImageManager;
import com.gcapp.tc.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 合买大厅方案adapter
 */
public class ProgrammeAdapter extends BaseAdapter{

	private Context mContext;
	private List<Schemes> programmelist = new ArrayList<Schemes>();
	private ViewHolder viewHolder = null;
	
	public ProgrammeAdapter(Context context, List<Schemes> list) {
		super();
		this.mContext = context;
		this.programmelist = list;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.follow_buy_item,null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.programme_user_name);
			viewHolder.content = (TextView) convertView.findViewById(R.id.programme_content);
			viewHolder.lottery_name = (TextView) convertView.findViewById(R.id.lottery_name);
			viewHolder.total_money = (TextView) convertView.findViewById(R.id.total_money);
			viewHolder.single_money = (TextView) convertView.findViewById(R.id.single_money);
			viewHolder.end_time = (TextView) convertView.findViewById(R.id.end_time);
			viewHolder.programme_user_odds = (TextView) convertView.findViewById(R.id.programme_user_odds);
			viewHolder.follow_btn = (Button) convertView.findViewById(R.id.follow_btn);
			viewHolder.programme_layout = (LinearLayout) convertView.findViewById(R.id.programme_layout);
			viewHolder.programme_user_img = (ImageView) convertView.findViewById(R.id.programme_user_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Schemes schemes = programmelist.get(position);
		viewHolder.name.setText(schemes.getInitiateName());
		viewHolder.programme_user_odds.setText(String.valueOf(schemes.getGreatmanOdds())+"%");
		viewHolder.content.setText(schemes.getDescription());
		viewHolder.lottery_name.setText(schemes.getLotteryName());
		viewHolder.total_money.setText(String.valueOf(schemes.getSelfmoney()));
		viewHolder.single_money.setText(String.valueOf(schemes.getShareMoney()));
		viewHolder.end_time.setText("截止："+schemes.getEndTime());
		
		String url = schemes.getInitiateImg();
		if(!url.equals(AppTools.url) && url != null && !url.equals("")) {
			ImageManager.getInstance().displayImage(
					url, viewHolder.programme_user_img,
					ImageManager.getViewsHeadOptions());
		}else{
			viewHolder.programme_user_img.setImageResource(R.drawable.center_circle_imageview);
		}
		
		viewHolder.follow_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				programmeDetail(position);
			}
		});
		viewHolder.programme_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				programmeDetail(position);
			}
		});
		viewHolder.content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				programmeDetail(position);
			}
		});
		viewHolder.programme_user_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = schemes.getInitiateUserID();
				String name = schemes.getInitiateName();
				toManProgramme(name,id);
			}
		});
		viewHolder.name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = schemes.getInitiateUserID();
				String name = schemes.getInitiateName();
				toManProgramme(name,id);
			}
		});
		
		return convertView;
	}
	
	class ViewHolder {
		public TextView name;
		public TextView content;
		public TextView lottery_name;
		public TextView total_money;
		public TextView programme_user_odds;
		public TextView single_money;
		public TextView end_time;
		public Button follow_btn;
		public LinearLayout programme_layout;
		private ImageView programme_user_img;
	}
	
	@Override
	public int getCount() {
		return (programmelist != null) ? programmelist.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return programmelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * 跳转至牛人方案列表
	 */
	private void toManProgramme(String name,String id) {
		Intent intent = new Intent(mContext,GreatManActivity.class);
		intent.putExtra("manID",id);
		intent.putExtra("greatmanName", name);
		mContext.startActivity(intent);
	}
	
	/**
	 * 跳转至跟单详情页
	 */
	private void programmeDetail(int position) {
		Intent intents;
		if (programmelist.size() != 0) {
			Schemes schemes = programmelist.get(position);
			if ("73".equals(schemes.getLotteryID())
					|| "72".equals(schemes.getLotteryID())
					|| "45".equals(schemes.getLotteryID())) {
				intents = new Intent(mContext, FollowInfo_jc_Activity.class);
			} else {
				intents = new Intent(mContext, FollowInfoActivity.class);
			}
			Bundle bundle = new Bundle();
			bundle.putSerializable("schem", schemes);
			intents.putExtra("bundle", bundle);
			mContext.startActivity(intents);
		}
	}
}
