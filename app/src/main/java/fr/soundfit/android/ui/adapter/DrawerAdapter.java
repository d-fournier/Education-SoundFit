package fr.soundfit.android.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.utils.ResourceUtils;

/**
 * Created by Donovan on 24/01/2015.
 */
public class DrawerAdapter extends BaseAdapter {

    Context mContext;

    public DrawerAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return mContext.getResources().getStringArray(R.array.drawer_items).length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        String name = mContext.getResources().getStringArray(R.array.drawer_items)[i];
        return  ResourceUtils.getResourceId(ResourceUtils.ResourceType.ID, name, mContext);
    }

    public int getItemType(int position){
        String name = mContext.getResources().getStringArray(R.array.drawer_items)[position];
        return mContext.getResources().getInteger(ResourceUtils.getResourceId(ResourceUtils.ResourceType.ID, name, mContext));
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolderItem viewHolder;
        if(view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_drawer, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.mTitleTV = (TextView) view.findViewById(R.id.drawer_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) view.getTag();
        }
        String name = mContext.getResources().getStringArray(R.array.drawer_items)[i];
        viewHolder.mTitleTV.setText(ResourceUtils.getResourceId(ResourceUtils.ResourceType.STRING, name, mContext));
        if(getItemType(i) == -1){
            viewHolder.mTitleTV.setTextColor(mContext.getResources().getColor(R.color.theme_red));
            viewHolder.mTitleTV.setBackgroundResource(R.color.theme_white);
        } else {
            viewHolder.mTitleTV.setTextColor(mContext.getResources().getColor(R.color.theme_white));
            viewHolder.mTitleTV.setBackgroundResource(R.color.theme_red);
        }

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemType(position) != -1;
    }

    private static class ViewHolderItem {
        TextView mTitleTV;
    }

}
