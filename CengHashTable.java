import java.util.ArrayList;
import java.util.List;

public class CengHashTable {

	private ArrayList<CengHashRow> table;
	private int globalDepth;
	private int rowCount;
	private int hashMod;
	private int bucketSize;

	public CengHashTable()
	{
		table = new ArrayList<>();
		globalDepth= 0;
		rowCount = 1;
		hashMod = CengPokeKeeper.getHashMod();
		bucketSize = CengPokeKeeper.getBucketSize();
		CengBucket bucket = new CengBucket(0,0,false ,bucketSize);
		CengHashRow row = new CengHashRow("0", bucket);
		table.add(row);
	}

	public CengHashTable(int globalDepth, int rowCount, int hashMod, int bucketSize){
		table = new ArrayList<>();
		this.globalDepth = globalDepth;
		this.rowCount = rowCount;
		this.hashMod = hashMod;
		this.bucketSize = bucketSize;
	}

	public void deletePoke(Integer pokeKey)
	{
		String hashVal = hash(pokeKey);
		String hp = hashVal.substring(0,globalDepth);
		if(globalDepth == 0){
			hp = "0";
		}
		int index = Integer.parseInt(hp,2);

		table.get(index).getBucket().deletePoke(pokeKey);
		int i;
		int empty=0;
		for(i=0;i<rowCount;i++){
			if(table.get(i).getBucket().isEmpty()){
				empty++;
			}
		}

		System.out.println("\"delete\": {\n" +
				"\t\"emptyBucketNum\": " + empty + "\n}");
	}

	public void addPoke(CengPoke poke)
	{
		String hashVal = hash(poke.pokeKey());
		int index = 0;
		if(globalDepth>=1){
			index = Integer.parseInt(hashVal.substring(0, globalDepth),2);
		}
		CengBucket target = table.get(0).getBucket();
		if(rowCount>=1) {
			target = table.get(index).getBucket();
		}

		if(!target.isFull()){
			target.addPoke(poke);
		}
		else{
			if(target.getDepth()<globalDepth){	//split
				int bDepth = target.getDepth();
				int interval = (int)(Math.pow(2,globalDepth-bDepth));
				int partStart = (index/interval)*(interval);
				int bCount = target.getCount();
				int k;
				CengBucket save = table.get(index).getBucket();
				for(k=partStart;k<partStart+interval;k+=interval/2){
					CengBucket newBucket = new CengBucket(bDepth+1, 0, save.isVisited(), save.getSize());
					int m;
					for(m=k;m<k+interval/2;m++){
						table.get(m).setBucket(newBucket);
					}
				}
				for(k=0;k<bCount;k++){
					CengPoke p = save.pokeAtIndex(k);
					addPoke(p);
				}
				addPoke(poke);
			}
			else{
				doubleSize();
				hashVal = hash(poke.pokeKey());
				index = Integer.parseInt(hashVal.substring(0, globalDepth),2);

				target = table.get(index).getBucket();
				int d = target.getDepth();
				CengBucket newBucket = new CengBucket(d+1, 0, false, bucketSize);
				CengBucket n1 = new CengBucket(d+1, 0, false, bucketSize);
				CengHashRow old;
				table.get(index).setBucket(newBucket);
				if(index % 2 == 0){
					old = table.get(index+1);
				}
				else{
					old = table.get(index-1);
				}
				int j=0;
				while(j<old.getBucket().getCount()){
					CengPoke p = old.getBucket().pokeAtIndex(j);
					String h2 = hash(p.pokeKey());
					if(Integer.parseInt(h2.substring(0,globalDepth)) == index){
						table.get(index).getBucket().addPoke(p);
					}
					else{
						n1.addPoke(p);
					}
					j++;
				}
				old.setBucket(n1);
				addPoke(poke);
			}
		}
	}

	public void searchPoke(Integer pokeKey)
	{
		String tag = "\"search\": {";
		String hashVal = hash(pokeKey);
		int index;
		String hp;
		if(globalDepth == 0){
			hp = "0";
		}
		else{
			hp = hashVal.substring(0,globalDepth);
		}
		index = Integer.parseInt(hp, 2);

		CengBucket bucket = table.get(index).getBucket();
		int bSize = bucket.getCount();
		int localDepth = bucket.getDepth();
		int exist = 0;
		int i;
		for(i=0; i<bSize;i++){
			if(bucket.pokeAtIndex(i).pokeKey() == pokeKey){
				exist = 1;
				break;
			}
		}
		if(exist == 1){
			int rows = (int)(Math.pow(2, globalDepth-localDepth));
			int partStart = (index/rows)*(rows);
			for(i=partStart;i<partStart+rows;i++){
				tag+= "\n" + printRow(table.get(i));
				if(i!=partStart+rows-1){
					tag += ",";
				}
			}
		}


		tag += "\n}";

		System.out.println(tag);
	}

	public void print()
	{
		String tag = ("\"table\": {");
		int i;
		for(i=0;i<rowCount;i++){
			tag += "\n" + printRow(table.get(i));
			if(i!=rowCount-1){
				tag += ",";
			}
		}
		tag += "\n}";

		System.out.println(tag);
	}

	public int prefixBitCount()
	{
		return globalDepth;
	}

	public int rowCount()
	{
		return rowCount;
	}

	public CengHashRow rowAtIndex(int index)
	{
		return table.get(index);
	}

	// Own Methods
	public String hash(Integer key){
		int mod = CengPokeKeeper.getHashMod();
		int digits = (int)(Math.log(mod)/Math.log(2));
		String hashValue;
		int code = key % mod;
		hashValue = Integer.toBinaryString(code);
		while(hashValue.length()<digits){
			hashValue = "0" + hashValue;
		}
		return hashValue;
	}


	public void doubleSize(){
		ArrayList<CengHashRow> newTable = new ArrayList<>();
		if(globalDepth==0){
			CengHashRow row = new CengHashRow("1", table.get(0).getBucket());
			table.add(row);
		}
		else{
			int i;
			for(i=0;i<rowCount;i++){
				String pre = table.get(i).getPrefix();
				CengHashRow row = new CengHashRow(pre+"1", table.get(i).getBucket());
				table.get(i).setPrefix(pre+"0");
				newTable.add(table.get(i));
				newTable.add(row);
			}
			this.table = newTable;
		}
		rowCount *= 2;
		globalDepth++;
	}

	public int getGlobalDepth(){
		return globalDepth;
	}

	public String printPoke(CengPoke x){
		String hashVal = hash(x.pokeKey());
		String tag = ("\t\t\t\t\"poke\": {\n" +
				"\t\t\t\t\t\"hash\": " + hashVal + ",\n" +
				"\t\t\t\t\t\"pokeKey\": " + x.pokeKey() + ",\n" +
				"\t\t\t\t\t\"pokeName\": " + x.pokeName() + ",\n" +
				"\t\t\t\t\t\"pokePower\": " + x.pokePower() + ",\n" +
				"\t\t\t\t\t\"pokeType\": " + x.pokeType() + "\n" +
				"\t\t\t\t}");
		return tag;
	}

	public String printRow(CengHashRow x){
		CengBucket bucket = x.getBucket();
		String prefix = x.getPrefix();
		String tag = ("\t\"row\": {\n" +
				"\t\t\"hashPref\": " + prefix + ",\n" +
				"\t\t\"bucket\": {\n" +
				"\t\t\t\"hashLength\": " +x.getBucket().getDepth() + ",\n" +
				"\t\t\t\"pokes\": [");

		int i;
		int size = bucket.pokeCount();
		for(i=0;i<size;i++){
			tag += "\n" + printPoke(bucket.getBucket().get(i));
			if(i!=size-1){
				tag += ",";
			}
		}
		tag += "\n" +
				"\t\t\t]\n" +
				"\t\t}\n" +
				"\t}";
		return tag;
	}
}
