package com.leaf.collegeidleapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.leaf.collegeidleapp.adapter.AllCommodityAdapter;
import com.leaf.collegeidleapp.bean.Commodity;
import com.leaf.collegeidleapp.util.CommodityDbHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.leaf.collegeidleapp.adapter.AllCommodityAdapter;
import com.leaf.collegeidleapp.bean.Commodity;
import com.leaf.collegeidleapp.util.CommodityDbHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * 主界面活动类
 * 修改说明：
 * 1. 添加自动刷新机制
 * 2. 优化数据库操作
 * 3. 修复列表刷新问题
 */
public class MainActivity extends AppCompatActivity {

    // 常量定义
    private static final int REQUEST_ADD_COMMODITY = 1001;

    // 控件声明
    private ListView lvAllCommodity;
    private ImageButton ibLearning, ibElectronic, ibDaily, ibSports;
    private TextView tvStuNumber;

    // 数据相关
    private List<Commodity> allCommodities = new ArrayList<>();
    private CommodityDbHelper dbHelper;
    private AllCommodityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化组件
        initViews();

        // 初始化数据
        initData();

        // 设置事件监听
        setupEventListeners();
    }

    /**
     * 初始化视图组件
     */
    private void initViews() {
        lvAllCommodity = findViewById(R.id.lv_all_commodity);
        tvStuNumber = findViewById(R.id.tv_student_number);
        ibLearning = findViewById(R.id.ib_learning_use);
        ibElectronic = findViewById(R.id.ib_electric_product);
        ibDaily = findViewById(R.id.ib_daily_use);
        ibSports = findViewById(R.id.ib_sports_good);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        dbHelper = new CommodityDbHelper(getApplicationContext(), CommodityDbHelper.DB_NAME, null, 1);

        // 初始化适配器
        adapter = new AllCommodityAdapter(getApplicationContext());
        lvAllCommodity.setAdapter(adapter);

        // 显示用户信息
        showUserInfo();

        // 加载初始数据
        refreshData();
    }

    /**
     * 显示登录用户信息
     */
    private void showUserInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String username = bundle.getString("username");
            tvStuNumber.setText("欢迎" + username + ",你好!");
        }
    }

    /**
     * 设置事件监听
     */
    private void setupEventListeners() {
        // 发布商品按钮
        ImageButton btnAdd = findViewById(R.id.ib_add_product);
        btnAdd.setOnClickListener(v -> navigateToAddCommodity());

        // 刷新按钮
        TextView tvRefresh = findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(v -> refreshData());

        // 分类按钮
        setupCategoryButtons();
    }

    /**
     * 跳转到添加商品界面
     */
    private void navigateToAddCommodity() {
        Intent intent = new Intent(this, AddCommodityActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", extractStudentId());
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_ADD_COMMODITY);
    }

    /**
     * 从欢迎语中提取学号
     */
    private String extractStudentId() {
        String welcomeText = tvStuNumber.getText().toString();
        return welcomeText.substring(2, welcomeText.length() - 4);
    }

    /**
     * 处理分类按钮点击
     */
    private void setupCategoryButtons() {
        final Bundle bundle = new Bundle();

        ibLearning.setOnClickListener(v -> {
            bundle.putInt("status", 1);
            navigateToCategory(bundle);
        });

        ibElectronic.setOnClickListener(v -> {
            bundle.putInt("status", 2);
            navigateToCategory(bundle);
        });

        ibDaily.setOnClickListener(v -> {
            bundle.putInt("status", 3);
            navigateToCategory(bundle);
        });

        ibSports.setOnClickListener(v -> {
            bundle.putInt("status", 4);
            navigateToCategory(bundle);
        });
    }

    /**
     * 跳转到分类界面
     */
    private void navigateToCategory(Bundle bundle) {
        Intent intent = new Intent(this, CommodityTypeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        new Thread(() -> {
            // 在子线程执行数据库操作
            List<Commodity> newData = dbHelper.readAllCommodities();

            runOnUiThread(() -> {
                // 更新UI线程
                allCommodities.clear();
                allCommodities.addAll(newData);
                adapter.setData(allCommodities);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_COMMODITY && resultCode == RESULT_OK) {
            refreshData(); // 添加商品成功后自动刷新
        }
    }
}