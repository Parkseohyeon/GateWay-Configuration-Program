package GW;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.comm.CommDriver;
import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

public class GateWay_Serial {

	SerialReader sr = new SerialReader();
	
	InputStream in;
	OutputStream out;
	CommPortIdentifier portIdentifier;
	SerialPort serialPort;
	CommPort commPort;

	String Receive_Data = "";

	int[] Receive_Data_int;
	char[] Receive_Data_char;

	char eZBRST;

	String ZB_CH;
	String ZB_TXPWR;
	String ZB_PAN;
	String ZB_TGT;
	String ZB_BRATE;
	String ZB_LID;
	
	String Node_ID;
	String Parent_ID;
	String nChilds;

	boolean Read = false;
	boolean Save = false;
	boolean pasing = false;

	int case_int = 0; // 1- connect , 2- fail, 3- use

	void connect(String portName, int Out_V) throws Exception {

		portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
			case_int = 3;
		} else {
			commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				System.out.println("connect");
				serialPort = (SerialPort) commPort;
				System.out.println(serialPort.getName());
				serialPort.setSerialPortParams(Out_V, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();

				// System.out.println("connect");
				// (new Thread(new SerialReader(in))).start();
				// (new Thread(new SerialWriter(out))).start();
				case_int = 1;

			} else {
				System.out.println("Error: Fail");
				case_int = 2;
			}
		}
	}

	void Serial_Write2(String msg) {

		try {

			System.out.println("○ Serial_Write2 - OK\n");
			System.out.println("넘김 - " + msg);
			(new Thread(new SerialWriter(out, msg))).start();

		} catch (Exception write) {
			System.out.println("Serial Write Error");
		}

	}


	void Serial_pasing(String msg, int Type) {
		
		pasing = false;
		
		if (Type == 0) {
			
			try {

				System.out.println("\n== zg_msg_pasing - Start ==\n");

				eZBRST = msg.charAt(2);

				ZB_CH = msg.substring(3, 5);
				System.out.println("ZB_CH \t\t - " + ZB_CH);
				ZB_TXPWR = msg.substring(5, 7);
				System.out.println("ZB_TXPWR \t - " + ZB_TXPWR);
				ZB_PAN = msg.substring(7, 11);
				System.out.println("ZB_PAN \t\t - " + ZB_PAN);
				ZB_TGT = msg.substring(11, 15);
				System.out.println("ZB_TGT \t\t - " + ZB_TGT);
				ZB_BRATE = msg.substring(15, 21);

				ZB_BRATE = String.valueOf((Integer.parseInt(ZB_BRATE)));

				System.out.println("ZB_BRATE \t - " + ZB_BRATE);
				ZB_LID = msg.substring(21, 25);
				System.out.println("ZB_LID \t\t - " + ZB_LID);

				pasing = true;

				System.out.println("\n== zg_msg_pasing - Complete ==\n");

			} catch (Exception e) {
				System.out.println("zg_msg_pasing 분류에  실패하였습니다.");

				pasing = false;
			}
			
		} else if ( Type == 1 ) {
			try {

				System.out.println("\n== rt_msg_pasing - Start ==\n");

				Node_ID = msg.substring(2,4);
				System.out.println("Node_ID \t\t - " + Node_ID);
				
				Parent_ID = msg.substring(4,6);
				System.out.println("Parent_ID \t\t - " + Parent_ID);
				
				nChilds = msg.substring(6,8);
				System.out.println("nChilds \t\t - " + nChilds);

				pasing = true;

				System.out.println("\n== rt_msg_pasing - Complete ==\n");

			} catch (Exception e) {
				System.out.println("rt_msg_pasing 분류에  실패하였습니다.");

				pasing = false;
			}
		}
	}

	/** */
	class SerialReader {

		InputStream in;
		String Read_msg = "";

		void Read_Data(InputStream in) {
			
			this.in = in;
		

			System.out.println("*** SerialReader Run\n");

			int len = -1;

			Receive_Data_int = new int[100];
			int s = 0;
			try {

				System.out.println("                  ∨∨∨∨∨∨∨∨∨∨∨∨∨∨∨∨∨∨     Read Start  ∨∨∨∨∨∨∨∨∨∨∨∨∨∨∨∨∨");

				
				while ((Receive_Data_int[s] = this.in.read()) != '\n') {

					if ((char) Receive_Data_int[0] == '@' || (char) Receive_Data_int[0] == '#') {
						System.out.print("'" + (char) Receive_Data_int[s] + "' ");
						s++;
					}
 
				}

				Receive_Data_char = new char[s + 1];

				for (int return_cnt = 0; return_cnt <= s; return_cnt++) {

					Receive_Data_char[return_cnt] = (char) Receive_Data_int[return_cnt];

				}

				String Receive_Data = String.valueOf(Receive_Data_char);

				System.out.println("\n\n" + Receive_Data);
				System.out.println("Receive_Data Size - " + Receive_Data.length());

				if ((Receive_Data_char[0] == '@' && Receive_Data_char[1] == '<')) {

					Serial_pasing(Receive_Data, 0);

				} else if (Receive_Data_char[0] == '#' && Receive_Data_char[1] == '<') {

					Serial_pasing(Receive_Data, 1);

				}

				System.out.println("                  ∧∧∧∧∧∧∧∧∧∧∧∧∧∧∧∧∧∧  Read End  ∧∧∧∧∧∧∧∧∧∧∧∧∧∧∧∧∧∧");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
	}

	/** */
	class SerialWriter implements Runnable {

		OutputStream out;
		int i = 1;
		String msg;

		public SerialWriter(OutputStream out, String msg) {
			this.out = out;
			this.msg = msg;
		}

		@Override
		public void run() {

			try {

				System.out.println("*** SerialWriter Run\n");

				if (msg.substring(0, 2).equals("@<")) {

					System.out.println("\n┌────────────────── start case 1 - zg_msg get ──────────────────┐\n");

					Read = false;

					this.out.write(msg.getBytes());

					System.out.println("Send-----" + msg);

					sr.Read_Data(in); 
							
					while (pasing == false) {

						Thread.sleep(1000);
						
						System.out.println("fail" + Receive_Data);

						this.out.write("@<Receive Fail\n".getBytes());
						sr.Read_Data(in);

					}

					Thread.sleep(3000);
					
					this.out.write("@<Receive Success\n".getBytes());
					
					pasing = false;
					Read = true;
					
					System.out.println("\n└───────────────── Complete ★★★★★ - zg_msg get ─────────────────┘\n");

				} else if (msg.substring(0, 2).equals("@>")) {
					
					Save = false;

					System.out.println("\n┌────────────────── start case 2 - zg_msg set ──────────────────┐\n");

					this.out.write(msg.getBytes());

					System.out.println("Send-----" + msg);

					sr.Read_Data(in);

					while (Receive_Data.equals("@>Receive Fail\n")) {

							Thread.sleep(1000);

							this.out.write(msg.getBytes());

							System.out.println("Send-----" + msg);

							sr.Read_Data(in);

					}

					Save = true;
					
					System.out.println("\n└───────────────── Complete ★★★★★ - zg_msg set ─────────────────┘\n");

				} else if (msg.substring(0, 2).equals("#<")) {

					System.out.println("\n┌────────────────── start case 3 - rt_msg get ──────────────────┐\n");

					Read = false;
					
					this.out.write(msg.getBytes());

					System.out.println("Send-----" + msg);

					sr.Read_Data(in);
					
					while (pasing == false) {

						
						Thread.sleep(1000);
						
						System.out.println("fail" + Receive_Data);

						this.out.write("#<Receive Fail\n".getBytes());
						sr.Read_Data(in);

					}
					
					Thread.sleep(3000);

					this.out.write("#<Receive Success\n".getBytes());

					System.out.println("Send----- #<Receive Success\n");
					
					pasing = false;
					Read = true;

					System.out.println("\n└───────────────── Complete ★★★★★ - rt_msg get ─────────────────┘\n");

				} else if (msg.substring(0, 2).equals("#>")) {

					System.out.println("\n┌────────────────── start case 4 - rt_msg set ──────────────────┐\n");
					
					Save = false;
					
					
					this.out.write(msg.getBytes());

					System.out.println("Send-----" + msg);

					sr.Read_Data(in);
					
					while (Receive_Data.equals("#>Receive Fail\n")) {

						Thread.sleep(1000);

						this.out.write(msg.getBytes());

						System.out.println("Send-----" + msg);

						sr.Read_Data(in);

				}
					
					Save = true;
					

					System.out.println("\n└───────────────── Complete ★★★★★ - rt_msg set ─────────────────┘\n");

				}
				
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

class ListPorts {

	String[] COM;
	CommPortIdentifier portIdentifier;
	SerialPort serialPort;
	CommPort commPort;
	CommDriver commDriver;
	StringBuilder output = new StringBuilder();

	Thread_usg[] tu = new Thread_usg[255];

	int i = 0;

	ListPorts() {

		COM = new String[255];

		ListVeiw();

		i = 2;

		try {
			for (int x = 0; x < 255; x++) {

				tu[x] = new Thread_usg("COM" + x);

				tu[x].start();

				tu[x].join();

				if ((tu[x].PortName.equals("") == false)
						&& (tu[x].PortName.equals(COM[0].replace("통신 포트 : ", "")) == false)
						&& (tu[x].PortName.equals(COM[1].replace("통신 포트 : ", "")) == false)) {
					COM[i] = "USB_Serial : " + tu[x].PortName;
					i++;
				}

			}

		} catch (Exception ee) {
			System.out.println("Error : ListPorts");
		}

	}

	void ListVeiw() {

		java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {

			CommPortIdentifier port = (CommPortIdentifier) portEnum.nextElement();

			String type;

			switch (port.getPortType()) {
			case CommPortIdentifier.PORT_PARALLEL:
				type = "Parallel";
				break;
			case CommPortIdentifier.PORT_SERIAL:

				if (i < 2) {
					COM[i] = "통신 포트 : " + port.getName();
					i++;
				}

				break;
			default: /// Shouldn't happen
				type = "Unknown";
				break;
			}

		}
	}

	class Thread_usg extends Thread {

		String PortName = "", portName;

		Thread_usg(String Name) {
			portName = Name;
		}

		@Override
		public void run() {

			try {
				portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

				CommPortIdentifier.getPortIdentifier(portName);

				commPort = portIdentifier.open(portName, 2000);

				PortName = commPort.getName();

				commPort.close();

			} catch (Exception e) {

			}
		}

	}

}
