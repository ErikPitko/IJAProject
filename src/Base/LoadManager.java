package Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Graphics.FXMLExampleController;
import Graphics.Panel;
import Graphics.Point2D;
import Graphics.Rect;

public class LoadManager
{
	
	private static int findBlockIndex(ArrayList<SerBlock> block, Block findBlock) {
		for (int i = 0; i < block.size(); i++) {
			if(block.get(i).getBlock() == findBlock)
				return i;
		}
		return -1;
	}
	private static int findInPortIndex(ArrayList<Port> inPorts, Block inBlock) {
		for (int i = 0; i < inPorts.size(); i++) {
			if (inPorts.get(i).GetFirstLink() == null)
				continue;
			if(inPorts.get(i).GetFirstLink().getInPort().GetBlock() == inBlock)
				return i;
		}
		return -1;
	}
	
	public static void saveScene(ArrayList<Block> blockList, File file) throws IOException {
		ArrayList<SerBlock> sBlock = new ArrayList<SerBlock>();
		for (Block b: blockList) {
			sBlock.add(new SerBlock(b, 
					new Point2D(b.getRect().getX(), b.getRect().getY()), 
					new Point2D(b.getRect().getWidth(), b.getRect().getHeight())));
		}
		FileOutputStream fout = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(sBlock);
		
		oos.close();
		fout.close();
	}
	
	public static void loadScene(File file) throws IOException, ClassNotFoundException {
		for (Block block : Panel.BlockList)
			block.DeleteBlock();
		Panel.BlockList.clear();
		FileInputStream fin = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fin);
		
		ArrayList<SerBlock> sBlockList = (ArrayList<SerBlock>) ois.readObject();
		ois.close();
		fin.close();
		
		
		for(SerBlock sBlock: sBlockList)
		{
			Rect rect = new Rect(sBlock.getPosition(), sBlock.getSize());
			Block block;
			if (sBlock.getBlock().getType() == EBlock.IN)
				block = new Block(sBlock.getBlock().getType(), rect, sBlock.getBlock().getValue());
			else
				block = new Block(sBlock.getBlock().getType(), rect);
			for(Port port : sBlock.getBlock().getInPorts()) {
				block.genInPort();
			}
			Panel.BlockList.add(block);
			block.Draw(FXMLExampleController.AnchorPanel);
		}
		
		for (int i = 0; i < sBlockList.size(); i++) {
			Block currBlock = sBlockList.get(i).getBlock();
			int linkSize = currBlock.GetOutPort().GetLinks().size();
			for (int j = 0; j < linkSize; j++) {
				Link currLink = currBlock.GetOutPort().GetLinks().get(j);
				int idxOutBlock = findBlockIndex(sBlockList, currLink.getOutPort().GetBlock());
				int idxInPort = findInPortIndex(sBlockList.get(idxOutBlock).getBlock().getInPorts(), currBlock);
				
				Link link = new Link(Panel.BlockList.get(i).GetOutPort(), 
						Panel.BlockList.get(idxOutBlock).getInPorts().get(idxInPort));
				link.Draw(FXMLExampleController.AnchorPanel);
			}
		}
	}
}
