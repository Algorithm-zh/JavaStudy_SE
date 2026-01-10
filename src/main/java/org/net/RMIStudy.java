package org.net;


import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.ZoneId;

/*
RMI的工作机制与RPC类似，但它支持面向对象的特性，允许传递对象而不仅仅是数据。其主要组件包括：
存根（Stub）：客户端的代理对象，负责将方法调用及参数序列化，发送给服务器端。
骨架（Skeleton）：服务器端的辅助类，接收客户端的调用请求，进行反序列化，调用实际的远程对象方法，并将结果返回给客户端。
需要注意的是，从Java 2 SDK（JDK 1.2）开始，骨架被废弃，取而代之的是反射机制来处理方法调用。
 */
/*
RMI的调用流程如下：
    1.客户端通过RMI注册表（RMI Registry）查找远程对象的引用。
    2.获取到远程对象的存根后，客户端调用存根的方法。
    3.存根将调用信息序列化，发送给服务器端。
    4.服务器端接收并反序列化请求，调用实际的远程对象方法。
    5.方法执行完毕后，服务器端将结果序列化，发送回客户端。
    6.客户端存根接收并反序列化结果，返回给调用者。
 */
//RPC 强调“过程”或“函数”的远程调用模型，客户端只需调用一个看似本地的函数，背后由 Stub/Skeleton 完成网络通信与序列化细节，适合过程式或函数式编程风格。
//RMI 则坚持“面向对象”的方法调用模型，将远程对象视为本地对象，支持方法重载、多态、对象引用传递等 Java 特性，更贴合面向对象设计。
//RPC 通常与语言无关，你可以用 Java、Go、Python、C++ 等多种语言实现同一套接口，通过 IDL（如 Protobuf、Thrift）生成各语言的 Stub，从而实现跨语言、跨平台的互操作。
//RMI 仅在 Java 生态中可用，客户端与服务端必须运行在兼容的 JVM 上，并共享相同或兼容的类定义，因此不支持其他语言调用。
//RPC 的传输协议灵活，可使用 HTTP/2（如 gRPC）、HTTP/1.1（如 JSON‑RPC）、原生 TCP/UDP（如 Thrift）、甚至自定义协议。不同协议在性能、可调试性和网络友好性上各有侧重。
//RMI 默认采用 JRMP（Java Remote Method Protocol），这是 Java 专有的基于 TCP 的协议；也可通过 RMI‑IIOP 绑定到 CORBA 的 IIOP 协议，但这需要额外配置和 CORBA 知识。
/*
        ┌ ─ ─ ─ ─ ─ ─ ─ ─ ┐         ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
          ┌─────────────┐                                 ┌────────────┐
        │ │   Service   │ │         │                     │  Service   │  │
          └─────────────┘                                 └────────────┘
        │        ▲        │         │                            ▲        │
        │        |                                               │
        │        │        │         │                            │        │
          ┌─────────────┐   Network    ┌───────────────┐   ┌────────────┐
        | │ Client Stub ├─┼─────────┼─▶│Server Skeleton│──▶│Service Impl│ │
          └─────────────┘              └───────────────┘   └────────────┘
        └ ─ ─ ─ ─ ─ ─ ─ ─ ┘         └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
        客户端持有的WorldClock接口实际上对应了一个“实现类”，它是由Registry内部动态生成的，并负责把方法调用通过网络传递到服务器端
        客户端的“实现类”称为stub，而服务器端的网络服务类称为skeleton，它会真正调用服务器端的WorldClockService
 */
public class RMIStudy {

    //下面的服务器的RMI代码写完后我们需要把写的服务暴露在网络上
    @Test
    public void test() throws RemoteException, NotBoundException {
        ServerService();
        ClientService();
    }
    public void ServerService() throws RemoteException {
        //实例化
        WordClock wordClock = new WordClockService();
        //将这个服务转换为远程服务接口
        WordClock skeleton = (WordClock) UnicastRemoteObject.exportObject(wordClock, 0);
        //将RMI服务注册到1099端口
        Registry registry = LocateRegistry.createRegistry(1099);
        //注册服务，起名
        registry.rebind("WordClock", skeleton);
    }

    public void ClientService() throws RemoteException, NotBoundException {
        //连接服务器
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        WordClock wordClock = (WordClock) registry.lookup("WordClock");
        //直接调用
        LocalDateTime now = wordClock.getLocalDateTime("Asia/Shanghai");
        System.out.println(now);
    }
}

//服务端和客户端必须共享同一个接口，派生自remote
//服务器接口
interface WordClock extends Remote{
    LocalDateTime getLocalDateTime(String zoneId) throws RemoteException;
}
//服务器实现类
class WordClockService implements WordClock{
    @Override
    public LocalDateTime getLocalDateTime(String zoneId) throws RemoteException {
        return LocalDateTime.now(ZoneId.of(zoneId)).withNano(0);
    }
}