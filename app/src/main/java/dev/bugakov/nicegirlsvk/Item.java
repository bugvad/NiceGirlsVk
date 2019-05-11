package dev.bugakov.nicegirlsvk;

public class Item {

    public String name;

    public Item(String name){

        this.name=name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

class ItemQuestion {
    //public long question_id;
    public String title;

    public ItemQuestion(String title){

        this.title=title;
    }
}