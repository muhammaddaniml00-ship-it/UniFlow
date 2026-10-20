package com.student.uniflow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.student.uniflow.MainActivity;
import com.student.uniflow.R;
import com.student.uniflow.adapter.RecentTaskAdapter;
import com.student.uniflow.database.DatabaseHelper;
import com.student.uniflow.model.Task;
import com.student.uniflow.model.UserProfile;

import java.util.List;

public class HomeFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TextView tvGreeting, tvTotalCount, tvPendingCount, tvDoneCount;
    private RecyclerView rvRecent;
    private RecentTaskAdapter recentAdapter;
    private View emptyRecentView;
    private Button btnGoToTasks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext());

        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvTotalCount = view.findViewById(R.id.tvTotalCount);
        tvPendingCount = view.findViewById(R.id.tvPendingCount);
        tvDoneCount = view.findViewById(R.id.tvDoneCount);
        rvRecent = view.findViewById(R.id.rvRecentTasks);
        emptyRecentView = view.findViewById(R.id.tvEmptyRecent);
        btnGoToTasks = view.findViewById(R.id.btnGoToTasks);

        rvRecent.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Task> recentTasks = dbHelper.getRecentTasks(5);
        recentAdapter = new RecentTaskAdapter(requireContext(), recentTasks);
        rvRecent.setAdapter(recentAdapter);

        btnGoToTasks.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTasks();
            }
        });

        refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        UserProfile profile = dbHelper.getProfile();
        if (profile != null && profile.getName() != null && !profile.getName().isEmpty()) {
            tvGreeting.setText("Halo, " + profile.getName() + " 👋");
        } else {
            tvGreeting.setText("Halo, Mahasiswa 👋");
        }

        int total = dbHelper.getTotalTaskCount();
        int pending = dbHelper.getPendingTaskCount();
        int done = dbHelper.getCompletedTaskCount();

        tvTotalCount.setText(String.valueOf(total));
        tvPendingCount.setText(String.valueOf(pending));
        tvDoneCount.setText(String.valueOf(done));

        List<Task> recentTasks = dbHelper.getRecentTasks(5);
        recentAdapter.setTaskList(recentTasks);

        if (recentTasks.isEmpty()) {
            rvRecent.setVisibility(View.GONE);
            emptyRecentView.setVisibility(View.VISIBLE);
        } else {
            rvRecent.setVisibility(View.VISIBLE);
            emptyRecentView.setVisibility(View.GONE);
        }
    }
}
