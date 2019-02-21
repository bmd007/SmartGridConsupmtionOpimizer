package ir.tiroon.schedulingApp.CustomLayout;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.tiroon.schedulingApp.Adapter.NavDrawerListAdapter;
import ir.tiroon.schedulingApp.Dialog.LoginDialog;
import ir.tiroon.schedulingApp.Fragment.DeviceListFragment;
import ir.tiroon.schedulingApp.Util.Languages;
import ir.tiroon.schedulingApp.Util.MyUtil;
import schedulingApp.tiroon.ir.R;

/**
 * Created by Lenovo on 21/10/2017.
 */
@SuppressWarnings("ResourceType")
public class MyDrawer {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;



    ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<>();
    NavDrawerListAdapter adapter;

    String[] mDrawerTitles;
    private Activity context;

    public MyDrawer(final Activity context) {

        this.context = context;
        
        mDrawerLayout = (DrawerLayout) context.findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(context, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == R.id.action_drawer)
                    if (mDrawerLayout.isDrawerOpen(Gravity.START))
                        mDrawerLayout.closeDrawer(Gravity.START);
                    else
                        mDrawerLayout.openDrawer(Gravity.START);


                return false;
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                context.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                context.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        //----------------------

        mDrawerTitles = context.getResources().getStringArray(R.array.drawer_item_names);
        TypedArray navMenuIcons = context.getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerList = (ListView) context.findViewById(R.id.left_drawer);

        navDrawerItems.add(new NavDrawerItem(mDrawerTitles[0], navMenuIcons.getResourceId(0, -1)));

        View header = context.getLayoutInflater().inflate(R.layout.nav_header_drawer_test, null);
        mDrawerList.addHeaderView(header);

        adapter = new NavDrawerListAdapter(context, navDrawerItems);
        mDrawerList.setAdapter(adapter);

//        mDrawerList.addFooterView(counterView);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyDrawer.this.selectAMenu(position);
            }
        });


        context.getWindow().getDecorView().setLayoutDirection(MyUtil.languageSelector == Languages.english ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);

        mDrawerList.setLayoutDirection(MyUtil.languageSelector == Languages.persian ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        mDrawerList.setTextDirection(MyUtil.languageSelector == Languages.persian ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);

        mDrawerLayout.setLayoutDirection(MyUtil.languageSelector == Languages.persian ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        mDrawerLayout.setTextDirection(MyUtil.languageSelector == Languages.persian ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);

    }

    public void selectAMenu(int position) {

        selectFragment(position);

        mDrawerList.setItemChecked(position, true);
        context.setTitle(mDrawerTitles[position - 1]);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    private void selectFragment(int position) {

        switch (position) {
            case 1: {
                    Fragment selectedFragment = DeviceListFragment.newInstance(position);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        selectedFragment.setEnterTransition(new Slide(Gravity.END));
                        selectedFragment.setExitTransition(new Slide(Gravity.START));
                    }
                    android.app.FragmentTransaction ft = context.getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, selectedFragment);
                    ft.setCustomAnimations(R.animator.enter, R.animator.exit);
//                    ft.addToBackStack("fragment");
                    ft.commit();

                break;
            }

        }

        MyUtil.LastSelectedFragmentNumber = position;
    }

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    public ListView getmDrawerList() {
        return mDrawerList;
    }

    public ActionBarDrawerToggle getmDrawerToggle() {
        return mDrawerToggle;
    }

    public ArrayList<NavDrawerItem> getNavDrawerItems() {
        return navDrawerItems;
    }

    public NavDrawerListAdapter getAdapter() {
        return adapter;
    }

    public String[] getmDrawerTitles() {
        return mDrawerTitles;
    }
}