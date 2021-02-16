package com.motomistry.motomistry.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.motomistry.motomistry.Adapter.CardAdapter;
import com.motomistry.motomistry.R;

public class CardFragment extends Fragment {

    private CardView cardView;
    View view;
    int image;

    public CardFragment() {
    }

    @SuppressLint("ValidFragment")
    public CardFragment(int image) {
        this.image = image;

    }

    public static Fragment getInstance(int position) {
        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.viewpager_item, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);
        ImageView imageView = (ImageView) view.findViewById(R.id.pager_image);
        imageView.setImageResource(image);
        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}
