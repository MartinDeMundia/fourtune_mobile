package com.coreictconsultancy.fourtune.pojo;
import java.io.Serializable;

public class Res_Event_Images implements Serializable {
    String id, image_path, parent_id, image_type,updated_at;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getImagepath() {
        return image_path;
    }
    public void setImagepath(String image_path) {
        this.image_path = image_path;
    }

    public String getParentid() {
        return parent_id;
    }
    public void setParentid(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getImagetype() {
        return image_type;
    }
    public void setImagetype(String image_type) {
        this.image_type = image_type;
    }
    public String getUpdatedat() {
        return updated_at;
    }
    public void setUpdatedat(String updated_at) {
        this.updated_at = updated_at;
    }

}


