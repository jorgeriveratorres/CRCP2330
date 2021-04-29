package ak.hack;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Asemblaje {

	Map<String, Integer> map = new HashMap<>(60);
	
	BufferedReader br;

	char A0 = 0;
	char C1 = 1;
	char L2 = 2;

	//16-bits
	int currentAddress = 16;
	int currentLine = 0;

	public static void main(String[] args) {
		
		if(args.length == 0) {
			System.exit(0);
		}

		Asemblaje asemblaje = new Asemblaje(args[0]);
		String line;

		try {
			FileWriter newFile = new FileWriter("out.hack");
			while(true) {
				line = asemblaje.parse();
				if (line == null) {
					newFile.close();
					return;
				}
				else {
					newFile.write(line);
					newFile.write("\n");
				}
			}
		}
		catch (Exception exception) {
			System.out.println("File could not be completed " + exception);
		}
	}

	public Asemblaje(String file) {

		try{
			br = new BufferedReader(new FileReader(file));
			map.put("SP", 0);
			map.put("LCL", 1);
			map.put("ARG", 2);
			map.put("THIS", 3);
			map.put("THAT", 4);
			map.put("SCREEN", 16384);
			map.put("KBD",24576);
			map.put("R0",0);
			map.put("R1",1);
			map.put("R2",2);
			map.put("R3",3);
			map.put("R4",4);
			map.put("R5",5);
			map.put("R6",6);
			map.put("R7",7);
			map.put("R8",8);
			map.put("R9",9);
			map.put("R10",10);
			map.put("R11",11);
			map.put("R12",12);
			map.put("R13",13);
			map.put("R14",14);
			map.put("R15",15);

			firstPass();
			closeReader();

			br = new BufferedReader(new FileReader(file));
		}
		catch (Exception e) {
			System.out.println("Mapping Issues: " + e);
		}
	}

	public char commandToBinary(String line) throws Exception {
		String command = line.replaceAll(".*=", "");
		command = command.replaceAll(";.*", "");

		switch(command) {
			case "0": 
				return 0b0101010;
			case "1":
				return 0b0111111;
			case "-1":
				return 0b0111010;
			case "D":
				return 0b0001100;
			case "A":
				return 0b0110000;
			case "!D":
				return 0b0001101;
			case "!A":
				return 0b0110001;
			case "-D":
				return 0b0001111; 
			case "-A":
				return 0b0110011;

			case "D+1":
				return 0b0011111;
			case "A+1":
				return 0b0110111;
			case "D-1":
				return 0b0001110;
			case "A-1":
				return 0b0110010;
			case "D+A":
				return 0b0000010;
			case "D-A":
				return 0b0010011;
			case "A-D":
				return 0b0000111;
			case "D&A":
				return 0b0000000;
			case "D|A":
				return 0b0010101;

			case "M": 
				return 0b1110000;
			case "!M":
				return 0b1110001;
			case "-M":
				return 0b1110011;
			case "M+1":
				return 0b1110111;
			case "M-1":
				return 0b1110010;
			case "D+M":
				return 0b1000010;
			case "D-M":
				return 0b1010011;
			case "M-D":
				return 0b1000111;
			case "D&M":
				return 0b1000000;
			case "D|M":
				return 0b1010101;
			default:
				throw new Exception("Command could not be converted");
				throw new Exception("Error");
		}
	}

	public char jump(String line) {
		if(line.indexOf(';') == -1) {
			return 0;
		}

		String jump = line.replaceAll(".*;", "");

		switch(jump) {
			case "JGT":
				return 0b001;
			case "JEQ":
				return 0b010;
			case "JGE":
				return 0b011;
			case "JLT":
				return 0b100;
			case "JNE":
				return 0b101;
			case "JLE":
				return 0b110;
			case "JMP":
				return 0b111;
			default:
				return 0;
		}
	}

	public void firstPass() throws Exception {

		String current = next();

		while(current != null){
			if(type(current) == L2) {
				if(map.put(current.substring(1, current.length() - 1), currentLine) != null) {
					return;
				}
			}
			else {
				currentLine += 1;
			}
			current = next();
		}
	}

	public String next() throws IOException {
		String current;

		while(true) {
			current = br.readLine();

			if (current == null) {
				closeReader();
				return null;
			}

			current = current.replaceAll("\\s","");
			current = current.replaceAll("//.*","");

			if(current.length() == 0){
				continue;
			}
			return current;
		}
	}

	public char type(String line){
		if(line.charAt(0) == '@') {
			return A0;
		}
		else {
			if(line.charAt(0) == '(') {
				return L2;
			}
			else {
				return C1;
			}
		}
	}

	public char to(String line){
		if (line.indexOf('=') == -1) {
			return 0;
		}

		String newLine = line.replaceAll("=.*", "");
		char result = 0;

		if (newLine.indexOf('A') != -1) {
			result |= 4;
		}

		if (newLine.indexOf('D') != -1) {
			result |= 2;
		}

		if (newLine.indexOf('M') != -1) {
			result |= 1;
		}

		return result;
	}

}	public String parse() throws Exception {
		String current = next();

		while(current != null && type(current) == L2)
		 {
			current = next();
		}


		if (current == null) 
		{
			return null;
		}

		if (type(current) == A0) {
			current = current.substring(1);

			if(current.charAt(0) < '0' || current.charAt(0) > '9') 
			{

				Integer address = map.get(current);
				if (address == null) {
					address = currentAddress;
					map.put(current, address);
					currentAddress += 1;
				}
				return String.format(Integer.toBinaryString(address)).replace("", "0");
			}

			else 
			{
				return String.format(Integer.toBinaryString(Integer.parseInt(current))).replace("", "0");
			}
		}

		int bin = 0b1110000000000000 + (commandToBinary(current) << 6) + (to(current) << 3) + jump(current);

		return String.format(Integer.toBinaryString(bin)).replace("", "0");
	}

	public void closeReader()
	 {

		try{

			if(br != null) {
				br.close();
			}
	}
}
