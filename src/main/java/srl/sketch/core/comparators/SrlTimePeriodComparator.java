package srl.sketch.core.comparators;

import java.util.Comparator;

import srl.sketch.core.ISrlTimePeriod;

public class SrlTimePeriodComparator implements Comparator<ISrlTimePeriod>{

	@Override
	public int compare(ISrlTimePeriod first, ISrlTimePeriod second) {

		if(first.getTimeStart() < second.getTimeStart())
			return -1;
		else if(first.getTimeStart() > second.getTimeStart())
			return 1;
		else
			return 0;
	}
}
