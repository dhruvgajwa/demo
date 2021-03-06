package com.getmate.demo181201.createEvent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.getmate.demo181201.Activities.MainActivity;
import com.getmate.demo181201.CurrentUserData;
import com.getmate.demo181201.CustomViews.EventTimelineImageView;
import com.getmate.demo181201.InterestSelection.RoundedImageView;
import com.getmate.demo181201.Objects.Event;
import com.getmate.demo181201.Objects.Profile;
import com.getmate.demo181201.R;
import com.getmate.demo181201.VolleyClasses.AppController;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PreviewEventActivity extends AppCompatActivity {
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    TextView salesStartFrom,salesStopAt,maxNoOfTickets;
    Event event = new Event();
    ArrayList<String> interestB = new ArrayList<>();
    ArrayList<String> interestI = new ArrayList<>();
    ArrayList<String> interestE = new ArrayList<>();
    ArrayList<String> allParentTags = new ArrayList<>();
    ArrayList<Event.Organisers> organisers = new ArrayList<>();
    ProgressDialog progressDialog;

    RelativeLayout link_relative_layout;
    EventTimelineImageView eventTimelineImageView;
    TextView title,venue,description,creatorName,creatorEmail,creatorPhone,creatorHandle;
    TextView orgname1,orgName2,orgName3;
    TextView orgEmail1,orgEmail2,orgEmail3;
    TextView orgP1,orgP2,orgP3;
    RoundedImageView creatorProfilePic;
    Button link,date,going,buyTicket;
    TextView link1,going1;
    Button cEmail,cPhone;
    LinearLayout org1,org2,org3;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    TextView from,to,ticketPrice,eventType;
    Button done,saveDraft;
    FirebaseFirestore db;
    FlexboxLayout flexboxLayout;


    public  void findViewsById(){
        eventTimelineImageView = findViewById(R.id.event_poster_sa);
        title = findViewById(R.id.title_sa);
        venue = findViewById(R.id.venue_sa);
        description = findViewById(R.id.description_sa);
        //link = findViewById(R.id.link_btn);
        link1 = findViewById(R.id.link_text);
        //date1 = findViewById(R.id.time_sa);
        going1 = findViewById(R.id.going_count);
        creatorName = findViewById(R.id.creator_name_sa);
        creatorPhone = findViewById(R.id.creator_phone_sa);
        creatorEmail = findViewById(R.id.creator_email_sa);
        creatorProfilePic = findViewById(R.id.creator_prfl_pic_sa);
        creatorHandle = findViewById(R.id.creator_handle_sa);
        orgname1 = findViewById(R.id.org1Name);
        orgEmail1 = findViewById(R.id.org1Email);
        orgP1 = findViewById(R.id.org1Phone);
        orgName2 = findViewById(R.id.org2Name);
        orgEmail2 = findViewById(R.id.org2Email);
        orgP2 = findViewById(R.id.org2Phone);
        orgName3 = findViewById(R.id.org3Name);
        orgEmail3 = findViewById(R.id.org3Email);
        orgP3 = findViewById(R.id.org3Phone);
        org1 = findViewById(R.id.organiserLinLay1);
        org2 = findViewById(R.id.organiserLinLay2);
        org3 = findViewById(R.id.organiserLinLay3);
        //buyTicket = findViewById(R.id.buy_ticket);
        cEmail = findViewById(R.id.creator_email_de);
        cPhone = findViewById(R.id.creator_phone_de);
        from = findViewById(R.id.from_text);
        to = findViewById(R.id.to_text);
        link_relative_layout = findViewById(R.id.link_rl);
        done = findViewById(R.id.done);
        saveDraft = findViewById(R.id.save_draft);
        ticketPrice = findViewById(R.id.ticket_price);
        eventType = findViewById(R.id.event_type);
        flexboxLayout = findViewById(R.id.tags_view);
        salesStartFrom = findViewById(R.id.sales_start_from);
        salesStopAt = findViewById(R.id.sales_stop_at);
        maxNoOfTickets = findViewById(R.id.max_no_of_tickets);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_event);

        Intent i = getIntent();
        findViewsById();
        String description= i.getStringExtra("description");
        String title = i.getStringExtra("title");
        Long from = i.getLongExtra("from",0);
        Long to = i.getLongExtra("to",0);

        progressDialog  = new ProgressDialog(PreviewEventActivity.this);
        interestB = i.getStringArrayListExtra("interestB");
        interestI = i.getStringArrayListExtra("interestI");
        interestE = i.getStringArrayListExtra("interestE");
        allParentTags = i.getStringArrayListExtra("AllParentInterests");

        ArrayList<String> allTags = new ArrayList<>();
        allTags.addAll(interestB);
        allTags.addAll(interestE);
        allTags.addAll(interestI);
        event.setTags(allTags);

        double lat = i.getDoubleExtra("lat",0);
        double lon = i.getDoubleExtra("lon",0);
        String address = i.getStringExtra("address");
        String  city = i.getStringExtra("city");
        String imageUri = i.getStringExtra("imageUri");
        Gson gson = new Gson();
         Double ticketPrice = Double.valueOf(i.getStringExtra("ticketPrice"));
        ArrayList<String > o = new ArrayList<>();
        o = i.getStringArrayListExtra("organisers");


        if(o!=null){
            for (int j=0; j<o.size();j++){
                organisers.add(gson.fromJson(o.get(j),Event.Organisers.class));
            }
        }
        else {

        }

        //USe this shit
        String eventType = i.getStringExtra("eventType");
        String link = i.getStringExtra("link");


        event.setTo(to);
        event.setFrom(from);
        event.setTitle(title);
        event.setDescription(description);
        //TODO: ADD To AND FROM TIMESTAMO IN LONG
        event.setAllParentTags(allParentTags);

        event.setLat(lat);
        event.setLon(lon);
        event.setAddress(address);
        event.setCity(city);
        event.setImageUrl(imageUri);
        event.setOrganisers(organisers);
        Profile currentUserProfile = CurrentUserData.getInstance().getCurrent();
        event.setCreatorName(currentUserProfile.getName());
        event.setCreatorFirebaseId(currentUserProfile.getFirebase_id());
        event.setCreatorProfilePic(currentUserProfile.getProfilePic());
        event.setCreatorEmail(currentUserProfile.getEmail());
        event.setCreatorHandle(currentUserProfile.getHandle());
        event.setTimestamp(Calendar.getInstance().getTimeInMillis());
        event.setCreatorPhoneNo(currentUserProfile.getPhoneNo());
        event.setUrl(link);
        event.setTime(String.valueOf(from));
        event.setTicketPrice(ticketPrice);
        event.setTicketType(i.getStringExtra("ticketType"));
        event.setPrivacy(i.getStringExtra("privacy"));
        event.setEventType(i.getStringExtra("eventType"));
        event.setSalesStartAt(i.getStringExtra("salesStartfrom"));
        event.setSalesStopAt(i.getStringExtra("salesStopAt"));
        event.setMaxNoOfTicket(i.getIntExtra("maxNoOfTickets",0));


     updateUI(event);

        db = FirebaseFirestore.getInstance();


     done.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
        progressDialog.show();
        progressDialog.setMessage("Publishing your event");

             DocumentReference ref = db.collection("events").document();
             String myId = ref.getId();
             event.setFirebaseId(myId);
             ref.set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()){

                         db.collection("profiles").
                                 document(currentUserProfile.getFirebase_id()).
                                 update("createdEvents",FieldValue.arrayUnion(myId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 progressDialog.dismiss();
                                 Intent i = new Intent(PreviewEventActivity.this,MainActivity.class);
                                 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                 startActivity(i);
                             }
                         });

                         //TODO:Where to go after successful event publishing
                     }
                 }
             });
         }
     });

     /*saveDraft.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             //TODO:  add event as a draft
             //TODO: It only adds the HashMap in case of array union objects
             db.collection("profiles").document(currentUserProfile.getFirebase_id())
                     .update("eventDraft",FieldValue.arrayUnion(event));
         }
     });*/






    }

    public void updateUI(Event event){
        title.setText(event.getTitle());
        venue.setText(event.getAddress());
        description.setText(event.getDescription());
        creatorName.setText(event.getCreatorName());
        going1.setText(event.getGoingCount() + " going");
        ticketPrice.setText("INR "+String.valueOf(event.getTicketPrice()));

        if (event.getUrl() != null) {
            link_relative_layout.setVisibility(View.VISIBLE);
            link1.setText(Html.fromHtml("<a href=\"" + event.getUrl() + "\">" + event.getUrl() + "</a>"));
            link1.setMovementMethod(LinkMovementMethod.getInstance());

        }


        if (event.getOrganisers() != null) {

            for (int i = 0; i < event.getOrganisers().size(); i++) {
                if (i == 0) {
                    org1.setVisibility(View.VISIBLE);
                    orgname1.setText(event.getOrganisers().get(0).getName());
                    orgEmail1.setText(event.getOrganisers().get(0).getEmail());
                    orgP1.setText(event.getOrganisers().get(0).getContactNumber());
                } else if (i == 1) {
                    View view = findViewById(R.id.view2);
                    view.setVisibility(View.VISIBLE);
                    org2.setVisibility(View.VISIBLE);
                    orgName2.setText(event.getOrganisers().get(1).getName());
                    orgEmail2.setText(event.getOrganisers().get(1).getEmail());
                    orgP2.setText(event.getOrganisers().get(1).getContactNumber());
                }
                if (i == 2) {
                    View view = findViewById(R.id.view3);
                    view.setVisibility(View.VISIBLE);
                    org3.setVisibility(View.VISIBLE);
                    orgName3.setText(event.getOrganisers().get(2).getName());
                    orgEmail3.setText(event.getOrganisers().get(2).getEmail());
                    orgP3.setText(event.getOrganisers().get(2).getContactNumber());
                }
            }

        }


        CharSequence f = DateUtils.getRelativeTimeSpanString(event.getFrom(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        from.setText(f);
        f = DateUtils.getRelativeTimeSpanString(event.getTo(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        to.setText(f);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(event.getTo());
        to.setText(simpleDateFormat.format(c.getTime())+" "+String.valueOf(c.
                get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(c.get(Calendar.MINUTE)));


        c.setTimeInMillis(event.getFrom());
        from.setText(simpleDateFormat.format(c.getTime())+" "+String.valueOf(c.
                get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(c.get(Calendar.MINUTE)));

    eventType.setText(event.getEventType()+" Event");


    if(event.getTicketType().equals("notRequired")){
       salesStopAt.setVisibility(View.GONE);
       salesStartFrom.setVisibility(View.GONE);
       maxNoOfTickets.setVisibility(View.GONE);
    }
    else {
        c.setTimeInMillis(Long.valueOf(event.getSalesStartAt()));
        salesStartFrom.setText(simpleDateFormat.format(c.getTime())+" "+String.valueOf(c.
                get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(c.get(Calendar.MINUTE)));

        c.setTimeInMillis(Long.valueOf(event.getSalesStopAt()));
        salesStopAt.setText(simpleDateFormat.format(c.getTime())+" "+String.valueOf(c.
                get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(c.get(Calendar.MINUTE)));

        maxNoOfTickets.setText(String.valueOf(event.getMaxNoOfTicket()));
    }





        creatorEmail.setText(event.getCreatorEmail());
        creatorPhone.setText(event.getCreatorPhoneNo());

        if (event.getOrganisers() != null) {
            Log.i("KaunHUMein", "org size is " + event.getOrganisers().size());
        } else {
            Log.i("KaunHUMein", "org size is null");
        }


        if (event.getOrganisers() != null) {

            for (int i = 0; i < event.getOrganisers().size(); i++) {
                if (i == 0) {
                    org1.setVisibility(View.VISIBLE);
                    orgname1.setText(event.getOrganisers().get(0).getName());
                    orgEmail1.setText(event.getOrganisers().get(0).getEmail());
                    orgP1.setText(event.getOrganisers().get(0).getContactNumber());
                } else if (i == 1) {
                    View view = findViewById(R.id.view2);
                    view.setVisibility(View.VISIBLE);
                    org2.setVisibility(View.VISIBLE);
                    orgName2.setText(event.getOrganisers().get(1).getName());
                    orgEmail2.setText(event.getOrganisers().get(1).getEmail());
                    orgP2.setText(event.getOrganisers().get(1).getContactNumber());
                }
                if (i == 2) {
                    View view = findViewById(R.id.view3);
                    view.setVisibility(View.VISIBLE);
                    org3.setVisibility(View.VISIBLE);
                    orgName3.setText(event.getOrganisers().get(2).getName());
                    orgEmail3.setText(event.getOrganisers().get(2).getEmail());
                    orgP3.setText(event.getOrganisers().get(2).getContactNumber());
                }
            }

        }


        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        Picasso.get().load(event.getCreatorProfilePic()).into(creatorProfilePic);

        if (event.getImageUrl() != null) {
            eventTimelineImageView.setImageUrl(event.getImageUrl(), imageLoader);
            eventTimelineImageView.setVisibility(View.VISIBLE);
            eventTimelineImageView.setResponseObserver(new EventTimelineImageView.ResponseObserver() {
                @Override
                public void onError() {

                }

                @Override
                public void onSuccess() {

                }
            });
        }
        else {
            eventTimelineImageView.setVisibility(View.VISIBLE);
            eventTimelineImageView.setImageResource(R.mipmap.event_placeholder);
        }


        flexboxLayout.setVisibility(View.VISIBLE);
        int  tagsCount =event.getTags().size() ;//... integer number of textviews
        TextView[] tages= new TextView[tagsCount];//create dynamic textviewsarray
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //loop to customize the text view and add it to flex box
        for (int i = 0; i < tagsCount; i++) {
            tages[i] = new TextView(PreviewEventActivity.this);
            GradientDrawable gD = new GradientDrawable();
            int strokeWidth = 5;
            int strokeColor = getResources().getColor(R.color.grey);

            gD.setStroke(strokeWidth, strokeColor);
            gD.setCornerRadius(15);
            gD.setShape(GradientDrawable.RECTANGLE);

            tages[i].setBackground(gD);
            tages[i].setText(event.getTags().get(i));
            layoutParams.setMargins(10, 5, 10, 5);
            tages[i].setPadding(17, 15, 17, 15);
            flexboxLayout.addView(tages[i], layoutParams);
        }





    }
}
