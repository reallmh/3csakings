package io.lmh.e.a3cs_akings.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.lmh.e.a3cs_akings.Message.ActiveListFragment;
import io.lmh.e.a3cs_akings.Message.MessageListFragment;
import io.lmh.e.a3cs_akings.R;

/**
 * Created by E on 8/6/2018.
 */

public class MessageFragment extends Fragment {

    //declare views
    ViewPager messageviewpager;
    TabLayout messagetabs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.messagefragment, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(this.isVisible()){
            if(isVisibleToUser){
                setUpViewPager(messageviewpager);
                messagetabs.setupWithViewPager(messageviewpager);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize views
        messagetabs = (TabLayout) view.findViewById(R.id.messagetabs);
        messageviewpager = (ViewPager) view.findViewById(R.id.messageviewpager);
        setUpViewPager(messageviewpager);
        messagetabs.setupWithViewPager(messageviewpager);


    }

    //for fragments
    class MessageViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragements = new ArrayList<>();
        private final List<String> mFragmentName = new ArrayList<>();

        public MessageViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragements.get(position);
        }

        @Override
        public int getCount() {
            return mFragements.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragements.add(fragment);
            mFragmentName.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return mFragmentName.get(position);
        }

    }


    //set up pager method
    private void setUpViewPager(ViewPager viewPager) {
        MessageFragment.MessageViewPagerAdapter adapter = new MessageFragment.MessageViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new MessageListFragment(), "messages");
        adapter.addFragment(new ActiveListFragment(), "active list");
        viewPager.setAdapter(adapter);

    }
}
