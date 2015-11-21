package com.terrier.boxcryptor.objects;

public class BCInventoryItemView {

	
	
	private String nomClair;
	
	private String nomChiffre;
	
	private boolean viewClair = true;
	
	public BCInventoryItemView(String nomClair, String nomChiffre){
		this.nomChiffre = nomChiffre;
		this.nomClair = nomClair;
	}
	
	
	public void alternateMessageView(){
		this.viewClair = !this.viewClair;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.viewClair ? this.nomClair : this.nomChiffre;
	}
}
