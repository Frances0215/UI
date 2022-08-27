package com.example.ui;

import android.content.Context;
import android.graphics.Color;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ui.WarnAddress;

import org.w3c.dom.Text;

import java.util.List;

public class WarnAddressAdapter extends BaseAdapter implements OnClickListener{

    private List<WarnAddress> mWarnAddresses;
    private LayoutInflater mInflater;
    private Callback mCallback;
//    private final static int one=1<<24;
//    private final static int two=2<<24;

    public interface Callback{
        public void click(View v);
    }

    public WarnAddressAdapter(Context context, List<WarnAddress> warnAddresses, Callback callback){
        mWarnAddresses = warnAddresses;
        mInflater = LayoutInflater.from(context);
        mCallback = callback;
    }
    @Override
    public int getCount() {
        return mWarnAddresses.size();
    }

    @Override
    public Object getItem(int position) {
        return mWarnAddresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.activity_warn_item,null);
            holder = new ViewHolder();
            holder.mTvWarn = (TextView)convertView.findViewById(R.id.mTvWarn);
            holder.mTvAddress = (TextView)convertView.findViewById(R.id.mTvAddress);
            holder.mCbUse = (CheckBox)convertView.findViewById(R.id.mCbWarn);
            convertView.setTag(holder);
        }else {
            holder =(ViewHolder)convertView.getTag();
        }
        holder.mCbUse.setChecked(mWarnAddresses.get(position).isUse());
        holder.mTvWarn.setText(mWarnAddresses.get(position).getName());
        holder.mTvAddress.setText("地址：圆心"+"("+mWarnAddresses.get(position).getAddress_x()+"，"+mWarnAddresses.get(position).getAddress_x()+")"+"半径："+mWarnAddresses.get(position).getRadius());

        holder.mCbUse.setOnClickListener(this);
        holder.mCbUse.setTag(R.id.one,position);
        holder.mCbUse.setTag(R.id.two,holder.mCbUse.isChecked());
        return convertView;
    }

    public class ViewHolder{
        public TextView mTvWarn;
        public TextView mTvAddress;
        public CheckBox mCbUse;
    }
    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }
}
//public class WarnAddressAdapter extends ArrayAdapter<WarnAddress> implements OnClickListener {
//    private int resourceId;
//    //private ClickUse mClickUse;
//    private Context mContext;
//    private InnerItemOnclickListener mListener;
//
//    public WarnAddressAdapter(@NonNull Context context, int testViewResourceId, List<WarnAddress> object) {
//        super(context, testViewResourceId,object);
//        resourceId = testViewResourceId;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
////        final ViewHolder viewHolder;
////        if(convertView == null){
////            viewHolder = new ViewHolder();
//////            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_warn_item,null);
//////            viewHolder.mCb = (CheckBox) convertView.findViewById(R.id.mCbWarn);
//////            viewHolder.t1 = (TextView)convertView.findViewById(R.id.mTvWarn);
//////            viewHolder.t2 = (TextView)convertView.findViewById(R.id.mTvAddress);
////            View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//////
////        TextView mTvWarn = (TextView)view.findViewById(R.id.mTvWarn);
////        TextView mTvAddress = (TextView)view.findViewById(R.id.mTvAddress);
////        CheckBox mCbWarn= (CheckBox) view.findViewById(R.id.mCbWarn);
////            convertView.setTag(viewHolder);
////        }else{
////            viewHolder=(ViewHolder)convertView.getTag();
////        }
////        WarnAddress warnAddress = getItem(position);
////        viewHolder.mCb.setChecked(warnAddress.isUse());
////        viewHolder.t1.setText(warnAddress.getName());
////        viewHolder.t2.setText("地址：圆心"+"("+warnAddress.getAddress_x()+"，"+warnAddress.getAddress_x()+")"+"半径："+warnAddress.getRadius());
////        return convertView;
//        WarnAddress warnAddress = getItem(position);
//        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//
//        TextView mTvWarn = (TextView)view.findViewById(R.id.mTvWarn);
//        TextView mTvAddress = (TextView)view.findViewById(R.id.mTvAddress);
//        CheckBox mCbWarn= (CheckBox) view.findViewById(R.id.mCbWarn);
//        mTvWarn.setText(warnAddress.getName());
//        mTvAddress.setText("地址：圆心"+"("+warnAddress.getAddress_x()+"，"+warnAddress.getAddress_x()+")"+"半径："+warnAddress.getRadius());
//        mCbWarn.setChecked(warnAddress.isUse());
//
////        mCbWarn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                boolean nowState;
//////                if (mCbWarn.isChecked()) nowState = true;
//////                else nowState = false;
//////                warnAddress.setUse(!nowState);
////                //mClickUse.click_use(v);
////                WarnManager mWarnManager = new WarnManager(mContext);
////                boolean use = warnAddress.isUse();
////                int warn_id = warnAddress.getId();
////                boolean flag = mWarnManager.updateWarnUseById(warn_id,use);
////                //                    Toast.makeText(this, "修改失败",Toast.LENGTH_SHORT).show();
////                if(!flag) Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
////                mWarnManager.closeDataBase();
////            }
////        });
//
//        return view;
//    }
//
//
//    public final static class ViewHolder {
//        CheckBox mCb;
//        TextView t1,t2;
//    }
//
//    interface InnerItemOnclickListener {
//        void itemClick(View v);
//    }
//
//    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
//        this.mListener=listener;
//    }
//
//    @Override
//    public void onClick(View v) {
//        mListener.itemClick(v);
//    }
//}
