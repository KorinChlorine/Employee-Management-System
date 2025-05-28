package jobs;

public class Librarian implements Job {
    private int booksEnchanted;

    public Librarian() {
        this.booksEnchanted = 7;
    }

    public int getBooksEnchanted() { return booksEnchanted; }

    public void setBooksEnchanted(int count) {
        this.booksEnchanted = count;
    }

    public String enchantBook() {
        return "Enchants book #" + ++booksEnchanted;
    }
    @Override
    public String getName() { return "Librarian"; }
    @Override
    public String getSkill() { return "Book enchanting and knowledge."; }
}
