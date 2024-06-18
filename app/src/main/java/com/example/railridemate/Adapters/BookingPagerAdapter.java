package com.example.railridemate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railridemate.R;
import com.example.railridemate.models.BookingModel;

import java.util.List;

public class BookingPagerAdapter extends FragmentPagerAdapter {

    public BookingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Return the fragment for each position (true/false)
        if (position == 0) {
            return new ReturnOptionTrueFragment();
        } else {
            return new ReturnOptionFalseFragment();
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Set tab titles
        if (position == 0) {
            return "Return Option True";
        } else {
            return "Return Option False";
        }
    }
}