package com.xile.script.http.helper.manager.mina;


import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * date: 2017/5/23 10:42
 *
 * @scene MINA Scoket客户端
 */
public class IMSocketClient {

    private String serverIP;
    private int serverPort;
    private IMSocketClientIOHandler imSocketClientIOHandler;
    public static NioSocketConnector connector = null;


    public IMSocketClient(String ip, int port) {
        serverIP = ip;
        serverPort = port;
    }

    public ConnectFuture socketConnect() {
        if (connector != null) {
            try {
                connector.dispose();
            } catch (Throwable te) {

            }
        }

        try {
            connector = new NioSocketConnector();
            connector.getSessionConfig().setKeepAlive(true);
            connector.setConnectTimeoutMillis(5 * 60 * 1000);

            TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
            factory.setDecoderMaxLineLength(Integer.MAX_VALUE);
            factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));

            //ObjectSerializationCodecFactory factory = new ObjectSerializationCodecFactory();
            //factory.setDecoderMaxObjectSize(Integer.MAX_VALUE);// 设定后服务器可以接收大数据
            //factory.setEncoderMaxObjectSize(Integer.MAX_VALUE);
            //connector.getFilterChain().addLast("ImObjectFilter", new ProtocolCodecFilter(factory));


            imSocketClientIOHandler = new IMSocketClientIOHandler();
            connector.setHandler(imSocketClientIOHandler);
            ConnectFuture future = connector.connect(new InetSocketAddress(serverIP, serverPort));
            future.awaitUninterruptibly();
            return future;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void destroy() {
        if (connector != null) {
            try {
                connector.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        connector = null;
    }

    public IMSocketClientIOHandler getImSocketClientIOHandler() {
        return imSocketClientIOHandler;
    }

    public void setImSocketClientIOHandler(
            IMSocketClientIOHandler imSocketClientIOHandler) {
        this.imSocketClientIOHandler = imSocketClientIOHandler;
    }
}
