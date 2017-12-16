package GW;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class GW_UDP_Transmitter {
	GW_UDP_Transmitter(String ip, int port) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.print("전송할 데이터 = ");
		String data = sc.next(); // 데이터의 입력을 받음.

		InetAddress ia = InetAddress.getByName(ip); // 주소에 대한 객체를
													// 만듬.
		DatagramPacket dp = new DatagramPacket(data.getBytes(), data.getBytes().length, ia, port);
		DatagramSocket soc = new DatagramSocket(); // 어디서 보냈는지는 중요하지 않음.
		soc.send(dp); // 소켓에 만든 데이터를 보냄.
		soc.close(); // 연결을 끊음.

		System.out.println("전송끝...");
	}
}

class GW_UDP_reader {
	GW_UDP_reader(int port) throws IOException {
		byte[] by = new byte[65508]; // 소포를 받을 빈공간을 만듬.
		DatagramPacket dp = new DatagramPacket(by, by.length); // 빈공간을 만듬.
		DatagramSocket soc = new DatagramSocket(port); // 받을 우편함을 설정
		System.out.println("Server Ready...");
		soc.receive(dp); // 빈소포에 낸 내용을 받음.
		soc.close(); // 연결을 끊음.

		System.out.println("보낸이 = " + dp.getAddress().getHostAddress());
		System.out.println("내용물 = " + new String(dp.getData()).trim());
	}
}



class ArpMacAddressGet {

	int ARP_Count = 0, Search_Count = 0, Divide_Count = 10, IP_Count = 0, MAC_Count = 0;
	String[] cmd = { "cmd", "/c", "arp", "-a" };
	String[] ARP_Table, IP, MAC, IP_range, Host_Name, My_IP;
	Process process = null;

	ArpMacAddressGet() {
		
		
		try{
			
			InetAddress local = InetAddress.getLocalHost();
			
			My_IP = local.getHostAddress().split("\\.");
			
			for (int pingTest_ip = 1; pingTest_ip <= 255; pingTest_ip++) {
				PingTest pt = new PingTest(My_IP[0]+"."+My_IP[1]+"."+My_IP[2]+"." + pingTest_ip);
				pt.start();
			}
			

			ARP_Table = new String[5000];
			IP_range = new String[255];
			Host_Name = new String[255];

			for (int p = 0; p <= 4999; p++) {
				ARP_Table[p] = "";
			}
			
		} catch (Exception e1) {
			System.out.println("IP 검색 에러");
		}

			try{
		
			process = new ProcessBuilder(cmd).start();

			SequenceInputStream seqIn = new SequenceInputStream(process.getInputStream(), process.getErrorStream());

			Scanner s = new Scanner(seqIn);

			while (s.hasNext() == true) {

				ARP_Table[ARP_Count] = s.next();

				if (ARP_Count >= 499) {
					break;
				}

				ARP_Count++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

			
		while (Search_Count <= 499) {
			if (ARP_Table[Search_Count].equals("ff-ff-ff-ff-ff-ff")) {
				break;
			}

			Search_Count++;
		}

		MAC = new String[(Search_Count / 3)];
		IP = new String[(Search_Count / 3)];

		while (Divide_Count <= Search_Count-2) {
			
			if (Pattern.matches("^[0-9.]+$", ARP_Table[Divide_Count])) {
				
				String[] compare = ARP_Table[Divide_Count].split("\\.");;
				
				if( !((My_IP[0]+"."+My_IP[1]+"."+My_IP[2]).equals(compare[0]+"."+compare[1]+"."+compare[2])) ) {
					break;
				}
				
				IP[IP_Count] = ARP_Table[Divide_Count];
				
				try {
					
					InetAddress ia =  InetAddress.getByName(IP[IP_Count]);
					Host_Name[IP_Count] = getHostName(ia);
					String hostname = System.getenv().get("HOSTNAME");
					System.out.println(hostname);
					
				} catch( Exception host) {
					System.out.println("호스트 네임 오류");
				}
				
				IP_Count++;
			} else if (Pattern.matches("^[0-9a-z-]+$", ARP_Table[Divide_Count])) {
				MAC[MAC_Count] = ARP_Table[Divide_Count];
				MAC_Count++;
			}
			Divide_Count++;
		}
		
	}
	
	String getHostName(InetAddress inaHost) throws UnknownHostException {
		try {
			Class clazz = Class.forName("java.net.InetAddress");
			Constructor[] constructors = clazz.getDeclaredConstructors();
			constructors[0].setAccessible(true);
			InetAddress ina = (InetAddress) constructors[0].newInstance();

			Field[] fields = ina.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals("nameService")) {
					field.setAccessible(true);
					Method[] methods = field.get(null).getClass().getDeclaredMethods();
					for (Method method : methods) {
						if (method.getName().equals("getHostByAddr")) {
							method.setAccessible(true);
						
							return (String) method.invoke(field.get(null), inaHost.getAddress());
							
						}
					}
				}
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("1");
		} catch (IllegalAccessException iae) {
			System.out.println("2");
		} catch (InstantiationException ie) {
			System.out.println("3");
		} catch (InvocationTargetException ite) {
			throw (UnknownHostException) ite.getCause();
		}
		return null;
	}
	
}

class PingTest extends Thread {

	String IP = "";

	public PingTest(String IP) {
		this.IP = IP;
	}

	@Override
	public void run() {
		try {
			InetAddress tagetIp = InetAddress.getByName(IP);
			boolean reachable = tagetIp.isReachable(2000);

			if (reachable) {
				 //System.out.println(IP + " - O");
			} else {
				 //System.out.println(IP + " - X");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class TCP_IP_Client {

	String ReceiveData;

	Socket socket = null;

	void TCP_IP_Client(String IP, int Port) {

		try {
			// 서버 연결

			socket = new Socket(IP, Port);
			System.out.println("서버에 연결되었습니다.");

			// DataSender ds = new DataSender(socket, Data);// 데이터 전송 클래스.
			// DataReceiver dr = new DataReceiver(socket);// 데이터 수신 클래스.

			// ds.start(); // 전송 스레드 시작
			// this.ReceiveData=dr.Data;

		} catch (IOException e) {
			System.out.println("연결 실패");
		}

	}
}

class DataReceiver extends Thread {
	Socket socket;
	DataInputStream dis;
	String Data;

	public DataReceiver(Socket socket) {
		this.socket = socket;
	}

	int s = 0;

	@Override
	public void run() {
		try {

			System.out.println("수신 작업을 시작합니다.");
			dis = new DataInputStream(socket.getInputStream());

			byte[] c = new byte[1024];

			System.out.print("데이터(");

			while ((c[s] = dis.readByte()) != 13) {
				s++;
			}

			Data = Arrays.toString(c);

			System.out.print(")을" + " 받았습니다.");

		} catch (IOException e) {
			System.out.println("받기 실패");
		}
	}
}





class DataSender extends Thread {
	Socket socket;
	DataOutputStream dos;

	BufferedInputStream bis;
	byte[] Data = new byte[23];

	
	boolean Sender_OK = false;

	public DataSender(Socket socket, byte[] Data) {

		this.socket = socket;
		this.Data = Data;

		
		try {
			// 데이터 전송용 스트림 생성
			dos = new DataOutputStream(socket.getOutputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("데이터를 전송합니다.");

			dos.write(Data);
			System.out.printf("데이터(%s)을 전송하였습니다.\n", Data);
			Sender_OK = true;

		} catch (IOException e) {
			System.out.println("보내기 실패");
		}
	}

}











class Inet_aton_ntoa{

	public static String inet_ntoa(long add) {  
	    return ((add & 0xff000000) >> 24) + "." + ((add & 0xff0000) >> 16)  
	            + "." + ((add & 0xff00) >> 8) + "." + ((add & 0xff));  
	} 
	
	public static long inet_aton(String add) {  
	    long result = 0;  
	    // number between a dot  
	    long section = 0;  
	    // which digit in a number  
	    int times = 1;  
	    // which section  
	    int dots = 0;  
	    for (int i = add.length() - 1; i >= 0; --i) {  
	        if (add.charAt(i) == '.') {  
	            times = 1;  
	            section <<= dots * 8;  
	            result += section;  
	            section = 0;  
	            ++dots;  
	        } else {  
	            section += (add.charAt(i) - '0') * times;  
	            times *= 10;  
	        }  
	    }  
	    section <<= dots * 8;  
	    result += section;  
	    return result;  
	}  

}