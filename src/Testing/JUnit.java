package Testing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import Base.Block;
import Base.EBlock;
import Base.Link;
import Base.Port;
import Graphics.Point2D;
import Graphics.Rect;

public class JUnit
{
    public Rect myRect;
    public Point2D point;
    private List<Block> blocks;
    
    @Before
    public void prepare() {
        point = new Point2D(5,5);
        myRect = new Rect(10,10,10,10);
        
        Link[] links;
		blocks = new ArrayList<Block>();
		blocks.add(new Block(EBlock.IN, new Rect(0, 0, 0, 0), 5));
		blocks.add(new Block(EBlock.IN, new Rect(0, 0, 0, 0), 2));
		
		links = new Link[] {new Link(), new Link()};
		links[0].setInPort(blocks.get(0).GetOutPort());
		links[1].setInPort(blocks.get(1).GetOutPort());
		createBlock(EBlock.ADD, links);
		
		links = new Link[] {new Link(), new Link()};
		links[0].setInPort(blocks.get(0).GetOutPort());
		links[1].setInPort(blocks.get(2).GetOutPort());
		createBlock(EBlock.ADD, links);
		
		links = new Link[] {new Link(), new Link(), new Link()};
		links[0].setInPort(blocks.get(blocks.size() - 1).GetOutPort());
		links[1].setInPort(blocks.get(blocks.size() - 2).GetOutPort());
		links[2].setInPort(blocks.get(1).GetOutPort());
		createBlock(EBlock.ADD, links);
    }
    
    @Test
	public void Simple() {
		Block.compute(blocks.get(2));
		assertEquals(7, blocks.get(2).getValue(), 0);

		blocks.get(2).setType(EBlock.SUB);
		Block.compute(blocks.get(2));
		assertEquals(3, blocks.get(2).getValue(), 0);
		
		blocks.get(2).setType(EBlock.MUL);
		Block.compute(blocks.get(2));
		assertEquals(10, blocks.get(2).getValue(), 0);
		
		blocks.get(2).setType(EBlock.DIV);
		Block.compute(blocks.get(2));
		assertEquals(5.0/2.0, blocks.get(2).getValue(), 0);
	}
	
	@Test
	public void TwoStageBlocks() {
		Block thisBlock = blocks.get(3);
		Block.compute(thisBlock);
		assertEquals(5+7, thisBlock.getValue(), 0);
		
		thisBlock.setType(EBlock.SUB);
		Block.compute(thisBlock);
		assertEquals(5-7, thisBlock.getValue(), 0);

		thisBlock.setType(EBlock.MUL);
		Block.compute(thisBlock);
		assertEquals(5*7, thisBlock.getValue(), 0);
		
		thisBlock.setType(EBlock.DIV);
		Block.compute(thisBlock);
		assertEquals(5.0/7.0, thisBlock.getValue(), 0);
		
		
//		Zacyklenie
//		blocks.get(2).genInPort();
//		blocks.get(2).getInPorts().get(2).setLink(new Link(blocks.get(blocks.size() - 1).getOutPorts().get(0)));
//		Block.compute(blocks.get(blocks.size() - 1));
		
		
	}

	@Test
	public void ThreeStageBlocks() {
		Block thisBlock = blocks.get(4);
		Block.compute(thisBlock);
		assertEquals(12+7+2, thisBlock.getValue(), 0);
		
		thisBlock.setType(EBlock.SUB);
		Block.compute(thisBlock);
		assertEquals(12-7-2, thisBlock.getValue(), 0);
		
		thisBlock.setType(EBlock.MUL);
		Block.compute(thisBlock);
		assertEquals(12 * 7 * 2, thisBlock.getValue(), 0);
		
		thisBlock.setType(EBlock.DIV);
		Block.compute(thisBlock);
		assertEquals((5.0+5.0+2.0)/(7.0)/2.0, thisBlock.getValue(), 0);
		
	}
	
	@Test
	public void elevenStageBlocks() {
		Random rand = new Random();
		double temp;
		double expectedValue = 0;
		Link[] links;
		blocks = new ArrayList<Block>();

		links = new Link[] {new Link(), new Link()};
		createReverseBlock(EBlock.ADD, links);
		
		for (int i = 1; i < 10; ++i) {
			for (int j = (int) Math.pow(2, i-1); j < Math.pow(2, i); ++j) {
				links = new Link[] {new Link(), new Link()};
				createReverseBlock(EBlock.ADD, links);
				Link previousLink = blocks.get(j/2).getInPorts().get(((j-1)%2==0) ? 0:1).getLink();
				previousLink.setInPort(blocks.get(j).GetOutPort());
				blocks.get(j/2).getInPorts().get(((j-1)%2==0) ? 0:1).setLink(previousLink);
				blocks.get(j).GetOutPort().setLink(previousLink);
			}
		}
		for (int j = (int) Math.pow(2, 9); j < Math.pow(2, 10); ++j) {
			temp = rand.nextDouble() * 50 - 25;
			blocks.add(new Block(EBlock.IN, new Rect(0, 0, 0, 0), temp));
			expectedValue += temp;
			Link previousLink = blocks.get(j/2).getInPorts().get(((j-1)%2==0) ? 0:1).getLink();
			previousLink.setInPort(blocks.get(j).GetOutPort());
			blocks.get(j/2).getInPorts().get(((j-1)%2==0) ? 0:1).setLink(previousLink);
			blocks.get(j).GetOutPort().setLink(previousLink);
		}
		Block.compute(blocks.get(1));
		assertEquals(expectedValue, blocks.get(1).getValue(), 0.000000001);
	}
    
	@Test
    public void PointTest()
    {
        assertEquals("Does point X = 5",5,point.X);
        assertEquals("Does point Y = 5",5,point.Y);
        assertEquals("Does point distance equal to 4",4.0,Point2D.Distance(point,new Point2D(5,9)), 0.000000001);
        assertEquals("Does point distance equal to 4",4.0,Point2D.Distance(point,new Point2D(9,5)), 0.000000001);
        assertEquals("Does point distance equal to sqrt(2)", Math.sqrt(8),Point2D.Distance(point,new Point2D(7,7)), 0.000000001);
        assertEquals("Does point distance equal to sqrt(50)", Math.sqrt(50),Point2D.Distance(point,new Point2D(0,0)), 0.000000001);
    }

    @Test
    public void RectContains()
    {
        assertEquals("Does Rect contains point X5 Y5.",false,myRect.Contains(new Point2D(5,5)));
        assertEquals("Does Rect contains point X5 Y10.",false,myRect.Contains(new Point2D(5,10)));
        assertEquals("Does Rect contains point X10 Y10.",true,myRect.Contains(new Point2D(10,10)));
        assertEquals("Does Rect contains point X20 Y10.",true,myRect.Contains(new Point2D(20,10)));
        assertEquals("Does Rect contains point X25 Y10.",false,myRect.Contains(new Point2D(25,10)));
        assertEquals("Does Rect contains point X20 Y20.",true,myRect.Contains(new Point2D(20,20)));
        assertEquals("Does Rect contains point X25 Y25.",false,myRect.Contains(new Point2D(20,25)));
        assertEquals("Does Rect contains point X0 Y0.",false,myRect.Contains(new Point2D(0,0)));
    }
    @Test
    public void RectContainsAdvanced()
    {
        assertEquals("Does Rect contains point XfloatMAX Y10.",false,myRect.Contains(new Point2D(Integer.MAX_VALUE,10)));
        assertEquals("Does Rect contains point X15 YfloatMAX.",false,myRect.Contains(new Point2D(15,Integer.MAX_VALUE)));
        assertEquals("Does Rect contains point XfloatMax YfloatMax.",false,myRect.Contains(new Point2D(Integer.MAX_VALUE,Integer.MAX_VALUE)));
        assertEquals("Does Rect contains point XfloatMin YfloatMin.",false,myRect.Contains(new Point2D(Integer.MIN_VALUE,Integer.MIN_VALUE)));
    }
    @Test
    public void RectIntersects()
    {
        assertEquals("Does Rect intersect Rect X0 Y0 W5 H5.",false,myRect.Intersect(new Rect(0,0,5,5)));
        assertEquals("Does Rect intersect Rect X0 Y0 W10 H10.",true,myRect.Intersect(new Rect(0,0,10,10)));
        assertEquals("Does Rect intersect Rect X20 Y20 W10 H10.",true,myRect.Intersect(new Rect(20,20,10,10)));
        assertEquals("Does Rect intersect Rect X21 Y21 W10 H10.",false,myRect.Intersect(new Rect(21,21,10,10)));
    }
    @Test
    public void RectIntersectsAdvanced()
    {
        assertEquals("Does Rect intersect Rect X0 Y0 W50 H50.",true,myRect.Intersect(new Rect(0,0,50,50)));
        assertEquals("Does Rect intersect Rect X0 Y0 WfloatMax HfloatMax.",true,myRect.Intersect(new Rect(0,0,Integer.MAX_VALUE,Integer.MAX_VALUE)));
        assertEquals("Does Rect intersect Rect X20 Y20 WfloatMax HfloatMax.",true,myRect.Intersect(new Rect(20,20,5000,5000)));
        assertEquals("Does Rect intersect Rect XfloatMin YfloatMin WfloatMax HfloatMax.",false,myRect.Intersect(new Rect(Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MAX_VALUE,Integer.MIN_VALUE)));
    }
    
    private void createBlock(EBlock type, Link[] links){
		Block thisBlock = new Block(type, new Rect(0, 0, 0, 0));
		blocks.add(thisBlock);
		Port inPort;
		for (int i = 0; i < links.length; ++i) {
			blocks.get(blocks.size() - 1).genInPort();
			inPort = thisBlock.getInPorts().get(i);
			links[i].setOutPort(inPort);
			inPort.setLink(links[i]);
		}
	}
	
	private void createReverseBlock(EBlock type, Link[] links){
		Block thisBlock = new Block(type, new Rect(0, 0, 0, 0));
		blocks.add(thisBlock);
		Port inPort;
		for (int i = 0; i < 2; ++i) {
			thisBlock.genInPort();
			inPort = thisBlock.getInPorts().get(i);
			links[i].setOutPort(inPort);
			inPort.setLink(links[i]);
		}
	}
}
