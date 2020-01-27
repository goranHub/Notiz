package com.example.notiz;

class Notiz {

    private String note1;
	private String note2;
    private boolean checked;
    private long id;

    public Notiz(long id, String note1, String note2, boolean checked) {
        this.note1 = note1;
	    this.note2 = note2;
        this.checked = checked;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getNote1() {
        return note1;
    }
	public String getNote2() {
		return note2;
	}

    public void setNote(String note) {
        this.note1 = note1;
    }




    public boolean setisChecked() {

        return !checked;
    }

    public boolean isChecked() {

        return checked;
    }
}

