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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDAO;
import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDatabase;
import es.uam.eps.sasi.passwordmanager.databinding.FragmentHomeSiteInfoBinding;


public class HomeSiteInfoFragment extends Fragment {

    // Binding
    private FragmentHomeSiteInfoBinding binding;

    // Inner variables
    private String username;
    private String siteId;

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
                R.layout.fragment_home_site_info,
                container,
                false
        );

        // Retrieve the username from arguments
        username = HomeSiteInfoFragmentArgs.fromBundle(getArguments()).getUsername();
        // Retrieve the site id
        siteId = HomeSiteInfoFragmentArgs.fromBundle(getArguments()).getSiteId();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get site
        Site site = passwordManagerDAO.getSiteById(siteId, username);

        // Site name
        binding.siteTextText.setText(site.getName());
        binding.passwordTextText.setText(R.string.hide_password_text);

        // Decrypt password
        User user = passwordManagerDAO.getUser(username);
        String planePassword = null;
        try {
            planePassword = decryptPassword(site.getPassword(), user.getPassword());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        // See password button
        AtomicBoolean buttonClicked = new AtomicBoolean(false);
        ImageButton passwordButton = binding.showPasswordButton;
        String finalPlanePassword = planePassword;
        passwordButton.setOnClickListener(view -> {
            if(!buttonClicked.get()) {
                buttonClicked.set(true);
                passwordButton.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
                binding.passwordTextText.setText(finalPlanePassword);
            } else {
                buttonClicked.set(false);
                passwordButton.setImageResource(R.drawable.ic_outline_remove_red_eye_24);
                binding.passwordTextText.setText(R.string.hide_password_text);
            }
        });


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

        // Go to home
        homeButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeSiteInfoFragmentDirections
                            .actionHomeSiteInfoFragmentToHomeFragment(username));
        });

        // Go to new site
        newSiteButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeSiteInfoFragmentDirections
                            .actionHomeSiteInfoFragmentToNewSiteFragment(username));
        });

        // Go to settings
        settingsButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(HomeSiteInfoFragmentDirections
                            .actionHomeSiteInfoFragmentToSettingsFragment(username));
        });
    }

    // Method to decrypt the password using AES-256 in CBC mode
    String decryptPassword(String encryptedPassword, String masterKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        // Get AES instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // Key specifications
        SecretKeySpec keySpec = new SecretKeySpec(Utils.hexStringToByteArray(masterKey), "AES");

        // Get IV and password
        byte[] input = Utils.hexStringToByteArray(encryptedPassword);
        byte[] IV = Arrays.copyOfRange(input, 0, 16);
        byte[] password = Arrays.copyOfRange(input, 16, input.length);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(IV));

        // Decrypt
        byte[] cipherText = cipher.doFinal(password);

        // Parse to string
        return Utils.toString(cipherText);
    }
}
