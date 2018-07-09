import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TcpServer implements Runnable{
	private ServerSocket myserversocket;
	private HashMap<String,String> hostm;
	private HashMap<String,String> MyTxt;
	public TcpServer(int port) throws IOException{
		myserversocket = new ServerSocket(port);
		myserversocket.setSoTimeout(5000);
		//记录文件所在的机器机器IP地址
		hostm = new HashMap<String, String>();
		MyTxt = new HashMap<String, String>();
		hostm.put("a.txt", "127.0.0.1");
		hostm.put("b.txt", "127.0.0.1");
		hostm.put("c.txt", "127.0.0.1");
		hostm.put("d.txt", "127.0.0.1");
		MyTxt.put("a.txt", "A");
		MyTxt.put("b.txt", "A");
		MyTxt.put("c.txt", "B");
		MyTxt.put("d.txt", "B");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				//监听端口
				Socket server =  myserversocket.accept();
				DataInputStream data_in = new DataInputStream(server.getInputStream());
				byte buffer[] = new byte[1024];
				
				int length = data_in.read(buffer);
				String key = new String(buffer,0,length);
				//查看要求的文件是否在本网段的终端上
				if(hostm.containsKey(key)){
					System.out.println(MyTxt.get(key)+" have received a request!");
					DataOutputStream data_out = new DataOutputStream(server.getOutputStream());
					System.out.println(key);
					data_out.write(hostm.get(key).getBytes());
					data_out.close();
				}
				else{
					DataOutputStream data_out = new DataOutputStream(server.getOutputStream());
					String str = "There is no this file here!";
					System.out.println(key);
					data_out.write(str.getBytes());
					data_out.close();
				}
			} catch (IOException e) {

			}
		}
	}
	public static void main(String[] args) throws IOException{
		TcpServer tcp = new TcpServer(8889);
		MyClient1 client1 = new MyClient1(9001);
		MyClient2 client2 = new MyClient2(9000);
		new Thread(tcp).start();
		new Thread(client2).start();
		new Thread(client1).start();		
	}
}
