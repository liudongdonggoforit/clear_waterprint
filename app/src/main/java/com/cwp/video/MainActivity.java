package com.cwp.video;


import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cwp.video.activities.BaseActivity;
import com.cwp.video.adapters.IconAdapter;
import com.cwp.video.models.Video;
import com.cwp.video.resolve.AnalyzerTask;
import com.cwp.video.resolve.Validator;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rv_provider;
    private IconAdapter iconAdapter;
    private EditText et_resolve;
    private Button bt_resolve;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        rv_provider = findViewById(R.id.main_rv_provide);
        et_resolve = findViewById(R.id.main_et_url);
        bt_resolve = findViewById(R.id.main_bt_resolve);
        iconAdapter = new IconAdapter(MainActivity.this);
        rv_provider.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        rv_provider.setAdapter(iconAdapter);
        bt_resolve.setOnClickListener(this);
        et_resolve.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = et_resolve.getCompoundDrawables()[2];
                if (drawable == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et_resolve.getWidth()
                        - et_resolve.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    et_resolve.setText("");
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_bt_resolve:
                String str = et_resolve.getText().toString();
                if (str.isEmpty() || !Validator.containsUrl(str)) {
                    Toast.makeText(MainActivity.this, R.string.no_url, Toast.LENGTH_SHORT).show();
                } else {
                    AnalyzerTask analyzerTask = new AnalyzerTask(MainActivity.this, new AnalyzerTask.AnalyzeListener() {
                        @Override
                        public void onAnalyzed(Video video) {
                            Log.i("resolve", video.getUrl());
                        }

                        @Override
                        public void onAnalyzeCanceled() {

                        }

                        @Override
                        public void onAnalyzeError(Exception e) {

                        }
                    });
                    analyzerTask.execute(str);
//                    et_resolve.setText("");
                }
                break;
        }
    }
}
