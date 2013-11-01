package com.myrontuttle.fin.trade.runner;

import java.util.List;

import org.osgi.framework.ServiceReference;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.adapt.StrategyDAO;

public class Activator {

    private StrategyDAO strategyDAO;
    
    public void setStrategyDAO(StrategyDAO sdao) {
    	System.out.println("Setting StrategyDAO");
    	this.strategyDAO = sdao;
    }

    public void bind(ServiceReference<?> reference) {
        System.out.println("Activator, service bound: " + reference);
        
        if (strategyDAO == null) {
			System.out.println("No StrategyDAO");
			return;
		} else {
			System.out.println("Found StrategyDAO");
		}
		
		try {
			System.out.println("Finding Simple Groups");
			List<Group> existingGroups = strategyDAO.findGroups();
			if (existingGroups != null && existingGroups.size() > 0) {
				System.out.println("Groups (id): ");
				for (Group group : existingGroups) {
					System.out.println(group.getGroupId());
				}
			} else {
				System.out.println("No existing groups. Creating new group");
				Group group = strategyDAO.newGroupRecord();
				/*
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
				*/
				strategyDAO.saveGroup(group);
				System.out.println("Group " + group.getGroupId() + ", created on: " + 
									group.getStartTime().toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
    }

    public void unbind(ServiceReference<?> reference) {
        System.out.println("Activator, service unbound: " + reference);

        try {
    		List<Group> existingGroups = strategyDAO.findGroups();
    		if (existingGroups != null && existingGroups.size() > 0) {
    			System.out.println("Groups (id - start date): ");
    			for (Group group : existingGroups) {
    				System.out.println(group.getGroupId() + " - " + group.getStartTime().toString());
    			}
    		} else {
    			System.out.println("No existing groups.");
    		}
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    }

}
