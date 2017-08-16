package com.Bluetooths;

/**
 * Created by shivam.garg on 1/21/2017.
 */

public class FallbackException extends Exception {

    private static final long serialVersionUID = 1L;

    public FallbackException(Exception e) {
        super(e);
    }
}
