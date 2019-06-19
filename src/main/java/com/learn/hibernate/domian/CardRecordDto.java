package com.learn.hibernate.domian;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class CardRecordDto {
    private String id;

    private Integer type;

    private String cardCardNo;

    private String cardCardSn;

    private Integer cardEmployeeState;

    @Nojoin
    private String createdUserName;

    @Nojoin
    private String createdDateTime;

    private String cardEmployeeName;

    private String cardEmployeeUserNo;

    private String cardEmployeeOrganizationName;

    @Ignore
    private String employeeStateName;

    @Ignore
    private String typeName;

    @Ignore
    private String createTime;

    public String getTypeName() {
        if(getType()!=null){
            switch (getType()){
                case 0:
                    typeName="卡挂失";
                    break;
                case 1:
                    typeName="自动销卡";
                    break;
                case 2:
                    typeName="销卡";
                    break;
                case 4:
                    typeName="开卡";
                    break;
            }
        }
        return typeName;
    }

    public String getEmployeeStateName() {
        if(getCardEmployeeState()!=null){
            switch (getCardEmployeeState()){
                case 0:
                    employeeStateName="默认";
                    break;
                case 1:
                    employeeStateName="正式";
                    break;
                case 2:
                    employeeStateName="试用";
                    break;
                case 4:
                    employeeStateName="离职";
                    break;
                default:
                    employeeStateName="默认";
                    break;
            }
        }
        return employeeStateName;
    }

    public String getCreateTime(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(getCreatedDateTime()!=null){
            createTime=sd.format(new Date(getCreatedDateTime()));
        }
        return createTime;
    }


}
