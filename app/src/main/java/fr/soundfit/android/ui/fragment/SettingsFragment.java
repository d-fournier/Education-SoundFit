package fr.soundfit.android.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

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
        mDrawerListView = (ListView) view.findViewById(R.id.list);
        mDrawerListView.setOnItemClickListener(this);
        mAdapter = new DrawerAdapter(getActivity(), R.array.settings_items);
        mDrawerListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String tag = mAdapter.getTag(position);
        int itemId = ResourceUtils.getResourceId(ResourceUtils.ResourceType.ID, tag, getActivity());
        switch(itemId){
            case R.id.id_settings_my_account:
                showShortly(R.string.account_desc);
                break;
            case R.id.id_settings_sharing:
                showShortly(R.string.share_desc);
                break;
            case R.id.id_settings_my_music:
                showMusicDialog();
                break;
            case R.id.id_settings_my_level:
                showLevelDialog();
                break;
            case R.id.id_settings_my_connected_objects:
                showShortly(R.string.connected_things_desc);
                break;
            case R.id.id_settings_about_soundfit:
                showAbout();
                break;
            case R.id.id_settings_discover_app:
                showShortly(R.string.connected_things_desc);
                break;
            case R.id.id_settings_contact_us:
                contactUs();
                break;
        }
    }

    private void contactUs(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.contact_mail)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.contact_subject));
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.contact_chooser)));
    }

    private void showLevelDialog(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(LevelChooserFragment.TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = LevelChooserFragment.newInstance(true);
        newFragment.show(ft, LevelChooserFragment.TAG);
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    private void showMusicDialog(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(MusicPreferenceChooserFragment.TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = MusicPreferenceChooserFragment.newInstance(true);
        newFragment.show(ft, MusicPreferenceChooserFragment.TAG);
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    private void showAbout(){
        new MaterialDialog.Builder(getActivity()).title(R.string.about).content(R.string.about_desc)
                .positiveText(R.string.ok).show();
    }

    private void showShortly(int message){
        new MaterialDialog.Builder(getActivity()).title(R.string.shortly).content(message)
                .positiveText(R.string.ok).show();
    }

}
