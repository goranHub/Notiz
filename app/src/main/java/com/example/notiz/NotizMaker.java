package com.example.notiz;

import java.util.ArrayList;
import java.util.List;

public class NotizMaker {


    List<Notiz> notizMakerList = new ArrayList<>();


    public NotizMaker(){
        maker();
    }

    public List<Notiz> maker(){

        Notiz neue1 = new Notiz(1, "note1", "1", false);
        Notiz neue2 = new Notiz(2, "note2", "2", false);
        Notiz neue3 = new Notiz(3, "note3", "3", false);
        Notiz neue4 = new Notiz(4, "note4", "4", false);

        notizMakerList.add(neue1);
        notizMakerList.add(neue2);
        notizMakerList.add(neue3);
        notizMakerList.add(neue4);
        return notizMakerList;
    }


}
