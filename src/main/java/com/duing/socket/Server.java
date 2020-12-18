package com.duing.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.Executors.newCachedThreadPool;

//服务端
public class Server {

    public static void main(String[] args) throws Exception {
        System.out.println("===============服务端已启动===============");

        ServerSocket server = new ServerSocket(9999);

        //创建缓冲线程池
        ExecutorService pool = Executors.newCachedThreadPool();

        //开启服务，等待客户端的连接
        while(true){
            //阻塞接受的方式
            final Socket socket = server.accept();
            pool.execute(new Runnable() {
                public void run() {
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void handler(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();

        byte[] bytes = new byte[1024];

        int count = is.read(bytes);

        while(count != -1){
            System.out.println(new String(bytes,0,count));
            count = is.read(bytes);
        }
        socket.close();
    }
}
