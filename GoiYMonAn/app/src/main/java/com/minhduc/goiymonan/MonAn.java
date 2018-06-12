package com.minhduc.goiymonan;

/**
 * Created by minhduc on 5/13/2017.
 */

public class MonAn {
    public String IdSP;
    public String IdLoai;
    public String TenSP;
    public int GiaSP;
    public String HinhSP;
    public String MoTa;

    public MonAn() {

    }

    public MonAn(String idSP, String idLoai, String tenSP, int giaSP, String hinhSP, String moTa) {
        IdSP = idSP;
        IdLoai = idLoai;
        TenSP = tenSP;
        GiaSP = giaSP;
        HinhSP = hinhSP;
        MoTa = moTa;
    }
}