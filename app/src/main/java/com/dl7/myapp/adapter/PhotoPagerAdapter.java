package com.dl7.myapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dl7.helperlibrary.indicator.SpinKitView;
import com.dl7.myapp.R;
import com.dl7.myapp.utils.ImageLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by long on 2016/8/29.
 * 图片浏览适配器
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private List<String> mImgList;
    private Context mContext;
    private OnPhotoClickListener mListener;


    public PhotoPagerAdapter(Context context, List<String> imgList) {
        this.mContext = context;
        this.mImgList = imgList;
    }


    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_photo_pager, null, false);
        final PhotoView photo = (PhotoView) view.findViewById(R.id.iv_photo);
        final SpinKitView loadingView = (SpinKitView) view.findViewById(R.id.loading_view);
        RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                loadingView.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                loadingView.setVisibility(View.GONE);
                photo.setImageDrawable(resource);
                return true;
            }
        };
        ImageLoader.loadFitCenter(mContext, mImgList.get(position), photo, requestListener);
        photo.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (mListener != null) {
                    mListener.onPhotoClick();
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setListener(OnPhotoClickListener listener) {
        mListener = listener;
    }

    public interface OnPhotoClickListener {
        void onPhotoClick();
    }
}
