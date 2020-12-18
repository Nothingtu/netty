package com.duing.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 利用的nio技术的client
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        System.out.println("===============客户端已启动================");

        //在客户端，打开一个通道
        SocketChannel socketChannel = SocketChannel.open();

        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1",9999);
        socketChannel.connect(socketAddress);

        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
        String message = "hello from client";
        writeBuffer.put(message.getBytes());
        writeBuffer.flip();
        socketChannel.write(writeBuffer);


        ByteBuffer readBuffer = ByteBuffer.allocate(128);
        socketChannel.read(readBuffer);
        readBuffer.flip();

        StringBuffer stringBuffer = new StringBuffer();
        while(readBuffer.hasRemaining()){
            stringBuffer.append((char)readBuffer.get());
        }
        System.out.println(stringBuffer.toString());
        socketChannel.close();
    }
}
