package com.example.ex07_grapic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<player_result> presults = new ArrayList<>();


    @Override
    public int getCount() {
        return presults.size();
    }

    @Override
    public player_result getItem(int position) {
        return presults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rank_list, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView rkresult = convertView.findViewById(R.id.rankresult);

        player_result presult = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */

        rkresult.setText(presult.getResult());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }

    public void addresult(String result) {

        player_result playerResult = new player_result();

        /* playerResult에 아이템을 setting한다. */

        playerResult.setResult(result);


        /* mItems에 MyItem을 추가한다. */
        presults.add(playerResult);

    }
}
