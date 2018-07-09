import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient1 implements Runnable{
	private ServerSocket myserversocket;
	public MyClient1(int port) throws IOException{
		myserversocket = new ServerSocket(port);
		myserversocket.setSoTimeout(5000);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				//�����Լ��Ķ˿��Ƿ�������
				Socket server =  myserversocket.accept();
				DataInputStream data_in = new DataInputStream(server.getInputStream());
				byte buffer[] = new byte[1024];
				int length = data_in.read(buffer);
				String getString = new String(buffer,0,length);
				System.out.println("A have received a file request!");
				//�������������
				DataOutputStream data_out = new DataOutputStream(server.getOutputStream());
				FileInputStream file_in = new FileInputStream(new File(getString));
				byte b[] = new byte[1024];
				file_in.read(b);
				data_out.write(b);
				data_out.close();
				server.close();
			} catch (IOException e) {

			}
		}
	}
	public static void main(String[] args) throws UnknownHostException, IOException{
		//��Ŀ¼��������ѯĿ���ļ����ڵ��ն˵�ַ
		Socket socket = new Socket("127.0.0.1", 8889);
		DataOutputStream data_out = new DataOutputStream(socket.getOutputStream());
		String key = "c.txt";
		data_out.write(key.getBytes());

		//�����ն˵�ַ
		InputStream input = socket.getInputStream();
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		String getString;
		getString=reader.readLine();
		input.close();
		System.out.println(getString);
		socket.close();
	    
		//�����ն˵�ַ�����ļ�		
		socket  = new Socket(getString, 9000);
	    OutputStream output = socket.getOutputStream();
		output.write(key.getBytes());
		input = socket.getInputStream();
		FileOutputStream file_out = new FileOutputStream(new File(key));
		int temp = 0;
		while((temp=input.read())!=-1){
			file_out.write(temp);
		}
		file_out.close();
		output.close();
		socket.close();
		
	}
}
