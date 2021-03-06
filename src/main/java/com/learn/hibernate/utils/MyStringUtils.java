package com.learn.hibernate.utils;

import java.util.List;

public class  MyStringUtils {

    /**
     * 给传入的驼峰命名法的字断转换为数据库的横杠隔开的方式 如 userName -> user_name
     * @param s
     * @return
     */
    public static synchronized String getSqlWord(String s) {
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
    public static synchronized String subStringLastChar(String s,char c) {
        return s.substring(s.lastIndexOf(c)+1);
    }




    public static synchronized String removeStringLastString(String sb, String c){
        return sb.substring(0,sb.lastIndexOf(c));
    }


    /**
     * 将string的首字母大写
     * @param str
     * @return
     */
    public static synchronized String upperFirtCharCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    /**
     * 将string的首字母小写
     * @param str
     * @return
     */
    public static synchronized String lowerCase(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
