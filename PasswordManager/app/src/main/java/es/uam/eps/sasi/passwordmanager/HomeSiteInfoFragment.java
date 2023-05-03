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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDAO;
import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDatabase;
import es.uam.eps.sasi.passwordmanager.databinding.FragmentHomeSiteInfoBinding;

public class HomeSiteInfoFragment extends Fragment {

    private String username;
    private String siteId;

    private FragmentHomeSiteInfoBinding binding;

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
                R.layout.fragment_home_site_info,
                container,
                false
        );

        // Retrieve the username from arguments
        username = HomeSiteInfoFragmentArgs.fromBundle(getArguments()).getUsername();
        siteId = HomeSiteInfoFragmentArgs.fromBundle(getArguments()).getSiteId();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Site site = passwordManagerDAO.getSiteById(siteId, username);

        binding.siteTextText.setText(site.getName());

        // Decrypt password
        User user = passwordManagerDAO.getUser(username);
        String planePassword = null;
        try {
            planePassword = decryptPassword(site.getPassword(), user.getPassword());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        binding.passwordTextText.setText(planePassword);

        // Back to home
        binding.backToHomeFab.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeSiteInfoFragmentDirections
                    .actionHomeSiteInfoFragmentToHomeFragment(username));
        });

        // Delete site
        binding.deleteSiteButton.setOnClickListener(view -> {
            passwordManagerDAO.deleteSite(site);

            Navigation.findNavController(view)
                    .navigate(HomeSiteInfoFragmentDirections
                    .actionHomeSiteInfoFragmentToHomeFragment(username));
        });

        // Bottom navigation
        ImageButton homeButton = binding.homeButton;
        ImageButton newSiteButton = binding.newSiteButton;
        ImageButton settingsButton = binding.settingsButton;

        // Home button filled
        homeButton.setImageResource(R.drawable.ic_twotone_vpn_key_24);
        homeButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.fill_green));

        homeButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeSiteInfoFragmentDirections
                            .actionHomeSiteInfoFragmentToHomeFragment(username));
        });

        newSiteButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeSiteInfoFragmentDirections
                            .actionHomeSiteInfoFragmentToNewSiteFragment(username));
        });

        settingsButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeSiteInfoFragmentDirections
                            .actionHomeSiteInfoFragmentToSettingsFragment(username));
        });
    }

    String decryptPassword(String encryptedPassword, String masterKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // Get AES instance
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // Key specifications
        SecretKeySpec keySpec = new SecretKeySpec(Utils.hexStringToByteArray(masterKey), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] cipherText = cipher.doFinal(Utils.hexStringToByteArray(encryptedPassword));

        return Utils.toString(cipherText);
    }
}
