package com.dmw.server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class UpdAppender extends AppenderBase<ILoggingEvent> {

    PatternLayoutEncoder encoder;
    int port;
    String ip;
    DatagramSocket socket;


    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }
    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }
    public UpdAppender() {
              //this.ip="127.0.0.1";
              //this.port=5070;
    }
    @Override
    public void start() {
        if (this.encoder == null) {
            addError("no layout of udp appender");
            return;
        }
        if (socket==null) {
            try {
                 socket = new DatagramSocket(30001);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        super.start();
    }
    @Override
    protected void append(ILoggingEvent event) {
        byte[] buf = encoder.getLayout().doLayout(event).trim().getBytes();
        try {
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket p = new DatagramPacket(buf, buf.length, address, port);
            socket.send(p);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void stop() {
        if (!socket.isClosed()) {
            socket.close();
        }
        super.stop();
    }

}