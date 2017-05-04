package com.suai.bitcoinsimulator.simulator.utils;

import java.util.TimerTask;
import com.suai.bitcoinsimulator.simulator.Simulator;


/**
*Update model and gui
*/
public class StepUpdater extends TimerTask {
 	private static Simulator bs;

 	public static  void setBitcoinSimulator(Simulator bss)
 	{
 		bs = bss;
 	}

    @Override
    public void run() {
		bs.nextStep();

 		//bs.getGUI().nextStep();

    }
}    
