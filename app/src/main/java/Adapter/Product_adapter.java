package Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crowdfire.cfalertdialog.CFAlertDialog;
//import com.crowdfire.cfalertdialog.CFAlertDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Model.Category_model;
import Model.Decription_model;
import Model.Product_model;
import cifato.foody.MainActivity;
import cifato.foody.R;
import util.DatabaseHandler;

import static android.icu.lang.UProperty.INT_START;


public class Product_adapter extends RecyclerView.Adapter<Product_adapter.MyViewHolder>
        implements Filterable {
    private CFAlertDialog alertDialog;
//    private ColorSelectionView colorSelectionView;
    private List<Product_model> modelList;
    private List<Product_model> mFilteredList;
    private List<Decription_model> decription_models;
    private Context context;
    private DatabaseHandler dbcart;
    private String in_Stock;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price, tv_total,  tv_add, mrpPrice;
        public ImageView iv_logo, iv_plus, iv_minus, iv_remove;
        public CardView pCardView;
        public EditText tv_contetiy;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            tv_contetiy = (EditText) view.findViewById(R.id.tv_subcat_contetiy);
            tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
            iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);
            mrpPrice = (TextView) view.findViewById(R.id.mrpPrice);
            pCardView = view.findViewById(R.id.card_view);

            iv_remove.setVisibility(View.GONE);
            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_logo.setOnClickListener(this);

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();

            if (id == R.id.iv_subcat_plus) {

                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));

            } else if (id == R.id.iv_subcat_minus) {

                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }

            } else if (id == R.id.tv_subcat_add) {

                HashMap<String, String> map = new HashMap<>();
//                HashMap<String, String> maps =new HashMap<>();
                if (dbcart.getCartCount() == 0)
                {

                    Toast.makeText(context, "They are equal", Toast.LENGTH_SHORT).show();
                    map.put("product_id", modelList.get(position).getProduct_id());
                    map.put("category_id", modelList.get(position).getCategory_id());
                    map.put("product_image", modelList.get(position).getProduct_image());
                    map.put("increament", modelList.get(position).getIncreament());
                    map.put("product_name", modelList.get(position).getProduct_name());
                    map.put("price", modelList.get(position).getPrice());
                    map.put("stock", modelList.get(position).getIn_stock());
                    map.put("title", modelList.get(position).getTitle());
                    map.put("unit", modelList.get(position).getUnit());
                    map.put("Mrp", modelList.get(position).getMrp());
                    map.put("unit_value", modelList.get(position).getUnit_value());

                    if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                        if (dbcart.isInCart(map.get("product_id"))) {
                            dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                        } else {
                            dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                        }
                    } else {
                        dbcart.removeItemFromCart(map.get("product_id"));
//                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                        tv_add.setText("Add To Card");
                    }

                    Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                    Double price = Double.parseDouble(map.get("price"));

                    tv_total.setText("" + price * items);
//                mrpPrice.setText(map.get("Mrp"));
                    ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

                }
                for(int i=0;i<dbcart.getCartCount(); i++) {
                    if(dbcart.getCartAll().get(i).get("category_id").equals(modelList.get(position).getCategory_id()))
                    {
                        Toast.makeText(context, "They are equal", Toast.LENGTH_SHORT).show();
                        map.put("product_id", modelList.get(position).getProduct_id());
                        map.put("category_id", modelList.get(position).getCategory_id());
                        map.put("product_image", modelList.get(position).getProduct_image());
                        map.put("increament", modelList.get(position).getIncreament());
                        map.put("product_name", modelList.get(position).getProduct_name());
                        map.put("price", modelList.get(position).getPrice());
                        map.put("stock", modelList.get(position).getIn_stock());
                        map.put("title", modelList.get(position).getTitle());
                        map.put("unit", modelList.get(position).getUnit());
                        map.put("Mrp", modelList.get(position).getMrp());
                        map.put("unit_value", modelList.get(position).getUnit_value());

                        if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                            if (dbcart.isInCart(map.get("product_id"))) {
                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                            } else {
                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                            }
                        } else {
                            dbcart.removeItemFromCart(map.get("product_id"));
//                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                            tv_add.setText("Add To Card");
                        }

                        Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                        Double price = Double.parseDouble(map.get("price"));

                        tv_total.setText("" + price * items);
//                mrpPrice.setText(map.get("Mrp"));
                        ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
                    }else {
                        ShowDialogForClearCart(position,map,tv_contetiy,tv_total,tv_add);
                        i=dbcart.getCartCount();
                        Toast.makeText(context, "They are not equal", Toast.LENGTH_SHORT).show();
                    }
                }


            } else if (id == R.id.iv_subcat_img) {
                showImage(modelList.get(position).getProduct_image());
            } /*else if (id == R.id.card_view) {
                showProductDetail(modelList.get(position).getProduct_image(),
                        modelList.get(position).getTitle(),
                        modelList.get(position).getProduct_description(),
                        modelList.get(position).getProduct_name(),
                        position, tv_contetiy.getText().toString());
            }*/

        }
    }

    private void ShowDialogForClearCart(final int position, final HashMap<String, String> map, final EditText tv_contetiy, final TextView tv_total, final TextView tv_add) {
        // Create Alert using Builder
//        String sourceString = "<strong>" + modelList.get(position).getTitle() + "</strong> " ;
//        colorSelectionView = new ColorSelectionView(this);
//        colorSelectionView.setSelectedColor(DEFAULT_BACKGROUND_COLOR);
        alertDialog = new CFAlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("We Are unable to add items in cart")
                .setMessage("Sorry,but you already have some items from others so we are unable to add from "+modelList.get(position).getTitle())
                .addButton("Clear Card and Add",-1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    //Adding to cart now
                        dbcart.clearCart();
                        if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                            Toast.makeText(context, "They are equal", Toast.LENGTH_SHORT).show();
                            map.put("product_id", modelList.get(position).getProduct_id());
                            map.put("category_id", modelList.get(position).getCategory_id());
                            map.put("product_image", modelList.get(position).getProduct_image());
                            map.put("increament", modelList.get(position).getIncreament());
                            map.put("product_name", modelList.get(position).getProduct_name());
                            map.put("price", modelList.get(position).getPrice());
                            map.put("stock", modelList.get(position).getIn_stock());
                            map.put("title", modelList.get(position).getTitle());
                            map.put("unit", modelList.get(position).getUnit());
                            map.put("Mrp", modelList.get(position).getMrp());
                            map.put("unit_value", modelList.get(position).getUnit_value());
                            if (dbcart.isInCart(map.get("product_id"))) {
                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                            } else {
                                dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                            }
                        } else {
                            dbcart.removeItemFromCart(map.get("product_id"));
//                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                            tv_add.setText("Add To Card");
                        }

                        Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                        Double price = Double.parseDouble(map.get("price"));

                        tv_total.setText("" + price * items);
//                mrpPrice.setText(map.get("Mrp"));
                        ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
                        // dismiss the dialog
                        alertDialog.dismiss();
                    }
                }).addButton("Cancel", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .onDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        // Update the color preview
//                        setSelectedBackgroundColor(colorSelectionView.selectedColor);
                    }
                })
                .create();

        alertDialog.show();
    }

    public Product_adapter(Context context, List<Product_model> modelList, List<Decription_model> decription_models) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
        this.decription_models = decription_models;
        this.context = context;
        dbcart = new DatabaseHandler(context);
    }

    @Override
    public Product_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product_rv, parent, false);

        context = parent.getContext();

        return new Product_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Product_adapter.MyViewHolder holder, int position) {
        Product_model mList = modelList.get(position);
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .centerCrop()
                .placeholder(R.drawable.cifatotrain)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_logo);
        in_Stock = mList.getIn_stock();
        holder.tv_title.setText(mList.getProduct_name());
        holder.mrpPrice.setText(mList.getMrp());
        holder.tv_price.setText(context.getResources().getString(R.string.tv_pro_price) + mList.getUnit_value() + " " +
                mList.getUnit() + " " + context.getResources().getString(R.string.currency) + " " + mList.getPrice());

        if (dbcart.isInCart(mList.getProduct_id())) {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            holder.tv_contetiy.setText(dbcart.getCartItemQty(mList.getProduct_id()));
        } else {
//            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
            if (!in_Stock.equals("0")) {
                holder.tv_add.setText("Add To Card");
            } else {
                holder.tv_add.setText("Out Of Stock");
                holder.iv_plus.setEnabled(false);
                holder.iv_minus.setEnabled(false);
                holder.tv_add.setEnabled(false);
                holder.pCardView.setEnabled(false);
            }
        }

        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getProduct_id()));
        Double price = Double.parseDouble(mList.getPrice());

        holder.tv_total.setText("" + price * items);

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = modelList;
                } else {

                    ArrayList<Product_model> filteredList = new ArrayList<>();

                    for (Product_model androidVersion : modelList) {

                        if (androidVersion.getProduct_name().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Product_model>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    private void showImage(String image) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.product_image_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .placeholder(R.drawable.cifatotrain)
                .crossFade()
                .into(iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void showProductDetail(String image, String title, String description, String detail, final int position, String qty) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_detail);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_product_detail_img);
        ImageView iv_minus = (ImageView) dialog.findViewById(R.id.iv_subcat_minus);
        ImageView iv_plus = (ImageView) dialog.findViewById(R.id.iv_subcat_plus);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_product_detail_title);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_product_detail);
        final TextView tv_contetiy = (TextView) dialog.findViewById(R.id.tv_subcat_contetiy);
        final TextView tv_add = (TextView) dialog.findViewById(R.id.tv_subcat_add);

        tv_title.setText(title);
        tv_detail.setText(detail);
        tv_contetiy.setText(qty);
        tv_detail.setText(description);


        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .placeholder(R.drawable.cifatotrain)
                .crossFade()
                .into(iv_image);

        if (dbcart.isInCart(modelList.get(position).getProduct_id())) {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(modelList.get(position).getProduct_id()));
        } else {
//            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
            tv_add.setText("Add To Card");
        }

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<dbcart.getCartCount();i++) {
                    HashMap<String, String> maps = dbcart.getCartAll().get(i);
                    if (maps.get("category_id").equals(modelList.get(position).getCategory_id()))
                    {
                        Toast.makeText(context, "they are "+modelList.get(position).getCategory_id(), Toast.LENGTH_SHORT).show();
                    }else {
//                        ghgh
//                        ShowDialogForClearCart(position);
                        Toast.makeText(context, "they are not "+modelList.get(position).getCategory_id(), Toast.LENGTH_SHORT).show();
                    }
                }
                HashMap<String, String> map = new HashMap<>();

                map.put("product_id", modelList.get(position).getProduct_id());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("increament", modelList.get(position).getIncreament());
                map.put("product_name", modelList.get(position).getProduct_name());

                map.put("price", modelList.get(position).getPrice());
                map.put("stock", modelList.get(position).getIn_stock());
                map.put("title", modelList.get(position).getTitle());
                map.put("unit", modelList.get(position).getUnit());

                map.put("unit_value", modelList.get(position).getUnit_value());

                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
//                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                    tv_add.setText("Add To Card");
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                Double price = Double.parseDouble(map.get("price"));

                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

                notifyItemChanged(position);

            }
        });

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }
            }
        });

    }

}