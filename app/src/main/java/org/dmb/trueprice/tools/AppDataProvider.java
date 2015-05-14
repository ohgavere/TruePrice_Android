package org.dmb.trueprice.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dmb.trueprice.objects.SyncGetterResponse;
import org.dmb.trueprice.objects.SyncHistory;
import org.dmb.trueprice.utils.internal.GsonConverter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class AppDataProvider {
	
	private static final String logHead = AppDataProvider.class.getSimpleName();
	
//	private static final String userFolderPrefix = "app_" ;
	private String userFolder ;
	
	private File currentUserFileFolder ;
		
	private static Context ctx ;
	
	public static final String SYNC_INIT_REQ_FILENAME = "init-request.json" ;
	public static final String SYNC_INIT_RESP_FILENAME = "init-response.json" ;
	
	public static final String SYNC_GETTER_RESP_FILENAME = "getter-response.json" ;
	public static final String SYNC_HISTORY_FILENAME = "sync-history.json" ;
	
	public static final String GENERIC_ICON_FOLDER = "icons@generic" ;
	public static final String USER_ICON_FOLDER = "icons" ;	
	
	public AppDataProvider (Context context) {
		AppDataProvider.ctx = context ;
		Log.d(logHead, "Initialized  WITHOUT  user Mail To Make Folder !! ");
	}
	
	public AppDataProvider (Context context, String userMailToMakeFolder) throws Exception {
		AppDataProvider.ctx = context ;
		this.userFolder = userMailToMakeFolder;
		checkUserFolderExists(this.userFolder);
		Log.d(logHead, "Initialized with userMailToMakeFolder : [" + userMailToMakeFolder 
			+ "] ==> exists() ? " + (currentUserFileFolder == null ? "FALSE" : "TRUE"));
	}
	
	public FileOutputStream getOutStream(String filename, int mode) throws FileNotFoundException {
		return ctx.openFileOutput(filename, mode) ;
	}
	
	public FileInputStream getInStream(String filename) throws FileNotFoundException {
		return ctx.openFileInput(filename) ;
	}
	
	
	public void write(String filename, int mode, byte[] donnees) throws FileNotFoundException, IOException {
		write(getOutStream(filename, mode), donnees);
	}
	public void write(FileOutputStream fOut, byte[] donnees) throws IOException {
		fOut.write(donnees);
		fOut.close();
	}

	public int read(String filename) throws FileNotFoundException, IOException {
		return read(getInStream(filename));
	}
	
	public int read(FileInputStream fIn) throws IOException {
		int returned = fIn.read();
		fIn.close();
		return returned;
	}
	
	
	public void setUserFolder(String userFolder) throws Exception {
        Log.i(logHead, "PROVIDER try changing user folder from ["
                + this.userFolder + "] = to => [" + userFolder + "] ...");
		this.userFolder = userFolder;
		checkUserFolderExists(userFolder);
        Log.i(logHead, "PROVIDER CHANGED.");
	}
	public String getUserFolder() {
		return userFolder;
	}
	
	
	
	public boolean checkFileExist(String filename) throws FileNotFoundException {
		return ctx.openFileInput(filename) == null ? false : true ;
	}
	
	public boolean checkUserFileExist(String filename) throws FileNotFoundException {
		return new File(userFolder + filename) == null ? false : true ;
	}
	
	public boolean delete(String filename) throws FileNotFoundException {
		if (ctx.deleteFile(filename) == true) {
			return true ;
		} else {
			try {
				checkFileExist(filename);
			} catch (Exception e) {
				throw new FileNotFoundException("This file does not exist, can't be deleted !");
			}
		}
		return false;
	}
	
	
	
	public File getApplicationFolder() {
		return ctx.getFilesDir() ;
	}
	
	public List<String> getAllUsersIdentifiers() {
		
		List<String> emails = new ArrayList<String>();
		
		File fMain = getApplicationFolder();
		
		if (fMain != null) {
			
			String[] childs = fMain.list();
			
			for (String child : childs) {
				if( ! child.contains("@") || ! child.contains(".")) {
					// log cannot add
				} else  {
					if (child == getUserFolder()) {
                        // Log it's already added ion adapter by the launcher
                    } else {
                        emails.add(child);
                    }
				}
			}
			
		} else {
//			log error 
		}
		
		return emails;
	}
	
	
	public String readFileInUserFolder (String filename) throws IOException  {
		
		String out = "";
		BufferedReader br = null ;
		try {
			
			br = new BufferedReader (new FileReader 
					(new File (currentUserFileFolder, filename))) ;
			
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				out += sCurrentLine;
			}
 
		} catch (IOException e) {
			Log.e(logHead, "Error reading file [" + filename +"] in user folder. Cause : " + e.getMessage());
		} finally {
			if (br != null) br.close();
		}		
		
//		return read(getInStream(filename));
		return out;
	}
	
	public String getUserInitRequestContent() {
		String out = null ;
		try {
			out = readFileInUserFolder(SYNC_INIT_REQ_FILENAME);
		} catch (Exception e) {
			Log.e(logHead, "Count not get Init Request Content : " + e.getMessage());
		}
		return out ;
	}
	
	public String getUserInitResponseContent() {
		String out = null ;
		try {
			out = readFileInUserFolder(SYNC_INIT_RESP_FILENAME);
		} catch (Exception e) {
			Log.e(logHead, "Count not get Init Request Content : " + e.getMessage());
		}		
		return out ;		
	}
	
	public String getUserGetterResponseContent() {
		String out = null ;
		try {
			out = readFileInUserFolder(SYNC_GETTER_RESP_FILENAME);
		} catch (Exception e) {
			Log.e(logHead, "Count not get Getter Resoponse Content : " + e.getMessage());
		}		
		return out ;		
	}	
		
	public String getUserSyncHistoryContent() {
		String out = null ;
		try {
			out = readFileInUserFolder(SYNC_HISTORY_FILENAME);
		} catch (Exception e) {
			Log.e(logHead, "Count not get Sync History Content : " + e.getMessage());
		}		
		return out ;		
	}		
	
	
	private boolean userFolderChecked = false ;
	public void writeFileInUserFolder (String filename, String content) throws Exception  {
		
		if (userFolderChecked == false) {
			checkUserFolderExists(getUserFolder());
		}
		
//		String out = "";
		BufferedWriter bw = null ;
		try {
			
			File f = new File (getCurrentUserFileFolder().getAbsoluteFile(), filename) ;
			String outLog = "" ;
			
			outLog += "WriteFileInUserFolder() =>\n\t We want [" + currentUserFileFolder + File.separator 
					+ filename + "]" + "\n\t We get [" + f.getAbsolutePath() + "]" ;
			
			if (f.exists()) {				
				outLog += "\n\tFile [" + f.getName() + "] writeable ? [" + f.canWrite() + "]";
				if (! f.canWrite()) {
					f.setWritable(true);
					outLog += "\t => try to change ... writeable ? [" + f.canWrite() + "]" ;
				}
				Log.i(logHead, outLog);
				
			} else {
				
				Log.i(logHead, outLog + "\n File [" + f.getAbsolutePath() + "] DOES NOT EXIST !");
				
				f.createNewFile();
				
				if (f.exists()) {				
					outLog = "\n2. File [" + f.getAbsolutePath() + "] writeable ? [" + f.canWrite() + "]";
					if (! f.canWrite()) {
						f.setWritable(true);
						outLog += " try to change ... writeable ? [" + f.canWrite() + "]" ;
					}
					Log.i(logHead, outLog);
				} else {
					Log.i(logHead, "2. File [" + f.getAbsolutePath() + "] DOES NOT EXIST !");					
				}
			}
			
			bw = new BufferedWriter(new FileWriter(f)) ;
			
			bw.write(content);
 
		} catch (IOException e) {
			Log.e(logHead, "Error writing file [" + filename +"] in user folder. Cause : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (bw != null) bw.close();
		}		
		
//		return read(getInStream(filename));
//		return out;
	}	
	
	

	public boolean writeFileInUserIconFolder (String filename, Bitmap bitmap) throws Exception  {
			
			File userIconFolder = getCurrentUserIconFolder();
			
			FileOutputStream fOut  = null ;
			try {
				
				File f = new File (userIconFolder.getAbsoluteFile(), filename) ;
//				String outLog = "" ;
//				
//				outLog += "Try with File =>\t We want [" + userIconFolder + File.separator 
//						+ filename + "]" + "\t We get [" + f.getAbsolutePath() + "] ==> \n" ;
				
				if (f.exists()) {				
//					outLog += " File exists ! [" + f.getName() + "] writeable ? [" + f.canWrite() + "]";
					if (! f.canWrite()) {
						f.setWritable(true);
//						outLog += " try to change ... writeable ? [" + f.canWrite() + "]" ;
					}
//					Log.i(logHead, outLog);
					
				} else {
					
//					Log.i(logHead, outLog + "\n File [" + f.getAbsolutePath() + "] does not exist.");
					
					f.createNewFile();
					
					if (f.exists()) {				
//						outLog = "\n2. File is created successfully [" + f.getAbsolutePath() + "] writeable ? [" + f.canWrite() + "]";
						if (! f.canWrite()) {
							f.setWritable(true);
//							outLog += " try to change ... writeable ? [" + f.canWrite() + "]" ;
						}
//						Log.i(logHead, outLog);
					} else {
						Log.i(logHead, "2nd & last try. File [" + f.getAbsolutePath() + "] DOES NOT EXIST !");					
					}
				}
				
				
			    fOut = new FileOutputStream(f);
			    

			    try {
				    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
				    return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false ;
				} 
			    
			} catch (IOException e) {
				Log.e(logHead, "Error writing bitmap [" + filename +"] in user icon folder. Cause : " + e.getMessage());
				e.printStackTrace();
			} finally {
				if (fOut != null) {fOut.flush();fOut.close();}
			}		
			
//			return read(getInStream(filename));
			return false;
		}	
		
	
	
	public boolean writeFileInGenericIconFolder (String filename, Bitmap bitmap) throws Exception  {
		
		File genericIconFolder = getGenericIconFolder();
		
		FileOutputStream fOut  = null ;
		try {
			
			File f = new File (genericIconFolder.getAbsoluteFile(), filename) ;
//			String outLog = "" ;
//			
//			outLog += "Try with File =>\t We want [" + genericIconFolder + File.separator 
//					+ filename + "]" + "\t We get [" + f.getAbsolutePath() + "] ==> \n" ;
			
			if (f.exists()) {				
//				outLog += " File exists ! [" + f.getName() + "] writeable ? [" + f.canWrite() + "]";
				if (! f.canWrite()) {
					f.setWritable(true);
//					outLog += " try to change ... writeable ? [" + f.canWrite() + "]" ;
				}
//				Log.i(logHead, outLog);
				
			} else {
				
//				Log.i(logHead, outLog + "\n File [" + f.getAbsolutePath() + "] does not exist.");
				
				f.createNewFile();
				
				if (f.exists()) {				
//					outLog = "\n2. File is created successfully [" + f.getAbsolutePath() + "] writeable ? [" + f.canWrite() + "]";
					if (! f.canWrite()) {
						f.setWritable(true);
//						outLog += " try to change ... writeable ? [" + f.canWrite() + "]" ;
					}
//					Log.i(logHead, outLog);
				} else {
					Log.i(logHead, "2nd & last try. File [" + f.getAbsolutePath() + "] DOES NOT EXIST !");					
				}
			}
			
			
		    fOut = new FileOutputStream(f);
		    

		    try {
			    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			    return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false ;
			} 
		    
		} catch (IOException e) {
			Log.e(logHead, "Error writing bitmap [" + filename +"] in generic icon folder. Cause : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (fOut != null) {fOut.flush();fOut.close();}
		}		
		
//		return read(getInStream(filename));
		return false;
	}	
		
	public Bitmap getBitmapFromGenericIconFolder (String iconName) throws Exception  {
		
		File genericIconFolder = getGenericIconFolder();
		
		Bitmap bmp = null ;
		
		try {
			
			File f = new File (genericIconFolder.getAbsoluteFile(), iconName) ;

			if (f.exists()) {			
				
				if (! f.canRead()) {
					f.setReadable(true);
				}
				
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//				Bitmap bitmap = BitmapFactory.decodeFile(photoPath, /*options*/);
//				selected_photo.setImageBitmap(bitmap);
				
				bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
				
				return bmp ;
				
			} else {
								
				throw new Exception("File [" + f.getAbsolutePath() + "] does not exist.");
				
			}
			
		    
		} catch (IOException e) {
			Log.e(logHead, "Error getting bitmap [" + iconName +"] from generic icon folder. Cause : " + e.getMessage());
			e.printStackTrace();
		} 	
		
//		return read(getInStream(filename));
		return bmp;
	}	
		
				
	
	public void writeUserInitRequestContent(String content) {
//		String out = null ;
		try {
//			out = 
					writeFileInUserFolder(SYNC_INIT_REQ_FILENAME, content);
		} catch (Exception e) {
			Log.e(logHead, "Count not get Init Request Content : " + e.getMessage());
		}
//		return out ;
	}
	
	public void writeUserInitResponseContent(String content) {
//		String out = null ;
		try {
//			out =
					writeFileInUserFolder(SYNC_INIT_RESP_FILENAME, content);
		} catch (Exception e) {
			Log.e(logHead, "Count not get Init Request Content : " + e.getMessage());
		}		
//		return out ;		
	}
	
	
	private void setCurrentUserFileFolder(File currentUserFileFolder) {
		this.currentUserFileFolder = currentUserFileFolder;
	}
	private File getCurrentUserFileFolder() {
		return currentUserFileFolder;
	}
	private File getCurrentUserIconFolder() {
		File finalFile = getCurrentUserFileFolder() ;
		finalFile = new File(finalFile, USER_ICON_FOLDER);	
		
		if (! finalFile.exists()) { if (! finalFile.mkdir() ) {
			Log.w(logHead, "User Icon Folder can't be found and/or created !");
		}}
		
		return finalFile ;
	}
	private File getGenericIconFolder() {
		File finalFile = getApplicationFolder() ;
		finalFile = new File(finalFile, GENERIC_ICON_FOLDER);	
		
		if (! finalFile.exists()) { if (! finalFile.mkdir() ) {
			Log.w(logHead, "Generic Icon Folder can't be found and/or created !");
		}}
		
		return finalFile ;
	}
	
	public ArrayList<String> getGenericIconFolderFilesNames() {
		ArrayList<String> filesNames = new ArrayList<String>();
		for (String name : getGenericIconFolder().list()) {
//			Log.d(logHead, "Add Icon (G) to files names");
			filesNames.add(name);
		}
		return filesNames;
	}		
	public ArrayList<String> getUserIconFolderFilesNames() {
		ArrayList<String> filesNames = new ArrayList<String>();
		for (String name : getCurrentUserIconFolder().list()) {
//			Log.d(logHead, "Add Icon (U) to files names");
			filesNames.add(name);
		}
		return filesNames;
	}

    private static boolean 			IS_NEW_USER	= false ;

    private void setIsNewUser (boolean isNewUser) {
        IS_NEW_USER = isNewUser ;
    }

    public boolean isNewUser() {
        return IS_NEW_USER ;
    }

	private boolean createUserFolder(String userMail) {

        setIsNewUser(true);

		Log.i(logHead, "Need to create user folder [ {" 
				+ ctx.getFilesDir() + "}" + File.separator + userMail +"]"	);
		
		// Create folder in /data/data/org.../{folder}
//		ctx.getDir(userMail, ctx.MODE_PRIVATE);
		
		// Create folder in /data/data/org.../files/{folder}
//		File f = new File(ctx.getFilesDir() + File.separator + userMail);
//		ctx.getDir(f.getAbsolutePath(), ctx.MODE_PRIVATE);
		/*	Separator not accepted	*/
		
		// Create folder in /data/data/org.../files/a.b@mail.com
		File f = new File(ctx.getFilesDir(), userMail);
		f.mkdir();
		
		Log.i(logHead, "Created user folder ? absPath[" + f.getAbsolutePath() 
			+ "] isDir?[" + f.isDirectory() +"]"
		);
		
		if (f.exists() && f.isDirectory()) {
			currentUserFileFolder = f ;
			return true ;
		}
		return false ;
				
	}
	private boolean checkUserFolderExists(String userMail) throws Exception{
		
		File dir = ctx.getFilesDir();
		
		String [] childs = dir.list();
		
//		Log.i(logHead, "Got FilesDir() for App == [" + dir.getAbsolutePath() + "], we want user folder [" + userMail +"]" );
		
		/*	DOES NOT WORK	*/
//		if( childs == null || childs.length < 1) { createUserFolder(userMail);}
		
//		else {
		
		
		
		
			boolean exists = false;

			for (String fname : childs) {
			
				if (fname.compareToIgnoreCase(userMail) == 0) {
					Log.i(logHead, "User folder exists [...." + File.separator + fname +"]");
					exists = true ;
					try {
						currentUserFileFolder = new File(ctx.getFilesDir(), fname);					
					} catch (Exception e) {
//						Log.e(logHead, "Can't get User Folder as file cause : "+ e.getMessage());
						e.printStackTrace();
					}
				} 
				else {
//					 Log.i(logHead, "file name does not match [" + userMail +"]<=!=>[" + fname +"]");
				}
				
			}
				
				
			// Si n'existe pas, on essye de le créer
			if (exists == false) { exists = createUserFolder(userMail); }
//			else {Log.w(logHead, "The folder exists !! ? ");}
			
			// Si il a été créé, on le recupere
			if (exists == true) { setCurrentUserFileFolder(new File(ctx.getFilesDir(),userMail));}
			
			
			// Test final
			// TODO Superflu ? ==> non car une erreur peut survenir si le if précédent est vérifié
			if (getCurrentUserFileFolder() == null) {
				throw new Exception("Cannot create or get as file the asked user folder ") ;
				
			} else {
				return true ;
			}
			
			
			
			
			
			
//		}
		
	}
	
	public void doExample(){
		
		String outLog ; 
		
		File f1 = ctx.getFilesDir() ;
		File f2 = ((Activity)ctx).getApplication().getDir("data-dir-1", ctx.MODE_WORLD_READABLE);
		String [] fListe = ctx.fileList();
		
				
		outLog = "\n getFilesDir() => [" + f1.getName() 
				+ "]\t on path[" 		+ f1.getAbsolutePath()
				+ "]\n\t isDir? [" 		+ f1.isDirectory() 
				+ "]\t readable[" 	+ f1.canRead() 
				+ "]\t writeable[" 	+ f1.canWrite() + "]"
				+ "]\t executable[" + f1.canExecute() + "]";
		
		f1.setReadable(true);
		f1.setWritable(true);		
		
		outLog = "\n getFilesDir() changed ? => " 
		+ " readable[" 	+ f1.canRead() 
		+ "]\t writeable[" 	+ f1.canWrite() + "]";				
		
		try {
			outLog += "\n getApplication().getDir(\"data-dir-1\" => [" + f2.getName() 
					+ "]\t on path[" 		+ f2.getAbsolutePath() 
					+ "]\n\t isDir? [" 		+ f2.isDirectory() 
					+ "]\t readable[" 	+ f2.canRead() 
					+ "]\t executable[" + f2.canExecute() + "]"
					+ " createNew ? [" + f2.createNewFile() +"]";	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int i=1;		
		outLog += "\n fileList() [ ...\n" ;
		
		for (String f : fListe) {			
			outLog += "File " + i + "[" + f + "]\t" ;			
			if (i % 3 == 0) outLog += "\n" ;

			try {
				File fichier = new File(f);
				if (fichier != null) {
					String outmore ;
					outmore = fichier.getAbsolutePath();
				
					if (outmore != null) ; {
						outLog += "at absPath[" + outmore +"]\n" ;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			i++;
		}
		outLog += "... ]" ;
		
		Log.i(logHead, outLog);
		
		
//		
//		File [] files = File.listRoots() ;
//		
//		if (files != null && files[0] != null) {
//			 
//			
//			outLog = "Got listRoots() file[0] => AbsPath[" ;	
//			outLog +=
//				files[0].getAbsolutePath() + "] isDir? [" + 
//				files[0].isDirectory()+"] with free space [" ;
//			outLog += files[0].getUsableSpace() + " bytes]." ;		
//			
//			outLog += "\n Got files in dir ==> \n" ;
//			
//			for (File f : files) {
//				
//				outLog += "Filename[" 
//					+ f.getName()
//					+ "]\t size[" 
//					+ f.length() + " bytes"
//					+ "]\n" 
//				;
//				
//			}		
//			Log.i(logHead, outLog);	
//			
//		} else {			
//			Log.w(logHead, "No Root Files list found ...");
//		}
		
		
		
//		String logOut = "Try to write files ==> ";
//		try {
//			
//			FileOutputStream fOut = provider.getOutStream("example-file-out.txt", Sync.MODE_WORLD_READABLE);	
//			
//			File f = new File("example-file-out.txt");
//			
//			Log.i(logHead, "Absolute file path = " + f.getAbsolutePath());
//			
////			fOut.;
//			
//			provider.write("example-request.txt", MODE_PRIVATE, (addExampleRequest()).getBytes());
//			logOut +=  "\t Request OK";
//			provider.write("example-response.txt", MODE_PRIVATE, (addExampleResponse()).getBytes());
//			logOut +=  "\t Response OK";
//			
//			int data = provider.read("example-request.txt");
//			logOut += "\n Just read request-file ==> [... \n" ;
//			logOut += data ;
//			logOut += "\n ...]" ;
//			
//			data = provider.read("example-response.txt");
//			logOut += "\n Just read response-file ==> [... \n" ;
//			logOut += data ;
//			logOut += "\n ...]" ;
//			
//		} catch (IOException e) {
//			logOut += "\t Could not write file because [" + e.getMessage() +"]" ;
////			e.printStackTrace();
//		} finally {			
//			Log.i(logHead, logOut);
//		}
//				
	}

	public void writeUserGetterResponseContent (SyncGetterResponse resp /*String content*/) {
//		String out = null ;
		try {
//			out =
			String content = GsonConverter.toJson(resp);
			writeFileInUserFolder(SYNC_GETTER_RESP_FILENAME, content);
		} catch (Exception e) {
			Log.e(logHead, "Count not write Getter Request Content : " + e.getMessage());
		}		
//		return out ;	
		
	}

	public void writeUserSynchronizedLists (SyncHistory history) {
//		String out = null ;
		try {
//			out =
			String content = GsonConverter.toJson(history);
			writeFileInUserFolder(SYNC_HISTORY_FILENAME, content);
		} catch (Exception e) {
			Log.e(logHead, "Count not write Sync History Content : " + e.getMessage());
		}		
//		return out ;	
		
	}	
}
