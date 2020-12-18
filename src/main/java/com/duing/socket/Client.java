package com.duing.socket;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1",9999);

        OutputStream fos = socket.getOutputStream();

        String str = "加油，中国！";

        fos.write(str.getBytes());
        socket.close();
    }
}
