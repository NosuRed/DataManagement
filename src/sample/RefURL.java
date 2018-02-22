package sample;

public class RefURL extends Reference {
    private String urlStr;

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

