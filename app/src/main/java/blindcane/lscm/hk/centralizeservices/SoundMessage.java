package blindcane.lscm.hk.centralizeservices;

import java.io.Serializable;

/**
 * Created by tauyeung on 4/5/2017.
 */

public class SoundMessage implements Serializable {
    private String content, url , fileName;
    private boolean urgent, isMP3, isFileExist;
    private int priority;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isFileExist() {
        return isFileExist;
    }

    public void setFileExist(boolean fileExist) {
        isFileExist = fileExist;
    }

    public SoundMessage(String content, String url , String fileName, boolean urgent, boolean isMP3 , boolean isFileExist, int priority) {
        this.content = content;
        this.url = url;
        this.fileName = fileName;
        this.urgent = urgent;
        this.isMP3 = isMP3;
        this.isFileExist = isFileExist;
        this.priority = priority;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public boolean isMP3() {
        return isMP3;
    }

    public void setMP3(boolean MP3) {
        isMP3 = MP3;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
