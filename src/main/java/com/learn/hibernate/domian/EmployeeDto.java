package com.learn.hibernate.domian;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeDto {
    private String id;

    private String name;

    private Integer sex;

    @Nojoin
    private String userNo;

    private Integer state;

    @Nojoin
    private String phoneNumber;

    @Nojoin
    private String departmentId;

    private String departmentName;

    private String departmentPath;

    @Nojoin
    private String organizationId;

    private String organizationName;

    private String organizationPath;

    @Nojoin
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    private String email;

    @Nojoin
    private Integer isFingerprints;

    @Nojoin
    private Integer isFace;

    @Nojoin
    @Ignore
    private String sexName;

    @Nojoin
    @Ignore
    private String stateName;

    @Ignore
    private String isFingerprintsName;

    @Ignore
    private String isFaceName;



    public String getSexName() {
        if(getSex()!=null){
            switch (getSex()){
                case 0:
                    sexName="男";
                    break;
                case 1:
                    sexName="女";
                    break;
            }
        }
        return sexName;
    }


    public String getStateName() {
        if(getState()!=null){
            switch (getState()){
                case 0:
                    stateName="默认";
                    break;
                case 1:
                    stateName="正式";
                    break;
                case 2:
                    stateName="试用";
                    break;
                case 4:
                    stateName="离职";
                    break;
            }
        }
        return stateName;
    }



    public String getIsFingerprintsName() {
        if(getIsFingerprints()!=null){
            switch (getIsFingerprints()){
                case 0:
                    isFingerprintsName="无";
                    break;
                case 1:
                    isFingerprintsName="有";
                    break;
            }
        }
        return isFingerprintsName;
    }

    public String getIsFaceName() {
        if(getIsFace()!=null){
            switch (getIsFace()){
                case 0:
                    isFaceName="无";
                    break;
                case 1:
                    isFaceName="有";
                    break;
            }
        }
        return isFaceName;
    }


}
