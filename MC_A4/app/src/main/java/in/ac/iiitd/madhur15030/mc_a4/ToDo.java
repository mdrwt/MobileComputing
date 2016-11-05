package in.ac.iiitd.madhur15030.mc_a4;

/**
 * Created by Madhur on 03/11/16.
 */

public class ToDo {
    public String title;
    public String detail;

    public ToDo(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
