package com.khazasid.android.doaqu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DoaFragment extends Fragment {

    private DoasAdapter doasAdapter;
    private RecyclerView recyclerView;
    private TextView emptyDoa;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doasAdapter = new DoasAdapter(getActivity());

        DoaViewModel doaViewModel = new ViewModelProvider(this).get(DoaViewModel.class);
        doaViewModel.getAllDoas().observe(this,
                doas -> doasAdapter.setData(doas, recyclerView, emptyDoa));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.doas_list, container, false);

        recyclerView = rootView.findViewById(R.id.doas_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        emptyDoa = rootView
                .findViewById(R.id.empty_text);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        emptyDoa.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(
                getResources(), R.drawable.ic_doa_border_black, null), null, null);

        emptyDoa.setText(R.string.doa_empty);

        recyclerView.setAdapter(doasAdapter);

        return rootView;
        
    }
}
