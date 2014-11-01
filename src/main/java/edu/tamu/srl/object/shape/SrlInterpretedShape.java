package edu.tamu.srl.object.shape;

import java.util.ArrayList;
import java.util.Collections;

import edu.tamu.srl.object.SrlInterpretation;

public abstract class SrlInterpretedShape extends SrlShape {

	
	/**
	 * Needed for serialization
	 * Note that if member variables are added
	 * that are not transient, then serialization
	 * will not hold for old saved dataS
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * add an interpretation for an object
	 * @param interpretation a string name representing the interpretation
	 * @param confidence a double representing the confidence
	 * @param complexity a double representing the complexity
	 */
	public void addInterpretation(String interpretation, double confidence, int complexity){
		m_interpretations.add(new SrlInterpretation(interpretation, confidence, complexity));
	}

	/**
	 * Constructor specifically to replace the clone() method
	 * @param s
	 */	
	public SrlInterpretedShape(SrlInterpretedShape s){
		super(s);
		this.m_interpretations = s.copyInterpretations();
		for(SrlObject sub : s.getSubShapes()){
			if(sub instanceof SrlPoint){
				this.add(new SrlPoint((SrlPoint)sub));
			} else if (sub instanceof SrlStroke){
				this.add(new SrlStroke((SrlStroke)sub));			
				//	} else if (sub instanceof SrlStroke){
			//		this.add(new SrlStroke((SrlStroke)sub));
				//	} else if (sub instanceof SrlStroke){
			//		this.add(new SrlStroke((SrlStroke)sub));
				//	} else if (sub instanceof SrlStroke){
			//		this.add(new SrlStroke((SrlStroke)sub));
			}
		}
	}
	
	public SrlInterpretedShape(){
		super();
	}
	
	/**
	 * Gets a list of all of the interpretations available for the object
	 * @return the list of interpretations
	 */
	public ArrayList<SrlInterpretation> getAllInterpretations(){
		Collections.sort(m_interpretations);
		return m_interpretations;
	}
	

	/**
	 * Make a cloned copy of the interpretations
	 * @return
	 */
	public ArrayList<SrlInterpretation> copyInterpretations(){
		ArrayList<SrlInterpretation> interps = new ArrayList<SrlInterpretation>();
		for(SrlInterpretation i : m_interpretations){
			interps.add(new SrlInterpretation(i));
		}
		return interps;
	}
	
	/**
	 * Returns the interpretation with the highest complexity
	 * and among those the one with the highest confidence
	 * @return best interpretation
	 */
	public SrlInterpretation getBestInterpretation(){
		Collections.sort(m_interpretations);
		return m_interpretations.get(0);
/**		if(m_interpretations.size() == 0) return null;
		SRL_Interpretation bestInterpretation = m_interpretations.get(0);
		for(SRL_Interpretation i : m_interpretations){
			if(i.compareTo(bestInterpretation) > 0){
				bestInterpretation = i;
			}
		}
		return bestInterpretation;
		**/
	}
	
	/**
	 * Checks if it has a particular interpretation, if so, 
	 * it returns it, else it returns null.
	 * @param interpretation the string name of the interpretation
	 * @return the interpretation requested (complete with confidence and complexity)
	 */
	public SrlInterpretation getInterpretation(String interpretation){
		for(SrlInterpretation i : m_interpretations){
			if(i.getInterpretation().equals(interpretation)){
				return i;
			}
		}
		return null;
	}
	
	/**
	 * Returns the complexity of a particular interpretation.
	 * If the interpretation does not exist, it return -1.
	 * @param interpretation the string name of the interpretation
	 * @return the complexity of that interpretation
	 */
	public double getInterpretationComplexity(String interpretation){
		for(SrlInterpretation i : m_interpretations){
			if(i.getInterpretation().equals(interpretation)){
				return i.getComplexity();
			}
		}
		return -1;
	}
	
	
	/**
	 * Returns the confidence of a particular interpretation.
	 * If the interpretation does not exist, it return -1.
	 * @param interpretation the string name of the interpretation
	 * @return the confidence of that interpretation
	 */
	public double getInterpretationConfidence(String interpretation){
		for(SrlInterpretation i : m_interpretations){
			if(i.getInterpretation().equals(interpretation)){
				return i.getConfidence();
			}
		}
		return -1;
	}
	
	/**
	 * @return the interpretations
	 */
	public ArrayList<SrlInterpretation> getInterpretations() {
		return m_interpretations;
	}
	

	/**
	 * Gets the list of subshapes
	 * @param type the interpretation of the object
	 * @return list of objects that make up this object
	 */
	public ArrayList<SrlInterpretedShape> getSubShapes(String type){
		ArrayList<SrlInterpretedShape> shapes = new ArrayList<SrlInterpretedShape>();
		for(SrlObject o : getSubShapes()){
			if(o instanceof SrlInterpretedShape){
				if(((SrlInterpretedShape)o).hasInterpretation(type)){
					shapes.add((SrlInterpretedShape)o);
				}
			}
		}
		return shapes;
	}
	

	/**
	 * Checks if it contains an interpretation of a certain type
	 * @param interpretation the string name of the interpretation
	 * @return a boolean if it has such an interpretation or not
	 */
	public boolean hasInterpretation(String interpretation){
		for(SrlInterpretation i : m_interpretations){
			if(i.getInterpretation().toLowerCase().equals(interpretation.toLowerCase())){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @param interpretations the interpretations to set
	 */
	public void setInterpretations(ArrayList<SrlInterpretation> interpretations) {
		m_interpretations = interpretations;
	}
	

	private ArrayList<SrlInterpretation> m_interpretations = new ArrayList<SrlInterpretation>();
	
	
}