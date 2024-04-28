
package com.example.finaldemo.utils;


public class StringDealer {
    public StringDealer() {
    }

    public String trimMax(String str) {
        if (str == null) return "";
        return str.trim().replaceAll("\\s+"," ");
    }

    public boolean checkEmailRegex(String email){
        String regex = "^[a-zA-Z0-9!#$%&'*+/=?^`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        return email.matches(regex);
    }

    public boolean checkPasswordRegex(String password){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,20}$";
        return password.matches(regex);
    }



}
