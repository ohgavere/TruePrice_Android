package org.dmb.trueprice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.content.ClipboardManager ;
import android.content.ClipData;

import org.dmb.trueprice.listeners.LauncherListener;
import org.dmb.trueprice.tools.AppDataProvider;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends MenuHandlerActionBarCompatAdapter {
	
	private final String logHead = this.getClass().getSimpleName();
	
	public final static String 	FLAG_SIGNED_IN		= "com.dmb.trueprice.android.FLAG_SIGNED_IN" ;
	public final static int 	REQID_LOGIN_RESULT	= 0 ;
	private static boolean 			IS_USER_SIGNED_IN	= false ;

	
	private static AppDataProvider provider = null ;	
	
	public static String currentUserEmail ;
	private static String loggedUser = "";

    private static void setLoggedUser(String loggedUser) {
        Launcher.loggedUser = loggedUser;
        try {
            provider.setUserFolder(loggedUser);
        } catch (Exception e) {
            Log.w("Launcher", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Change the signin status for one user.
     * As this lock the user email, it should be recorded first.
     */
	public void changeSigninStatus() { IS_USER_SIGNED_IN = ! IS_USER_SIGNED_IN;
        if (IS_USER_SIGNED_IN == true) {
            Log.d(logHead, "Just logged in with mail [" + currentUserEmail + "]");
            setLoggedUser(currentUserEmail);
        } else {
            loggedUser = "";
        }
    }
	public static boolean getSigninStatus() { return IS_USER_SIGNED_IN ; }




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_launcher);
				
		
		
		
		LauncherListener lstnr = new LauncherListener(Launcher.this);
		
		Button btt = (Button) findViewById(R.id.launcher_btt_login);	
		
		btt.setOnClickListener(lstnr);
		
		
		btt = (Button) findViewById(R.id.launcher_btt_sync);
		
		btt.setOnClickListener(lstnr);
		
		
		btt = (Button) findViewById(R.id.launcher_btt_run);
		
		btt.setOnClickListener(lstnr);
		
		
		btt = (Button) findViewById(R.id.launcher_btt_calc);
		
		btt.setOnClickListener(lstnr);



        logonStatusImageView = (ImageView) findViewById(R.id.img_launcher_login_status);

        logonStatusTextView = (TextView) findViewById(R.id.launcher_txt_login_status);

	}

    private ArrayAdapter<String> adapter ;

    private Spinner spin ;

    private ImageView logonStatusImageView ;
    private TextView  logonStatusTextView ;

    @Override
    protected void onStart() {
        super.onStart();

        /*
        Toast.makeText(	Launcher.this, "YOU ARE " +
                        ( getSigninStatus() ? "SIGNED IN" : "DISCONNECTED") ,
                Toast.LENGTH_SHORT).show();
        */

        //showSigninStatus();

    }

    private void showSigninStatus () {

        if (getSigninStatus() == true) {

            logonStatusImageView.setImageResource(R.drawable.connect_light_green);

            logonStatusTextView.setText(R.string.launcher_login_status_on);

            logonStatusTextView.setTextColor(getResources().getColor(R.color.launcher_logon_status_on));

        } else {

            logonStatusImageView.setImageResource(R.drawable.connect_light_grey);

            logonStatusTextView.setText(R.string.launcher_login_status_off);

            logonStatusTextView.setTextColor(getResources().getColor(R.color.launcher_logon_status_off));

        }
    }

    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
        showSigninStatus();

		try {
//			if (currentUserEmail == null ) {
//				currentUserEmail = Launcher.currentUserEmail ;}


            if (provider == null) {
                Log.d("Launcher", "Provider is null. Creation ...");
                provider = new AppDataProvider(Launcher.this);
            } else {
                Log.d("Launcher", "Provider exist for [" + provider.getUserFolder() + "] -> we need user [" + loggedUser + "]. Get user folder -> ...");
                currentUserEmail = provider.getUserFolder();
                Log.d("Launcher", "... -> got user folder [" + currentUserEmail + "]");
            }


			if (spin == null) {
                Log.d("Launcher", "Spinner 'spin' is null. Creation ...");
                spin = (Spinner) findViewById(R.id.launcher_user_id_spinner);
            }


            boolean noUserFound = false ;

			if (adapter == null || ( loggedUser != null && provider.isNewUser() == true ) ) {
                Log.d("Launcher", "spin Adapter is null. Creation ...");

                List<String> emails = new ArrayList<String>();
/*
                if (provider.isNewUser() == true) {
                    emails.add(loggedUser) ;
                    Log.i(logHead, "Added new User to spinner adapter : [" + loggedUser + "]");
                }
*/
                emails.addAll(provider.getAllUsersIdentifiers());


                // Now if there's no data in 'emails', this is a 'first use' of this app,
                // so we set a message to help user to go on.



                if (emails.size() == 0 || emails.isEmpty()) {

                    Log.i(logHead, "No user found at all (size = " + emails.size() + "). Gonna 'start' tutorial ...");
                    noUserFound = true ;

                    String helpmsg_clic = "Première utilisation ? Cliquez ici !" ;
                    String helpmsg_content_0 = "\n\t\tBienvenue sur l'application True|$|Price !"
                        + "\n\n Si vous voyez ce message, vous venez probablement de l'installer."
                            + "\n Les prochaines étapes sont : ";
                    String helpmsg_content_1 =
                        "1. Si vous ne possédez pas encore de compte True|$|Price, rendez-vous sur la plateforme Web. L'inscription se fait en moins de 30 secondes !"
                            + "\n\t https://www.trueprice.ddns.net" ;
                    String helpmsg_content_2 =
                        "2. Pour pouvoir utiliser l'application mobile, vous devez créer au minimum une liste contenant au moins un produit.";
                    String helpmsg_content_3 =
                        "3. Revenez sur l'application mobile. Sur l'écran d'acceuil, entrez dans l'application 'Login' pour vous identifier.\n(Connexion internet requise jusqu'à la fin de l'étape suivante).";
                    String helpmsg_content_4 =
                        "4. Une fois connecté, rendez-vous dans l'application 'Sync' pour synchroniser une ou plusieurs listes de courses.\n(Un tutoriel plus complet est disponible dans l'application 'Sync').";
                    String helpmsg_content_5 =
                        "5. Vous pouvez désormais utiliser les listes synchronisées pour calculer/enregistrer leur prix, tout en comparant avec leurs statistiques pour un achat malin !";
                    String helpmsg_content_6 =
                        "6. De retour à la maison, au bureau ou à proximité d'une connexion internet gratuite, synchronisez vos listes vers la plateforme Web.\nLes statistiques de VOS produits seront mises à jour en quelques secondes.";
                    String helpmsg_content_7 =
                        "\nVous êtes maintenant un consommateur expérimenté ! Félicitations !!!"
                    ;

                    emails.add(helpmsg_clic);
                    emails.add(helpmsg_content_0);
                    emails.add(helpmsg_content_1);
                    emails.add(helpmsg_content_2);
                    emails.add(helpmsg_content_3);
                    emails.add(helpmsg_content_4);
                    emails.add(helpmsg_content_5);
                    emails.add(helpmsg_content_6);
                    emails.add(helpmsg_content_7);

                    Log.i(logHead, "... Ok.");
                }

                adapter = new ArrayAdapter<String> (
                        this,
                        R.layout.listitem_simple_item_white,
                        emails
                );

                //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    //OK only API < 14
                //adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

                // simple_spinner_item  ==>> trop petit, pas de padding
                // select_dialog_item   ==>> textSize ok mais pas de scroll
                // support_simple_spinner_dropdown_item ==>>> TextSize BEST but one line no scroll
                // listitem_simple_item_white_multiline => multiline don't work

                // CRACHES
                // expandable_list_content
                // simple_expandable_list_item_1
                // activity_list_item


                adapter.setDropDownViewResource(R.layout.listitem_simple_item_white_multiline);


            }
					

			spin.setAdapter(adapter);
				
            if (noUserFound == false) {

                spin.setOnItemSelectedListener(new OnItemSelectedListener() {



                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String selectedEmail = adapter.getItem(position);

                        int actualPosition = adapter.getPosition(currentUserEmail) ;

                        Log.d(logHead,
                                "Gonna set currentUserEmail to [ " + selectedEmail + " ] at pos (" + position + ")"
                        );

                        if (setCurrentUserMail(selectedEmail) == false ) {
                            Toast.makeText(Launcher.this, "Please log out before changing user.", Toast.LENGTH_SHORT).show();

                            spin.setSelection(actualPosition);

                        } else {

                            int sdk_Version = android.os.Build.VERSION.SDK_INT;
                            if (sdk_Version < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                clipboard.setText(selectedEmail);
                                //Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
                            } else {
                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                android.content.ClipData clip = android.content.ClipData.newPlainText("User Email", selectedEmail);
                                clipboard.setPrimaryClip(clip);
                                //Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
                            }

                            Toast.makeText(Launcher.this, R.string.email_to_clipboard, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}

                });

            }

            spin.setClipChildren(true);
            spin.setHorizontalScrollBarEnabled(true);
            spin.setVerticalScrollBarEnabled(true);



				//Log.i(logHead, "Got appDataProvider with userEmail[" + currentUserEmail + "]");
//			}
            // else
            if (currentUserEmail != null) {

                int pos = ( adapter == null ? -1 : adapter.getPosition(currentUserEmail) ) ;

                Log.i(logHead," (set spinner selected position [" + pos + "] from email [" + currentUserEmail + "])");

                spin.setSelection(pos);


            } else if (loggedUser != null) {

                int pos = (adapter == null ? -1 : adapter.getPosition(loggedUser));

                Log.i(logHead, " (set spinner selected position [" + pos + "] from logged in user [" + loggedUser + "])");

                spin.setSelection(pos);

            } else {
                Log.d(logHead, "Email ends with null ? [" + currentUserEmail + "]");
            }

		} catch (Exception e) {
			Log.w(logHead, "onResume() failed with userEmail[" + currentUserEmail + "] because ==>\n" + e.getMessage());
            e.printStackTrace();
		}			
		
	}
	
	
	public static boolean setCurrentUserMail(String mail) {
		if( getSigninStatus() == false ) {
            Launcher.currentUserEmail = mail;
            Log.d("Launcher", "NEW user selected with email [" + mail + "] logged in ? -> " + getSigninStatus());
            return true;
        } else {
            Log.d("Launcher", "Cannot change selected user : already connected with email [" + currentUserEmail + "]");

            return false ;
        }
    }
//	public static void setCurrentUserPseudo(String currentUserPseudo) {
//		Launcher.currentUserPseudo = currentUserPseudo;
//	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 * 
	 * arg0 == request Code
	 * arg1 == result Code
	 */
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
//		super.onActivityResult(arg0, arg1, arg2);
		
		switch (arg0) {
			
			case REQID_LOGIN_RESULT :
				
				if (arg1 == RESULT_OK) {
					
//					if (arg2 != null && arg2.getBooleanExtra
//						(Launcher.FLAG_SIGNED_IN, false) ) {
////					
//						IS_USER_SIGNED_IN = true ;
////						
//						Toast.makeText(	Launcher.this,
//								
//								"YOU ARE SIGNED IN" ,
//								
////								"Double check for login result : [" + 
////								arg2.getBooleanExtra(FLAG_SIGNED_IN, false) + "]",
//								
//							Toast.LENGTH_SHORT	).show();
					
					if (arg2 != null && arg2.getBooleanExtra(FLAG_SIGNED_IN,
							// par defaut on renvoye l'etat actuel 
							getSigninStatus()	) 
							//on teste si l'�tat recu a chang�
							!= getSigninStatus() 
					) {
						
						// on change l'etat dans le launcher
						// == RECUPERE LE RESULTAT
						changeSigninStatus();
						
						Toast.makeText(	Launcher.this, "YOU ARE " + 
								( getSigninStatus() ? "SIGNED IN" : "DISCONNECTED") ,
						Toast.LENGTH_SHORT).show();
						
					} else {
//						IS_USER_SIGNED_IN = false ;
						changeSigninStatus();
						Log.d(this.getClass().getSimpleName(), 
						"Login Extra check FAILED : result was OK but FLAG == " + getSigninStatus());
					}
//					
				} else if (arg0 == RESULT_CANCELED) {
					
//					Toast.makeText(	Launcher.this, "Operation cancelled" ,
//					Toast.LENGTH_SHORT).show();
					
				} else {
					
					IS_USER_SIGNED_IN = false ;
					Log.d(this.getClass().getSimpleName(), 
					"Login resultCode failed : " + arg1 +", FLAG == " + getSigninStatus());					
				}
				
				Log.d(this.getClass().getSimpleName(), "Login stopped, FLAG == " + getSigninStatus());
				
				break;

		default:
			
			Toast.makeText(Launcher.this, "Don't know from wich activity you come from [" 
					 + " reqID = " + arg0 + " ]"	, Toast.LENGTH_SHORT	).show();
			
			break;
		}
		
		
		
		
		
		
	}
	
	
	
}
