package com.tidevalet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tidevalet.helpers.Post;
import com.tidevalet.helpers.Properties;
import com.tidevalet.service.ulservice;
import com.tidevalet.thread.adapter;

import java.util.ArrayList;
import java.util.Calendar;

public class Preview extends Activity {
	NumberPicker numberPicker1, numberPicker2;
	String numberPicker1Value, numberPicker2Value;
	TextView submitReport, startNewReport;
	TextView dateTxt, timeTxt;
	EditText addExtraDetail;
	String latitude, longitude, address, fileUrl, date, time, addDetail;
	Uri fileAttachment;
	ImageView secondImagePreview;
	Bundle b;
    Context context = this;
	Uri attachFiles;
	ArrayList<String> uriList;
	ArrayList<Uri> arrayUri = new ArrayList<Uri>();
	StringBuilder splitAddress = new StringBuilder();
	String shortAddress;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);
        Bundle data = getIntent().getExtras();
        uriList = (ArrayList<String>) data.getStringArrayList("uri_list");
        latitude = data.getString("latitude");
        longitude = data.getString("longitude");
        address = data.getString("address");
        date = data.getString("date");
        time = data.getString("time");
        InitializeUI();
    }
    private void InitializeUI() {
		submitReport = (TextView) findViewById(R.id.submitReportTxt);
		startNewReport = (TextView) findViewById(R.id.startNewReportTxt);
		dateTxt = (TextView) findViewById(R.id.dateTxt);
		timeTxt = (TextView) findViewById(R.id.timeTxt);
		addExtraDetail = (EditText) findViewById(R.id.addDetailEdtTxt);
		secondImagePreview = (ImageView) findViewById(R.id.secondImagePreview);
		if (uriList.size() == 0) { }
		else{
			fileUrl = uriList.get(0).toString();
			fileAttachment = Uri.parse(fileUrl);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			final Bitmap bitmap = BitmapFactory.decodeFile(
					fileAttachment.getPath(), options);
			secondImagePreview.setImageBitmap(bitmap);
		}
		for (int i = 0; i < uriList.size(); i++) {
			arrayUri.add(Uri.parse(uriList.get(i).toString()));
		}
		dateTxt.setText(date);
		timeTxt.setText(time);
		submitReport.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addDetail = addExtraDetail.getText().toString();
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND_MULTIPLE);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{"jpittmanpc@gmail.com"});
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "New Properties Report:" + "\n" + numberPicker1Value);
				emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
                        arrayUri);
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "A new Violation1 report has been submitted"
                                + "\n"
                                + "Properties Type: "
                                + numberPicker1Value
                                + "\n"
                                + "Address of Properties: "
                                + numberPicker2Value
                                + "\n"
                                + "GPS Coordinates: "
                                + "\n"
                                + "("
                                + latitude
                                + ","
                                + longitude
                                + ")"
                                + "\n"
                                + "Address:"
                                + "\n"
                                + numberPicker2Value
                                + "\n"
                                + "\n"
                                + "The Properties image was captured at:"
                                + "\n"
                                + time
                                + " on "
                                + date
                                + "\n"
                                + "Additional comments about the Violation1:"
                                + "\n" + addDetail);
                Post post = new Post();
                post.setIsPosted(0);
                long pupilId = 41;
				post.setViolationType("Type " + numberPicker1Value + " address" + numberPicker2Value);
                post.setLocalImagePath(fileAttachment.getPath());
                post.setViolationId(pupilId);
                post.setTimestamp(Calendar.getInstance().toString());

				try {
                    startSubmit(Uri.parse(fileUrl).toString(), post, context);
					/*startActivity(Intent.createChooser(emailIntent,
							"Send email......"));
					finish();*/
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getApplicationContext(),
							"There is no email client installed",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		startNewReport.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Preview.this, App.class);
				startActivity(intent);
			}
		});

		numberPicker1 = (NumberPicker) findViewById(R.id.picker1);
		numberPicker2 = (NumberPicker) findViewById(R.id.picker2);
		numberPicker1.setWrapSelectorWheel(true);
		numberPicker2.setWrapSelectorWheel(true);


		if (address.length() > 23) {
			for (int i = 0; i < 19; i++) {
				splitAddress.append(address.charAt(i));
			}
			shortAddress = splitAddress.toString() + " ...";
		}
		else{

			shortAddress = address;
		}
		final String[] values1 = { "Not Tied", "Overflowing", "Stinks", "Ripped", "Not There", "Dynamic" };
		final String[] values2 = { shortAddress, "", "Other", "AptComplex2", "Complex3", "Dynamic List" };
		numberPicker1.setMinValue(0);
		numberPicker1.setMaxValue(values1.length - 1);
		numberPicker1.setDisplayedValues(values1);
		numberPicker1Value = values1[0].toString();

		numberPicker2.setMinValue(0);
		numberPicker2.setMaxValue(values2.length - 1);
		numberPicker2.setDisplayedValues(values2);
		numberPicker2Value = values2[0].toString();


		if (numberPicker2Value.isEmpty() || numberPicker2Value.length() == 0) {
			numberPicker2Value = "(null)";
		}else if(numberPicker2Value.equals("Other")) {
			numberPicker2Value = "Other";
		}else{
			numberPicker2Value = address;
		}


		numberPicker1.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
									  int newVal) {
				// TODO Auto-generated method stub
				numberPicker1Value = values1[newVal].toString();
			}
		});

		numberPicker2.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
									  int newVal) {
				// TODO Auto-generated method stub
				if(newVal == 0){
					numberPicker2Value = address;
				}
				if (numberPicker2Value.isEmpty() || numberPicker2Value.length() == 0) {
					numberPicker2Value = "(null)";
				}else{
					numberPicker2Value = values2[newVal].toString();
				}

			}
		});
	}
    private void startSubmit(String image, Post post, Context context) {
        Properties properties = new Properties();
        properties.setId(post.getId());
        properties.setName("Testing");
        adapter dbAdapter = new adapter(this);
        dbAdapter.open();
        post = dbAdapter.addPost(post);
        dbAdapter.close();
        Intent service = new Intent(this,  ulservice.class);
        service.putExtra("id", post.getId());
        startService(service);
        setResult(RESULT_OK);
        finish();
       // String result = WebUtils.uploadPostToWordpress(image, params, context);
    }

}
