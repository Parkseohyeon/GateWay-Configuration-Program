package GW;

public class Casting {

	public static String HEX_CHARACTERS="0123456789ABCDEF";

	
	public byte[] intToByteArray(int value, int n) {
		byte[] byteArray = new byte[4];

		if (n == 1) {
			byteArray[0] = (byte) (value & 0xFF);
		}
		if (n == 4) {
			byteArray[0] = (byte) (value & 0xFF);
			byteArray[1] = (byte) ((value >> 8) & 0xFF);
			byteArray[2] = (byte) ((value >> 16) & 0xFF);
			byteArray[3] = (byte) ((value >> 24) & 0xFF);
		}
		return byteArray;
	}

	public static byte[] longToByteArray(long l) {

		byte[] b = new byte[4];

		b[3] = (byte) (l);
		l >>>= 8;
		b[2] = (byte) (l);
		l >>>= 8;
		b[1] = (byte) (l);
		l >>>= 8;
		b[0] = (byte) (l);

		return b;
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	public int hexTodec(String hexValue) {

		hexValue = hexValue.toUpperCase();

		int decimalResult = 0;

		for (int i = 0; i < hexValue.length(); i++) {

			char digit = hexValue.charAt(i);
			int digitValue = HEX_CHARACTERS.indexOf(digit);
			decimalResult = decimalResult * 16 + digitValue;
		}
		return decimalResult;
	}

	public static String stringToHex(String s) {
	    String result = "";
	    
	    for (int i = 0; i < s.length(); i++) {
	      result += String.format("%02X ", (int) s.charAt(i));
	    }
	    System.out.println(result);
	    return result;
	  }
	
}
