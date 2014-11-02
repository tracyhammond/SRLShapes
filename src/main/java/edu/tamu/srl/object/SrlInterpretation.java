package edu.tamu.srl.object;

/**
 * Interpretation data class, holds confidence and complexity
 * @author hammond
 * @copyright Tracy Hammond, Sketch Recognition Lab, Texas A&M University
 */
public class SrlInterpretation implements Comparable<SrlInterpretation>{

	/**
	 * A string description of the interpretation
	 */
	private String mInterpretation;
	
	/**
	 * A numerical measurement of confidence of an interpretation
	 */
	private double mConfidence;

	/**
	 * A numerical measurement of complexity of an interpretation
	 * (Obviously more complex interpretations are preferred.)
	 */
	private int mComplexity;
	
	/**
	 * A string holding information about this interpretation
	 */
	private String mNote;
	
	/**
	 * @return the note
	 */
	public String getNote() {
		return mNote;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		mNote = note;
	}

	/**
	 * Constructor that set the three values
	 * @param interpretation string name describing the interpretation
	 * @param confidence double representing confidence
	 * @param complexity double representing complexity
	 */
	public SrlInterpretation(String interpretation, double confidence,
			int complexity) {
		mInterpretation = interpretation;
		mConfidence = confidence;
		mComplexity = complexity;
	}

	/**
	 * This constructor should be used instead of the clone method
	 * @param i
	 */
	public SrlInterpretation(SrlInterpretation i){
		this.mComplexity = i.mComplexity;
		this.mConfidence = i.mConfidence;
		this.mInterpretation = i.mInterpretation;
		this.mNote = i.mNote;
		
	}
	
	@Override
	/**
	 * Compares two interpretations. 
	 * It prefers interpretations of high complexity
	 * If the complexities are equal, it prefers interpretations of higher confidence
	 */
	public int compareTo(SrlInterpretation other) {
		if(Double.compare(mComplexity, other.getComplexity()) == 0){
			return -1 * Double.compare(mConfidence, other.getConfidence());
		}
		return -1 * Double.compare(mComplexity, other.getComplexity());
	}

	/**
	 * get the interpretation of an object
	 * @return a string name for the interpretation
	 */
	public String getInterpretation() {
		return mInterpretation;
	}

	/**
	 * set the interpretation of an object
	 * @param interpretation a string name of the interpretation
	 */
	public void setInterpretation(String interpretation) {
		mInterpretation = interpretation;
	}

	/**
	 * Get the complexity of an interpretation
	 * @return a number representing the complexity of an interpretation
	 */
	public int getComplexity() {
		return mComplexity;
	}

	/**
	 * Set the complexity of an interpretation
	 * @param complexity a number representing the complexity of an interpretation
	 */
	public void setComplexity(int complexity) {
		mComplexity = complexity;
	}

	/**
	 * Get the confidence of an interpretation
	 * @return a number representing the confidence of an interpretation
	 */
	public double getConfidence() {
		return mConfidence;
	}

	/**
	 * Set the confidence of an interpretation
	 * @param confidence a number representing the confidence of an interpretation
	 */
	public void setConfidence(double confidence) {
		mConfidence = confidence;
	}
	
	public String toString(){
		return "Interpretation " + mInterpretation + ": confidence = " + mConfidence + ", complexity = " + mComplexity + ", note: " + mNote;
	}

}
