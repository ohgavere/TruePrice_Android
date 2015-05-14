/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.utils.internal;

import java.io.Reader;

import org.dmb.trueprice.objects.SyncGetterRequest;
import org.dmb.trueprice.objects.SyncGetterResponse;
import org.dmb.trueprice.objects.SyncHistory;
import org.dmb.trueprice.objects.SyncInitRequest;
import org.dmb.trueprice.objects.SyncInitResponse;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Guitch
 */
public abstract class GsonConverter {
    
//    private static final Gson converter = new Gson();
    private static final Gson converter = new GsonBuilder().setPrettyPrinting().create();
    
    public static String toJson(Object obj) {
    	try {
	        return converter.toJson(obj);
		} catch (Exception e) {
			Log.e(GsonConverter.class.getSimpleName(), "Error trying to parse [" + obj.getClass().getSimpleName() +"] to String [" + e.getMessage() +"]");
//			e.printStackTrace();
		}
		return null;        
    }

    
    
//    public static Object fromJson(String json) {
//        return converter.fromJson(json, Object.class);
//    }
    
    public static Object fromJson(String json, Class<?> cls) {
    	try {
	        return converter.fromJson(json, cls);
		} catch (Exception e) {
			Log.e(GsonConverter.class.getSimpleName(), "Error trying to parse to Object [" + e.getMessage() +"]");
//			e.printStackTrace();
		}
		return null;        
    }

    public static Object fromJson(Reader reader, Class<?> cls) {
    	try {
    		return converter.fromJson(reader, cls);
		} catch (Exception e) {
			Log.e(GsonConverter.class.getSimpleName(), "Error trying to parse to Object [" + e.getMessage() +"]");
//			e.printStackTrace();
		}
		return null;        
    }

    
/////////////
//    
//    SYNC INIT
//    
//    public static SyncInitRequest fromJsonInitRequest(Reader reader) {
//        return converter.fromJson(reader,SyncInitRequest.class);
//    }
    public static SyncInitRequest fromJsonInitRequest(String jsonData) {
    	try {
            return converter.fromJson(jsonData,SyncInitRequest.class);
		} catch (Exception e) {
			Log.e(GsonConverter.class.getSimpleName(), "Error trying to parse to SyncInitRequest [" + e.getMessage() +"]");
//			e.printStackTrace();
		}
		return null;
    }
    
    public static SyncInitResponse fromJsonInitResponse(Reader reader) {
    	try {
	        return converter.fromJson(reader,SyncInitResponse.class);
		} catch (Exception e) {
			Log.e(GsonConverter.class.getSimpleName(), "Error trying to parse to SyncInitResponse [" + e.getMessage() +"]");
//			e.printStackTrace();
		}
	return null;        
    }
    public static SyncInitResponse fromJsonInitResponse(String jsonData) {
    	try {
    		return converter.fromJson(jsonData,SyncInitResponse.class);
		} catch (Exception e) {
			Log.e(GsonConverter.class.getSimpleName(), "Error trying to parse to SyncInitResponse [" + e.getMessage() +"]");
//			e.printStackTrace();
		}
		return null;    	
    }
    
/////////////
//    
//    SYNC GETTER
//    
    
    public static SyncGetterRequest fromJsonGetterRequest(String jsonData) {
    	try {
	        return converter.fromJson(jsonData,SyncGetterRequest.class);
		} catch (Exception e) {
			Log.e(GsonConverter.class.getSimpleName(), "Error trying to parse to SyncGetterRequest [" + e.getMessage() +"]");
//			e.printStackTrace();
		}
		return null;        
    }
    public static SyncGetterResponse fromJsonGetterResponse(String jsonData) {
    	try {
    		return converter.fromJson(jsonData,SyncGetterResponse.class);
		} catch (Exception e) {
			Log.e(GsonConverter.class.getSimpleName(), "Error trying to parse to SyncInitResponse [" + e.getMessage() +"]");
//			e.printStackTrace();
		}
		return null;    	
    }
    
/////////////
//
//	  SYNC HISTORY
//    
    public static SyncHistory fromJsonSyncHistory(String jsonData) {
    	try {
    		return converter.fromJson(jsonData, SyncHistory.class);
		} catch (Exception e) {
			Log.e(GsonConverter.class.getSimpleName(), "Error trying to parse to SyncInitResponse [" + e.getMessage() +"]");
//			e.printStackTrace();
		}
		return null;    	
    }    
    
/////////////
//    
//    SYNC SETTER
//    
}
