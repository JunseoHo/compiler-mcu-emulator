package generator;

public enum DataType {

    SHORT("short", 2),

    INT("int", 4),

    LONG("long", 8);

    private final String name;
    private final int size;

    DataType(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
