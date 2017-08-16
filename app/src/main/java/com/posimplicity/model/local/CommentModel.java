package com.posimplicity.model.local;

import com.easylibs.sqlite.IModel;


public class CommentModel implements IModel {

    private String commentString;
    private String commentType;
    private boolean isCommentSelected;

    public CommentModel(String commentString, String pCommentType) {

        this.commentString = commentString;
        commentType = pCommentType;
    }

    public CommentModel() {
    }

    @Override
    public long getRowId() {
        return 0;
    }

    @Override
    public void setRowId(long pRowId) {

    }

    public String getCommentString() {
        return commentString;
    }

    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }


    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public boolean isCommentSelected() {
        return isCommentSelected;
    }

    public void setCommentSelected(boolean commentSelected) {
        isCommentSelected = commentSelected;
    }
}
