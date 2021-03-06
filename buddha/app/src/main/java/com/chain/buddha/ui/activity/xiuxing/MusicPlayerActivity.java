//package com.chain.buddha.ui.activity.xiuxing;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.provider.Settings;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//
//import com.android.imusic.music.bean.AudioInfo;
//import com.android.imusic.music.bean.MusicParams;
//import com.android.imusic.music.dialog.QuireDialog;
//import com.chain.buddha.R;
//import com.chain.buddha.ui.BaseActivity;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.music.player.lib.manager.MusicSubjectObservable;
//import com.music.player.lib.manager.MusicWindowManager;
//import com.music.player.lib.manager.SqlLiteCacheManager;
//import com.android.imusic.music.model.MusicLrcRowParserEngin;
//import com.android.imusic.music.utils.MediaUtils;
//import com.music.player.lib.bean.BaseAudioInfo;
//import com.music.player.lib.bean.MusicLrcRow;
//import com.music.player.lib.bean.MusicStatus;
//import com.music.player.lib.constants.MusicConstants;
//import com.music.player.lib.listener.MusicJukeBoxStatusListener;
//import com.music.player.lib.listener.MusicOnItemClickListener;
//import com.music.player.lib.listener.MusicPlayerEventListener;
//import com.music.player.lib.manager.MusicPlayerManager;
//import com.music.player.lib.util.Logger;
//import com.music.player.lib.util.MusicClickControler;
//import com.music.player.lib.util.MusicUtils;
//import com.music.player.lib.view.MusicJukeBoxBackgroundLayout;
//import com.music.player.lib.view.MusicJukeBoxView;
//import com.music.player.lib.view.dialog.MusicAlarmSettingDialog;
//import com.music.player.lib.view.dialog.MusicPlayerListDialog;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Observable;
//import java.util.Observer;
//
///**
// * TinyHung@Outlook.com
// * 2019/3/6
// * Audio Player
// * ?????????????????????
// * ???????????????????????????????????????
// * MusicPlayerService??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
// * MusicPlayerActivity??????????????????????????????????????????????????????????????????????????????????????????????????????????????????MusicPlayerService??????????????????
// * MusicPlayerManager??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
// * MusicJukeBoxView??????????????????
// * MusicJukeBoxBackgroundLayout??????????????????UI?????????????????????
// * MusicJukeBoxCoverPager????????????????????????
// * MusicAlarmSettingDialog???????????????????????????
// * MusicPlayerListDialog????????????????????????????????????
// */
//
//public class MusicPlayerActivity extends BaseActivity implements
//        MusicJukeBoxStatusListener, MusicPlayerEventListener, Observer {
//
//    private static final String TAG = "MusicPlayerActivity";
//    private MusicJukeBoxView mMusicJukeBoxView;
//    private SeekBar mSeekBar;
//    private MusicJukeBoxBackgroundLayout mRootLayout;
//    private ImageView mMusicBtnPlayPause,mMusicPlayerModel,mBtnCollect;
//    private TextView mViewTitle,mTotalTime,mCurrentTime,mMusicAlarm,mSubTitle;
//    private Handler mHandler;
//    private MusicClickControler mClickControler;
//    private boolean isVisibility=false;
//    private boolean isTouchSeekBar=false;//????????????????????????seekBar
//    private MusicLrcRowParserEngin mParserEngin;
//
//    @SuppressLint("WrongViewCast")
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //??????????????????
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//        setContentView(R.layout.music_activity_player);
//        initViews();
//        //??????????????????????????????
//        MusicPlayerManager.getInstance().addOnPlayerEventListener(this);
//        //????????????????????????????????????????????????
//        MusicPlayerManager.getInstance().addObservable(this);
//        mHandler=new Handler(Looper.getMainLooper());
//        //???????????????????????????????????????????????????n?????????
//        mClickControler=new MusicClickControler();
//        mClickControler.init(1,600);
//        getIntentParams(getIntent(),true);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        getIntentParams(intent,false);
//
//    }
//
//    public Handler getHandler() {
//        if(null==mHandler){
//            mHandler=new Handler(Looper.getMainLooper());
//        }
//        return mHandler;
//    }
//
//    /**
//     * ????????????
//     * @param intent
//     * @param isOnCreate ??????????????????
//     */
//    private void getIntentParams(Intent intent,boolean isOnCreate) {
//        //Music??????
//        long musicID = intent.getLongExtra(MusicConstants.KEY_MUSIC_ID,0);
//        if(musicID<=0){
//            finish();
//            return;
//        }
//
//        //?????????????????????
//        BaseAudioInfo currentPlayerMusic = MusicPlayerManager.getInstance().getCurrentPlayerMusic();
//        //????????????????????????
//        if(!isOnCreate&&null!=currentPlayerMusic&&currentPlayerMusic.getAudioId()==musicID){
//            return;
//        }
//        MusicWindowManager.getInstance().onInvisible();
//        MusicPlayerManager.getInstance().onCheckedPlayerConfig();//?????????????????????
//        String musicList = intent.getStringExtra(MusicConstants.KEY_MUSIC_LIST);
//        if(!TextUtils.isEmpty(musicList)){
//            MusicParams params= new Gson().fromJson(musicList,new TypeToken< MusicParams>(){}.getType());
//            if(null!=params&&null!=params.getAudioInfos()){
//                final List<AudioInfo> thisMusicLists=new ArrayList<>();
//                thisMusicLists.addAll(params.getAudioInfos());
//                final int index=MusicUtils.getInstance().getCurrentPlayIndex(thisMusicLists,musicID);
//                if(null!=currentPlayerMusic&&currentPlayerMusic.getAudioId()==musicID&&
//                        MusicPlayerManager.getInstance().getPlayerState()==MusicConstants.MUSIC_PLAYER_PLAYING){
//                    //???????????????????????????
//                    MusicPlayerManager.getInstance().updateMusicPlayerData(thisMusicLists,index);
//                    onStatusResume(musicID);
//                }else{
//                    MusicPlayerManager.getInstance().onReset();
//                    if(null!=mSeekBar){
//                        mSeekBar.setSecondaryProgress(0);
//                        mSeekBar.setProgress(0);
//                    }
//                    //?????????????????????????????????onOffsetPosition??????????????????
//                    MusicPlayerManager.getInstance().updateMusicPlayerData(thisMusicLists,index);
//                    mMusicJukeBoxView.setNewData(thisMusicLists,index,true);
//                }
//            }else{
//                if(null!=currentPlayerMusic){
//                    onStatusResume(musicID);
//                }
//            }
//        }else{
//            if(null!=currentPlayerMusic){
//                onStatusResume(musicID);
//            }
//        }
//    }
//
//    /**
//     * ????????????
//     * @param musicID
//     */
//    private void onStatusResume(long musicID) {
//        List<BaseAudioInfo> currentPlayList = (List<BaseAudioInfo>) MusicPlayerManager.getInstance().getCurrentPlayList();
//        int currentPlayIndex = MusicUtils.getInstance().getCurrentPlayIndex(currentPlayList, musicID);
//        mMusicJukeBoxView.setNewData(currentPlayList,currentPlayIndex,false);
//        isVisibility=true;
//        //???????????????????????????????????????????????????
//        MusicPlayerManager.getInstance().onCheckedCurrentPlayTask();
//    }
//
//    /**
//     * ???????????????
//     */
//    @SuppressLint("WrongViewCast")
//    private void initViews() {
//        View.OnClickListener onClickListener=new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    //????????????
//                    case R.id.music_btn_model:
//                        MusicPlayerManager.getInstance().changedPlayerPlayModel();
//                        break;
//                    //?????????
//                    case R.id.music_btn_last:
//                        if(mClickControler.canTrigger()){
//                            int lastPosition = MusicPlayerManager.getInstance().playLastIndex();
//                            setCurrentMusicItem(lastPosition);
//                        }
//                        break;
//                    //???????????????
//                    case R.id.music_btn_play_pause:
//                        if(mClickControler.canTrigger()){
//                            MusicPlayerManager.getInstance().playOrPause();
//                        }
//                        break;
//                    //?????????
//                    case R.id.music_btn_next:
//                        if(mClickControler.canTrigger()){
//                            int nextPosition = MusicPlayerManager.getInstance().playNextIndex();
//                            setCurrentMusicItem(nextPosition);
//                        }
//                        break;
//                    //??????
//                    case R.id.music_btn_menu:
//                        MusicPlayerListDialog.getInstance(MusicPlayerActivity.this).
//                                setMusicOnItemClickListener(new MusicOnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(View view, int posotion,long musicID) {
//                                        setCurrentMusicItem(posotion);
//                                    }
//                                }).show();
//                        break;
//                    //????????????
//                    case R.id.music_btn_alarm:
//                        MusicAlarmSettingDialog.getInstance(MusicPlayerActivity.this).
//                                setOnAlarmModelListener(new MusicAlarmSettingDialog.OnAlarmModelListener() {
//                                    @Override
//                                    public void onAlarmModel(int alarmModel) {
//                                        final int musicAlarmModel =
//                                                MusicPlayerManager.getInstance().setPlayerAlarmModel(alarmModel);
//                                        getHandler().post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                setPlayerConfig(-1,musicAlarmModel,true);
//                                            }
//                                        });
//                                    }
//                                }).show();
//                        break;
//                    //??????
//                    case R.id.music_back:
//                        onBackOutPlayer();
//                        break;
//                    //??????
//                    case R.id.music_top_collect:
//                        if(null!=mMusicJukeBoxView&&null!=mMusicJukeBoxView.getCurrentMedia()){
//                            BaseAudioInfo currentMedia = mMusicJukeBoxView.getCurrentMedia();
//                            if(mBtnCollect.isSelected()){
//                                boolean isSuccess = MusicPlayerManager.getInstance().unCollectMusic(currentMedia.getAudioId());
//                                if(isSuccess){
//                                    mBtnCollect.setSelected(false);
//                                }
//                            }else{
//                                boolean isSuccess = MusicPlayerManager.getInstance().collectMusic(currentMedia);
//                                if(isSuccess){
//                                    mBtnCollect.setSelected(true);
//                                    MusicPlayerManager.getInstance().observerUpdata(new MusicStatus());
//                                }
//                            }
//                        }
//                        break;
//                }
//            }
//        };
//
//        findViewById(R.id.music_btn_model).setOnClickListener(onClickListener);
//        findViewById(R.id.music_btn_last).setOnClickListener(onClickListener);
//        findViewById(R.id.music_btn_play_pause).setOnClickListener(onClickListener);
//        findViewById(R.id.music_btn_next).setOnClickListener(onClickListener);
//        findViewById(R.id.music_btn_menu).setOnClickListener(onClickListener);
//
//        mMusicPlayerModel = (ImageView) findViewById(R.id.music_btn_model);
//        mMusicBtnPlayPause = (ImageView) findViewById(R.id.music_btn_play_pause);
//        findViewById(R.id.music_back).setOnClickListener(onClickListener);
//        mMusicAlarm = (TextView) findViewById(R.id.music_btn_alarm);
//        mMusicAlarm.setOnClickListener(onClickListener);
//        mCurrentTime = (TextView) findViewById(R.id.music_current_time);
//        mTotalTime = (TextView) findViewById(R.id.music_total_time);
//        mBtnCollect = (ImageView) findViewById(R.id.music_top_collect);
//        mBtnCollect.setOnClickListener(onClickListener);
//        //??????
//        mMusicJukeBoxView = (MusicJukeBoxView) findViewById(R.id.music_discview);
//        mSeekBar = (SeekBar) findViewById(R.id.music_seek_bar);
//        mRootLayout = (MusicJukeBoxBackgroundLayout) findViewById(R.id.root_layout);
//        mMusicJukeBoxView.setPlayerInfoListener(this);
//        mViewTitle = (TextView) findViewById(R.id.music_title);
//        mSubTitle = (TextView) findViewById(R.id.music_sub_title);
//        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(fromUser){
//                    long durtion = MusicPlayerManager.getInstance().getDurtion();
//                    if(durtion>0){
//                        mCurrentTime.setText(MusicUtils.getInstance().stringForAudioTime(
//                                progress * durtion / 100));
//                    }
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                isTouchSeekBar=true;
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                isTouchSeekBar=false;
//                long durtion = MusicPlayerManager.getInstance().getDurtion();
//                if(durtion>0){
//                    long currentTime = seekBar.getProgress() * durtion / 100;
//                    MusicPlayerManager.getInstance().seekTo(currentTime);
//                }
//            }
//        });
//        mClickControler = new MusicClickControler();
//        mClickControler.init(3,1);
//        //??????????????????
//        RelativeLayout toolBar = (RelativeLayout) findViewById(R.id.music_top_bar);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolBar.getLayoutParams();
//        layoutParams.setMargins(0,MusicUtils.getInstance().getStatusBarHeight(this),0,0);
//    }
//
//    /**
//     * ???????????????
//     * @param position ??????
//     */
//    private boolean setCurrentMusicItem(int position) {
//        boolean smoothScroll=false;
//        if(null!=mMusicJukeBoxView&&position>-1){
//            if(Math.abs(mMusicJukeBoxView.getCurrentItem()-position)>2){
//                mMusicJukeBoxView.setCurrentMusicItem(position, false,true);
//            }else{
//                smoothScroll=true;
//                mMusicJukeBoxView.setCurrentMusicItem(position, true,false);
//            }
//        }
//        return smoothScroll;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        isVisibility=true;
//        //????????????,??????????????????????????????????????????
//        if(null!=mBtnCollect&&null!=mMusicJukeBoxView&&null!=mMusicJukeBoxView.getCurrentMedia()){
//            boolean isExist = SqlLiteCacheManager.getInstance().isExistToCollectByID(mMusicJukeBoxView.getCurrentMedia().getAudioId());
//            mBtnCollect.setSelected(isExist);
//        }
//        if(MusicPlayerManager.getInstance().getPlayerState()==MusicConstants.MUSIC_PLAYER_PLAYING){
//            if(null!=mMusicBtnPlayPause) mMusicBtnPlayPause.setImageResource(R.drawable.music_player_pause_selector);
//            if(null!= mMusicJukeBoxView){
//                mMusicJukeBoxView.onStart();
//            }
//        }
//        MusicWindowManager.getInstance().onInvisible();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(null!= mMusicJukeBoxView){
//            mMusicJukeBoxView.onPause();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        isVisibility=false;
//    }
//
//    /**
//     * ????????????????????????ICON
//     * @param playerModel
//     * @param isToast ??????????????????
//     * @return
//     */
//    private int getResToPlayModel(int playerModel,boolean isToast) {
//        int playerModelToRes = MediaUtils.getInstance().getPlayerModelToWhiteRes(playerModel);
//        String playerModelToString = MediaUtils.getInstance().getPlayerModelToString(MusicPlayerActivity.this,playerModel);
//        if(isToast){
//            Toast.makeText(MusicPlayerActivity.this,playerModelToString,Toast.LENGTH_SHORT).show();
//        }
//        return playerModelToRes;
//    }
//
//    //========================================?????????????????????==========================================
//
//    /**
//     * ????????????????????????ViewPager??????
//     * @param audioInfo ????????????
//     */
//    @Override
//    public void onScrollOffsetObject(BaseAudioInfo audioInfo) {
//        mViewTitle.setText(audioInfo.getAudioName());
//        mSubTitle.setText(audioInfo.getNickname());
//    }
//
//    /**
//     * Pager????????????????????????????????????UI
//     * @param audioInfo ????????????
//     * @param newPosition ???????????????????????????Position
//     */
//    @Override
//    public void onVisible(BaseAudioInfo audioInfo, int newPosition) {
//        if(null!=audioInfo){
//            mViewTitle.setText(audioInfo.getAudioName());
//            mSubTitle.setText(audioInfo.getNickname());
//            mTotalTime.setText(MusicUtils.getInstance().stringForAudioTime(audioInfo.getAudioDurtion()));
//            //????????????
//            boolean isExist = SqlLiteCacheManager.getInstance().isExistToCollectByID(audioInfo.getAudioId());
//            mBtnCollect.setSelected(isExist);
//            mRootLayout.setBackgroundCover(MusicUtils.getInstance().getMusicFrontPath(audioInfo),1200);
//        }
//    }
//
//    /**
//     * Pager?????????????????????,?????????????????????
//     * @param oldPosition ??????????????????Position
//     */
//    @Override
//    public void onInvisible(int oldPosition) {
//        if(null!=mCurrentTime){
//            mCurrentTime.setText("00:00");
//            mSeekBar.setSecondaryProgress(0);
//            mSeekBar.setProgress(0);
//        }
//        if(isVisibility){
//            MusicPlayerManager.getInstance().onReset();
//        }else{
//            if(null!=mMusicJukeBoxView){
//                mMusicJukeBoxView.updatePosition();
//            }
//        }
//    }
//
//    /**
//     * ???????????????????????????????????????ViewPager??????????????????????????????????????????????????????
//     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//     * @param position ??????
//     * @param audioInfo ????????????
//     * @param startPlayer true:???????????? false:??????????????????
//     */
//    @Override
//    public void onOffsetPosition(int position, BaseAudioInfo audioInfo, boolean startPlayer) {
//        if(null!=audioInfo&&startPlayer){
//            mCurrentTime.setText("00:00");
//            mSeekBar.setSecondaryProgress(0);
//            mSeekBar.setProgress(0);
//            //????????????
//            MusicPlayerManager.getInstance().startPlayMusic(position);
//        }
//    }
//
//    /**
//     * ?????????????????????,????????????????????????????????????????????????????????????
//     * @param playerState ?????????????????????
//     */
//    @Override
//    public void onJukeBoxState(int playerState) {
////        if(null!=mMusicBtnPlayPause){
////            if(playerState==MusicConstants.JUKE_BOX_PLAY){
////                mMusicBtnPlayPause.setImageResource(R.drawable.music_player_pause_selector);
////            }else if(playerState==MusicConstants.JUKE_BOX_PAUSE){
////                mMusicBtnPlayPause.setImageResource(R.drawable.music_player_play_selector);
////            }
////        }
//    }
//
//    /**
//     * ???????????????????????????????????????????????????????????????????????????
//     * @param view click view
//     */
//    @Override
//    public void onClickJukeBox(View view) {
//        //?????????????????????
//        if(null==mParserEngin){
//            mParserEngin = new MusicLrcRowParserEngin();
//        }
//        mMusicJukeBoxView.setLrcRows(MusicPlayerManager.getInstance().getCurrentPlayerID()+"",
//                MusicPlayerManager.getInstance().getCurrentPlayerHashKey(),mParserEngin);
//    }
//
//    /**
//     * ??????????????????????????????????????????????????????
//     * @param lrcRow ????????????
//     */
//    @Override
//    public void onLrcSeek(MusicLrcRow lrcRow) {
//        if(null!=lrcRow){
//            MusicPlayerManager.getInstance().seekTo(lrcRow.getTime());
//        }
//    }
//
//    //========================================?????????????????????==========================================
//
//    /**
//     * ?????????????????????
//     * @param playerState ?????????????????????
//     * @param message
//     */
//    @Override
//    public void onMusicPlayerState(final int playerState, final String message) {
//        Logger.d(TAG,"onMusicPlayerState-->"+playerState);
//        getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                if (playerState==MusicConstants.MUSIC_PLAYER_ERROR&&!TextUtils.isEmpty(message)) {
//                    Toast.makeText(MusicPlayerActivity.this,message,Toast.LENGTH_SHORT).show();
//                }
//                switch (playerState) {
//                    case MusicConstants.MUSIC_PLAYER_PREPARE:
//                        if (null != mMusicAlarm &&MusicPlayerManager.getInstance().getPlayerAlarmModel()
//                                !=MusicConstants.MUSIC_ALARM_MODEL_0) {
//                            Drawable drawable = getResources().getDrawable(R.drawable.ic_music_alarm_pre);
//                            mMusicAlarm.setCompoundDrawablesWithIntrinsicBounds(drawable,
//                                    null, null, null);
//                            mMusicAlarm.setTextColor(Color.parseColor("#F8E71C"));
//                        }
//                        if (null != mMusicBtnPlayPause) mMusicBtnPlayPause.setImageResource(
//                                R.drawable.music_player_pause_selector);
//                        if (null != mMusicJukeBoxView) mMusicJukeBoxView.onStart();
//                        break;
//                    case MusicConstants.MUSIC_PLAYER_BUFFER:
//
//                        break;
//                    case MusicConstants.MUSIC_PLAYER_PLAYING:
//                        if (null != mMusicBtnPlayPause)
//                            mMusicBtnPlayPause.setImageResource(R.drawable.music_player_pause_selector);
//                        if (null != mMusicJukeBoxView) mMusicJukeBoxView.onStart();
//                        break;
//                    case MusicConstants.MUSIC_PLAYER_PAUSE:
//                        if (null != mMusicBtnPlayPause)
//                            mMusicBtnPlayPause.setImageResource(R.drawable.music_player_play_selector);
//                        if (null != mMusicJukeBoxView) mMusicJukeBoxView.onPause();
//                        break;
//                    case MusicConstants.MUSIC_PLAYER_STOP:
//                        if (null != mMusicBtnPlayPause) mMusicBtnPlayPause.setImageResource(
//                                R.drawable.music_player_play_selector);
//                        if (null != mCurrentTime) mCurrentTime.setText("00:00");
//                        if(null!=mSeekBar){
//                            mSeekBar.setSecondaryProgress(0);
//                            mSeekBar.setProgress(0);
//                        }
//                        if(null!=mMusicAlarm){
//                            Drawable drawable = getResources().getDrawable(R.drawable.ic_music_alarm_noimal);
//                            mMusicAlarm.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
//                            mMusicAlarm.setTextColor(Color.parseColor("#FFFFFF"));
//                            mMusicAlarm.setText(getString(R.string.text_music_alarm));
//                        }
//                        if (null != mMusicJukeBoxView) mMusicJukeBoxView.onStop();
//                        break;
//                    case MusicConstants.MUSIC_PLAYER_ERROR:
//                        if (null != mMusicBtnPlayPause){
//                            mMusicBtnPlayPause.setImageResource(R.drawable.music_player_play_selector);
//                        }
//                        if (null != mSeekBar) {
//                            mSeekBar.setSecondaryProgress(0);
//                            mSeekBar.setProgress(0);
//                        }
//                        if (null != mCurrentTime){
//                            mCurrentTime.setText("00:00");
//                        }
//                        if (null != mMusicJukeBoxView){
//                            mMusicJukeBoxView.onStop();
//                        }
//                        break;
//                }
//            }
//        });
//    }
//
//    /**
//     * ?????????????????????
//     * @param totalDurtion ?????????
//     */
//    @Override
//    public void onPrepared(final long totalDurtion) {
//        if(null!=mTotalTime){
//            mTotalTime.setText(MusicUtils.getInstance().stringForAudioTime(totalDurtion));
//        }
//        if(isVisibility&&null!=mMusicJukeBoxView){
//            getHandler().post(new Runnable() {
//                @Override
//                public void run() {
//                    mMusicJukeBoxView.onStart();
//                }
//            });
//        }
//    }
//
//    /**
//     * ????????????
//     * @param percent ?????????
//     */
//    @Override
//    @Deprecated
//    public void onBufferingUpdate(final int percent) {
//        if(null!=mSeekBar&&mSeekBar.getSecondaryProgress()<100){
//            mSeekBar.setSecondaryProgress(percent);
//        }
//    }
//
//    @Override
//    public void onInfo(int event, int extra) {}
//
//    /**
//     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//     * @param musicInfo ?????????????????????
//     * @param position ???????????????????????????
//     */
//    @Override
//    public void onPlayMusiconInfo(BaseAudioInfo musicInfo,final int position) {
//        getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                setCurrentMusicItem(position);
//            }
//        });
//    }
//
//    /**
//     * ??????????????????,???????????????????????????????????????????????????????????????????????????
//     * ???????????????????????? MusicPlayerManager.getInstance().continuePlay(String sourcePath);??????
//     * @param musicInfo ????????????
//     * @param position ??????
//     */
//    @Override
//    public void onMusicPathInvalid(BaseAudioInfo musicInfo, int position) {
//        if(null!=mMusicJukeBoxView){
//            mMusicJukeBoxView.onStop();
//        }
//    }
//
//    /**
//     * ??????????????????????????????????????????????????????
//     * @param totalDurtion
//     * @param currentDurtion
//     * @param alarmResidueDurtion
//     */
//    @Override
//    public void onTaskRuntime(final long totalDurtion, final long currentDurtion,
//                              final long alarmResidueDurtion,int bufferProgress) {
//        updataPlayerParams(totalDurtion,currentDurtion,alarmResidueDurtion,bufferProgress);
//    }
//
//    /**
//     * ???????????????
//     * @param playModel ????????????
//     * @param alarmModel ????????????
//     * @param isToast ??????????????????
//     */
//    @Override
//    public void onPlayerConfig(final int playModel, final int alarmModel,
//                               final boolean isToast) {
//        getHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                setPlayerConfig(playModel,alarmModel,isToast);
//            }
//        });
//    }
//
////    @Override
////    public void finish() {
////        super.finish();
////        overridePendingTransition(0, R.anim.music_bottom_menu_exit);
////    }
//
//    /**
//     * ???????????????
//     * @param playModel
//     * @param alarmModel
//     */
//    private synchronized void setPlayerConfig(int playModel, int alarmModel,
//                                              boolean isToast) {
//        if(playModel>-1&&null!=mMusicPlayerModel){
//            mMusicPlayerModel.setImageResource(getResToPlayModel(playModel,isToast));
//        }
//        if(null!=mMusicAlarm){
//            if(alarmModel==MusicConstants.MUSIC_ALARM_MODEL_0){
//                Drawable drawable = getResources().getDrawable(R.drawable.ic_music_alarm_noimal);
//                mMusicAlarm.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
//                mMusicAlarm.setText(getString(R.string.text_music_alarm));
//                mMusicAlarm.setTextColor(Color.parseColor("#FFFFFF"));
//            }else {
//                Drawable drawable = getResources().getDrawable(R.drawable.ic_music_alarm_pre);
//                mMusicAlarm.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
//                //????????????????????????????????????
//                String durtion="00:00";
//                if(alarmModel==MusicConstants.MUSIC_ALARM_MODEL_10){
//                    durtion="10:00";
//                }else if(alarmModel==MusicConstants.MUSIC_ALARM_MODEL_15){
//                    durtion="15:00";
//                }else if(alarmModel==MusicConstants.MUSIC_ALARM_MODEL_30){
//                    durtion="30:00";
//                }else if(alarmModel==MusicConstants.MUSIC_ALARM_MODEL_60){
//                    durtion="01:00:00";
//                }else if(alarmModel==MusicConstants.MUSIC_ALARM_MODEL_CURRENT){
//                    durtion="00:00";
//                }
//                mMusicAlarm.setText(durtion);
//                mMusicAlarm.setTextColor(Color.parseColor("#F8E71C"));
//            }
//        }
//    }
//
//    /**
//     * ?????????????????????
//     * @param totalDurtion ????????? ??????
//     * @param currentDurtion ????????????????????? ??????
//     * @param alarmResidueDurtion ???????????????????????????????????????????????? 60*1000 ????????????????????????????????????
//     * @param bufferProgress ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//     *                       ???????????????????????????????????????????????????????????????
//     */
//    private synchronized void updataPlayerParams(final long totalDurtion, final long currentDurtion,
//                                                 final long alarmResidueDurtion, int bufferProgress) {
//        if(isVisibility&&null!=mSeekBar){
//            //????????????????????????
//            if(mSeekBar.getSecondaryProgress()<100){
//                mSeekBar.setSecondaryProgress(bufferProgress);
//            }
//            if(totalDurtion>-1){
//                if(!isTouchSeekBar){
//                    int progress = (int) (((float) currentDurtion / totalDurtion) * 100);// ??????????????????
//                    mSeekBar.setProgress(progress);
//                }
//            }
//            getHandler().post(new Runnable() {
//                @Override
//                public void run() {
//                    //?????????????????????
//                    if(!isTouchSeekBar&&totalDurtion>-1){
//                        if(null!=mTotalTime){
//                            mTotalTime.setText(MusicUtils.getInstance().stringForAudioTime(totalDurtion));
//                            mCurrentTime.setText(MusicUtils.getInstance().stringForAudioTime(currentDurtion));
//                        }
//                    }
//                    if(null!=mMusicJukeBoxView){
//                        mMusicJukeBoxView.updateLrcPosition(currentDurtion);
//                    }
//                    //??????????????????
//                    if(alarmResidueDurtion<=0){
//                        if(null!=mMusicAlarm){
//                            Drawable drawable = getResources().getDrawable(R.drawable.ic_music_alarm_noimal);
//                            mMusicAlarm.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
//                            mMusicAlarm.setTextColor(Color.parseColor("#FFFFFF"));
//                            mMusicAlarm.setText(getString(R.string.text_music_alarm));
//                        }
//                        return;
//                    }
//                    if(alarmResidueDurtion>-1&&alarmResidueDurtion <= (60 * 60)){
//                        String audioTime = MusicUtils.getInstance().stringForAudioTime(alarmResidueDurtion*1000);
//                        if(null!=mMusicAlarm) mMusicAlarm.setText(audioTime);
//                    }
//                }
//            });
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            onBackOutPlayer();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    /**
//     * ?????????????????????
//     */
//    private void onBackOutPlayer() {
//        //?????????????????????????????????????????????????????????????????????????????????????????????
//        if(null!=mMusicJukeBoxView&&!mMusicJukeBoxView.isBackPressed()){
//            return;
//        }
//        if(!MusicWindowManager.getInstance().checkAlertWindowsPermission(MusicPlayerActivity.this)){
//            QuireDialog.getInstance(MusicPlayerActivity.this)
//                    .setTitleText(getString(R.string.text_music_close_tips))
//                    .setContentText(getString(R.string.text_music_close_permission_tips))
//                    .setSubmitTitleText(getString(R.string.text_music_open))
//                    .setCancelTitleText(getString(R.string.text_music_stop_play))
//                    .setTopImageRes(R.drawable.ic_setting_tips1)
//                    .setBtnClickDismiss(false)
//                    .setDialogCancelable(false)
//                    .setOnQueraConsentListener(new QuireDialog.OnQueraConsentListener() {
//                        @Override
//                        public void onConsent(QuireDialog dialog) {
//                            dialog.dismiss();
//                            try {
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.setData(Uri.parse( "package:"+
//                                            MusicUtils.getInstance().getPackageName(MusicPlayerActivity.this)));
//                                    MusicPlayerActivity.this.startActivityForResult(intent,MusicConstants.REQUST_WINDOWN_PERMISSION);
//                                } else {
//                                    Toast.makeText(MusicPlayerActivity.this,getString(R.string.text_music_active_open),Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent();
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
//                                    startActivityForResult(intent,MusicConstants.REQUST_WINDOWN_PERMISSION);
//                                }
//                            }catch (RuntimeException e){
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onRefuse(QuireDialog dialog) {
//                            dialog.dismiss();
//                            MusicPlayerManager.getInstance().onStop();
//                            finish();
//                        }
//                    }).show();
//            return;
//        }
//        createMiniJukeBoxToWindown();
//    }
//
//    /**
//     * ??????????????????????????????????????????
//     */
//    private void createMiniJukeBoxToWindown() {
//        if(!MusicWindowManager.getInstance().isWindowShowing()){
//            if(null!=MusicPlayerManager.getInstance().getCurrentPlayerMusic()){
//                BaseAudioInfo musicInfo = MusicPlayerManager.getInstance().getCurrentPlayerMusic();
//                MusicWindowManager.getInstance().createMiniJukeBoxToWindown(getApplicationContext());
//                MusicStatus musicStatus=new MusicStatus();
//                musicStatus.setId(musicInfo.getAudioId());
//                String frontPath=MusicUtils.getInstance().getMusicFrontPath(musicInfo);
//                musicStatus.setCover(frontPath);
//                musicStatus.setTitle(musicInfo.getAudioName());
//                int playerState = MusicPlayerManager.getInstance().getPlayerState();
//                boolean playing = playerState==MusicConstants.MUSIC_PLAYER_PLAYING
//                        || playerState==MusicConstants.MUSIC_PLAYER_PREPARE
//                        || playerState==MusicConstants.MUSIC_PLAYER_BUFFER;
//                musicStatus.setPlayerStatus(playing?MusicStatus.PLAYER_STATUS_START:MusicStatus.PLAYER_STATUS_PAUSE);
//                MusicWindowManager.getInstance().updateWindowStatus(musicStatus);
//            }
//        }
//        //????????????????????????????????????????????????????????????
//        MusicWindowManager.getInstance().onVisible();
//        finish();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(MusicWindowManager.getInstance().checkAlertWindowsPermission(MusicPlayerActivity.this)){
//            createMiniJukeBoxToWindown();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        isVisibility=false;
//        if(null!=mHandler){
//            mHandler.removeMessages(0);
//            mHandler.removeCallbacksAndMessages(null);
//            mHandler=null;
//        }
//        MusicPlayerManager.getInstance().removePlayerListener(this);
//        if(null!= mMusicJukeBoxView){
//            mMusicJukeBoxView.onDestroy();
//            mMusicJukeBoxView =null;
//        }
//        if(null!=mRootLayout){
//            mRootLayout.onDestroy();
//            mRootLayout=null;
//        }
//        isTouchSeekBar=false;
//    }
//
//    /**
//     * ???????????????????????????????????????????????????????????????????????????
//     * @param o Observable
//     * @param arg ??????
//     */
//    @Override
//    public void update(Observable o, Object arg) {
//        if(o instanceof MusicSubjectObservable && null!=arg&& arg instanceof MusicStatus){
//            //????????????,??????????????????????????????????????????
//            if(null!=mBtnCollect&&null!=mMusicJukeBoxView&&null!=mMusicJukeBoxView.getCurrentMedia()){
//                boolean isExist = SqlLiteCacheManager.getInstance().isExistToCollectByID(mMusicJukeBoxView.getCurrentMedia().getAudioId());
//                mBtnCollect.setSelected(isExist);
//            }
//        }
//    }
//}