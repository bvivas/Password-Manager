package es.uam.eps.sasi.passwordmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import es.uam.eps.sasi.passwordmanager.databinding.FragmentNewSiteBinding;

public class NewSiteFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        FragmentNewSiteBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_new_site,
                container,
                false
        );

        return binding.getRoot();
    }
}
