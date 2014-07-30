package com.alastar.game.enums;

public class TypeId
{
    
 public static int getTypeId(Type t)
 {
     switch(t){
        case Entity:
            return 1;
        case Item:
            return 3;
        case Projectile:
            return 2;
        case Tile:
            return 0;
        default:
            return 0;
         
     }
 }
 
}
