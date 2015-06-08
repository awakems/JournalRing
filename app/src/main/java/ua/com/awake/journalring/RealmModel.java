package ua.com.awake.journalring;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Void on 04.06.2015.
 */
public class RealmModel extends RealmObject {
    private String number;
    private Date startCall;
    private Date endCall;
    private String conv_result;
    private String typeCall;

    public String getTypeCall() {
        return typeCall;
    }

    public void setTypeCall(String typeCall) {
        this.typeCall = typeCall;
    }

    public String getConv_result() {
        return conv_result;
    }

    public void setConv_result(String conv_result) {
        this.conv_result = conv_result;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getStartCall() {
        return startCall;
    }

    public void setStartCall(Date startCall) {
        this.startCall = startCall;
    }

    public Date getEndCall() {
        return endCall;
    }

    public void setEndCall(Date endCall) {
        this.endCall = endCall;
    }


}
