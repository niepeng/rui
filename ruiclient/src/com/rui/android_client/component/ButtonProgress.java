package com.rui.android_client.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.rui.android_client.R;

public class ButtonProgress extends RelativeLayout {

	private Context mContext;

	private ButtonProgress mView;
	private Button mButton;
	private ProgressBar mProgressBar;

	private int mButtonColor;
	private float mButtonTextSize;
	private int mButtonTextColor;
	private String mButtonText;

	private Drawable mProgressDrawable;
	private int mProgressMax;

	private boolean isProgressing = false;

	private OnButtonClickListener mButtonClickListener;

	public interface OnButtonClickListener {
		public void onStart(View v);

		public void onStop(View v);
	}

	public void setOnButtonClickListener(OnButtonClickListener l) {
		mButtonClickListener = l;
	}

	public void updateProgress(int progress) {
		if (!isProgressing) {
			return;
		}
		mProgressBar.setProgress(progress);
	}

	public void setProgressMax(int max) {
		mProgressBar.setMax(max);
	}

	public void setText(String text) {
		mButton.setText(text);
	}

	public ButtonProgress(Context context) {
		this(context, null);
	}

	public ButtonProgress(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ButtonProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttributes(attrs, defStyle);
		init(context);
	}

	private void initAttributes(AttributeSet attrs, int defStyle) {
		if (attrs == null) {
			return;
		}

		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.ButtonProgress, defStyle, 0);
		mButtonColor = a.getColor(R.styleable.ButtonProgress_button_color,
				Color.TRANSPARENT);
		mButtonTextSize = a.getDimension(
				R.styleable.ButtonProgress_button_text_size, 14);
		mButtonTextColor = a.getColor(
				R.styleable.ButtonProgress_button_text_color, Color.WHITE);
		mButtonText = a.getString(R.styleable.ButtonProgress_button_text);
		mProgressDrawable = a
				.getDrawable(R.styleable.ButtonProgress_progress_bar_drawable);
		mProgressMax = a.getInteger(R.styleable.ButtonProgress_progress_max,
				100);
		a.recycle();
	}

	private void init(Context context) {
		this.mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.layout_button_progress,
				this);
		mView = this;
		mButton = (Button) findViewById(R.id.button);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);

		initViewContent();
		initListener();
	}

	private void initViewContent() {
		setButtonViewContent();
		setProgressBarViewContent();
	}

	private void setButtonViewContent() {
		mButton.setBackgroundColor(mButtonColor);
		mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonTextSize);
		mButton.setTextColor(mButtonTextColor);
		mButton.setText(mButtonText);
	}

	private void setProgressBarViewContent() {
		mProgressBar.setProgressDrawable(mProgressDrawable);
		mProgressBar.setMax(mProgressMax);
	}

	private void initListener() {
		setClickable(true);
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isProgressing = !isProgressing;
				if (isProgressing) {
					if (mButtonClickListener != null)
						mButtonClickListener.onStart(mView);
				} else {
					mProgressBar.setProgress(0);
					if (mButtonClickListener != null)
						mButtonClickListener.onStop(mView);
				}
			}
		});
	}

}
