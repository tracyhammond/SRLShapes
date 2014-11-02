package srl.sketch.graphics;

/**
 * Created by gigemjt on 10/29/14.
 */

import srl.sketch.core.SrlSketch;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * SketchCanvas is a container that stores sketch data and its SVG representation together.
 * If serialized to an xml file
 * @author George R. Lucchese
 *
 */
class SvgSketchCanvas { /*extends SVGCanvas {
    SrlSketch sketch;

    private SvgSketchCanvas(){

    }
    public SvgSketchCanvas(SrlSketch sketch){
        super();
        setSketch(sketch);
    }

    /**
     * @return the sketch
     */
    /*
    public SrlSketch getSketch() {
        return sketch;
    }

    /**
     * @param sketch the sketch to set
     */
    /*
    public void setSketch(SrlSketch sketch) {
        this.sketch = sketch;
        //this.addShape(sketch.toSVGShape());
    }


    /*
     * SERIALIZATION
     */
    /*
    public static SVGSketchCanvas deserialize(InputStream in) throws Exception{
        Persister persister = new Persister(buildXMLTypeMatcher());
        return persister.read(SVGSketchCanvas.class, in);
    }

    public static SVGSketchCanvas deserialize(File in) throws Exception{
        Persister persister = new Persister(buildXMLTypeMatcher());
        return persister.read(SVGSketchCanvas.class, in);
    }

    public void serialize(OutputStream out) throws Exception{

        Persister persister = new Persister(buildXMLTypeMatcher());
        persister.write(this, out);
    }

    public void serialize(File out) throws Exception{
        Persister persister = new Persister(buildXMLTypeMatcher());
        persister.write(this, out);
    }

    public static RegisterMatcher buildXMLTypeMatcher(){
        return SVGCanvas.buildXMLTypeMatcher().extend(SrlSketch.buildXMLTypeMatcher());
    }

*/
}
