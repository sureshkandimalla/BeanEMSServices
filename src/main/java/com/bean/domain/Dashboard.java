package com.bean.domain;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dashboard {
	
	private long activeProjects;
	private double totalRevenue;
	private double totalCost;
	
	
	@Override
	public String toString() {
		return "Dashboard [activeProjects=" + activeProjects + ", totalRevenue=" + totalRevenue + ", totalCost="
				+ totalCost + "]";
	}
	

}
