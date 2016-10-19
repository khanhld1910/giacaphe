package vivaladk.giacaphe;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class MyArrayAdapter extends ArrayAdapter<GiaCa> {

	Activity context = null;
	int resource;
	ArrayList<GiaCa> arr_giaca = null;

	public MyArrayAdapter(Activity context, int resource, ArrayList<GiaCa> arr_giaca) {
		super(context, resource, arr_giaca);
		this.context = context;
		this.resource = resource;
		this.arr_giaca = arr_giaca;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(resource, null);

		final TextView tv_ten, tv_gia, tv_thaydoi;

		tv_ten = (TextView) convertView.findViewById(R.id.tv_ten);
		tv_gia = (TextView) convertView.findViewById(R.id.tv_gia);
		tv_thaydoi = (TextView) convertView.findViewById(R.id.tv_thaydoi);

		final GiaCa giaSP = arr_giaca.get(position);

		if (giaSP.thongtin.size() == 3) {// chi lay dong co du lieu
			tv_ten.setText(giaSP.thongtin.get(0));
			tv_gia.setText(giaSP.thongtin.get(1));
			try {
				double thaydoi = Double.parseDouble(giaSP.thongtin.get(2));
				if (thaydoi > 0) {
					tv_thaydoi.setText("+" + thaydoi);
					tv_thaydoi.setTextColor(R.color.increase);
				} else {
					tv_thaydoi.setText(giaSP.thongtin.get(2));
				}
			} catch (Exception e) {
				tv_thaydoi.setText(giaSP.thongtin.get(2));
			}

		} else if (giaSP.thongtin.size() == 1) {
			tv_ten.setText(giaSP.thongtin.get(0));
		} else if (giaSP.thongtin.size() == 0) {

			tv_ten.setText("TT Nhân Xô");
			tv_gia.setText("Đơn giá");
			tv_thaydoi.setText("Thay đổi");
		}
		return convertView;
	}

}
