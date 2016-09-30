package com.compay.xm.androidnotificationsystem.model_classes;

/**
 * Created by vivek on 30/9/16.
 */
public class MessageChatModel {
    private String message;
    private String recipient;
    private String sender;

    private int mRecipientOrSenderStatus;


    /* Setter */

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecipientOrSenderStatus(int recipientOrSenderStatus) {
        this.mRecipientOrSenderStatus = recipientOrSenderStatus;
    }

    public void setRecipient(String givenRecipient){
        recipient=givenRecipient;
    }

    public void setSender(String givenSender){
        sender=givenSender;
    }


    /* Getter */

    public String getMessage() {
        return message;
    }

    public String getRecipient(){
        return recipient;
    }

    public String getSender(){
        return sender;
    }

    public int getRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }
}
