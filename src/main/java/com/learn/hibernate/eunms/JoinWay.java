package com.learn.hibernate.eunms;

public enum  JoinWay {

    LEFT(1),
    RIGHT(2),
    INNER(3);
    private int value;
    private JoinWay(int value){
        this.value=value;
    }
    public int value(){
        return this.value;
    }


}
