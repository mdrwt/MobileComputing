package in.ac.iiitd.mt14033.passwordmanager.model;

/**
 * Created by Madhur on 04/06/16.
 */
public class DLOption {
    private String optionTitle;
    private int optionImageResId;

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public int getOptionImageResId() {
        return optionImageResId;
    }

    public void setOptionImageResId(int optionImage) {
        this.optionImageResId = optionImage;
    }

    public DLOption(String optionTitle, int optionImageResId) {
        this.optionTitle = optionTitle;
        this.optionImageResId = optionImageResId;
    }
}
