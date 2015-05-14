package org.dmb.trueprice.adapters;

import org.dmb.trueprice.fragments.RunPdtExpect;
import org.dmb.trueprice.fragments.RunPdtFinal;
import org.dmb.trueprice.fragments.RunPdtInit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RunProduitPagerAdapter extends FragmentPagerAdapter {

	public RunProduitPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	

    @Override
    public Fragment getItem(int i) {
    	
    	Fragment frag = null ;
    	switch (i) {
		
    	case 0:	frag = new RunPdtInit(); break ;
		case 1: frag = new RunPdtExpect(); break ;
		case 2: frag = new RunPdtFinal(); break ;
			
		}
    	return frag ;
    		
//        Fragment fragment = new DemoObjectFragment();
//        Bundle args = new Bundle();
//        args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1); // Our object is just an integer :-P
//        fragment.setArguments(args);
//        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
    	String frag = "";
    	switch (position) {
		
    	case 0:		frag = "Etat initial" 	; break;
		case 1: 	frag = "Previsionnel" 	; break;
		case 2: 	frag = "Resultat" 		; break;
			
		}
    	return frag;
    }	

}
