import java.util.List;

public class CengHashRow {

	// GUI-Based Methods
	// These methods are required by GUI to work properly.

	private String prefix;
	private CengBucket bucket;

	public CengHashRow(String prefix, CengBucket bucket){
		this.prefix = prefix;
		this.bucket = bucket;
	}

	public CengHashRow(String prefix){
		this.prefix = prefix;
	}

	public String hashPrefix()
	{
		return prefix;
	}
	
	public CengBucket getBucket()
	{
		return bucket;
	}
	
	public boolean isVisited()
	{
		return bucket.isVisited();
	}

	
	// Own Methods
	public String getPrefix(){
		return prefix;
	}
	public void setPrefix(String x){
		prefix = x;
	}
	public void setBucket(CengBucket bucket){
		this.bucket = bucket;
	}
}
