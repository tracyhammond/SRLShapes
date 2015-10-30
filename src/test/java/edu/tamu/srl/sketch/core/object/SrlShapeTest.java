package edu.tamu.srl.sketch.core.object;

import edu.tamu.srl.sketch.core.abstracted.SrlObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gigemjt on 12/23/14.
 */
public class SrlShapeTest {
    /**
     * KEY:  Shape = S, Stroke = T
     * S1 is the shape this method is called on.
     *       S1
     *      /| \
     *    S2 S3 S4
     *   / |  |    \
     *  T1 S5 T2   S6
     *     |        |
     *     T3      T4
     *
     * Resultant List:
     * S5, S3, S6
     */
    @Test
    public void leafShapeTest() {
        SrlStroke t1 = new SrlStroke();
        SrlStroke t2 = new SrlStroke();
        SrlStroke t3 = new SrlStroke();
        SrlStroke t4 = new SrlStroke();
        SrlShape s1 = new SrlShape();
        SrlShape s2 = new SrlShape();
        SrlShape s3 = new SrlShape();
        SrlShape s4 = new SrlShape();
        SrlShape s5 = new SrlShape();
        SrlShape s6 = new SrlShape();

        // construct tree leafs
        s5.add(t3);
        s3.add(t2);
        s6.add(t4);

        // constructor containers
        s2.add(t1);
        s2.add(s5);
        s4.add(s6);

        s1.add(s2);
        s1.add(s3);
        s1.add(s4);

        // adding test cases to tree.

        ArrayList<SrlShape> expected = new ArrayList<>();
        expected.add(s5);
        expected.add(s3);
        expected.add(s6);
        List<SrlShape> objs = s1.getRecursiveLeafShapes();
        System.out.println(objs);
        Assert.assertEquals(expected, objs);
    }

    /**
     * KEY:  Shape = S, Stroke = T
     * S1 is the shape this method is called on.
     *       S1
     *      /| \
     *    S2 S3 S4
     *   / |  |    \
     *  T1 S5 T2   S6
     *     |        |
     *     T3      T4
     *
     * Resultant List:
     * S1, S2, T1, S5, T3, S3, T2, S4, S6, T4
     */
    @Test
    public void subObjectTest() {
        SrlStroke t1 = new SrlStroke();
        SrlStroke t2 = new SrlStroke();
        SrlStroke t3 = new SrlStroke();
        SrlStroke t4 = new SrlStroke();
        SrlShape s1 = new SrlShape();
        SrlShape s2 = new SrlShape();
        SrlShape s3 = new SrlShape();
        SrlShape s4 = new SrlShape();
        SrlShape s5 = new SrlShape();
        SrlShape s6 = new SrlShape();

        // construct tree leafs
        s5.add(t3);
        s3.add(t2);
        s6.add(t4);

        // constructor containers
        s2.add(t1);
        s2.add(s5);
        s4.add(s6);

        s1.add(s2);
        s1.add(s3);
        s1.add(s4);

        // adding test cases to tree.

        ArrayList<SrlObject> expected = new ArrayList<>();
        // S1, S2, T1, S5, T3, S3, T2, S4, S6, T4
        expected.add(s1);
        expected.add(s2);
        expected.add(t1);
        expected.add(s5);
        expected.add(t3);
        expected.add(s3);
        expected.add(t2);
        expected.add(s4);
        expected.add(s6);
        expected.add(t4);
        List<SrlObject> objs = s1.getRecursiveSubObjectList();
        System.out.println(objs);
        Assert.assertEquals(expected, objs);
    }

    /**
     * KEY:  Shape = S, Stroke = T
     * S1 is the shape this method is called on.
     *       S1
     *      /| \
     *    S2 S3 S4
     *   / |  |    \
     *  T1 S5 T2   S6
     *     |        |
     *     T3      T4
     *
     * Resultant List:
     * T1, T3, T2, T4
     */
    @Test
    public void subStrokeTest() {
        SrlStroke t1 = new SrlStroke();
        SrlStroke t2 = new SrlStroke();
        SrlStroke t3 = new SrlStroke();
        SrlStroke t4 = new SrlStroke();
        SrlShape s1 = new SrlShape();
        SrlShape s2 = new SrlShape();
        SrlShape s3 = new SrlShape();
        SrlShape s4 = new SrlShape();
        SrlShape s5 = new SrlShape();
        SrlShape s6 = new SrlShape();

        // construct tree leafs
        s5.add(t3);
        s3.add(t2);
        s6.add(t4);

        // constructor containers
        s2.add(t1);
        s2.add(s5);
        s4.add(s6);

        s1.add(s2);
        s1.add(s3);
        s1.add(s4);

        // adding test cases to tree.

        ArrayList<SrlObject> expected = new ArrayList<>();
        // T1, T3, T2, T4
        expected.add(t1);
        expected.add(t3);
        expected.add(t2);
        expected.add(t4);
        List<SrlStroke> objs = s1.getRecursiveStrokeList();
        System.out.println(objs);
        Assert.assertEquals(expected, objs);
    }
}
