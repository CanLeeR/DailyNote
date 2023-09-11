package com.example.tamp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.tamp.R;
import com.example.tamp.data.repository.UserRepository;
import com.example.tamp.ui.activities.LoginActivity;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFragment extends Fragment {

    private TextView userNameTextView;
    private TextView diaryCountTextView;
    private TextView listCountTextView;
    private UserRepository userRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);  // 这样才能让Fragment添加自己的选项菜单。
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        userRepository = new UserRepository(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userNameTextView = view.findViewById(R.id.userName);
        diaryCountTextView = view.findViewById(R.id.diaryCount);
        listCountTextView = view.findViewById(R.id.listCount);

        fetchAndDisplayData();
        CircleImageView profileImageView = view.findViewById(R.id.userProfileImage);
        profileImageView.setOnClickListener(v -> onProfileImageClick(v));
    }

    private static final int PERMISSIONS_REQUEST_READ_STORAGE = 2;

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_STORAGE);
        }
    }

    private static final int IMAGE_PICK_REQUEST = 1;

    public void onProfileImageClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                CircleImageView profileImageView = getView().findViewById(R.id.userProfileImage);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_option) {
            //清除信息
            SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            // 跳转到登录界面并清除其他活动
            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchAndDisplayData() {
        // 获取用户名
        String username = userRepository.getUserName();
        userNameTextView.setText(username);
        // 获取日记数量
        userRepository.getDiaryCount(count -> {
            diaryCountTextView.setText (String.valueOf(count));
        });
        userRepository.getToDoCount(count -> {
            listCountTextView.setText(String.valueOf(count));
        });
    }
}
