package com.example.asus_cp.dongmanbuy.adapter;

import com.example.asus_cp.dongmanbuy.R;

/**
 * 驾车路线规划的适配器
 * Created by asus-cp on 2016-07-27.
 */
public class JiaCheLuXianGuiHuaAdapter{

   /* private String tag="JiaCheLuXianGuiHuaAdapter";

    private Context context;
    private List<DrivingRouteLine> routeLines;
    private LayoutInflater inflater;



    public JiaCheLuXianGuiHuaAdapter(Context context, List<DrivingRouteLine> routeLines) {
        this.context = context;
        this.routeLines = routeLines;
        inflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return routeLines.size();
    }

    @Override
    public Object getItem(int position) {
        return routeLines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=inflater.inflate(R.layout.gong_jiao_lu_xian_item_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.gongJiaoNameTextView= (TextView) v.findViewById(R.id.text_gong_jiao_name_lu_xian_item);
            viewHolder.timeTextView= (TextView) v.findViewById(R.id.text_time_lu_xian_item);
            viewHolder.distanceTextView= (TextView) v.findViewById(R.id.text_distance_lu_xian_item);
            viewHolder.luXianDescTextView= (TextView) v.findViewById(R.id.text_lu_xian_desc_lu_xian_item);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        DrivingRouteLine line=routeLines.get(position);//代表的是一条线路方案
        List<DrivingRouteLine.DrivingStep> steps=line.getAllStep();//所有的换乘段

        StringBuilder sb=new StringBuilder();
        int j=1;
        for(int i=0;i<steps.size();i++){
            MyLog.d(tag,"入口信息的标题：   "+steps.get(i).getEntrance().getTitle());
            MyLog.d(tag,"出口信息的标题：   "+steps.get(i).getExit().getTitle());
            MyLog.d(tag,"该路段的描述:    "+steps.get(i).getInstructions());
            sb.append(j + "," + steps.get(i).getInstructions() + "\n");
            j++;
        }

        viewHolder.gongJiaoNameTextView.setText("路线"+(position+1)+":");
        viewHolder.luXianDescTextView.setText(sb.toString());
        int time = line.getDuration();
        if ( time / 3600 == 0 ) {
            viewHolder.timeTextView.setText("大约需要：" + time / 60 + "分钟");
        } else {
            viewHolder.timeTextView.setText( "大约需要：" + time / 3600 + "小时" + (time % 3600) / 60 + "分钟" );
        }
        viewHolder.distanceTextView.setText("距离大约是：" + line.getDistance() + "米");
        return v;
    }


    class ViewHolder{
        TextView gongJiaoNameTextView;
        TextView timeTextView;
        TextView distanceTextView;
        TextView luXianDescTextView;
    }*/
}
