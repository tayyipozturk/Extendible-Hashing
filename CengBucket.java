import java.util.ArrayList;
import java.util.List;

public class CengBucket {

	// GUI-Based Methods
	// These methods are required by GUI to work properly.
	private ArrayList<CengPoke> bucket;
	private String prefix;
	private int depth;
	private int size;
	private int count;
	private boolean isVisited;
	private static int number;
	private int my;

	public CengBucket(int depth, int count, boolean isVisited, int size)
	{
		//bucket = createList();
		bucket = new ArrayList<CengPoke>();
		this.depth = depth;
		this.size = size;
		this.count = count;
		this.isVisited = isVisited;
		my = number;
		number++;
	}
	
	public int pokeCount()
	{
		return count;
	}
	
	public CengPoke pokeAtIndex(int index)
	{
		return bucket.get(index);
	}
	
	public int getHashPrefix()
	{
		return prefix.length();
	}
	
	public Boolean isVisited()
	{
		return isVisited;
	}

	public boolean isEmpty(){
		return (count == 0);
	}

	public boolean isFull(){
		return (size == count);
	}

	public void addPoke(CengPoke poke){
		bucket.add(poke);
		count++;
	}

	public void deletePoke(Integer pokeKey){
		int i;
		for(i=0;i<count;i++){
			if(bucket.get(i).pokeKey() == pokeKey){
				bucket.remove(i);
				count--;
			}
		}
	}

	public void setPrefix(String prefix){
		this.prefix = prefix;
	}

	public List<CengPoke> getBucket(){
		return bucket;
	}

	public void setDepth(int depth){
		this.depth = depth;
	}

	public int getDepth(){
		return depth;
	}

	public int getSize(){
		return size;
	}

	public int getCount(){
		return count;
	}
}
