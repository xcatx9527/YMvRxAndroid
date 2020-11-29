package com.xile.script.utils.script;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.chenyang.lloglib.LLog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.config.ExecEnum;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.http.common.GetHttpRequest;
import com.xile.script.http.common.HttpConstants;
import com.xile.script.http.common.JsonCallback;
import com.xile.script.http.helper.brush.mina.BIMSocketClient;
import com.xile.script.http.helper.brush.mina.BOrderStateConfig;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.http.helper.brush.mina.BrushTask;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.traffic.StatisticTrafficManager;
import com.zhima.proxy.ZMProxy;
import com.zhima.proxy.interfaces.ILineCallback;
import com.zhima.proxy.interfaces.IStateListener;
import com.zhima.proxy.interfaces.ProxyStatus;
import com.zhima.proxy.model.ErrorInfo;
import com.zhima.proxy.model.LineBean;
import okhttp3.Call;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//import com.zhima.proxy.core.SoError;
//import com.zhima.proxy.core.SoListener;
//import com.zhima.proxy.core.SoState;

/**
 * @descript 芝麻代理
 */

public class ZimaVpnUtil {
    private static ZimaVpnUtil instance;

    private static MyHandle vpnHandler = null;
    private String vpnState = "";
    private long lastRunningTime = 0;
    private String currentIp = "";
    private String currentCity = "";
    public Object[] sCity;
    public Object[] sCityCode;
    //public  List<String> sCities = new ArrayList<>();
    public ConcurrentHashMap<String, String> sCities = new ConcurrentHashMap<>();

    private ZimaVpnUtil() {
    }

    public static synchronized ZimaVpnUtil getInstance() {
        if (instance == null) {
            instance = new ZimaVpnUtil();
        }
        return instance;
    }

    public void initCity() {
        if (Constants.sBrushOrderInfo != null && Constants.sBrushOrderInfo.getVpnList() != null && Constants.sBrushOrderInfo.getVpnList().size() > 0) {
            LLog.i(Constants.sBrushOrderInfo.getVpnList().toString());
            List<String> keys = new ArrayList<>();
            List<String> values = new ArrayList<>();
            Set<String> set = Constants.sBrushOrderInfo.getVpnList().keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                keys.add(key);
                String value = Constants.sBrushOrderInfo.getVpnList().get(key);
                values.add(value);
            }
            sCity = keys.toArray();
            sCityCode = values.toArray();
        } else {
            sCity = new String[]{"烟台", "绍兴", "莱芜", "金华", "乐山", "铜陵", "辽阳", "鹤壁", "合肥", "宁波", "济南", "滨州", "益阳", "南通", "宿迁", "景德镇", "泸州", "扬州", "泰州", "衢州", "吉安", "镇江", "池州", "日照", "温州", "池州", "威海", "徐州", "常州", "珠海", "鞍山", "泰安", "忻州", "阜新", "宣城", "金华", "盐城", "铁岭", "枣庄", "晋城", "新余", "南通", "淮安", "雅安", "沈阳", "日照", "丽水", "景德镇", "九江", "三明", "舟山", "南昌", "绍兴", "滨州", "庆阳", "合肥", "重庆", "重庆", "德阳", "新余", "广州", "徐州", "徐州", "宁德", "池州", "泰州", "呼和浩特", "盘锦", "鄂尔多斯", "淮安", "萍乡", "鹰潭", "镇江", "阜阳", "珠海", "南平", "泰安", "南通", "南通", "徐州", "漳州", "马鞍山", "杭州", "保定", "鹤壁", "潍坊", "宿迁", "眉山", "湖州", "自贡", "鹤壁", "金华", "济南", "宁波", "衢州", "鹤壁", "台州", "怒江", "怒江", "宣城", "菏泽", "阜阳", "赣州", "曲靖", "曲靖", "厦门", "临沧", "临沧", "萍乡", "北京", "分宜", "阜阳", "湛江", "铜陵", "铜陵", "铜陵", "上海", "南京", "芜湖", "徐州", "蚌埠", "珠海", "嘉兴", "黄山", "常州", "扬州", "苏州", "德阳", "南昌", "德阳", "扬州", "忻州", "萍乡", "文山", "普洱", "赣州", "分宜", "德宏", "岳阳", "楚雄", "菏泽", "辽阳", "绍兴", "宣城", "丽江", "丽江", "丽江", "丽江", "金华", "滨州", "滨州", "盐城", "嘉兴", "威海", "威海", "嘉兴", "淄博", "淄博", "泰州", "金华", "台州", "台州", "芜湖", "玉溪", "普洱", "昭通", "曲靖", "玉溪", "盐城", "盐城", "亳州", "无锡", "南通", "丽水", "镇江", "镇江", "无锡", "杭州", "杭州", "洛阳", "金华", "池州", "常州", "马鞍山", "保山", "温州", "温州", "苏州", "南平", "上饶", "上饶", "丹东", "宁波", "宁波", "宁波", "六安", "温州", "池州", "宁德", "台州", "厦门", "宿迁", "嘉兴", "抚顺", "抚顺", "合肥", "宁波", "淮安", "南通", "常州", "常州", "淮安", "淮安", "台州", "台州", "泰州", "淮安", "阜阳", "台州", "本溪"};
            //Collections.addAll(sCities, city);
            sCityCode = new String[]{"dx370600-1", "lt330600-1", "lt371200-1", "lt330700-1", "lt511100-1", "dx340700-1", "lt211000-1", "lt410600-1", "lt340100-1", "lt330200-1", "dx370100-1", "dx371600-1", "dx430900-1", "dx320600-2", "dx321300-1", "dx360200-2", "lt510501-1", "dx321000-1", "dx321200-1", "dx330800-1", "dx360800-1", "dx321100-1", "lt341700-1", "dx371100-1", "dx330300-1", "dx341700-1", "dx371000-1", "dx320300-1", "dx320400-1", "dx440400-3", "lt210300-1", "dx370900-1", "lt140900-1", "lt210900-1", "lt341800-1", "dx330700-1", "dx320900-1", "lt211200-1", "lt370400-1", "dx140500-1", "dx360500-1", "dx320600-1", "dx320800-1", "lt511800-1", "lt210100-1", "dx371100-2", "dx331100-1", "dx360200-1", "dx360400-1", "lt350400-1", "dx330900-1", "dx360100-1", "dx330600-1", "lt371600-1", "lt621000-1", "dx340100-1", "dx500300-1", "dx500300-2", "lt510600-1", "dx360500-2", "dx440100-1", "dx320300-2", "dx320300-3", "lt350900-1", "lt341700-2", "dx321200-2", "lt150100-1", "lt211100-1", "lt150600-1", "lt320800-1", "dx360300-1", "dx360600-1", "lt321100-1", "lt341200-1", "dx440400-2", "lt350700-1", "dx370900-2", "dx320600-3", "dx320600-4", "lt320300-1", "dx350600-1", "dx340500-1", "dx330100-1", "lt130600-1", "lt410600-2", "lt370700-1", "dx321300-2", "lt511400-1", "dx330500-1", "lt510300-1", "lt410600-3", "dx330700-2", "dx370100-2", "dx330200-1", "dx330800-2", "lt410600-4", "dx331000-3", "dx533300-1", "dx533300-2", "dx341800-1", "lt371700-1", "lt341200-3", "dx360700-1", "dx530300-1", "dx530300-2", "dx350200-1", "dx530900-1", "dx530900-2", "dx360300-2", "lt110105-1", "dx360521-1", "lt341200-2", "lt440800-1", "dx340700-3", "dx340700-2", "dx340700-4", "lt310112-1", "dx320100-1", "lt340200-1", "dx320300-4", "dx340300-1", "dx440400-1", "dx330400-1", "dx341000-2", "dx320400-2", "dx321000-3", "dx320500-1", "lt510600-2", "dx360100-3", "dx510600-1", "dx321000-2", "lt140900-2", "dx360300-3", "dx532600-1", "dx530821-1", "dx360700-2", "dx360521-2", "dx533100-1", "lt430600-1", "dx532301-1", "lt371700-2", "lt211000-2", "dx330600-3", "dx341800-2", "dx530700-1", "dx530700-2", "dx530700-3", "dx530700-4", "dx330700-3", "lt371600-2", "lt371600-3", "dx320900-2", "dx330400-2", "dx371000-2", "dx371000-3", "dx330400-3", "dx370300-1", "dx370300-2", "dx321200-3", "dx330700-4", "dx331000-1", "dx331000-2", "dx340200-1", "dx530400-1", "dx530821-2", "dx530600-1", "dx530300-3", "dx530400-2", "dx320900-3", "dx320900-4", "dx341600-1", "dx320200-1", "dx320600-5", "dx331100-3", "dx321100-2", "dx321100-3", "dx320200-2", "dx330100-2", "dx330100-3", "dx410300-1", "dx330700-5", "dx341700-2", "dx320400-3", "dx340500-2", "dx530500-1", "dx330300-2", "dx330300-3", "dx320500-2", "dx350700-1", "dx361100-1", "dx361100-2", "dx210600-1", "dx330200-5", "dx330200-3", "dx330200-4", "dx341500-1", "dx330300-4", "dx341700-3", "dx350900-1", "lt331000-1", "dx350200-2", "dx321300-3", "dx330400-4", "lt210400-2", "lt210400-1", "dx340100-2", "dx330200-6", "dx320800-2", "lt320600-1", "dx320400-4", "dx320400-5", "lt320800-2", "lt320800-3", "dx331000-4", "dx331000-5", "dx321200-4", "dx320800-3", "dx341200-1", "lt331000-2", "lt210500-1"};
        }
        for (int i = 0; i < sCity.length; i++) {
            sCities.put((String) sCity[i], (String) sCityCode[i]);
        }
    }

    public void setCity(String[] city, String[] cityCode) {
        sCity = city.clone();
        sCityCode = cityCode.clone();
    }

    public void sendMsg(int type, long delayTime) {
        long time = System.currentTimeMillis();
        Message msg = vpnHandler.obtainMessage();
        msg.what = type;
        msg.obj = time;
        vpnHandler.sendMessageDelayed(msg, delayTime);
    }

    private class MyHandle extends Handler {
        public MyHandle(Looper looper) {
            super(looper);
            initCity();
            vpnState();
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (Constants.PLAY_STATE == PlayEnum.START_PLAY) {
                switch (msg.what) {
                    case 1: //注册流程
                        int num = new Random().nextInt(sCity.length);
                        LLog.i("当前num:" + num);
                        String city = (String) sCity[num];
                        String cityCode = (String) sCityCode[num];
                        ZMProxy.getInstance().getServLineList(cityCode, 1 * 3600, new ILineCallback() {
                            @Override
                            public void showLineList(int code, String msg, List<LineBean> list) {
                                if (code == 0 && list != null && list.size() > 0) {
                                    LineBean lineBean = list.get(new Random().nextInt(list.size()));
                                    if (lineBean != null) {
                                        if (System.currentTimeMillis() - Constants.getOrderTime > 3 * 60 * 1000) {  //VPN切三分钟超时
                                            vpnFailure();
                                            return;
                                        }
                                        LLog.i("当前城市--> city:" + city + "  lineBean.getCity():" + lineBean.getCity());
                                        LLog.i("当前线路-->  :" + cityCode + "  lineBean.getRemoteIp():" + lineBean.getRemoteIp());
                                        if (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START) {
                                            if (Constants.sBrushOrderInfo != null) {
                                                if (System.currentTimeMillis() - Constants.getOrderTime > 5 * 60 * 1000) {  //VPN切三分钟超时
                                                    if ("连接成功".equals(vpnState)) {
                                                        return;
                                                    }
                                                    vpnFailure();
                                                    return;
                                                }
                                                try {

                                                    if ("连接成功".equals(vpnState)) {
                                                        return;
                                                    }
                                                    currentIp = lineBean.getRemoteIp();
                                                    currentCity = lineBean.getCity();
//                                                    sendMsg(5, 100);
                                                    String userName = Constants.sBrushOrderInfo.getVpnAccount();
                                                    String passWord = Constants.sBrushOrderInfo.getVpnPasswd();
                                                    LLog.i("服务器 VPN userName:" + userName);
                                                    LLog.i("服务器 VPN passWord:" + passWord);
                                                    ZMProxy.getInstance().startProxy(lineBean.getRemoteIp(), userName, passWord);
                                                    SpUtil.putKeyString(PlatformConfig.CURRENT_IP_INFO, city);//存当前IP所在的城市
                                                    SpUtil.putKeyString(PlatformConfig.CURRENT_REMOTE_IP_ADDRESS, lineBean.getRemoteIp());//存当前IP地址
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } else {
                                            if ("连接成功".equals(vpnState)) {
                                                return;
                                            }
                                            try {

                                                currentIp = lineBean.getRemoteIp();
                                                currentCity = lineBean.getCity();
//                                                sendMsg(5, 100);
                                                String userName = SpUtil.getKeyString(PlatformConfig.VPN_USERNAME, "");
                                                String passWord = SpUtil.getKeyString(PlatformConfig.VPN_PASSWORD, "");
                                                LLog.i("本地测试 VPN userName:" + userName);
                                                LLog.i("本地测试 VPN passWord:" + passWord);
                                                ZMProxy.getInstance().startProxy(lineBean.getRemoteIp(), userName, passWord);
                                                SpUtil.putKeyString(PlatformConfig.CURRENT_IP_INFO, city);//存当前IP所在的城市
                                                SpUtil.putKeyString(PlatformConfig.CURRENT_REMOTE_IP_ADDRESS, lineBean.getRemoteIp());//存当前IP地址
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    LLog.e("vpn failure :" + msg + "  currentCity:" + city + "  cityCode:" + cityCode);
                                }
                            }
                        });

//                        ZMProxy.getInstance().getLineList(new ILineCallback() {
//                            @Override
//                            public void showLineList(int code, String s, List<LineBean> list) {
//                                if (code == 0) {    // 线路列表获取成功
//                                    List<LineBean> listLineBean = new ArrayList<LineBean>();
//                                    for (int i = 0; i < list.size(); i++) {
//                                        LineBean lineBean = list.get(i);
//                                        if (!lineBean.getCity().equals("混拨") && sCities.keySet().contains(lineBean.getCity())) {
//                                            listLineBean.add(lineBean);
//                                        }
//                                    }
//                                    if (listLineBean.size() > 0) {
//                                        Random random = new Random();
//
//                                        int num = random.nextInt(listLineBean.size());
//                                        LLog.i("当前num:" + num);
//                                        LineBean lineBean = listLineBean.get(num);
//                                        LLog.i("当前城市:" + lineBean.getCity());
//                                        LLog.i("当前线路:" + lineBean.getRemoteIp());
//                                        if (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START) {
//                                            if (Constants.sBrushOrderInfo != null) {
//                                                if (System.currentTimeMillis() - Constants.getOrderTime > 3 * 60 * 1000) {  //VPN切三分钟超时
//                                                    if ("连接成功".equals(vpnState)) {
//                                                        return;
//                                                    }
//                                                    vpnFailure();
//                                                    return;
//                                                }
//                                                String userName = Constants.sBrushOrderInfo.getVpnAccount();
//                                                String passWord = Constants.sBrushOrderInfo.getVpnPasswd();
//                                                if ("连接成功".equals(vpnState)) {
//                                                    return;
//                                                }
//                                                LLog.i("服务器 VPN userName:" + userName);
//                                                LLog.i("服务器 VPN passWord:" + passWord);
//                                                ZMProxy.getInstance().startProxy(lineBean.getRemoteIp(), userName, passWord);
//                                                SpUtil.putKeyString(PlatformConfig.CURRENT_IP_INFO, lineBean.getCity());//存当前IP所在的城市
//                                                SpUtil.putKeyString(PlatformConfig.CURRENT_REMOTE_IP_ADDRESS, lineBean.getRemoteIp());//存当前IP地址
//                                            }
//                                        } else {
//                                            if ("连接成功".equals(vpnState)) {
//                                                return;
//                                            }
//                                            String userName = SpUtil.getKeyString(PlatformConfig.VPN_USERNAME, "");
//                                            String passWord = SpUtil.getKeyString(PlatformConfig.VPN_PASSWORD, "");
//                                            LLog.i("本地测试 VPN userName:" + userName);
//                                            LLog.i("本地测试 VPN passWord:" + passWord);
//                                            ZMProxy.getInstance().startProxy(lineBean.getRemoteIp(), userName, passWord);
//                                            SpUtil.putKeyString(PlatformConfig.CURRENT_IP_INFO, lineBean.getCity());//存当前IP所在的城市
//                                            SpUtil.putKeyString(PlatformConfig.CURRENT_REMOTE_IP_ADDRESS, lineBean.getRemoteIp());//存当前IP地址
//                                        }
//                                    }
//                                }
//                            }
//                        });

                        break;
                    case 2:  //登录流程:
                        String tempCityLogin = (String) sCity[new Random().nextInt(sCity.length)];
                        String tempCityCodeLogin = sCities.get(tempCityLogin);
                        if (Constants.sBrushOrderInfo != null && Constants.sBrushOrderInfo.getUserModel() != null && !TextUtils.isEmpty(Constants.sBrushOrderInfo.getUserModel().getLoginIpInfo())) {
                            tempCityLogin = Constants.sBrushOrderInfo.getUserModel().getLoginIpInfo();
                            tempCityCodeLogin = sCities.get(tempCityLogin);
                        }
                        String cityLogin = tempCityLogin;
                        String cityCodeLogin = tempCityCodeLogin;
                        ZMProxy.getInstance().getServLineList(cityCodeLogin, 1 * 3600, new ILineCallback() {
                            @Override
                            public void showLineList(int code, String msg, List<LineBean> list) {
                                if (code == 0 && list != null && list.size() > 0) {
                                    LineBean lineTempBean = list.get(new Random().nextInt(list.size()));
                                    if (lineTempBean != null) {
                                        if (System.currentTimeMillis() - Constants.getOrderTime > 3 * 60 * 1000) {  //VPN切三分钟超时
                                            vpnFailure();
                                            return;
                                        }
                                        LLog.i("当前城市--> cityLogin:" + cityLogin + "  lineBean.getCity():" + lineTempBean.getCity());
                                        LLog.i("当前线路--> cityCodeLogin:" + cityCodeLogin + "  lineBean.getRemoteIp():" + lineTempBean.getRemoteIp());
                                        if (RecordFloatView.bigFloatState == RecordFloatView.EXEC && Constants.EXEC_STATE == ExecEnum.EXEC_START) {
                                            if (Constants.sBrushOrderInfo != null) {
                                                if (System.currentTimeMillis() - Constants.getOrderTime > 5 * 60 * 1000) {  //VPN切五分钟超时
                                                    if ("连接成功".equals(vpnState)) {
                                                        return;
                                                    }
                                                    vpnFailure();
                                                    return;
                                                }
                                                try {

                                                    if ("连接成功".equals(vpnState)) {
                                                        return;
                                                    }
                                                    currentIp = lineTempBean.getRemoteIp();
                                                    String userName = Constants.sBrushOrderInfo.getVpnAccount();
                                                    String passWord = Constants.sBrushOrderInfo.getVpnPasswd();
                                                    LLog.e("服务器 VPN userName:" + userName);
                                                    LLog.e("服务器 VPN passWord:" + passWord);

                                                    sendMsg(5, 100);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } else {
                                            if ("连接成功".equals(vpnState)) {
                                                return;
                                            }
                                            try {
                                                String userName = SpUtil.getKeyString(PlatformConfig.VPN_USERNAME, "");
                                                String passWord = SpUtil.getKeyString(PlatformConfig.VPN_PASSWORD, "");
                                                LLog.e("本地测试 VPN userName:" + userName);
                                                LLog.e("本地测试 VPN passWord:" + passWord);
                                                ZMProxy.getInstance().startProxy(lineTempBean.getRemoteIp(), userName, passWord);
                                                SpUtil.putKeyString(PlatformConfig.CURRENT_IP_INFO, cityLogin);//存当前IP所在的城市
                                                SpUtil.putKeyString(PlatformConfig.CURRENT_REMOTE_IP_ADDRESS, lineTempBean.getRemoteIp());//存当前IP地址
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    LLog.e("vpn failure :" + msg + "  cityLogin:" + cityLogin + "  cityCodeLogin:" + cityCodeLogin);
                                    vpnFailure();
                                    return;
                                }
                            }
                        });

                        break;
                    case 3:

                        initCity();
                        BrushOrderHelper.getInstance().vpnConnected = true;
                        break;

                    case 4:
                        long time = Long.valueOf(msg.obj.toString());
                        if (lastRunningTime > time) {
                            Log.e("", "lastRunningTime:" + lastRunningTime);
                            return;
                        }
                        lastRunningTime = System.currentTimeMillis();
                        if (!"连接成功".equals(vpnState)) {
                            if (Constants.sBrushOrderInfo == null || Constants.sBrushOrderInfo.getUserModel() == null || TextUtils.isEmpty(Constants.sBrushOrderInfo.getUserModel().getLoginIpInfo())) {
                                sendMsg(1, 1000);
                            } else {
                                sendMsg(2, 1000);
                            }
                            sendMsg(4, 15000);
                        }
                        break;

                    case 5:  // 检测ip是否重复
                        if (!TextUtils.isEmpty(currentIp)) {
                            checkIp(currentIp);
                        } else {
                            linkVpn();
                        }
                        break;
                    default:

                        break;
                }
            }
        }
    }

    /**
     * 进行连接VPN操作
     */
    public void linkVpn() {
        currentIp = "";
        BrushOrderHelper.getInstance().vpnConnected = false;
        if (vpnHandler == null) {
            vpnHandler = new MyHandle(Looper.getMainLooper());
        }
        closeVpn();
        sendMsg(4, 5000);
    }

    public void closeVpn() {
        try {
            ZMProxy.getInstance().stopProxy();
            vpnState = "连接关闭";
            RecordFloatView.updateMessage("芝麻VPN 关闭成功!");
            BIMSocketClient.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 杀死VPN相关所有的一切
     */
    public void vpnDestroy() {
        ZMProxy.getInstance().onDestroy();
    }

    /**
     * VPN链接失败，返回订单结果
     */
    public void vpnFailure() {
        vpnHandler.removeMessages(4);
        vpnHandler.removeMessages(1);
        vpnHandler.removeMessages(2);
        LLog.e("返回订单结果 芝麻vpn连接失败...");
        initCity();
        SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_TYPE, BOrderStateConfig.ORDER_VPN_FAIL);
        SpUtil.putKeyString(PlatformConfig.CURRENT_BRUSH_RESULT_ERRORDESC, "芝麻vpn连接失败");
        BrushTask.getInstance().sendMessage(BrushTask.B_MSG_TYPE_ORDER_RESULT, 1000);
        Constants.PLAY_STATE = PlayEnum.STOP_PLAY;
    }

    /**
     * 监听芝麻VPN连接状态
     */
    public void vpnState() {
        ZMProxy.getInstance().addStateListener(new IStateListener() {
            @Override
            public void onStatusChanged(ProxyStatus proxyStatus, ErrorInfo errorInfo) {
                LLog.e("ProxyStatus: " + proxyStatus + ", errorInfo: " + (errorInfo != null ? ("msg:" + errorInfo.getMsg() + " code:" + errorInfo.getCode()) : null));
                if (proxyStatus == ProxyStatus.CONNECTING) {
                    vpnState = "连接中";
                    LLog.i("芝麻VPN 连接中...");
                    RecordFloatView.updateMessage("芝麻VPN 连接中...");
                } else if (proxyStatus == ProxyStatus.CONNECTED) {//CONNECTING
                    vpnState = "连接成功";
                    LLog.i("芝麻VPN 连接成功!");
                    RecordFloatView.updateMessage("芝麻VPN 连接成功!");
                    StatisticTrafficManager.getInstance().startTimeData(10000);
                    vpnHandler.removeMessages(4);
                    vpnHandler.removeMessages(1);
                    vpnHandler.removeMessages(2);
                    if (Constants.sBrushOrderInfo != null) {
                        Constants.sBrushOrderInfo.getUserModel().setLoginIpInfo(SpUtil.getKeyString(PlatformConfig.CURRENT_IP_INFO, ""));
                    }
                    sendMsg(3, 100);
                } else if (proxyStatus == ProxyStatus.DISCONNECTED) {
                    vpnState = "断开连接";
                }
            }
        });

//        ZMProxy.getInstance().setProxyListener(new SoListener() {
//            @Override
//            public void proxyState(SoState soState, SoError soError) {
//                switch (soState) {
//                    case CONNECTING:
//                        LLog.e("连接中...");
//                        vpnState = "连接中";
//                        LLog.i("芝麻VPN 连接中...");
//                        RecordFloatView.updateMessage("芝麻VPN 连接中...");
//                        break;
//                    case CONNECTED:
//                        vpnState = "连接成功";
//                        LLog.i("芝麻VPN 连接成功!");
//                        RecordFloatView.updateMessage("芝麻VPN 连接成功!");
//                        StatisticTrafficManager.getInstance(ScriptApplication.getInstance()).startTimeData(10000);
//                        vpnHandler.removeMessages(4);
//                        vpnHandler.removeMessages(1);
//                        vpnHandler.removeMessages(2);
//                        if (Constants.sBrushOrderInfo != null) {
//                            Constants.sBrushOrderInfo.getUserModel().setLoginIpInfo(SpUtil.getKeyString(PlatformConfig.CURRENT_IP_INFO, ""));
//                        }
//                        sendMsg(3, 100);
//                        break;
//                    case DISCONNECTED:
//                        LLog.e("已断开");
//                        vpnState = "断开连接";
//                        break;
//                    case FAIILED:
//                        LLog.e("vpn连接失败");
//                        if (soError.getCode() != 0) {
//                            LLog.e("code: " + soError.getCode() + ", msg: " + soError.getMsg());
//                        }
//                        vpnFailure();
//                        break;
//                }
//            }
//        });


    }

    /**
     * VPN是否打开
     *
     * @return
     */
    public boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


    public void checkIp(String ip) {
        //LLog.i("流量 message    :  " + jsonObject.toString());

        GetHttpRequest getHttpRequest = new GetHttpRequest();
        Map<String, String> map = new HashMap<>();
        map.put("remoteIp", ip);
        map.put("orderId", Constants.sBrushOrderInfo.getOrderId());
        LLog.e("服务器 remoteIp:" + ip);
        getHttpRequest.get(HttpConstants.ORDER_CHECK_IP, map, new JsonCallback() {
            @Override
            public void onResponse(JSONObject response, int id) {
                super.onResponse(response, id);
                int code = (int) response.opt("code");
                if (code == 0) {
                    String userName = Constants.sBrushOrderInfo.getVpnAccount();
                    String passWord = Constants.sBrushOrderInfo.getVpnPasswd();
                    LLog.e("服务器 VPN userName:" + userName);
                    LLog.e("服务器 VPN passWord:" + passWord);
                    LLog.e("服务器 currentIp:" + currentIp);
                    ZMProxy.getInstance().startProxy(currentIp, userName, passWord);
                    SpUtil.putKeyString(PlatformConfig.CURRENT_IP_INFO, currentCity);//存当前IP所在的城市
                    SpUtil.putKeyString(PlatformConfig.CURRENT_REMOTE_IP_ADDRESS, currentIp);//存当前IP地址
                } else { //ip重复
                    linkVpn();
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                linkVpn();
            }
        });

    }
}
