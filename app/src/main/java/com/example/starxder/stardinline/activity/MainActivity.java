package com.example.starxder.stardinline.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.starxder.stardinline.Adapter.OrderAdapter;
import com.example.starxder.stardinline.Beans.Order;
import com.example.starxder.stardinline.R;
import com.example.starxder.stardinline.Utils.CommonUtil;
import com.example.starxder.stardinline.Utils.DateUtils;
import com.example.starxder.stardinline.Utils.PrintUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zj.btsdk.BluetoothService;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity implements View.OnClickListener {

    private List<Order> list_A = new ArrayList<Order>();
    private List<Order> list_B = new ArrayList<Order>();
    private List<Order> list_C = new ArrayList<Order>();
    private List<Order> list_all;
    private Button btn_A, btn_B, btn_C, btn_connect;
    public static final int SHOW_RESPONSE = 0;
    private OrderAdapter adapter_A;
    private OrderAdapter adapter_B;
    private OrderAdapter adapter_C;
    private ListView listView_A;
    private ListView listView_B;
    private ListView listView_C;
    BluetoothService mService = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;  //获取设备消息
    BluetoothDevice con_dev = null;
    private static final int REQUEST_ENABLE_BT = 2;
    String frontNum, myNum, nowNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = new BluetoothService(this, mHandler);
        //蓝牙不可用退出程序
        if (mService.isAvailable() == false) {
            Toast.makeText(this, "蓝牙未链接", Toast.LENGTH_LONG).show();
            finish();
        }


        btn_A = (Button) findViewById(R.id.btn_getA);
        btn_A.setOnClickListener(this);
        btn_B = (Button) findViewById(R.id.btn_getB);
        btn_B.setOnClickListener(this);
        btn_C = (Button) findViewById(R.id.btn_getC);
        btn_C.setOnClickListener(this);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(this);
//        ---------------------瞎编数据-----------------------
        sendRequestWithHttpUrlConnection();


        adapter_A = new OrderAdapter(MainActivity.this, R.layout.orderitem, list_A);
        adapter_B = new OrderAdapter(MainActivity.this, R.layout.orderitem, list_B);
        adapter_C = new OrderAdapter(MainActivity.this, R.layout.orderitem, list_C);

        //-----------------------------------------A-------------------------------------------
        listView_A = (ListView) findViewById(R.id.lv1);
        listView_A.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Order order = list_A.get(position);
                AlertDialog.Builder Item_alert = new AlertDialog.Builder(MainActivity.this);
                Item_alert.setTitle("点餐系统");
                Item_alert.setMessage("请选择您的操作");
                Item_alert.setCancelable(true);
                Item_alert.setPositiveButton("用餐", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定用餐执行方法，发送请求！
                        update(order.getOrdercode(), "a");


                    }
                });
                Item_alert.setNegativeButton("过号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定过号行方法，发送请求！
                        update(order.getOrdercode(), "b");
                    }
                });
                Item_alert.show();
            }
        });

        //-----------------------------------------B-------------------------------------------
        listView_B = (ListView) findViewById(R.id.lv2);
        listView_B.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Order order = list_B.get(position);
                AlertDialog.Builder Item_alert = new AlertDialog.Builder(MainActivity.this);
                Item_alert.setTitle("点餐系统");
                Item_alert.setMessage("请选择您的操作");
                Item_alert.setCancelable(true);
                Item_alert.setPositiveButton("用餐", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定用餐执行方法，发送请求！
                        update(order.getOrdercode(), "a");


                    }
                });
                Item_alert.setNegativeButton("过号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定过号行方法，发送请求！
                        update(order.getOrdercode(), "b");
                    }
                });
                Item_alert.show();
            }
        });


        //-----------------------------------------C-------------------------------------------
        listView_C = (ListView) findViewById(R.id.lv3);
        listView_C.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Order order = list_C.get(position);
                AlertDialog.Builder Item_alert = new AlertDialog.Builder(MainActivity.this);
                Item_alert.setTitle("点餐系统");
                Item_alert.setMessage("请选择您的操作");
                Item_alert.setCancelable(true);
                Item_alert.setPositiveButton("用餐", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定用餐执行方法，发送请求！
                        update(order.getOrdercode(), "a");


                    }
                });
                Item_alert.setNegativeButton("过号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定过号行方法，发送请求！
                        update(order.getOrdercode(), "b");
                    }
                });
                Item_alert.show();
            }
        });


        listView_A.setAdapter(adapter_A);


        listView_B.setAdapter(adapter_B);


        listView_C.setAdapter(adapter_C);


        cdt.start();
    }


    @Override
    public void onStart() {
        super.onStart();
        //蓝牙未打开，打开蓝牙
        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        }
        Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);      //运行另外一个类的活动
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }


    private void parseJSONWithJSONObject(String jsonData, String key1, String key2, String key3) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            frontNum = jsonObject.getString(key1);
            myNum = jsonObject.getString(key2);
            nowNum = jsonObject.getString(key3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getA:
                AlertDialog.Builder getA_alert = new AlertDialog.Builder(MainActivity.this);
                getA_alert.setTitle("点餐系统");
                getA_alert.setMessage("确定要取A类桌号吗？");
                getA_alert.setCancelable(true);
                getA_alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //确定取号执行方法，发送请求！


                        //首先创建请求的客户端对象
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60*1000, TimeUnit.SECONDS).build();
                        //使用Request.Builder来创建请求对象
                        Request.Builder builder = new Request.Builder();

                        //指定使用GET请求,并且指定要请求的地址
                        final Request request = builder.get().url(CommonUtil.BaseUrl +"/web-frame/order/insert.do?ordertype=A&gettype=pb").build();
                        //将请求加入请求队列,将请求封装成Call对象
                        Call call = client.newCall(request);
                        //使用异步的方式来得到请求的响应并且处理
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                //请求失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {

//                                "frontNum":1,
//                                        "myNum":"A16",
//                                        "nowNum":"A15"
                                //请求成功
                                //此处非UI线程
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String datas;
                                        try {
                                            datas = response.body().string();
                                            Log.d("点餐", datas);
                                            parseJSONWithJSONObject(datas, "frontNum", "myNum", "nowNum");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        //------------------------------------------------------------
                                        //开启打印
                                        //打印的信息
                                        StringBuffer buffer1 = new StringBuffer();
                                        StringBuffer buffer2 = new StringBuffer();
                                        try {
                                            String currTime = DateUtils.getStandardDate(System.currentTimeMillis()); //当前时间
                                            buffer1.append("取票时间：").append(currTime).append("\n\n");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String title = PrintUtils.printTitle("店名");
                                        buffer1.append(title).append("\n\n");
                                        String msg1 = PrintUtils.printThreeData("", "您的桌号为", "");
                                        buffer1.append(msg1).append("\n");

                                        String msg0 = buffer1.toString();

                                        mService.sendMessage(msg0 + "\n", "GBK");


                                        byte[] cmd = new byte[3];
                                        cmd[0] = 0x1b;//00011011
                                        cmd[1] = 0x21;//00100001
                                        cmd[2] |= 0x30;
                                        mService.write(cmd);
                                        mService.sendMessage("      " + myNum + "\n\n\n", "GBK");

                                        cmd[2] &= 000000000;
                                        mService.write(cmd);


                                        String msg10 = PrintUtils.printThreeData("", "前方还有" + frontNum + "桌", "");
                                        buffer2.append(msg10).append("\n\n");
                                        String msg11 = PrintUtils.printThreeData("", "目前已到" + nowNum + "桌", "");
                                        buffer2.append(msg11).append("\n\n");

                                        String msg = buffer2.toString();


                                        mService.sendMessage(msg + "\n", "GBK");

                                        //--------------------------------------------------------------------

                                        sendRequestWithHttpUrlConnection();
                                    }
                                });
                            }
                        });


                    }
                });
                getA_alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                getA_alert.show();

                break;

            case R.id.btn_getB:
                AlertDialog.Builder getB_alert = new AlertDialog.Builder(MainActivity.this);
                getB_alert.setTitle("点餐系统");
                getB_alert.setMessage("确定要取B类桌号吗？");
                getB_alert.setCancelable(true);
                getB_alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定取号执行方法，发送请求！
                        //首先创建请求的客户端对象
                        OkHttpClient client = new OkHttpClient();
                        //使用Request.Builder来创建请求对象
                        Request.Builder builder = new Request.Builder();
                        //指定使用GET请求,并且指定要请求的地址
                        Request request = builder.get().url(CommonUtil.BaseUrl +"/web-frame/order/insert.do?ordertype=B&gettype=pb").build();
                        //将请求加入请求队列,将请求封装成Call对象
                        Call call = client.newCall(request);
                        //使用异步的方式来得到请求的响应并且处理
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                //请求失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                //请求成功
                                //此处非UI线程
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String datas;
                                        try {
                                            datas = response.body().string();
                                            Log.d("点餐", datas);
                                            parseJSONWithJSONObject(datas, "frontNum", "myNum", "nowNum");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        //------------------------------------------------------------
                                        //开启打印
                                        //打印的信息
                                        StringBuffer buffer1 = new StringBuffer();
                                        StringBuffer buffer2 = new StringBuffer();
                                        try {
                                            String currTime = DateUtils.getStandardDate(System.currentTimeMillis()); //当前时间
                                            buffer1.append("取票时间：").append(currTime).append("\n\n");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String title = PrintUtils.printTitle("店名");
                                        buffer1.append(title).append("\n\n");
                                        String msg1 = PrintUtils.printThreeData("", "您的桌号为", "");
                                        buffer1.append(msg1).append("\n");

                                        String msg0 = buffer1.toString();

                                        mService.sendMessage(msg0 + "\n", "GBK");


                                        byte[] cmd = new byte[3];
                                        cmd[0] = 0x1b;//00011011
                                        cmd[1] = 0x21;//00100001
                                        cmd[2] |= 0x30;
                                        mService.write(cmd);
                                        mService.sendMessage("      " + myNum + "\n\n\n", "GBK");

                                        cmd[2] &= 000000000;
                                        mService.write(cmd);


                                        String msg10 = PrintUtils.printThreeData("", "前方还有" + frontNum + "桌", "");
                                        buffer2.append(msg10).append("\n\n");
                                        String msg11 = PrintUtils.printThreeData("", "目前已到" + nowNum + "桌", "");
                                        buffer2.append(msg11).append("\n\n");

                                        String msg = buffer2.toString();


                                        mService.sendMessage(msg + "\n", "GBK");
                                        //--------------------------------------------------------------------

                                        sendRequestWithHttpUrlConnection();
                                    }
                                });
                            }
                        });
                    }
                });
                getB_alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                getB_alert.show();


                break;

            case R.id.btn_getC:

                AlertDialog.Builder getC_alert = new AlertDialog.Builder(MainActivity.this);
                getC_alert.setTitle("点餐系统");
                getC_alert.setMessage("确定要取C类桌号吗？");
                getC_alert.setCancelable(true);
                getC_alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定取号执行方法，发送请求！

                        //首先创建请求的客户端对象
                        OkHttpClient client = new OkHttpClient();
                        //使用Request.Builder来创建请求对象
                        Request.Builder builder = new Request.Builder();
                        //指定使用GET请求,并且指定要请求的地址
                        Request request = builder.get().url(CommonUtil.BaseUrl +"/web-frame/order/insert.do?ordertype=C&gettype=pb").build();
                        //将请求加入请求队列,将请求封装成Call对象
                        Call call = client.newCall(request);
                        //使用异步的方式来得到请求的响应并且处理
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                //请求失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                //请求成功
                                //此处非UI线程
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String datas;
                                        try {
                                            datas = response.body().string();
                                            Log.d("点餐", datas);
                                            parseJSONWithJSONObject(datas, "frontNum", "myNum", "nowNum");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        //------------------------------------------------------------
                                        //开启打印
                                        //打印的信息
                                        StringBuffer buffer1 = new StringBuffer();
                                        StringBuffer buffer2 = new StringBuffer();
                                        try {
                                            String currTime = DateUtils.getStandardDate(System.currentTimeMillis()); //当前时间
                                            buffer1.append("取票时间：").append(currTime).append("\n\n");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String title = PrintUtils.printTitle("店名");
                                        buffer1.append(title).append("\n\n");
                                        String msg1 = PrintUtils.printThreeData("", "您的桌号为", "");
                                        buffer1.append(msg1).append("\n");

                                        String msg0 = buffer1.toString();

                                        mService.sendMessage(msg0 + "\n", "GBK");


                                        byte[] cmd = new byte[3];
                                        cmd[0] = 0x1b;//00011011
                                        cmd[1] = 0x21;//00100001
                                        cmd[2] |= 0x30;
                                        mService.write(cmd);
                                        mService.sendMessage("      " + myNum + "\n\n\n", "GBK");

                                        cmd[2] &= 000000000;
                                        mService.write(cmd);


                                        String msg10 = PrintUtils.printThreeData("", "前方还有" + frontNum + "桌", "");
                                        buffer2.append(msg10).append("\n\n");
                                        String msg11 = PrintUtils.printThreeData("", "目前已到" + nowNum + "桌", "");
                                        buffer2.append(msg11).append("\n\n");

                                        String msg = buffer2.toString();


                                        mService.sendMessage(msg + "\n", "GBK");
                                        //--------------------------------------------------------------------
                                        sendRequestWithHttpUrlConnection();

                                    }
                                });
                            }
                        });
                    }
                });
                getC_alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                getC_alert.show();


                break;

            case R.id.btn_connect:
                //搜索蓝牙设备
                Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);      //运行另外一个类的活动
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                //请求打开蓝牙
                if (resultCode == Activity.RESULT_OK) {
                    //蓝牙已经打开
                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                } else {
                    //用户不允许打开蓝牙
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                //请求连接某一蓝牙设备
                if (resultCode == Activity.RESULT_OK) {
                    //已点击搜索列表中的某个设备项
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    //获取列表项中设备的mac地址
                    con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);
                }
                break;
        }
    }


    //监听返回键点击事件
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            AlertDialog.Builder isExit = new AlertDialog.Builder(MainActivity.this);
            isExit.setTitle("点餐系统");
            isExit.setMessage("确定要退出吗？");
            isExit.setCancelable(false);
            isExit.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            isExit.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sendRequestWithHttpUrlConnection();

                }
            });
            isExit.show();
        } else {

        }
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    Log.d("response", response);
                    List<Order> list = getListOrderByGson(response);
                    list_all = getListOrderByGson(response);
                    //得到所有的Json数据
                    initData(list_all);

                    adapter_A.notifyDataSetChanged();
                    adapter_B.notifyDataSetChanged();
                    adapter_C.notifyDataSetChanged();

                    break;
            }

        }
    };

    private void update(String ordercode, String dealtype) {
        //确定取号执行方法，发送请求！

        //首先创建请求的客户端对象
        OkHttpClient client = new OkHttpClient();
        //使用Request.Builder来创建请求对象
        Request.Builder builder = new Request.Builder();
        //指定使用GET请求,并且指定要请求的地址
        Request request = builder.get().url(CommonUtil.BaseUrl +"/web-frame/order/update.do?ordercode=" + ordercode + "&&dealtype=" + dealtype).build();
        //将请求加入请求队列,将请求封装成Call对象
        Call call = client.newCall(request);
        //使用异步的方式来得到请求的响应并且处理
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //请求成功
                //此处非UI线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("点餐", response.body().toString());
                        sendRequestWithHttpUrlConnection();
                    }
                });
            }
        });

    }

    private void initData(List<Order> list) {
        list_A.clear();
        list_B.clear();
        list_C.clear();
        for (Order order : list) {
            if (order.getOrdertype().equals("A")) {
                list_A.add(order);
            } else if (order.getOrdertype().equals("B")) {
                list_B.add(order);
            } else if (order.getOrdertype().equals("C")) {
                list_C.add(order);
            }
        }
    }

    public List<Order> getListOrderByGson(String jsonString) {
        List<Order> list = new ArrayList<Order>();
        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<Order>>() {

        }.getType());
        return list;
    }


    private void sendRequestWithHttpUrlConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(CommonUtil.BaseUrl +"/web-frame/order/init.do?");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8 * 1000);
                    connection.setReadTimeout(8 * 1000);
                    InputStream in = connection.getInputStream();
                    //下面将对获取到的输入流进行读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    //将服务器返回的结果放到Message中
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 创建一个Handler实例，用于接收BluetoothService类返回回来的消息
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            //已连接
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            btn_connect.setBackgroundResource(R.mipmap.icon_connected);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            //正在连接
                            Log.i("Bluetooth", ".....is connecting");
                            break;
                        case BluetoothService.STATE_LISTEN:
                            //监听连接的到来
                        case BluetoothService.STATE_NONE:
                            Log.i("Bluetooth", ".....wait connecting");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    btn_connect.setBackgroundResource(R.mipmap.icon_printing);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };


    //定时器
    CountDownTimer cdt = new CountDownTimer(99999999, 30 * 1000) {
        @Override
        public void onTick(long l) {
            //刷新view
            sendRequestWithHttpUrlConnection();

        }

        @Override
        public void onFinish() {

        }
    };
}



