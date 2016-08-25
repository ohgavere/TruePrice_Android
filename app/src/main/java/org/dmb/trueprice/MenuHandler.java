package org.dmb.trueprice;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuHandler extends Activity  {

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
////		setContentView(R.layout.activity_menu_handler);
//	}
	
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }
    
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        Log.i(this.getClass().getSimpleName(), "onOptionsItemSelected : ID : " + id);
        
        String bttClickedLabel = "" ;
        
        switch (id) {
/*		case R.id.action_about:
			bttClickedLabel = this.getApplicationContext().getResources().getString(R.string.abc_action_bar_home_description) ;
//			break;
			
		case R.id.action_settings:
			bttClickedLabel = this.getApplicationContext().getResources().getString(R.id.action_settings) ;
//			break;
			
		case R.id.action_link_webapp:
			bttClickedLabel = this.getApplicationContext().getResources().getString(R.id.action_link_webapp) ;
//			break;
*/
		default:

            Toast.makeText(this, "You clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();


            break;
        }

        //Toast.makeText(this, "You clicked : " + bttClickedLabel, Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

    	
    	
    	
    	return super.onPrepareOptionsMenu(menu);
    }
    
	
}
