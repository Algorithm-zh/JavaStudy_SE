package org.net;

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpStudy {
    @Test
    public void test1() throws IOException, InterruptedException {
        //一个应用程序通过一个Socket来建立一个远程连接，而Socket内部通过TCP/IP协议把数据传输到网络
        //Socket = ip:port
        Thread th = new Thread(() -> {
            try {
                Server.listen(6666);//监听端口6666
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        th.start();
        Thread th2 = new Thread(() -> {
            new Client().connect("localhost", 6666);
        });
        th2.start();
        th.join();
        th2.join();
    }
}

//服务端
class Server{
    //使用线程池管理连接线程(使用虚拟线程)
    private static final ExecutorService es = Executors.newCachedThreadPool(Thread.ofVirtual().factory());
    public static void listen(int port) throws IOException {
        try(ServerSocket server = new ServerSocket(port)){
            while(true){
                //每当有一个新链接就创建一个新的socket， 用一个新线程来处理连接
                Socket socket = server.accept();
                es.submit(new ServerHandler(socket));
            }
        }catch(IOException e){
            System.out.println("server closed.");
        }
    }
}

class ServerHandler implements Runnable{
    private final Socket socket;
    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(InputStream input = socket.getInputStream();OutputStream output = socket.getOutputStream()){
            handle(input, output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("client disconnected.");
    }
    public void handle(InputStream input, OutputStream output) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        bw.write("你好， 小明!\n");
        bw.flush();//强制数据立马从缓存区发送出去
        while(true){
            String line = br.readLine();
            if(line.equals("bye")){
                bw.write("再见， 小明!\n");
                bw.flush();
                break;
            }
            bw.write("你输入的是：" + line + "\n");
            bw.flush();
        }
    }
}

//客户端
class Client{
    public void connect(String ip, int port){
        try(Socket socket = new Socket(ip, port)){
            try(InputStream input = socket.getInputStream(); OutputStream output = socket.getOutputStream()){
                handle(input, output);
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("connect close");
        }
    }
    private void handle(InputStream input, OutputStream output) throws IOException {
        var readr = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        Scanner sc = new Scanner(System.in);
        System.out.println("[Server] " + readr.readLine());
        while(true){
            System.out.println(">>> ");
            String s = sc.nextLine();
            System.out.println(s);
            writer.write(s);
            writer.newLine();//向服务端发一个换行符，标记消息的结束. 或者直接在字符串后面加\n也可以
            writer.flush();
            String resp = readr.readLine();
            System.out.println("<<< " + resp);
            if(resp.equals("bye")){
                break;
            }
        }
    }
}