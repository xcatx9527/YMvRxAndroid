package com.xile.script.http.helper.brush.mina;


import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * date: 2017/5/23 10:42
 *
 * @scene B_MINA Scoket客户端
 */
public class BIMSocketClient {

    private String b_serverIP;
    private int b_serverPort;
    public static BIMSocketClientIOHandler mBIMSocketClientIOHandler;
    public static NioSocketConnector mBconnector = null;


    public BIMSocketClient(String ip, int port) {
        b_serverIP = ip;
        b_serverPort = port;
    }

    public ConnectFuture socketConnect() {
        if (mBconnector != null) {
            try {
                mBconnector.dispose();
            } catch (Throwable te) {

            }
        }

        try {
            mBconnector = new NioSocketConnector();
            mBconnector.getSessionConfig().setKeepAlive(true);
            mBconnector.setConnectTimeoutMillis(5 * 60 * 1000);

            //ObjectSerializationCodecFactory factory = new ObjectSerializationCodecFactory();
            //factory.setDecoderMaxObjectSize(Integer.MAX_VALUE);
            //factory.setEncoderMaxObjectSize(Integer.MAX_VALUE);
            //mBconnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));

            TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
            factory.setDecoderMaxLineLength(Integer.MAX_VALUE);
            factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
            mBconnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));

            //ObjectSerializationCodecFactory factory = new ObjectSerializationCodecFactory();
            //factory.setDecoderMaxObjectSize(Integer.MAX_VALUE);// 设定后服务器可以接收大数据
            //factory.setEncoderMaxObjectSize(Integer.MAX_VALUE);
            //mBconnector.getFilterChain().addLast("ImObjectFilter", new ProtocolCodecFilter(factory));


            mBIMSocketClientIOHandler = new BIMSocketClientIOHandler();
            mBconnector.setHandler(mBIMSocketClientIOHandler);
            ConnectFuture future = mBconnector.connect(new InetSocketAddress(b_serverIP, b_serverPort));
            future.awaitUninterruptibly();
            return future;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void destroy() {
        if (mBconnector != null) {
            try {
                mBconnector.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mBconnector = null;
    }

    public BIMSocketClientIOHandler getBIMSocketClientIOHandler() {
        return mBIMSocketClientIOHandler;
    }

    public void setBIMSocketClientIOHandler(
            BIMSocketClientIOHandler BIMSocketClientIOHandler) {
        mBIMSocketClientIOHandler = BIMSocketClientIOHandler;
    }
}
