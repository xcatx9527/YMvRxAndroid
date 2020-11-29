package com.xile.script.utils.script;

import com.alibaba.fastjson.JSONObject;
import com.chenyang.lloglib.LLog;
import com.xile.script.http.common.HttpConstants;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ConnectUtil {

    public static String httpTools(String urlStr, boolean isReadContent, Map<String, String> headerMap, String postContent) throws Exception {
        return httpTools(urlStr, isReadContent, headerMap, postContent, false, null, 0, false);
    }

    public static String httpTools(String urlStr, boolean isReadContent, Map<String, String> headerMap, String postContent, boolean isLog) throws Exception {
        return httpTools(urlStr, isReadContent, headerMap, postContent, false, null, 0, isLog);
    }

    public static String httpTools(String urlStr, boolean isReadContent, Map<String, String> headerMap, String postContent, boolean isProxy, String curIPAddr, int curIPPort, boolean isLog) throws Exception {
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(urlStr);

            if (isProxy && curIPAddr != null && curIPAddr.isEmpty() == false) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(curIPAddr, curIPPort));
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }


            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            if (postContent != null) {
                conn.setRequestMethod("POST");
            } else {
                conn.setRequestMethod("GET");
            }
            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    conn.setRequestProperty(key, headerMap.get(key));
                }
            }

            if (postContent != null) {
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                printWriter.write(postContent);//post的参数 xx=xx&yy=yy
                // flush输出流的缓冲
                printWriter.flush();
            }

            int rtnCode = conn.getResponseCode();

            if (rtnCode == 200) {
                if (isReadContent == true) {
                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while (-1 != (len = is.read(buffer))) {
                        if (isLog) {
                            System.out.println("baos==" + new String(buffer));
                        }
                        baos.write(buffer, 0, len);
                        baos.flush();
                    }
                    String res = baos.toString("utf-8");
                    if (isLog) {
                        System.out.println("res:" + res);
                    }
                    return res;
                }
                return "suc";
            } else if (rtnCode == 302) {
                String locationUrl = conn.getHeaderField("Location");
                LLog.i("locationUrl:" + locationUrl);
                String res = httpTools(locationUrl, true, headerMap, postContent, isProxy, curIPAddr, curIPPort, false);
                return res;
            } else {
                LLog.i("rtnCode:" + rtnCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static String function(String url, String reqType, String osType) {
        boolean isProxy = true;
        String curIPAddr = null;
        int curIPPort = 0;
        while (isProxy) {
            try {
                String vpnGetUrl = HttpConstants.GET_IP;
                String res = httpTools(vpnGetUrl, true, null, null);

                if (res == null) {
                    Thread.sleep(3000);
                    continue;
                }
                JSONObject object = null;
                try {
                    object = JSONObject.parseObject(res);
                } catch (Exception e) {
                }

                //System.out.println("vpn res:"+res);
                if (object == null) {
                    Thread.sleep(3000);
                    continue;
                }
                int ERRORCODE = object.getIntValue("code");
                if (ERRORCODE != 0) {
                    Thread.sleep(3000);
                    continue;
                }

                curIPAddr = object.getString("ip");
                String tempCurIPPort = object.getString("port");
                curIPPort = Integer.valueOf(tempCurIPPort);
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }

        LLog.i("[curIPAddr:" + curIPAddr + "][curIPPort:" + curIPPort + "]");

        String res = null;
        try {
            String userAgentIOS = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Version/10.0 Mobile/14D27 Safari/602.1";
            String userAgentAnd = "Mozilla/5.0 (Linux; U; Android 5.0; zh-CN; Lenovo A3900 Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 OPR/12.11.0.1 Mobile Safari/537.36";
            String userAgentWIN = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36";
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            headerMap.put("Accept-Encoding", "gzip, deflate");
            headerMap.put("Accept-Language", "zh-CN,zh;q=0.89");
            headerMap.put("Connection", "keep-alive");
            if ("iOS".equals(osType)) {
                headerMap.put("User-Agent", userAgentIOS);
            } else {
                headerMap.put("User-Agent", userAgentAnd);
            }
            if ("POST".equals(reqType)) {
                res = httpTools(url, true, headerMap, "", isProxy, curIPAddr, curIPPort, false);
            } else {
                res = httpTools(url, true, headerMap, null, isProxy, curIPAddr, curIPPort, false);
            }
            LLog.i("res=" + res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

}
