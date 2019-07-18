package com.learn.hibernate.domian;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private String id;
    @Nojoin
    private String userNo;
    @Nojoin
    private String userName;

    @Nojoin
    private String organizationId;//组织单位id

    @Nojoin
    private String organizationName;//组织单位名称

    @Nojoin
    private String departmentId;//部门id

    @Nojoin
    private String departmentName;//部门名称

    @Nojoin
    private String deviceId;//设备id

    private String deviceName;//设备名称

    @Nojoin
    private String deviceTmodel;//设备型号

    private String ip;//ip

    private Integer port;//端口

    private String signature;//签名

    @Nojoin
    private Integer isSync;//是否同步(0同步,1不同步)
    @Ignore
    private String isSyncName;
    public String getIsSyncName(){
        if(this.isSync!=null){
            switch (this.isSync){
                case 0:this.isSyncName="同步";
                case 1:this.isSyncName="不同步";
            }
        }
        return this.isSyncName;
    }

    private Long time;//考勤时间
    @Ignore
    private Date timeFormat;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    public Date getTimeFormat(){
        if(this.time!=null){
            return new Date(this.time);
        }
        return null;
    }

    @Nojoin
    private Long endTime;//最后采集时间
    @Ignore
    private Date endTimeFormat;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    public Date getEndTimeFormat(){
        if(this.endTime!=null){
            return new Date(this.endTime);
        }
        return null;
    }
    @Nojoin
    private Long createdDateTime;
    @Nojoin
    private Long lastUpdateDateTime;
    @Nojoin
    private String createdUserId;
    @Nojoin
    private String createdUserName;
    @Nojoin
    private String lastUpdateUserId;
    @Nojoin
    private String lastUpdateUserName;
    @Ignore
    private Date createdDateTimeFormat;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    public Date getCreatedDateTimeFormat() {
        if(this.createdDateTime!=null){
            return new Date(this.createdDateTime);
        }
        return null;
    }
    @Ignore
    private Date lastUpdateDateTimeFormat;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    public Date getLastUpdateDateTimeFormat() {
        if(this.lastUpdateDateTime!=null){
            return new Date(this.lastUpdateDateTime);
        }
        return null;
    }
}
