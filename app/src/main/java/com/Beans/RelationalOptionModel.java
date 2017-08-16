package com.Beans;

import java.util.List;

public class RelationalOptionModel implements Comparable<RelationalOptionModel>{
	
	private OptionModel optionModel;
	private List<SubOptionModel> listOfSubOptionModel;
	
	public RelationalOptionModel(OptionModel optionModel,
			List<SubOptionModel> listOfSubOptionModel) {
		super();
		this.optionModel = optionModel;
		this.listOfSubOptionModel = listOfSubOptionModel;
	}

	public OptionModel getOptionModel() {
		return optionModel;
	}

	public void setOptionModel(OptionModel optionModel) {
		this.optionModel = optionModel;
	}

	public List<SubOptionModel> getListOfSubOptionModel() {
		return listOfSubOptionModel;
	}

	public void setListOfSubOptionModel(List<SubOptionModel> listOfSubOptionModel) {
		this.listOfSubOptionModel = listOfSubOptionModel;
	}

	@Override
	public int compareTo(RelationalOptionModel another) {		
		return Integer.parseInt(this.getOptionModel().getOptionSortOrder()) - Integer.parseInt(another.getOptionModel().getOptionSortOrder());
	}
	
}
