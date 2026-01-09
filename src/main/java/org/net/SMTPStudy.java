package org.net;

import org.junit.jupiter.api.Test;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//我们把类似Outlook这样的邮件软件称为MUA：Mail User Agent，意思是给用户服务的邮件代理；邮件服务器则称为MTA：Mail Transfer Agent，意思是邮件中转的代理；
// 最终到达的邮件服务器称为MDA：Mail Delivery Agent，意思是邮件到达的代理。电子邮件一旦到达MDA，就不再动了。
// 实际上，电子邮件通常就存储在MDA服务器的硬盘上，然后等收件人通过软件或者登陆浏览器查看邮件
//SMTP协议是基于TCP协议实现的邮件传输协议, MUA到MTA发送邮件的协议就是SMTP
//POP3协议是基于TCP协议实现的邮件协议，MDA到MTA接收邮件的协议就是POP3,端口一般是110，如果要加密是995
//IMAP也是一种接收邮件的协议,端口143，加密993
//IMAP和POP3的主要区别是，IMAP协议在本地的所有操作都会自动同步到服务器上，并且，IMAP可以允许用户在邮件服务器的收件箱中创建文件夹
public class SMTPStudy {

    //想发送邮件我们需要的信息
    /*
    1.MTA服务器的地址和端口号
        常用的有：
        163邮箱：smtp.163.com 465/25(没开ssl)
        gmail邮箱：smtp.gmail.com 465
        腾讯邮箱：smtp.qq.com 465
    2.SMTP服务器的登录信息：通常是使用自己的邮件地址作为用户名，登录口令是用户口令或者一个独立设置的SMTP口令
     */
    //依赖为
    //javax.mail:mail
    @Test
    public void test1() throws MessagingException, IOException {
        //服务器地址
        String smtp = "smtp.163.com";
        //用户名
        String username = "zxe_wy@163.com";
        //登录口令
        String password = "SLfF53BrQGesFTKi";
        //创建Properties对象存储信息
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "25");
        //获取Session实例(会话) 位于javax.mail.mail里
        Session session = Session.getInstance(props, new Authenticator() {//如果服务器需要认证，则需要传入一个Authenticator对象,返回指定的用户名和口令
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);//设置debug模式调式，会打印更多信息

        //发送邮件
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));//设置发送方地址(一般发送方地址都要要求和用户名相同)
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("2625695527@qq.com"));//设置接收方地址
        message.setSubject("测试邮件", "UTF-8");//主题
        message.setText("你好，这是在测试邮件发送", "UTF-8");
        Transport.send(message);

        //发送附件
        Multipart multipart = new MimeMultipart();
        //添加text
        BodyPart textPart = new MimeBodyPart();
        //如果给一个<img src="http://example.com/test.jpg">，这样的外部图片链接通常会被邮件客户端过滤，并提示用户显示图片并不安全。只有内嵌的图片才能正常在邮件中显示。
        textPart.setContent("<h1>Hello</h1><p><img src=\"cid:img01\"></p>", "text/html;charset=UTF-8");
        multipart.addBodyPart(textPart);
        //添加image
        BodyPart imagepart = new MimeBodyPart();
        String fileName = "C:\\Users\\R9000P\\Desktop\\bk.jpg";
        imagepart.setFileName(fileName);
        InputStream input = new FileInputStream(fileName);
        imagepart.setDataHandler(new DataHandler(new ByteArrayDataSource(input, "image/jpeg")));
        //与html里的img标签关联
        imagepart.setHeader("Content-ID", "<img01>");
        multipart.addBodyPart(imagepart);

        //设置邮件内容为附件multipart
        message.setContent(multipart);
        Transport.send(message);
    }


}
