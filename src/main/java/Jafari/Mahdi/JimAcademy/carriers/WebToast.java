package Jafari.Mahdi.JimAcademy.carriers;

public class WebToast {
    private Long toastType;
    private String title;
    private String information;

    public WebToast(Long toastType,String title) {
        this.toastType = toastType;
        this.title = title;
    }


    public Long getToastType() {
        return toastType;
    }

    public void setToastType(Long toastType) {
        this.toastType = toastType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public enum ToastType{
        CONFIRMATION_TYPE(1L),
        INFO_TYPE(1L),
        ALERT_TYPE(2L),
        ERROR_TYPE(3L);

        private Long value;

        ToastType(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return value;
        }
    }
}
