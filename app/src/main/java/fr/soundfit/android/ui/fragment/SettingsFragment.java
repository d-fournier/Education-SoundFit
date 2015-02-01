package fr.soundfit.android.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import fr.soundfit.android.R;
import fr.soundfit.android.ui.adapter.DrawerAdapter;
import fr.soundfit.android.utils.ResourceUtils;

/**
 * Created by Donovan on 01/02/2015.
 */
public class SettingsFragment extends GenericFragment implements AdapterView.OnItemClickListener {

    private DrawerAdapter mAdapter;
    private ListView mDrawerListView;


    public static SettingsFragment newInstance(){
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        mDrawerListView = (ListView) view;
        mDrawerListView.setOnItemClickListener(this);
        mAdapter = new DrawerAdapter(getActivity(), R.array.settings_items);
        mDrawerListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String tag = mAdapter.getTag(position-1);
        int itemId = ResourceUtils.getResourceId(ResourceUtils.ResourceType.ID, tag, getActivity());

        switch(itemId){
            case R.id.id_settings_my_account:
                break;
            case R.id.id_settings_sharing:
                break;
            case R.id.id_settings_my_music:
                break;
            case R.id.id_settings_my_level:
                break;
            case R.id.id_settings_my_connected_objects:
                break;
            case R.id.id_settings_about_soundfit:
                break;
            case R.id.id_settings_discover_app:
                break;
            case R.id.id_settings_contact_us:
                break;
        }
    }
}
