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

import java.util.List;

import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDAO;
import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDatabase;
import es.uam.eps.sasi.passwordmanager.databinding.FragmentSignupBinding;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;


public class SignupFragment extends Fragment {

    // Binding
    private FragmentSignupBinding binding;

    // Inner variables
    private String username;
    private String password;
    private String repeatedPassword;

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
                R.layout.fragment_signup,
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
        binding.repeatPasswordTextInput.setText(null);

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

        // Update the password text field
        TextWatcher repeatedPasswordTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                repeatedPassword = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        binding.repeatPasswordTextInput.addTextChangedListener(repeatedPasswordTextWatcher);

        // Sign up
        binding.signupButton.setOnClickListener(view -> {
            // Add new user
            List<User> users = passwordManagerDAO.getUsers();
            boolean repeatedName = false;

            // Check username does not already exist
            if(!users.isEmpty()) {
                for(User u : users) {
                    if(username.equals(u.getName())) {
                        repeatedName = true;
                        break;
                    }
                }
            }
            if (repeatedName) {
                Snackbar.make(view, R.string.user_already_exists_error_message, Snackbar.LENGTH_LONG).show();
            } else {
                // Check both passwords match
                if(!password.equals(repeatedPassword)) {
                    Snackbar.make(view, R.string.passwords_match_error_message, Snackbar.LENGTH_LONG).show();
                } else {
                    // Encrypt password with SHA-256
                    String encryptedPassword = null;
                    try {
                        encryptedPassword = encryptPassword(password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    // Add user
                    User user = new User(username, encryptedPassword);
                    passwordManagerDAO.addUser(user);

                    // Go to home
                    Navigation.findNavController(view)
                            .navigate(SignupFragmentDirections
                                    .actionSignupFragmentToHomeFragment(username));
                }
            }
        });

        // Go to login
        binding.loginButton.setOnClickListener(view -> {
            Navigation.findNavController(view)
                    .navigate(SignupFragmentDirections
                            .actionSignupFragmentToLoginFragment());
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
