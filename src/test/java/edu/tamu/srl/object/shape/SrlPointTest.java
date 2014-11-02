package edu.tamu.srl.object.shape;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by gigemjt on 11/1/14.
 */
public class SrlPointTest {
    static public void main(String args[]){
        SrlPoint p1 = new SrlPoint(3,4,1);
        System.out.println(p1.toString());
        p1.setAttribute("favorite", "yellow");
        p1.setP(1, 2);
        System.out.println(p1.toString());
        SrlPoint o = new SrlPoint(p1);
        System.out.println(o.toString());
        o.setAttribute("favorite", "orange");
        p1.setP(6, 7);
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("test.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(o);
            out.writeObject(p1);
            out.close();
            fileOut.close();
            System.out.println("File saved");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileIn = new FileInputStream("test.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SrlPoint p = (SrlPoint) in.readObject();
            SrlPoint p2 = (SrlPoint) in.readObject();
            in.close();
            fileIn.close();
            System.out.println(p.toString());
            System.out.println(p.toStringLong());
            System.out.println(p.getOrigX() + ", " + p2.getOrigY());
            System.out.println(p.getAttribute("favorite"));
            p.setAttribute("favorite", "red");
            System.out.println(p2.toString());
            System.out.println(p2.toStringLong());
            System.out.println(p2.getOrigX() + ", " + p2.getOrigY());
            System.out.println(p2.getAttribute("favorite"));
            System.out.println(p.getAttribute("favorite"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
