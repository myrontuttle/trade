package com.myrontuttle.fin.trade.runner;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.adapt.StrategyDAO;

public class Activator implements BundleActivator {

    private StrategyDAO strategyDAO;
    
    public void setStrategyDAO(StrategyDAO sdao) {
    	this.strategyDAO = sdao;
    }
    
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Trade Runner");
		if (strategyDAO == null) {
			System.out.println("No StrategyDAO");
			return;
		}
		List<Group> existingGroups = strategyDAO.findGroups();
		if (existingGroups != null && existingGroups.size() > 0) {
			System.out.println("Groups (id - start date): ");
			for (Group group : existingGroups) {
				System.out.println(group.getGroupId() + " - " + group.getStartTime().toString());
			}
		} else {
			System.out.println("No existing groups. Creating new group");
			Group group = strategyDAO.newGroupRecord();
			group.setAlertAddress("wsodinvestor@gmail.com");
			group.setActive(true);
			group.setAlertsPerSymbol(2);
			group.setEliteCount(3);
			group.setSize(15);
			group.setEvaluationStrategy(Group.RANDOM_EVALUATOR);
			group.setExpressionStrategy(Group.NO_EXPRESSION);
			group.setFrequency(Group.DAILY);
			group.setNumberOfScreens(1);
			group.setMaxSymbolsPerScreen(5);
			group.setMutationFactor(.05);
			strategyDAO.saveGroup(group);
			System.out.println("Group " + group.getGroupId() + ", created on: " + 
								group.getStartTime().toString());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping Trade Runner");

		if (strategyDAO == null) {
			System.out.println("No StrategyDAO");
			return;
		}

		List<Group> existingGroups = strategyDAO.findGroups();
		if (existingGroups != null && existingGroups.size() > 0) {
			System.out.println("Groups (id - start date): ");
			for (Group group : existingGroups) {
				System.out.println(group.getGroupId() + " - " + group.getStartTime().toString());
			}
		} else {
			System.out.println("No existing groups.");
		}
	}

}
