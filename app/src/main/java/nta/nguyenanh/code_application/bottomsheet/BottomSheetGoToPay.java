package nta.nguyenanh.code_application.bottomsheet;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;

import nta.nguyenanh.code_application.DetailProductActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.Product;
import nta.nguyenanh.code_application.model.ProductCart;

public class BottomSheetGoToPay {
    Context context;
    Product product;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout group_1, group_2, group_3, group_4;
    String colorSelect = null;
    int totalSelect = 1;

    public BottomSheetGoToPay(Context context, Product product) {
        this.context = context;
        this.product = product;
    }

    private void setSheet() {
        View viewDiaLog = View.inflate(context, R.layout.custom_layout_addcart, null);
        bottomSheetDialog = new BottomSheetDialog(context, R.style.ThemeOverlay_App_BottomSheetDialog);
        bottomSheetDialog.setContentView(viewDiaLog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);


        RoundedImageView color_1, color_2, color_3, color_4;
        ImageView image_product_sheet, image_1, image_2, image_3,image_4;
        TextView price, total;


        color_1 = viewDiaLog.findViewById(R.id.color_1);
        color_2 = viewDiaLog.findViewById(R.id.color_2);
        color_3 = viewDiaLog.findViewById(R.id.color_3);
        color_4 = viewDiaLog.findViewById(R.id.color_4);

        image_product_sheet = viewDiaLog.findViewById(R.id.image_product_sheet);
        price = viewDiaLog.findViewById(R.id.price_product);
        total = viewDiaLog.findViewById(R.id.total_product);
        image_1 = viewDiaLog.findViewById(R.id.image_1);
        image_2 = viewDiaLog.findViewById(R.id.image_2);
        image_3 = viewDiaLog.findViewById(R.id.image_3);
        image_4 = viewDiaLog.findViewById(R.id.image_4);
        group_1 = viewDiaLog.findViewById(R.id.group_1);
        group_2 = viewDiaLog.findViewById(R.id.group_2);
        group_3 = viewDiaLog.findViewById(R.id.group_3);
        group_4 = viewDiaLog.findViewById(R.id.group_4);


        Glide.with(context).load(product.getImage().get(1)).into(image_product_sheet);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        price.setText(formatter.format(product.getPrice())+"đ");
        total.setText("Kho: " +product.getTotal());

        if(product.getColor().get(0).equals("#000000")) {
            group_1.setVisibility(View.GONE);
        }
        if(product.getColor().get(1).equals("#000000")) {
            group_2.setVisibility(View.GONE);
        }
        if(product.getColor().get(2).equals("#000000")) {
            group_3.setVisibility(View.GONE);
        }
        if(product.getColor().get(3).equals("#000000")) {
            group_4.setVisibility(View.GONE);
        }

        color_1.setBackgroundColor(Color.parseColor(product.getColor().get(0)));
        color_2.setBackgroundColor(Color.parseColor(product.getColor().get(1)));
        color_3.setBackgroundColor(Color.parseColor(product.getColor().get(2)));
        color_4.setBackgroundColor(Color.parseColor(product.getColor().get(3)));

        if(product.getImage().size() == 4) {
            Glide.with(context).load(product.getImage().get(0)).into(image_1);
            Glide.with(context).load(product.getImage().get(1)).into(image_2);
            Glide.with(context).load(product.getImage().get(2)).into(image_3);
            Glide.with(context).load(product.getImage().get(3)).into(image_4);
        } else {
            Glide.with(context).load(product.getImage().get(0)).into(image_1);
            Glide.with(context).load(product.getImage().get(1)).into(image_2);
            Glide.with(context).load(product.getImage().get(2)).into(image_3);
        }

        group_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_1.setBackgroundResource(R.drawable.btn_musam);
                group_2.setBackgroundResource(R.drawable.btn_borderbutton_login);
                group_3.setBackgroundResource(R.drawable.btn_borderbutton_login);
                group_4.setBackgroundResource(R.drawable.btn_borderbutton_login);
                colorSelect = product.getColor().get(0);
            }
        });
        group_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_2.setBackgroundResource(R.drawable.btn_musam);
                group_1.setBackgroundResource(R.drawable.btn_borderbutton_login);
                group_3.setBackgroundResource(R.drawable.btn_borderbutton_login);
                group_4.setBackgroundResource(R.drawable.btn_borderbutton_login);
                colorSelect = product.getColor().get(1);
            }
        });
        group_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_3.setBackgroundResource(R.drawable.btn_musam);
                group_2.setBackgroundResource(R.drawable.btn_borderbutton_login);
                group_1.setBackgroundResource(R.drawable.btn_borderbutton_login);
                group_4.setBackgroundResource(R.drawable.btn_borderbutton_login);
                colorSelect = product.getColor().get(2);
            }
        });
        group_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_4.setBackgroundResource(R.drawable.btn_musam);
                group_2.setBackgroundResource(R.drawable.btn_borderbutton_login);
                group_3.setBackgroundResource(R.drawable.btn_borderbutton_login);
                group_1.setBackgroundResource(R.drawable.btn_borderbutton_login);
                colorSelect = product.getColor().get(3);
            }
        });


        TextView total_;
        ImageView plus, minus;

        plus = viewDiaLog.findViewById(R.id.plus);
        minus = viewDiaLog.findViewById(R.id.minus);
        total_ = viewDiaLog.findViewById(R.id.total);
        total_.setText("1");

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(total_.getText().toString()) == 20) {
                    Toast.makeText(context, "Không được mua quá 20 sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    int total = Integer.parseInt(total_.getText().toString()) + 1;
                    total_.setText(total+"");
                    totalSelect = Integer.parseInt(total_.getText().toString());
                }


            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.parseInt(total_.getText().toString());
                if(total == 1) {
                    Toast.makeText(context, "Số lượng phải ít nhất là 1", Toast.LENGTH_SHORT).show();
                } else  {
                    total = Integer.parseInt(total_.getText().toString()) - 1;
                    total_.setText(total+"");
                    totalSelect = Integer.parseInt(total_.getText().toString());
                }
            }
        });


        Button addCart = viewDiaLog.findViewById(R.id.addCart);
        addCart.setText("Mua ngay");
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCart product1 = getDetailProductCart();
                if(product1 != null) {
                    ((DetailProductActivity)context).goToPay(product1);
                    bottomSheetDialog.hide();
                }
            }
        });


    }

    public void showSheet() {
        setSheet();
        bottomSheetDialog.show();
    }

    public ProductCart getDetailProductCart() {
        if(colorSelect == null) {
            Toast.makeText(context, "Bạn chưa chọn màu", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            ProductCart product1 = new ProductCart();
            product1.setId(product.getId());
            product1.setPrice(product.getPrice());
            product1.setNameproduct(product.getNameproduct());
            product1.setColor(colorSelect);
            product1.setTotal(totalSelect);
            product1.setImage(product.getImage().get(0));

            return product1;
        }
    }


}
