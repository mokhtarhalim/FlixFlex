package com.halim.flixflex.Profil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.halim.flixflex.ClassesUtils.LoadingDialog;
import com.halim.flixflex.ClassesUtils.StaticMethods;
import com.halim.flixflex.Connection.LoginActivity;
import com.halim.flixflex.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilFragment extends Fragment {

    private Context context;

    private TextView btnLogOut;
    private LinearLayout changeEmailLayout;
    private LinearLayout changePasswordLayout;

    public ProfilFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.profil_fragment, container, false);

        context = rootView.getContext();

        findViewsById(rootView);

        //Button to show bottomSheet to update Email
        changeEmailLayout.setOnClickListener(view -> showBottomSheetDialog("Email"));

        //Button to show bottomSheet to update Password
        changePasswordLayout.setOnClickListener(view -> showBottomSheetDialog("Password"));

        //Button Log out from application
        btnLogOut.setOnClickListener(view -> {

            FirebaseAuth.getInstance().signOut();

            context.startActivity(new Intent(context, LoginActivity.class));
            ((AppCompatActivity) context).finishAffinity();
        });

        return rootView;
    }

    private void findViewsById(View rootView) {
        btnLogOut = rootView.findViewById(R.id.btnLogOut);
        changeEmailLayout = rootView.findViewById(R.id.changeEmailLayout);
        changePasswordLayout = rootView.findViewById(R.id.changePasswordLayout);
    }

    //Custom bottom sheet to update email and password
    private void showBottomSheetDialog(String typeUpdate) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_update);

        TextInputLayout newEmailLayout = bottomSheetDialog.findViewById(R.id.newEmailLayout);
        TextInputLayout newPasswordLayout = bottomSheetDialog.findViewById(R.id.newPasswordLayout);
        TextInputEditText currentEmailInput = bottomSheetDialog.findViewById(R.id.currentEmailInput);
        TextInputEditText newEmailInput = bottomSheetDialog.findViewById(R.id.newEmailInput);
        TextInputEditText currentPasswordInput = bottomSheetDialog.findViewById(R.id.currentPasswordInput);
        TextInputEditText newPasswordInput = bottomSheetDialog.findViewById(R.id.newPasswordInput);
        TextView btnUpdate = bottomSheetDialog.findViewById(R.id.btnUpdate);

        if (typeUpdate.equals("Email")) {
            //Updating Email
            newPasswordLayout.setVisibility(View.GONE);
            btnUpdate.setOnClickListener(view -> {
                if (StaticMethods.validateEmail(newEmailInput.getText().toString().trim())) {
                    updateEmail(bottomSheetDialog, currentEmailInput.getText().toString().trim(), newEmailInput.getText().toString().trim(), currentPasswordInput.getText().toString().trim());
                } else {
                    Toast.makeText(context, context.getString(R.string.email_not_valid), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            //Updating Password
            newEmailLayout.setVisibility(View.GONE);
            if (StaticMethods.checkPassword(newPasswordInput.getText().toString().trim())) {
                btnUpdate.setOnClickListener(view -> updatePassword(bottomSheetDialog, currentEmailInput.getText().toString().trim(), currentPasswordInput.getText().toString().trim(), newPasswordInput.getText().toString().trim()));
            } else {
                Toast.makeText(context, context.getString(R.string.password_must_contains), Toast.LENGTH_SHORT).show();
            }

        }

        bottomSheetDialog.show();
    }

    private void updateEmail(BottomSheetDialog bottomSheetDialog, String currentEmail, String newEmail, String password) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Check if current email and password are valid in Firebase
        auth.signInWithEmailAndPassword(currentEmail, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

                //Updating user's Email
                userAuth.updateEmail(newEmail).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "Email Changed" + " Current Email is " + newEmail, Toast.LENGTH_LONG).show();
                    } else {
                        loadingDialog.dismiss();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(context, context.getString(R.string.oops_an_error_has_occurred), Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                    bottomSheetDialog.dismiss();
                });

            } else {
                loadingDialog.dismiss();
                bottomSheetDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.wrong_email_or_password), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            loadingDialog.dismiss();
            bottomSheetDialog.dismiss();
            Toast.makeText(context, context.getString(R.string.wrong_email_or_password), Toast.LENGTH_SHORT).show();
        });

    }

    private void updatePassword(BottomSheetDialog bottomSheetDialog, String email, String currentPassword, String newPassword) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Check if current email and password are valid in Firebase
        auth.signInWithEmailAndPassword(email, currentPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

                //Updating user's Email
                userAuth.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "Password Changed" + " Current Password is " + newPassword, Toast.LENGTH_LONG).show();
                    } else {
                        loadingDialog.dismiss();
                        bottomSheetDialog.dismiss();
                        Toast.makeText(context, context.getString(R.string.oops_an_error_has_occurred), Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                    bottomSheetDialog.dismiss();
                });

            } else {
                loadingDialog.dismiss();
                bottomSheetDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.wrong_email_or_password), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            loadingDialog.dismiss();
            bottomSheetDialog.dismiss();
            Toast.makeText(context, context.getString(R.string.wrong_email_or_password), Toast.LENGTH_SHORT).show();
        });

    }

}
