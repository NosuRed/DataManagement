package sample;

public class RefArchive extends Reference {
    String archShed, archRack, archFolder;
    //TODO comment
    protected RefArchive(int referenceID, String archShed, String archRack, String archFolder){
        super.referenceId = referenceID;
        this.archShed = archShed;
        this.archRack = archRack;
        this.archFolder = archFolder;
    }

    @Override
    protected int getReferenceID() {
        return super.referenceId;
    }

    public String getArchFolder() {
        return archFolder;
    }

    public String getArchRack() {
        return archRack;
    }

    public String getArchShed() {
        return archShed;
    }
}
