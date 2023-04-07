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

import java.nio.charset.StandardCharsets;
import java.util.List;

import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDAO;
import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDatabase;
import es.uam.eps.sasi.passwordmanager.databinding.FragmentSignupBinding;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class SignupFragment extends Fragment {
    private FragmentSignupBinding binding;

    private String username;
    private String password;
    private String repeatedPassword;

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

        //BottomNavigationView menu = requireActivity().findViewById(R.id.navView);
        //menu.setVisibility(View.INVISIBLE);

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

        // Signed up
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

                    //menu.setVisibility(View.VISIBLE);

                    Navigation.findNavController(view)
                            .navigate(SignupFragmentDirections
                                    .actionSignupFragmentToHomeFragment(username));
                }
            }
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
