package zxc.laitooo.apitester;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Laitooo San on 29/05/2019.
 */

public class ParamsHolder extends RecyclerView.ViewHolder {

    EditText key;
    EditText value;
    ImageButton add;

    public ParamsHolder(View itemView) {
        super(itemView);

        key = (EditText)itemView.findViewById(R.id.param_key);
        value = (EditText)itemView.findViewById(R.id.param_value);
        add = (ImageButton)itemView.findViewById(R.id.add);
    }
}
