package thpplnetwork.android_medis_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import thpplnetwork.android_medis_app.R;
import thpplnetwork.android_medis_app.app.AppController;
import thpplnetwork.android_medis_app.model.Rekam;

public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Rekam> Rekamitem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Rekam> Rekamitem) {
        this.activity = activity;
        this.Rekamitem= Rekamitem;
    }

    @Override
    public int getCount() {
        return Rekamitem.size();
    }

    @Override
    public Object getItem(int location) {
        return Rekamitem.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        //NetworkImageView thumbNail = (NetworkImageView) convertView .findViewById(R.id.thumbnail);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView worth = (TextView) convertView.findViewById(R.id.worth);
        TextView source = (TextView) convertView.findViewById(R.id.source);
        TextView year = (TextView) convertView.findViewById(R.id.inYear);
        TextView source2 = (TextView) convertView.findViewById(R.id.source2);

        // getting billionaires data for the row
        Rekam m = Rekamitem.get(position);

        // thumbnail image
      //  thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // name
        name.setText(String.valueOf(m.getNik()));


        // Wealth Source
        source.setText("Anjuran = " + String.valueOf(m.getAnjuran()));
        source2.setText("Indikator = " + String.valueOf(m.getIndikator()));


        worth.setText(String.valueOf(m.getid_dokter()));

        // release year
        year.setText(String.valueOf(m.getTgl_periksa())+" hari lalu");

        return convertView;
    }

}
