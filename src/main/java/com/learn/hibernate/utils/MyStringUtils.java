package com.learn.hibernate.utils;

import java.util.List;

public class  MyStringUtils {

    /**
     * 给传入的驼峰命名法的字断转换为数据库的横杠隔开的方式 如 userName -> user_name
     * @param s
     * @return
     */
    public static String getSqlWord(String s) {
        StringBuilder sb = new StringBuilder();
        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            if(Character.isUpperCase(chars[i])) {
                if(i == 0) {
                    sb.append(Character.toLowerCase(chars[i]));
                }else {
                    sb.append("_" + Character.toLowerCase(chars[i]));
                }
            }else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 获取最后一个指定char后边的String
     * @return
     */
    public static String subStringLastChar(String s,char c) {
        return s.substring(s.lastIndexOf(c)+1);
    }




    public static String removeStringLastString(StringBuilder sb, String c){
        return sb.substring(0,sb.lastIndexOf(c));
    }
}
