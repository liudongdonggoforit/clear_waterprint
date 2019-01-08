package com.cwp.video;


import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cwp.video.activities.BaseActivity;
import com.cwp.video.adapters.IconAdapter;
import com.cwp.video.adapters.VideoAdapter;
import com.cwp.video.models.Video;
import com.cwp.video.network.DownloadQueue;
import com.cwp.video.network.Downloader;
import com.cwp.video.network.FileStorage;
import com.cwp.video.resolve.AnalyzerTask;
import com.cwp.video.resolve.Validator;
import com.cwp.video.views.VideoViewHolder;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import org.jsoup.helper.DataUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity implements View.OnClickListener,DownloadQueue.QueueListener {

    private RecyclerView rv_provider;
    private RecyclerView rv_videos;
    private IconAdapter iconAdapter;
    private EditText et_resolve;
    private Button bt_resolve;
    protected DownloadQueue downloadQueue = new DownloadQueue();

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        downloadQueue.setQueueListener(this);
        rv_provider = findViewById(R.id.main_rv_provide);
        et_resolve = findViewById(R.id.main_et_url);
        bt_resolve = findViewById(R.id.main_bt_resolve);
        iconAdapter = new IconAdapter(MainActivity.this);
        rv_provider.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        rv_provider.setAdapter(iconAdapter);

        rv_videos = findViewById(R.id.main_rv_videos);
        rv_videos.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv_videos.setHasFixedSize(true);
        rv_videos.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                NiceVideoPlayer niceVideoPlayer = ((VideoViewHolder) holder).mVideoPlayer;
                if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                }
            }
        });
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
                            String url = "https://aweme.snssdk.com/aweme/v1/play/?video_id=" + video.getId() + "&line=0&ratio=720p&media_type=4&vr_type=0&test_cdn=None&improve_bitrate=0";
                            video.setUrl(url);

                            Downloader videoDownloader = new Downloader(video.getUrl()).setFileAsDCIM(FileStorage.TYPE.VIDEO, "video-"+ video.getId() + ".mp4");

                            if (!videoDownloader.getFile().exists()) {
                                downloadQueue.add("video-" + video.getId(), videoDownloader);
                            }

                            try{
                                downloadQueue.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            List<Video> mVideoList = new ArrayList<>();
                            mVideoList.add(0, video);
                            VideoAdapter adapter = new VideoAdapter(MainActivity.this, mVideoList);
                            rv_videos.setAdapter(adapter);
                            rv_videos.setVisibility(View.VISIBLE);
                            rv_provider.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        Log.i("onBackPressed","onBackPressed");
        if (NiceVideoPlayerManager.instance().onBackPressd())
            return;
    }

    @Override
    public void onQueueDownloaded(DownloadQueue downloadQueue, ArrayList<String> accidentHashes) {

    }

    @Override
    public void onQueueProgress(DownloadQueue downloadQueue, long loaded, long total) {

    }

    @Override
    public void onDownloaded(String hash, Downloader downloader) {

    }

    @Override
    public void onDownloadProgress(String hash, Downloader downloader, long loaded, long total) {

    }

    @Override
    public void onDownloadCanceled(String hash, Downloader downloader) {

    }

    @Override
    public void onDownloadError(String hash, Downloader downloader, Exception e) {

    }
}
