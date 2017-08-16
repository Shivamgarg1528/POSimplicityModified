package com.Beans;

import java.util.ArrayList;
import java.util.List;

public class CheckOutParentModel {


	private ProductModel product;
	private ArrayList<RelationalOptionModel> childs;	
	private ExtraProductArgument extraArgument;

	public CheckOutParentModel(ProductModel product,
			ArrayList<RelationalOptionModel> list, ExtraProductArgument extraArgument) {
		super();
		this.product = product;
		this.childs  = list;
		this.extraArgument = extraArgument;
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}


	public ArrayList<RelationalOptionModel> getChilds() {
		return childs;
	}

	public void setChilds(ArrayList<RelationalOptionModel> childs) {
		this.childs = childs;
	}

	public ExtraProductArgument getExtraArgument() {
		return extraArgument;
	}

	public void setExtraArgument(ExtraProductArgument extraArgument) {
		this.extraArgument = extraArgument;
	}

	@Override
	public int hashCode() {
		final int prime = 11;
		int result      = 1;
		for(int index = childs.size()-1 ; index >= 0 ; index --){			
			RelationalOptionModel relationalOptionModel = childs.get(index);
			OptionModel             optionModel         = relationalOptionModel.getOptionModel();
			List<SubOptionModel>    suboptionModelList  = relationalOptionModel.getListOfSubOptionModel();

			for(int value = suboptionModelList.size() - 1 ; value >= 0 ; value -- ){
				result = prime * result + Integer.parseInt(suboptionModelList.get(value).getSubOptionId());
			}			
			result = prime * result + Integer.parseInt(optionModel.getOptionId());
		}
		result = prime * result + Integer.parseInt(product.getProductId());
		return result;
	}

	@Override
	public boolean equals(Object v) {

		if (v instanceof CheckOutParentModel) {

			// object need to be mached
			CheckOutParentModel rhsParent                    = (CheckOutParentModel) v;
			ProductModel rhsProductModel                     = rhsParent.getProduct();
			List<RelationalOptionModel> rhsChilds     	     = rhsParent.getChilds();

			// each object for the v object will be matched 
			ProductModel lhsProductModel            		 = product;
			List<RelationalOptionModel> lhsChilds		     = childs;
			

			List<Integer> rhsChildIds = new ArrayList<Integer>();
			for(int index = rhsChilds.size()-1 ; index >= 0 ; index --){
				RelationalOptionModel relationalOptionModel = rhsChilds.get(index);
				List<SubOptionModel> liSubOptionModels      = relationalOptionModel.getListOfSubOptionModel();

				for(int value = liSubOptionModels.size() - 1 ; value >= 0 ;value -- ){
					rhsChildIds.add(Integer.parseInt(liSubOptionModels.get(value).getSubOptionId()));
				}				
			}

			List<Integer> lhsChildIds = new ArrayList<Integer>();
			for(int index = lhsChilds.size()-1 ; index >= 0 ; index --){
				RelationalOptionModel relationalOptionModel = lhsChilds.get(index);
				List<SubOptionModel> liSubOptionModels      = relationalOptionModel.getListOfSubOptionModel();

				for(int value = liSubOptionModels.size() - 1 ; value >= 0 ;value -- ){
					lhsChildIds.add(Integer.parseInt(liSubOptionModels.get(value).getSubOptionId()));
				}				
			}
			
			if(lhsChildIds.size() == rhsChildIds.size()){

				boolean element = rhsProductModel.getProductId().equalsIgnoreCase(lhsProductModel.getProductId()) && lhsChildIds.containsAll(rhsChildIds) && rhsProductModel.getProductPrice().equalsIgnoreCase(lhsProductModel.getProductPrice());
				return element;
			}
			else{				
				return false;
			}
		}
		return false;
	}
}

