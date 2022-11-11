package nta.nguyenanh.code_application.fragment;

import static nta.nguyenanh.code_application.MainActivity.listProduct;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import nta.nguyenanh.code_application.adapter.BannerAdapter;
import nta.nguyenanh.code_application.adapter.ProductAdapter;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.model.Product;
import nta.nguyenanh.code_application.pagination.EndlessRecyclerViewScrollListener;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentSnapshot lastVisible;
    private boolean isScrolling;
    private boolean isLastItem;

    ViewPager viewPager;
    ViewPager viewPager_2;

    CircleIndicator circleIndicator, circleIndicator_2;

    BannerAdapter photoAdapter, photoAdapter_2;

    List<String> listPhoto, listRechargeCard;

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
        NestedScrollView nestedScrollView = view.findViewById(R.id.nestedscrollviewHome);
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                WindowManager windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                if(scrollY < recyclerView_flashsale.getHeight()) {
                    recyclerView_flashsale.setNestedScrollingEnabled(false);
                } else {
                    recyclerView_flashsale.setNestedScrollingEnabled(true);
                }
            }
        });


        // Khởi tạo data cho banner
        listPhoto = listBanner();
        listRechargeCard = listRechargeCard();
        // set Adapter
        photoAdapter = new BannerAdapter(getContext(), listPhoto);
        viewPager.setAdapter(photoAdapter);
        // Gắn circleIndicator
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideBanner();
        // Tương tự như trên cho banner nạp thẻ
        photoAdapter_2 = new BannerAdapter(getContext(), listRechargeCard);
        viewPager_2.setAdapter(photoAdapter_2);
        circleIndicator_2.setViewPager(viewPager_2);
        photoAdapter_2.registerDataSetObserver(circleIndicator_2.getDataSetObserver());
        autoSlidereChargeCard();


        progess = new DiaLogProgess(getContext());
        progess.showDialog("Loading");
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        loadNextPage();

        EndlessRecyclerViewScrollListener scrollListener;
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

//                loadNextDataFromApi(page);
                Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
            }
        };
//        recyclerView_flashsale.addOnScrollListener(scrollListener);
//        recyclerView_flashsale.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
////                Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
//                Log.d("SCROLL", "onScrollStateChanged: "+recyclerView_flashsale.getHeight()+"W: "+recyclerView_flashsale.getWidth());
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
//                Log.d("SCROLL", "onScrolled: ");
//            }
//        });
    }

    private void loadNextDataFromApi(int page) {
        Query nextQuery = FirebaseFirestore.getInstance().collection("product")
                .startAfter(lastVisible)
                .limit(5);
        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                }
                Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
//                lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 2);

//                if (task.getResult().size() < 30) {
//                    isLastItem = true;
//                }
            }
        });
    }


    // set data cho listbanner -- banner chính của home
    private List<String> listBanner() {
        List<String> list = new ArrayList<>();
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_1.jpg?alt=media&token=9a4debc3-8232-411c-9385-06574d4e70b1");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_2.jpg?alt=media&token=f3d1ccf0-87a8-4fca-9c05-a5a616649be4");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_3.jpg?alt=media&token=b4de77ab-c432-477f-800a-0f9085ed5737");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_4.jpg?alt=media&token=75bf780e-9c85-4423-9a3c-6dd1ac8a7b2d");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_5.jpg?alt=media&token=5b86d2af-1f5c-4f14-9bf0-e471440779f0");
        return list;
    }

    // set data cho listRechargeCard -- banner nạp thẻ và dịch vụ
    private List<String> listRechargeCard() {
        List<String> list = new ArrayList<>();
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_napthe_1.jpg?alt=media&token=166b3a56-67c1-45ec-afd1-84d1f225bde0");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_napthe_2.jpg?alt=media&token=c9a84c2f-d351-4909-a069-6d21216083fe");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_napthe_3.jpg?alt=media&token=ae269698-7b7d-4747-94cc-21bad4b0fb51");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_napthe_4.jpg?alt=media&token=b62d3dfd-721d-4d84-9d16-053bc83d1ed7");
        return list;
    }

    private void autoSlideBanner() {
        if (listPhoto == null || listPhoto.isEmpty() || viewPager == null) {
            return;
        }
        // Init timer
        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = listPhoto.size() - 1;

                        if (currentItem < totalItem) {
                            currentItem++;
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
        if (listPhoto == null || listPhoto.isEmpty() || viewPager == null) {
            return;
        }
        // Init timer
        if (timer_2 == null) {
            timer_2 = new Timer();
        }

        timer_2.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager_2.getCurrentItem();
                        int totalItem = listRechargeCard.size() - 1;

                        if (currentItem < totalItem) {
                            currentItem++;
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

                            }
                            progess.hideDialog();
                            Log.d("CHECK_SIZE", "onComplete 1: " + listProduct.size());
                            // đổ lên list
                            recyclerView_flashsale.setLayoutManager(manager);
                            adapter = new ProductAdapter(listProduct, getContext());
                            recyclerView_flashsale.setAdapter(adapter);


                            // phân trang
                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                            Toast.makeText(getContext(), "Frist Page Product", Toast.LENGTH_SHORT).show();
                            Log.d("DATA_FIRST", "onComplete: "+lastVisible);
                            RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                                        isScrolling = true;
                                    }
                                }

                                @Override
                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    int[] firstVisibleItems = null;
                                    firstVisibleItems  = manager.findFirstVisibleItemPositions(firstVisibleItems );
                                    int visibleItemCount = manager.getChildCount();
                                    int totalItem = manager.getItemCount();

                                    if(isScrolling && (firstVisibleItems[0] + visibleItemCount) == totalItem && !isLastItem) {
                                        isScrolling = false;
                                        Query nextQuery = FirebaseFirestore.getInstance().collection("product")
                                                .startAfter(lastVisible)
                                                .limit(10);
                                        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                                }
                                                Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                                                Log.e("CHECK_SIZE", "onComplete: "+listProduct.size() );
                                                adapter.notifyDataSetChanged();
                                                lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                                                if(task.getResult().size() < 10 ){
                                                    isLastItem = true;
                                                    return;
                                                }
                                            }
                                        });

                                    }
                                }
                            };
                            recyclerView_flashsale.addOnScrollListener(onScrollListener);

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
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timer_2 != null) {
            timer_2.cancel();
            timer_2 = null;
        }
    }

}