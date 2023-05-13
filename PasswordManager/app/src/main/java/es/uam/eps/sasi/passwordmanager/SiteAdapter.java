package es.uam.eps.sasi.passwordmanager;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uam.eps.sasi.passwordmanager.databinding.ListItemSiteBinding;


public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.SiteHolder> {

    private List<Site> list;
    private String username;

    public SiteAdapter(List<Site> list, String username) {
        this.list = list;
        this.username = username;
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

            // Go to site
            binding.siteWidget.setOnClickListener(view -> {
                Navigation.findNavController(view)
                        .navigate(HomeFragmentDirections
                        .actionHomeFragmentToHomeSiteInfoFragment(username, site.getId()));
            });
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
