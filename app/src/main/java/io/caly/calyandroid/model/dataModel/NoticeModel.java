package io.caly.calyandroid.model.dataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by jspiner on 2017. 3. 4..
 */

public class NoticeModel {

    @SerializedName("notice_title")
    public String noticeTitle;

    @SerializedName("create_datetime")
    public Date createDateTime;

    @SerializedName("notice_description")
    public String noticeDescription;

    @Expose
    public boolean isHeader = true;

    @Expose
    public boolean isExpandabled = false;

    public NoticeModel(String noticeDescription){
        this.noticeDescription = noticeDescription;
        isHeader = false;
    }

    public NoticeModel(){
        noticeTitle="test1";
        noticeDescription="dd";

    }
}
