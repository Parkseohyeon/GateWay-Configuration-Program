package GW;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

class Basic_set extends JPanel implements ItemListener {
	
	JButton Serial_Connect , Read, Save;
	ButtonGroup bg_main;

	JCheckBox UDP_CheckBox, DHCP_CheckBox, Zb_reset;
	JComboBox ZB_CH_ComboBox, ZB_TXPWR_Combo, Channel;
	JPanel T_Panel, SIPP, GIPP, PortP, GWP, NMP, Server1, Server2, GateWay1, GateWay2, ZBP, ZBP2, Zb_Chip_P, reset_Panel, Serial,
	ZB_PAN_P, Target_P, Local_P;

	JLabel T_Lable, DHCP_Label, SIP, GIP, Port, GW, NM, simg, gimg, Z_CH_Label, Z_OutV_Label, Channel_Label, Velocity_Label1, Velocity_Label2, Zb_Chip,
	Zb_reset_Label, ZB_PAN, Target, Local, Connect_L;

	JLabel[] SDot = new JLabel[5];
	JLabel[] GDot = new JLabel[5];
	JLabel[] GWDot = new JLabel[5];
	JLabel[] NDot = new JLabel[5];

	JTextField PortT, ZB_PAN_T, Target_T, Local_T; ;

	JTextField[] SIPT = new JTextField[5];
	JTextField[] GIPT = new JTextField[5];
	JTextField[] GWT = new JTextField[5];
	JTextField[] NMT = new JTextField[5];

	JTextField SvT;
	JTextField GWYT;

	Color blue = new Color(90, 174, 255);

	JTextField jtfChat = new JTextField();

	String[] Z_Ch, Z_OutV;

	boolean UDP = true, DHCP = true, reset = true;

	public Basic_set() {
		
		Channel = new JComboBox();
		
		UDP_CheckBox = new JCheckBox("", true);
		UDP_CheckBox.setBackground(Color.white);
		UDP_CheckBox.addItemListener(this);
		
		DHCP_CheckBox = new JCheckBox("", true);
		DHCP_CheckBox.setBackground(Color.white);
		DHCP_CheckBox.addItemListener(this);

		Zb_reset = new JCheckBox("", true);
		Zb_reset.setBackground(Color.white);
		Zb_reset.addItemListener(this);
		
		Z_OutV = new String[18];
		Z_Ch = new String[16];

		Channel_Label = new JLabel("시리얼 포트 : ");
		Connect_L = new JLabel("");
		Velocity_Label1 = new JLabel("  시리얼 속도 :");
		Velocity_Label2 = new JLabel(" 115200");
		Velocity_Label2.setFont(new Font("맑은고딕", Font.BOLD , 15));
		Velocity_Label2.setForeground(Color.red);
		Z_CH_Label = new JLabel("지그비채널 :");
		Z_OutV_Label = new JLabel("지그비출력 :");
		
		ZB_PAN = new JLabel("PAN ID : ");
		ZB_PAN_T = new JTextField(5);
		ZB_PAN_T.setEnabled(false);
		ZB_PAN_T.setDocument(new JTextFieldLimit(5, 3));
		
		Target = new JLabel("Target Device : ");
		Target_T = new JTextField(5);
		Target_T.setEnabled(false);
		Target_T.setDocument(new JTextFieldLimit(5, 3));
	
		Local = new JLabel("Local ID : ");
		Local_T = new JTextField(5);
		Local_T.setEnabled(false);
		Local_T.setDocument(new JTextFieldLimit(5, 3));
		
		for (int i = 0; i <= 17; i++) {

			if (i <= 15) {
				
				Z_Ch[i] = Integer.toHexString(0x0B + i);
				Z_Ch[i] = Z_Ch[i].toUpperCase();
				if (Z_Ch[i].length() == 1) {
					Z_Ch[i] = "0" + Z_Ch[i] + "      ";
				}

			}

			Z_OutV[i] = Integer.toHexString(0x00 + i);
			Z_OutV[i] = Z_OutV[i].toUpperCase();
			if (Z_OutV[i].length() == 1) {
				Z_OutV[i] = "0" + Z_OutV[i] + "         ";
				;
			}

		}

		ZB_CH_ComboBox = new JComboBox(Z_Ch);
		ZB_CH_ComboBox.setEnabled(false);
		ZB_TXPWR_Combo = new JComboBox(Z_OutV);
		ZB_TXPWR_Combo.setEnabled(false);
		
		ZBP = new JPanel();
		ZBP.setBackground(null);
		ZBP2 = new JPanel();
		ZBP2.setBackground(null);
		Serial = new JPanel();
		Serial.setBackground(null);
		
		ZB_PAN_P = new JPanel();
		ZB_PAN_P.setBackground(null);
		Target_P = new JPanel();
		Target_P.setBackground(null);
		Local_P = new JPanel();
		Local_P.setBackground(null);

		
		for (int i = 1; i <= 4; i++) {
			
			SDot[i] = new JLabel(".");
			GDot[i] = new JLabel(".");
			GWDot[i] = new JLabel(".");
			NDot[i] = new JLabel(".");
			SIPT[i] = new JTextField(5);
			SIPT[i].setDocument(new JTextFieldLimit(3, 2));
			GIPT[i] = new JTextField(5);
			GIPT[i].setDocument(new JTextFieldLimit(3, 2));
			GIPT[i].setEnabled(false);
			GWT[i] = new JTextField(5);
			GWT[i].setDocument(new JTextFieldLimit(3, 2));
			GWT[i].setEnabled(false);
			NMT[i] = new JTextField(5);
			NMT[i].setDocument(new JTextFieldLimit(3, 2));
			NMT[i].setEnabled(false);
		
		}
		


		
		simg = new JLabel(new ImageIcon("Server.png"));
		gimg = new JLabel(new ImageIcon("GateWay.png"));

		SvT = new JTextField(10);
		SvT.setDocument(new JTextFieldLimit(18, 1));
		SvT.setText("             Sever");
		SvT.setEnabled(false);

		GWYT = new JTextField(10);
		GWYT.setDocument(new JTextFieldLimit(18, 1));
		GWYT.setText("          GateWay");
		GWYT.setEnabled(false);

		PortT = new JTextField(5);
		PortT.setDocument(new JTextFieldLimit(5, 2));
		
		T_Panel = new JPanel();
		reset_Panel = new JPanel();
		
		T_Lable = new JLabel("태블릿 연결");
		DHCP_Label = new JLabel("DHCP");
		DHCP_Label.setFont(new Font("맑은고딕", T_Lable.getFont().getStyle(), 15));
		
		Zb_reset_Label = new JLabel("공장 초기화");
		
		reset_Panel.setBackground(Color.white);
		T_Panel.setBackground(Color.white);

		
		Server1 = new JPanel();
		Server1.setBackground(blue);
		GateWay1 = new JPanel();
		GateWay1.setBackground(blue);
		Server2 = new JPanel();
		Server2.setBackground(Color.white);
		GateWay2 = new JPanel();
		GateWay2.setBackground(Color.white);
		SIPP = new JPanel();
		SIPP.setBackground(Color.white);
		GWP = new JPanel();
		GWP.setBackground(Color.white);
		GIPP = new JPanel();
		GIPP.setBackground(Color.white);
		NMP = new JPanel();
		NMP.setBackground(Color.white);
		PortP = new JPanel();
		PortP.setBackground(Color.white);

		Zb_Chip_P = new JPanel();
		Zb_Chip_P.setBackground(Color.white);
		Zb_Chip = new JLabel(new ImageIcon("chip.png"));
		
		SIP = new JLabel("IP 주소                          ");
		GIP = new JLabel("IP 주소                          ");
		Port = new JLabel("포트                               ");
		GW = new JLabel("게이트웨이                  ");
		NM = new JLabel("네트워크마스크          ");

		
		Serial_Connect = new JButton("시리얼 연결(C)");
		Serial_Connect.setForeground(Color.white);
		Serial_Connect.setBackground(new Color(140,140,140));
		
		Read = new JButton("읽기(R)");
		Read.setForeground(Color.white);
		Read.setBackground(blue);
		
		Save = new JButton("저장(S)");
		Save.setForeground(Color.white);
		Save.setBackground(Color.gray);

		bg_main = new ButtonGroup();
		this.setLayout(null);
		setBorder(BorderFactory.createTitledBorder(""));

		Serial.setBounds(180, 10, 380, 35);
		Connect_L.setBounds(390, 40, 180, 35);
		Server1.setBounds(20, 40, 122, 31);
		GateWay1.setBounds(20, 175, 122, 31);
		Server2.setBounds(20, 80, 120, 70);
		GateWay2.setBounds(20, 215, 120, 70);
		PortP.setBounds(150, 115, 205, 30);
		GWP.setBounds(150, 220, 420, 30);
		NMP.setBounds(150, 260, 420, 30);
		SIPP.setBounds(150, 75, 420, 30);
		GIPP.setBounds(150, 180, 420, 30);
		Serial_Connect.setBounds(260, 340, 120, 30);
		Read.setBounds(390, 340, 80, 30);
		Save.setBounds(480, 340, 80, 30);
		T_Panel.setBounds(5, 340, 180, 30);
		
		bg_main.add(Read);
		bg_main.add(Save);

		Server1.add(SvT);
		Server2.add(simg);
		GateWay1.add(GWYT);
		GateWay2.add(gimg);
		SIPP.add(SIP);
		PortP.add(Port);
		GIPP.add(GIP);
		GWP.add(GW);
		NMP.add(NM);

		for (int i = 1; i <= 4; i++) {
			SIPP.add(SIPT[i]);
			GIPP.add(GIPT[i]);
			GWP.add(GWT[i]);
			NMP.add(NMT[i]);

			if (i == 4) {

			} else {
				SIPP.add(SDot[i]);
				GIPP.add(GDot[i]);
				GWP.add(GWDot[i]);
				NMP.add(NDot[i]);
			}
		}

		PortP.add(PortT);

		ZB_PAN_P.add(ZB_PAN); ZB_PAN_P.add(ZB_PAN_T);
		Target_P.add(Target); Target_P.add(Target_T);
		Local_P.add(Local); Local_P.add(Local_T);
		
		T_Panel.add(UDP_CheckBox);
		T_Panel.add(T_Lable);
		T_Panel.add(DHCP_CheckBox);
		T_Panel.add(DHCP_Label);
		
		reset_Panel.add(Zb_reset);
		reset_Panel.add(Zb_reset_Label);
		
		add(Read);
		add(Server1);
		add(Server2);
		add(GateWay1);
		add(GateWay2);
		add(Save);
		add(Serial_Connect);
		add(T_Panel);
		Zb_Chip_P.add(Zb_Chip);
		Serial.add(Channel_Label);
		Serial.add(Channel);
		Serial.add(Velocity_Label1);
		Serial.add(Velocity_Label2);
		
		ZBP.add(Z_CH_Label);
		ZBP.add(ZB_CH_ComboBox);
		
		ZBP2.add(Z_OutV_Label);
		ZBP2.add(ZB_TXPWR_Combo);

		add(Serial);
		add(Connect_L);
		add(SIPP);
		add(PortP);
		add(GIPP);
		add(GWP);
		add(NMP);
		add(ZBP);

		this.setBounds(110, 30, 580, 385);
		this.setVisible(true);
		this.setBackground(Color.white);
		
	}

	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();

		if( source == UDP_CheckBox ) { 
			
			if (e.getStateChange() == ItemEvent.DESELECTED ) {
				T_Lable.setForeground(Color.gray);
				UDP = false;
			} else {
				T_Lable.setForeground(Color.black);
				UDP = true;
			}
		}
		
		if( source == DHCP_CheckBox ) { 
			
			if (e.getStateChange() == ItemEvent.DESELECTED ) {
				DHCP_Label.setForeground(Color.gray);
				DHCP = false;
			} else {
				DHCP_Label.setForeground(Color.black);
				DHCP = true;
			}
		}
		
		if( source == Zb_reset ) { 
			
			if (e.getStateChange() == ItemEvent.DESELECTED ) {
				Zb_reset_Label.setForeground(Color.gray);
				reset = false;
			} else {
				Zb_reset_Label.setForeground(Color.black);
				reset = true;
			}
		}	
		
	}
}

class Routing extends JPanel implements MouseListener {

	JPanel P1, P2, P3, L1, L2, L3, Table;

	JButton Del, Save, Add, Tab, Clear, Modify;

	JLabel GWL, DL, GW_DL, LI, LI2, PI, Modify_Label;

	JTextField GW_Local_ID, Node_ID, PIT, Modify_TextField;

	JTable jTable;

	JRadioButton TI, CCP;

	ButtonGroup bg_main;

	String Space = "                                       ";

	DefaultTableModel model;

	Color blue = new Color(90, 174, 255);
	Color yellow = new Color(255, 187, 0);

	Font Table_f = new Font("돋움체", Font.PLAIN, 20);
	Font Tab_f = new Font("맑은고딕", Font.PLAIN, 3);

	JTextField jtfChat = new JTextField();

	int row = 9999;

	Routing() {

		L1 = new JPanel();
		L1.setBackground(Color.white);

		L2 = new JPanel();
		L2.setBackground(Color.white);

		L3 = new JPanel();
		L3.setBackground(Color.white);

		P1 = new JPanel();
		P1.setBackground(yellow);

		P2 = new JPanel();
		P2.setBackground(yellow);

		P3 = new JPanel();
		P3.setBackground(yellow);

		Table = new JPanel();
		Table.setBackground(Color.white);
		Table.setLayout(null);

		GWL = new JLabel("게이트웨이");
		GWL.setForeground(Color.white);
		DL = new JLabel("단말기");
		DL.setForeground(Color.white);
		GW_DL = new JLabel("게이트웨이-단말기 경로");
		GW_DL.setForeground(Color.white);
		LI = new JLabel("    LOCAL ID : ");
		LI2 = new JLabel("    LOCAL ID : ");
		PI = new JLabel("    Parent ID : ");
		
		Modify_Label = new JLabel("수정할 부모 ID를 입력하세요 : ▲");
		Modify_Label.setBounds(170, 150, 150, 25);
		Modify_Label.setVisible(false);

		GW_Local_ID = new JTextField(6);

		GW_Local_ID.setDocument(new JTextFieldLimit(4, 2));
		GW_Local_ID.setText("0000");
		GW_Local_ID.setEditable(false);

		Node_ID = new JTextField(6);
		Node_ID.setDocument(new JTextFieldLimit(4, 2));
		Node_ID.setEditable(true);
		Node_ID.setText("0001");

		PIT = new JTextField(6);
		PIT.setDocument(new JTextFieldLimit(4, 2));
		PIT.setText("");

		TI = new JRadioButton("    온도 감시기          ");
		TI.setBackground(Color.white);

		CCP = new JRadioButton("    CCP 온도계");
		CCP.setBackground(Color.white);

		Add = new JButton("추가(A)");
		Add.setForeground(Color.white);
		Add.setBackground(blue);

		Del = new JButton("삭제(D)");
		Del.setForeground(Color.white);
		Del.setBackground(blue);

		Save = new JButton("저장(S)");
		Save.setForeground(Color.white);
		Save.setBackground(Color.gray);
		
		Modify = new JButton("수정(M)");
		Modify.setForeground(Color.white);
		Modify.setBackground(Color.gray);
		
		Clear = new JButton("초기화(C)");
		Clear.setForeground(Color.white);
		Clear.setBackground(Color.gray);

		Tab = new JButton(">");
		Tab.setForeground(Color.white);
		Tab.setFont(Tab_f);
		Tab.setBackground(blue);

		bg_main = new ButtonGroup();
		this.setLayout(null);

		setBorder(BorderFactory.createTitledBorder(""));

		String columnNames[] = { "단말기 ID", "Tree Level", "Hop 수", "HOP1", "HOP2", "HOP3", "HOP4", "HOP5", "HOP6",
				"HOP7", "HOP8", "HOP9", "HOP10", "state" };

		String rowData[][] = { { "", "", "", "", "", "", "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "", "", "", "", "", "", "" },
				{ "", "", "", "", "", "", "", "", "", "", "", "", "" }, { "", "", "", "", "", "", "", "", "", "", "", "", "" }
				,{ "", "", "", "", "", "", "", "", "", "", "", "", "" },{ "", "", "", "", "", "", "", "", "", "", "", "", "" }};

		model = new DefaultTableModel(rowData, columnNames) {

			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		jTable = new JTable(model);

		jTable.setFont(Table_f);
		jTable.setRowHeight(24);
		jTable.getTableHeader().setReorderingAllowed(false);

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTable.addMouseListener(this);

		for (int i = 0; i < 2; i++) {
			jTable.getColumnModel().getColumn(i).setMaxWidth(74);
			jTable.getColumnModel().getColumn(i).setMinWidth(74);
			jTable.getColumnModel().getColumn(i).setWidth(74);
		}

		for (int i = 2; i < 13; i++) {
			jTable.getColumnModel().getColumn(i).setMaxWidth(62);
			jTable.getColumnModel().getColumn(i).setMinWidth(62);
			jTable.getColumnModel().getColumn(i).setWidth(62);
		}

		jTable.getColumnModel().getColumn(13).setMaxWidth(0);
		jTable.getColumnModel().getColumn(13).setMinWidth(0);
		jTable.getColumnModel().getColumn(13).setWidth(0);
		

		JScrollPane sp = new JScrollPane(jTable);

		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		P1.setSize(50, 30);
		P2.setBounds(18, 70, 75, 25);
		P3.setBounds(18, 150, 150, 25);

		Table.setBounds(18, 180, 538, 150);
		sp.setBounds(2, 2, 536, 146);

		L1.setBounds(5, 20, 255, 35);
		L2.setBounds(120, 65, 250, 35);
		L3.setBounds(95, 100, 430, 35);

		Clear.setBounds(160, 340, 100, 30);
		Modify.setBounds(270, 340, 100, 30);
		Del.setBounds(390, 340, 80, 30);
		Save.setBounds(480, 340, 80, 30);
		Tab.setBounds(550, 0, 30, 30);

		P1.add(GWL);
		P2.add(DL);
		P3.add(GW_DL);

		Table.add(sp);

		L1.add(P1);
		L1.add(LI);
		L1.add(GW_Local_ID);
		L2.add(TI);
		L2.add(CCP);
		L3.add(LI2);
		L3.add(Node_ID);
		L3.add(PI);
		L3.add(PIT);
		L3.add(new JLabel("            "));
		L3.add(Add);

		bg_main.add(Del);
		bg_main.add(Save);
		
		add(Modify);
		add(Clear);
		add(Del);
		add(Save);
		add(L1);
		add(L2);
		add(Table);
		add(L3);
		add(P2);
		add(P3);
		add(Tab);

		this.setBounds(110, 30, 580, 385);
		this.setVisible(true);
		this.setBackground(Color.white);
	}

	String Local_ID() {

		String Local_ID = "";

		return Local_ID;
	}

	public void mouseClicked(MouseEvent me) {

		row = jTable.getSelectedRow();

		System.out.println("\n" + row + "줄");
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

