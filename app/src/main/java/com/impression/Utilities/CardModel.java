package com.impression.Utilities;

/**
 * Created by ayushgulati on 6/12/16.
 */
public class CardModel {


    public int id ;
    public String templateId ;
    public String CardName ;
    public String Json ;

    public CardModel(int id , String templateId , String Cd ,String json)
    {
        this.id = id ;
        this.templateId = templateId;
        CardName = Cd ;
        this.Json = json ;
    }
}
