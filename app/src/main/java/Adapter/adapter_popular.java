package Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billy.youtube_api.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MyVideo1;

/**
 * Created by Vu Khac Hoi on 9/17/2017.
 */

public class adapter_popular extends ArrayAdapter<MyVideo1> {
    Activity context;
    int resource;
    List<MyVideo1> objects;

    public adapter_popular(Activity context, int resource, List<MyVideo1> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);

        TextView textView=row.findViewById(R.id.txtTitle1);
        TextView textView1=row.findViewById(R.id.txtchannel);
        ImageView imageView=row.findViewById(R.id.imageView1);


        final MyVideo1 hoa = this.objects.get(position);
        textView.setText(hoa.getTitle().toString());
        textView1.setText(hoa.getChannelTitle().toString());
        Picasso.with(getContext()).load(hoa.getThumnail()).into(imageView);
        return row;
    }
}