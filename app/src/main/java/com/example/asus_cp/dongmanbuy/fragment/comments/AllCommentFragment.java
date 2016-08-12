package com.example.asus_cp.dongmanbuy.fragment.comments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.product_detail.CommetnActivity;
import com.example.asus_cp.dongmanbuy.adapter.CommentAdapter;
import com.example.asus_cp.dongmanbuy.model.Comment;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.DialogHelper;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全部评价
 * Created by asus-cp on 2016-06-02.
 */
public class AllCommentFragment extends Fragment {
    private Context context;
    private ListView listView;
    private String userCommentUrl = "http://www.zmobuy.com/PHP/index.php?url=/comments";//用户评论的接口
    private RequestQueue requestQueue;
    private Good good;
    private String tag="AllCommentFragment";
    private List<Comment> comments;
    private LinearLayout moRenLinearLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.all_comments_fragment_layout,null);
        listView= (ListView) v.findViewById(R.id.list_all_comment);
        moRenLinearLayout= (LinearLayout) v.findViewById(R.id.ll_no_comment_display_all);
        context=getActivity();
        CommetnActivity commetnActivity= (CommetnActivity) getActivity();
        good=commetnActivity.getGood();
        comments=new ArrayList<Comment>();
        requestQueue= MyApplication.getRequestQueue();
        //弹出正在加载的对话框
        DialogHelper.showDialog(context);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, userCommentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //关闭正在加载的对话框
                DialogHelper.dissmisDialog();
                MyLog.d(tag, "返回的数据是" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        Comment commet = new Comment();
                        JSONObject ziJsonObject = jsonArray.getJSONObject(i);
                        commet.setId(ziJsonObject.getString("id"));
                        commet.setAuthor(ziJsonObject.getString("author"));
                        commet.setContent(ziJsonObject.getString("content"));
                        commet.setCreateTime(ziJsonObject.getString("create"));
                        commet.setReContent(ziJsonObject.getString("re_content"));
                        comments.add(commet);
                    }
                    if(comments.size()>0){
                        CommentAdapter commentAdapter=new CommentAdapter(context,comments);
                        listView.setAdapter(commentAdapter);
                    }else{
                        listView.setVisibility(View.GONE);
                        moRenLinearLayout.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("json", "{\"goods_id\":\"" + good.getGoodId() + "\",\"pagination\":{\"page\":\"1\",\"count\":\"100\"}}");
                return map;
            }
        };
        requestQueue.add(stringRequest);
        return v;
    }
}
