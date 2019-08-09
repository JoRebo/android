package facele.cl.mypymepos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import facele.cl.mypymepos.Activities.Emision;
import facele.cl.mypymepos.Activities.NavDrawer;
import facele.cl.mypymepos.Activities.Perfil;
import facele.cl.mypymepos.R;

public class FragmentInicio extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View myFragmentView;

    public FragmentInicio() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_inicio, container, false);

        ((NavDrawer) getActivity()).setActionBarTitle("INICIO");

        LinearLayout emision = myFragmentView.findViewById(R.id.layout_emision);
        emision.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), Emision.class);
            startActivity(intent);
        });

        LinearLayout perfil = myFragmentView.findViewById(R.id.layout_perfil);
        perfil.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), Perfil.class);
            startActivity(intent);
          });
          
        LinearLayout reporte = myFragmentView.findViewById(R.id.layout_reporte);
        reporte.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FragmentReporte())
                    .addToBackStack("REPORTE")
                    .commit();
        });

        return myFragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
