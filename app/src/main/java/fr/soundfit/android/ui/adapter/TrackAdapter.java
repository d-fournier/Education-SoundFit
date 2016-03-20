package fr.soundfit.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import fr.soundfit.android.R;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.adapter
 * By Donovan on 02/02/2015.
 */
public class TrackAdapter extends ArrayAdapter<Track> {

    private Context mContext;

    public TrackAdapter(Context context, int resource, List<Track> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        Track track = getItem(position);
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

        // TODO Resource
        vh.mTitleTV.setText(track.getTitle() + " - " + track.getArtist().getName());
        ImageLoader.getInstance().displayImage(track.getAlbum().getCoverUrl(),
                vh.mPictTV);

        return view;
    }

    private class ViewHolder{
        TextView mTitleTV;
        ImageView mPictTV;
    }
}
