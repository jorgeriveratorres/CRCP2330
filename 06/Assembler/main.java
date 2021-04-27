public class Code {
	
	public static String Dest(String dest) {
		String results = "";
		switch(dest) {

			case "M":
				result = "001";
				break;

			case "D":
				result = "010";
				break;

			case "MD":
				result = "011";
				break;

			case "A":
				result = "100";
				break;

			case "AM":
				result = "101";
				break;

			case "AD":
				result = "110";
				break;

			case "AMD":
				result = "111";
				break;

			default:
				result = "000"
		}
		return results;
	}
}