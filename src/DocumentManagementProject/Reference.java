package DocumentManagementProject;

public abstract class Reference {
    protected int referenceId;

    /**
     * the reference class is abstract so other classes inherent its constructor and getter method
     */
    protected Reference(){
        this.referenceId = referenceId;
    }

   protected abstract int getReferenceID();
}
