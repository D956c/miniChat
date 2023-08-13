package com.ddchat_server.util;

//字符串校验工具
public class VerifyUtil {
    public static final String genders = "女男";

    public static String nicknameVerify(String nickname){
        if(nickname==null) return "昵称为空";
        if(nickname.length()<1) return "昵称最少需要1个字符";
        if(nickname.length()>15) return "昵称最多不超过15个字符";
        if(nickname.contains(" ")) return "昵称不能包含空格";
        return null;
    }

    public static String emailVerify(String email){
        if(email==null) return "邮箱为空";
        String tegex="[a-zA-Z0-9_]+@\\w+(\\.com|\\.cn){1}";
        if(email.matches(tegex))
            return null;
        return "邮箱格式不合法";
    }

    public static String passwordVerify(String password){
        if(password==null)  return "密码为空";
        if(password.length()<3)
            return "密码不能少于3位";
        return null;
    }

    public static int genderVerify(String gender){
        if(gender==null|| gender.equals(""))    return -1;
        return genders.indexOf(gender);
    }
}
