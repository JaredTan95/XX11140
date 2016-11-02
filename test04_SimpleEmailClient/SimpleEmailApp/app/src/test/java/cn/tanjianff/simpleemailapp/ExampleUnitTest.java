package cn.tanjianff.simpleemailapp;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import cn.tanjianff.simpleemailapp.dbEntity.MailBean;
import cn.tanjianff.simpleemailapp.dbEntity.receiveMailBean;
import cn.tanjianff.simpleemailapp.util.javaMailUtil;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //@Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    //@Test
    public void sendTextmail_isOk() throws Exception {
        MailBean mailBean = new MailBean();
        mailBean.setHost("smtp.163.com")
                .setUsername("tanjian20150101@163.com").setPassword("tj1002")
                .setFrom("tanjian20150101@163.com").setTo("773767470@qq.com").setSubject("app群发测试邮件").setContent("正文:javaMial测试邮件!");
        javaMailUtil.sendTextEmail(mailBean);

    }

    //@Test
    public void receiveMail_isOk() throws Exception {
        MailBean mailBean = new MailBean();
        mailBean.setHost("smtp.163.com")
                .setUsername("tanjian20150101@163.com").setPassword("tj1002")
                .setFrom("tanjian20150101@163.com");
        for(receiveMailBean item:javaMailUtil.receive(mailBean)){
            System.out.println(item.getFrom());
        }
    }

    @Test
    public void validateAccount_isOk() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host","smtp.163.com");
        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.debug","true");
        Transport transport=Session.getInstance(props).getTransport();
        transport.connect("tanjian20150101@163.com","tj1002");
        System.out.println(transport.getURLName());
        System.out.println(transport.isConnected());
    }

}