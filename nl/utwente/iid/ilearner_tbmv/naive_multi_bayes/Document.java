package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

public class Document {
	private Category category;
	private String contents;

	public Document(Category category, String contents) {
		this.category = category;
		this.contents = contents;
	}

	public Document(String contents) {
		this.category = null;
		this.contents = contents;
	}

	public String[] getContentsAsArray() {
		return Utils.split(contents);
	}

	public String[] getStrippedContentsAsArray() {
		return Utils.splitStripped(contents);
	}

	public String getContents() {
		return contents;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
