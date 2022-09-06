package com.example.wordwiki.ui_main.actionbar.setting.sub_settings;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentAboutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AboutFragment extends Fragment {
    private FragmentAboutBinding binding;
    FirebaseDatabase fireDatabase;
    ImageFilterView toLinkedin, toGit, toGmail;
    ImageButton toSettings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // set actionbar title
        TextView titleDialog = root.findViewById(R.id.toolbar_title);
        titleDialog.setText("");

        // setup feed of friends in the feed
        toLinkedin = root.findViewById(R.id.toLinkedn);
        toGit = root.findViewById(R.id.toGit);
        toGmail = root.findViewById(R.id.toGmail);
        toSettings = root.findViewById(R.id.toolbar_back_btn);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navCo = navHostFragment.getNavController();

        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("a", "onClick: aaaa");
                navCo.navigate(R.id.action_navigation_about_to_navigation_setting);
            }
        });

        setUpConnections();

        return root;
    }

    private void setUpConnections() {
        toLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.linkedin.com/in/sauliuslipkevicius/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        toGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/saulius-lipkevicius");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        toGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipientList = "sauliuslipkevicius@gmail.com";
                String[] recipients = recipientList.split(",");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Virtual Dictionary User Message");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi, ");

                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client."));
            }
        });
    }
}