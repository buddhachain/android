package com.chain.buddha.ui.activity.xiuxing;

import android.os.Bundle;
import android.widget.TextView;

import com.chain.buddha.R;
import com.chain.buddha.ui.BaseActivity;

import butterknife.BindView;

/**
 * 排行榜
 */
public class BookReaderActivity extends BaseActivity {

    @BindView(R.id.text_back)
    TextView mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        mTitle.setText("梵音");

    }

}
