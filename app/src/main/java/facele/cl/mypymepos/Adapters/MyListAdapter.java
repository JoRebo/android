package facele.cl.mypymepos.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import facele.cl.mypymepos.R;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] titulos;

    public MyListAdapter(Activity context, String[] titulos) {
        super(context, R.layout.row_reporte, titulos);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.titulos = titulos;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.row_reporte, null, true);

        TextView titulo = rowView.findViewById(R.id.reporte_title);
        titulo.setText(titulos[position]);

        Button btn = rowView.findViewById(R.id.reporte_button);
        btn.setOnClickListener(view1 -> {
            Toast.makeText(context, "Boton " + position, Toast.LENGTH_SHORT).show();
        });

        return rowView;
    }

    ;
}
