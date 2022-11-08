package nta.nguyenanh.code_application.fragment;

import static nta.nguyenanh.code_application.MainActivity.listProduct;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.PhotoAdapter;
import nta.nguyenanh.code_application.adapter.ProductAdapter;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.model.Photo_banner;
import nta.nguyenanh.code_application.model.Product;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentSnapshot lastVisible;
    private boolean isScrolling;
    private boolean isLastItem;

    RecyclerView.OnScrollListener onScrollListener;
    boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    ViewPager viewPager;
    ViewPager viewPager_2;

    CircleIndicator circleIndicator, circleIndicator_2;

    PhotoAdapter photoAdapter, photoAdapter_2;

    List<Photo_banner> listPhoto, listRechargeCard;

    Timer timer, timer_2;

    RecyclerView recyclerView_flashsale;
    ProductAdapter adapter;
    private StaggeredGridLayoutManager manager;

    DiaLogProgess progess;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Ánh xạ
        viewPager = view.findViewById(R.id.viewpageHome);
        viewPager_2 = view.findViewById(R.id.viewpageHome_2);
        circleIndicator = view.findViewById(R.id.circleIndicator);
        circleIndicator_2 = view.findViewById(R.id.circleIndicator_2);
        recyclerView_flashsale = view.findViewById(R.id.recyclerView_flashsale);

        // Khởi tạo data cho banner
        listPhoto = listBanner();
        listRechargeCard = listRechargeCard();
        // set Adapter
        photoAdapter = new PhotoAdapter(getContext(), listPhoto);
        viewPager.setAdapter(photoAdapter);
        // Gắn circleIndicator
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideBanner();
        // Tương tự như trên cho banner nạp thẻ
        photoAdapter_2 = new PhotoAdapter(getContext(), listRechargeCard);
        viewPager_2.setAdapter(photoAdapter_2);
        circleIndicator_2.setViewPager(viewPager_2);
        photoAdapter_2.registerDataSetObserver(circleIndicator_2.getDataSetObserver());
        autoSlidereChargeCard();

        // Lấy thời gian hiện tại
        progess = new DiaLogProgess(getContext());
        progess.showDialog("Loading");
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        loadNextPage();

        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                    Toast.makeText(getContext(), "đúng", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = manager.getChildCount();
                totalItemCount = manager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = manager.findFirstVisibleItemPositions(firstVisibleItems);

                if (loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        Log.d("tag", "LOAD NEXT ITEM");
                    }
                }
            }

//        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                    isScrolling = true;
//                    Toast.makeText(getContext(), "đúng", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int[] firstVisibleItems = null;
//                firstVisibleItems  = manager.findFirstVisibleItemPositions(firstVisibleItems );
//                int visibleItemCount = manager.getChildCount();
//                int totalItem = manager.getItemCount();
//
//                if(isScrolling && (firstVisibleItems[0] + visibleItemCount) >= 0) {
//                    isScrolling = false;
//                    Query nextQuery = FirebaseFirestore.getInstance().collection("product")
//                            .startAfter(lastVisible)
//                            .limit(10);
//                    nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                ArrayList<String> color = (ArrayList<String>) document.getData().get("color");
//                                ArrayList<String> images = (ArrayList<String>) document.getData().get("image");
//                                Product product = new Product(document.getId(),
//                                        document.getData().get("nameproduct").toString(),
//                                        document.getData().get("describe").toString(),
//                                        Float.parseFloat(document.getData().get("price").toString()),
//                                        Integer.parseInt(document.getData().get("available").toString()),
//                                        color, images,
//                                        Integer.parseInt(document.getData().get("sale").toString()),
//                                        Integer.parseInt(document.getData().get("sold").toString()),
//                                        Integer.parseInt(document.getData().get("total").toString()),
//                                        document.getData().get("id_category").toString());
//                                listProduct.add(product);
//                            }
//                            Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
//                            adapter.notifyDataSetChanged();
//                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
//
//                            if(task.getResult().size() < 15 ){
//                                isLastItem = true;
//                            }
//                        }
//                    });
//
//                }
//            }
//        };
//        recyclerView_flashsale.addOnScrollListener(new PaginationScrollListener(manager) {
//            @Override
//            public void loadMoreItem() {
//                isLoading = true;
//                current += 1;
//                loadNextPage();
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//        });

        };
        recyclerView_flashsale.addOnScrollListener(onScrollListener);

    }
    // set data cho listbanner -- banner chính của home
    private List<Photo_banner> listBanner() {
        List<Photo_banner> list = new ArrayList<>();
        list.add(new Photo_banner(R.drawable.banner_1));
        list.add(new Photo_banner(R.drawable.banner_2));
        list.add(new Photo_banner(R.drawable.banner_3));
        list.add(new Photo_banner(R.drawable.banner_4));
        list.add(new Photo_banner(R.drawable.banner_5));
        return list;
    }
    // set data cho listRechargeCard -- banner nạp thẻ và dịch vụ
    private List<Photo_banner> listRechargeCard() {
        List<Photo_banner> list = new ArrayList<>();
        list.add(new Photo_banner(R.drawable.banner_napthe_1));
        list.add(new Photo_banner(R.drawable.banner_napthe_2));
        list.add(new Photo_banner(R.drawable.banner_napthe_3));
        list.add(new Photo_banner(R.drawable.banner_napthe_4));
        return list;
    }

    private void autoSlideBanner() {
        if(listPhoto == null || listPhoto.isEmpty() || viewPager == null) {
            return;
        }
        // Init timer
        if(timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = listPhoto.size() -1;

                        if(currentItem < totalItem) {
                            currentItem ++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }
    private void autoSlidereChargeCard() {
        if(listPhoto == null || listPhoto.isEmpty() || viewPager == null) {
            return;
        }
        // Init timer
        if(timer_2 == null) {
            timer_2 = new Timer();
        }

        timer_2.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager_2.getCurrentItem();
                        int totalItem = listRechargeCard.size() -1;

                        if(currentItem < totalItem) {
                            currentItem ++;
                            viewPager_2.setCurrentItem(currentItem);
                        } else {
                            viewPager_2.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 5000);
    }

    private void loadNextPage() {
        db.collection("product").limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> color = (ArrayList<String>) document.getData().get("color");
                                ArrayList<String> images = (ArrayList<String>) document.getData().get("image");
                                Product product = new Product(document.getId(),
                                        document.getData().get("nameproduct").toString(),
                                        document.getData().get("describe").toString(),
                                        Float.parseFloat(document.getData().get("price").toString()),
                                        Integer.parseInt(document.getData().get("available").toString()),
                                        color, images,
                                        Integer.parseInt(document.getData().get("sale").toString()),
                                        Integer.parseInt(document.getData().get("sold").toString()),
                                        Integer.parseInt(document.getData().get("total").toString()),
                                        document.getData().get("id_category").toString());
                                listProduct.add(product);
                                Log.d("CHECK", "onComplete: "+ listProduct.size());
                            }
                            progess.hideDialog();
                            // đổ lên list
                            recyclerView_flashsale.setLayoutManager(manager);
                            adapter = new ProductAdapter(listProduct, getContext());
                            recyclerView_flashsale.setAdapter(adapter);

                            // phân trang
                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                            Toast.makeText(getContext(), "Frist Page Product", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.w("readDataProduct", "Error getting documents.", task.getException());
                            progess.hideDialog();
                        }
                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // hủy timer khi fragment bị hủy
        if(timer != null) {
            timer.cancel();
            timer = null;
        }

        if(timer_2 != null) {
            timer_2.cancel();
            timer_2 = null;
        }
    }

}