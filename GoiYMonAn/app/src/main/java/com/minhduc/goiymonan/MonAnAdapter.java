package com.minhduc.goiymonan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by minhduc on 5/13/2017.
 */

public class MonAnAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<MonAn> sanPhamList;

    public MonAnAdapter(Context context, int layout, List<MonAn> sanPhamList) {
        this.context = context;
        this.layout = layout;
        this.sanPhamList = sanPhamList;
    }

    @Override
    public int getCount() {
        return sanPhamList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        ImageView imgHinh;
        TextView txtTen, txtGia;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imgHinh = (ImageView) view.findViewById(R.id.imageviewSanPham);
            holder.txtTen = (TextView) view.findViewById(R.id.textviewTenSanPham);
            holder.txtGia = (TextView) view.findViewById(R.id.textviewGia);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MonAn monAn = sanPhamList.get(i);

        holder.txtTen.setText(monAn.TenSP);

        DecimalFormat dinhdangSo = new DecimalFormat("###,###,###");
        holder.txtGia.setText(dinhdangSo.format(monAn.GiaSP) + "Đ");
        Picasso.with(context).load(monAn.HinhSP).into(holder.imgHinh);
        Picasso.with(context)
                .load(monAn.HinhSP)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.imgHinh);
        return view;
    }
}
