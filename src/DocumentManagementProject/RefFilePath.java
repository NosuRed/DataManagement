package DocumentManagementProject;

public class RefFilePath extends Reference {

    private String filePathStr, fileNameStr;

    /**
     * @param referenceID the id of the reference
     * @param filePathStr is the filePath of a file
     * @param fileNameStr is the name of a file
     */
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
