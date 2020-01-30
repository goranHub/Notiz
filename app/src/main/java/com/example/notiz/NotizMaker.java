package com.example.notiz;

import java.util.ArrayList;
import java.util.List;

public class NotizMaker {


	List<Notiz> notizMakerList = new ArrayList<>();


	public List<Notiz> maker() {

		Notiz neue1 = new Notiz(1, "beispielData1", "1", false);
		Notiz neue2 = new Notiz(2, "beispielData2", "2", false);
		Notiz neue3 = new Notiz(3, "beispielData3", "3", false);
		Notiz neue4 = new Notiz(4, "beispielData4", "4", false);

		notizMakerList.add(neue1);
		notizMakerList.add(neue2);
		notizMakerList.add(neue3);
		notizMakerList.add(neue4);

			return notizMakerList;
		}

	}



