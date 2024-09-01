package com.library;

public class Member {
    private int memberId;
    private String name;
    private double mobile_no;
    private String email;

    public Member(int memberId,String name,double mobile_no,String email) {
        this.memberId = memberId;
        this.name=name;
        this.mobile_no = mobile_no;
        this.email = email;
    }

    public int getMemberId() {
        return memberId;
    }


    public String getName() {
        return name;
    }



    public double getMobile_no() {
        return mobile_no;
    }



    public String getEmail() {
        return email;
    }


}
