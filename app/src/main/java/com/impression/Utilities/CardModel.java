package com.impression.Utilities;

import java.io.Serializable;

/**
 * Created by ayushgulati on 6/12/16.
 */
public class CardModel implements Serializable{


    public static final long serialVersionUID = 1L ;

    public int id ;
    public String email ;
    public String templateId ;
    public String CardName ;
    public String Json ;

    public CardModel(String email ,int id , String templateId , String Cd ,String json)
    {
        this.id = id ;
        this.templateId = templateId;
        CardName = Cd ;
        this.Json = json ;
    }
}
