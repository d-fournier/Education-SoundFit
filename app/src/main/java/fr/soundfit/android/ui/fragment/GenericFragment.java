package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pnikosis.materialishprogress.ProgressWheel;

import fr.soundfit.android.R;

/**
 * Project : SoundFit
 * Package : fr.soundfit.android.ui.fragment
 * By Donovan on 07/01/2015.
 */
public abstract class GenericFragment extends Fragment{

    private FrameLayout mContainer;
    private ProgressWheel mProgress;

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
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        mContainer = (FrameLayout) view.findViewById(R.id.container);
        mProgress = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        View content = inflater.inflate(getLayoutId(), mContainer, true);
        bindView(content);
        //mContainer.addView(content);
        return view;
    }

    protected void initArg(Bundle args){ }
    protected abstract int getLayoutId();
    protected void bindView(View view){ }

    protected void displayLoading(boolean isLoading){
        if(isLoading){
            mContainer.setVisibility(View.INVISIBLE);
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mContainer.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
        }
    }
}

