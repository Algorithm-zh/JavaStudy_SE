package org.net;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

//UDP没有连接，数据包是一次收发一个，没有流
//udp也是使用socket，应用程序在使用UDP时必须指定网络接口(ip和端口)
//注意：tcp和udp是两套独立的端口，即如果tcp占用了1234端口，不影响另一个应用程序用udp占用端口1234
public class UdpStudy {

    @Test
    public void test1() throws IOException, InterruptedException {
        Thread th = new Thread(UdpStudy::UdpServerStart);
        th.start();
        Thread th2 = new Thread(UdpStudy::UdpClientStart);
        th2.start();

        th.join();
        th2.join();
    }
    public static void UdpServerStart() {
        //服务端
        try(DatagramSocket ds = new DatagramSocket(6666)){
            while(true){
                //数据缓冲区
                byte[] buffer = new byte[1024];
                //收到数据存到buffer里
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet);//收取一个udp数据包

                //将收到的数据按照字符集转换成字符串
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                System.out.println("[Client]: " + msg);

                //发送数据
                byte[] data = "ACK".getBytes(StandardCharsets.UTF_8);
                //当服务器收到一个DatagramPacket后，通常必须立刻回复一个或多个UDP包，因为客户端地址在DatagramPacket中，每次收到的DatagramPacket可能是不同的客户端，如果不回复，客户端就收不到任何UDP包
                packet.setData(data);
                ds.send(packet);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //客户端使用UDP时，只需要直接向服务器端发送UDP包，然后接收返回的UDP包
    public static void UdpClientStart() {
        //客户端
        try(DatagramSocket ds = new DatagramSocket()){//客户端无需指定端口，由操作系统自动指定
            ds.setSoTimeout(1000);//接收udp包时等待不会超过1秒
            ds.connect(InetAddress.getByName("127.0.0.1"), 6666);//这里的connect不是真的连接，而是为了在客户端的DatagramSocket实例中保存服务端的ip和端口

            //发送数据
            byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(data, data.length);
            ds.send(packet);

            //接收数据
            byte[] buffer = new byte[1024];
            packet = new DatagramPacket(buffer, buffer.length);
            ds.receive(packet);
            String resp = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
            System.out.println("[Server]: " + resp);
            ds.disconnect();
            ds.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


