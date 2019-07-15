package com.learn.hibernate.domian;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import lombok.Data;

@Data
public class CardDto {
   private String id;

   @Nojoin
   private String cardNo;

   @Nojoin
   private String cardSn;

   private Integer state;

   @Nojoin
   private String createdUserName;

   @Nojoin
   private String lastUpdateUserName;

   @Nojoin
   private Long createdDateTime;

   @Nojoin
   private Long lastUpdateDateTime;

   @Nojoin
   private String employeeId;

   private String employeeName;

   @Nojoin
   private String employeeUserNo;

   private Integer employeeState;

   @Nojoin
   private String organizationId;

   private String organizationName;

   @Nojoin
   @Ignore
   private String cardStateName;

   @Nojoin
   @Ignore
   private String employeeStateName;



   public String getCardStateName() {
      if(getState()!=null){
         switch (getState()){
            case 0:
               cardStateName="使用中";
               break;
            case 1:
               cardStateName="已挂失";
               break;
            case 2:
               cardStateName="已销卡";
               break;
         }
      }
      return cardStateName;
   }
   public String getEmployeeStateName() {
      if((getEmployeeState())!=null){
         switch (getEmployeeState()){
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



}
