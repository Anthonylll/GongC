package com.gcapp.tc.sd.ui.win_dialog.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcapp.tc.sd.ui.MainActivity;
import com.gcapp.tc.R;

public class NiftyDialogBuilder extends Dialog implements DialogInterface {
	private MainActivity activity;
	private Effectstype type = null;
	private RelativeLayout mRelativeLayoutView;
	private TextView dialog_win_tv;
	private Button dialog_btn_find;
	private int mDuration = -1;
	private static int mOrientation = 1;
	private boolean isCancelable = true;
	private volatile static NiftyDialogBuilder instance;

	public NiftyDialogBuilder(Context context) {
		super(context);
		init(context);
		activity = (MainActivity) context;
	}

	public NiftyDialogBuilder(Context context, int theme) {
		super(context, theme);
		init(context);
		activity = (MainActivity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

	}

	public static NiftyDialogBuilder getInstance(Context context) {

		int ort = context.getResources().getConfiguration().orientation;
		if (mOrientation != ort) {
			mOrientation = ort;
			instance = null;
		}

		if (instance == null) {
			synchronized (NiftyDialogBuilder.class) {
				if (instance == null) {
					instance = new NiftyDialogBuilder(context,
							R.style.dialog_untran);
				}
			}
		}
		return instance;

	}

	private void init(Context context) {
		View mDialogView = View.inflate(context, R.layout.dialog_win, null);
		dialog_win_tv = (TextView) mDialogView.findViewById(R.id.dialog_win_tv);
		dialog_btn_find = (Button) mDialogView
				.findViewById(R.id.dialog_btn_find);
		mRelativeLayoutView = (RelativeLayout) mDialogView
				.findViewById(R.id.mRelativeLayoutView);
		dialog_btn_find.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.toFindWininfo();// 查看中奖详情
				dismiss();
			}
		});
		dialog_win_tv.setText("10000元");
		setContentView(mDialogView);
		this.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				if (type == null) {
					type = Effectstype.Shake;
				}
				start(type);
			}
		});
		mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isCancelable)
					dismiss();
			}
		});
	}

	public NiftyDialogBuilder withEffect(Effectstype type) {
		this.type = type;
		return this;
	}

	public NiftyDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCanceledOnTouchOutside(cancelable);
		return this;
	}

	public NiftyDialogBuilder isCancelable(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCancelable(cancelable);
		return this;
	}

	private void toggleView(View view, Object obj) {
		if (obj == null) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void show() {
		super.show();
	}

	private void start(Effectstype type) {
		BaseEffects animator = type.getAnimator();
		if (mDuration != -1) {
			animator.setDuration(Math.abs(mDuration));
		}
		animator.start(mRelativeLayoutView);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}
}
