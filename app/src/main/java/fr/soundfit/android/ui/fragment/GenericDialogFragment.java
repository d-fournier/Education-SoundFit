package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 07/01/2015.
 */
public abstract class GenericDialogFragment extends DialogFragment {

    protected static final String EXTRA_IS_DIALOG = "fr.soundfit.android.EXTRA_IS_DIALOG";

    protected boolean mIsDialog = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initArg(getArguments());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        bindView(view);
        return view;
    }

    protected void initArg(Bundle args){
        if(args != null){
            mIsDialog = args.getBoolean(EXTRA_IS_DIALOG);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mIsDialog && getDialog() != null){
            getDialog().getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }
    }


    protected abstract int getLayoutId();
    protected void bindView(View view){ }

}

