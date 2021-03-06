package restful.Enity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "property")
public class Property {
    @Id
    private long server_uuid;
    @Field("content")
    private String content;
    @Field("createDate")
    private String createDate;
    @Field("updateDate")
    private String updateDate;


    public Property() {
        this.createDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.updateDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public Property (long server_uuid, String content) {
        this.server_uuid = server_uuid;
        this.content = content;
        this.createDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.updateDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public long getServer_uuid() {
        return server_uuid;
    }

    public void setServer_uuid(long server_uuid) {
        this.server_uuid = server_uuid;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
        this.updateDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public String getCreateDate() {
        return createDate;
    }
}
