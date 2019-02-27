package BlockChainPckg;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.Assert;

public class BlockChainTest {

	@Test
	//Constructor method should create a blockchain array containing a genesis block
	public void testBlockChain() {
		BlockChain bc=new BlockChain();
		int size=bc.blockChain.size();
		Transaction genesisBlockTransaction=bc.blockChain.get(0).transaction;
		assertEquals(1, size);
		assertEquals("noone",genesisBlockTransaction.fromAddress);
	}

	@Test
	//GetLatestBlock should get the most recent block in the blockchain
	public void testGetLatestBlock() {
		BlockChain bc=new BlockChain();
		Block latest=bc.getLatestBlock();
		assertEquals(bc.blockChain.get(0),latest);
	}

	@Test
	//AddBlock should add a new block with the correct data to the block chain
	//the data field MUST NOT be null
	public void testAddBlock() {
		BlockChain bc=new BlockChain();
		for(int i=0;i<10;i++){
			Block b1=new Block(new Transaction("x","y",100));
			bc.addBlock(b1);
		}
		assertEquals(11,bc.blockChain.size());
	}

	@Test
	//Block data CANNOT be modified
	//Block order CANNOT be changed
	//testIsChainValid() confirms that neither of these happen
	public void testIsChainValid() {
		//1. return false if blockdata is modified
		
		//1a)Test that g-block cannot be modified
		BlockChain bc=new BlockChain();
		assertEquals(true,bc.isChainValid());
		bc.blockChain.get(0).transaction=new Transaction("you","me",1000000);
		assertEquals(false,bc.isChainValid());
		
		//1b)Test that added blocks cannot be modified
		BlockChain bc2=new BlockChain();
		bc2.addBlock(new Block(new Transaction("a","b",5)));
		assertEquals(true,bc2.isChainValid());
		bc2.blockChain.get(1).transaction.fromAddress="block infinity";
		assertEquals(false,bc2.isChainValid());
		
		//2.return false if block order is modified
		BlockChain bc3=new BlockChain();
		bc3.addBlock(new Block(new Transaction("a","c",1000)));
		assertEquals(true,bc3.isChainValid());
		Block temp=bc3.blockChain.get(0);
		bc3.blockChain.set(0,bc3.blockChain.get(1));
		bc3.blockChain.set(1, temp);
		assertEquals(false,bc2.isChainValid());
	}
	
	@Test
	//MineBlock runs the proof of work algorithm and then adds the pending block to the blockchain
	public void testMineBlock(){
		BlockChain bc4=new BlockChain();
		Block b=new Block(new Transaction("c","d",1000));
		bc4.mineBlock(b);
		assertEquals(bc4.blockChain.size(),2);
		assertEquals(bc4.blockChain.get(1).transaction.toAddress,"d");
	}
	
	@Test
	//a transaction should be successfully created. No null fields are allowed
	public void testCreateTransaction(){
		BlockChain bc5=new BlockChain();
		bc5.createTransaction(new Transaction("person1","person2",1000));
		assertEquals(bc5.pendingTransactions.get(0).fromAddress,"person1");
	}
	
	@Test
	//A transaction in the array is mined, added to the blockchain, and then the miner gets an award
	public void testMineTransaction(){
		BlockChain bc6=new BlockChain();
		bc6.createTransaction(new Transaction("person1","person2",1000));
		bc6.mineTransaction("Bobby");
		assertEquals(bc6.pendingTransactions.get(0).toAddress,"Bobby");
		assertEquals(bc6.pendingTransactions.get(0).fromAddress,"money-tree");
		assertEquals(bc6.pendingTransactions.get(0).transferAmount==1,true);
		
	}
	
	@Test
	public void testGetBalance(){
		BlockChain bc7=new BlockChain();
		bc7.createTransaction(new Transaction("p1","p2",37.5));
		bc7.createTransaction(new Transaction("xx","p1",975));
		bc7.mineTransaction("someguy");
		bc7.mineTransaction("someguy");
		bc7.mineTransaction("someguy");
		assertEquals(bc7.getBalance("someguy")==1,true);
		assertEquals(bc7.getBalance("p1")==975-37.5,true);
	}
	
	}

