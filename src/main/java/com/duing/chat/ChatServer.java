package com.duing.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 用这个类来模拟群聊系统（服务端）
 */
public class ChatServer {
    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    //在构造方法里初始化成员变量
    public ChatServer() {
        try {
            //打开一个选择器
            selector = Selector.open();

            //打开一个服务端的通道，并设置相关的参数状态
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(9898));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设计一个方法用来监听，是否有事件发生
    public void listen() throws IOException {
        while (true) {
            int count = selector.select();
            if (count > 0) {//表示有事件发生
                Set<SelectionKey> selectionKey = selector.selectedKeys();

                //遍历Set<SelectionKey>中的事件
                Iterator<SelectionKey> it = selectionKey.iterator();
                if (it.hasNext()) {
                    SelectionKey key = it.next();
                    //将当前的key移出，避免重复操作
                    it.remove();

                    if (key.isAcceptable()) {
                        //通过服务端的serverSocketChannel获取客户端的通道
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        //将客户端的通道设置为非阻塞的
                        socketChannel.configureBlocking(false);
                        //将客户端的通道注册到selector中
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        //通知是谁上线了
                        System.out.println("data : 用户"+ socketChannel.socket().getRemoteSocketAddress() + "上线啦！");
                    } else if (key.isReadable()) {
                        //调用读取信息的方法
                        readData(key);
                    }
                }
            }
        }
    }

    //读取信息的方法
    public void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            //先获取客户端的通道
            socketChannel = (SocketChannel) key.channel();
            //给readBuffer分配1024的大小
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(readBuffer);
            readBuffer.flip();
            StringBuffer stringBuffer = new StringBuffer();
            while(readBuffer.hasRemaining()) {
                stringBuffer.append((char)readBuffer.get());
            }
            System.out.println(stringBuffer.toString());
            writeData(stringBuffer.toString(),socketChannel);
        } catch (IOException e) {
            //当socketChannel为空时，表示用户下线了
            System.out.println("data : 用户"+ socketChannel.socket().getRemoteSocketAddress()+ "下线啦！");
            key.cancel();
        }

    }

    //将接受到的信息的发给别人
    public void writeData(String msg,SocketChannel selfSocketChannel) {
        //通过选择器获取所有的selectionKey
        Set<SelectionKey> selectionKeys = selector.keys();
        //遍历selectionKeys
        for(SelectionKey selectionKey : selectionKeys){
                //通过selectionKey获取对应的通道
                Channel channel =  selectionKey.channel();
                if(channel instanceof SocketChannel && channel != selfSocketChannel){
                    SocketChannel socketChannel = (SocketChannel)channel;
                    //发送消息
                    ByteBuffer writeBuffer = ByteBuffer.wrap(msg.getBytes());
                    try {
                        socketChannel.write(writeBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
//                    writeBuffer.put(msg.getBytes());
//                    writeBuffer.flip();
//                    try {
//                        socketChannel.write(writeBuffer);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

        }
    }

    public static void main(String[] args){
        ChatServer chatServer = new ChatServer();
        try {
            chatServer.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
