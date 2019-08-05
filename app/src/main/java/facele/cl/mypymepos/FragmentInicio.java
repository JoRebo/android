package facele.cl.mypymepos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
