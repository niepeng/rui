package com.rui.android_client.component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rui.android_client.R;

public class TextViewWithLabelLayout extends RelativeLayout {

	private Context mContext;
	private String mLabelText, mContentText;
	private int mLabelColor = Color.BLACK, mContentColor = Color.BLACK;
	private float mLabelFontSize = 14, mContentFontSize = 14;
	private OnLayoutClickListener mClickListener;

	private RelativeLayout layoutView;
	private RelativeLayout clickableLayout;
	private TextView labelView, contentView;

	public interface OnLayoutClickListener {
		public void click(View v);
	}

	public TextViewWithLabelLayout(Context context) {
		super(context);
		init(context);
	}

	public TextViewWithLabelLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttrs(attrs);
		init(context);
	}

	public TextViewWithLabelLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initAttrs(attrs);
		init(context);
	}

	public void setLabelText(String text) {
		labelView.setText(text);
	}

	public void setLabelColor(int color) {
		labelView.setTextColor(color);
	}

	public void setLabelTextSize(float size) {
		if (size > 0) {
			labelView.setTextSize(size);
		}
	}

	public void setContentText(String text) {
		contentView.setText(text);
	}

	public void setContentColor(int color) {
		contentView.setTextColor(color);
	}

	public void setContentColors(ColorStateList colors) {
		contentView.setTextColor(colors);
	}

	public void setContentTextSize(float size) {
		if (size > 0) {
			contentView.setTextSize(size);
		}
	}

	public void setOnLayoutClickListener(OnLayoutClickListener clickListener) {
		mClickListener = clickListener;
	}

	@Override
	public void setBackgroundResource(int resid) {
		clickableLayout.setBackgroundResource(resid);
	}

	public void setLeftDrawable(Drawable image) {
		contentView.setCompoundDrawablesWithIntrinsicBounds(image, null, null,
				null);
	}

	public void setRightDrawable(Drawable image) {
		contentView.setCompoundDrawablesWithIntrinsicBounds(null, null, image,
				null);
	}

	private void initAttrs(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.TextViewWithLabel, 0, 0);
			if (a.hasValue(R.styleable.TextViewWithLabel_label)) {
				mLabelText = a.getString(R.styleable.TextViewWithLabel_label);
			}
			if (a.hasValue(R.styleable.TextViewWithLabel_label_color)) {
				mLabelColor = a.getColor(
						R.styleable.TextViewWithLabel_label_color, Color.BLACK);
			}
			if (a.hasValue(R.styleable.TextViewWithLabel_label_text_size)) {
				mLabelFontSize = a.getDimension(
						R.styleable.TextViewWithLabel_label_text_size, 14);
			}
			if (a.hasValue(R.styleable.TextViewWithLabel_content)) {
				mContentText = a
						.getString(R.styleable.TextViewWithLabel_content);
			}
			if (a.hasValue(R.styleable.TextViewWithLabel_content_color)) {
				mContentColor = a.getColor(
						R.styleable.TextViewWithLabel_content_color,
						Color.BLACK);
			}
			if (a.hasValue(R.styleable.TextViewWithLabel_content_text_size)) {
				mContentFontSize = a.getDimension(
						R.styleable.TextViewWithLabel_content_text_size, 14);
			}
			a.recycle();
		}
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater.from(mContext).inflate(
				R.layout.layout_textview_with_label, this);
		layoutView = this;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutView.setLayoutParams(params);
		initView();
		setViewContent();
		registerListner();
	}

	private void initView() {
		clickableLayout = (RelativeLayout) layoutView
				.findViewById(R.id.clickable_layout);
		labelView = (TextView) layoutView.findViewById(R.id.label);
		contentView = (TextView) layoutView.findViewById(R.id.content);

		setLabelColor(mLabelColor);
		setLabelTextSize(mLabelFontSize);

		setContentColor(mContentColor);
		setContentTextSize(mContentFontSize);
	}

	private void setViewContent() {
		setLabelText(mLabelText);
		setContentText(mContentText);
	}

	private void registerListner() {
		clickableLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!layoutView.isEnabled()) {
					return;
				}
				if (mClickListener != null) {
					mClickListener.click(v);
				}
			}
		});
	}

}
