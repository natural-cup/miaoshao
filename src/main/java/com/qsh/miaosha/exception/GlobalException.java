package com.qsh.miaosha.exception;

import com.qsh.miaosha.result.CodeMsg;

public class GlobalException extends RuntimeException{

    static final long serialVersionUID =1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm=cm;
    }

    public CodeMsg getCm() {
        return cm;
    }


}
