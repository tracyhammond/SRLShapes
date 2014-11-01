/*******************************************************************************
 *  Revision History:<br>
 *  SRL Member - File created
 *
 *  <p>
 *  <pre>
 *  This work is released under the BSD License:
 *  (C) 2012 Sketch Recognition Lab, Texas A&M University (hereafter SRL @ TAMU)
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Sketch Recognition Lab, Texas A&M University 
 *        nor the names of its contributors may be used to endorse or promote 
 *        products derived from this software without specific prior written 
 *        permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY SRL @ TAMU ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL SRL @ TAMU BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  </pre>
 *  
 *******************************************************************************/
package srl.sketch.controller;

import java.util.LinkedList;
import java.util.List;

import srl.sketch.core.AbstractSrlComponent;
import srl.sketch.core.SrlSketch;
import srl.sketch.core.SrlStroke;
import srl.sketch.controller.event.SketchModifiedListener;
import srl.sketch.controller.event.StrokeAddedListener;
public class SketchController {
	private SrlSketch sketch;

	/*
	 * Listeners
	 */
	private List<StrokeAddedListener> strokeAddedListeners = new LinkedList<StrokeAddedListener>();
	private List<SketchModifiedListener> sketchModifiedListeners = new LinkedList<SketchModifiedListener>();

	public SketchController(){
		this(new SrlSketch());
	}
	public SketchController(SrlSketch sketch){
		setSketch(sketch);
	}
	public void setSketch(SrlSketch sketch){
		this.sketch = sketch;
		onSketchModified();
	}
	public synchronized void addComponent(AbstractSrlComponent component){
		if(component != null){
			this.sketch.add(component);
			if(component instanceof SrlStroke){
				for(StrokeAddedListener listener:strokeAddedListeners){
					listener.onStrokeAdded((SrlStroke)component);
				}
			}
			onSketchModified();
		}
	}
	public synchronized boolean removeComponent(AbstractSrlComponent component){
		if(component!=null){
			if(this.sketch.remove(component)){
				return true;
			}
		}
		return false;
	}

	protected void onSketchModified(){
		for(SketchModifiedListener listener:sketchModifiedListeners){
			listener.onSketchModified(sketch);
		}
	}

	public synchronized void clearSketch(){
		sketch.clear();
		onSketchModified();
	}

	public SrlSketch getSketch(){
		return sketch;
	}


	/*
	 * Listener controls
	 */
	public synchronized  void addSketchModifiedListener(SketchModifiedListener listener){
		sketchModifiedListeners.add(listener);
	}
	public synchronized  void removeSketchModifiedListener(SketchModifiedListener listener){
		sketchModifiedListeners.remove(listener);
	}

	public synchronized  void addStrokeAddedListener(StrokeAddedListener listener){
		strokeAddedListeners.add(listener);
	}
	public synchronized  void removeStrokeAdddedListener(StrokeAddedListener listener){
		strokeAddedListeners.remove(listener);
	}
}
