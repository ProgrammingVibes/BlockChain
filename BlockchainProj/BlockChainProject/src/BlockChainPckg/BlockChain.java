package BlockChainPckg; 
import java.util.ArrayList;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import java.util.Random;

class Transaction{
	String fromAddress;
	String toAddress;
	double transferAmount;
	public Transaction(String fromAddress, String toAddress, double transferAmount) {
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.transferAmount = transferAmount;
	}
	@Override
	public String toString() {
		return "Transaction [fromAddress=" + fromAddress + ", toAddress=" + toAddress + ", transferAmount="
				+ transferAmount + "]";
	}
	
	
}
class Block{
	long timestamp;
	String hash;
	Transaction transaction;
	String previousHash;
	long nonce=0;
	public Block(Transaction t){
		this.transaction=t;
	}

	String calculateHash(int arrayValue){
		if (this.previousHash==null){
			this.previousHash="null";
		}
		String blockInfo=arrayValue+this.timestamp+this.transaction.toString()+this.previousHash.toString()+nonce;
		byte[] inputBytes=blockInfo.getBytes();
		String hashValue = null;
		MessageDigest m = null;
		try{
			m= MessageDigest.getInstance("SHA-256");
			m.update(inputBytes);
			byte[] digestedBytes=m.digest();
			hashValue=DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
		}catch(Exception e){}
		
		return hashValue;
	}
}
public class BlockChain {
	ArrayList<Block> blockChain=new ArrayList<Block>();
	ArrayList<Transaction> pendingTransactions=new ArrayList<Transaction>();
	private int difficulty=4;
	private double miningReward=1;
	public BlockChain(){
		blockChain.add(this.createGenesisBlock());
	}
	
	public void createTransaction(Transaction transaction){
		pendingTransactions.add(transaction);
	}
	
	public void mineTransaction(String toAddress){
		if(pendingTransactions.size()>0){
			Block b=new Block(pendingTransactions.get(0));
			mineBlock(b);
			pendingTransactions.remove(0);
			pendingTransactions.add(new Transaction("money-tree",toAddress,miningReward));
		}
	}
	
	private Block createGenesisBlock(){
		Block genesisBlock= new Block(new Transaction("noone","noone",0));
		genesisBlock.timestamp=System.currentTimeMillis();
		genesisBlock.previousHash=null;
		genesisBlock.hash=genesisBlock.calculateHash(0);
		return genesisBlock;
	}
	
	Block getLatestBlock(){
		return blockChain.get(blockChain.size()-1);
	}
	
	protected void mineBlock(Block b){
		String prefix="";
		b.hash=b.calculateHash(blockChain.size());
		for(int i=0;i<difficulty;i++){
			prefix+="0";
		}
		while(!b.hash.startsWith(prefix)){
			b.nonce=new Random().nextLong();
			b.hash=b.calculateHash(blockChain.size());
		}
		System.out.println(b.hash);
		this.addBlock(b);
		
	}
	
	public double getBalance(String address){
		double balance=0;
		double amountTransfered=0;
		for (int i=0;i<blockChain.size();i++){
			amountTransfered=blockChain.get(i).transaction.transferAmount;
			if(blockChain.get(i).transaction.toAddress.equals(address)){
				balance+=amountTransfered;
			}
			if(blockChain.get(i).transaction.fromAddress.equals(address)){
				balance-=amountTransfered;
			}
		}
		return balance;
	}
	
	protected void addBlock(Block block){
			block.previousHash=this.getLatestBlock().hash;
			block.timestamp=System.currentTimeMillis();
			//blockChain.size()==the index value of the to-be-added block
			block.hash=block.calculateHash(blockChain.size());
			blockChain.add(block);
		
	}
	
	boolean isChainValid(){
		Block currentBlock;
		Block previousBlock;
		for(int i=0;i<blockChain.size();i++){
			currentBlock=blockChain.get(i);
			if(i!=0){
				previousBlock=blockChain.get(i-1);
				if(currentBlock.previousHash!=previousBlock.hash){
					return false;
				}
			}
			if(!currentBlock.hash.equals(currentBlock.calculateHash(i))){
				System.out.println(currentBlock.hash);
				System.out.println(currentBlock.calculateHash(i));
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		

	}

}
