package es.uam.eps.sasi.passwordmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import es.uam.eps.sasi.passwordmanager.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private String username;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home,
                container,
                false
        );

        // Retrieve the username from arguments
        username = HomeFragmentArgs.fromBundle(getArguments()).getUsername();


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Button homeButton = binding.homeButton;
        Button newSiteButton = binding.newSiteButton;
        Button settingsButton = binding.settingsButton;

        homeButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeFragmentDirections
                            .actionHomeFragmentSelf(username));
        });

        newSiteButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeFragmentDirections
                            .actionHomeFragmentToNewSiteFragment(username));
        });

        settingsButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeFragmentDirections
                            .actionHomeFragmentToSettingsFragment(username));
        });
    }
}
