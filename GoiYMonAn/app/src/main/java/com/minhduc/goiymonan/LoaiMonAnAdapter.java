package com.minhduc.goiymonan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by minhduc on 5/11/2017.
 */

public class LoaiMonAnAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<LoaiMonAn> loaiSPList;

    public LoaiMonAnAdapter(Context context, int layout, List<LoaiMonAn> loaiSPList) {
        this.context = context;
        this.layout = layout;
        this.loaiSPList = loaiSPList;
    }

    @Override
    public int getCount() {
        return loaiSPList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgHinh;
        TextView txtTen;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.imgHinh = (ImageView) view.findViewById(R.id.imageviewLoaiSP);
            holder.txtTen = (TextView) view.findViewById(R.id.textviewTenLoai);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        LoaiMonAn loaiSP = loaiSPList.get(i);

        holder.txtTen.setText(loaiSP.TenLoai);

        Picasso.with(context).
                load(loaiSP.HinhLoai).placeholder(R.drawable.loading).error(R.drawable.no_photo).into(holder.imgHinh);
        return view;
    }

}
