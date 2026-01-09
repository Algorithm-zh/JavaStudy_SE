package org.net;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class HttpStudy {
    //超文本传输协议，tcp基础，80端口，加密443
    //服务端的http其实就是web服务器，这是后面web编程的内容，这里只简单写一下客户端

    //java11开始，使用HttpClient，可以链式调用api
    static HttpClient httpClient = HttpClient.newBuilder().build();

    //http get
    @Test
    public void test1() throws URISyntaxException, IOException, InterruptedException {
        String url = "Https://www.baidu.com/";
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                //设置请求头
                .header("User-Agent", "Java HttpClient").header("Accept", "*/*")
                .timeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //http允许重复header key，所以一个header可以对应多个value
        Map<String, List<String>> headers = response.headers().map();
        for(String header : headers.keySet()){
            System.out.println(header + ": " + headers.get(header).get(0));
        }
        //请求体
        System.out.println(response.body().substring(0, 1024) + " ...");
    }

    //http post
    @Test
    public void test2() throws URISyntaxException, IOException, InterruptedException {
        String url = "Https://www.baidu.com/";
        String body = "username=admin&password=admin";
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                //设置请求头
                .header("Accept", "*/*")
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .timeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
