package com.khazasid.android.doaqu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.regex.Pattern;

public class SearchFragment extends Fragment {

    private SearchsAdapter searchsAdapter;
    private TextView emptySearchText;
    private TextView searchResult;
    private DoaViewModel searchViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() != null){
            searchsAdapter = new SearchsAdapter(getActivity(), getActivity().findViewById(R.id.app_barr));
        }

        searchViewModel = new ViewModelProvider(this).get(DoaViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_list, container, false);

        emptySearchText = rootView
                .findViewById(R.id.empty_search_text);

        searchResult = rootView.findViewById(R.id.result_search_text);

        RecyclerView recyclerView = rootView.findViewById(R.id.search_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(searchsAdapter);

        return rootView;
    }

    void searchDoa(String query){

        emptySearchText.setText(getString(R.string.search_empty, query));

        query = query.replaceAll("\"", Pattern.quote("[5178]"));

        String[] stringsQuery = query.trim().toLowerCase().split(" ");

        SearchsAdapter.search = stringsQuery;

        StringBuilder finalQueryBuilder = new StringBuilder();
        for(int i=0; i <stringsQuery.length; i++){

            finalQueryBuilder.append(stringsQuery[i]);
            finalQueryBuilder.append("*");

            if(i != stringsQuery.length-1){
                finalQueryBuilder.append(" ");
            }
        }

        searchViewModel.getSearchDoas(finalQueryBuilder.toString()).observe(this,
                searchDoas -> searchsAdapter.setData(searchDoas, emptySearchText,
                        searchResult));
    }
}
