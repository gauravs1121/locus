package com.locus.locusdemo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.locus.locusdemo.PassDataListener;
import com.locus.locusdemo.R;
import com.locus.locusdemo.model.DataModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 101;
    private static final int CAMERA_INTENT = 102;
    private Context context;
    private ArrayList<DataModel> dataList;
    private DataHolder holder;
    //    int position;
    private String id;
    private PassDataListener listener;

    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();

    DataAdapter(Context context, ArrayList<DataModel> dataList,  PassDataListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener =  listener;
    }

    @NonNull
    @Override
    public DataAdapter.DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_data, viewGroup, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.DataHolder dataHolder, final int i) {
        DataModel model = dataList.get(i);
        holder = dataHolder;

        dataHolder.tvTitle.setText(model.getTitle());
        dataHolder.switchComment.setChecked(model.getOptionModel().isShowComment());

        switch (model.getType()) {
            case "PHOTO":
                dataHolder.ivThumbnail.setVisibility(View.VISIBLE);
                dataHolder.ivDelete.setVisibility(View.VISIBLE);
                dataHolder.tvProvide.setVisibility(View.GONE);
                dataHolder.rgOptions.setVisibility(View.GONE);
                dataHolder.etComment.setVisibility(View.GONE);
                dataHolder.switchComment.setVisibility(View.GONE);
                break;
            case "SINGLE_CHOICE":
                dataHolder.ivThumbnail.setVisibility(View.GONE);
                dataHolder.ivDelete.setVisibility(View.GONE);
                dataHolder.tvProvide.setVisibility(View.GONE);
                dataHolder.rgOptions.setVisibility(View.VISIBLE);
                dataHolder.etComment.setVisibility(View.GONE);
                dataHolder.switchComment.setVisibility(View.GONE);
                break;
            case "COMMENT":
                dataHolder.ivThumbnail.setVisibility(View.GONE);
                dataHolder.ivDelete.setVisibility(View.GONE);
                dataHolder.tvProvide.setVisibility(View.VISIBLE);
                dataHolder.rgOptions.setVisibility(View.GONE);
                dataHolder.etComment.setVisibility(View.GONE);
                dataHolder.switchComment.setVisibility(View.VISIBLE);
                break;
        }

        if (model.getOptionModel().isShowComment())
            dataHolder.etComment.setVisibility(View.VISIBLE);

        dataHolder.etComment.setText(model.getOptionModel().getComment());

        dataHolder.etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dataList.get(i).getOptionModel().setComment(s.toString());
                listener.passData(dataList);
            }
        });

        dataHolder.switchComment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                dataHolder.etComment.setVisibility(View.VISIBLE);
            else
                dataHolder.etComment.setVisibility(View.GONE);

            dataList.get(i).getOptionModel().setShowComment(isChecked);
            listener.passData(dataList);
        });

        dataHolder.ivThumbnail.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)
                        context, Manifest.permission.CAMERA)) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    ((MainActivity) context).startActivityForResult(cameraIntent, CAMERA_INTENT);
                } else {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }

            } else {
                id = model.getId();
                holder = dataHolder;
                if (imageHashmap.get(id) == null) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    ((MainActivity) context).startActivityForResult(cameraIntent, CAMERA_INTENT);
                } else {

//                    Bitmap bmp =
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byte[] byteArray = stream.toByteArray();

                    imageHashmap.get(model.getId()).compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Intent intent = new Intent(context, FullScreenActivity.class);
                    intent.putExtra("Bitmap", imageHashmap.get(model.getId()));

                    context.startActivity(intent);
                }
            }

        });

        dataHolder.ivDelete.setOnClickListener(v -> {
            if (imageHashmap.get(model.getId()) != null) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setCancelable(false);
                dialog.setTitle("Remove Image");
                dialog.setMessage("Are you sure you want to remove this image?");
                dialog.setPositiveButton("Delete", (dialog12, id) -> {
                    dataHolder.ivThumbnail.setImageResource(android.R.drawable.ic_menu_camera);
                    imageHashmap.put(model.getId(), null);
                })
                        .setNegativeButton("Cancel ", (dialog1, which) -> {
                            dialog1.dismiss();
                        });

                final AlertDialog alert = dialog.create();
                alert.show();

            } else
                Toast.makeText(context, R.string.image_not_found, Toast.LENGTH_SHORT).show();

        });


    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public int getItemViewType(int position) {
        return position;
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_INTENT) {
            if (resultCode == RESULT_OK && data != null)
            if (data != null) {
                Bitmap image = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                image = Bitmap.createScaledBitmap(image, 100, 100, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageHashmap.put(id, image);
                image.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                holder.ivThumbnail.setImageBitmap(image);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    ((MainActivity) context).startActivityForResult(cameraIntent, CAMERA_INTENT);
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    class DataHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvProvide;
        ImageView ivThumbnail, ivDelete;
        EditText etComment;
        Switch switchComment;
        RadioGroup rgOptions;
        RadioButton rbGood, rbOk, rbBad;

        DataHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvProvide = itemView.findViewById(R.id.tv_provide);
            ivDelete = itemView.findViewById(R.id.img_delete);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            etComment = itemView.findViewById(R.id.et_comment);
            switchComment = itemView.findViewById(R.id.switch_comment);
            rgOptions = itemView.findViewById(R.id.rg_choice);
            rbGood = itemView.findViewById(R.id.rb_good);
            rbOk = itemView.findViewById(R.id.rb_ok);
            rbBad = itemView.findViewById(R.id.rb_bad);
        }
    }
}
