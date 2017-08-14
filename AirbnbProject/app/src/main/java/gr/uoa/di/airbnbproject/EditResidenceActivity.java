package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fromRESTful.Residences;
import fromRESTful.Users;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import util.RestAPI;
import util.RestClient;
import util.RetrofitCalls;
import util.Session;
import util.Utils;
public class EditResidenceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int RESULT_LOAD_IMAGE =1;
    public static final int GET_FROM_GALLERY = 3;

    String resType, token;
    Integer residenceId;
    Residences selectedResidence;

    Boolean user;

    ImageButton bcontinue, btnStartDate, btnEndDate;
    ImageView mImageView;
    EditText etUpload, etTitle, etAbout, etAddress, etCity, etCountry, etAmenities, etFloor, etRooms, etBaths, etView, etSpaceArea, etGuests, etMinPrice, etAdditionalCost, etCancellationPolicy, etRules;
    TextView tvStartDate, tvEndDate;
    Spinner etType;
    String photo;

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    CheckBox cbKitchen, cbLivingRoom;
    boolean bkitchen, blivingRoom;

    Context c;
    Users host;
    RetrofitCalls retrofitCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = Utils.getSessionData(EditResidenceActivity.this);
        token = sessionData.getToken();
        if (!sessionData.getUserLoggedInState()) {
            Intent intent = new Intent(this, GreetingActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.layout_residence_editfields);
        c = this;

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        user = false;
        residenceId = buser.getInt("residenceId");

        retrofitCalls = new RetrofitCalls();
        if(Utils.isTokenExpired(token))
        {
            Utils.logout(this);
            finish();
        }
        selectedResidence = retrofitCalls.getResidenceById(token, Integer.toString(residenceId));
        userInputLayout();
        ArrayList<Users> getUsersByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        host = getUsersByUsername.get(0);

        TextView residencetxt = (TextView) findViewById(R.id.residencetxt);
        residencetxt.setText("Edit Residence#" + residenceId);

        Button uploadImage = (Button) findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        saveResidence();

        /** BACK BUTTON **/
        Utils.manageBackButton(this, HostActivity.class, user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            mImageView = (ImageView) findViewById(R.id.photo);
            photo = selectedImage.toString();
            mImageView.setImageBitmap(BitmapFactory.decodeFile(selectedImage.toString()));

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                mImageView.setImageBitmap(bitmap);
                new EditResidenceActivity.SendImageTask().execute(Utils.getRealPathFromURI(EditResidenceActivity.this, selectedImage));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            File file = new File(params[0]);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            /** MultipartBody.Part is used to send also the actual file name **/
            MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
            /** add another part within the multipart request **/
            RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, "User Image");

            RestAPI restAPI = RestClient.getClient(token).create(RestAPI.class);
            Call<String> call = restAPI.uploadResidenceImg(residenceId, description, body);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            } catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String nothing) {}
    }

    public void userInputLayout () {
        //etUpload             = (EditText)findViewById(R.id.etUpload);
        etTitle              = (EditText)findViewById(R.id.etTitle);
        etAbout              = (EditText)findViewById(R.id.etAbout);
        etAddress            = (EditText)findViewById(R.id.etAddress);
        etCity               = (EditText)findViewById(R.id.etCity);
        etCountry            = (EditText)findViewById(R.id.etCountry);
        etAmenities          = (EditText)findViewById(R.id.etAmenities);
        etFloor              = (EditText)findViewById(R.id.etFloor);
        etRooms              = (EditText)findViewById(R.id.etRooms);
        etBaths              = (EditText)findViewById(R.id.etBaths);
        etView               = (EditText)findViewById(R.id.etView);
        etSpaceArea          = (EditText)findViewById(R.id.etSpaceArea);
        etGuests             = (EditText)findViewById(R.id.etGuests);
        etMinPrice           = (EditText)findViewById(R.id.etMinPrice);
        etAdditionalCost     = (EditText)findViewById(R.id.etAdditionalCost);
        tvStartDate          = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate            = (TextView)findViewById(R.id.tvEndDate);
        etCancellationPolicy = (EditText)findViewById(R.id.etCancellationPolicy);
        etRules              = (EditText)findViewById(R.id.etRules);

        btnStartDate        = (ImageButton)findViewById(R.id.btnStartDate);
        btnEndDate          = (ImageButton)findViewById(R.id.btnEndDate);

        cbKitchen           = (CheckBox)findViewById(R.id.cbKitchen);
        cbLivingRoom        = (CheckBox)findViewById(R.id.cbLivingRoom);
        cbKitchen.setChecked(selectedResidence.getKitchen());
        cbLivingRoom.setChecked(selectedResidence.getLivingRoom());

        bcontinue = (ImageButton)findViewById(R.id.ibContinue);

        etType = (Spinner) findViewById(R.id.etType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this, R.array.residence_types_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        etType.setAdapter(spinneradapter);
        etType.setOnItemSelectedListener(this);
        etType.setSelection(spinneradapter.getPosition(selectedResidence.getType()));

        etTitle.setText(selectedResidence.getTitle());
        etAbout.setText(selectedResidence.getAbout());
        etAddress.setText(selectedResidence.getAddress());
        etCity.setText(selectedResidence.getCity());
        etCountry.setText(selectedResidence.getCountry());
        etAmenities.setText(selectedResidence.getAmenities());
        etFloor.setText(Integer.toString(selectedResidence.getFloor()));
        etRooms.setText(Integer.toString(selectedResidence.getRooms()));
        etBaths.setText(Integer.toString(selectedResidence.getBaths()));
        etView.setText(selectedResidence.getView());
        etSpaceArea.setText(Double.toString(selectedResidence.getSpaceArea()));
        etGuests.setText(Integer.toString(selectedResidence.getGuests()));
        etMinPrice.setText(Double.toString(selectedResidence.getMinPrice()));
        etAdditionalCost.setText(Double.toString(selectedResidence.getAdditionalCostPerPerson()));
        etCancellationPolicy.setText(selectedResidence.getCancellationPolicy());
        etRules.setText(selectedResidence.getRules());
        String startdate ="";
        Date startDate = selectedResidence.getAvailableDateStart();

        if(startDate !=null){
            SimpleDateFormat newDateFormat = new SimpleDateFormat(Utils.APP_DATE_FORMAT);
            startdate = newDateFormat.format(startDate);
        }
        tvStartDate.setText(startdate);

        String enddate ="";
        Date endDate = selectedResidence.getAvailableDateEnd();

        if(endDate !=null){
            SimpleDateFormat newDateFormat = new SimpleDateFormat(Utils.APP_DATE_FORMAT);
            enddate = newDateFormat.format(endDate);
        }
        tvEndDate.setText(enddate);

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnStartDate) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mStartYear = c.get(Calendar.YEAR);
                    mStartMonth = c.get(Calendar.MONTH);
                    mStartDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditResidenceActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tvStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mStartYear, mStartMonth, mStartDay);
                    datePickerDialog.show();
                }
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnEndDate) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mEndYear = c.get(Calendar.YEAR);
                    mEndMonth = c.get(Calendar.MONTH);
                    mEndDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditResidenceActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tvEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mEndYear, mEndMonth, mEndDay);
                    datePickerDialog.show();
                }
            }
        });
    }

    /**  An item was selected. You can retrieve the selected item using parent.getItemAtPosition(pos) **/
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { resType = parent.getItemAtPosition(pos).toString(); }
    /** Another interface callback **/
    public void onNothingSelected(AdapterView<?> parent) {}

    public void saveResidence () {
        bcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title                      = etTitle.getText().toString();
                final String type                       = resType;
                final String about                      = etAbout.getText().toString();
                final String address                    = etAddress.getText().toString();
                final String city                       = etCity.getText().toString();
                final String country                    = etCountry.getText().toString();
                final String amenities                  = etAmenities.getText().toString();
                final String floor                      = etFloor.getText().toString();
                final String rooms                      = etRooms.getText().toString();
                final String baths                      = etBaths.getText().toString();
                final String view                       = etView.getText().toString();
                final String spaceArea                  = etSpaceArea.getText().toString();
                final String guests                     = etGuests.getText().toString();
                final String minPrice                   = etMinPrice.getText().toString();
                final String additionalCostPerPerson    = etAdditionalCost.getText().toString();
                final String availableStartDate         = tvStartDate.getText().toString();
                final String availableEndDate           = tvEndDate.getText().toString();
                final String cancellationPolicy         = etCancellationPolicy.getText().toString();
                final String rules                      = etRules.getText().toString();

                bkitchen = (cbKitchen).isChecked();
                blivingRoom = (cbLivingRoom).isChecked();

                final String kitchen = Boolean.toString(bkitchen);
                final String livingRoom = Boolean.toString(blivingRoom);

                Date startDate = Utils.ConvertStringToDate(availableStartDate, Utils.DATABASE_DATE_FORMAT);
                String convertedStartDate = Utils.ConvertDateToString(startDate, Utils.DATABASE_DATE_FORMAT);

                Date endDate = Utils.ConvertStringToDate(availableEndDate, Utils.DATABASE_DATE_FORMAT);
                String convertedEndDate = Utils.ConvertDateToString(endDate, Utils.DATABASE_DATE_FORMAT);

                if (type.length() == 0 || title.length() == 0 || about.length() == 0 || address.length() == 0 || city.length() == 0 || country.length() == 0 || amenities.length() == 0 || floor.length() == 0
                        || rooms.length() == 0 || baths.length() == 0 || view.length() == 0 || spaceArea.length() == 0 || guests.length() == 0 || minPrice.length() == 0
                        || additionalCostPerPerson.length() == 0 || cancellationPolicy.length() == 0 || rules.length() == 0 || kitchen.length() == 0 || livingRoom.length() == 0
                        || convertedStartDate.length() == 0 || convertedEndDate.length() == 0 || photo.length() == 0) {
                    Toast.makeText(c, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    token = PutResult(host, title, type, about, address, city, country, amenities, Integer.parseInt(floor), Integer.parseInt(rooms),
                            Integer.parseInt(baths), view, Double.parseDouble(spaceArea), Integer.parseInt(guests), Double.parseDouble(minPrice),
                            Double.parseDouble(additionalCostPerPerson), cancellationPolicy, rules, Boolean.parseBoolean(kitchen),
                            Boolean.parseBoolean(livingRoom), startDate, endDate, photo);

                    if (!token.isEmpty() && token!=null && token!="not") {
                        Intent hostIntent = new Intent(EditResidenceActivity.this, HostActivity.class);
                        Bundle bhost = new Bundle();
                        user=false;
                        bhost.putBoolean("type", user);
                        hostIntent.putExtras(bhost);
                        try {
                            startActivity(hostIntent);
                            finish();
                        } catch (Exception e) {
                            Log.e("",e.getMessage());
                        }
                    } else {
                        Toast.makeText(c, "Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                        Utils.logout(EditResidenceActivity.this);
                        finish();
                    }
                }
            }
        });

    }
    public String PutResult(Users host, String title, String type, String about, String address, String city, String country, String amenities, int floor, int rooms,
                            int baths, String view, double spaceArea, int guests, double minPrice, double additionalCostPerPerson, String cancellationPolicy,
                            String rules, boolean kitchen, boolean livingRoom, Date startDate, Date endDate, String photo)
    {
        Residences ResidenceParameters = new Residences(host, title, type, about, address, city, country, amenities, floor, rooms, baths, view, spaceArea, guests, minPrice,
                additionalCostPerPerson, cancellationPolicy, rules, kitchen, livingRoom, startDate, endDate, photo);

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        token = retrofitCalls.editResidence(token, residenceId, ResidenceParameters);
        return token;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == RESULT_LOAD_IMAGE && requestCode == RESULT_OK && data != null) {
//            Uri selectedImage = data.getData();
//            imageToUpload.setImageURI(selectedImage);
//        }
//    }
}