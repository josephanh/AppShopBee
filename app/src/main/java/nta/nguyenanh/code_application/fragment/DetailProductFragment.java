package nta.nguyenanh.code_application.fragment;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.DetailProductImageAdapter;
import nta.nguyenanh.code_application.dialog.DialogConfirm;
import nta.nguyenanh.code_application.interfaces.OnClickDiaLogConfirm;
import nta.nguyenanh.code_application.model.Product;

public class DetailProductFragment extends Fragment {

    Product product;
    ArrayList<String> listUrlImage = new ArrayList<>();
    DetailProductImageAdapter photoAdapter;

    TextView name_product, price_product, describe;

    public static final String NameFragment = DetailProductFragment.class.getName();

    DialogConfirm dialogConfirm;

    public DetailProductFragment() {
        // Required empty public constructor
    }

    public static DetailProductFragment newInstance(Product product) {
        DetailProductFragment fragment = new DetailProductFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable("product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView btn_back = view.findViewById(R.id.btn_back);
        ViewPager viewPager = view.findViewById(R.id.viewpageImage);
        CircleIndicator circleIndicator = view.findViewById(R.id.circleIndicatorImageDetail);
        ImageView show_sheet = view.findViewById(R.id.show_sheet);

        name_product = view.findViewById(R.id.name_product);
        price_product = view.findViewById(R.id.price_product);
        describe = view.findViewById(R.id.describe);

        name_product.setText(product.getNameproduct());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        price_product.setText(formatter.format(product.getPrice())+"đ");

        String[] words = product.getDescribe().split("\\. ");
        for (String w : words) {
           describe.setText(describe.getText().toString() + android.text.Html.fromHtml("<div>"+w+"</div><br>"));
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager() != null){
                    getFragmentManager().popBackStack();
                }
            }
        });
        // gắn hình cho viewpage
        listUrlImage = product.getImage();
        photoAdapter = new DetailProductImageAdapter(getContext(), listUrlImage);
        viewPager.setAdapter(photoAdapter);
        // Gắn circleIndicator
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        show_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel == null) {
                    dialogConfirm = new DialogConfirm(getContext());
                    dialogConfirm.showDialog(product);
                } else {
                    showSheet();
                }
            }
        });
    }

    private void showSheet() {
        View viewDiaLog = getLayoutInflater().inflate(R.layout.custom_layout_addcart, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.ThemeOverlay_App_BottomSheetDialog);
        bottomSheetDialog.setContentView(viewDiaLog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();

        RoundedImageView color_1, color_2, color_3, color_4;
        color_1 = viewDiaLog.findViewById(R.id.color_1);

        color_2 = viewDiaLog.findViewById(R.id.color_2);
        color_3 = viewDiaLog.findViewById(R.id.color_3);
        color_4 = viewDiaLog.findViewById(R.id.color_4);

        color_1.setBackgroundColor(Color.parseColor(product.getColor().get(0)));
        color_2.setBackgroundColor(Color.parseColor(product.getColor().get(1)));
        color_3.setBackgroundColor(Color.parseColor(product.getColor().get(2)));
        color_4.setBackgroundColor(Color.parseColor(product.getColor().get(3)));

    }

}