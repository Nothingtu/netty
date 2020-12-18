package com.duing.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Test {

    public static void main(String[] args){
        FileChannel channel = null;
        FileOutputStream fos = null;

        File file = new File("nio.txt");
        if( ! file.exists()) {
            try {
                file.createNewFile();

                //向nio.txt中写入内容
                fos = new FileOutputStream(file);

                channel = fos.getChannel();

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                String hello = "hello my nio";
                byteBuffer.put(hello.getBytes());

                byteBuffer.flip();

                channel.write(byteBuffer);

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try{
                    if(fos != null) fos.close();
                    if(channel != null) channel.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


    }
}
