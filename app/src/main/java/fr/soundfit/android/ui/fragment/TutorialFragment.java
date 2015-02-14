package fr.soundfit.android.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.deezer.sdk.model.Playlist;

import org.w3c.dom.Text;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.activity.WelcomeActivity;
import fr.soundfit.android.utils.PrefUtils;
import fr.soundfit.android.utils.ResourceUtils;

/**
 * Created by Donovan on 14/02/2015.
 */
public class TutorialFragment extends GenericFragment implements View.OnClickListener {

    public static final String TAG = TutorialFragment.class.getSimpleName();

    private static final String EXTRA_TAG = "fr.soundfit.android.EXTRA_TAG";

    private String mTag;
    private ImageButton mNextButton;

    public static TutorialFragment newInstance(String tag) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TAG, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tutorial;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        ((ImageView)view.findViewById(R.id.tutorial_background)).setImageResource(
                ResourceUtils.getResourceId(ResourceUtils.ResourceType.DRAWABLE, mTag, getActivity()));
        ((TextView)view.findViewById(R.id.tutorial_title)).setText(
                ResourceUtils.getResourceId("title", ResourceUtils.ResourceType.STRING, mTag, getActivity()));
        ((TextView)view.findViewById(R.id.tutorial_desc)).setText(
                ResourceUtils.getResourceId("desc", ResourceUtils.ResourceType.STRING, mTag, getActivity()));
        mNextButton = (ImageButton) view.findViewById(R.id.next_bt);
        mNextButton.setOnClickListener(this);
    }

    @Override
    protected void initArg(Bundle args) {
        super.initArg(args);
        if(args != null){
            mTag = args.getString(EXTRA_TAG);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mNextButton){
            Activity act = getActivity();
            if(act != null && act instanceof WelcomeActivity){
                ((WelcomeActivity)act).onNextPageClick();
            }
        }
    }

}
