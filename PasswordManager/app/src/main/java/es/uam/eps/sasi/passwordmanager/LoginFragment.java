package es.uam.eps.sasi.passwordmanager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDAO;
import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDatabase;
import es.uam.eps.sasi.passwordmanager.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    private String username;
    private String password;

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
                R.layout.fragment_login,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        BottomNavigationView menu = requireActivity().findViewById(R.id.navView);
        menu.setVisibility(View.INVISIBLE);

        // Clear inputs
        binding.userTextInput.setText(null);
        binding.passwordTextInput.setText(null);

        // Update the user text field
        TextWatcher usernameTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        binding.userTextInput.addTextChangedListener(usernameTextWatcher);

        // Update the password text field
        TextWatcher passwordTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        binding.passwordTextInput.addTextChangedListener(passwordTextWatcher);

        // Log in
        binding.loginButton.setOnClickListener(view ->{
            // Check the user exists
            List<User> users = passwordManagerDAO.getUsers();
            boolean foundUser = false;

            for(User u : users) {
                if(username.equals(u.getName())) {
                    foundUser = true;
                }
            }
            if(!foundUser) {
                Snackbar.make(view, "user does not exist", Snackbar.LENGTH_LONG).show();
            } else {
                // Check password is correct
                // Encrypt input password with SHA-256
                String encryptedPassword = null;
                try {
                    encryptedPassword = encryptPassword(password);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                String userPassword = passwordManagerDAO.getUserPassword(username);
                if(!encryptedPassword.equals(userPassword)) {
                    Snackbar.make(view, "incorrect password", Snackbar.LENGTH_LONG).show();
                } else {
                    menu.setVisibility(view.VISIBLE);

                    Navigation.findNavController(view)
                            .navigate(LoginFragmentDirections
                                    .actionLoginFragmentToHomeFragment(username));
                }
            }
        });

        // Go to sign up
        binding.goToSignupText.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(LoginFragmentDirections
                            .actionLoginFragmentToSignupFragment());
        });
    }

    String encryptPassword(String planePassword) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(planePassword.getBytes());

        StringBuilder hexString = new StringBuilder();
        for(byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
