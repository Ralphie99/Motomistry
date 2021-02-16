package com.motomistry.motomistry.New.Adapter;

import android.view.View;
import android.widget.ListAdapter;

import com.motomistry.motomistry.New.ListXpandable.AbstractSlideExpandableListAdapter;
import com.motomistry.motomistry.R;

public class VenderListAdapter extends AbstractSlideExpandableListAdapter {
    private int toggle_button_id;
    private int expandable_view_id;

    public VenderListAdapter(ListAdapter wrapped, int toggle_button_id, int expandable_view_id) {
        super(wrapped);
        this.toggle_button_id = toggle_button_id;
        this.expandable_view_id = expandable_view_id;
    }

  /*  public VenderListAdapter(ListAdapter wrapped) {
       // this(wrapped, R.id.expandable_toggle_button, R.id.expandable);
    }*/

    @Override
    public View getExpandToggleButton(View parent) {
        return parent.findViewById(toggle_button_id);
    }

    @Override
    public View getExpandableView(View parent) {
        return parent.findViewById(expandable_view_id);
    }
}
