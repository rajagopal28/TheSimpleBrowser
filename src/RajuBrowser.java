import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;
import javax.microedition.io.Connector;
import javax.microedition.rms.*;
import javax.microedition.midlet.MIDlet;


public class RajuBrowser extends MIDlet implements CommandListener, ItemStateListener, ItemCommandListener
{
	private Display display;
	private TextField addressBox;
	private Form mainForm,form,bookForm;
	private Alert alert;
	private Command exitCommand,goCommand,showCommand,bookCommand,deleteCommand,backCommand;
	private ChoiceGroup choice,bookmarks,savedmarks;
	private RecordStore bookstore=null,recordstore = null;
	Image fb0,fb,goog,yaho,wiki,youtub,defimg,img1,img2;

	public RajuBrowser()
	{
		display = Display.getDisplay(this);
		try
		{
		 fb0=Image.createImage("/facebook0.png");
		 fb=Image.createImage("/facebook.png");
		 goog=Image.createImage("/google.png");
		 yaho=Image.createImage("/yahoo.png");
		 wiki=Image.createImage("/wiki.png");
		 youtub=Image.createImage("/youtube.png");
		 defimg=Image.createImage("/default.png");
		 img1=Image.createImage("/image1.png");
		 img2=Image.createImage("/image2.png");
		}
		catch(Exception error)
		{
		 alert = new Alert("Error Creating",error.toString(), null, AlertType.WARNING);
		 alert.setTimeout(Alert.FOREVER);
		 display.setCurrent(alert);			
		}


		choice=new ChoiceGroup("",Choice.MULTIPLE);
		choice.append("Save History?",img1);
		choice.append("Bookmark This Site?",img2);

		bookmarks=new ChoiceGroup("Bookmarks",Choice.EXCLUSIVE);
		bookmarks.append("0.FaceBook",fb0);
		bookmarks.append("FaceBook",fb);
		bookmarks.append("Google",goog);
		bookmarks.append("Yahoo!!",yaho);
		bookmarks.append("Wikipedia",wiki);
		bookmarks.append("Youtube",youtub);

		exitCommand=new Command("Quit",Command.EXIT,2);
		backCommand=new Command("Back",Command.EXIT,2);
		goCommand=new Command("Go",Command.SCREEN,1);
		showCommand = new Command("Show History", Command.SCREEN, 1);
		bookCommand=new Command("Show Bookmarks",Command.SCREEN,1);
		deleteCommand = new Command("Delete History", Command.SCREEN, 1);

		addressBox=new TextField("URL::","http://",100,TextField.ANY);
		mainForm=new Form("Browser Window");
		mainForm.append(addressBox);
		mainForm.append(choice);
		mainForm.append(bookmarks);
		mainForm.addCommand(exitCommand);
		mainForm.addCommand(goCommand);
		mainForm.addCommand(showCommand);
		mainForm.addCommand(bookCommand);
		mainForm.setCommandListener(this);
		mainForm.setItemStateListener(this);
	


		display.setCurrent(mainForm);

	}
	private void deleteAllHistory()
	{
		if (RecordStore.listRecordStores() != null)
		{
			try
			{
				RecordStore.deleteRecordStore("myHistoryStore");
			}
			catch (Exception error)
			{
				alert = new Alert("Error Removing", error.toString(), null, AlertType.WARNING);
				alert.setTimeout(Alert.FOREVER);
				display.setCurrent(alert);
			}
		}
	}
	private void addHistory(String url)
	{
		try
		{
			recordstore = RecordStore.openRecordStore("myHistoryStore", true);
		}
		catch (Exception error)
		{
			alert = new Alert("Error Creating", error.toString(), null, AlertType.WARNING);
			alert.setTimeout(Alert.FOREVER);
			display.setCurrent(alert);
		}
		try
		{
			String outputData = url;
			byte[] byteOutputData = outputData.getBytes();
			recordstore.addRecord(byteOutputData, 0, byteOutputData.length);
		}
		catch (Exception error)
		{
			alert = new Alert("Error Writing", error.toString(), null, AlertType.WARNING);
			alert.setTimeout(Alert.FOREVER);
			display.setCurrent(alert);
		}

		try
		{
			recordstore.closeRecordStore();
		}
		catch (Exception error)
		{
			alert = new Alert("Error Closing", error.toString(), null, AlertType.WARNING);
			alert.setTimeout(Alert.FOREVER);
			display.setCurrent(alert);
		}
	}
	private void initHistoryForm()
	{
		form = new Form("History");
		form.addCommand(backCommand);
		form.addCommand(deleteCommand);
		form.setCommandListener(this);
	}
	private void initBookForm()
	{
		bookForm = new Form("Saved Bookmarks");
		bookForm.addCommand(backCommand);
		bookForm.addCommand(deleteCommand);
		bookForm.setCommandListener(this);
		bookForm.setItemStateListener(this);
	}

	private void downloadPage(String url) throws IOException
	{
		
		try
		{
			if (this.platformRequest(url) == true)
			{
			 // MIDlet must Exit before the browser can
			 // launch
			 this.notifyDestroyed();
			}
		}

		catch(IOException e)
		{
		   // handle the exception
		 System.out.println("Error::"+e.toString());
		}
		finally
		{
		}

	}


	/**
	 * This will be invoked when we activate the MIDlet.
	 */
	public void startApp()
	{
		// Use the specified URL is overriden in the descriptor
	}

	public void commandAction(Command c, Displayable s)
	{
		if(c==exitCommand)
		{
		 destroyApp(false);
	            	 notifyDestroyed();
		}
		if(s==form)
		{
		 if(c==backCommand)
		 {
			display.setCurrent(mainForm);
		 }
		 if(c==deleteCommand)
		 {
			System.out.println("Deleteing All Histories");
			deleteAllHistory();
		 }
		}
		if(s==bookForm && c==deleteCommand)
		{
			System.out.println("Deleteing All BookMarks");

		 	if (RecordStore.listRecordStores() != null)
			{
			 try
			 {
			  RecordStore.deleteRecordStore("myBookMarks");
			 }
			 catch (Exception error)
			 {
			  alert = new Alert("Error Removing", error.toString(),null, AlertType.WARNING);
			  alert.setTimeout(Alert.FOREVER);
			  display.setCurrent(alert);
			 }
		 	}


		}
		if(s==bookForm && c==backCommand)
		{
			display.setCurrent(mainForm);
		}
		if(c==bookCommand)
		{
			initBookForm();
			display.setCurrent(bookForm);
			try
			{
				bookstore = RecordStore.openRecordStore("myBookMarks", true);
			}
			catch (Exception error)
			{
				alert = new Alert("Error Creating", error.toString(), null, AlertType.WARNING);
				alert.setTimeout(Alert.FOREVER);
				display.setCurrent(alert);
			}
			System.out.println("IN here");
		 try
		 {
			byte[] byteInputData = new byte[1];
			int length = 0;
			savedmarks=new ChoiceGroup("Saved BookMarks",Choice.EXCLUSIVE);
			String list= new String();
			initBookForm();
			display.setCurrent(bookForm);
			for (int x = 1; x <= bookstore.getNumRecords(); x++)
			{
			  byteInputData = new byte[bookstore.getRecordSize(x)];
   		  	  length = bookstore.getRecord(x, byteInputData, 0);
			  list=new String(byteInputData,0,length);
			  savedmarks.append(list,defimg);
			  //bookForm.append(list[x-1] + "\n"); 
			}
			bookForm.append(savedmarks);
		}
		catch (Exception error)
		{
		 alert = new Alert("Error Reading", error.toString(),null, AlertType.WARNING);
		 alert.setTimeout(Alert.FOREVER);
		 display.setCurrent(alert);
		}
			//closeStore();
		try
		{
			bookstore.closeRecordStore();
		}
		catch (Exception error)
		{
			alert = new Alert("Error Closing", error.toString(), null, AlertType.WARNING);
			alert.setTimeout(Alert.FOREVER);
			display.setCurrent(alert);
		}

		}
		if(c==showCommand)
		{
			try
			{
				recordstore = RecordStore.openRecordStore("myHistoryStore", true);
			}
			catch (Exception error)
			{
				alert = new Alert("Error Creating", error.toString(), null, AlertType.WARNING);
				alert.setTimeout(Alert.FOREVER);
				display.setCurrent(alert);
			}
			try
			{
				byte[] byteInputData = new byte[1];
				int length = 0;

				String list[] = new String[recordstore.getNumRecords()];
				initHistoryForm();
				display.setCurrent(form);
				for (int x = 1; x <= recordstore.getNumRecords(); x++)
				{
					byteInputData = new byte[recordstore.getRecordSize(x)];
					length = recordstore.getRecord(x, byteInputData, 0);
					list[x - 1] = new String(byteInputData, 0, length);
					form.append(list[x - 1] + "\n");
				}
			}
			catch (Exception error)
			{
				alert = new Alert("Error Reading", error.toString(), null, AlertType.WARNING);
				alert.setTimeout(Alert.FOREVER);
				display.setCurrent(alert);
			}
			try
			{
				recordstore.closeRecordStore();
			}
			catch (Exception error)
			{
				alert = new Alert("Error Closing", error.toString(), null, AlertType.WARNING);
				alert.setTimeout(Alert.FOREVER);
				display.setCurrent(alert);
			}
	    }
		if(c==goCommand)
		{
			if(addressBox.getString().equalsIgnoreCase("http://"))
			{
			System.out.println("No Address Entered");
			 alert = new Alert("Error Surfing","URL Not Entered", null, AlertType.WARNING);
			 alert.setTimeout(Alert.FOREVER);
			 display.setCurrent(alert);
			
			  }
			else
			{
			 try
			{
			 boolean pick[]=new boolean[choice.size()];
	   		 choice.getSelectedFlags(pick);
	   		 if(pick[0])
	   		 {
				//save The Current Address
				 addHistory(addressBox.getString());
	   		 }
			 if(pick[1])
			{
			  System.out.println("Bookmark Selected");
				//Bookmark The Current Address
			  try
			  {
				  bookstore = RecordStore.openRecordStore("myBookMarks", true);
			  }
			  catch (Exception error)
			  {
				  alert = new Alert("Error Creating", error.toString(), null, AlertType.WARNING);
				  alert.setTimeout(Alert.FOREVER);
				  display.setCurrent(alert);
			  }
				try
				{
				  String outputData = addressBox.getString();
				 byte[] byteOutputData = outputData.getBytes();
				 bookstore.addRecord(byteOutputData,0,byteOutputData.length);
 				}
				catch (Exception error)
				{
				 alert = new Alert("Error Writing",error.toString(), null, AlertType.WARNING);
				 alert.setTimeout(Alert.FOREVER);
				 display.setCurrent(alert);
				}

				try
				{
					bookstore.closeRecordStore();
				}
				catch (Exception error)
				{
					alert = new Alert("Error Closing", error.toString(), null, AlertType.WARNING);
					alert.setTimeout(Alert.FOREVER);
					display.setCurrent(alert);
				}

			}
			 downloadPage(addressBox.getString());
			 addressBox.setString("http://");
			}

			catch(IOException e)
			{
		   	 // handle the exception
		 	 System.out.println("Error::"+e.toString());
			}
			}

		}
	}
    public void itemStateChanged(Item i) {
	if (i == choice) 
	{
	   boolean pick[]=new boolean[choice.size()];
	   choice.getSelectedFlags(pick);
	   if(pick[0])
	   {
		System.out.println("History Selected");
		
	   }
	   if (pick[1])
	   {
		   System.out.println("Bookmarks Selected");
	   }
		
	}
	if(i==savedmarks)
	{
	 String addr=savedmarks.getString(savedmarks.getSelectedIndex());
	 if(addr.length()!=0 && !(addr.equalsIgnoreCase("http://")))
	 {
	
		  boolean pick[] = new boolean[choice.size()];
		  choice.getSelectedFlags(pick);
		  if (pick[0])
		  {
			  //save The Current Address
			  addHistory(addr);
		  }
 	try{

		downloadPage(addr);
	  }
	catch(Exception e)
	{
		System.out.println("Error reading:"+e.toString());
	}
	 }
	}
	if(i == bookmarks)
	{
	 String addr,gotoption;
	 addr=new String();
	 gotoption=bookmarks.getString(bookmarks.getSelectedIndex());
	  if(gotoption.equals("0.FaceBook"))
	  {
		addr="http://0.facebook.com";
	  }
	  if(gotoption.equals("FaceBook"))
	  {
		addr="http://www.facebook.com";
	  }
	  if(gotoption.equals("Google"))
	  {
		addr="http://www.google.com";
	  }
	  if(gotoption.equals("Yahoo!!"))
	  {
		addr="http://www.yahoo.com";
	  }
	  if(gotoption.equals("Wikipedia"))
	  {
		addr="http://en.wikipedia.org";
	  }
	  if(gotoption.equals("Youtube"))
	  {
		addr="http://www.youtube.com";
	  }
	 try
	  {
		  boolean pick[] = new boolean[choice.size()];
		  choice.getSelectedFlags(pick);
		  if (pick[0])
		  {
			  //save The Current Address
			  addHistory(addr);
		  }
 	   downloadPage(addr);
	  }
	 catch (Exception error)
	 {
	  alert = new Alert("Error Reading", error.toString(),null, AlertType.WARNING);
	  alert.setTimeout(Alert.FOREVER);
	  display.setCurrent(alert);
	 }
	}
	
    }

     public void commandAction(Command c, Item i) 
     {

     }

	/**
	 * Pause, discontinue....
	 */
	public void pauseApp()
	{
	}

	/**
	 * Destroy must cleanup everything.
	 */
	public void destroyApp(boolean unconditional)
	{
	}
}