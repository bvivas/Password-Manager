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

import es.uam.eps.sasi.passwordmanager.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private String username;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_settings,
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
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentToHomeFragment(username));
        });

        newSiteButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentToNewSiteFragment(username));
        });

        settingsButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentSelf(username));
        });
    }
}
