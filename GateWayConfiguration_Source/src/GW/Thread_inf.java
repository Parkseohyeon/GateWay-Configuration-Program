package GW;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;

import GW.Thread_admin.Thread_Conut;

class Thread_admin { // 데이터 전송 스레드 정보

	Thread_Conut thread_Count = new Thread_Conut();
	Thread_Conut thread_Count2 = new Thread_Conut();
	Casting casting = new Casting();

	String Zg_msg_set = "@>"; // zg_msg 전송
	String Zg_msg_get = "@<"; // zg_msg 요청

	String RT_set = "#>"; // 라우팅 테이블 전송
	String RT_get = "#<"; // 라우팅 테이블 요청

	String RS = "Receive Success"; // 전송 성공
	String RF = "Receive Fail"; // 전송 실패

	String N = "\n";

	String Receive_Data;

	int rt_cnt = 0; // n of entries (라우팅 테이블 수)의 카운터
	int s; // 데이터 수신 카운터

	int[] Receive_Data_int_arr = new int[255]; // int array Data 보관.

	char eZBRST;
	char eUDP;
	
	String GateWay_IP;
	String Network_Mask;
	String GateWay;
	String Server_IP;
	String Port;
	String ZB_CH;
	String ZB_POWER;
	String ZB_PAN;
	String ZB_TGT;
	String ZB_BRATE;
	String ZB_LID;

	String[] entry;

	String[] Dest_ID = new String[255];
	String[] nHOPS = new String[255];
	String[][] HOP_Level = new String[255][11];
	String[] Tree = new String[255];
	
	char zg_RT;
	char Send_Read;

	DataOutputStream dos; // 데이터 전송 스트림
	DataInputStream dis; // 데이터 수신 스트림

	public void DOS_DIS(Socket socket) { // 전송, 수신 스트림 생성 함수

		try {
			// 데이터 전송용, 수신용 스트림 생성
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String Receive_Data() { // 데이터 수신용 함수

		System.out.println("\n▽▽▽▽▽▽▽▽▽▽▽▽▽Ready for receiving▽▽▽▽▽▽▽▽▽▽▽▽▽\n");

		try {

			Receive_Data_int_arr = new int[100]; // 데이터 수신 공간

			s = 0;

			while ((Receive_Data_int_arr[s] = dis.read()) != 10) // 10 == '\n'

			{

				if (((char) Receive_Data_int_arr[0] == '@') || ((char) Receive_Data_int_arr[0] == '#')) {

					System.out.print(" '" + (char)Receive_Data_int_arr[s] + "'");
					s++;

				}

			}

			char[] Receive_Data_char_arr = new char[s];

			for (int return_cnt = 0; return_cnt < s; return_cnt++) {

				Receive_Data_char_arr[return_cnt] = (char) Receive_Data_int_arr[return_cnt];

			}
			

			Receive_Data = String.valueOf(Receive_Data_char_arr);

			System.out.println(" -- " +Receive_Data + " / length = " + Receive_Data.length()); // 데이터 출력.

			System.out.println("\n△△△△△△△△△△△△△              Read Exit      △△△△△△△△△△△△△\n");
			

			return Receive_Data;

		} catch (Exception e) {
			System.out.println("Recieve_Data() - Error");

			return "Error";
		}
	}


	public int Entry_Sender(int cnt, String[] Send_Data, int time) {

		thread_Count.Set_time(time);

		try {

			int x_cnt = 0;
			System.out.println("▽▽▽▽▽▽▽▽▽▽▽▽▽   Entry Sender Start ▽▽▽▽▽▽▽▽▽▽▽▽▽\n");
			System.out.println("전송 데이터 길이 ----- " + Send_Data[x_cnt].length() + "\n");

			while (x_cnt < Send_Data.length) {

				System.out.println(x_cnt + "번째 줄  발송 -- " + String.valueOf(Send_Data[x_cnt]));
				dos.write(Send_Data[x_cnt].getBytes());
				System.out.println(x_cnt + "번째 줄  발송 완료" + "\n");
				x_cnt++;

				while (!(Receive_Data().equals("#>Receive Success"))) {

					System.out.println("waiting..... #>Receive Success");

				}


				Thread.sleep(500);

			}

			System.out.println("\n△△△△△△△△△△△△△   Entry Sendet exit △△△△△△△△△△△△△\n");

			return 0;

		} catch (Exception e) {
			System.out.println("엔트리 발신 실패");
			return -1;
		}
	}

	public int parsing(String msg, int case_int) {

		if (case_int == 0) {

			System.out.println("%%%%%% zg_msg pasing start %%%%%%\n");
			
			try {

				eZBRST = msg.charAt(2);

				System.out.println("eZBRST - " + eZBRST);
				
				eUDP = msg.charAt(3);
				
				System.out.println("eUDP - " + eUDP);

				GateWay_IP = msg.substring(4, 7) + "." + msg.substring(7, 10) + "." + msg.substring(10, 13) + "."
						+ msg.substring(13, 16);
				
				System.out.println("GateWay_IP - " + GateWay_IP);

				Network_Mask = msg.substring(16, 19) + "." + msg.substring(19, 22) + "." + msg.substring(22, 25) + "."
						+ msg.substring(25, 28);

				System.out.println("Network_Mask - " + Network_Mask);
				
				GateWay = msg.substring(28, 31) + "." + msg.substring(31, 34) + "." + msg.substring(34, 37) + "."
						+ msg.substring(37, 40);

				System.out.println("GateWay - " + GateWay);
				
				Server_IP = msg.substring(40, 43) + "." + msg.substring(43, 46) + "." + msg.substring(46, 49) + "."
						+ msg.substring(49, 52);
				
				System.out.println("Server_IP - " + Server_IP);

				Port = msg.substring(52, 56);
				
				System.out.println("Port - " + Port);

				ZB_CH = msg.substring(56, 58);
				
				System.out.println("ZB_CH - " + ZB_CH);

				ZB_POWER = msg.substring(58, 60);
				
				System.out.println("ZB_POWER - " + ZB_POWER);

				ZB_PAN = msg.substring(60, 64);
				
				System.out.println("ZB_PAN - " + ZB_PAN);

				ZB_TGT = msg.substring(64, 68);
				
				System.out.println("ZB_TGT - " + ZB_TGT);

				ZB_BRATE = msg.substring(68, 74);

				System.out.println("ZB_BRATE - " + ZB_BRATE);
				
				ZB_LID = msg.substring(74, 78);
				
				System.out.println("ZB_LID - " + ZB_LID);

				System.out.println("\n%%%%%% zg_msg pasing success %%%%%%\n");
				
				return 0;

			} catch (Exception e) {
				System.out.println("\n%%%%%% zg_msg 분류에  실패하였습니다. %%%%%%\n");

				return -1;
			}
		} else if (case_int == 1) {
			
			System.out.println("%%%%%% rt_msg pasing start %%%%%%\n");
			
			try {

				
				Dest_ID[rt_cnt] = msg.substring(2 , 4);
				Dest_ID[rt_cnt] = String.valueOf(String.format("%04d",Integer.parseInt(Dest_ID[rt_cnt],16)));
				
				System.out.println("%%% Dest_ID set - " + Dest_ID[rt_cnt]);
				
				nHOPS[rt_cnt] = String.valueOf(Character.getNumericValue(msg.charAt(4)));
				Tree[rt_cnt] = String.valueOf(Character.getNumericValue(msg.charAt(4))+1);
				
				System.out.println("%%% nHOPS set - " + nHOPS[rt_cnt]);
				
				for ( int hop = 0 ; hop < Character.getNumericValue(msg.charAt(4)); hop++ ) {
					
					HOP_Level[rt_cnt][hop] =  msg.substring( (2*hop)+3 , (2*hop)+5 );
					HOP_Level[rt_cnt][hop] = String.valueOf(String.format("%04d",Integer.parseInt(HOP_Level[rt_cnt][hop],16)));
					
					System.out.println("%%% HOP_Level set - " + HOP_Level[rt_cnt][hop]);
				}
				

				System.out.println("\n%%%%%% rt_msg pasing success %%%%%%\n");
				
				return 0;

			} catch (Exception e) {
				System.out.println("%%%%%% rt_msg 분류에  실패하였습니다. %%%%%%\n");

				return -1;
			}

		}
		else {
			
			return -1;
		
		}
	}

	class Thread_Conut extends Thread {

		int s, Count;

		boolean Time_out;

		void Set_time(int s) {
			this.s = s;
		}

		@Override
		public void run() {
			try {

				Time_out = false;

				System.out.println("---타이머 시작.---");

				Count = 0;

				while (Count == s) {

					Thread.sleep(1000);

					Count++;

					System.out.println("--------- " + Count);

				}

				System.out.println("---타이머 종료.---");

				Time_out = true;

			} catch (Exception e) {
				System.out.println("time_out Error\n");
			}
		}
	}

}

/*--------------------------------------------Save of Basic_Set------------------------------------------------------------*/
/*
 * Save of Basic_Set (기본 설정의 저장) zg_msg -
 * '@'(1B)|'>'(1B)|eUDP(1B)|IP(4B)|SN(4B)|GW(4B)|SVR_IP(4B)|SVR_PORT(2B)| ZB_CH(
 * 1B)|ZB_TXPWR(1B)
 * 
 */
class zg_msg_Save extends Thread {

	Thread_admin Thread_admin = new Thread_admin(); // 스레드 정보 객체 생성

	byte[] Send_Data; // zg_msg 크기

	boolean Save; // 정보 업로드의 시작을 위한 플래그

	public zg_msg_Save(Socket socket, String Data) {

		this.Send_Data = Data.getBytes(); // msg 할당

		Thread_admin.DOS_DIS(socket); // 데이터 스트림 생성
	}

	@Override
	public void run() {
		try {

			Save = false; // Save reset

			System.out.println("\n▼▼▼▼▼▼▼▼▼▼▼▼▼   zg_msg Save - Start ▼▼▼▼▼▼▼▼▼▼▼▼▼\n");// zg_msg
																	// 데이터를
																	// 전송 시작
																	// 표시.

			Thread_admin.dos.write((Thread_admin.Zg_msg_set + Thread_admin.N).getBytes()); // Sending
																							// "@>"
			System.out.println(" --- send @>");

			while (Thread_admin.Receive_Data().equals("#@>OK")) {

				Thread.sleep(500);

				Thread_admin.dos.write((Thread_admin.Zg_msg_set + Thread_admin.N).getBytes());

			}
			; // Waiting "@>OK"

			Thread_admin.dos.write(Send_Data); // zg_msg 전송
			System.out.println("Send _ zg_msg.......");

			while (!(Thread_admin.Receive_Data().equals("@>Receive Success"))) { // Waiting
																					// "@>Receive
																					// Success"

				Thread.sleep(500);

				Thread_admin.dos.write(Send_Data); // zg_msg 재전송.

			}
			;

			System.out.printf("▲▲▲▲▲▲▲▲▲▲▲▲▲   Zg_msg Save Success ▲▲▲▲▲▲▲▲▲▲▲▲▲\n\n"); // zg_msg 데이터 전송 완료
														// 표시.

			Save = true; // Save enable.

		} catch (Exception e) {
			System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲   Zg_msg Save Fail ▲▲▲▲▲▲▲▲▲▲▲▲▲\n\n");
		}
	}
}

/*--------------------------------------------Read of Basic_Set------------------------------------------------------------*/
/*
 * Read of Basic_Set (기본 설정의 읽기) zg_msg -
 * '@'(1B)|'<'(1B)|eUDP(1B)|IP(4B)|SN(4B)|GW(4B)|SVR_IP(4B)|SVR_PORT(2B)| ZB_CH(
 * 1B)|ZB_TXPWR(1B)
 * 
 */
class zg_msg_Read extends Thread {

	Thread_admin Thread_admin = new Thread_admin(); // 스레드 정보 객체 생성

	boolean Read; // 정보 업로드의 시작을 위한 플래그

	int time_out;

	public zg_msg_Read(Socket socket) {

		Thread_admin.DOS_DIS(socket); // 데이터 스트림 생성

		Thread_admin.thread_Count.Set_time(3);

	}

	@Override
	public void run() {
		try {

			Read = false; // Read reset

			System.out.println("\n▼▼▼▼▼▼▼▼▼▼▼▼▼  Zg_msg Read - Start ▼▼▼▼▼▼▼▼▼▼▼▼▼\n");// zg_msg 데이터를 수신 시작
														// 표시.

			Thread_admin.dos.write((Thread_admin.Zg_msg_get + Thread_admin.N).getBytes());// Sending
																							// "@<"
			System.out.println(" --- send @<\n");

			while (Thread_admin.parsing(Thread_admin.Receive_Data(), 0) != 0) { // 받은
																				// zg_msg의
																				// 분류가
																				// 성공할때
																				// 까지
																				// 반복.

				// Add Counter - If(time_out = true) -- Thread Stop();

				Thread.sleep(500);

				Thread_admin.dos.write((Thread_admin.Zg_msg_get + Thread_admin.RF + '\0').getBytes()); // @<Receive
																										// Fail
																										// 전송.

			}

			Thread_admin.dos.write((Thread_admin.Zg_msg_get + Thread_admin.RS + '\0').getBytes()); // @<Receive
																									// Success
																									// 전송.

			Read = true; // Read enable

			System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲   Zg_msg Read Success ▲▲▲▲▲▲▲▲▲▲▲▲▲\n\n"); // zg_msg 데이터 수신 완료
														// 표시.

		} catch (Exception e) {
			System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲    Zg_msg Read Fail   ▲▲▲▲▲▲▲▲▲▲▲▲▲\n\n");
		}
	}
}

/*--------------------------------------------Save of Routing------------------------------------------------------------*/
/*
 * Save of Routing (라우팅의 저장) RT - '#'(1B)|'>'(1B)|'add' or 'del'|DEST...
 * 
 */
class RT_Save extends Thread {

	Thread_admin Thread_admin = new Thread_admin(); // 스레드 정보 객체 생성
	Casting casting = new Casting();

	String[] Send_Data;

	String RT_set_add_del;

	int cnt = 0;

	boolean RT_Save;

	public RT_Save(Socket socket, int n_add, int n_del, String[] Data) {

		String add = String.format("%02X",n_add);
		String del = String.format("%02X",n_del);
		
		
		RT_set_add_del = "#>A" + add + "D" +  del + "M00" + "\n" ;

		cnt = n_add + n_del;

		Send_Data = Data; // 데이터 할당

		Thread_admin.DOS_DIS(socket); // 데이터 스트림 생성

	}

	@Override
	public void run() {
		try {

			RT_Save = false;

			System.out.println("\n▼▼▼▼▼▼▼▼▼▼▼▼▼  RT Save - Start ▼▼▼▼▼▼▼▼▼▼▼▼▼\n");
			// RT 데이터 전송

			Thread_admin.dos.write(RT_set_add_del.getBytes()); // "#><n_add><n_del>"
			System.out.println("Send-- " + RT_set_add_del + "\n");

			while (!(Thread_admin.Receive_Data().equals("#>OK"))) {

				// Add Counter - If(time_out = true) -- Thread Stop();

			}
			; // Waiting "@>OK"

			while (Thread_admin.Entry_Sender(cnt, Send_Data, 3) == -1) {

				// Add Counter - If(time_out = true) -- Thread Stop();

			}
			; // Waiting "@>OK"

			System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲   RT Save Success ▲▲▲▲▲▲▲▲▲▲▲▲▲\n\n");

			RT_Save = true;

		} catch (IOException e) {
			System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲   RT Save Fail ▲▲▲▲▲▲▲▲▲▲▲▲▲\n\n");
		}
	}
}

/*--------------------------------------------Read of Routing------------------------------------------------------------*/
/*
 * Read of Routing (라우팅의 상태 요청) RT - '#'(1B)|'<'(1B)|DEST1.......DESTn
 * 
 */
class RT_Read extends Thread {

	Thread_admin Thread_admin = new Thread_admin(); // 스레드 정보 객체 생성
	Casting casting = new Casting();

	byte[] Send_Data = new byte[255];

	String RT_set_add_del;

	int cnt = 0; // entry counter
	int rt_entry = 0;

	boolean RT_Read;
	
	public RT_Read(Socket socket) {

		Thread_admin.DOS_DIS(socket); // 데이터 스트림 생성

	}

	@Override
	public void run() {
		try {

			RT_Read = false;
			
			System.out.println("\n▼▼▼▼▼▼▼▼▼▼▼▼▼  RT Read - Start ▼▼▼▼▼▼▼▼▼▼▼▼▼\n");
			// RT 데이터 전송

			Thread_admin.dos.write((Thread_admin.RT_get + '\n').getBytes()); // "#<"
			System.out.println("--- Sending #<");

			Thread_admin.Receive_Data();
			
			cnt = Integer.parseInt(Thread_admin.Receive_Data.substring(2, 4),16); // 엔트리 수  할당
			Thread_admin.rt_cnt = 0;

			System.out.println("########################### 라우팅 테이블 엔트리 수  ########################### : " + cnt);

			Thread_admin.entry = new String[cnt];// 엔트리 수 만큼 스트링 배열 생성

			Thread_admin.dos.write("#<OK\n".getBytes()); // OK 송신
			System.out.println("--- Sending #<OK\n");

			while (cnt > 0) {

				if ( Thread_admin.parsing(Thread_admin.Receive_Data(), 1) == 0 ) {

					Thread_admin.rt_cnt++;
					cnt--;
					System.out.println("남은 엔트리 수 : ---------------------------------------- : " + cnt + "\n");
					
					
						Thread_admin.dos.write((Thread_admin.RT_get + Thread_admin.RS + '\n').getBytes());
						System.out.println("Send - #<Recieve Success\n");
					

				} else {

					Thread_admin.dos.write((Thread_admin.RT_get + Thread_admin.RF + '\n').getBytes()); // 수신 실패

					System.out.println("Send - #<Recieve Fail\n");

				}


			} // 엔트리 수신이 완료 할때 까지 반복

			System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲   RT Read Success ▲▲▲▲▲▲▲▲▲▲▲▲▲\n\n");
			
			RT_Read = true;

		} catch (Exception e) {
			System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲    RT Read Fail   ▲▲▲▲▲▲▲▲▲▲▲▲▲\n\n");
		}
	}
}
