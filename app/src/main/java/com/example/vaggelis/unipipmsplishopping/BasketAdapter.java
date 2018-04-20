package com.example.vaggelis.unipipmsplishopping;

/**
 * Created by vaggelis on 24/02/17.
 */
//Addapter from the basker
import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class BasketAdapter extends ArrayAdapter<Product>{
    public BasketAdapter(Context context,ArrayList<Product>products)
    {
        super(context,0,products);
    }
    private String code;
    private String date;
    private String description;
    private String latitube;
    private String longitube;
    private String price;
    private String title;
    private String image;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Product product = getItem(position);

        if (convertView == null)
        {
            // connect addapetr with basket_item layout
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.basket_item,parent,false);

        }
        TextView title = (TextView) convertView.findViewById(R.id.basketitle);
        TextView description = (TextView) convertView.findViewById(R.id.basketdescription);
        final TextView price = (TextView) convertView.findViewById(R.id.basketprice);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.basketimageViewimage);
        final Button movebatton = (Button) convertView.findViewById(R.id.basketmovebutton);
// use picaso to load image
        Picasso.with(getContext())
                .load(product.getImage())
                .into(imageView);

        title.setText(product.getTitle());
        description.setText(product.getDescription());
        price.setText(product.getPrice());

        movebatton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addtobasket addtobasket = new Addtobasket();
                if (v == movebatton)
                {
                    addtobasket.move(product.getCode(),product.getTitle(),product.getPrice(),product.getLongitube(),product.getLatitube(),product.getDescription(),product.getDate(),product.getImage());
                    Toast.makeText(getContext(),"Prodact remove to basket",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}

