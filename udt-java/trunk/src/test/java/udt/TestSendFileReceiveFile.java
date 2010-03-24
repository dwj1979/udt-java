package udt;

import java.io.File;
import java.io.FileInputStream;

import udt.util.ReceiveFile;
import udt.util.SendFile;
import udt.util.UDTThreadFactory;

public class TestSendFileReceiveFile extends UDTTestBase{

	volatile boolean serverStarted=false;
	
	public void test1()throws Exception{
		runServer();
		while(!serverStarted)Thread.sleep(100);
		File f=new File("src/test/java/datafile");
		File tmp=File.createTempFile("udtest-", null);
		
		String[] args=new String[]{"localhost","65321",f.getAbsolutePath(),tmp.getAbsolutePath()};
		ReceiveFile.main(args);
		//check temp data file
		String md5_sent=readAll(new FileInputStream(f),4096);
		String md5_received=readAll(new FileInputStream(tmp),4096);
		assertEquals(md5_sent, md5_received);
	}
	
	private void runServer(){
		Runnable r=new Runnable(){
			public void run(){
				serverStarted=true;
				String []args=new String[]{"65321"};
				try{
					SendFile.main(args);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		};
		Thread t=UDTThreadFactory.get().newThread(r);
		t.start();
	}
}