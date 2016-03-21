package model;

/**
 * Created by kevinftd on 16/3/18.
 */
public class Card {

    private int id;

    private String chName;
    private String enName;
    private String occupation;
    private String rarity;
    private String type;
    private int manaCost;
    private int health;
    private int attack;
    private String special;
    private String desc;

    public Card(CardBuilder builder){
        this.chName = builder.chName;
        this.enName = builder.enName;
        this.occupation = builder.occupation;
        this.rarity = builder.rarity;
        this.type = builder.type;
        this.manaCost = builder.manaCost;
        this.health = builder.health;
        this.attack = builder.attack;
        this.special = builder.special;
        this.desc = builder.desc;
    }

    public static class CardBuilder {
        private String chName;
        private String enName;
        private String occupation;
        private String rarity;
        private String type;
        private int manaCost;
        private int health;
        private int attack;
        private String special;
        private String desc;

        public Card build(){
            return new Card(this);
        }

        public CardBuilder chname(String chName) {
            this.chName = chName;
            return this;
        }

        public CardBuilder enname(String enName) {
            this.enName = enName;
            return this;
        }

        public CardBuilder occupation(String occupation) {
            this.occupation = occupation;
            return this;
        }

        public CardBuilder rarity(String rarity) {
            this.rarity = rarity;
            return this;
        }

        public CardBuilder type(String type) {
            this.type = type;
            return this;
        }

        public CardBuilder manaCost(int manaCost) {
            this.manaCost = manaCost;
            return this;
        }

        public CardBuilder health(int health) {
            this.health = health;
            return this;
        }

        public CardBuilder attack(int attack) {
            this.attack = attack;
            return this;
        }

        public CardBuilder special(String special) {
            this.special = special;
            return this;
        }

        public CardBuilder desc(String desc) {
            this.desc = desc;
            return this;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
