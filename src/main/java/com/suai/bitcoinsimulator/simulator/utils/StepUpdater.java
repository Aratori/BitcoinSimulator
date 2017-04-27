package com.suai.bitcoinsimulator.simulator.utils;

import java.util.TimerTask;
import com.suai.bitcoinsimulator.simulator.BitcoinSimulator;


/**
*Update model and gui
*/
public class StepUpdater extends TimerTask {
 	private static BitcoinSimulator bs;

 	public static  void setBitcoinSimulator(BitcoinSimulator bss)
 	{
 		bs = bss;
 	}

    @Override
    public void run() {
		bs.nextStep();

 		//bs.getGUI().nextStep();

    }
}    
