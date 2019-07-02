package com.orshoham.statsme.tab4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.orshoham.statsme.MainActivity;
import com.orshoham.statsme.R;

public class Tab4Settings extends Fragment {

    private FirebaseAuth auth;

    ListView list;

    String[] itemname ={
            "Game Rules",
            "Edit Profile",
            "Share",
            "Contact Us",
            "About",
            "Logout"
    };

    Integer[] imgid={
            R.drawable.setting_game_rules,
            R.drawable.setting_edit_profile,
            R.drawable.setting_share,
            R.drawable.setting_contact_us,
            R.drawable.setting_about,
            R.drawable.setting_logout,
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4_settings, container, false);

        auth = FirebaseAuth.getInstance();

        Tab4CustomListAdapter adapter=new Tab4CustomListAdapter(getActivity(), itemname, imgid);
        list =(ListView)rootView.findViewById(R.id.settingListView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Log.i("position", Integer.toString(position));
                if (position == 0){
                    Intent intent = new Intent(getActivity(), Tab4GameRules.class);
                    startActivity(intent);
                }
                if (position == 1){
                    Intent intent = new Intent(getActivity(), Tab4EditProfile.class);
                    startActivity(intent);
                }
                if (position == 2){
                    Intent i=new Intent(android.content.Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject test");
                    i.putExtra(android.content.Intent.EXTRA_TEXT, "Hey! this text has sent from StatsMe");
                    startActivity(Intent.createChooser(i,"Share via"));
                }
                if (position == 3){
                    Intent intent = new Intent(getActivity(), Tab4ContactUs.class);
                    startActivity(intent);
                }
                if (position == 4){
                    Intent intent = new Intent(getActivity(), Tab4About.class);
                    startActivity(intent);
                }
                if (position == 5){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Do you want to logout?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    auth.signOut();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });

        return rootView;
    }


}