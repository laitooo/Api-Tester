package zxc.laitooo.apitester;

/**
 * Created by Laitooo San on 29/05/2019.
 */

public class Param {

    String key;
    String value;

    public Param(String value, String key) {
        this.value = value;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
