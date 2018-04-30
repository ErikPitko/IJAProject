/*******************************************************************************
 *
 * VUT FIT Brno - IJA project BlockDiagram
 *
 * Copyright (C) 2018, Adam Petras (xpetra19)
 * Copyright (C) 2018, Erik Pitko (xpitko00)
 * 
 * Contributors: 
 * 		Adam Petras - GUI, base application implementation, tests
 * 		Erik Pitko - base application implementation, Doxygen doc, tests, save/load scene
 * 
 ******************************************************************************/
package Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Graphics.MainWindowController;
import Graphics.Panel;
import Graphics.Point2D;
import Graphics.Rect;

/**
 * Collection of static functions used for saving and loading scene.
 */
public class LoadManager {

	/**
	 * Find block index.
	 *
	 * @param block
	 *            serializable list of blocks
	 * @param findBlock
	 *            the block to be found
	 * @return index of block in the serializable list of blocks
	 */
	private static int findBlockIndex(ArrayList<SerBlock> block, Block findBlock) {
		for (int i = 0; i < block.size(); i++) {
			if (block.get(i).getBlock() == findBlock)
				return i;
		}
		return -1;
	}

	/**
	 * Find port index.
	 *
	 * @param inPorts
	 *            list of the input ports
	 * @param inBlock
	 *            block to be found
	 * @return the index of port with given block
	 */
	private static int findInPortIndex(ArrayList<Port> inPorts, Block inBlock) {
		for (int i = 0; i < inPorts.size(); i++) {
			if (inPorts.get(i).GetFirstLink() == null)
				continue;
			if (inPorts.get(i).GetFirstLink().getInPort().GetBlock() == inBlock)
				return i;
		}
		return -1;
	}

	/**
	 * Saves scene in a file.
	 *
	 * @param blockList
	 *            the block list
	 * @param file
	 *            the output file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void saveScene(ArrayList<Block> blockList, File file) throws IOException {
		ArrayList<SerBlock> sBlock = new ArrayList<SerBlock>();
		for (Block b : blockList) {
			sBlock.add(new SerBlock(b, new Point2D(b.getRect().getX(), b.getRect().getY()),
					new Point2D(b.getRect().getWidth(), b.getRect().getHeight())));
		}
		FileOutputStream fout = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(sBlock);

		oos.close();
		fout.close();
	}

	/**
	 * Load scene.
	 *
	 * @param file
	 *            the input file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static void loadScene(File file) throws IOException, ClassNotFoundException {
		Panel.ClearAllBlocks();
		FileInputStream fin = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fin);

		ArrayList<SerBlock> sBlockList = (ArrayList<SerBlock>) ois.readObject();
		ois.close();
		fin.close();

		for (SerBlock sBlock : sBlockList) {
			Rect rect = new Rect(sBlock.getPosition(), sBlock.getSize());
			Block block;
			if (sBlock.getBlock().getType() == EBlock.IN)
				block = new Block(sBlock.getBlock().getType(), rect, sBlock.getBlock().getValue());
			else
				block = new Block(sBlock.getBlock().getType(), rect);
			for (Port port : sBlock.getBlock().getInPorts()) {
				block.genInPort();
			}
			Panel.BlockList.add(block);
			block.Draw(MainWindowController.AnchorPanel);
		}

		for (int i = 0; i < sBlockList.size(); i++) {
			Block currBlock = sBlockList.get(i).getBlock();
			if (currBlock.GetOutPort() == null)
				continue;
			int linkSize = currBlock.GetOutPort().GetLinks().size();
			for (int j = 0; j < linkSize; j++) {
				Link currLink = currBlock.GetOutPort().GetLinks().get(j);
				int idxOutBlock = findBlockIndex(sBlockList, currLink.getOutPort().GetBlock());
				int idxInPort = findInPortIndex(sBlockList.get(idxOutBlock).getBlock().getInPorts(), currBlock);

				Link link = new Link(Panel.BlockList.get(i).GetOutPort(),
						Panel.BlockList.get(idxOutBlock).getInPorts().get(idxInPort));
				link.Draw(MainWindowController.AnchorPanel);
			}
		}
	}
}
