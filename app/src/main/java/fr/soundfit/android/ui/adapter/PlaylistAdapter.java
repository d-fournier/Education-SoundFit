package fr.soundfit.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deezer.sdk.model.Playlist;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import fr.soundfit.android.R;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.adapter
 * By Donovan on 02/02/2015.
 */
public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    private Context mContext;

    public PlaylistAdapter(Context context, int resource, List<Playlist> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        Playlist playlist = getItem(position);
        ViewHolder vh;

        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.item_list, null);
            vh = new ViewHolder();
            vh.mTitleTV = (TextView) view.findViewById(R.id.item_title);
            vh.mPictTV = (ImageView) view.findViewById(R.id.item_picture);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }


        vh.mTitleTV.setText(playlist.getTitle());
        ImageLoader.getInstance().displayImage(playlist.getPictureUrl(), vh.mPictTV);

        return view;
    }

    private class ViewHolder{
        TextView mTitleTV;
        ImageView mPictTV;
    }
}
