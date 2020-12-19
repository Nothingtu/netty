package com.duing.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 利用的nio技术的server
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        System.out.println("===============服务端已启动================");
        //先获取serverSocketChannel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //给serverSocketChannel绑定端口号和ip地址
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 9999);
        serverSocketChannel.socket().bind(socketAddress);

        //将serverSocketChannel通道设置为非阻塞的
        serverSocketChannel.configureBlocking(false);

        //打开一个选择器
        Selector selector = Selector.open();

        //将通道注册到选择器中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //遍历选择器中是否有选择器所关注的事情
        while (true) {
            int ready = selector.select();
            if (ready == 0) continue;
            //表示有选择器所关注的事情发生

            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //先将事件移出，避免重复操作
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    //处理连接事件
                    System.out.println("客户端已连接");
                    //通过服务端的通道，获取客户端的通道
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_WRITE);

                } else if (selectionKey.isWritable()) {
                    //处理写事件
                    //通过事件获取到通道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    //初始化分配一个128大小的Buffer
                    ByteBuffer writeBuffer = ByteBuffer.allocate(128);
                    String message = "hello from server";
                    writeBuffer.put(message.getBytes());
                    writeBuffer.flip();
                    socketChannel.write(writeBuffer);

                    selectionKey.interestOps(selectionKey.OP_READ);

                } else if (selectionKey.isReadable()) {
                    System.out.println("开始读了");
                    //处理读事件
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    ByteBuffer readBuffer = ByteBuffer.allocate(128);
                    int count = socketChannel.read(readBuffer);
                    readBuffer.flip();
                    StringBuffer stringBuffer = new StringBuffer();

                    while (count > 0) {
                        while (readBuffer.hasRemaining()) {
                            stringBuffer.append((char) readBuffer.get());
                        }
                        System.out.println(stringBuffer.toString());
                    }
                    readBuffer.clear();
                } else if (selectionKey.isConnectable()) {

                }
            }

        }

    }

}
