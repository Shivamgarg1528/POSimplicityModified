package com.Beans;

public class CommentModel implements Comparable<CommentModel>{
	private int commentId;
	private String commentString;
	private boolean isCommentSelected;


	public CommentModel( String commentString,
			boolean isCommentSelected) {
		super();
		this.commentString = commentString;
		this.isCommentSelected = isCommentSelected;
	}


	public CommentModel() {}


	public int getCommentId() {
		return commentId;
	}


	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}


	public String getCommentString() {
		return commentString;
	}


	public void setCommentString(String commentString) {
		this.commentString = commentString;
	}


	public boolean isCommentSelected() {
		return isCommentSelected;
	}


	public void setCommentSelected(boolean isCommentSelected) {
		this.isCommentSelected = isCommentSelected;
	}

	@Override
	public int compareTo(CommentModel another) {
		return commentId - another.getCommentId();
	}

}
