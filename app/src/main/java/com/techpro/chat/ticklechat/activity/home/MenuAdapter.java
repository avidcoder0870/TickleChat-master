package com.techpro.chat.ticklechat.activity.home;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.models.MenuItems;
import com.techpro.chat.ticklechat.utils.AppUtils;

import java.util.ArrayList;


/**
 * Created by vishalrandive on 15/02/16.
 */
public class MenuAdapter extends ArrayAdapter<MenuItems> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<MenuItems> menuItemList = new ArrayList<>();

    public MenuAdapter(Context context, int layoutResourceId, ArrayList<MenuItems> menuItemList) {
        super(context, layoutResourceId, menuItemList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.menuItemList = menuItemList;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

//            holder = new TempViewHolder();

            TextView tvTitle = (TextView) row.findViewById(R.id.tvMenuItemName);
            TextView tvSubTitle = (TextView) row.findViewById(R.id.tvMenuItemSubTitle);
            ImageView logo = (ImageView) row.findViewById(R.id.ivMenuItemIcon);
            //holder.tvFontIcon = (FontIcon) row.findViewById(R.chatUserList.ivMenuItemIcon);
            TextView tvAlert = (TextView)row.findViewById(R.id.tvMenuItemAlert);
//			holder.title.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.font_type_opensans_semibold)));
//            row.setTag(holder);
//            if(position ==3){
//                tvTitle.setText(Html.fromHtml(menuItemList.get(position).getMenuItemName()+" "+"<font color=#fc7585><sup><small><b>New</b></small></sup></font>"));
//            }
            tvSubTitle.setText(menuItemList.get(position).getMenuItemSubTitles());
            tvTitle.setText(menuItemList.get(position).getMenuItemName());
            // holder.tvFontIcon.setText(menuItemList.get(position).getMenuItemDrawableTxt());
            logo.setImageResource(menuItemList.get(position).getMenuItemDrawable());
            logo.setColorFilter(context.getResources().getColor(R.color.textColorSecondary));

//        if(position==5)
//            holder.tvAlert.setVisibility(View.VISIBLE);
//        else
            tvAlert.setVisibility(View.GONE);
//        } else {
//            holder = (MenuAdapter.TempViewHolder) row.getTag();
        }

//		if(!isCustomTemplete)
//			holder.title.setTextColor(item.getTitleColor());
//		holder.logo.setImageDrawable(item.getImage());
        return row;
    }

}
