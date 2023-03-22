package com.example.sbb2;

//RuntimeException 상속시 에러 발생하면 프로그램이 자동종료됨
public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String msg){
        super(msg);
    }
}
