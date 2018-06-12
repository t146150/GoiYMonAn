package com.minhduc.goiymonan;

/**
 * Created by minhduc on 5/13/2017.
 */
public class DatHang {
    public String IdDH;
    public String IdSanPham;
    public String HoTen;
    public String Email;
    public String SoDienThoai;
    public String GhiChu;

    public DatHang() {

    }

    public DatHang(String idDH, String idSanPham, String hoTen, String email, String soDienThoai, String ghiChu) {
        IdDH = idDH;
        IdSanPham = idSanPham;
        HoTen = hoTen;
        Email = email;
        SoDienThoai = soDienThoai;
        GhiChu = ghiChu;
    }
}
