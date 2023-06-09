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

import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDAO;
import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDatabase;
import es.uam.eps.sasi.passwordmanager.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

    // Binding
    private FragmentLoginBinding binding;

    // Inner variables
    private String username;
    private String password;

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
                R.layout.fragment_login,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

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
            // Check fields are not empty
            if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
                Snackbar.make(view, R.string.empty_error_message, Snackbar.LENGTH_LONG).show();
            } else {
                // Check the user exists
                List<User> users = passwordManagerDAO.getUsers();
                boolean foundUser = false;

                for(User u : users) {
                    if(username.equals(u.getName())) {
                        foundUser = true;
                    }
                }
                if(!foundUser) {
                    Snackbar.make(view, R.string.user_not_found_error_message, Snackbar.LENGTH_LONG).show();
                } else {
                    // Check password is correct
                    // Encrypt input password with SHA-256
                    String encryptedPassword = null;
                    try {
                        encryptedPassword = encryptPassword(password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    // Check if they match
                    String userPassword = passwordManagerDAO.getUserPassword(username);
                    if(!encryptedPassword.equals(userPassword)) {
                        Snackbar.make(view, R.string.incorrect_password_error_message, Snackbar.LENGTH_LONG).show();
                    } else {
                        // Go to home
                        Navigation.findNavController(view)
                                .navigate(LoginFragmentDirections
                                        .actionLoginFragmentToHomeFragment(username));
                    }
                }
            }
        });

        // Go to sign up
        binding.signupButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(LoginFragmentDirections
                            .actionLoginFragmentToSignupFragment());
        });
    }

    // Method to encrypt password and store it using SHA-256 hash function
    String encryptPassword(String planePassword) throws NoSuchAlgorithmException {
        // Get digest
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(planePassword.getBytes());

        // Parse it to hexadecimal
        return Utils.toHex(hash);
    }
}
