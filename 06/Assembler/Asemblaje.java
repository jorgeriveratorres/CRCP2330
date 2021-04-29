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

	