package nta.nguyenanh.code_application.pagination;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private StaggeredGridLayoutManager manager;

    public PaginationScrollListener(StaggeredGridLayoutManager manager) {
        this.manager = manager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = manager.getChildCount();
        int totalItemCount  = manager.getItemCount();
        int[] firstVisibleItemCount = manager.findFirstVisibleItemPositions(new int[]{10});

        if(isLoading() || isLastPage()) {
            return;
        }
        if(firstVisibleItemCount.length >= 0 && (visibleItemCount +firstVisibleItemCount.length) >= totalItemCount){
            loadMoreItem();
        }
    }

    public abstract void loadMoreItem();
    public abstract boolean isLoading();
    public abstract boolean isLastPage();
}
