package com.itlao.repairservice.home.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.itlao.cmd.MyCmd;
import com.itlao.repairservice.ActivityFindPassword;
import com.itlao.repairservice.LayoutZiDingYi;
import com.itlao.repairservice.QuetionDetailActivity;
import com.itlao.repairservice.R;

import com.itlao.repairservice.findmaster.ctrl.ZhaoShiFuActivity;
import com.itlao.repairservice.findq.ctrl.FindQuetionActivity;
import com.itlao.repairservice.home.ListMainPageQuetionsShow;
import com.itlao.repairservice.pcenter.PersonalCenterActivity;
import com.itlao.repairservice.pcenter.imcustomer.ctrl.IamKehuActivity;
import com.itlao.repairservice.publicq.ctrl.FabuActivity2;
import com.itlao.repairservice.publicq.ctrl.FabuActivity2.MyLocationListenner;
import com.itlao.repairservice.register.ctrl.ActivityRegister;
import com.itlao.repairservice.utils.SendRequestUtil;
import com.itlao.repairservice.utils.Send_Notification;
import com.itlao.slidingmenu.SlidingMenu;
import com.itlao.utils.http.handler.RequestStringHandler;
import com.itlao.utils.util.JsonUtil;
import com.srx.widget.TabBarView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static MainActivity mainActivity;// /storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg", "/storage/sdcard1/tieba/long.jpg", "/storage/sdcard1/tieba/bing.jpg

    private int mark = 1;
    private int mark_new = 0;
    private int mark_fujin = 1;
    private ImageView bt_zhaoshifu;
    private ImageView bt_fabu;
    private ImageView bt_zhaowenti;
    private ImageView bt_my;
    private MaterialAnimatedSwitch bt_double_select;
    private ImageView select_new;
    private ImageView select_fujin;
    private static final String TAG = "ChrisSlideMenu";
    private LinearLayout mainLayout;
    private RelativeLayout leftLayout;
    private RelativeLayout rightLayout;
    private LayoutZiDingYi layoutSlideMenu;
    private ListView mListMore;
    private ImageView ivMore;
    private GestureDetector mGestureDetector;
    private static final int SPEED = 30;
    private boolean bIsScrolling = false;
    private int iLimited = 0;
    private int mScroll = 0;
    private View mClickedView = null;
    private Intent intent_zahoshifu;
    private Intent intent_fabu;
    private Intent intent_zhaowenti;
    private Intent intent_my;
    private Intent intent_register;
    private GridView page_home_grid_main_page;
    private SlidingMenu slidingMenu;
    private boolean isExit = false;
    private TextView zhaoshifuTV;
    private TextView fabuTV;
    private TextView wentiTV;
    private TextView myTV;
    private ImageView ivSettings;
    private RequestStringHandler readtaskListHandler;
    private RequestStringHandler readtaskListHandler2;
    public static boolean isNear = false;
    private TextView tv_all;

    private TextView select_all;
    public static String query = "";
    public static boolean isSolved = false;
    public static boolean isAsc = true;
    public static double myLat;
    public static double myLng;
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    public String adrress;
    private TabBarView tabBarAnimView;
    private LinearLayout ll_near;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian_page);
        mainActivity = this;

        initView();

        tabBarAnimView = (TabBarView) findViewById(R.id.tabBarAnimView);
//        tabBarAnimView.setDrawingCacheBackgroundColor(getResources().getColor(R.color.blue));

        initTabBarAnimView();
        tabBarAnimView.setOnTabBarClickListener(onTabBarClickListener);

        Intent intent = getIntent();
        // 客户登录
        if (intent.getFlags() == Intent.FLAG_ACTIVITY_CLEAR_TOP
                && !intent.getBooleanExtra("login", false)) {

            // android.os.Process.killProcess(android.os.Process.myPid());
            System.out.println("finish!!!!");
            finish();

        }

        intent_zahoshifu = new Intent(this, ZhaoShiFuActivity.class);
        intent_fabu = new Intent(this, FabuActivity2.class);
        intent_zhaowenti = new Intent(this, FindQuetionActivity.class);
        intent_my = new Intent(this, PersonalCenterActivity.class);
        intent_register = new Intent(this, ActivityRegister.class);

        String fileDirPath = android.os.Environment
                .getExternalStorageDirectory().getAbsolutePath()// 得到外部存储卡的数据库的路径名
                + "/RepairService";// 我要存储的目录
        File f = new File(fileDirPath);
        File[] files = f.listFiles();// 列出所有文件

        if (MyCmd.id == -1) {

            // 储存个人信息
            MyCmd.setId(-1);
            MyCmd.setPassword(null);
            MyCmd.setName(null);
            MyCmd.setNickname(null);
            MyCmd.setSex(-1);
            MyCmd.setAddress(null);
            MyCmd.setLongitude(null);
            MyCmd.setLatitude(null);
            MyCmd.setH_s(null);
            MyCmd.setP_n(null);
            MyCmd.setQq(null);
            MyCmd.setEmail(null);
            MyCmd.setIs_pro(0);
            MyCmd.setT_s(0);
            MyCmd.setProfession(-1);
            MyCmd.setPro_det(null);
            MyCmd.setStatus(1);
            // MyCmd.setDirty(json.getInt("dirty"));
        }

        setListener();

        initSlidingMenu();
        // 调用图片，文字说明
        // List_main_page_quetions_show.List_questions_show(item_message,pc_path);
        ListMainPageQuetionsShow list_main_page_quetions_show = new ListMainPageQuetionsShow();
        list_main_page_quetions_show.List_questions_show(slidingMenu);

        setHandler(this);

        Timer timer = new Timer();
        timer.schedule(new mTimerTask(), 0, 3000);

        final ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
        if (listItems.size() <= 0) {

            for (int i = 0; i < 15; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();

                map.put("item", "类型");
                listItems.add(map);
            }
        }
        String[] from1 = {"item"};
        // grid_item.xml中对应的ImageView控件和TextView控件
        int[] to1 = {R.id.item_textView_leixing1};
        SimpleAdapter adapterSimple = new SimpleAdapter(
                this, listItems,
                R.layout.grid_item_leixing_list, from1, to1);
        GridView gridView = (GridView) this
                .findViewById(R.id.page_home_grid_people);
        // 对GridView进行适配
        gridView.setAdapter(adapterSimple);
                    /* 动态跟新ListView */
        adapterSimple.notifyDataSetChanged();
    }

    private TabBarView.OnTabBarClickListener onTabBarClickListener = new TabBarView.OnTabBarClickListener() {

        @Override
        public void onMainBtnsClick(int position, int[] clickLocation) {

            switch (position) {

                case 0:
                    startActivity(intent_zahoshifu);
                    break;
                case 1:
                    if (MyCmd.id == -1) {// 游客
                        startActivity(intent_register);
                    } else {
                        startActivity(intent_fabu);
                    }
                    break;
                case 3:
                    slidingMenu.toggle(true);
                    break;
                case 4:
                    startActivity(intent_my);
                    break;
            }

        }

        @Override
        public void onMainBtnsClick(int position) {

        }

        @Override
        public void onLeftBtnClick(int page) {

        }

        @Override
        public void onRightBtnClick(int page) {

        }

    };

    private void initTabBarAnimView() {
        tabBarAnimView.setMainBitmap(R.drawable.plus_icon);
//		tabBarAnimView.bindBtnsForPage(0, R.drawable.nearby_icon, R.drawable.search_icon, R.drawable.chats_icon);
//		tabBarAnimView.bindBtnsForPage(1, R.drawable.profile_icon, R.drawable.edit_icon, 0);
//		tabBarAnimView.bindBtnsForPage(2, R.drawable.chats_icon, R.drawable.search_icon, 0);
//		tabBarAnimView.bindBtnsForPage(3, R.drawable.settings_icon, 0, R.drawable.edit_icon);
        tabBarAnimView.bindBtnsForPage(0, R.drawable.nearby_icon, R.drawable.search_icon, 0);
        tabBarAnimView.bindBtnsForPage(1, R.drawable.profile_icon, R.drawable.search_icon, 0);
        tabBarAnimView.bindBtnsForPage(2, R.drawable.chats_icon, R.drawable.search_icon, 0);
        tabBarAnimView.bindBtnsForPage(3, R.drawable.settings_icon, R.drawable.search_icon, 0);
        tabBarAnimView.initializePage(0);
    }

    class mTimerTask extends TimerTask {

        @Override
        public void run() {

            // Toast.makeText(MainActivity.this, "定时器",
            // Toast.LENGTH_SHORT).show();

            if (MyCmd.id != -1) {

                System.out.println("登录-定时器");
                sendResquest();
                sendResquest2();
            } else {

                System.out.println("游客-定时器");
            }

        }

    }

    public void sendResquest() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", MyCmd.id);

        try {

            SendRequestUtil.post("AndroidCtrl.do?action=getMsg", params,
                    readtaskListHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendResquest2() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", MyCmd.id);

        try {

            SendRequestUtil.post("AndroidCtrl.do?action=getMasterFeedbackMsg", params,
                    readtaskListHandler2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {

//		bt_zhaoshifu = (ImageView) findViewById(R.id.main_button_zhaoshifu);
//		bt_fabu = (ImageView) findViewById(R.id.main_button_fabu);
//		bt_zhaowenti = (ImageView) findViewById(R.id.main_button_zhaowenti);
//		bt_my = (ImageView) findViewById(R.id.main_button_my);
        page_home_grid_main_page = (GridView) findViewById(R.id.page_home_grid_main_page);

        bt_double_select = (MaterialAnimatedSwitch) findViewById(R.id.double_select);
        select_new = (ImageView) findViewById(R.id.select_new);
        select_fujin = (ImageView) findViewById(R.id.select_fujin);
        ll_near = (LinearLayout) findViewById(R.id.ll_near);
        ivMore = (ImageView) findViewById(R.id.ivMore);
        ivSettings = (ImageView) findViewById(R.id.ivSettings);
//		zhaoshifuTV = (TextView) findViewById(R.id.zhaoshifuTV);
//		fabuTV = (TextView) findViewById(R.id.fabuTV);
//		wentiTV = (TextView) findViewById(R.id.wentiTV);
//		myTV = (TextView) findViewById(R.id.myTV);
        select_all = (TextView) findViewById(R.id.select_all);
    }

    private void setListener() {

        select_all.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "选择全部", Toast.LENGTH_SHORT)
                        .show();
                isSolved = false;
                query = "";
                ListMainPageQuetionsShow.c_id = -1;
                ListMainPageQuetionsShow.listItems.clear();// 清除问题数据
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("c_id", ListMainPageQuetionsShow.c_id);//类型id
                params.put("isSolved", isSolved);//是否解决
                if (isNear) {
                    params.put("myLat", myLat);//当前纬度
                    params.put("myLng", myLng);//当前经度
                }
                params.put("filter", query);//搜索关键字
                params.put("sortName", "p_t");//排序字段
                params.put("isAsc", isAsc);//升序
                params.put("isNear",isNear );//是否解决
                params.put("pageNum", ListMainPageQuetionsShow.pageNum);
                params.put("pageSize", ListMainPageQuetionsShow.pageSize);
                // HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
                SendRequestUtil.post("HomeCtrl/photo", params,
                        ListMainPageQuetionsShow.readtaskListHandler);
            }
        });

		/*tv_all.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Toast.makeText(MainActivity.this, "选择全部", Toast.LENGTH_SHORT)
				.show();
				isSolved=false;
				query="";
				ListMainPageQuetionsShow.c_id=-1;
				ListMainPageQuetionsShow.listItems.clear();// 清除问题数据
			    Map<String, Object> params = new HashMap<String, Object>();
				params.put("c_id", ListMainPageQuetionsShow.c_id);//类型id
				params.put("isSolved", isSolved);//是否解决
				params.put("filter", query);//搜索关键字
				params.put("sortName", "p_t");//排序字段
				params.put("isAsc", isAsc);//升序
				params.put("pageNum", ListMainPageQuetionsShow.pageNum);
				params.put("pageSize", ListMainPageQuetionsShow.pageSize);
				// HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
				SendRequestUtil.post("HomeCtrl.do?action=photo", params,
						ListMainPageQuetionsShow.readtaskListHandler);
			}
		});*/
        ivSettings.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                onSearchRequested();

                return false;
            }
        });
        // 左图标
        ivMore.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                slidingMenu.toggle(true);
            }
        });
//		bt_zhaoshifu.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				bt_zhaoshifu.setBackgroundResource(R.drawable.shifu_selected);
//				startActivity(intent_zahoshifu);
//				zhaoshifuTV.setTextColor(getResources().getColor(
//						R.color.main_blue));
//			}
//		});
//		bt_fabu.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				bt_fabu.setBackgroundResource(R.drawable.fabu_selected);
//
//				if (MyCmd.id == -1) {// 游客
//
//					startActivity(intent_register);
//					fabuTV.setTextColor(getResources().getColor(
//							R.color.main_blue));
//				} else {
//
//					startActivity(intent_fabu);
//					fabuTV.setTextColor(getResources().getColor(
//							R.color.main_blue));
//				}
//			}
//		});
//
//		bt_zhaowenti.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				slidingMenu.toggle(true);
//			}
//		});
//		bt_my.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				bt_my.setBackgroundResource(R.drawable.my_selected);
//
//				startActivity(intent_my);
//				myTV.setTextColor(getResources().getColor(R.color.main_blue));
//				// }
//
//			}
//		});

        bt_double_select.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {

                if (mark == 0) {
//					bt_double_select
//							.setBackgroundResource(R.drawable.double_selected);
                    mark++;
                    isSolved = false;
                    ListMainPageQuetionsShow.listItems.clear();// 清除问题数据
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("c_id", ListMainPageQuetionsShow.c_id);//类型id
                    params.put("isSolved", isSolved);//是否解决
                    params.put("isNear", isNear);//是否搜索附近
                    params.put("filter", query);//搜索关键字
                    params.put("sortName", "p_t");//排序字段
                    params.put("isAsc", isAsc);//升序
                    params.put("pageNum", ListMainPageQuetionsShow.pageNum);
                    params.put("pageSize", ListMainPageQuetionsShow.pageSize);
                    // HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
                    SendRequestUtil.post("HomeCtrl/photo", params,
                            ListMainPageQuetionsShow.readtaskListHandler);
                } else {
//					bt_double_select
//							.setBackgroundResource(R.drawable.double_unselect);
                    mark--;
                    isSolved = true;
                    ListMainPageQuetionsShow.listItems.clear();// 清除问题数据
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("c_id", ListMainPageQuetionsShow.c_id);//类型id
                    params.put("isSolved", isSolved);//是否解决
                    params.put("isNear", isNear);//是否搜索附近
                    params.put("filter", query);//搜索关键字
                    params.put("sortName", "p_t");//排序字段
                    params.put("isAsc", isAsc);//升序
                    params.put("pageNum", ListMainPageQuetionsShow.pageNum);
                    params.put("pageSize", ListMainPageQuetionsShow.pageSize);
                    // HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
                    SendRequestUtil.post("HomeCtrl/photo", params,
                            ListMainPageQuetionsShow.readtaskListHandler);
                }
            }
        });
        select_new.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                if (mark_new == 0) {
                    select_new.setBackgroundResource(R.drawable.selected);
                    mark_new++;
                    isAsc = true;
                } else {
                    select_new.setBackgroundResource(R.drawable.unselected);
                    mark_new--;
                    isAsc = false;
                }
            }
        });
        ll_near.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                if (mark_fujin == 0) {//选中附近
                    select_fujin.setBackgroundResource(R.drawable.selected);
                    mark_fujin++;
                    isNear = true;
                    locationAddress();

                    ListMainPageQuetionsShow.listItems.clear();// 清除问题数据
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("c_id", ListMainPageQuetionsShow.c_id);//类型id
                    params.put("isSolved", isSolved);//是否解决
                    params.put("isNear", isNear);//是否搜索附近
                    if (isNear) {
                        params.put("myLat", myLat);//当前纬度
                        params.put("myLng", myLng);//当前经度
                    }
                    params.put("filter", query);//搜索关键字
                    params.put("sortName", "p_t");//排序字段
                    params.put("isAsc", isAsc);//升序
                    params.put("pageNum", ListMainPageQuetionsShow.pageNum);
                    params.put("pageSize", ListMainPageQuetionsShow.pageSize);
                    // HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
                    SendRequestUtil.post("HomeCtrl/photo", params,
                            ListMainPageQuetionsShow.readtaskListHandler);

                } else {
                    isNear = false;
                    ListMainPageQuetionsShow.listItems.clear();// 清除问题数据
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("c_id", ListMainPageQuetionsShow.c_id);//类型id
                    params.put("isSolved", isSolved);//是否解决
                    params.put("isNear", isNear);//是否搜索附近
                    params.put("filter", query);//搜索关键字
                    params.put("sortName", "p_t");//排序字段
                    params.put("isAsc", isAsc);//升序
                    params.put("pageNum", ListMainPageQuetionsShow.pageNum);
                    params.put("pageSize", ListMainPageQuetionsShow.pageSize);
                    // HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
                    SendRequestUtil.post("HomeCtrl/photo", params,
                            ListMainPageQuetionsShow.readtaskListHandler);
                    select_fujin.setBackgroundResource(R.drawable.unselected);
                    mark_fujin--;
                }
            }
        });

    }

    public void setHandler(Context context) {
        readtaskListHandler = new RequestStringHandler(context) {

            private boolean hasMsg = false;

            @Override
            public void handleString(String response) throws Exception {
                JSONObject json = new JSONObject(response);

                // login_message = JsonUtil.getString(json, "register");
                hasMsg = json.getBoolean("hasMsg");

                if (hasMsg) {

                    String nickname = json.getString("nickname");

                    Send_Notification notice = new Send_Notification(
                            MainActivity.this, QuetionDetailActivity.class);
                    Intent intent = new Intent(MainActivity.this, QuetionDetailActivity.class);

                    intent.putExtra("stlyeMarks", 2);//游客：0，师傅：1，客户;2
                    System.out.println("json:" + json);
                    //intent.putExtra("id", json.getDouble("q_id")+"");
                    // intent.putExtra("id", JsonUtil.getString(json, "id"));
                    String id_str = json.getDouble("q_id") + "";

                    try {
                        id_str = id_str.substring(0, id_str.indexOf("."));
                    } catch (Exception e) {

                    }
                    intent.putExtra("isFeedback", true);
                    intent.putExtra("id", id_str);
                    System.out.println("q_id:" + json.getDouble("q_id"));
                    intent.putExtra("status", 2 + "");//json.put("q_id", q_id);//问题id
					
					/*notice.sendNotification("有新消息到了!", "有新消息到了!", "您发布的问题被"
							+ nickname + "师傅接单了");*/
                    notice.sendUserNotification("有新消息到了!", "有新消息到了!", "您发布的问题被"
                            + nickname + "师傅接单了", intent);

                } else {


                }

            }

        };

        readtaskListHandler2 = new RequestStringHandler(context) {

            private boolean hasMsg = false;

            @Override
            public void handleString(String response) throws Exception {
                JSONObject json = new JSONObject(response);

                hasMsg = json.getBoolean("hasMsg");

                if (hasMsg) {

                    //String nickname = json.getString("nickname");

                    Send_Notification notice = new Send_Notification(
                            MainActivity.this, QuetionDetailActivity.class);
                    Intent intent = new Intent(MainActivity.this, QuetionDetailActivity.class);

                    intent.putExtra("stlyeMarks", 2);//游客：0，师傅：1，客户;2
                    System.out.println("json:" + json);

                    String id_str = json.getDouble("q_id") + "";

                    try {
                        id_str = id_str.substring(0, id_str.indexOf("."));
                    } catch (Exception e) {

                    }
                    intent.putExtra("isFeedback", true);
                    intent.putExtra("id", id_str);
                    System.out.println("q_id:" + json.getDouble("q_id"));
                    intent.putExtra("status", 2 + "");//json.put("q_id", q_id);//问题id
					
					/*notice.sendNotification("有新消息到了!", "有新消息到了!", "您发布的问题被"
							+ nickname + "师傅接单了");*/
                    notice.sendUserNotification("有新消息到了!", "师傅已经对您的问题反馈了!", "您发布的问题被师傅解决了吗", intent);

                } else {


                }

            }

        };
    }

    /**
     * 初始化左右滑动菜单
     *
     * @author hezheng
     */
    public void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidth(3);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffset(200);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // slidingMenu.setMenu(R.layout.home_right_main);
        slidingMenu.setMenu(R.layout.fragment_lists_sliding);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		bt_zhaoshifu.setBackgroundResource(R.drawable.zhaoshifu);
//		bt_fabu.setBackgroundResource(R.drawable.fabu);
//		bt_zhaowenti.setBackgroundResource(R.drawable.wenti);
//		bt_my.setBackgroundResource(R.drawable.my);

//		zhaoshifuTV.setTextColor(getResources().getColor(R.color.black));
//		fabuTV.setTextColor(getResources().getColor(R.color.black));
//		wentiTV.setTextColor(getResources().getColor(R.color.black));
//		myTV.setTextColor(getResources().getColor(R.color.black));

    }

    // 重写Activity中onKeyDown（）方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 当keyCode等于退出事件值时
            ToQuitTheApp();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    // 封装ToQuitTheApp方法
    private void ToQuitTheApp() {
        if (isExit) {
            // ACTION_MAIN with category CATEGORY_HOME 启动主屏幕
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);// 使虚拟机停止运行并退出程序
        } else {
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出APP", Toast.LENGTH_SHORT)
                    .show();
            mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后发送消息
        }
    }

    // 创建Handler对象，用来处理消息
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {// 处理消息
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        // TODO 自动生成的方法存根
        Toast.makeText(this, "do search " + query, 0).show();
        this.query = query;
        ListMainPageQuetionsShow.listItems.clear();// 清除问题数据
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("c_id", ListMainPageQuetionsShow.c_id);//类型id
        params.put("isSolved", false);//是否解决
        params.put("isNear",isNear );//是否解决
        if (isNear) {
            params.put("myLat", myLat);//当前纬度
            params.put("myLng", myLng);//当前经度
        }
        params.put("filter", query);//搜索关键字
        params.put("sortName", "p_t");//排序字段
        params.put("isAsc", true);//升序
        params.put("pageNum", ListMainPageQuetionsShow.pageNum);
        params.put("pageSize", ListMainPageQuetionsShow.pageSize);
        // HomeCtrl.do：为访问服务器HomeCtrl类，action=photo：访问方法photo
        SendRequestUtil.post("HomeCtrl/photo", params,
                ListMainPageQuetionsShow.readtaskListHandler);

    }

    public void locationAddress() {

        // 定位初始化

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null)
                return;

            adrress = location.getAddrStr();
            myLat = location.getLatitude();// 纬度myLat
            myLng = location.getLongitude();// 经度myLng

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}
