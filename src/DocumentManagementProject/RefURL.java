package DocumentManagementProject;

public class RefURL extends Reference {
    private String urlStr;

    /**
     * creates a URL
     * @param referenceId is the id for this reference
     * @param urlStr
     */
    protected RefURL(int referenceId, String urlStr){
        super.referenceId = referenceId;
        this.urlStr = urlStr;
    }

    public String getUrlStr() {
        return urlStr;
    }

    @Override
    protected int getReferenceID() {
        return super.referenceId;
    }
}

