package org.dmb.trueprice;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuHandlerActionBarCompatAdapter extends ActionBarActivity {

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
               
        String bttClickedLabel = " ! not defined !" ;
        
        if 			(id == R.id.btn_sync_synchronize) 		
        	{ bttClickedLabel = getString(R.string.action_about) ;
        
        } else if 	(id == R.id.action_settings) 	
        	{ bttClickedLabel = getString(R.string.action_settings) ;
		
        } else if 	(id == R.id.action_link_webapp)	
			{ bttClickedLabel = getString(R.string.link_webapp) ;

		}
//        else { Log.i(this.getClass().getSimpleName(),
//					" ID [\t" + id + " ] not matched <=>\n [" 
//					+ "\t" + R.id.action_about + " | "
//					+ "\t" + R.id.action_settings + " | "
//					+ "\t" + R.id.action_link_webapp + " | "
//					+ "]" ) ;
//		}
        
        
        else if (id == android.R.id.home) {
        	
            // This is called when the Home (Up) button is pressed in the action bar.
            // Create a simple intent that starts the hierarchical parent activity and
            // use NavUtils in the Support Package to ensure proper handling of Up.
        									// MainActivity.class
            Intent upIntent = new Intent(this, RunListeStart.class);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is not part of the application's task, so create a new task
                // with a synthesized back stack.
                TaskStackBuilder.from(this)
                        // If there are ancestor activities, they should be added here.
                        .addNextIntent(upIntent)
                        .startActivities();
                finish();
            } else {
                // This activity is part of the application's task, so simply
                // navigate up to the hierarchical parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
        }
        
        
        
        
        
        
        Toast.makeText(this, "You clicked : " + bttClickedLabel, Toast.LENGTH_SHORT).show();
		
        return super.onOptionsItemSelected(item);
    }

    

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	return super.onPrepareOptionsMenu(menu);
    }
    
	
}
