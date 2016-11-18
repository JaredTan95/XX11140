package cn.tanjianff.sampleemail;

import org.junit.Test;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import cn.tanjianff.sampleemail.dbEntity.MailBean;
import cn.tanjianff.sampleemail.dbEntity.receiveMailBean;
import cn.tanjianff.sampleemail.util.javaMailUtil;

/**
 * Created by tanjian on 16/11/16.
 * 测试JavamailUtils工具类中的方法
 */

public class JavamailUtils_test {
   // @Test
    public void JavamailUtils_test_isok(){

    }

    /*测试通过*/
    //@Test
    public void validateAccount_isOk() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host","smtp.163.com");
        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.debug","true");
        Transport transport= Session.getInstance(props).getTransport();
        transport.connect("tanjian20150101@163.com","tj1002");
        System.out.println(transport.getURLName());
        System.out.println(transport.isConnected());
    }


    /*测试通过*/
    //@Test
    public void receiveMail_isOk() throws Exception {
        MailBean mailBean = new MailBean();
        mailBean.setHost("smtp.163.com")
                .setUsername("tanjian20150101@163.com").setPassword("tj1002")
                .setFrom("tanjian20150101@163.com");
        for(receiveMailBean item: javaMailUtil.receive(mailBean)){
            System.out.println(item.getFrom());
        }
    }
}
