package Testing;

import Base.Block;
import Base.EBlock;
import Base.Link;
import Graphics.Point2D;
import Graphics.Rect;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class JUnit
{
    public Rect myRect;
    public Point2D point;
    public Block block1;
    public Block block2;
    public Block block3;
    @Before
    public void prepare() {
        block1 = new Block(EBlock.SUM,new Rect(10,10,10,10),10,20,30,40);
        block2 = new Block(EBlock.SUM,new Rect(10,10,10,10));
        block3 = new Block(EBlock.MUL,new Rect(0,0,0,0),3);
        point = new Point2D(5,5);
        myRect = new Rect(10,10,10,10);
    }
    @Test
    public void BlockTest()
    {
        assertEquals("Block1 input port0 = 10",(double)10,block1.GetInputPort(0).GetValue());
        assertEquals("Block1 input port1 = 20",(double)20,block1.GetInputPort(1).GetValue());
        assertEquals("Block1 input port2 = 30",(double)30,block1.GetInputPort(2).GetValue());
        assertEquals("Block1 input port3 = 40",(double)40,block1.GetInputPort(3).GetValue());
    }
    @Test
    public void AdvancedBlockTesting()
    {
        // 10 20 30 40 >> Block --> 100 5 >> Block --> 105 2 3 >> Block
        //                 SUM                SUM                  MUL
        //                 100                105                  630
        new Link(block1.Output(),block2.GetEmptyInputPort());
        assertEquals("Block1 output port = 0",(double)0,block1.Output().GetValue());
        assertEquals("Block2 input port0 = 100",(double)0,block2.GetInputPort(0).GetValue());
        block1.Calculate();
        assertEquals("Block1 output port = 100",(double)100,block1.Output().GetValue());
        assertEquals("Block2 input port0 = 100",(double)100,block2.GetInputPort(0).GetValue());
        new Link(block2.Output(),block3.GetEmptyInputPort());
        block3.SetPortValue(3);
        block3.SetPortValue(2);
        block2.SetPortValue(5);
        assertEquals("Block2 output port = 0",(double)0,block2.Output().GetValue());
        block2.Calculate();
        assertEquals("Block2 output port = 105",(double)105,block2.Output().GetValue());
        assertEquals("Block3 input port0 = 105",(double)105,block3.GetInputPort(0).GetValue());
        assertEquals("Block3 output port = 0",(double)0,block3.Output().GetValue());
        block3.Calculate();
        assertEquals("Block1 output port = 100",(double)100,block1.Output().GetValue());
        assertEquals("Block2 output port = 105",(double)105,block2.Output().GetValue());
        assertEquals("Block3 output port = 630",(double)630,block3.Output().GetValue());
    }
    @Test
    public void PointTest()
    {
        assertEquals("Does point X = 5",5,point.X);
        assertEquals("Does point Y = 5",5,point.Y);
        assertEquals("Does point distance equal to 4",4f,(float)Point2D.Distance(point,new Point2D(5,9)));
        assertEquals("Does point distance equal to 4",4f,(float)Point2D.Distance(point,new Point2D(9,5)));
        assertEquals("Does point distance equal to sqrt(2)", Math.sqrt(8),Point2D.Distance(point,new Point2D(7,7)));
        assertEquals("Does point distance equal to sqrt(50)", Math.sqrt(50),Point2D.Distance(point,new Point2D(0,0)));
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
}
