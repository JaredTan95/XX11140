package cn.tanjianff.simpleemailapp.dbEntity;
import java.util.Vector;

public class MailBean {

    private String to;                              // 收件人
    private String from;                            // 发件人
    private String host;                            // SMTP主机
    private String username;                        // 发件人的用户名
    private String password;                        // 发件人的密码
    private String subject;                         // 邮件主题
    private String content;                         // 邮件正文
    Vector<String> file;                            // 多个附件
    private String filename;                        // 附件的文件名

    public MailBean() {
    }

    public String getTo() {
        return to;
    }

    public String getHost() {
        return host;
    }

    public String getFrom() {
        return from;
    }

    public MailBean setTo(String to) {
        this.to = to;
        return this;
    }

    public MailBean setFrom(String from) {
        this.from = from;
        return this;
    }

    public MailBean setHost(String host) {
        this.host = host;
        return this;
    }

    public MailBean setUsername(String username) {
        this.username = username;
        return this;
    }

    public MailBean setPassword(String password) {
        this.password = password;
        return this;
    }

    public MailBean setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getFilename() {
        return filename;
    }

    public Vector<String> getFile(){
        return file;
    }

    public void attachFile(String fileName) {
        if(file == null)
            file = new Vector<String>();
        file.addElement(fileName);
    }
}
