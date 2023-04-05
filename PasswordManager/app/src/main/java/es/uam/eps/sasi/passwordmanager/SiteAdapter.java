package es.uam.eps.sasi.passwordmanager;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uam.eps.sasi.passwordmanager.databinding.ListItemSiteBinding;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.SiteHolder> {

    private List<Site> list;

    public SiteAdapter(List<Site> list) {
        this.list = list;
    }

    public class SiteHolder extends RecyclerView.ViewHolder {
        private ListItemSiteBinding binding;

        public SiteHolder(ListItemSiteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Site site) {
            binding.setSite(site);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public SiteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemSiteBinding itemBinding = ListItemSiteBinding.inflate(
                inflater,
                parent,
                false);
        return new SiteHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
