package DocumentManagementProject;

public class RefArchive extends Reference {
    private String archShed, archRack, archFolder;

    /**
     * a Archive object is created
     * @param referenceID is the id given to the reference
     * @param archShed is the shed name
     * @param archRack is the name of the rack
     * @param archFolder is the name of the folder
     */
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
