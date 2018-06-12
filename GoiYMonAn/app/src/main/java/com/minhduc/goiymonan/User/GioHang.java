package com.minhduc.goiymonan.User;

import java.io.Serializable;

/**
 * Created by minhduc on 5/15/2017.
 */

public class GioHang  implements Serializable {
    public int IdDH;
    public String IdSanPham;
    public String ThoiGian;
    public String GhiChu;
    public GioHang() {

    }

    public GioHang(int idDH, String idSanPham, String thoiGian, String ghiChu) {
        IdDH = idDH;
        IdSanPham = idSanPham;
        ThoiGian = thoiGian;
        GhiChu = ghiChu;
    }

    public int getIdDH() {
        return IdDH;
    }

    public void setIdDH(int idDH) {
        IdDH = idDH;
    }

    public String getIdSanPham() {
        return IdSanPham;
    }

    public void setIdSanPham(String idSanPham) {
        IdSanPham = idSanPham;
    }

    public String getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(String thoiGian) {
        ThoiGian = thoiGian;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }
}
