package vivaladk.giacaphe;
import java.util.ArrayList;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class MainActivity extends Activity {

	public Spinner spinner;
	public ListView lv_giaca;
	public TextView submit;
	public ProgressDialog mProgressDialog;
	public String url = "http://giacaphe.com/gia-ca-phe-noi-dia/";
	public ArrayList<GiaCa> arr_giaca;
	public TextView tv_message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		spinner = (Spinner) findViewById(R.id.spinner);
		lv_giaca = (ListView) findViewById(R.id.lv_giaca);
		submit = (TextView) findViewById(R.id.btn_submit);
		tv_message = (TextView) findViewById(R.id.tv_message);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(R.color.actionbarcolor));
		setSpinnerView();
		viewGiaCaPhe();

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				arr_giaca = new ArrayList<GiaCa>();
				viewGiaCaPhe();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_info:
			showInfo();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showInfo() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.info_dialog, null);
		final EditText info_text = (EditText) layout.findViewById(R.id.info_text);
		info_text.setText(getResources().getString(R.string.infotext));
		info_text.setKeyListener(null);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Giới thiệu");
		alert.setView(layout);
		alert.setCancelable(true);
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

		});
		AlertDialog dialog = alert.create();
		dialog.show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public String getDateFormat(Calendar c, int type) {

		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);

		if (type == 1) {
			if (dayOfMonth < 10 && month < 10) {
				return "0" + dayOfMonth + "/0" + month + "/" + year;
			} else if (dayOfMonth < 10) {
				return "0" + dayOfMonth + "/" + month + "/" + year;
			} else if (month < 10) {
				return dayOfMonth + "/0" + month + "/" + year;
			} else {
				return dayOfMonth + "/" + month + "/" + year;

			}
		} else {
			if (dayOfMonth < 10 && month < 10) {
				return year + "-0" + month + "-0" + dayOfMonth;
			} else if (dayOfMonth < 10) {
				return year + "-" + month + "-0" + dayOfMonth;
			} else if (month < 10) {
				return year + "-0" + month + "-" + dayOfMonth;
			} else {
				return year + "-" + month + "-" + dayOfMonth;

			}

		}

	}

	public void setSpinnerView() {
		ArrayList<String> paths = new ArrayList<String>();

		for (int i = 0; i < 15; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, -i + 1);
			paths.add(getDateFormat(c, 1));

		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,
				paths);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

	}

	public String getSelectedDate(int type) {
		int position = spinner.getSelectedItemPosition();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -position + 1);
		if (type == 1) {
			return getDateFormat(c, 1);
		} else {
			return getDateFormat(c, 2);
		}
	}

	public void viewGiaCaPhe() {
		// ket noi toi gia ca phe
		String selectedDay = getSelectedDate(2);
		url = "http://giacaphe.com/gia-ca-phe-noi-dia-ngay-" + selectedDay + "/";
		// Log.e("DDDD", url);
		new ConnectTask().execute();
	}

	private class ConnectTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(MainActivity.this);
			mProgressDialog.setTitle("Lấy dữ liệu");
			mProgressDialog.setMessage("Đang tải...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				// Connect to the web site
				Document doc = Jsoup.connect(url).get();
				Element table = doc.getElementById("gia_trong_nuoc");
				Elements rows = table.getElementsByTag("tr");

				for (Element row : rows) {
					GiaCa item = new GiaCa();
					Elements cols = row.getElementsByTag("td");

					for (Element col : cols) {
						item.thongtin.add(col.text());
						// Log.e("item.thongtin", col.text());
					}
					arr_giaca.add(item);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mProgressDialog.dismiss();
			if (arr_giaca == null || arr_giaca.isEmpty()) {
				lv_giaca.setVisibility(View.GONE);
				tv_message.setVisibility(View.VISIBLE);
			} else {
				lv_giaca.setVisibility(View.VISIBLE);
				tv_message.setVisibility(View.GONE);
				MyArrayAdapter adapter = new MyArrayAdapter(MainActivity.this, R.layout.my_item_layout, arr_giaca);
				lv_giaca.setAdapter(adapter);
			}

		}
	}
}
