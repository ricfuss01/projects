package com.example.magicelementsfight.luchasCarpeta;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.magicelementsfight.R;

import java.util.List;

public class AdapterHistorial extends ArrayAdapter<ItemHistorial> {
        private Context context;
        private List<ItemHistorial> itemList;

        public AdapterHistorial(Context context, List<ItemHistorial> itemList) {
            super(context, R.layout.historial_card, itemList);
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.historial_card, parent, false);

            TextView textViewItem = view.findViewById(R.id.textcardhistorial);
            textViewItem.setText(itemList.get(position).getText());
            textViewItem.setTextColor(itemList.get(position).getColor());
            if(itemList.get(position).getText().split(":")[0].equals("Tu")){
                textViewItem.setGravity(Gravity.LEFT);
                textViewItem.setCompoundDrawablesWithIntrinsicBounds(null, null, itemList.get(position).getPhoto(), null);
            }
            else{
                textViewItem.setGravity(Gravity.RIGHT);
                textViewItem.setCompoundDrawablesWithIntrinsicBounds(itemList.get(position).getPhoto(), null, null, null);
            }
            return view;
        }

}
