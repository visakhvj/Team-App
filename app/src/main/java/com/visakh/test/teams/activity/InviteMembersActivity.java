package com.visakh.test.teams.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.visakh.test.teams.R;
import com.visakh.test.teams.api.APIManager;
import com.visakh.test.teams.model.TeamDetails;

import java.util.HashMap;


public class InviteMembersActivity extends AppCompatActivity {

    final String TEAM_ID="test";

    APIManager apiManager;
    TeamDetails mTeamDetails;
    TextView txtCurrentMemberCount, txtLimitCount, txtSupportersCount, txtSupportersLimitCount;
    EditText edtPermission;

    Button btnShareQRCode,btnCopyLink;

    LinearLayout lnrSupporters;

    HashMap<String, String> roleLookUp;

    String invitationURL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Invite Members");
        setContentView(R.layout.activity_invite_members);
        initialize();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }

    void initialize() {
        apiManager = new APIManager();
        roleLookUp = new HashMap<>();

        txtCurrentMemberCount = findViewById(R.id.txtCurrentMemberCount);
        txtLimitCount = findViewById(R.id.txtLimitCount);
        txtSupportersCount = findViewById(R.id.txtSupportersCount);
        txtSupportersLimitCount = findViewById(R.id.txtSupportersLimitCount);
        lnrSupporters = findViewById(R.id.lnrSupporters);
        edtPermission = findViewById(R.id.edtPermission);
        btnShareQRCode = findViewById(R.id.btnShareQRCode);
        btnCopyLink = findViewById(R.id.btnCopyLink);

        txtCurrentMemberCount.setText("");
        txtLimitCount.setText("");
        txtSupportersCount.setText("");
        txtSupportersLimitCount.setText("");

        edtPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadInvitationPermission();
            }
        });

        btnCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!invitationURL.equals("")){
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Invitation URL",invitationURL);
                    clipboard.setPrimaryClip(clip);
                }
                else {
                    Toast.makeText(InviteMembersActivity.this, "Please select the permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShareQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!invitationURL.equals("")){
                    Intent intent = new Intent(InviteMembersActivity.this, QRCodeActivity.class);
                    intent.putExtra(QRCodeActivity.INVITATION_URL,invitationURL);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(InviteMembersActivity.this, "Please select the permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new LoadTeamDetails().execute("");
    }

    void loadInvitationPermission() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(InviteMembersActivity.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(InviteMembersActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Coach");
        arrayAdapter.add("Player Coach");
        arrayAdapter.add("Player");
        if (mTeamDetails.plan.supporterLimit != 0)
            arrayAdapter.add("Supporter");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                //Check and disable the click
                String value = roleLookUp.get(strName);
                if(mTeamDetails.members.total<mTeamDetails.plan.memberLimit) {
                    edtPermission.setText(strName);
                    new CreateInvitation().execute(value);
                    dialog.dismiss();
                }
            }
        });

        builderSingle.show();
    }


    class LoadTeamDetails extends AsyncTask<String, TeamDetails, TeamDetails> {

        @Override
        protected TeamDetails doInBackground(String... strings) {
            roleLookUp.put("Coach", "manager");
            roleLookUp.put("Player Coach", "editor");
            roleLookUp.put("Player", "member");
            roleLookUp.put("Supporter", "readonly");
            return apiManager.getTeamDetails(TEAM_ID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(TeamDetails teamDetails) {
            super.onPostExecute(teamDetails);
            if (teamDetails != null) {
                //Update the current list details
                txtCurrentMemberCount.setText(Integer.toString(teamDetails.members.total - teamDetails.members.supporters));
                txtSupportersCount.setText(Integer.toString(teamDetails.members.supporters));
                txtLimitCount.setText((Integer.toString(teamDetails.plan.memberLimit)));
                if (teamDetails.plan.supporterLimit != 0) {
                    txtSupportersLimitCount.setText(Integer.toString(teamDetails.plan.supporterLimit));
                } else {
                    //Hide the supporter session
                    lnrSupporters.setVisibility(View.GONE);
                }
            }
            mTeamDetails = teamDetails;
        }
    }

    class CreateInvitation extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return apiManager.createInvitationURL(TEAM_ID,strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            invitationURL = s;
        }
    }


}