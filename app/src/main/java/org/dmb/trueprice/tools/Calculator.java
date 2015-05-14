package org.dmb.trueprice.tools;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.edsdev.jconvert.domain.ConversionType;
import com.edsdev.jconvert.persistence.DataLoader;
import com.edsdev.jconvert.presentation.ConversionTypeData;

public class Calculator {
	
	private static final String logHead = Calculator.class.getSimpleName();
	
	List domainData = new DataLoader().loadData(); 
	ConversionTypeData ctd = null;
	
	public Calculator() {
        
        Iterator iter = domainData.iterator();
        while (iter.hasNext()) {
            ConversionType type = (ConversionType) iter.next();
            if (type.getTypeName().equals("Mass")) {
                ctd = new ConversionTypeData(type);
                break;
            }
        }
        
	}
	
	public static double fromSaleToMeasure (double qttValue, double salePrice) throws ParseException {
		double result = ( 1 / qttValue ) * salePrice ;
//		Log.d(logHead, " Measure for 1.00 from qtt [" 
//			+ qttValue + "] at cost [" + salePrice + "] is [" + result + "] =>= "
//			+ "\t  ( 1 / " + qttValue + " ) * " + salePrice
//		);
		return roundDoubleDecimal(result);
	}
	
	public static double fromMeasureToSale (double qttValue, double measurePrice) throws ParseException {
//		double result = (measurePrice /  ( 1 / qttValue )) ;
//		Log.d(logHead, " Measure for " + qttValue + " cost [" + result + "] =>"
//			+ "\t  ( " + measurePrice + " /  ( 1 / " + qttValue + "))" 
//		);
		double result = (measurePrice * qttValue ) ;
//		Log.d(logHead, " Sale Price for qtt [" + qttValue 
//			+ "] at cost [" + measurePrice + "] for 1.00 is [" + result + "] =>= "
//			+ "\t  ( " + measurePrice + " * " + qttValue + ")" 
//		);		
		return roundDoubleDecimal(result);
	}
	
	public static double fromPricesToQtt (double measurePrice, double salePrice) throws ParseException {
		double result = salePrice / measurePrice ;
//		Log.d(logHead, " Qtt for Sale price [" + salePrice 
//			+ "] at Measure Price for 1.00 [" + measurePrice + "] is [" + result + "] =>= "
//			+ "\t  ( " + salePrice + " / " + measurePrice + " ) "
//		);
		return roundDoubleDecimal(result);
	}
		
	
	public static double getSafeDoubleFromString(String strValue) throws ParseException {

		double result = -1 ;
		String strLog = null ;		
		
		try {						
			
			strLog = "Gonna try to parse from [ " + strValue + " ]";
			
			if (strValue.contains("%")) { strValue = strValue.replace("%", "");	}
						
			if (strValue.contains(",")) { strValue = strValue.replaceAll(",", ".") ;}			
			
			if (strValue.endsWith(".")) { strValue = strValue.replace(".", "") ; }
			 
			strLog += " ... new String:[ " + strValue + " ] ... parse .... \t" ;  
			
			try {
				result = Double.valueOf(strValue);
				strLog += " OK !\n" ;
			} catch (Exception e) {
//				Log.e(logHead, "Could not get safe double : " + e.getMessage());
				throw new ParseException ("Could not get safe double from [ " + strValue + " ] : " + e.getMessage() , 0);
//				e.printStackTrace();
			}
			
			
								
			if (result != -1) {
				
				result = roundDoubleDecimal(result) ;
				
			}			
								
			strLog += "returning safe double ? [ " + result + " ]";
			
		} catch (Exception e) {
//			e.printStackTrace();
			throw new ParseException("Error getting safe Double : " + e.getMessage(), -1);
		} finally {
//			 if (strLog != null) {	Log.d(logHead, strLog);		}
		 }
		
		return result;	
	}

	
	public static Double roundDoubleDecimal (Double dbl) throws ParseException {
		
		String strLog = null ;
		
		double newResult = -1 ;
		String str = Double.toString(dbl);
		String ch = "";						
		
		try {
			
			// Because of different user locales, even passinfg '.' to DecimalFormat constructor()
			if (str.contains(",")) {
				ch = "," ;
			} else if (str.contains(".")) {
				ch = "." ;
			}
										
			if (str.substring(str.indexOf(ch)).length() > 3 ) {

				strLog = "Formatting decimal for [ " + dbl + " ] ... " ;
				
				DecimalFormat df = new DecimalFormat("#.000");
				
				String strFormatted = df.format(dbl) ;
				
				strLog += " given formattedResult = [ " + strFormatted + " ] ... " ;
				
//				String strResult = str.replace(
//					str.substring(str.indexOf(ch)),
//					strFormatted
//				);
				
				if (strFormatted.contains(",")) {	strFormatted = strFormatted.replaceAll(",", ".") ;	}
				
				while (strFormatted.endsWith("0")) {						
					strFormatted = strFormatted.substring(0, strFormatted.lastIndexOf("0"));						
				} 
				
				strLog += " given strResult = [ " + strFormatted + " ] ... " ;
				
				newResult = Double.valueOf(strFormatted);
				
				strLog += " newResult is Double:[" + newResult + "]" ;

			}
			
			if (newResult > 0) {	dbl = newResult ;	}
			
//			 if (strLog != null) {	Log.d(logHead, strLog);		}
						
		} catch (Exception e) {
//			e.printStackTrace();
			throw new ParseException("Error getting safe Double : " + e.getMessage(), -1);
		}
		
		return dbl ;
	}
	
	
	
	
}
