package com.khazasid.android.doaqu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookmarkFragment extends Fragment {

    private RecyclerView recyclerView;
    private DoasAdapter bookmarksAdapter;
    private TextView emptyBookmark;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookmarksAdapter = new DoasAdapter(getActivity());

        DoaViewModel doaViewModel = new ViewModelProvider(this).get(DoaViewModel.class);
        doaViewModel.getAllBookmarkDoas().observe(this,
                doas -> bookmarksAdapter.setData(doas, recyclerView, emptyBookmark));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.doas_list, container, false);

        recyclerView = rootView.findViewById(R.id.doas_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        emptyBookmark = rootView
                .findViewById(R.id.empty_text);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        emptyBookmark.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(
                getResources(), R.drawable.ic_bookmark_border_black, null), null, null);

        emptyBookmark.setText(R.string.bookmark_empty);

        recyclerView.setAdapter(bookmarksAdapter);

        return rootView;
    }

}
