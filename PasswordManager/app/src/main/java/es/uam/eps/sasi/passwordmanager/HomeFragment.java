package es.uam.eps.sasi.passwordmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.List;

import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDAO;
import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDatabase;
import es.uam.eps.sasi.passwordmanager.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private String username;

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
                R.layout.fragment_home,
                container,
                false
        );

        // Retrieve the username from arguments
        username = HomeFragmentArgs.fromBundle(getArguments()).getUsername();

        // Get list of sites to set the adapter
        List<Site> list = passwordManagerDAO.getUserSites(username);
        SiteAdapter adapter = new SiteAdapter(list, username);
        binding.siteListRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Bottom navigation
        ImageButton homeButton = binding.homeButton;
        ImageButton newSiteButton = binding.newSiteButton;
        ImageButton settingsButton = binding.settingsButton;

        // Home button filled
        homeButton.setImageResource(R.drawable.ic_twotone_vpn_key_24);
        homeButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.fill_green));

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
