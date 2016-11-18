package cn.tanjianff.sampleemail.dbEntity;

import java.io.Serializable;

/**
 * Created by tanjian on 16/11/2.
 * 邮件抽象实体
 */

public class receiveMailBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String subject; //主题
    private String from;//发送人
    private String receiveAddress;//收件人
    private String sendData;//发送时间
    private boolean isSeen;//是否已读
    private String priority;//邮件优先级
    private boolean needReply;//是否需要回执
    private String mailSize;//邮件大小
    private boolean hasAttachment;//是否包含附件
    private String content;//邮件正文

    public receiveMailBean(){
        this.subject="hahahahahahah";
    }

    public receiveMailBean(String subject, String from, String receiveAddress,
                           String sendData, boolean isSeen, String priority,
                           boolean needReply, String mailSize,
                           boolean hasAttachment, String content) {
        this.subject = subject;
        this.from = from;
        this.receiveAddress = receiveAddress;
        this.sendData = sendData;
        this.isSeen = isSeen;
        this.priority = priority;
        this.needReply = needReply;
        this.mailSize = mailSize;
        this.hasAttachment = hasAttachment;
        this.content = content;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public void setSendData(String sendData) {
        this.sendData = sendData;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setNeedReply(boolean needReply) {
        this.needReply = needReply;
    }

    public void setMailSize(String mailSize) {
        this.mailSize = mailSize;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getSubject() {
        return subject;
    }

    public String getFrom() {
        return from;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public String getSendData() {
        return sendData;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isNeedReply() {
        return needReply;
    }

    public String getMailSize() {
        return mailSize;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public String getContent() {
        return content;
    }
}
