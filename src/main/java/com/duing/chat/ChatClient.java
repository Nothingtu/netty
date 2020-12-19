package com.duing.chat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * 用这个类来模拟群聊系统的客户端
 */
public class ChatClient {

    private Selector selector;
    private SocketChannel socketChannel;

    public ChatClient() {
        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9998));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("data : 用户" + socketChannel.socket().getLocalAddress() + "上线了");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //发送信息
    public void sendMessage(String message) {
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        try {
            writeBuffer.put(message.getBytes("UTF-8"));
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //接受信息
    public void readMessage() {
        try {
            int count = selector.select();
            if (count > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();

                while (it.hasNext()) {
                    SelectionKey selectionKey = it.next();
                    it.remove();
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(readBuffer);

//                        String msg = new String(readBuffer.array());
//                        System.out.println(msg);
                        readBuffer.flip();

                        StringBuffer stringBuffer = new StringBuffer();
                        while (readBuffer.hasRemaining()) {
                            stringBuffer.append((char) readBuffer.get());
                        }
                        System.out.println(stringBuffer.toString());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        System.out.println("客户端启动");
        new Thread() {
            public void run() {
                while (true) {
                    chatClient.readMessage();

                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            System.out.println("读取到的客户端信息："+msg);
            chatClient.sendMessage(msg);
        }
    }
}
