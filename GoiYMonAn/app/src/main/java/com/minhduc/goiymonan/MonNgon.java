package com.minhduc.goiymonan;

/**
 * Created by minhduc on 5/15/2017.
 */


public class MonNgon {
    public String IdMonNgon;
    public String IdSP;

    public String TenSP;
    public int GiaSP;
    public String HinhSP;
    public String MoTa;

    public MonNgon() {

    }

    public MonNgon(String idMonNgon, String idSP, String tenSP, int giaSP, String hinhSP, String moTa) {
        IdMonNgon = idMonNgon;
        IdSP = idSP;
        TenSP = tenSP;
        GiaSP = giaSP;
        HinhSP = hinhSP;
        MoTa = moTa;
    }
}