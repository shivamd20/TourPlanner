package io.shivam.travelplanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.shivam.travelplanner.ItemFragment.OnListFragmentInteractionListener;
import io.shivam.travelplanner.dummy.DummyContent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Route} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    
    public static class Route extends Stack<String>{

        public int cost=0;

    }

    private  final List<Route> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<Route> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    public  void sort()
    {

        for(int position=0;position<mValues.size();position++) {
            int cost = 0;

            int a = Integer.parseInt(mValues.get(position).get(0));

            for (int i = 1; i < mValues.get(position).size(); i++) {
                try {

                    cost += MainActivity.costHash.get(a + ":" + mValues.get(position).get(i));
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                a++;

            }


            mValues.get(position).cost = cost;



        }

        Collections.sort(mValues, new Comparator<Route>() {
            @Override
            public int compare(Route strings, Route t1) {

                if (strings.cost > t1.cost) return +1;

                if (strings.cost < t1.cost) return -1;

                return 0;
            }
        });
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.mItem = mValues.get(position);


        int cost=Integer.parseInt(mValues.get(position).get(0));


        if(cost==0)
        {
            mValues.remove(position);

            return;
        }




        int a=Integer.parseInt(mValues.get(position).get(0));

        for(int i=1;i<mValues.get(position).size();i++)
        {
            try {

                cost+=MainActivity.costHash.get(a+":"+mValues.get(position).get(i));
            }
            catch (NullPointerException ne)
            {
             ne.printStackTrace();
            }

            a++;

        }

        if(mValues.get(position).cost==0)
            return;

        holder.mIdView.setText( mValues.get(position).cost+"K.M.");

        StringBuilder sb=new StringBuilder("start->");

        for (String str :
                mValues.get(position)) {

        int n=    Integer.parseInt(str);

           str= DummyContent.NODE_MAP.get(n);

            sb.append(str+" ->");

        }

        sb.append("end");

        holder.mContentView.setText(sb.toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Route mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
