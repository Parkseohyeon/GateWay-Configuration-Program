package GW;

import javax.comm.CommPortIdentifier;

class Device_inf {

	String Local_ID;
	String Parent_ID;
	int Tree;
	String[] HOP = new String[10];
	String Type;

	int n_HOP;

	Device_inf(String Local_ID, String Parent_ID, String Type) {

		this.Local_ID = Local_ID;
		this.Parent_ID = Parent_ID;
		this.Type = Type;

		for(int i=0; i<10; i++ ) {
			HOP[i] = "";
		}

	}
}

