package com.student.uniflow.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.student.uniflow.R;
import com.student.uniflow.database.DatabaseHelper;
import com.student.uniflow.model.UserProfile;

public class ProfileFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TextView tvName, tvNim, tvProgram;
    private TextView tvStatDone, tvStatPending;
    private View cardProfile, statsRow;
    private Button btnEditProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext());

        tvName = view.findViewById(R.id.tvProfileName);
        tvNim = view.findViewById(R.id.tvProfileNim);
        tvProgram = view.findViewById(R.id.tvProfileProgram);
        tvStatDone = view.findViewById(R.id.tvStatDone);
        tvStatPending = view.findViewById(R.id.tvStatPending);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        cardProfile = view.findViewById(R.id.cardProfile);
        statsRow = view.findViewById(R.id.statsRow);

        btnEditProfile.setOnClickListener(v -> showEditDialog());

        // Animasi slide-up untuk kartu profil
        if (cardProfile != null) {
            cardProfile.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up_fade));
        }
        // Animasi pop untuk baris statistik
        if (statsRow != null) {
            statsRow.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.pop_scale));
        }

        refreshProfile();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshProfile();
    }

    private void refreshProfile() {
        UserProfile profile = dbHelper.getProfile();

        if (profile != null && profile.getName() != null && !profile.getName().isEmpty()) {
            tvName.setText(profile.getName());
            tvNim.setText(profile.getNim() != null && !profile.getNim().isEmpty()
                    ? profile.getNim() : "-");
            tvProgram.setText(profile.getProgram() != null && !profile.getProgram().isEmpty()
                    ? profile.getProgram() : "-");
        } else {
            tvName.setText("Belum diisi");
            tvNim.setText("-");
            tvProgram.setText("-");
        }

        tvStatDone.setText(String.valueOf(dbHelper.getCompletedTaskCount()));
        tvStatPending.setText(String.valueOf(dbHelper.getPendingTaskCount()));
    }

    private void showEditDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_edit_profile, null);

        EditText etName = dialogView.findViewById(R.id.etProfileName);
        EditText etNim = dialogView.findViewById(R.id.etProfileNim);
        EditText etProgram = dialogView.findViewById(R.id.etProfileProgram);

        UserProfile existing = dbHelper.getProfile();
        if (existing != null) {
            etName.setText(existing.getName());
            etNim.setText(existing.getNim());
            etProgram.setText(existing.getProgram());
        }

        new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setTitle("Edit Profil")
                .setView(dialogView)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String nim = etNim.getText().toString().trim();
                    String program = etProgram.getText().toString().trim();

                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(requireContext(),
                                "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    UserProfile profile = new UserProfile(name, nim, program);
                    dbHelper.saveProfile(profile);
                    Toast.makeText(requireContext(), "Profil disimpan!", Toast.LENGTH_SHORT).show();
                    refreshProfile();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}
