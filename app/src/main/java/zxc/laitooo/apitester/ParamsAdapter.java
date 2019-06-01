package zxc.laitooo.apitester;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Laitooo San on 29/05/2019.
 */

public class ParamsAdapter extends RecyclerView.Adapter<ParamsHolder> {

    ArrayList<Param> params;
    Context c;

    public ParamsAdapter(ArrayList<Param> params, Context c) {
        this.params = params;
        this.c = c;
    }

    @Override
    public ParamsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.param,parent,false);
        return new ParamsHolder(v);
    }

    @Override
    public void onBindViewHolder(final ParamsHolder holder, final int position) {

        final Param param = params.get(position);

        if (position == params.size() -1){
            holder.add.setImageResource(android.R.drawable.ic_input_add);
        }else {
            holder.add.setImageResource(android.R.drawable.ic_input_delete);
        }

        holder.key.setText(param.getKey());
        holder.value.setText(param.getValue());

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == params.size() -1) {
                    params.add(new Param("", ""));
                    param.setKey(holder.key.getText().toString());
                    param.setValue(holder.value.getText().toString());
                    notifyDataSetChanged();
                }else {
                    params.remove(position);
                    param.setKey(holder.key.getText().toString());
                    param.setValue(holder.value.getText().toString());
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return params.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
