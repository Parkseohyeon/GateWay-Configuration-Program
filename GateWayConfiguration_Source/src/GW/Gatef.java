package GW;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.Socket;

import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class GateWay_Window extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

	/*
	 * 객체 생성
	 * 
	 */

	Setting setting = new Setting();
	Main main = new Main();
	Ethernet et = new Ethernet();
	Serial sr = new Serial();
	Basic_set bs = new Basic_set();
	Routing rt = new Routing();
	GateWay_Serial gs = new GateWay_Serial();
	TCP_IP_Client TIC = new TCP_IP_Client();
	Device_inf di[] = new Device_inf[255];
	Casting casting = new Casting();
	Table_Set ts = new Table_Set();
	Thread thread = new Thread(ts);
	SerialConnect_Check sc = new SerialConnect_Check();

	MyRenderer renderer = new MyRenderer();
	Method method = new Method();
	/*
	*
	*
	*/

	String COM;
	String IP_Space = "";
	String MAC_Space = "";
	String Type = "Temp_test";
	String Connect_Port = "";

	String[] Connect_IP = new String[255];
	String[] Connect = new String[255];
	String[] returnCOM;

	int OV = 110;
	int rt_count = 0, Local_count = 0, GUI_NODE_Count = 0, GUI_CCP_Count = 0, Del_Count = 0, Del_Count2 = 0,
			Add_Count = 0, Add_Count2 = 0;
	int[] tree_count = new int[255], Del = new int[255], Add_Dev_ID = new int[255], Del_Tree = new int[255],
			Del_num = new int[255], Del_Dev_ID = new int[255], Add_nHOP = new int[255], Del_nHOP = new int[255];

	int[][] Add_HOP = new int[255][255];

	// 마우스 드래그
	boolean isDragged, isSelected;
	int offX, offY;
	int GUI_GW_x, GUI_GW_y;

	JButton Tab1, Tab2;
	JPanel GUI, GUI_GWP, Zg_inf_Panel;
	JPanel[][] TREE = new JPanel[255][255];
	JLabel Title, Wating_img, Zg_inf_Label;

	Color blue = new Color(90, 174, 255);
	Color yellow = new Color(255, 187, 0);
	Font f = new Font("HY헤드라인M", Font.BOLD | Font.ITALIC, 20);

	ImageIcon img = new ImageIcon("Server.png");

	JLabel GUI_GW;
	JLabel[] GUI_NODE = new JLabel[255];
	JLabel[] GUI_CCP = new JLabel[255];

	Graphics g;

	boolean Flag = false, Time_Flag = false, Channel_Flag = false;

	GateWay_Window() {

		this.setSize(700, 450); // 사이즈 가로 700, 세로 500
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("GateWay Setting");
		this.setBackground(Color.white);
		this.setIconImage(img.getImage());
		this.setLayout(null);

//		Wating_img = new JLabel(new ImageIcon("TIME.png"));
//		Wating_img.setBounds(280, 0, 100, 100);
//
//		GUI = new JPanel();
//		GUI.setBounds(705, 32, 475, 382);
//		GUI.setBackground(Color.white);
//		GUI.setLayout(null);
//		GUI.addMouseListener(this);
//		GUI.addMouseMotionListener(this);

		Zg_inf_Panel = new JPanel();
		Zg_inf_Panel.setBounds(699, 32, 186, 382);
		Zg_inf_Panel.setBackground(Color.white);
		Zg_inf_Panel.setLayout(null);

//		GUI_GW = new JLabel(new ImageIcon("gui_gateway.png"));
//		GUI_GW.setBounds(200, 5, 80, 70);
//		GUI_GW.setBackground(Color.lightGray);
//
//		g = GUI.getGraphics();
//		isDragged = false;

		Title = new JLabel("GateWay");
		Title.setFont(f);
		Title.setForeground(yellow);
		Title.setBounds(5, 2, 120, 30);

		Zg_inf_Label = new JLabel("지그비 설정");
		Zg_inf_Label.setFont(new Font("신명조", Font.BOLD, 15));
		Zg_inf_Label.setForeground(blue);
		Zg_inf_Label.setBounds(699, 4, 120, 30);

		Tab1 = new JButton("기본 설정");
		Tab2 = new JButton("라우팅");
		Tab1.setBackground(Color.white);
		Tab2.setBackground(Color.white);
		Tab1.setBounds(110, 0, 100, 30);
		Tab2.setBounds(210, 0, 100, 30);

		bs.Zb_Chip_P.setBounds(55, 30, 70, 70);
		bs.reset_Panel.setBounds(10, 120, 150, 30);
		bs.ZBP.setBounds(22, 160, 150, 35);
		bs.ZBP2.setBounds(21, 200, 160, 35);
		bs.ZB_PAN_P.setBounds(45, 250, 130, 30);
		bs.Target_P.setBounds(5, 290, 170, 30);
		bs.Local_P.setBounds(41, 330, 130, 30);

		rt.TI.setSelected(true);

		setting.E_button.addActionListener(this);
		setting.S_button.addActionListener(this);
		bs.Save.addActionListener(this);
		bs.Read.addActionListener(this);
		bs.Channel.addActionListener(this);
		bs.Serial_Connect.addActionListener(this);
		sr.Channel.addActionListener(this);
		sr.Save.addActionListener(this);
		sr.Read.addActionListener(this);
		sr.Serial_Connect.addActionListener(this);
		et.search.addActionListener(this);
		setting.clear.addActionListener(this);
		et.connect.addActionListener(this);
		rt.Add.addActionListener(this);
		rt.Del.addActionListener(this);
		rt.Clear.addActionListener(this);
		rt.Modify.addActionListener(this);
		rt.Save.addActionListener(this);
		rt.TI.addActionListener(this);
		rt.CCP.addActionListener(this);
		rt.Tab.addActionListener(this);
		this.Tab1.addActionListener(this);
		this.Tab2.addActionListener(this);

		sr.jTable.addMouseListener(this);

		setting.setLayout(null);

//		this.add(Wating_img);
		this.add(Title);
		this.add(Zg_inf_Label);
		this.add(setting);
		this.add(main);
		this.add(et);
		this.add(sr);
		this.add(Tab1);
		this.add(Tab2);
		this.add(bs);
		this.add(rt);
//		this.add(GUI);
//		this.GUI.add(GUI_GW);
		Zg_inf_Panel.add(bs.Zb_Chip_P);
		Zg_inf_Panel.add(bs.reset_Panel);
		Zg_inf_Panel.add(bs.ZBP);
		Zg_inf_Panel.add(bs.ZBP2);
		Zg_inf_Panel.add(bs.ZB_PAN_P);
		Zg_inf_Panel.add(bs.Target_P);
		Zg_inf_Panel.add(bs.Local_P);
		this.add(Zg_inf_Panel);

		this.setResizable(false); // 프레임 크기 고정
		Tab1.setFocusPainted(false);
		Tab2.setFocusPainted(false);
		this.setVisible(true); // 프레임 화면 출력 설정
		et.setVisible(false);
		sr.setVisible(false);
		Tab1.setVisible(false);
		Tab2.setVisible(false);
		bs.setVisible(false);
		rt.setVisible(false);
//		Wating_img.setVisible(false);

		sc.start();

	}

	public void actionPerformed(ActionEvent e) { // 액션이벤트 처리

		/*--------------------------------------------Ethernet Event------------------------------------------------------------*/
		/*
		 * 
		 * 
		 * 
		 * 
		 *
		 */
		if (e.getSource() == setting.E_button) { // Ethernet Button

			if (TIC.socket == null) {

				method.View_Ethernet();
				this.setSize(700, 450);

			} else {

				method.View_Connect_Ethernet();
				this.setSize(900, 450);

			}
		}

		// Search Button
		if (e.getSource() == et.search) {

//			Wating_img.setVisible(true);
//			Wating_img.setBounds(180, 280, 100, 100);

			Table_Set ts = new Table_Set();

			ts.start();

			while (Time_Flag == false) {

			}

//			Wating_img.setVisible(false);

		}

		// Connect Button
		if (e.getSource() == et.connect) {

			// try {
			//
			// if (et.IP_Text == false) {
			//
			// TIC.TCP_IP_Client(Connect_IP[et.row], 6000);
			//
			// } else if (et.IP_Text == true) {
			//
			// TIC.TCP_IP_Client(et.IP_TextField.getText(), 6000);
			//
			// }
			//
			// if (TIC.socket.isConnected() == true) {
			//
			// Connect[et.row] = "연결 됨";
			//
			// Thread_Cennect tc = new Thread_Cennect();
			//
			// tc.start();
			//
			// setting.clear.setVisible(true);
			//
			// method.Visable(); // 기본 설정 모드로 설정.
			// this.setSize(900, 450);
			//
			// //e.setSource(bs.Read);
			// } else {
			//
			// Connect[et.row] = "연결 안됨";
			// ts.Thread_restart();
			// }
			//
			// } catch (Exception connect) {
			// System.out.println(" 연결할 수 없는 상태입니다. ");
			// }

			method.View_Routing();
		}

		// Clear Button
		if (e.getSource() == setting.clear) {

			try {
				TIC.socket.close();
				TIC.socket = null;
				this.setSize(700, 450);
				setting.clear.setVisible(false);

				method.View_SearchBox();

				System.out.println("서버에 연결이 해제 되었습니다.");
				Connect[et.row] = "연결 안됨";
				ts.Thread_restart();

			} catch (Exception clear) {
				System.out.println("해제 오류");

			}

		}
		// ^^^
		/*--------------------------------------------Ethernet Event------------------------------------------------------------*/

		/*---------------------------------------------Serial Event-------------------------------------------------------------*/
		/*
		 *
		 * 
		 * 
		 * 
		 * 
		 */
		if (e.getSource() == setting.S_button) {

			method.View_Serial();
			this.setSize(700, 450);

		}

		if (e.getSource() == bs.Serial_Connect || e.getSource() == sr.Serial_Connect) {

			String connect_name;

			if (Channel_Flag == false) {

				try {

					if (e.getSource() == bs.Serial_Connect) {

						connect_name = ((String) bs.Channel.getSelectedItem()).replace("통신 포트 : ", "");
						connect_name = ((String) bs.Channel.getSelectedItem()).replace("USB_Serial : ", "");

						gs.connect(connect_name, 115200);

						if (gs.case_int == 1) { // connect {

							bs.Channel.setSelectedItem((String) bs.Channel.getSelectedItem());
							sr.Channel.setSelectedItem((String) bs.Channel.getSelectedItem());

							bs.Channel.setEnabled(false);
							sr.Channel.setEnabled(false);

							bs.Serial_Connect.setText("시리얼 해체(C)");
							sr.Serial_Connect.setText("시리얼 해체(C)");
							bs.Serial_Connect.setBackground(Color.ORANGE);
							sr.Serial_Connect.setBackground(Color.ORANGE);

							for (int i = 1; i <= 4; i++) {

								bs.GIPT[i].setEnabled(true);
								bs.GWT[i].setEnabled(true);
								bs.NMT[i].setEnabled(true);

							}
							sr.Local_ID_TF.setEnabled(true);
							sr.Parent_ID_TF.setEnabled(true);
							sr.n_Child_TF.setEnabled(true);
							sr.Pan_ID_TF.setEnabled(true);
							sr.Target_TF.setEnabled(true);
							sr.Local2_ID_TF.setEnabled(true);
							sr.Z_Channel.setEnabled(true);
							sr.Z_Output.setEnabled(true);

							Channel_Flag = true;

							method.View_Basic_Set();

							bs.Connect_L.setText(((String) bs.Channel.getSelectedItem()) + " 연결 됨");
						}

					} else if (e.getSource() == sr.Serial_Connect) {

						connect_name = ((String) sr.Channel.getSelectedItem()).replace("통신 포트 : ", "");
						connect_name = ((String) sr.Channel.getSelectedItem()).replace("USB_Serial : ", "");

						gs.connect(connect_name, 115200);

						if (gs.case_int == 1) { // connect {

							bs.Channel.setSelectedItem((String) sr.Channel.getSelectedItem());
							sr.Channel.setSelectedItem((String) sr.Channel.getSelectedItem());

							bs.Channel.setEnabled(false);
							sr.Channel.setEnabled(false);

							bs.Serial_Connect.setText("시리얼 해체(C)");
							sr.Serial_Connect.setText("시리얼 해체(C)");
							bs.Serial_Connect.setBackground(Color.ORANGE);
							sr.Serial_Connect.setBackground(Color.ORANGE);

							for (int i = 1; i <= 4; i++) {

								bs.GIPT[i].setEnabled(true);
								bs.GWT[i].setEnabled(true);
								bs.NMT[i].setEnabled(true);

							}
							sr.Local_ID_TF.setEnabled(true);
							sr.Parent_ID_TF.setEnabled(true);
							sr.n_Child_TF.setEnabled(true);
							sr.Pan_ID_TF.setEnabled(true);
							sr.Target_TF.setEnabled(true);
							sr.Local2_ID_TF.setEnabled(true);
							sr.Z_Channel.setEnabled(true);
							sr.Z_Output.setEnabled(true);

							Channel_Flag = true;

							method.View_Serial();

							bs.Connect_L.setText(((String) bs.Channel.getSelectedItem()) + " 연결 됨");

						}
					}

					if (gs.case_int == 2) { // non-connect

						for (int i = 1; i <= 4; i++) {

							bs.GIPT[i].setEnabled(false);
							bs.GWT[i].setEnabled(false);
							bs.NMT[i].setEnabled(false);

						}

						sr.Local_ID_TF.setEnabled(false);
						sr.Parent_ID_TF.setEnabled(false);
						sr.n_Child_TF.setEnabled(false);
						sr.Pan_ID_TF.setEnabled(false);
						sr.Target_TF.setEnabled(false);
						sr.Local2_ID_TF.setEnabled(false);
						sr.Z_Channel.setEnabled(false);
						sr.Z_Output.setEnabled(false);

						method.View_Basic_Set();

					} else if (gs.case_int == 3) { // overlap

						bs.Channel.setSelectedItem(bs.Connect_L.getText().substring(0, 4));

					}

				} catch (Exception e1) {

					System.out.println("bs or sr 시리얼 연결 에러");
					bs.Connect_L.setText("");

				}

			} else if (Channel_Flag == true) {

				try {

					if (e.getSource() == bs.Serial_Connect) {

						bs.Channel.setEnabled(true);
						sr.Channel.setEnabled(true);

						gs.serialPort.close();
						gs.portIdentifier = null;

						bs.Serial_Connect.setText("시리얼 연결(C)");
						sr.Serial_Connect.setText("시리얼 연결(C)");
						bs.Serial_Connect.setBackground(Color.gray);
						sr.Serial_Connect.setBackground(Color.gray);

					} else if (e.getSource() == sr.Serial_Connect) {

						bs.Channel.setEnabled(true);
						sr.Channel.setEnabled(true);

						gs.serialPort.close();
						gs.portIdentifier = null;

						bs.Serial_Connect.setText("시리얼 연결(C)");
						sr.Serial_Connect.setText("시리얼 연결((C)");
						bs.Serial_Connect.setBackground(Color.gray);
						sr.Serial_Connect.setBackground(Color.gray);
					}

					for (int i = 1; i <= 4; i++) {

						bs.GIPT[i].setEnabled(false);
						bs.GWT[i].setEnabled(false);
						bs.NMT[i].setEnabled(false);

					}

					sr.Local_ID_TF.setEnabled(false);
					sr.Parent_ID_TF.setEnabled(false);
					sr.n_Child_TF.setEnabled(false);
					sr.Pan_ID_TF.setEnabled(false);
					sr.Target_TF.setEnabled(false);
					sr.Local2_ID_TF.setEnabled(false);
					sr.Z_Channel.setEnabled(false);
					sr.Z_Output.setEnabled(false);

					Channel_Flag = false;

					if (e.getSource() == sr.Serial_Connect) {
						method.View_Basic_Set();
					} else {
						method.View_Serial();
					}

				} catch (Exception e1) {

					System.out.println("bs or sr 시리얼 연결 에러");
					bs.Connect_L.setText("");

				}

			}
		}

		if (e.getSource() == sr.Read) {

			if (gs.case_int == 1) {

				gs.Serial_Write2("@<\n");

				while (gs.Read == false) {

				}

				sr.Pan_ID_TF.setText(gs.ZB_PAN);
				sr.Target_TF.setText(gs.ZB_TGT);
				sr.Local2_ID_TF.setText(gs.ZB_LID);
				sr.Z_Channel.setSelectedItem(gs.ZB_CH + "      ");
				sr.Z_Output.setSelectedItem(gs.ZB_TXPWR + "         ");
				sr.Boadrate_CB.setSelectedItem(gs.ZB_BRATE);

				if (gs.eZBRST == 'T') {
					sr.Zb_reset.setSelected(true);
					sr.Zb_reset_Label.setForeground(Color.black);
					sr.reset = true;
				} else if (gs.eZBRST == 'F') {
					sr.Zb_reset.setSelected(false);
					sr.Zb_reset_Label.setForeground(Color.lightGray);
					sr.reset = false;
				}

				try {

					Thread.sleep(2000);

				} catch (Exception seding) {
					System.out.println("시리얼 전송 실패 ");
				}

				gs.Read = false;

				gs.Serial_Write2("#<\n");

				while (gs.Read == false) {

				}

				sr.Local_ID_TF.setText(String.format("%04d", Integer.parseInt(gs.Node_ID, 16)));
				sr.Parent_ID_TF.setText(String.format("%04d", Integer.parseInt(gs.Parent_ID, 16)));
				sr.n_Child_TF.setText(String.format("%04d", Integer.parseInt(gs.nChilds, 16)));

				gs.Read = false;
			}
		}

		if (e.getSource() == sr.Save) {

			///////////////////////////////////////////////////////////////////////////////////////////////////////
			String zb_msg;

			String ZB_CH = ((String) sr.Z_Channel.getSelectedItem()).replace(" ", ""); // 3Byte
			String ZB_TXPWR = ((String) sr.Z_Output.getSelectedItem()).replace(" ", ""); // 3Byte
			String ZB_PAN = String.format("%04x", Integer.parseInt(sr.Pan_ID_TF.getText(), 16)); // 5Byte
			String ZB_TGT = String.format("%04x", Integer.parseInt(sr.Target_TF.getText(), 16)); // 5Byte
			String ZB_BRATE = String.format("%06d", Integer.parseInt((String) sr.Boadrate_CB.getSelectedItem())); // 7Byte
			String ZB_LID = String.format("%04x", Integer.parseInt(sr.Local2_ID_TF.getText(), 16)); // 5Byte

			String N = "\n";
			String reset;

			if (sr.reset == true) {

				reset = "T";

			} else {

				reset = "F";

			}

			zb_msg = "@>" + reset + ZB_CH + ZB_TXPWR + ZB_PAN + ZB_TGT + ZB_BRATE + ZB_LID + N;

			///////////////////////////////////////////////////////////////////////////////////////////////////////

			String rt_msg;

			String Node_ID = String.format("%02x", Integer.parseInt(sr.Local_ID_TF.getText())); // 4Byte
			String Parent_ID = String.format("%02x", Integer.parseInt(sr.Parent_ID_TF.getText())); // 4Byte
			String nChilds_ID = String.format("%02x", Integer.parseInt(sr.n_Child_TF.getText())); // 4Byte

			rt_msg = "#>" + Node_ID + Parent_ID + nChilds_ID + N;

			///////////////////////////////////////////////////////////////////////////////////////////////////////

			System.out.println("\n ☆☆☆☆☆☆☆☆☆☆☆☆  message 생성 - zb_msg / rt msg  ☆☆☆☆☆☆☆☆☆☆☆☆\n");

			System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆ zb_msg : " + zb_msg + N);

			System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆ rt_msg : " + rt_msg + N);

			if (gs.case_int == 1) {

				try {

					gs.Save = false;

					gs.Serial_Write2(zb_msg);

					while (gs.Save == false) {

					}

					Thread.sleep(3000);

					gs.Serial_Write2(rt_msg);

				} catch (Exception seding) {
					System.out.println("시리얼 전송 실패 ");
				}
			}
		}

		if (e.getSource() == sr.next) {

			for (int dev_cnt = 0; dev_cnt < 255; dev_cnt++) {

			}
		}

		/*---------------------------------------------Serial Event-------------------------------------------------------------*/

		/*-------------------------------------------Basic_Set Event-----------------------------------------------------------*/
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 */

		// Basic_Set Button
		if (e.getSource() == Tab1) {

			method.View_Basic_Set();
			this.setSize(900, 450);

		}

		// Basic_Set Save
		if (e.getSource() == bs.Save) {

			String Sever_IP = String.valueOf(String.format("%03d", Integer.parseInt(bs.SIPT[1].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.SIPT[2].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.SIPT[3].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.SIPT[4].getText())));

			String Port = bs.PortT.getText();

			String GateWay_IP = String.valueOf(String.format("%03d", Integer.parseInt(bs.GIPT[1].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GIPT[2].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GIPT[3].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GIPT[4].getText())));

			String GateWay = String.valueOf(String.format("%03d", Integer.parseInt(bs.GWT[1].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GWT[2].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GWT[3].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GWT[4].getText())));

			String Network_Mask = String.valueOf(String.format("%03d", Integer.parseInt(bs.NMT[1].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.NMT[2].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.NMT[3].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.NMT[4].getText())));

			String ZB_CH = (bs.ZB_CH_ComboBox.getSelectedItem().toString()).replace(" ", "");

			String ZB_TXPWR = (bs.ZB_TXPWR_Combo.getSelectedItem().toString()).replace(" ", "");

			String ZB_PAN = bs.ZB_PAN_T.getText();

			String ZB_TGT = bs.Target_T.getText();

			String ZB_BRATE = "038400";

			String ZB_LID = bs.Local_T.getText();

			char eUDP;
			char eZBRST;
			char DHCP;

			if (bs.UDP == true) {
				eUDP = 'T';

			} else {
				eUDP = 'F';
			}

			if (bs.DHCP == true) {
				DHCP = 'T';

			} else {
				DHCP = 'F';
			}

			if (bs.reset == true) {
				eZBRST = 'T';

			} else {
				eZBRST = 'F';
			}

			// ZB msg
			// first field: '@' - ZB, '#' - RT
			// second field:
			// '>' : send new ZB_IN setting (if '@' in first field)
			// send new RT setting (if '# in first field)
			// '<' : request current ZB_IN setting (if '@' in first field)
			// request current RT setting (if '# in first field)

			// '@'|'>'(1B)|eUDP(1B)|IP(4B)|SN(4B)|GW(4B)|SVR_IP(4B)|SVR_PORT(2B)|ZB_CH(1B)|ZB_TXPWR(1B)
			// ZB_IN protocol
			// if Server request current ZB_IN settings to GW
			// 1. '@'|'<'| (SVR)
			// 2. '@'|'<'|
			// eUDP(1B)|IP(4B)|SN(4B)|GW(4B)|SVR_IP(4B)|SVR_PORT(2B)|ZB_CH(1B)|ZB_TXPWR(1B)
			// (GW)
			// 3. '@'|'<'| ACK("Receive Success) (SVR)
			// if Server send new ZB_IN settings to GW
			// 1. '@'|'>'| (SVR)
			// 2. '@'|'>'| ACK("OK") (GW)
			// 2. '@'|'>'|
			// eUDP(1B)|IP(4B)|SN(4B)|GW(4B)|SVR_IP(4B)|SVR_PORT(2B)|ZB_CH(1B)|ZB_TXPWR(1B)
			// (SVR)
			// 3. '@'|'>'| ACK("Receive Success) (GW)

			// RT protocol
			// if Server request current RT table to Gateway,
			// 1. '#'|'<'| (SVR)
			// 2. '#'|'<'| number of entries(1B) (GW)
			// 3. '#'|'<'| ACK("OK") (SVR)
			// 4. '#'|'<'| DEST(2B)|nHOPS(1B)|LV1(2B)|...|LV10(2B) (GW)
			// 3. '#'|'<'|ACK("Receive Success") (SVR) or
			// '#'|'<'|NACK("Receive Fail") (SVR)
			// if Server change current RT table and want to send it to GW,
			// 1. '#'|'>'| number of entries(1B) (SVR)
			// 2. '#'|'>'|ACK("OK") (GW)
			// 3. '#'|'>'|"add"|DEST(2B)|nHOPS(1B)|LV1(2B)|...|LV10(2B) (SVR)
			// '#'|'>'|"del"|DEST(2B)|nHOPS(1B)|LV1(2B)|...|LV10(2B) (SVR)
			// 4. '#'|'>'|ACK("Receive Success") (GW)
			// '#'|'>'|NACK("Receive Fail") (GW)

			String Message; // 소포를 받을 빈공간을 만듬.

			Message = "@>" + eZBRST + eUDP + GateWay_IP + Network_Mask + GateWay + Sever_IP + Port + ZB_CH + ZB_TXPWR
					+ ZB_PAN + ZB_TGT + ZB_BRATE + ZB_LID + "\n";

			System.out.println(eZBRST);
			System.out.println(eUDP);
			System.out.println(GateWay_IP);
			System.out.println(Network_Mask);
			System.out.println(GateWay);
			System.out.println(Sever_IP);
			System.out.println(Port);
			System.out.println(ZB_PAN);
			System.out.println(ZB_TGT);
			System.out.println(ZB_BRATE);
			System.out.println(ZB_LID);

			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			GateWay_IP = String.valueOf(String.format("%03d", Integer.parseInt(bs.GIPT[1].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GIPT[2].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GIPT[3].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GIPT[4].getText())));

			GateWay = String.valueOf(String.format("%03d", Integer.parseInt(bs.GWT[1].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GWT[2].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GWT[3].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.GWT[4].getText())));

			Network_Mask = String.valueOf(String.format("%03d", Integer.parseInt(bs.NMT[1].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.NMT[2].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.NMT[3].getText())))
					+ String.valueOf(String.format("%03d", Integer.parseInt(bs.NMT[4].getText())));

			String msg;
			String DHCP_set;
			String N = "\n";

			if (bs.DHCP == true) {
				DHCP_set = "T";
			} else {
				DHCP_set = "F";
			}

			msg = "@>" + DHCP_set + GateWay_IP + Network_Mask + GateWay + N;

			if (TIC.socket.isConnected() == true) {

				zg_msg_Save zms = new zg_msg_Save(TIC.socket, Message.toString());// 데이터
																					// 전송
				// 클래스.
				zms.start();

				while (zms.Save == false) {
				}
				;// 정보 전송이 완료 전 까지 다른 작업은 불가능.
			}

			if (gs.case_int == 1) {

				try {
					System.out.println("\n".getBytes().length);
					gs.Serial_Write2(msg);
				} catch (Exception emsg) {
					System.out.println("Zg_msg Save - Serial Error");
				}

			}
		}

		// Basic_Set Read
		if (e.getSource() == bs.Read) {

			zg_msg_Read zmr = new zg_msg_Read(TIC.socket);// 데이터 전송
			zmr.start();

			while (zmr.Read == false) {
			}
			;// 정보 업로드 가능 전 까지 다른 작업은 불가능.

			if (zmr.Thread_admin.eUDP == 'F' && bs.UDP == true) {
				bs.UDP_CheckBox.setSelected(false);
				bs.UDP = false;
				bs.T_Lable.setForeground(Color.gray);
			} else if (zmr.Thread_admin.eUDP == 'T' && bs.UDP == false) {
				bs.UDP_CheckBox.setSelected(true);
				bs.UDP = true;
				bs.T_Lable.setForeground(Color.black);
			}

			if (zmr.Thread_admin.eZBRST == 'F' && bs.reset == true) {
				bs.Zb_reset.setSelected(false);
				bs.reset = false;
				bs.Zb_reset_Label.setForeground(Color.gray);
			} else if (zmr.Thread_admin.eZBRST == 'T' && bs.reset == false) {
				bs.Zb_reset.setSelected(false);
				bs.reset = false;
				bs.Zb_reset_Label.setForeground(Color.black);
			}

			String[] SIP = zmr.Thread_admin.Server_IP.split("\\.");

			String Port = zmr.Thread_admin.Port;

			String[] GWIP = zmr.Thread_admin.GateWay_IP.split("\\.");
			String GW[] = zmr.Thread_admin.GateWay.split("\\.");
			String NWM[] = zmr.Thread_admin.Network_Mask.split("\\.");

			bs.SIPT[1].setText(SIP[0]);
			bs.SIPT[2].setText(SIP[1]);
			bs.SIPT[3].setText(SIP[2]);
			bs.SIPT[4].setText(SIP[3]);

			bs.PortT.setText(Port);

			bs.GIPT[1].setText(GWIP[0]);
			bs.GIPT[2].setText(GWIP[1]);
			bs.GIPT[3].setText(GWIP[2]);
			bs.GIPT[4].setText(GWIP[3]);

			bs.GWT[1].setText(GW[0]);
			bs.GWT[2].setText(GW[1]);
			bs.GWT[3].setText(GW[2]);
			bs.GWT[4].setText(GW[3]);

			bs.NMT[1].setText(NWM[0]);
			bs.NMT[2].setText(NWM[1]);
			bs.NMT[3].setText(NWM[2]);
			bs.NMT[4].setText(NWM[3]);

			bs.ZB_CH_ComboBox.setSelectedItem(zmr.Thread_admin.ZB_CH);

			bs.ZB_TXPWR_Combo.setSelectedItem(zmr.Thread_admin.ZB_POWER);

			bs.ZB_PAN_T.setText(zmr.Thread_admin.ZB_PAN);

			bs.Target_T.setText(zmr.Thread_admin.ZB_TGT);

			bs.Local_T.setText(zmr.Thread_admin.ZB_LID);

		}

		// ^^^
		/*------------------------------------------Basic_Set Event-----------------------------------------------------------*/

		/*--------------------------------------------Routing Event------------------------------------------------------------*/
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		if (e.getSource() == Tab2) { // SR

			RT_Read rt_read = new RT_Read(TIC.socket); // "#<"

			String Parent_ID = "0000";
			
			int RT_entry = rt_read.Thread_admin.rt_cnt;

			method.View_Routing();

			if (this.Flag == true) {
				this.setSize(1200, 450);
			} else {
				this.setSize(700, 450);
			}

			rt_read.start();

			while (rt_read.RT_Read == false) {
				// Wating for RT_Read;;
			}

			if (RT_entry > 0) {

				rt.model.setNumRows(0);
				sr.model.setNumRows(0);

				System.out.println("Routing Entry Count -- " + RT_entry);

				for (int insert_cnt = 0; insert_cnt < RT_entry; insert_cnt++) {

					String Dest_ID = rt_read.Thread_admin.Dest_ID[insert_cnt];
					String Tree = rt_read.Thread_admin.Tree[insert_cnt];
					String nHop = rt_read.Thread_admin.nHOPS[insert_cnt];
					String[] Hop = rt_read.Thread_admin.HOP_Level[insert_cnt];
					
					int  nHop_int = Integer.parseInt(nHop);
					int  Tree_int = Integer.parseInt(Tree);
					int  Node_Count = Integer.parseInt(Dest_ID) -1;

					String[] newObject = { Dest_ID, Tree, nHop, Hop[0], Hop[1], Hop[2], Hop[3], Hop[4], Hop[5], Hop[6],
							Hop[7], Hop[8], Hop[9] };

					if ( nHop_int > 0) {
						
						Parent_ID = Hop[ nHop_int - 1];
					
					}

					String[] newObject_serial = { Dest_ID, Parent_ID };

					di[Node_Count] = new Device_inf(Dest_ID, Parent_ID, "Temp_test");

					di[Node_Count].n_HOP = nHop_int;
					di[Node_Count].Tree = Tree_int;

					for (int hop_cnt = 0; hop_cnt < di[Node_Count].n_HOP; hop_cnt++) {
						di[Node_Count].HOP[hop_cnt] = Hop[hop_cnt];
					}

					rt.jTable.setDefaultRenderer(Object.class, renderer);

					rt.model.insertRow(insert_cnt, newObject);
					sr.model.insertRow(insert_cnt, newObject_serial);
					rt.model.setValueAt("insert", insert_cnt, 13);
					
					rt_count++;

				}

				if ( RT_entry < 5) {
					rt.model.setRowCount(6);
					sr.model.setRowCount(6);
				} else {
					rt.model.setRowCount(rt_count + 1);
					sr.model.setRowCount(rt_count + 1);
				}

				for (int clear_cnt = rt_count; clear_cnt < rt.model.getRowCount(); clear_cnt++) {
					rt.model.setValueAt("", clear_cnt, 0);
					rt.model.setValueAt("", clear_cnt, 13);
				}

				Add_Count = 0;
				Add_Count2 = 0;
				Del_Count = 0;
				Del_Count2 = 0;

			}

		}

		if (e.getSource() == rt.Tab) {

			if (this.Flag == false) {
				this.setSize(1200, 450);
				this.Flag = true;
			} else {
				this.setSize(700, 450);
				this.Flag = false;
			}
		}

		if (e.getSource() == rt.TI) {
			rt.CCP.setSelected(false);
			rt.TI.setSelected(true);
			Type = "Temp_test";
		}

		if (e.getSource() == rt.CCP) {
			rt.TI.setSelected(false);
			rt.CCP.setSelected(true);
			Type = "CCP";

		}

		if (e.getSource() == rt.Clear) {

			int clear_cnt_end = rt_count;
			boolean clear_b = false;

			if (Add_Count > 0 || Del_Count > 0 || Add_Count2 > 0 || Del_Count2 > 0) {

				Add_Dev_ID = new int[255];
				Del_Dev_ID = new int[255];
				Add_Count = 0;
				Del_Count = 0;
				Add_Count2 = 0;
				Del_Count2 = 0;

				for (int clear_cnt = 0; clear_cnt < rt_count; clear_cnt++) {

					if (rt.model.getValueAt(clear_cnt, 13).equals("Add")) {

						if (clear_b == false) {
							clear_cnt_end = clear_cnt;
							clear_b = true;
						}

						di[clear_cnt] = null;

					} else {

						rt.model.setValueAt("insert", clear_cnt, 13);
						Local_count = Integer.valueOf(rt.model.getValueAt(clear_cnt, 0).toString());
					}

				}

				rt.jTable.setDefaultRenderer(Object.class, renderer);

				rt.model.setRowCount(clear_cnt_end);
				rt_count = clear_cnt_end;

				if (rt_count == 0) {
					Local_count = 0;
					rt.Node_ID.setText(String.valueOf(String.format("%04d", Local_count + 1)));
				} else {
					rt.Node_ID.setText(String.valueOf(String.format("%04d", Local_count + 1)));
				}

				if (rt.model.getRowCount() < 5) {
					rt.model.setRowCount(6);
					sr.model.setRowCount(6);
				} else {
					rt.model.setRowCount(rt_count + 1);
					sr.model.setRowCount(rt_count + 1);
				}

				for (int clear = rt_count; clear < rt.model.getRowCount(); clear++) {
					rt.model.setValueAt("", clear, 0);
					rt.model.setValueAt("", clear, 13);
				}

			}
		}

		/*
		 * Routing Add
		 * 
		 * GateWay Local ID is only 0000 Local ID is Count Number ( Local ID =
		 * rt(Routing)_count )
		 * 
		 * 
		 * 
		 */
		if (e.getSource() == rt.Add) {
			
			String Node_ID = String.format("%04d", Integer.parseInt(rt.Node_ID.getText())); 

			String Parent_ID = String.format("%04d", Integer.parseInt(rt.PIT.getText())); 
			
			String Compare = "";
			
			boolean Ovrelap = false;
			boolean Start = false; // 단말기 추가 하기 위한 플래그
			
			int Blank_row = 0;
			int blank_end = 0;
			
			int Parent_ID_Number = Integer.parseInt(Parent_ID) - 1; // 테이블의

			int Local_count = Integer.parseInt(Node_ID) - 1;
			
			
			
			for (int Ovrelap_cnt = 0; Ovrelap_cnt < rt_count; Ovrelap_cnt++) { // 만큼
				if (rt.model.getValueAt(Ovrelap_cnt, 0).equals(Node_ID)) {Ovrelap = true;}
			}
			
			for (int Blank_Count = 0; Blank_Count < rt.model.getRowCount(); Blank_Count++) { // 만큼
				if ((rt.model.getValueAt(Blank_Count, 0) == null
						|| rt.model.getValueAt(Blank_Count, 0).equals("")) && blank_end == 0) {
					Blank_row = Blank_Count;
					blank_end = 1;
				}
			}
			
			////////////////////////////////////////////////////////////////////////////////////////
			/**************************************************************************************/
			////////////////////////////////////////////////////////////////////////////////////////
			
			
			if ( Parent_ID.equals("") && Node_ID.equals("") ) {
				System.out.println("아무것도 입력하지 않았습니다.");
			
			} else {

				for (int search_count = 0; search_count <= rt_count; search_count++) { 

					if ((rt.model.getValueAt(search_count, 0).equals(Parent_ID) || Parent_ID.equals("0000"))
							&& (rt.model.getValueAt(search_count, 0).equals(Node_ID) == false) && Ovrelap == false) {

						/*
						 * (rt.model.getValueAt(search_count,
						 * 0).equals(Parent_ID) - 입력한 Parent_ID가 테이블의 Local ID 중
						 * 일치하는 것이 있는지 검사 Parent_ID.equals("0000") - 입력한
						 * Parent_ID가 0000일 경우 추가가 가능.
						 * rt.model.getValueAt(search_count, 0).equals(Local_ID)
						 * == false) - 추가 할 Local_ID가 테이블에 없을 경우 추가 가능
						 */

						di[Local_count] = new Device_inf(Node_ID, Parent_ID, Type); 

			
						/*
						 * String Local_ID - 단말기 ID String Parent_ID - 부모 ID int
						 * Tree - 트리 레벨 String[] HOP = new String[10; -
						 * HOP1~HOPn (n <=10) String Type - CCP or Temp_test
						 * (CCP 온도계 또는 온도 감지기) int n_HOP - HOP의 수
						 * 
						 */

						if (Parent_ID.equals("0000")) { 
							
							di[Local_count].Tree = 1; // 트리 레벨 1
							Start = true;

						} else { // 입력한 Parent_ID가 "0000"이 아니고 위의 검색을 만족한 경우

							
							di[Local_count].Tree = Integer.parseInt((rt.model.getValueAt(search_count, 1)).toString())
									+ 1;

							// 검색해서 찾은 단말기의 Tree Level의 값에 1증가하여 저장

							di[Local_count].n_HOP = di[Local_count].Tree - 1;
							// HOP수 = Tree Level -1
							// Add할

							if (di[Local_count].n_HOP >= 1) { 

								di[Local_count].HOP[di[Local_count].n_HOP - 1] = Parent_ID;



								if (di[Parent_ID_Number].Type.equals("CCP")) { 
									
									System.out.println("CCP에는 연결 할 수 없습니다.");
								} else { // 추가할 Parent_ID의 타입이 온도 감지기인 경우
									Start = true; // 추가 가능.
								}

								/*
								 * HOP <DEST>
								 */

								for (int j = di[Local_count].n_HOP - 2; j >= 0; j--) { 
									di[Local_count].HOP[j] = di[Parent_ID_Number].Parent_ID; 
									Parent_ID_Number = Integer.parseInt(di[Parent_ID_Number].Parent_ID) - 1; 
									
								}
							}
						}

					}

				}

				if (Start == true) { // 추가 시작

//					int Tree_Number = di[Local_count].Tree; 
//
//					// System.out.println(TREE[Tree_Number]);
//
//					if (TREE[Tree_Number][tree_count[Tree_Number]] == null) {
//
//						TREE[Tree_Number][0] = new JPanel(); // 트리 생성
//						TREE[Tree_Number][0].setBounds(5, 80 + (75 * (Tree_Number - 1)), 70, 70); 
//
//						TREE[Tree_Number][0].setBackground(null);
//
//						GUI.add(TREE[Tree_Number][0]);
//
//						TREE[Tree_Number][0].addMouseListener(this);
//						TREE[Tree_Number][0].addMouseMotionListener(this);
//
//						// System.out.println(Tree_Number);
//						// System.out.println(tree_count[Tree_Number]);
//
//					} else {
//
//						tree_count[Tree_Number]++; // 트리별 디바이스 수 카운터
//
//						TREE[Tree_Number][tree_count[Tree_Number]] = new JPanel(); 
//						TREE[Tree_Number][tree_count[Tree_Number]].setBackground(null);
//
//						GUI.add(TREE[Tree_Number][tree_count[Tree_Number]]);
//						TREE[Tree_Number][tree_count[Tree_Number]].setBounds(5 + (75 * (tree_count[Tree_Number])),
//								80 + (75 * (Tree_Number - 1)), 70, 70);
//						
//						// System.out.println("!" + Tree_Number);
//						// System.out.println("!" +
//						// tree_count[Tree_Number]);
//					}
//
//					/*
//					 * 
//					 */
//					if (di[Local_count].Type.equals("Temp_test")) { 
//						
//						GUI_NODE[Local_count] = new JLabel(new ImageIcon("gui_node.png")); 
//						
//						TREE[Tree_Number][tree_count[Tree_Number]].add(GUI_NODE[Local_count]);
//						GUI_NODE_Count++;
//					} else if (di[Local_count].Type.equals("CCP")) { // CCP일
//																		// 경우
//						GUI_CCP[Local_count] = new JLabel(new ImageIcon("gui_ccp.png")); 
//						
//						TREE[Tree_Number][tree_count[Tree_Number]].add(GUI_CCP[Local_count]);
//						GUI_CCP_Count++;
//					}
//
//					// System.out.println("@" + Tree_Number);
//					// System.out.println("@" + tree_count[Tree_Number]);
//
//					/*
//					 * System.out.println((int) GUI_GW.getLocation().getX() +
//					 * "  " + (int) GUI_GW.getLocation().getY() + "  " + (int)
//					 * TREE[Tree_Number][tree_count[Tree_Number]].
//					 * getLocation(). getX() + "  " + (int)
//					 * TREE[Tree_Number][tree_count[Tree_Number]].
//					 * getLocation(). getY());
//					 */
//
//					GUI.setVisible(false);
//					GUI.setVisible(true);

					String[] newObject = { Node_ID, String.valueOf(di[Local_count].Tree),
							String.valueOf(di[Local_count].n_HOP), di[Local_count].HOP[0], di[Local_count].HOP[1],
							di[Local_count].HOP[2], di[Local_count].HOP[3], di[Local_count].HOP[4],
							di[Local_count].HOP[5], di[Local_count].HOP[6], di[Local_count].HOP[7],
							di[Local_count].HOP[8], di[Local_count].HOP[9] };

					String[] newObject_serial = { Node_ID, Parent_ID };

					rt.model.insertRow(Blank_row, newObject);

					rt.model.setValueAt("Add", Blank_row, 13);
				
					sr.model.insertRow(Blank_row, newObject_serial);
					
					Add_Count2++;

					rt_count++; // 테이블의 행 갯수;

					
					if (rt_count < 5) {
						rt.model.setRowCount(6);
					} else {
						rt.model.setRowCount(rt_count+1);
					}

					rt.jTable.setDefaultRenderer(Object.class, renderer);
						
					for (int clear = rt_count; clear < rt.model.getRowCount(); clear++) {
						rt.model.setValueAt("", clear, 0);
						rt.model.setValueAt("", clear, 13);
					}
					
					System.out.println("◇◇◇◇◇◇◇◇◇◇◇◇◇◇      추가 할 Dev_ID " + "[" + Local_count + "] - " + di[Local_count].Local_ID
							+ "   |    추가할  Dev_nHop -" + di[Local_count].n_HOP + " ( Add_Count - " + Add_Count2 + ")");
					
					
					loop1:
					for (int Compare_cnt = 1; Compare_cnt < 255; Compare_cnt++) { 
						Compare = String.format("%04d", Compare_cnt);
						int s = 0;
						loop2:
						for (int cnt = 0; cnt < rt_count; cnt++) {
							if (!(rt.model.getValueAt(cnt, 0).equals(Compare))) { s++; }
						}
						
						if( s == rt_count-1 ) { break loop1;}
					}
					
					rt.Node_ID.setToolTipText(Compare);
					
				}

			}

		}

		/*
		 * Routing Modify
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		if (e.getSource() == rt.Modify) {

			if (rt.Modify.getText().equals("수정 취소 (R)")) {

				rt.Modify.setText("수정(C)");
				rt.Clear.setEnabled(true);
				rt.Save.setEnabled(true);

			} else {

				if (rt.row < 255 && rt.row >= 0) {

					rt.Modify_Label.setVisible(true);
					rt.Clear.setEnabled(false);
					rt.Save.setEnabled(false);

					rt.Add.setText("수정(C)");

					rt.Modify.setText("수정 취소 (R)");

					if (!String.format("%04d", rt.PIT.getText()).equals(null)) {
						if (!String.format("%04d", rt.PIT.getText()).equals("")) {

							String Modify_Parent_ID = String.format("%04d", rt.PIT.getText());

						}

					}

				}
			}
		}

		/*
		 * Routing Delete
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		if (e.getSource() == rt.Del) {

			try {

				if (rt_count > 0) {

					if ((rt.jTable.getValueAt(rt.row, 13).equals("Add"))) {

						di[Integer.parseInt(rt.jTable.getValueAt(rt.row, 0).toString()) - 1] = null;
						rt.model.removeRow(rt.row);
						rt_count--;

						if (rt.model.getRowCount() < 5) {
							rt.model.setRowCount(6);
							sr.model.setRowCount(6);
						}

						Local_count = Integer.parseInt(rt.model.getValueAt(rt_count - 1, 0).toString());

						rt.Node_ID.setText(String.valueOf(String.format("%04d", Local_count + 1)));

					} else {

						if (!(rt.jTable.getValueAt(rt.row, 0).equals(""))
								&& !(rt.jTable.getValueAt(rt.row, 0).equals("Del"))) {

							rt.jTable.setValueAt("Del", rt.row, 13);
							rt.jTable.setDefaultRenderer(Object.class, renderer);

							Del_Count2++;

							System.out.println("삭제용 + " + Del_Count2);

						}
					}
				}

				if (rt_count == 0) {

					Local_count = 0;
					rt.Node_ID.setText(String.valueOf(String.format("%04d", Local_count + 1)));
				}

			} catch (Exception row) {
				System.out.println("\n 》》》》》》》》》》》》》》》    Please Click!\n");
			}

			rt.row = 9999;
		}

		/*
		 * Routing Save
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		if (e.getSource() == rt.Save) {

			if (Add_Count2 > 0 || Del_Count2 > 0) {

				String[] msg = new String[255];
				String[] Hop = new String[11];
				String Hop_last = "";

				int msize = 0;
				int ad_b;

				System.out.println("\n################ msg 생성    |  생성 행 수 ----  " + (Add_Count2 + Del_Count2) + "\n");
				System.out.println("ADD - " + Add_Count2);
				System.out.println("DEI - " + Del_Count2);

				for (int cnt = 0; cnt < rt_count; cnt++) {

					if (rt.model.getValueAt(cnt, 13).equals("Add")) {

						Add_Dev_ID[Add_Count] = Integer.parseInt(rt.model.getValueAt(cnt, 0).toString());

						Add_nHOP[Add_Count] = Integer.parseInt(rt.model.getValueAt(cnt, 2).toString());

						msg[msize] = "#>Add" + String.format("%02x", Add_Dev_ID[Add_Count])
								+ Integer.toHexString(Add_nHOP[Add_Count]);

						for (int hop_add = 0; hop_add < Add_nHOP[Add_Count]; hop_add++) {
							Add_HOP[Add_Count][hop_add] = Integer
									.parseInt(rt.model.getValueAt(cnt, hop_add + 3).toString());
							msg[msize] += String.format("%02x", Add_HOP[Add_Count][hop_add]);
						}

						msg[msize] += "\n";
						System.out.println(" 4-- msize - " + msize + " |  msg - " + msg[msize]);

						Add_Count++;
						msize++;

					}
				}

				for (int cnt = 0; cnt < rt_count; cnt++) {

					if (rt.model.getValueAt(cnt, 13).equals("Del")) {

						Del_Dev_ID[Del_Count] = Integer.parseInt(rt.jTable.getValueAt(cnt, 0).toString());

						Del_nHOP[Del_Count] = Integer.parseInt(rt.jTable.getValueAt(cnt, 2).toString());

						System.out.println("------------ Del_Action ");

						System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆      삭제된 Dev_ID - " + Del_Dev_ID[Del_Count]
								+ "   |    삭제된 Dev_nHop -" + Del_nHOP[Del_Count]);

						/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

						msg[msize] = "#>Del" + String.format("%02x", Del_Dev_ID[Del_Count])
								+ Integer.toHexString(Del_nHOP[Del_Count]) + "\n";

						/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

						Del_Count++;

						msize++;

					}
				}

				for (int cnt = 0; cnt < rt_count; cnt++) {

					if (rt.model.getValueAt(cnt, 13).equals("Del")) {

						di[cnt] = null;

						rt.model.removeRow(cnt);
						sr.model.removeRow(cnt);

						cnt--;

						String[] obj = { "", "", "", "", "", "", "", "", "", "", "", "", "", "" };

						rt.model.insertRow(rt_count, obj);
						sr.model.insertRow(rt_count, obj);

					}

				}

				rt.jTable.setDefaultRenderer(Object.class, renderer);

				rt_count -= Del_Count;

				System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆      전체 엔트리 수 - " + rt_count);

				if (rt_count == 0) {

					Local_count = 0000;
					rt.Node_ID.setText(String.valueOf(String.format("%04d", Local_count + 1)));
					rt.model.setRowCount(6);
					sr.model.setRowCount(6);

				} else if (rt_count < 5) {
					rt.model.setRowCount(6);
					sr.model.setRowCount(6);
				} else {

					rt.model.setRowCount(rt_count + 1);
					sr.model.setRowCount(rt_count + 1);

					Local_count = Integer.parseInt(rt.model.getValueAt(rt_count - 1, 0).toString());
					rt.Node_ID.setText(String.valueOf(String.format("%04d", Local_count + 1)));
				}

				for (int clear = rt_count; clear < rt.model.getRowCount(); clear++) {
					rt.model.setValueAt("", clear, 0);
					rt.model.setValueAt("", clear, 13);
				}

				for (int cnt = 0; cnt < rt_count; cnt++) {

					boolean serch = false;

					if (Integer.parseInt(rt.model.getValueAt(cnt, 2).toString()) > 0) {

						rt.model.setValueAt("", cnt, 13);

						for (int hop_up = 0; hop_up < Integer
								.parseInt(rt.model.getValueAt(cnt, 2).toString()); hop_up++) {

							serch = false;

							for (int cnt2 = 0; cnt2 < rt_count; cnt2++) {

								if (!(rt.model.getValueAt(cnt, hop_up + 3).equals(rt.model.getValueAt(cnt2, 0)))) {

									System.out.println("not Search  - ◆ : " + (rt.model.getValueAt(cnt, hop_up + 3)
											+ " ★ : " + rt.model.getValueAt(cnt2, 0)));

									if (serch == false) {

										rt.model.setValueAt("disable", cnt, 13);

									}
								} else {

									System.out.println("!!!!!!! Search !!!!!");

									rt.model.setValueAt("insert", cnt, 13);
									serch = true;
								}

							}

						}

					}

					if (rt.model.getValueAt(cnt, 13).equals("Add")) {

						rt.model.setValueAt("insert", cnt, 13);

					}

				}

				for (int com = 0; com < msize; com++) {

					System.out.println("RT_msg " + com + "번째 줄 -" + msg[com] + " / size - " + msg[com].length() + "\n");

				}

				String[] return_msg = new String[msize];

				for (int ret_cnt = 0; ret_cnt < msize; ret_cnt++) {
					return_msg[ret_cnt] = msg[ret_cnt];
				}

				// RT_Save rt_save = new RT_Save(TIC.socket, Add_Count,
				// Del_Count, return_msg);
				//
				// rt_save.start();
				//
				// while (rt_save.RT_Save == false) {
				//
				// }

				Add_Dev_ID = new int[255];
				Del_Dev_ID = new int[255];
				Add_Count = 0;
				Add_Count2 = 0;
				Del_Count = 0;
				Del_Count2 = 0;

				System.out.println("#>>>>>>>>>> Save Complete!!\n");
			}
		}

		// ^^^
		/*--------------------------------------------Routing Event------------------------------------------------------------*/

	} // actionPerformed End

	/*--------------------------------------------GateWay_Window Method--------------------------------------------------------*/
	/*
	 * Routing Delete
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	public void mousePressed(MouseEvent me) {

		// if (TREE[1][0].contains(new Point(me.getX(), me.getY()))) {
		// // #1 마우스 버튼 누름
		// // 사각형내 마우스 클릭 상대 좌표를 구함
		// // 현재 마우스 스크린 좌표에서 사각형 위치 좌표의 차이를 구함
		//
		// offX = me.getX() - TREE[1][0].getLocation().x;
		// offY = me.getY() - TREE[1][0].getLocation().y;
		//
		// // 드래그 시작을 표시
		// isDragged = true;
		// isSelected = false;
		//
		// }
		//
		// if (GUI.contains(new Point(me.getX(), me.getY()))) {
		//
		// System.out.println(GUI.getLocation().x);
		//
		// offX = me.getX() - GUI.getLocation().x;
		// offY = me.getY() - GUI.getLocation().y;
		//
		// // 드래그 시작을 표시
		// isDragged = true;
		// isSelected = true;
		//
		// }
	}

	public void mouseReleased(MouseEvent me) {
		// // 마우스 버튼이 릴리즈되면 드래그 모드 종료
		// isDragged = false;
	}

	public void mouseDragged(MouseEvent me) {

		// if (isDragged) {
		//
		// GUI_GW_x = me.getX() - offX;
		// GUI_GW_y = me.getY() - offY;
		// }
		//
		// System.out.println(GUI_GW_x + " " + me.getY() + " " + offX);
		//
		// if (isSelected != true && GUI_GW_x > 0 && GUI_GW_y > 70) {
		// TREE[1][0].setLocation(GUI_GW_x, GUI_GW_y);
		// }
		//
		// GUI.setLocation(GUI_GW_x, GUI_GW_y);

	}

	public void mouseMoved(MouseEvent me) {
	}

	public void mouseClicked(MouseEvent me) {

		sr.row = sr.jTable.getSelectedRow();

		int nCh = 0;

		if (!(sr.jTable.getValueAt(sr.row, 0).equals(""))) {

			sr.Local_ID_TF.setText(sr.model.getValueAt(sr.row, 0).toString());
			sr.Parent_ID_TF.setText(sr.model.getValueAt(sr.row, 1).toString());

			for (int sr_cnt = 0; sr_cnt < rt_count; sr_cnt++) {

				if (Integer.parseInt(rt.model.getValueAt(sr_cnt, 2).toString()) > 0) {

					if (sr.model.getValueAt(sr.row, 0).equals(rt.model.getValueAt(sr_cnt,
							Integer.parseInt(rt.model.getValueAt(sr_cnt, 2).toString()) + 2))) {
						nCh++;
					}
				}
			}

			sr.n_Child_TF.setText(String.format("%04d", nCh));

		}

	}

	public void mouseEntered(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}

	// ^^^
	/*--------------------------------------------Mouse Event------------------------------------------------------------*/

	class Table_Set extends Thread {

		@Override
		public void run() {
			try {

				Time_Flag = false;

				System.out.println("\n\n\t\t\t\t\t\t\t\t!--- 테이블 최신화 카운터 시작---!" + "\n");

				while (!Thread.currentThread().isInterrupted()) {

					ArpMacAddressGet AMG = new ArpMacAddressGet();

					try {

						et.model.setNumRows(0);

						for (int AMG_Count = 0; AMG_Count < AMG.IP_Count; AMG_Count++) {

							if (AMG.IP[AMG_Count].length() == 11) {
								IP_Space = "     ";
								MAC_Space = "   ";
							} else if (AMG.IP[AMG_Count].length() == 12) {
								IP_Space = "    ";
								MAC_Space = "   ";
							} else {
								IP_Space = "   ";
								MAC_Space = "   ";
							}

							Connect_IP = AMG.IP;

							String[] newObject = { "  " + AMG.IP[AMG_Count] + IP_Space + AMG.MAC[AMG_Count] + MAC_Space
									+ AMG.Host_Name[AMG_Count] };

							et.model.addRow(newObject);

						}

						Time_Flag = true;

						Thread.sleep(10000);

					} catch (Exception e) {
						System.out.println("\n\n\t\t\t\t\t\t\t\t\t Table Set Error");
					}

					Thread.sleep(20000);

					// System.out.println("\n\t\t\t\t\t\t\t\t\t =-테이블 최신화
					// 완료.-=\n");

				}

			} catch (Exception e) {
				System.out.println("Table set Fail");
			} finally {
				System.out.println("Thread Dead....");
			}
		}

		public void Thread_restart() {

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("\n\n\t\t\t\t\t\t\t\t\t Thread Restart Error");
			}

			thread.interrupt();

		}
	}

	class Thread_Cennect extends Thread {

		int s, Count;

		boolean time_out;

		@Override
		public void run() {
			try {

				System.out.println("\n\n\t\t\t\t\t\t\t\t  연결 상태 확인 시작 (간격 20초) \n");

				while (hostAvailabilityCheck(TIC.socket.getInetAddress().getHostAddress(),
						TIC.socket.getPort()) == true) {

					Thread.sleep(10000);

					System.out.println(
							"\n\n\n\t\t\t\t\t\t\t Gateway - " + TIC.socket.getInetAddress() + "정상 연결 중.....\n");
					System.out.println();

				}

				// setting.clear.setVisible(false);

				// method.View_SearchBox();

				System.out.println("\n\n\n\t\t\t\t\t\t\t\t\t연결 상태 확인 종료.(연결 해제 상태)\n");

				setting.clear.doClick();

			} catch (Exception e) {
				System.out.println("\n\n\t\t\t\t\t\t\t\t\ttime_out Error");
			}
		}

		public boolean hostAvailabilityCheck(String server_addr, int port) {
			try (Socket s = new Socket(server_addr, port)) {
				return true;
			} catch (IOException ex) {
				/* ignore */
			}
			return false;
		}

	}

	class SerialConnect_Check extends Thread {

		CommPortIdentifier portIdentifier;
		String[] compare = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				"", "" };
		int True = 0;

		@Override
		public void run() {

			System.out.println("\n\n\t\t\t\t\t\t\t\t!--- 시리얼 연결 카운터 시작---!" + "\n");

			while (true) {

				try {

					ListPorts lp = new ListPorts();

					returnCOM = new String[lp.i];

					// System.out.println("\t\t\t\t\t\t\t\t 포트 최신화.");

					for (int j = 0; j < lp.i; j++) {
						returnCOM[j] = lp.COM[j];
						// System.out.println("\t\t\t\t\t\t\t\t\t 확인 포트 -- " +
						// returnCOM[j]);
					}

					if (lp.i == 0) {
						returnCOM = new String[1];
						returnCOM[0] = "null";
					}

					for (int cr = 0; cr < returnCOM.length; cr++) {

						if (compare[cr].equals(returnCOM[cr])) {
							True = 1;
							// System.out.println(compare[cr]);
							// System.out.println(returnCOM[cr]);
						} else {
							True = 0;
						}

					}

					if (True == 0) {

						bs.Channel.removeAllItems();
						sr.Channel.removeAllItems();

						for (int rcnt = 0; rcnt < returnCOM.length; rcnt++) {

							bs.Channel.insertItemAt(returnCOM[rcnt], rcnt);
							bs.Channel.setSelectedIndex(0);
							sr.Channel.insertItemAt(returnCOM[rcnt], rcnt);
							sr.Channel.setSelectedIndex(0);

						}
					}

					for (int cr = 0; cr < returnCOM.length; cr++) {

						compare[cr] = returnCOM[cr];

					}

					Thread.sleep(3000);

				} catch (Exception e) {

				}
			}
		}
	}

	class Method {

		void Visable() {
			Zg_inf_Panel.setVisible(true);
			Tab1.setVisible(true);
			Tab2.setVisible(true);
			et.setVisible(false);
			sr.setVisible(false);
			rt.setVisible(false);
			bs.setVisible(true);
			GUI.setVisible(false);
			Tab1.setBackground(blue);
			Tab1.setForeground(Color.white);
			Tab2.setForeground(Color.black);
			Tab2.setBackground(Color.WHITE);
		}

		void View_SearchBox() {

			Zg_inf_Panel.setVisible(false);
			et.setVisible(true);
			rt.setVisible(false);
			sr.setVisible(false);
			Tab1.setForeground(Color.black);
			Tab2.setForeground(Color.black);
			Tab1.setBackground(Color.white);
			Tab2.setBackground(Color.white);
			Tab1.setVisible(false);
			Tab2.setVisible(false);
			setting.E_button.setBounds(20, 50, 60, 60);
			setting.S_button.setBounds(20, 180, 60, 60);
			setting.E_Label.setForeground(Color.black);
			setting.S_Label.setForeground(Color.black);
			setting.E_button.setBorderPainted(false);
			setting.S_button.setBorderPainted(false);

		}

		void View_Ethernet() {

			Zg_inf_Panel.setVisible(true);
			main.setVisible(false);
			setting.E_button.setBorderPainted(true);
			setting.S_button.setBorderPainted(false);
			setting.E_Label.setForeground(blue);
			setting.S_Label.setForeground(Color.black);
			setting.E_button.setBounds(15, 45, 68, 68);
			setting.S_button.setBounds(20, 180, 60, 60);
			Tab1.setBackground(Color.white);
			Tab1.setForeground(Color.black);
			Tab2.setForeground(Color.black);
			Tab2.setBackground(Color.WHITE);
			sr.setVisible(false);
			rt.setVisible(false);
			et.setVisible(true);

		}

		void View_Serial() {
			Zg_inf_Panel.setVisible(false);
			setting.E_button.setBorderPainted(false);
			setting.S_button.setBorderPainted(true);
			setting.S_Label.setForeground(blue);
			setting.E_Label.setForeground(Color.black);
			setting.S_button.setBounds(15, 175, 66, 66);
			setting.E_button.setBounds(20, 50, 60, 60);
			main.setVisible(false);
			et.setVisible(false);
			rt.setVisible(false);
			bs.setVisible(false);
			Tab1.setVisible(false);
			Tab2.setVisible(false);
			sr.setVisible(true);

		}

		void View_Connect_Ethernet() {

			Visable();
			Zg_inf_Panel.setVisible(true);
			setting.E_button.setBorderPainted(true);
			setting.S_button.setBorderPainted(false);
			setting.E_Label.setForeground(blue);
			setting.S_Label.setForeground(Color.black);
			setting.E_button.setBounds(15, 45, 68, 68);
			setting.S_button.setBounds(20, 180, 60, 60);

		}

		void View_Basic_Set() {
			Zg_inf_Panel.setVisible(true);
			GUI.setVisible(false);
			Tab1.setBackground(blue);
			Tab1.setForeground(Color.white);
			Tab2.setForeground(Color.black);
			Tab2.setBackground(Color.WHITE);
			et.setVisible(false);
			sr.setVisible(false);
			rt.setVisible(false);
			bs.setVisible(true);

		}

		void View_Routing() {
			Zg_inf_Panel.setVisible(false);
//			GUI.setVisible(true);
			Tab2.setBackground(blue);
			Tab2.setForeground(Color.white);
			Tab1.setForeground(Color.black);
			Tab1.setBackground(Color.WHITE);
			et.setVisible(false);
			sr.setVisible(false);
			bs.setVisible(false);
			rt.setVisible(true);

		}

	}

}

class Setting extends JPanel {

	JButton E_button, S_button, clear;
	JLabel E_Label, S_Label, IP_Label;
	ButtonGroup bg_set, bg2_main;

	Font f = new Font("맑은 고딕", Font.PLAIN, 15);
	Color blue = new Color(90, 174, 255);

	public Setting() {

		bg_set = new ButtonGroup();

		// 버튼, 라벨 설정
		E_button = new JButton(new ImageIcon("Ethernet.png"));
		S_button = new JButton(new ImageIcon("Serial.png"));
		E_Label = new JLabel("GateWay");
		S_Label = new JLabel("Device");

		clear = new JButton("해제(D)");
		clear.setForeground(Color.white);
		clear.setBackground(Color.gray);

		E_Label.setFont(f);
		S_Label.setFont(f);

		E_button.setBounds(20, 50, 60, 60);
		E_button.setBackground(blue);
		S_button.setBounds(20, 180, 60, 60);
		S_button.setBackground(blue);
		E_Label.setBounds(20, 75, 100, 100);
		S_Label.setBounds(27, 205, 100, 100);

		clear.setBounds(9, 340, 80, 30);

		E_button.setBorderPainted(false);
		S_button.setBorderPainted(false);

		setBorder(BorderFactory.createTitledBorder(""));

		add(E_button);
		add(S_button);
		add(E_Label);
		add(S_Label);
		add(clear);

		bg_set.add(E_button);
		bg_set.add(S_button);

		clear.setVisible(false);

		this.setBounds(5, 30, 100, 385);
		this.setBackground(Color.white);
	}
}

class Main extends JPanel {
	ButtonGroup bg_main;
	JLabel label1, label2;
	Font f;

	Main() {
		bg_main = new ButtonGroup();
		this.setLayout(null);

		f = new Font("맑은 고딕", Font.BOLD, 20);
		setBorder(BorderFactory.createTitledBorder(""));

		label1 = new JLabel("Welcome!");
		label1.setBounds(120, 130, 100, 100);
		label1.setFont(f);

		label2 = new JLabel("i-HACCP 설정 프로그램");
		label2.setBounds(120, 160, 300, 100);
		label2.setFont(f);

		add(label1);
		add(label2);

		this.setBounds(110, 30, 580, 385);
		this.setBackground(Color.white);
	}

}

class Ethernet extends JPanel implements ItemListener, MouseListener {

	int row;

	JButton search, connect;
	JLabel text, IP_Label;
	JCheckBox IP_CheckBox;
	JPanel IP_Input, Table;
	JTextField IP_TextField;
	JTable jTable;
	String Space = "                                      ";
	DefaultTableModel model;

	ButtonGroup bg_main;

	Color blue = new Color(90, 174, 255), Init;
	Font f = new Font("맑은 고딕", Font.BOLD, 15);
	Font Table_f = new Font("돋움체", Font.PLAIN, 20);

	JTextField jtfChat = new JTextField();

	boolean IP_Text = false;

	public Ethernet() {

		this.setLayout(null);

		String columnNames[] = { "IP Address" + Space + "MAC Address" + Space + "호스트 이름" };

		String rowData[][] = {};

		model = new DefaultTableModel(rowData, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		jTable = new JTable(model);

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 단일선택

		jTable.setFont(Table_f);
		jTable.setRowHeight(25);

		Init = jTable.getSelectionBackground();

		JScrollPane sp = new JScrollPane(jTable);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		Table = new JPanel();
		Table.setBackground(Color.lightGray);

		JTextArea ta = new JTextArea();

		IP_CheckBox = new JCheckBox("", true);
		IP_CheckBox.setBackground(Color.white);
		IP_CheckBox.addItemListener(this);

		IP_Input = new JPanel();
		IP_Label = new JLabel("IP주소 입력");
		IP_Input.setBackground(Color.white);

		IP_TextField = new JTextField(12);
		IP_TextField.setBackground(Color.white);
		IP_TextField.setDocument(new JTextFieldLimit(15, 2));

		IP_CheckBox.setSelected(false);
		IP_TextField.setEditable(false);

		search = new JButton("검색(S)");
		search.setBackground(blue);
		search.setForeground(Color.white);
		connect = new JButton("연결(C)");
		connect.setForeground(Color.white);
		connect.setBackground(Color.gray);

		text = new JLabel("검색된 장치");
		text.setFont(f);

		sp.setBounds(2, 2, 546, 256);
		Table.setBounds(15, 60, 550, 260);
		IP_Input.setBounds(5, 340, 250, 30);
		search.setBounds(300, 340, 80, 30);
		connect.setBounds(390, 340, 80, 30);
		text.setBounds(15, 10, 200, 50);

		this.setLayout(null);
		Table.setLayout(null);

		IP_Input.add(IP_CheckBox);
		IP_Input.add(IP_Label);
		IP_Input.add(IP_TextField);
		Table.add(sp);
		add(IP_Input);
		add(search);
		add(connect);
		add(text);
		add(Table);

		ta.setCaretPosition(ta.getDocument().getLength());
		this.setBounds(110, 30, 580, 385);
		this.setVisible(true);
		this.setBackground(Color.white);
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED) {

			IP_TextField.setEditable(false);
			jTable.setEnabled(true);
			jTable.setSelectionBackground(Init);
			jTable.addMouseListener(this);
			IP_Text = false;

		} else {

			try {

				IP_Text = true;
				IP_TextField.setEditable(true);
				jTable.removeMouseListener(this);
				jTable.setSelectionBackground(null);

			} catch (Exception e1) {
				System.out.println("직접 입력 모드입니다.");
			}

		}
	}

	public void mouseClicked(MouseEvent me) {
		row = jTable.getSelectedRow();
		System.out.println("\n" + row + "줄 " + jTable.getValueAt(row, 0) + "\n");
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}

class Serial extends JPanel implements ItemListener {

	ButtonGroup bg_main;

	JPanel DVP, CBox, DV_Data, ZBox, ZBox2, Routing, Zb, Serial, Local, Parent_ID, n_Child, Boadrate, Pan_ID, Target,
			Local2, Routing_P, Zb_P, reset_Panel, Table;

	JLabel DVL, LID, PID, CNC, TL, Z_ChL, Z_OpL, Routing_Title, Zb_Title, Boadrate_L, Local_T, Parent_ID_T, n_Child_T,
			Pan_ID_T, Target_T, Channel_Label, Velocity_Label1, Velocity_Label2, Local2_T, Zb_reset_Label;

	JComboBox Channel, Z_Channel, Z_Output, Boadrate_CB;

	JCheckBox Zb_reset;

	String[] Boadrate_S, Z_OutV, Z_Ch;

	JButton Read, Save, next, Serial_Connect;

	DefaultTableModel model;

	JTable jTable;

	JTextField Local_ID_TF, Parent_ID_TF, n_Child_TF, Pan_ID_TF, Target_TF, Local2_ID_TF;

	Color yellow = new Color(255, 187, 0);

	Color Light_blue = new Color(194, 226, 232);

	Font f = new Font("맑은 고딕", Font.PLAIN, 15);

	Font Table_f = new Font("돋움체", Font.PLAIN, 20);

	Color blue = new Color(90, 174, 255);

	boolean reset = false;

	int j = 0;
	int color = 0;
	int row = 9999;

	Serial() {

		String columnNames[] = { "단말기 ID", "부모 ID" };

		String rowData[][] = new String[0][0];

		model = new DefaultTableModel(rowData, columnNames) {

			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		jTable = new JTable(model);

		jTable.setFont(Table_f);
		jTable.setRowHeight(30);
		jTable.getTableHeader().setReorderingAllowed(false);

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		for (int i = 0; i < 2; i++) {
			jTable.getColumnModel().getColumn(i).setMaxWidth(102);
			jTable.getColumnModel().getColumn(i).setMinWidth(102);
			jTable.getColumnModel().getColumn(i).setWidth(102);
		}

		JScrollPane sp = new JScrollPane(jTable);

		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		sp.setBounds(0, 0, 120, 320);
		Table = new JPanel();
		// Table.setBackground(Color.white);
		Table.setLayout(null);

		Table.setBounds(10, 58, 120, 320);

		Read = new JButton("읽기(R)");
		Read.setForeground(Color.white);
		Read.setBackground(blue);
		Read.setBounds(390, 340, 80, 30);

		Save = new JButton("저장(S)");
		Save.setForeground(Color.white);
		Save.setBackground(Color.gray);
		Save.setBounds(480, 340, 80, 30);

		Local = new JPanel();
		Local.setBackground(null);
		Local_T = new JLabel("Local ID : ");
		Local_ID_TF = new JTextField(5);
		Local_ID_TF.setEnabled(false);
		Local_ID_TF.setDocument(new JTextFieldLimit(5, 2));
		Local_T.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		Local.add(Local_T);
		Local.add(Local_ID_TF);
		Local.setBounds(34, 30, 150, 30);

		Parent_ID = new JPanel();
		Parent_ID.setBackground(null);
		Parent_ID_T = new JLabel("Parent_ID : ");
		Parent_ID_TF = new JTextField(5);
		Parent_ID_TF.setEnabled(false);
		Parent_ID_TF.setDocument(new JTextFieldLimit(5, 2));
		Parent_ID_T.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		Parent_ID.add(Parent_ID_T);
		Parent_ID.add(Parent_ID_TF);
		Parent_ID.setBounds(220, 30, 160, 30);

		n_Child = new JPanel();
		n_Child.setBackground(null);
		n_Child_T = new JLabel("자식 노드 수 : ");
		n_Child_TF = new JTextField(5);
		n_Child_TF.setEnabled(false);
		n_Child_TF.setDocument(new JTextFieldLimit(5, 2));
		n_Child_T.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		n_Child.add(n_Child_T);
		n_Child.add(n_Child_TF);
		n_Child.setBounds(9, 72, 170, 30);

		Pan_ID = new JPanel();
		Pan_ID.setBackground(null);
		Pan_ID_TF = new JTextField(5);
		Pan_ID_TF.setEnabled(false);
		Pan_ID_TF.setDocument(new JTextFieldLimit(5, 3));
		Pan_ID_T = new JLabel("ZB_PAN : ");
		Pan_ID_T.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		Pan_ID.add(Pan_ID_T);
		Pan_ID.add(Pan_ID_TF);
		Pan_ID.setBounds(41, 30, 140, 30);

		Target = new JPanel();
		Target.setBackground(null);
		Target_TF = new JTextField(5);
		Target_TF.setEnabled(false);
		Target_TF.setDocument(new JTextFieldLimit(5, 3));
		Target_T = new JLabel("Target Device : ");
		Target_T.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		Target.add(Target_T);
		Target.add(Target_TF);
		Target.setBounds(0, 70, 180, 30);

		Local2 = new JPanel();
		Local2.setBackground(null);
		Local2_T = new JLabel("Local ID : ");
		Local2_ID_TF = new JTextField(5);
		Local2_ID_TF.setEnabled(false);
		Local2_ID_TF.setDocument(new JTextFieldLimit(5, 3));
		Local2_T.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		Local2.add(Local2_T);
		Local2.add(Local2_ID_TF);
		Local2.setBounds(34, 110, 150, 30);

		//////////////////////////////////////////////////////////////////////////////////////////////// <
		Boadrate = new JPanel();
		Boadrate.setBackground(null);
		Boadrate_L = new JLabel("전송 속도 : ");
		Boadrate_S = new String[14];

		Boadrate_S[0] = "110";
		for (int i = 1; i <= 8; i++) {
			Boadrate_S[i] = String.valueOf(300 * ((int) Math.pow(2, i - 1)));
		}
		for (int i = 1; i <= 5; i++) {
			Boadrate_S[i + 8] = String.valueOf(57600 * ((int) Math.pow(2, i - 1)));
		}

		Boadrate_CB = new JComboBox(Boadrate_S);
		Boadrate_CB.setEnabled(false);
		Boadrate_CB.setSelectedIndex(8);
		Boadrate.add(Boadrate_L);
		Boadrate.add(Boadrate_CB);
		Boadrate.setBounds(195, 110, 150, 35);
		//////////////////////////////////////////////////////////////////////////////////////////////// >

		reset_Panel = new JPanel();
		Zb_reset_Label = new JLabel("공장 초기화");
		Zb_reset = new JCheckBox("", true);
		Zb_reset.setBackground(null);
		Zb_reset.addItemListener(this);
		reset_Panel.setBackground(null);
		reset_Panel.add(Zb_reset);
		reset_Panel.add(Zb_reset_Label);
		reset_Panel.setBounds(305, 0, 100, 30);

		//////////////////////////////////////////////////////////////////////////////////////////////// <

		Z_OutV = new String[19];
		Z_Ch = new String[16];

		for (int i = 0; i <= 18; i++) {

			if (i <= 15) {

				Z_Ch[i] = String.format("%02x", 11 + i) + "      ";
				Z_Ch[i] = Z_Ch[i].toUpperCase();

			}

			Z_OutV[i] = String.format("%02x", 0 + i) + "         ";
			Z_OutV[i] = Z_OutV[i].toUpperCase();

		}

		//////////////////////////////////////////////////////////////////////////////////////////////// >

		this.setLayout(null);

		this.setBounds(110, 30, 580, 385);
		this.setVisible(true);
		this.setBackground(Color.white);

		DVP = new JPanel();
		CBox = new JPanel();
		ZBox = new JPanel();
		ZBox2 = new JPanel();
		DV_Data = new JPanel();
		DV_Data.setBackground(Color.white);
		DV_Data.setLayout(null);

		CBox.setBackground(Color.white);
		ZBox.setBackground(null);
		ZBox2.setBackground(Color.white);
		Routing = new JPanel();
		Routing.setLayout(null);
		Routing.setBackground(Color.white);
		Routing_P = new JPanel();
		Routing_P.setLayout(null);
		Routing_P.setBackground(blue);

		Zb = new JPanel();
		Zb.setLayout(null);
		Zb.setBackground(Color.white);
		Zb_P = new JPanel();
		Zb_P.setLayout(null);
		Zb_P.setBackground(blue);

		Routing_Title = new JLabel("＊ 라우팅  ＊");
		Zb_Title = new JLabel("＊ 지그비  ＊");

		DVL = new JLabel("단말기 ID");

		Channel_Label = new JLabel("시리얼 포트 : ");

		Velocity_Label1 = new JLabel("  시리얼 속도 :");
		Velocity_Label2 = new JLabel(" 115200");
		Velocity_Label2.setFont(new Font("맑은고딕", Font.BOLD, 15));
		Velocity_Label2.setForeground(Color.RED);

		Z_OpL = new JLabel("전송 세기 : ");
		Z_ChL = new JLabel("채널 : ");

		LID = new JLabel("LOCAL ID : ");
		PID = new JLabel("PARENT ID : ");
		CNC = new JLabel("Child Node 수  : ");
		TL = new JLabel("Tree Level : ");

		Channel = new JComboBox();

		Z_Channel = new JComboBox(Z_Ch);
		Z_Output = new JComboBox(Z_OutV);

		//////////////////////////////////////////////////////////////////////////////////////////////// <

		CBox.setBounds(180, 10, 380, 35);
		ZBox.setBounds(220, 30, 120, 35);
		ZBox2.setBounds(180, 70, 180, 35);

		DV_Data.setBounds(125, 60, 147, 180);
		DVL.setForeground(Color.white);
		DVP.setBounds(20, 25, 100, 25);
		DVP.setBackground(yellow);

		Routing_P.setBounds(140, 55, 420, 115);
		Routing.setBounds(1, 1, 418, 113);

		Zb_P.setBounds(140, 175, 420, 160);
		Zb.setBounds(1, 1, 418, 158);

		LID.setBounds(5, 5, 150, 30);
		LID.setFont(f);
		PID.setBounds(5, 30, 150, 30);
		PID.setFont(f);
		CNC.setBounds(5, 55, 150, 30);
		CNC.setFont(f);
		TL.setBounds(5, 80, 150, 30);
		TL.setFont(f);

		Serial_Connect = new JButton("시리얼 연결(C)");
		Serial_Connect.setForeground(Color.white);
		Serial_Connect.setBackground(new Color(140, 140, 140));
		Serial_Connect.setBounds(260, 340, 120, 30);

		//////////////////////////////////////////////////////////////////////////////////////////////// >

		Routing_Title.setBounds(2, 2, 100, 30);
		Zb_Title.setBounds(2, 2, 100, 30);

		//////////////////////////////////////////////////////////////////////////////////////////////// <

		Z_Channel.setEnabled(false);
		Z_Output.setEnabled(false);
		Boadrate_CB.setEnabled(false);

		ZBox.add(Z_ChL);
		ZBox.add(Z_Channel);

		ZBox2.add(Z_OpL);
		ZBox2.add(Z_Output);

		Zb.add(Zb_Title);
		Zb.add(Pan_ID);
		Zb.add(ZBox);
		Zb.add(Target);
		Zb.add(ZBox2);
		Zb.add(Local2);
		Zb.add(Boadrate);
		Zb.add(reset_Panel);
		Zb_P.add(Zb);

		Routing.add(Routing_Title);
		Routing.add(Local);
		Routing.add(Parent_ID);
		Routing.add(n_Child);
		Routing_P.add(Routing);

		CBox.add(Channel_Label);
		CBox.add(Channel);
		CBox.add(Velocity_Label1);
		CBox.add(Velocity_Label2);

		DVP.add(DVL);

		DV_Data.add(LID);
		DV_Data.add(PID);
		DV_Data.add(CNC);
		DV_Data.add(TL);

		add(Read);
		add(Save);
		add(Routing_P);
		add(Zb_P);
		add(DVP);
		add(CBox);
		add(Serial_Connect);

		Table.add(sp);
		add(Table);

		//////////////////////////////////////////////////////////////////////////////////////////////// >
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			Zb_reset_Label.setForeground(Color.lightGray);
			reset = false;
		} else {
			Zb_reset_Label.setForeground(Color.BLACK);
			reset = true;
		}
	}

}

class JTextFieldLimit extends PlainDocument {
	private int limit;
	private boolean toUppercase = false;
	int Type = 0;

	JTextFieldLimit(int limit, int Type) {
		super();
		this.Type = Type;
		this.limit = limit;
	}

	JTextFieldLimit(int limit, boolean upper) {
		super();
		this.limit = limit;
		this.toUppercase = upper;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

		if (str == null) {
			return;
		}

		if ((getLength() + str.length()) <= limit) {
			if (toUppercase) {
				str = str.toUpperCase();
			}

			if (Type == 1) {

				super.insertString(offset, str, attr);

			} else if (Type == 2) {

				if (str.charAt(0) >= '0' && str.charAt(0) <= '9' || str.charAt(0) == '.')
					super.insertString(offset, str, attr);

			} else if (Type == 3) {

				if ((str.charAt(0) >= '0' && str.charAt(0) <= '9') || (str.charAt(0) >= 'a' && str.charAt(0) <= 'f')
						|| (str.charAt(0) >= 'A' && str.charAt(0) <= 'F'))
					super.insertString(offset, str, attr);

			}
		}

	}
}

class MyRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		String status = (String) table.getModel().getValueAt(row, 13);
		if (!isSelected) {

			if (!table.isRowSelected(row)) {

				if ("insert".equals(status)) {

					c.setBackground(new Color(255, 255, 220));// yellow
					c.setForeground(Color.BLACK);

				} else if ("Add".equals(status)) {

					c.setBackground(new Color(220, 220, 255));// blue
					c.setForeground(Color.BLACK);

				} else if ("Del".equals(status)) {

					c.setBackground(new Color(255, 220, 220)); // red
					c.setForeground(Color.BLACK);

				} else if ("disable".equals(status)) {

					c.setBackground(new Color(220, 220, 220));
					c.setForeground(Color.red);

				} else {
					c.setBackground(table.getBackground());
					c.setForeground(table.getForeground());

				}

			}
		}
		return c;

	}

}
