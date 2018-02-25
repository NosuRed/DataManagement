package sample;

public class RefFilePath extends Reference {

    String filePathStr, fileNameStr;

    protected RefFilePath(int referenceID, String filePathStr, String fileNameStr){
        super.referenceId = referenceID;
        this.filePathStr = filePathStr;
        this.fileNameStr = fileNameStr;
    }

    @Override
    protected int getReferenceID() {
        return super.referenceId;
    }

    public String getFilePathStr() {
        return filePathStr;
    }

    public String getFileNameStr() {
        return fileNameStr;
    }
}
