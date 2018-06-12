package com.minhduc.goiymonan.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhduc.goiymonan.R;

import java.util.List;

/**
 * Created by minhduc on 5/15/2017.
 */

public class GioHangAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<GioHang> gioHangList;


    public GioHangAdapter(Context context, int layout, List<GioHang> sanphList ) {
        this.context = context;
        this.layout = layout;
        this.gioHangList = sanphList;

    }

    private class ViewHolder{
        ImageView imgHinhGH;
        TextView txtSanPham,txtGia, txtThoiGian;

    }
    @Override
    public int getCount() {
        return gioHangList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GioHangAdapter.ViewHolder holder;

        if(view == null){
            holder = new GioHangAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imgHinhGH = (ImageView) view.findViewById(R.id.imageviewGioHang);
            holder.txtSanPham = (TextView) view.findViewById(R.id.textviewSanPhamGioHang);
//            holder.txtGia = (TextView) view.findViewById(R.id.textviewGiaGioHang);
//            DecimalFormat dinhdangSo = new DecimalFormat("###,###,###");
//            holder.txtGia.setText(dinhdangSo.format(haiSan.GiaHS) + "Đ");

            holder.txtThoiGian = (TextView) view.findViewById(R.id.textviewThoiGian);
            view.setTag(holder);
        }else {
            holder = (GioHangAdapter.ViewHolder) view.getTag();
        }
        GioHang datHang = gioHangList.get(i);
        holder.txtSanPham.setText(datHang.IdSanPham);
        holder.txtThoiGian.setText(datHang.ThoiGian);
        holder.imgHinhGH.setImageResource(R.drawable.giohang);
        return view;
    }
}
