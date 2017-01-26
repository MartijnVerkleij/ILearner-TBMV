package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

public class Document {
	String category;
	String contents;

	public Document(String category, String contents) {
		this.category = category;
		this.contents = contents;
	}

	public Document(String contents) {
		this.category = null;
		this.contents = contents;
	}
	
	public String getContents() {
		return contents;
	}

	public String[] getContentsAsArray() {
		return contents.split(" |.|,|?|!");
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
}
