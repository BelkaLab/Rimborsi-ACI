package belka.us.acirefund.brand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import belka.us.acirefund.R;
import belka.us.acirefund.model.Brand;
import belka.us.acirefund.model.Refund;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fabriziorizzonelli on 27/09/2016.
 */

public class BrandAdapter extends ArrayAdapter<Brand> implements SpinnerAdapter {

    private Context mContext;
    private List<Brand> mBrandList;

    static class ViewHolder {
        @BindView(R.id.spinner_field_text_view)
        TextView field;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public BrandAdapter(Context context, List<Brand> brands) {
        super(context, R.layout.spinner_item, brands);
        this.mContext = context;
        this.mBrandList = brands;
    }

    @Override
    public int getCount() {
        return mBrandList.size();
    }

    @Override
    public Brand getItem(int position) {
        return mBrandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent, R.layout.spinner_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent, R.layout.spinner_drop_down_item);
    }

    private View initView(int position, View convertView, ViewGroup parent, int resourceId) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.field.setText(mBrandList.get(position).getName());

        return convertView;
    }
}
