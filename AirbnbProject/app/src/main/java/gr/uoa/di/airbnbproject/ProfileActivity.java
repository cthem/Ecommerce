package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import fromRESTful.Users;
import util.ListAdapterProfile;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.getSessionData;

public class ProfileActivity extends AppCompatActivity {
    Users loggedinUser;
    String username, token;
    ListView list;
    Context c;

    ImageButton btnMenu;
    ImageView userImage;

    ListAdapterProfile adapter;
    String[] userdetails;
    Boolean user;
    RetrofitCalls retrofitCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = getSessionData(ProfileActivity.this);
        if (!sessionData.getUserLoggedInState()) {
            Utils.logout(this);
            finish();
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        token = sessionData.getToken();
        username = sessionData.getUsername();
        c = this;

        if(Utils.isTokenExpired(token)) {
            Utils.logout(this);
            finish();
        }

        setContentView(R.layout.activity_profile);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        btnMenu = (ImageButton)findViewById(R.id.btnMenu);

        userdetails = new String[9];
        manageToolbarButtons();
        retrofitCalls = new RetrofitCalls();
        loggedinUser    = retrofitCalls.getUserbyUsername(token, username).get(0);
        userdetails[0]  = loggedinUser.getFirstName();
        userdetails[1]  = loggedinUser.getLastName();
        userdetails[2]  = loggedinUser.getUsername();
        userdetails[3]  = loggedinUser.getEmail();
        userdetails[4]  = loggedinUser.getPhoneNumber();
        userdetails[5]  = loggedinUser.getBirthDate();
        userdetails[6]  = loggedinUser.getCountry();
        userdetails[7]  = loggedinUser.getCity();
        userdetails[8]  = loggedinUser.getAbout();

        adapter = new ListAdapterProfile(this, userdetails);
        list = (ListView)findViewById(R.id.profilelist);
        list.setAdapter(adapter);

        userImage = (ImageView) findViewById(R.id.userImage);
        Utils.loadProfileImage(ProfileActivity.this, userImage, loggedinUser.getPhoto());

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(ProfileActivity.this, user);

        /** BACK BUTTON **/
        Utils.manageBackButton(this, (user)?HomeActivity.class:HostActivity.class, user);
    }

    public void manageToolbarButtons() {
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ProfileActivity.this, btnMenu);
                popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Bundle buser = new Bundle();
                        buser.putBoolean("type", user);
                        if (item.getItemId() == R.id.reviews) {
                            Intent historyReviewsIntent = new Intent(ProfileActivity.this, HistoryReviewsActivity.class);
                            historyReviewsIntent.putExtras(buser);
                            startActivity(historyReviewsIntent);
                        } else if (item.getItemId() == R.id.reservations) {
                            Intent historyReservationsIntent = new Intent(ProfileActivity.this, HistoryReservationsActivity.class);
                            historyReservationsIntent.putExtras(buser);
                            startActivity(historyReservationsIntent);
                        } else if (item.getItemId() == R.id.editprofile) {
                            Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                            editIntent.putExtras(buser);
                            startActivity(editIntent);
                        } else if (item.getItemId() == R.id.deleteProfile) {
                            new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("Delete Account").setMessage("Do you really want to delete your account?").setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if (retrofitCalls.deleteUserById(token, loggedinUser.getId().toString()) == null) {
                                            Toast.makeText(c, "Account deleted!", Toast.LENGTH_SHORT).show();
                                            Utils.logout(ProfileActivity.this);
                                        } else {
                                            Toast.makeText(c, "Something went wrong, account is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }
}