package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

import com.gcapp.tc.dataaccess.OddsModel;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author anthony
 * @date 2018-5-23 上午10:56:42
 * @version 5.5.0 
 * @Description 赔率列表adapter
 */
public class OddsListAdapter extends BaseAdapter{

	private int resourceId;
    private Context mContext = null;
    private List<OddsModel> mlist = null;
    private String opt;
    private String flag;
	
	public OddsListAdapter(Context context, int textViewResourceId,List<OddsModel> list,String opt,String oddsflag) {
		this.mContext = context;
        this.resourceId = textViewResourceId;
        this.mlist = list;
        this.opt = opt;
        this.flag = oddsflag;
	}

	@Override
	public View getView(int position,View convertView,ViewGroup parent) {
		OddsModel item = (OddsModel) getItem(position);
		ViewHolder viewHolder = null;
		if(convertView == null) {
			 viewHolder = new ViewHolder ();
			 convertView = LayoutInflater.from (mContext).inflate (resourceId, null);
			 viewHolder.odds_company_name = (TextView) convertView.findViewById(R.id.odds_company_name);
			 viewHolder.odds_init_win = (TextView) convertView.findViewById(R.id.odds_init_win);
			 viewHolder.odds_init_flat = (TextView) convertView.findViewById(R.id.odds_init_flat);
			 viewHolder.odds_init_lose = (TextView) convertView.findViewById(R.id.odds_init_lose);
			 viewHolder.odds_stant_win = (TextView) convertView.findViewById(R.id.odds_stant_win);
			 viewHolder.odds_stant_flat = (TextView) convertView.findViewById(R.id.odds_stant_flat);
			 viewHolder.odds_stant_lose = (TextView) convertView.findViewById(R.id.odds_stant_lose);
			 viewHolder.instant_layout = (LinearLayout) convertView.findViewById(R.id.instant_layout);
			 convertView.setTag (viewHolder);
		} else {
			 viewHolder = (ViewHolder) convertView.getTag ();
		 }
		if (item != null) {
			viewHolder.odds_company_name.setText(item.getOddsCompany());
			
			//主队即时赔率变化设置
			SetFlat(viewHolder.odds_stant_win,item.getOddsInstantWin(),item.gethChange());
//			if(item.gethChange().equals("up")) {
//				viewHolder.odds_stant_win.setTextColor(mContext.getResources().getColor(R.color.red));
//				viewHolder.odds_stant_win.setText(item.getOddsInstantWin()+" ↑");
//			}else if(item.gethChange().equals("down")){
//				viewHolder.odds_stant_win.setTextColor(mContext.getResources().getColor(R.color.green));
//				viewHolder.odds_stant_win.setText(item.getOddsInstantWin()+" ↓");
//			}else{
//				viewHolder.odds_stant_win.setTextColor(mContext.getResources().getColor(R.color.gray));
//				viewHolder.odds_stant_win.setText(item.getOddsInstantWin());
//			}
			//客队即时赔率变化设置
			SetFlat(viewHolder.odds_stant_lose,item.getOddsInstantLose(),item.getgChange());
//			if(item.getgChange().equals("up")) {
//				viewHolder.odds_stant_lose.setTextColor(mContext.getResources().getColor(R.color.red));
//				viewHolder.odds_stant_lose.setText(item.getOddsInstantLose()+" ↑");
//			}else if(item.getgChange().equals("down")){
//				viewHolder.odds_stant_lose.setTextColor(mContext.getResources().getColor(R.color.green));
//				viewHolder.odds_stant_lose.setText(item.getOddsInstantLose()+" ↓");
//			}else{
//				viewHolder.odds_stant_lose.setTextColor(mContext.getResources().getColor(R.color.gray));
//				viewHolder.odds_stant_lose.setText(item.getOddsInstantLose());
//			}
			
			viewHolder.odds_init_win.setText(item.getOddsInitialWin());
			viewHolder.odds_init_lose.setText(item.getOddsInitialLose());
			if(opt.equals("3004") && flag.equals("0")) {
				viewHolder.odds_init_flat.setVisibility(View.GONE);
				viewHolder.odds_stant_flat.setVisibility(View.GONE);
				viewHolder.instant_layout.setVisibility(View.GONE);
			}else if(opt.equals("3004") && flag.equals("1")) {
				viewHolder.odds_init_flat.setVisibility(View.VISIBLE);
				viewHolder.odds_stant_flat.setVisibility(View.VISIBLE);
				viewHolder.instant_layout.setVisibility(View.GONE);
				viewHolder.odds_init_flat.setText(item.getOddsInitialFlat());
				SetFlat(viewHolder.odds_stant_flat,item.getOddsInstantFlat(),item.getfChange());
			}else{
				viewHolder.odds_init_flat.setVisibility(View.VISIBLE);
				viewHolder.odds_stant_flat.setVisibility(View.VISIBLE);
				viewHolder.instant_layout.setVisibility(View.VISIBLE);
				viewHolder.odds_init_flat.setText(item.getOddsInitialFlat());
				SetFlat(viewHolder.odds_stant_flat,item.getOddsInstantFlat(),item.getfChange());
			}
		}
		return convertView;
	}
	
	private void SetFlat(TextView view ,String flat, String change){
		if(change.equals("1")) {
			view.setTextColor(mContext.getResources().getColor(R.color.red));
			view.setText(flat+" ↑");
		}else if(change.equals("-1")){
			view.setTextColor(mContext.getResources().getColor(R.color.green));
			view.setText(flat+" ↓");
		}else{
			view.setTextColor(mContext.getResources().getColor(R.color.gray));
			view.setText(flat);
		}
	}
	
	class ViewHolder {
        TextView odds_company_name;
        TextView odds_init_win;
        TextView odds_init_flat;
        TextView odds_init_lose;
        TextView odds_stant_win;
        TextView odds_stant_flat;
        TextView odds_stant_lose;
        LinearLayout instant_layout;
	}
	
	@Override
	public int getCount() {
		return (mlist == null ? 0 : mlist.size ());
	}

	@Override
	public Object getItem(int position) {
		return (mlist == null ? null : mlist.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
}
