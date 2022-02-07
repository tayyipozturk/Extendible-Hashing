import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CengPokeParser {

	public static ArrayList<CengPoke> parsePokeFile(String filename)
	{
		ArrayList<CengPoke> pokeList = new ArrayList<>();

		String curline;
		String[] data;

		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((curline = br.readLine()) != null){
				data = curline.split("\t");
				System.out.println(curline);
				pokeList.add(new CengPoke(Integer.parseInt(data[1]), data[2], data[3],data[4]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pokeList;
	}
	
	public static void startParsingCommandLine() throws IOException
	{
		Scanner inp = new Scanner(System.in);

		while(inp.hasNextLine()){
			String nextLine = inp.nextLine();
			String[] data = nextLine.split("\t");
			String command = data[0];
			if(command.equals("quit")){
				break;
			}
			else if(command.equals("add")){
				CengPoke newPoke = new CengPoke(Integer.parseInt(data[1]),
												data[2],
												data[3],
												data[4]);
				CengPokeKeeper.addPoke(newPoke);
			}
			else if(command.equals("search")){
				CengPokeKeeper.searchPoke(Integer.parseInt(data[1]));
			}
			else if(command.equals("delete")){
				CengPokeKeeper.deletePoke(Integer.parseInt(data[1]));
			}
			else if(command.equals("print")){
				CengPokeKeeper.printEverything();
			}
		}
	}
}
