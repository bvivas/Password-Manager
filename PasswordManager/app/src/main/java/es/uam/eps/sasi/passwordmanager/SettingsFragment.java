package es.uam.eps.sasi.passwordmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.List;

import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDAO;
import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDatabase;
import es.uam.eps.sasi.passwordmanager.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {

    // Binding
    private FragmentSettingsBinding binding;

    // Inner variables
    private String username;

    // Database
    PasswordManagerDatabase database = PasswordManagerDatabase.getInstance(App.getContext());
    PasswordManagerDAO passwordManagerDAO = database.getPasswordManagerDAO();

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
        username = SettingsFragmentArgs.fromBundle(getArguments()).getUsername();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set username in text
        binding.userName.setText(username);
        // Set number of sites
        List<Site> sites = passwordManagerDAO.getUserSites(username);
        int numSites = sites.size();
        if(numSites == 1) {
            binding.numSites.setText(numSites + " site");
        } else {
            binding.numSites.setText(numSites + " sites");
        }

        // Log out
        binding.logOutButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentToLoginFragment());
        });

        // Delete user account
        binding.deleteAccountButton.setOnClickListener(view -> {
            User user = passwordManagerDAO.getUser(username);
            // Delete user sites
            passwordManagerDAO.deleteUserSites(username);
            // Delete user
            passwordManagerDAO.deleteUser(user);

            // Go to login
            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentToLoginFragment());
        });

        // Bottom navigation
        ImageButton homeButton = binding.homeButton;
        ImageButton newSiteButton = binding.newSiteButton;
        ImageButton settingsButton = binding.settingsButton;

        // Settings button filled
        settingsButton.setImageResource(R.drawable.ic_twotone_person_outline_24);
        settingsButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.fill_green));

        // Go to home
        homeButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentToHomeFragment(username));
        });

        // Go to new site
        newSiteButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentToNewSiteFragment(username));
        });

        // Go to settings
        settingsButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(SettingsFragmentDirections
                            .actionSettingsFragmentSelf(username));
        });
    }
}
