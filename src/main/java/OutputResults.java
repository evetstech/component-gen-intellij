import java.util.ArrayList;
import java.util.List;

public class OutputResults {
    private boolean IsSuccessful;
    private List<String> Errors;

    public OutputResults() {
        Errors = new ArrayList<>();
        IsSuccessful = true;
    }

    public void setSuccessful(boolean isSuccess) {
        IsSuccessful = isSuccess;
    }

    public void addErrorMessage(String error) {
        Errors.add(error);
    }

    public boolean isSuccessful() {
        return IsSuccessful;
    }

    public List<String> errorMessage() {
        return Errors;
    }
}