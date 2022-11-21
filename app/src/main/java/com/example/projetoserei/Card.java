package com.example.projetoserei;

public class Card {

    private String Name;
    private String Description;
    private String Category;
    private boolean PublicCard;
    private int QuestionQuantity;
    private String UserId;

    public Card() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public boolean isPublicCard() {
        return PublicCard;
    }

    public void setPublicCard(boolean publicCard) {
        PublicCard = publicCard;
    }

    public int getQuestionQuantity() {
        return QuestionQuantity;
    }

    public void setQuestionQuantity(int questionQuantity) {
        QuestionQuantity = questionQuantity;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
