package org.dmb.trueprice.adapters;

import java.util.HashMap;

import org.dmb.trueprice.RunProduit;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ProductsIconsGalleryImageAdapter extends BaseAdapter {
	
	private final String logHead = this.getClass().getSimpleName();
	
	private Context mContext;

//    private int [] mImageIds = {
//            R.drawable.abricots,
//            R.drawable.ail,
//            R.drawable.airelles,
//            R.drawable.ananas,
//            R.drawable.bananes,
//            R.drawable.carottes,
//            R.drawable.cerise,
//            R.drawable.champignons
//    };
	
//	private String [] mImageName = {} ;
//	
//	private Bitmap [] mIconsBitmaps = {} ;
	
	// Icones triés par Id de produit
	private HashMap<Long, Bitmap> mIconsBitmaps = null ;
	
	// id de produits triés par position
	private HashMap<Integer, Long> mPositionMap = new HashMap<Integer, Long>() ;

    public ProductsIconsGalleryImageAdapter(Context context) {
        mContext = context;
    }
    
    public ProductsIconsGalleryImageAdapter(Context context, HashMap<Long, Bitmap> namesAndBitmaps/*String [] iconNames, Bitmap [] images*/) {
        mContext 		= context ;
//        mImageName 		= iconNames ;
//        mIconsBitmaps 	= images ;
        
        setData(namesAndBitmaps);
    }    

    public int getCount() {
//        return mImageName.length;
    	return mIconsBitmaps.size();
    }

    public Bitmap getItem(int position) {
        return mIconsBitmaps.get(mPositionMap.get(position));
    }

//    public String getIconName (int position) {
//    	return mImageName[position];
//    }
    
    public long getItemId(int position) {
    	Long ret = this.mPositionMap.get(position); 
    	
    	if (ret == null ) { ret = Long.valueOf(Integer.toString(-1)) ; }
	   	
    	Log.d(logHead, "getItemId(" + position + ") == " + ret );
    	
    	if (ret != null && ret > -1) {
    	   	Log.d(logHead, "Try to update UI for this item ..."
    	   	);
    	   	
    		RunProduit.setSelectedProductId(ret);
    		
    		Log.d(logHead, " ... DONE ... ");
    	}
        return ret;
        
      
    }
    
    public void setData(HashMap<Long, Bitmap> namesAndBitmaps) {
    
    	Log.d(logHead, "setData( x" + namesAndBitmaps.size() + " icons)");
    	
    	this.mIconsBitmaps = namesAndBitmaps;
    	
//    	if (mPositionMap == null) { mPositionMap = new HashMap<Integer, Long>(); }
    	
    	int pos = 0 ;
    	
    	String strLog = "";
    	
    	for (Long key : namesAndBitmaps.keySet()) {
//    		strLog += "\t Add pos -> id :: " + pos + " -> " + key + "\n";
    		mPositionMap.put(pos, key);
    		pos++ ;
    	}
    	
    	Log.d(logHead, strLog);
    	
    	
    }

    public View getView(int index, View view, ViewGroup viewGroup) {
        
        ImageView i = new ImageView(mContext);

//        i.setImageResource(mImageIds[index]);
//        i.setLayoutParams(new Gallery.LayoutParams(200, 200));
    
//        i.setScaleType(ImageView.ScaleType.FIT_XY);

//        i.setImageBitmap(mIconsBitmaps[index]);
        i.setImageBitmap(getItem(index));
        
        return i;
    }	



}
